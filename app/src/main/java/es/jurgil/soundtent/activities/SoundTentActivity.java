package es.jurgil.soundtent.activities;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.google.inject.Inject;

import java.io.File;
import java.io.IOException;

import es.jurgil.soundtent.R;
import es.jurgil.soundtent.factories.AndroidModelFactory;
import es.jurgil.soundtent.factories.RunnableAdapterFactory;
import es.jurgil.soundtent.runnables.MediaRecorderViewAdapter;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.main)
public class SoundTentActivity extends RoboActivity {
    public static final String LOG_PRE = "SoundTent";
    public static final long DELAY_MILLIS = 100L;

    @InjectView(R.id.sound_indicator)
    private View soundIndicatorView;
    @Inject
    private Handler handler;
    @Inject
    private RunnableAdapterFactory runnableAdapterFactory;
    @Inject
    private AndroidModelFactory modelFactory;

    private MediaRecorderViewAdapter mediaRecorderViewAdapter;
    private MediaRecorder mediaRecorder;
    private String tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();

        tempFile = getTempFile();
        mediaRecorder = modelFactory.createMediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(tempFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mediaRecorder.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaRecorderViewAdapter = runnableAdapterFactory.createMediaRecorderCaptor(mediaRecorder, soundIndicatorView, handler);

        handler.postDelayed(mediaRecorderViewAdapter, DELAY_MILLIS);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(mediaRecorderViewAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

        new File(tempFile).delete();
    }

    private String getTempFile() {
        try {
            return File.createTempFile("soundtent", null, getCacheDir()).getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
