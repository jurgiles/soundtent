package com.momus.SoundTent.runnables;

import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import com.momus.SoundTent.activities.SoundTentActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.application;

@RunWith(RobolectricTestRunner.class)
public class MediaRecorderViewAdapterTest {
    @Mock
    private Handler handler;
    @Mock
    private MediaRecorder mediaRecorder;

    private View view = new View(application);

    @Before
    public void setup(){
        initMocks(this);
    }

    @Test
    public void shouldScheduleUpdateFromMediaRecorder() {
        MediaRecorderViewAdapter mediaRecorderViewAdapter = new MediaRecorderViewAdapter(mediaRecorder, view, handler);

        mediaRecorderViewAdapter.run();

        verify(handler).postDelayed(mediaRecorderViewAdapter, SoundTentActivity.DELAY_MILLIS);
    }
}
