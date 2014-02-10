package com.momus.SoundTent;

import com.google.inject.util.Modules;
import org.robolectric.Robolectric;
import roboguice.RoboGuice;

public class MockInjector {
    private final TestModule testModule;

    public MockInjector() {
        testModule = new TestModule();
    }

    public static MockInjector mockInjector(){
        return new MockInjector();
    }

    public MockInjector bind(Class clazz, Object object) {
        testModule.bind(clazz, object);
        return this;
    }

    public void inject(){
        RoboGuice.setBaseApplicationInjector(
                Robolectric.application,
                RoboGuice.DEFAULT_STAGE,
                Modules.override(RoboGuice.newDefaultRoboModule(Robolectric.application))
                        .with(testModule));
    }
}
