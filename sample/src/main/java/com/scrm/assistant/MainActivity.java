package com.scrm.assistant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scrm.assistant.Utils.SPUtils;
import com.scrm.assistant.Utils.TimeUtils;
import com.scrm.assistant.Utils.UriCacher;

import org.json.JSONException;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button btnTest;
    TextView tvDebug;
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
        tvDebug = findViewById(R.id.tvDebug);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDebug.setText("abc");
            }
        });

        findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int j = random.nextInt();

                for (int i = 1 + j; i <= 1000 + j; i++) {
//                    mFileCacher_Upload.putString("key" + i, "{\"number\":" + "\"com.scrm.im/com.scrm.assistant.MainActivity" + i + "\"" + "}");
                    mFileCacher_Upload.putString("" + i, new UriCacher("uri " + i, "md5").toJsonString());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.e("TAG", i + ": " + mFileCacher_Upload.getString("" + i, ""));
                }
            }
        });

        findViewById(R.id.btnLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i <= 1000; i++) {
                    if (mFileCacher_Upload.contains("key" + i)) {
                        Log.e("TAG", i + "  " + mFileCacher_Upload.getString("" + i, ""));
                    }
                }
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clear();
            }
        });
    }
//
//    private void clear() {
//        Map<String, ?> all = mFileCacher_Upload.getAll();
//        Log.e("TAG", all.size() + "");
//        if (all != null && all.size() > 50) {
//            Set<String> keySet = all.keySet();
//            for (String key : keySet) {
//                try {
//                    Log.e("TAG", key);
//                    UriCacher uriCacher = new UriCacher((String) all.get(key));
//
//                    if (uriCacher.getTimes() + 100 < TimeUtils.seconds()) {
//                        Log.e("TAG", mFileCacher_Upload.getString(key, ""));
//                        mFileCacher_Upload.remove(key);
//                        Log.e("TAG", mFileCacher_Upload.getString(key, "aaaa"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
