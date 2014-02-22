package com.momus.SoundTent.runnables;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.momus.SoundTent.activities.SoundTentActivity;

import static java.lang.Math.min;

public class MediaRecorderViewAdapter implements Runnable{
    public static final double FADE_RATE = .97;
    public static final int MAX_COLOR = 255;
    public static final int AMP_COLOR_RATIO = 90;
    private final MediaRecorder mediaRecorder;
    private final View view;
    private final Handler handler;
    private Double maxAmplitude;

    @Inject
    public MediaRecorderViewAdapter(@Assisted MediaRecorder mediaRecorder, @Assisted View view, @Assisted Handler handler) {
        this.mediaRecorder = mediaRecorder;
        this.view = view;
        this.handler = handler;
        this.maxAmplitude = Double.valueOf(Integer.MIN_VALUE);
    }

    @Override
    public void run(){
        Double amplitude = Double.valueOf(mediaRecorder.getMaxAmplitude());

        if(amplitude > maxAmplitude){
            maxAmplitude = amplitude;
        }else{
            maxAmplitude = maxAmplitude * FADE_RATE;
        }

        int newColor = Color.rgb(min(maxAmplitude.intValue() / AMP_COLOR_RATIO, MAX_COLOR), 0, 0);

        view.setBackgroundColor(newColor);

        handler.postDelayed(this, SoundTentActivity.DELAY_MILLIS);
    }
}
