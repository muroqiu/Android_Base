package com.scrm.im;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.scrm.im.Utils.SPUtils;
import com.scrm.im.Utils.Simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnTest;
    EditText tvDebug;
    /**
     * 文件上传缓存对应表
     */
    private static SPUtils.Sp mFileCacher_Upload = SPUtils.getSp(SPUtils.SpType.FILE_UPLOAD_CACHER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btnTest = findViewById(R.id.btnTest);
        tvDebug = findViewById(R.id.edtDebug);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvDebug.append(Simulator.execute("du -sm /sdcard/* | awk '$1 > 100' | sort -nr | head -3"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
                try {
                    tvDebug.append(Simulator.execute("du -sm /sdcard/tencent/MicroMsg/* | awk '$1 > 100' | sort -nr | head -3"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
                try {
                    tvDebug.append(Simulator.execute("du -sm /sdcard/scrm/* | awk '$1 > 100' | sort -nr | head -3"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
                tvDebug.append("\n");
            }
        });

        findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvDebug.append(Simulator.execute("du -sm /data/data/* | awk '$1 > 100' | sort -nr | head -3"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
                try {
                    tvDebug.append(Simulator.execute("du -sm /data/data/com.tencent.mm/* | awk '$1 > 100' | sort -nr | head -3"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
                try {
                    tvDebug.append(Simulator.execute("du -sm /data/data/com.tencent.mm/MicroMsg/* | awk '$1 > 100' | sort -nr | head -3"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    tvDebug.append(Simulator.execute("rm system/etc/whitelistapps"));
//                    tvDebug.append(Simulator.execute("rm data/data/scrm/whitelistapps"));
//                    tvDebug.append(Simulator.execute("rm data/scrm/whitelistapps"));
//                    tvDebug.append("\n");
//                } catch (Exception e) {
//                    Log.e("MainActivity", e.toString());
//                }

                pickDataFromFile();
            }
        });

    }

    /**
     * 按行读取文件内容
     *
     * @param path
     * @return
     */
    public static StringBuilder pickDataLine(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.d("FileUtils", "file doesn't exists");
            return null;
        }
        InputStreamReader reader = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            br = new BufferedReader(reader);
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("DATA-USAGE")) {
                    Log.d("FileUtils", line);
                    String tmp = (line.substring(0,23) + " " + line.substring(line.indexOf("DATA-USAGE")));
                    sb.append(tmp);
                    sb.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb;
    }

    protected void pickDataFromFile() {
//        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dirPath = new File("/sdcard/scrm/picks/");
        File[] files = dirPath.listFiles();
        //如果为空对象，或为空文件夹，则直接删除文件夹
        if (files == null || files.length == 0) {
            return;
        }
        File targetFile = new File ("/sdcard/scrm/target.txt");
        if (targetFile.exists()) {
            targetFile.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            for (File itemFile : files) {
                StringBuilder sb = pickDataLine(itemFile.getAbsolutePath());
                fos.write(sb.toString().getBytes());
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
