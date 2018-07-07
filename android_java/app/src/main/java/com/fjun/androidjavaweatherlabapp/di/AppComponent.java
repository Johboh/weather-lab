package com.fjun.androidjavaweatherlabapp.di;

import android.app.Application;

import com.fjun.androidjavaweatherlabapp.MyApplication;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class, ActivityModule.class})
public interface AppComponent extends AndroidInjector<MyApplication> {

    void inject(MyApplication app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
