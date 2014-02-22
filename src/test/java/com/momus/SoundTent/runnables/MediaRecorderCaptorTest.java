package com.momus.SoundTent.runnables;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

@RunWith(RobolectricTestRunner.class)
public class MediaRecorderCaptorTest {
    public static final int AMP_VALUE = 1100;
    public static final int HIGHER_AMP_VALUE = 5000;
    public static final int MAX_AMP_READING = 32767;
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
    public void shouldSetColorOfBackgroundRelativeToAmplitudeWhenHigherThanLastReading() {
        when(mediaRecorder.getMaxAmplitude()).thenReturn(AMP_VALUE).thenReturn(HIGHER_AMP_VALUE);

        MediaRecorderCaptor mediaRecorderCaptor = new MediaRecorderCaptor(mediaRecorder, view, handler);
        mediaRecorderCaptor.run();
        mediaRecorderCaptor.run();

        int expectedColor = Color.rgb(HIGHER_AMP_VALUE / MediaRecorderCaptor.AMP_COLOR_RATIO, 0, 0);

        assertThat(view.getBackground()).isEqualTo(new ColorDrawable(expectedColor));
    }

    @Test
    public void shouldSlowlyDarkenBackgroundColor() {
        when(mediaRecorder.getMaxAmplitude()).thenReturn(HIGHER_AMP_VALUE).thenReturn(AMP_VALUE);

        MediaRecorderCaptor mediaRecorderCaptor = new MediaRecorderCaptor(mediaRecorder, view, handler);
        mediaRecorderCaptor.run();
        mediaRecorderCaptor.run();

        Double expectedAmplitude = HIGHER_AMP_VALUE * MediaRecorderCaptor.FADE_RATE / MediaRecorderCaptor.AMP_COLOR_RATIO;
        int expectedColor = Color.rgb(expectedAmplitude.intValue(), 0, 0);
        assertThat(view.getBackground()).isEqualTo(new ColorDrawable(expectedColor));
    }

    @Test
    public void shouldSetBackgroundColorToMaxIfAmplitudeCreatesColorOver255() {
        when(mediaRecorder.getMaxAmplitude()).thenReturn(MAX_AMP_READING);

        MediaRecorderCaptor mediaRecorderCaptor = new MediaRecorderCaptor(mediaRecorder, view, handler);
        mediaRecorderCaptor.run();

        int expectedColor = Color.rgb(255, 0, 0);
        assertThat(view.getBackground()).isEqualTo(new ColorDrawable(expectedColor));
    }
}
