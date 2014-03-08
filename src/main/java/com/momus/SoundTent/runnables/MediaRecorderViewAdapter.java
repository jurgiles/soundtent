package com.momus.SoundTent.runnables;

import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.momus.SoundTent.activities.SoundTentActivity;
import com.momus.SoundTent.colorfx.FadeFx;

public class MediaRecorderViewAdapter implements Runnable{
    private final MediaRecorder mediaRecorder;
    private final View view;
    private final Handler handler;

    /* todo: inject */
    FadeFx fadeFx = new FadeFx();

    @Inject
    public MediaRecorderViewAdapter(@Assisted MediaRecorder mediaRecorder, @Assisted View view, @Assisted Handler handler) {
        this.mediaRecorder = mediaRecorder;
        this.view = view;
        this.handler = handler;
    }

    @Override
    public void run(){
        int newColor = fadeFx.process(mediaRecorder.getMaxAmplitude());

        view.setBackgroundColor(newColor);

        handler.postDelayed(this, SoundTentActivity.DELAY_MILLIS);
    }
}
