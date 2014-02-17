package com.momus.SoundTent.threads;

import android.media.MediaRecorder;
import android.os.Handler;
import android.widget.TextView;
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
    public static final int AMP_VALUE = 10;
    @Mock
    private Handler handler;
    @Mock
    private MediaRecorder mediaRecorder;

    private TextView textView = new TextView(Robolectric.application);

    @Before
    public void setup(){
        initMocks(this);
    }

    @Test
    public void shouldScheduleUpdateFromMediaRecorder() {
        MediaRecorderCaptor mediaRecorderCaptor = new MediaRecorderCaptor(mediaRecorder, textView, handler);

        mediaRecorderCaptor.run();

        verify(handler).postDelayed(mediaRecorderCaptor, SoundTentActivity.DELAY_MILLIS);
    }

    @Test
    public void shouldSetTextViewToMaxAmplitude() {
        when(mediaRecorder.getMaxAmplitude()).thenReturn(AMP_VALUE);

        new MediaRecorderCaptor(mediaRecorder, textView, handler).run();

        assertThat(textView.getText()).isEqualTo(String.valueOf(AMP_VALUE));
    }
}
