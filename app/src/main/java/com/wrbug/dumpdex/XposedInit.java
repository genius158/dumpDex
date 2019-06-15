package com.wrbug.dumpdex;

import android.os.Build;
import android.util.Log;
import com.wrbug.dumpdex.dump.LowSdkDump;
import com.wrbug.dumpdex.dump.OreoDump;
import com.wrbug.dumpdex.util.DeviceUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;

import static com.wrbug.dumpdex.MainActivity.FILE_PATH;
import static com.wrbug.dumpdex.MainActivity.TAG;

/**
 * XposedInit
 *
 * @author wrbug
 * @since 2018/3/20
 */
public class XposedInit implements IXposedHookLoadPackage {

  public static void log(String txt) {
    Log.e(TAG, txt);
    XposedBridge.log("dumpdex-> " + txt);
  }

  public static void log(Throwable t) {
    XposedBridge.log(t);
    Log.e(TAG, t.getMessage());
  }

  @Override public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
    String pName = MainActivity.readFile(FILE_PATH);
    Log.e(TAG, "target packageName " + pName);

    final String packageName = lpparam.packageName;
    if (pName.equals(packageName)) {
      String path = "/data/data/" + packageName + "/dump";
      File parent = new File(path);
      if (!parent.exists() || !parent.isDirectory()) {
        parent.mkdirs();
      }
      log("sdk version:" + Build.VERSION.SDK_INT);
      if (DeviceUtils.isOreo() || DeviceUtils.isPie()) {
        OreoDump.init(lpparam);
      } else {
        LowSdkDump.init(lpparam);
      }
    }
  }
}
