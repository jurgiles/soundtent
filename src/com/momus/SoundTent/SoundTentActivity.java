package com.momus.SoundTent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SoundTentActivity extends RoboActivity {
    @InjectView(R.id.sound_indicator_background)
    private TextView soundIndicatorBackground;

    @Inject
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        soundIndicatorBackground.setText("it works");

        mediaPlayer.isPlaying();
    }
}
