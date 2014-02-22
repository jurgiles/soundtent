package com.momus.SoundTent.runnables;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.momus.SoundTent.activities.SoundTentActivity;

public class MediaRecorderCaptor implements Runnable{

    private final MediaRecorder mediaRecorder;
    private final View view;
    private final Handler handler;

    @Inject
    public MediaRecorderCaptor(@Assisted MediaRecorder mediaRecorder, @Assisted View view, @Assisted Handler handler) {
        this.mediaRecorder = mediaRecorder;
        this.view = view;
        this.handler = handler;
    }

    @Override
    public void run(){
        Integer maxAmplitude = mediaRecorder.getMaxAmplitude();

        view.setBackgroundColor(Color.argb(255, maxAmplitude/98, 0, 0));

        handler.postDelayed(this, SoundTentActivity.DELAY_MILLIS);
    }
}
