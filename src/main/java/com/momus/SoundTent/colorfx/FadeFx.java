package com.momus.SoundTent.colorfx;

import android.graphics.Color;

public class FadeFx {
    static protected final int BIT_PER_AMPLITUDE = 128;
    static protected final float FADE_RATE = .97f;

    /* todo: change to hsv
     public static final float[] HSV_START_COLOR = new float[]{166.714279f, 0.818713f, 0.670588f};
     public static final int RGB_START_COLOR = Color.rgb(31, 171, 140);
     public static final float VALUE_PER_AMP = MAX_VALUE / 32768.0f;
    */

    private int lastAmplitude = 0;

    public int process(int amplitude) {
        int color;

        if(amplitude > lastAmplitude){
            lastAmplitude = amplitude;
            color = Color.rgb(0, amplitude / BIT_PER_AMPLITUDE, 0);
        }else{
            lastAmplitude = Math.round(lastAmplitude * FADE_RATE);
            color = Color.rgb(0, lastAmplitude / BIT_PER_AMPLITUDE, 0);
        }

        return color;
    }
}
