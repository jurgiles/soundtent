package com.momus.SoundTent;

import android.util.Log;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.momus.SoundTent.activities.SoundTentActivity;
import com.momus.SoundTent.factories.AndroidModelFactory;
import com.momus.SoundTent.factories.RunnableAdapterFactory;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        Log.d(SoundTentActivity.LOG_PRE, "Loading app module");

        install(new FactoryModuleBuilder().build(RunnableAdapterFactory.class));
        install(new FactoryModuleBuilder().build(AndroidModelFactory.class));
    }
}
