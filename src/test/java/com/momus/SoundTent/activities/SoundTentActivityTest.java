package com.momus.SoundTent.activities;

import android.media.MediaRecorder;
import android.os.Handler;
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SoundTentActivityTest {
    private final ActivityController<SoundTentActivity> activityController = Robolectric.buildActivity(SoundTentActivity.class);
    @Mock
    private MediaRecorder mediaRecorder;
    @Mock
    private Handler handler;

    @Before
    public void setup() {
        initMocks(this);

        mockInjector()
                .bind(MediaRecorder.class, mediaRecorder)
                .bind(Handler.class, handler)
                .inject();
    }

    @Test
    public void shouldNotUseMediaRecorderBeforeOnStart() {
        activityController.create();

        verifyZeroInteractions(mediaRecorder);
    }

    @Test
    public void onCreateShouldPrepareAndStartRecorder() throws IOException {
        activityController.create().start();

        verify(mediaRecorder).prepare();
        verify(mediaRecorder).start();
    }

    @Test
    public void onCreateShouldSetupAudioRecorder() {
        activityController.create().start();

        verify(mediaRecorder).setAudioSource(MediaRecorder.AudioSource.MIC);
        verify(mediaRecorder).setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        verify(mediaRecorder).setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    @Test
    public void onCreateShouldCreateTempFile(){
        activityController.create().start();

        verify(mediaRecorder).setOutputFile(anyString());
    }

    @Test
    public void onCreateShouldScheduleUpdatesFromMediaRecorderCaptor() {
        activityController.create().start();

        verify(handler).postDelayed(isA(MediaRecorderCaptor.class), eq(SoundTentActivity.DELAY_MILLIS));
    }

    @Test
    public void onCreateShouldThrowExceptionWhenMediaRecorderFailsToPrepare() throws IOException {
        doThrow(new IOException()).when(mediaRecorder).prepare();

        verifyException(activityController.create(), RuntimeException.class).start();
    }

    @Test
    public void onStopShouldStopMediaRecorder() {
        activityController.create().stop();

        verify(mediaRecorder).stop();
    }

    @Test
    public void onStopShouldResetMediaRecorder() {
        activityController.create().start().pause().stop();

        verify(mediaRecorder).reset();
    }

    @Test
    public void onStopShouldReleaseMediaRecorder() {
        activityController.create().start().pause().stop();

        verify(mediaRecorder).release();
    }

    @Test
    public void onStopShouldRemoveMediaRecorderCaptorFromHandler() {
        activityController.create().start().pause();
        verify(handler, never()).removeCallbacks(isA(MediaRecorderCaptor.class));

        activityController.stop();
        verify(handler).removeCallbacks(isA(MediaRecorderCaptor.class));
    }


}
