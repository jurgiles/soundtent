package com.momus.SoundTent.factories;

import android.media.MediaRecorder;
import android.os.Handler;
import android.widget.TextView;
import com.google.inject.assistedinject.Assisted;

public interface RunnableCaptorFactory {
    Runnable createMediaRecorderCaptor(@Assisted MediaRecorder mediaRecorder, @Assisted TextView textView, @Assisted Handler handler);
}
