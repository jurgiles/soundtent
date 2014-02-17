package com.momus.SoundTent.activities;

import android.media.MediaRecorder;
import android.os.Handler;
import com.momus.SoundTent.threads.MediaRecorderCaptor;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SoundTentActivityTest {
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
    public void onCreateShouldPrepareAndStartRecorder() throws IOException {
        Robolectric.buildActivity(SoundTentActivity.class).create();

        verify(mediaRecorder).prepare();
        verify(mediaRecorder).start();
    }

    @Test
    public void onCreateShouldSetupAudioRecorder() {
        Robolectric.buildActivity(SoundTentActivity.class).create();

        verify(mediaRecorder).setAudioSource(MediaRecorder.AudioSource.MIC);
        verify(mediaRecorder).setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        verify(mediaRecorder).setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    @Test
    public void onCreateShouldCreateTempFile(){
        Robolectric.buildActivity(SoundTentActivity.class).create();

        verify(mediaRecorder).setOutputFile(anyString());
    }

    @Test
    public void onCreateShouldScheduleUpdatesFromMediaRecorderCaptor() {
        Robolectric.buildActivity(SoundTentActivity.class).create();

        verify(handler).postDelayed(isA(MediaRecorderCaptor.class), eq(SoundTentActivity.DELAY_MILLIS));
    }

    @Test
    public void onCreateShouldThrowExceptionWhenMediaRecorderFailsToPrepare() throws IOException {
        doThrow(new IOException()).when(mediaRecorder).prepare();

        ActivityController<SoundTentActivity> soundTentActivityActivityController = Robolectric.buildActivity(SoundTentActivity.class);

        verifyException(soundTentActivityActivityController, RuntimeException.class).create();
    }
}
