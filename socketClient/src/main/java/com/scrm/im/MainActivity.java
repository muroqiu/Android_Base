package com.scrm.im;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Socket socket = null;
    String buffer = "";
    TextView tv_show;
    Button bt_send,bt_connect;
    EditText edt_IP;
    EditText edt_Comment;
    String geted1;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                tv_show.append("server:"+bundle.getString("msg")+"\n");

            // 每次客户端传输完数据后，再次调用扫描功能，实现连续扫描的功能（应该有更好的方法，暂时不知道）
            // startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClick();
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

    private void initClick() {
        bt_connect.setOnClickListener(this);
        bt_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_connect:
                new ConThread(edt_IP.getEditableText().toString()).start();
                break;
            case R.id.bt_send:
                geted1 = edt_Comment.getText().toString();
//                tv_show.append("client:"+geted1+"\n");
                //启动线程 向服务器发送和接收信息
                new MyThread(geted1).start();
                break;
            default:
                break;
        }
    }
    private void initView() {
        tv_show = (TextView) findViewById(R.id.tv_show);
        bt_send = (Button) findViewById(R.id.bt_send);
        edt_Comment = (EditText) findViewById(R.id.edt_Comment);
        edt_IP = (EditText) findViewById(R.id.edt_IP);
        bt_connect=findViewById(R.id.bt_connect);
    }

    class ConThread extends Thread {

        public String txt1;

        public ConThread(String str) {
            txt1 = str;
        }
        @Override
        public void run() {
            //定义消息
            Message msg = new Message();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();
            try {
                //连接服务器 并设置连接超时为10秒
                socket = new Socket();
                Log.d("SSS", txt1);
                socket.connect(new InetSocketAddress(txt1, 30000), 10000); //端口号和IP更换为自己的，也可以手动输入

            } catch (SocketTimeoutException aa) {
                //连接超时 在UI界面显示消息
                bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class MyThread extends Thread {

        public String txt1;

        public MyThread(String str) {
            txt1 = str;
        }

        @Override
        public void run() {
            //定义消息
            Message msg = new Message();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();
            try {
                //获取输入输出流
                OutputStream ou = socket.getOutputStream();
                BufferedReader bff = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                //读取发来服务器信息
                String line = null;
                buffer="";
                while ((line = bff.readLine()) != null) {
                    buffer = line + buffer;
                }

                //向服务器发送信息
//                ou.write(txt1.getBytes("utf-8"));
//                ou.flush();
                send(ou);
                bundle.putString("msg", buffer.toString());
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
                //关闭各种输入输出流
                bff.close();
                ou.close();

            } catch (SocketTimeoutException aa) {
                //连接超时 在UI界面显示消息
                bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void send(OutputStream ou) throws IOException {
        for (int i = 0; i < 100 ; i++) {
            ou.write(("client" + i + " ").getBytes("utf-8"));
            ou.flush();
        }
    }
}

