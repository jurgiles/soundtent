package com.momus.SoundTent;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.momus.SoundTent.MockInjector.mockInjector;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SoundTentActivityTest {
    @Mock
    private MediaPlayer mediaPlayer;

    private Activity activity;

    @Before
    public void setup() {
        initMocks(this);

        mockInjector().bind(MediaPlayer.class, mediaPlayer).inject();

        activity = Robolectric.buildActivity(SoundTentActivity.class).create().get();
    }

    @Test
    public void onCreateShouldSetTextToItWorks(){
        TextView soundIndicatorBackground = (TextView) activity.findViewById(R.id.sound_indicator_background);

        assertThat(soundIndicatorBackground.getText()).isEqualTo("it works");
    }

    @Test
    public void onCreateShouldCheckIfMediaPlayerIsPlaying() {
        verify(mediaPlayer).isPlaying();
    }
}
