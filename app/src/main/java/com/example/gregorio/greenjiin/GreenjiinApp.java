package com.example.gregorio.greenjiin;

import android.app.Application;
import com.facebook.appevents.AppEventsLogger;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GreenjiinApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
    Realm.setDefaultConfiguration(realmConfiguration);

    AppEventsLogger.activateApp(this);
  }
}
