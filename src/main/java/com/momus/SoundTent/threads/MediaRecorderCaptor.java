package com.momus.SoundTent.threads;

import android.media.MediaRecorder;
import android.os.Handler;
import android.widget.TextView;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.momus.SoundTent.activities.SoundTentActivity;

public class MediaRecorderCaptor implements Runnable{

    private final MediaRecorder mediaRecorder;
    private final TextView textView;
    private final Handler handler;

    @Inject
    public MediaRecorderCaptor(@Assisted MediaRecorder mediaRecorder, @Assisted TextView textView, @Assisted Handler handler) {
        this.mediaRecorder = mediaRecorder;
        this.textView = textView;
        this.handler = handler;
    }

    @Override
    public void run(){
        Integer maxAmplitude = mediaRecorder.getMaxAmplitude();

        textView.setText(String.valueOf(maxAmplitude));

        handler.postDelayed(this, SoundTentActivity.DELAY_MILLIS);
    }
}
