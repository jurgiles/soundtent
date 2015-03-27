package es.jurgil.soundtent;

import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import es.jurgil.soundtent.activities.SoundTentActivity;
import es.jurgil.soundtent.factories.AndroidModelFactory;
import es.jurgil.soundtent.factories.RunnableAdapterFactory;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        Log.d(SoundTentActivity.LOG_PRE, "Loading app module");

        install(new FactoryModuleBuilder().build(RunnableAdapterFactory.class));
        install(new FactoryModuleBuilder().build(AndroidModelFactory.class));
    }
}
