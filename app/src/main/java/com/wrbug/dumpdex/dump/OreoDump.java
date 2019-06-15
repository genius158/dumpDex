package com.wrbug.dumpdex.dump;

import android.util.Log;
import com.wrbug.dumpdex.BuildConfig;
import com.wrbug.dumpdex.Native;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.wrbug.dumpdex.MainActivity.TAG;

/**
 * OreoDump
 *
 * @author WrBug
 * @since 2018/3/23
 */
public class OreoDump {

  public static void log(String txt) {
    if (!BuildConfig.DEBUG) {
      return;
    }
    XposedBridge.log("dumpdex-> " + txt);
    Log.e(TAG, "dumpdex-> " + txt);
  }

  public static void init(final XC_LoadPackage.LoadPackageParam lpparam) {
    Native.dump(lpparam.packageName);
  }
}
