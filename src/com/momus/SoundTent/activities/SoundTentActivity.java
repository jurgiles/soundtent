package com.momus.SoundTent.activities;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.google.inject.Inject;
import com.momus.SoundTent.R;
import com.momus.SoundTent.factories.RunnableCaptorFactory;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.io.File;
import java.io.IOException;

@ContentView(R.layout.main)
public class SoundTentActivity extends RoboActivity {
    public static final String LOG_PRE = "SoundTent";
    public static final long DELAY_MILLIS = 150L;

    @InjectView(R.id.sound_indicator)
    private TextView soundIndicatorView;
    @Inject
    private MediaRecorder mediaRecorder;
    @Inject
    private Handler handler;
    @Inject
    private RunnableCaptorFactory runnableCaptorFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getTempFile());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mediaRecorder.start();

        Runnable mediaRecorderCaptor = runnableCaptorFactory.createMediaRecorderCaptor(mediaRecorder, soundIndicatorView, handler);

        handler.postDelayed(mediaRecorderCaptor, DELAY_MILLIS);
    }

    private String getTempFile() {
        try {
            return File.createTempFile("st-", ".3gpp").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
