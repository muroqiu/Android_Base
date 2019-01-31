package com.scrm.test.sqlite;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;


public class LogProcessService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init("LogProcessService");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        for (int i =0; i<= 100; i++) {
//            SystemClock.sleep(5);
            LogUtils.d("LogProcessService", i + " just test LogProcessService");
        }
        return super.onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
