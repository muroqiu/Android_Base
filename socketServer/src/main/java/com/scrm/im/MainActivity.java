package com.scrm.im;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {

    public static ServerSocket serverSocket = null;
    public static TextView tv_IP, tv_show;
    public Button btn_send;
    public EditText edt_comment;
    private String IP = "";
    String buffer = "";

    OutputStream output;
    Socket socket;

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what==0x11) {
                Bundle bundle = msg.getData();
                tv_show.setText("recive:  "+bundle.getString("msg")+"\n");
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initService();

    }

    @Override
    protected void onDestroy() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private void initView() {
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_IP = (TextView) findViewById(R.id.tv_IP);
        edt_comment = findViewById(R.id.edt_Comment);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        IP = getlocalip();
        tv_IP.setText("IP addresss:"+IP);
    }


    private void initService(){
        new Thread() {
            public void run() {
                Bundle bundle = new Bundle();
                bundle.clear();
                try {
                    serverSocket = new ServerSocket(30000);
                    while (true) {
                        try {
                            socket = serverSocket.accept();
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    while (true) {
                        Message msg = new Message();
                        msg.what = 0x11;
                        try {
                            BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String line = null;
                            buffer = "";
                            while ((line = bff.readLine())!=null) {
                                buffer = line + buffer;
                            }

                            if (!TextUtils.isEmpty(buffer)) {
                                bundle.putString("msg", buffer.toString());
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            };
        }.start();
    }

    /**
     * 或取本机的ip地址
     */
    private String getlocalip(){
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        //  Log.d(Tag, "int ip "+ipAddress);
        if(ipAddress==0)return null;
        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
                +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
    }

//
//    class MyThread extends Thread {
//
//        public String txt1;
//
//        public MyThread(String str) {
//            txt1 = str;
//        }
//
//        @Override
//        public void run() {
//            //定义消息
//            Message msg = new Message();
//            msg.what = 0x11;
//            Bundle bundle = new Bundle();
//            bundle.clear();
//            try {
//                //获取输入输出流
//                OutputStream ou = socket.getOutputStream();
//                BufferedReader bff = new BufferedReader(new InputStreamReader(
//                        socket.getInputStream()));
//                //读取发来服务器信息
//                String line = null;
//                buffer="";
//                while ((line = bff.readLine()) != null) {
//                    buffer = line + buffer;
//                }
//
//                //向服务器发送信息
//                ou.write(txt1.getBytes("utf-8"));
//                ou.flush();
//                bundle.putString("msg", buffer.toString());
//                msg.setData(bundle);
//                //发送消息 修改UI线程中的组件
//                myHandler.sendMessage(msg);
//                //关闭各种输入输出流
//                bff.close();
//                ou.close();
//
//            } catch (SocketTimeoutException aa) {
//                //连接超时 在UI界面显示消息
//                bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
//                msg.setData(bundle);
//                //发送消息 修改UI线程中的组件
//                myHandler.sendMessage(msg);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
