package com.momus.SoundTent;

import android.util.Log;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.momus.SoundTent.activities.SoundTentActivity;
import com.momus.SoundTent.factories.RunnableCaptorFactory;
import com.momus.SoundTent.threads.MediaRecorderCaptor;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        Log.d(SoundTentActivity.LOG_PRE, "Loading app module");

        install(new FactoryModuleBuilder()
                .implement(Runnable.class, MediaRecorderCaptor.class)
                .build(RunnableCaptorFactory.class));
    }
}
