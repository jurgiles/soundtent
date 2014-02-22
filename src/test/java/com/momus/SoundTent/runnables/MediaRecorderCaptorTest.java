package com.momus.SoundTent.runnables;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import com.momus.SoundTent.activities.SoundTentActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class MediaRecorderCaptorTest {
    public static final int AMP_VALUE = 1100;
    @Mock
    private Handler handler;
    @Mock
    private MediaRecorder mediaRecorder;

    private View view = new View(Robolectric.application);

    @Before
    public void setup(){
        initMocks(this);
    }

    @Test
    public void shouldScheduleUpdateFromMediaRecorder() {
        MediaRecorderCaptor mediaRecorderCaptor = new MediaRecorderCaptor(mediaRecorder, view, handler);

        mediaRecorderCaptor.run();

        verify(handler).postDelayed(mediaRecorderCaptor, SoundTentActivity.DELAY_MILLIS);
    }

    @Test
    public void shouldSetColorOfBackgroundToTextViewToMaxAmplitude() {
        when(mediaRecorder.getMaxAmplitude()).thenReturn(AMP_VALUE);

        new MediaRecorderCaptor(mediaRecorder, view, handler).run();

        int expectedColor = Color.argb(255, AMP_VALUE/98, 0,0);
        assertThat(shadowOf(view).getBackgroundColor()).isEqualTo(expectedColor);
    }
}
