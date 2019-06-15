package com.wrbug.dumpdex.util;

import android.util.Log;
import de.robv.android.xposed.XposedBridge;
import java.io.FileOutputStream;

import static com.wrbug.dumpdex.MainActivity.TAG;

/**
 * Created by wrbug on 2017/8/23.
 */
public class FileUtils {

  public static void writeByteToFile(byte[] data, String path) {
    try {
      FileOutputStream localFileOutputStream = new FileOutputStream(path);
      localFileOutputStream.write(data);
      localFileOutputStream.close();
    } catch (Exception e) {
      XposedBridge.log(e);
      Log.e(TAG, e.getMessage());
    }
  }
}
