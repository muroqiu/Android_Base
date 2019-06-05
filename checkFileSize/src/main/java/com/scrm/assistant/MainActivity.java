package com.scrm.assistant;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scrm.assistant.Utils.SPUtils;
import com.scrm.assistant.Utils.Simulator;
import com.scrm.assistant.Utils.UriCacher;

import java.util.Random;

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
                    tvDebug.append(Simulator.execute("du -sm /data/data/* | awk '$1 > 3' | sort -nr | head -5"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
            }
        });

        findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvDebug.append(Simulator.execute("du -sm /sdcard/* | awk '$1 > 3' | sort -nr | head -5"));
                    tvDebug.append("\n");
                } catch (Exception e) {
                    Log.e("MainActivity", e.toString());
                }
            }
        });

    }

}
