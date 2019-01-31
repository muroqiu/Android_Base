package com.scrm.test.sqlite;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;


public class LogProcessServiceNew extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.initNew(LogUtils.sProcessName);
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        for (int i =0; i<= 100; i++) {
//            SystemClock.sleep(10);
            LogUtils.d("LogProcessService", i + " just test LogProcessServiceNew");
        }
        return super.onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
