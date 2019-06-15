package com.wrbug.dumpdex;

import android.util.Log;
import de.robv.android.xposed.XposedBridge;

import static com.wrbug.dumpdex.MainActivity.TAG;

/**
 * Native
 *
 * @author WrBug
 * @since 2018/3/23
 */
public class Native {
  public static void log(String txt) {
    XposedBridge.log("dumpdex.Native-> " + txt);
    Log.e(TAG, "dumpdex.Native-> " + txt);
  }

  static {
    try {
      System.load("/data/local/tmp/libnativeDump.so");
      log("loaded libnativeDump.so");
    } catch (Throwable t) {
      System.load("/data/local/tmp/libnativeDump64.so");
      log("loaded libnativeDump64.so");
    }
    //        System.loadLibrary("nativeDump");
  }

  public static native void dump(String packageName);
}
