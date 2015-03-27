package es.jurgil.soundtent.factories;

import android.media.MediaRecorder;
import android.os.Handler;
import android.view.View;

import com.google.inject.assistedinject.Assisted;

import es.jurgil.soundtent.runnables.MediaRecorderViewAdapter;

public interface RunnableAdapterFactory {
    MediaRecorderViewAdapter createMediaRecorderCaptor(@Assisted MediaRecorder mediaRecorder, @Assisted View view, @Assisted Handler handler);
}
