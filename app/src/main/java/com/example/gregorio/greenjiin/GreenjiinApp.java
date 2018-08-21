package com.example.gregorio.greenjiin;

import android.app.Application;
import io.realm.Realm;

public class GreenjiinApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
  }
}
