package com.momus.SoundTent.runnables;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.momus.SoundTent.activities.SoundTentActivity;

public class MediaRecorderCaptor implements Runnable{

    public static final double FADE_RATE = .9;
    private final MediaRecorder mediaRecorder;
    private final View view;
    private final Handler handler;
    private Double maxAmplitude;

    @Inject
    public MediaRecorderCaptor(@Assisted MediaRecorder mediaRecorder, @Assisted View view, @Assisted Handler handler) {
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

        int newColor = Color.rgb(maxAmplitude.intValue() / 98, 0, 0);

        view.setBackgroundColor(newColor);

        handler.postDelayed(this, SoundTentActivity.DELAY_MILLIS);
    }
}
