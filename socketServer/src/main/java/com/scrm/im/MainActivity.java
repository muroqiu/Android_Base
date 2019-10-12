package com.scrm.im;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    public static ServerSocket serverSocket = null;
    public static TextView tv_IP, tv_show;
    private String IP = "";
    String buffer = "";
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
        receive();

    }

    private void initView() {
        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_IP = (TextView) findViewById(R.id.tv_IP);
        IP = getlocalip();
        tv_IP.setText("IP addresss:"+IP);
    }

    private void receive(){
        new Thread() {
            public void run() {
                Bundle bundle = new Bundle();
                bundle.clear();
                OutputStream output;
                String str = "hello hehe";
                try {
                    serverSocket = new ServerSocket(30000);
                    Socket socket = serverSocket.accept();
                    output = socket.getOutputStream();
//                    output.write(str.getBytes("utf-8"));
//                    output.write("a2".getBytes("utf-8"));
//                    output.flush();
                    send(output);
                    socket.shutdownOutput();
                    Message msg = new Message();
                    msg.what = 0x11;
                    BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        try {
                            String line = null;
                            buffer = "";
                            while ((line = bff.readLine())!=null) {
                                buffer = line + buffer;
                            }
                            if (!TextUtils.isEmpty(buffer)) {
                                bundle.putString("msg", buffer.toString());
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);

                                if (buffer.contains("88")) {
                                    break;
                                }
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    bff.close();
                    output.close();
                    socket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            };
        }.start();
    }

    private void send(OutputStream output) throws IOException {
        for (int i = 0; i < 10 ; i++) {
            output.write(("server " + i + "\n").getBytes("utf-8"));
            output.flush();
        }

    }
//
//    private void receive(){
//        new Thread() {
//            public void run() {
//                Bundle bundle = new Bundle();
//                bundle.clear();
//                OutputStream output;
//                String str = "hello hehe";
//                try {
//                    serverSocket = new ServerSocket(30000);
//                    while (true) {
//                        Message msg = new Message();
//                        msg.what = 0x11;
//                        try {
//                            Socket socket = serverSocket.accept();
//                            output = socket.getOutputStream();
//                            output.write(str.getBytes("utf-8"));
//                            output.flush();
//                            socket.shutdownOutput();
//                            BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                            String line = null;
//                            buffer = "";
//                            while ((line = bff.readLine())!=null) {
//                                buffer = line + buffer;
//                            }
//                            bundle.putString("msg", buffer.toString());
//                            msg.setData(bundle);
//                            mHandler.sendMessage(msg);
//                            bff.close();
//                            output.close();
//                            socket.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            };
//        }.start();
//    }

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

}
