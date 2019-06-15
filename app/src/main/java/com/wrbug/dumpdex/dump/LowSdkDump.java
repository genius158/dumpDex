package com.wrbug.dumpdex.dump;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.wrbug.dumpdex.Native;
import com.wrbug.dumpdex.util.DeviceUtils;
import com.wrbug.dumpdex.util.FileUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.lang.reflect.Method;

import static com.wrbug.dumpdex.MainActivity.TAG;

/**
 * LowSdkDump
 *
 * @author WrBug
 * @since 2018/3/23
 */
public class LowSdkDump {
  public static void log(String txt) {
    Log.e(TAG, "dumpdex.LowSdkDump-> " + txt);
    XposedBridge.log("dumpdex.LowSdkDump-> " + txt);
  }

  public static void init(final XC_LoadPackage.LoadPackageParam lpparam) {
    log("start hook Instrumentation#newApplication");
    if (DeviceUtils.supportNativeHook()) {
      Native.dump(lpparam.packageName);
    }
    XposedHelpers.findAndHookMethod("android.app.Instrumentation", lpparam.classLoader,
        "newApplication", ClassLoader.class, String.class, Context.class, new XC_MethodHook() {
          @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            log("Application 232323  " + param.getResult());
            dump(lpparam.packageName, param.getResult());
            attachBaseContextHook(lpparam, ((Application) param.getResult()));
          }
        });
  }

  private static Method dexGetbytes;
  private static Method getDex;

  private static void initRefect() {
    try {
      Class dex = Class.forName("com.android.dex.Dex");
      dexGetbytes = dex.getDeclaredMethod("getBytes", new Class[0]);
      getDex = Class.forName("java.lang.Class").getDeclaredMethod("getDex", new Class[0]);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  private static void dump(String packageName, Object target) {
    if (!(target instanceof Class)) {
      target = target.getClass();
    }
    try {
      if (dexGetbytes == null || getDex == null) {
        initRefect();
      }
      byte[] bytes = (byte[]) dexGetbytes.invoke(getDex.invoke(target), new Object[0]);
      String path = "/data/data/" + packageName + "/dump";
      File file = new File(path, "source-" + bytes.length + ".dex");
      if (file.exists()) {
        log(file.getName() + " exists");
        return;
      }
      FileUtils.writeByteToFile(bytes, file.getAbsolutePath());
    } catch (Throwable e) {
      log("dump     " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void attachBaseContextHook(final XC_LoadPackage.LoadPackageParam lpparam,
      final Application application) {
    ClassLoader classLoader = application.getClassLoader();
    XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, boolean.class,
        new XC_MethodHook() {
          @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            log("loadClass->" + param.args[0]);
            Class result = (Class) param.getResult();
            if (result != null) {
              dump(lpparam.packageName, result);
            }
          }
        });
    XposedHelpers.findAndHookMethod("java.lang.ClassLoader", classLoader, "loadClass", String.class,
        boolean.class, new XC_MethodHook() {
          @Override protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            log("loadClassWithclassLoader->" + param.args[0]);
            Class result = (Class) param.getResult();
            if (result != null) {
              dump(lpparam.packageName, result);
            }
          }
        });
  }
}
