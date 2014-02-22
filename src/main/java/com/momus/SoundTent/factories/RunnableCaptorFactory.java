package com.momus.SoundTent.factories;

import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;
import com.google.inject.assistedinject.Assisted;
import com.momus.SoundTent.runnables.MediaRecorderCaptor;

public interface RunnableCaptorFactory {
    MediaRecorderCaptor createMediaRecorderCaptor(@Assisted MediaRecorder mediaRecorder, @Assisted View view, @Assisted Handler handler);
}
