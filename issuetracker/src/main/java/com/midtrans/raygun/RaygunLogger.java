package com.midtrans.raygun;

import android.util.Log;

public class RaygunLogger {

  public static void d(String string) {
    if (string != null) {
      Log.d("Raygun4Android", string);
    }
  }

  public static void i(String string) {
    if (string != null) {
      Log.i("Raygun4Android", string);
    }
  }

  public static void w(String string) {
    if (string != null) {
      Log.w("Raygun4Android", string);
    }
  }

  public static void e(String string) {
    if (string != null) {
      Log.e("Raygun4Android", string);
    }
  }
}