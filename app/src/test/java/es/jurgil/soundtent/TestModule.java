package es.jurgil.soundtent;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import java.util.HashMap;

import es.jurgil.soundtent.factories.RunnableAdapterFactory;

public class TestModule extends AbstractModule {
    private static final HashMap<Class, Object> classMocks = new HashMap<Class, Object>();

    /* todo:  add ability to inject into test classes */
    /* todo:  add ability to automatically bind mocks to injections */

    public void bind(Class clazz, Object object){
        classMocks.put(clazz, object);
    }

    @Override
    protected void configure() {
        for(Class clazzes : classMocks.keySet()){
           bind(clazzes).toInstance(classMocks.get(clazzes));
        }

        install(new FactoryModuleBuilder().build(RunnableAdapterFactory.class));
    }
}
