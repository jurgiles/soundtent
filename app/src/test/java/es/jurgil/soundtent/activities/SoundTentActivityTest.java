package es.jurgil.soundtent.activities;

import android.media.MediaRecorder;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.util.ActivityController;

import java.io.IOException;

import es.jurgil.soundtent.factories.AndroidModelFactory;
import es.jurgil.soundtent.runnables.MediaRecorderViewAdapter;
import static es.jurgil.soundtent.MockInjector.mockInjector;

import static com.googlecode.catchexception.CatchException.verifyException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Robolectric.shadowOf;


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

        activityController = buildActivity(SoundTentActivity.class);
    }

    @Test
    public void shouldNotUseMediaRecorderBeforeOnStart() {
        activityController.create();

        verifyZeroInteractions(mediaRecorder);
    }

    @Test
    public void onCreateShouldSetActivityToKeepScreenAwake() {
        Window activityWindow = activityController.create().get().getWindow();

        boolean keepScreenOnFlag = shadowOf(activityWindow).getFlag(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        assertThat(keepScreenOnFlag).isTrue();
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
    public void onStartShouldCreateTempRecordingFile() throws IOException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        SoundTentActivity activity = activityController.create().start().resume().get();

        verify(mediaRecorder).setOutputFile(captor.capture());

        String tempFile = captor.getValue();
        assertThat(tempFile).contains(activity.getCacheDir().getCanonicalPath());
        assertThat(tempFile).contains("soundtent");
    }

    @Test
    public void onResumeShouldScheduleUpdatesFromMediaRecorderCaptor() {
        activityController.create().start();
        verify(handler, never()).postDelayed(isA(MediaRecorderViewAdapter.class), eq(SoundTentActivity.DELAY_MILLIS));

        activityController.resume();
        verify(handler).postDelayed(isA(MediaRecorderViewAdapter.class), eq(SoundTentActivity.DELAY_MILLIS));
    }

    @Test
    public void onStartShouldThrowExceptionWhenMediaRecorderFailsToPrepare() throws IOException {
        doThrow(new IOException()).when(mediaRecorder).prepare();

        verifyException(activityController.create(), RuntimeException.class).start();
    }

    @Ignore
    public void onStopShouldOnlyCleanUpIfTempFileIsAround() {
        //todo: can we check state of mediarecorder?
        activityController.create().start().resume().pause().stop();

        verify(mediaRecorder, never()).stop();
        verify(mediaRecorder, never()).release();
        verify(mediaRecorder, never()).reset();
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
    public void onStopShouldDeleteTempRecordingFile() {
        SoundTentActivity activity = activityController.create().start().get();

        assertThat(activity.getCacheDir().listFiles().length).isEqualTo(1);

        activityController.pause().stop();

        assertThat(activity.getCacheDir().listFiles().length).isEqualTo(0);
    }

    @Test
    public void onPauseShouldRemoveMediaRecorderCaptorFromHandler() {
        activityController.create().start().resume();
        verify(handler, never()).removeCallbacks(isA(MediaRecorderViewAdapter.class));

        activityController.pause();
        verify(handler).removeCallbacks(isA(MediaRecorderViewAdapter.class));
    }
}
