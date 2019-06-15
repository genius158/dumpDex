package com.wrbug.dumpdex;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "XposedDump";
  public static final String FILE_PATH =
      Environment.getExternalStorageDirectory().getAbsolutePath() + "/dumpPath.txt";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      ActivityCompat.requestPermissions(this, new String[] {
          Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
      }, 111);
    }
  }

  @Override protected void onResume() {
    super.onResume();
    try {
      String txt = readFile(FILE_PATH);
      Log.e(TAG, "onResume: " + txt);
      ((EditText) findViewById(R.id.et_package_name)).setText(txt);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    try {
      writeFile(((EditText) findViewById(R.id.et_package_name)).getText().toString(), FILE_PATH);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static void writeFile(String data, String path) {
    FileWriter fileOutputStream = null;
    try {
      fileOutputStream = new FileWriter(path);
      fileOutputStream.write(data);
      fileOutputStream.flush();
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
    } finally {
      try {
        fileOutputStream.close();
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  public static String readFile(String path) {
    BufferedReader fileOutputStream = null;
    try {
      fileOutputStream = new BufferedReader(new FileReader(path));
      return fileOutputStream.readLine();
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
    } finally {
      try {
        fileOutputStream.close();
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
