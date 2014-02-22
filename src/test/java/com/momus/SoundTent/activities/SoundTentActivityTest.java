package com.momus.SoundTent.activities;

import android.media.MediaRecorder;
import android.os.Handler;
import com.momus.SoundTent.factories.AndroidModelFactory;
import com.momus.SoundTent.runnables.MediaRecorderCaptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ActivityController;

import java.io.IOException;

import static com.googlecode.catchexception.CatchException.verifyException;
import static com.momus.SoundTent.MockInjector.mockInjector;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SoundTentActivityTest {
    @Mock
    private MediaRecorder mediaRecorder;
    @Mock
    private Handler handler;
    @Mock
    private AndroidModelFactory modelFactory;

    private ActivityController<SoundTentActivity> activityController;

    @Before
    public void setup() {
        initMocks(this);

        mockInjector()
                .bind(MediaRecorder.class, mediaRecorder)
                .bind(Handler.class, handler)
                .bind(AndroidModelFactory.class, modelFactory)
                .inject();

        when(modelFactory.createMediaRecorder()).thenReturn(mediaRecorder);

        activityController = Robolectric.buildActivity(SoundTentActivity.class);
    }

    @Test
    public void shouldNotUseMediaRecorderBeforeOnStart() {
        activityController.create();

        verifyZeroInteractions(mediaRecorder);
    }

    @Test
    public void onStartShouldPrepareAndStartRecorder() throws IOException {
        activityController.create().start();

        verify(mediaRecorder).prepare();
        verify(mediaRecorder).start();
    }

    @Test
    public void onStartShouldSetupAudioRecorder() {
        activityController.create().start();

        verify(mediaRecorder).setAudioSource(MediaRecorder.AudioSource.MIC);
        verify(mediaRecorder).setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        verify(mediaRecorder).setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    @Test
    public void onStartShouldCreateTempFile(){
        activityController.create().start().resume();

        verify(mediaRecorder).setOutputFile(anyString());
    }

    @Test
    public void onResumeShouldScheduleUpdatesFromMediaRecorderCaptor() {
        activityController.create().start();
        verify(handler, never()).postDelayed(isA(MediaRecorderCaptor.class), eq(SoundTentActivity.DELAY_MILLIS));

        activityController.resume();
        verify(handler).postDelayed(isA(MediaRecorderCaptor.class), eq(SoundTentActivity.DELAY_MILLIS));
    }

    @Test
    public void onStartShouldThrowExceptionWhenMediaRecorderFailsToPrepare() throws IOException {
        doThrow(new IOException()).when(mediaRecorder).prepare();

        verifyException(activityController.create(), RuntimeException.class).start();
    }

    @Test
    public void onStopShouldStopMediaRecorder() {
        activityController.create().start().resume().pause().stop();

        verify(mediaRecorder).stop();
    }

    @Test
    public void onStopShouldResetMediaRecorder() {
        activityController.create().start().resume().pause().stop();

        verify(mediaRecorder).reset();
    }

    @Test
    public void onStopShouldReleaseMediaRecorder() {
        activityController.create().start().resume().pause().stop();

        verify(mediaRecorder).release();
    }

    @Test
    public void onPauseShouldRemoveMediaRecorderCaptorFromHandler() {
        activityController.create().start().resume();
        verify(handler, never()).removeCallbacks(isA(MediaRecorderCaptor.class));

        activityController.pause();
        verify(handler).removeCallbacks(isA(MediaRecorderCaptor.class));
    }
}
