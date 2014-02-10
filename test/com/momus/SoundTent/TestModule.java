package com.momus.SoundTent;

import com.google.inject.AbstractModule;

import java.util.HashMap;

public class TestModule extends AbstractModule {
    private HashMap<Class, Object> classMocks = new HashMap<Class, Object>();

    public void bind(Class clazz, Object object){
        classMocks.put(clazz, object);
    }

    @Override
    protected void configure() {
        for(Class clazzes : classMocks.keySet()){
           bind(clazzes).toInstance(classMocks.get(clazzes));
        }
    }
}
