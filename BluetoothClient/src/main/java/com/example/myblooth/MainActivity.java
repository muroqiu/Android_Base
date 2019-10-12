package com.example.myblooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
    private BluetoothAdapter bAtapter;
    private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
    private List<String> nameList = new ArrayList<String>();
    private ListView mListView;
    private BluetoothSocket bs;
    private EditText mEditText;
    private StringBuilder incoming = new StringBuilder();
    private TextView tv;
    private BluetoothSocket transferSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.et_id);
        tv = (TextView) findViewById(R.id.tv_id);
        initView();
    }

    public void click(View view) {
        if (transferSocket != null) {
            String str = mEditText.getText().toString();
            sendMessage(transferSocket, "客户端说：" + str + "\n");
        } else {
            new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    connectToServerSocket(null,
                            UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
                }
            }.start();
        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        bAtapter = BluetoothAdapter.getDefaultAdapter();
        mListView = (ListView) findViewById(R.id.list_id);
        mListView.setOnItemClickListener(this);
//        startDiscovery();
//        mListView.setAdapter(baseAdapter);
    }

    private void startDiscovery() {
        // TODO Auto-generated method stub
        registerReceiver(discoveryRsult, new IntentFilter(
                BluetoothDevice.ACTION_FOUND));

        deviceList.clear();
        nameList.clear();
        bAtapter.startDiscovery();

    }

    BroadcastReceiver discoveryRsult = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            nameList.add(arg1.getStringExtra(BluetoothDevice.EXTRA_NAME));
            deviceList.add((BluetoothDevice) arg1
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
            Log.i("caohaidemo", "nameList:" + nameList.size());
            baseAdapter.notifyDataSetChanged();

        }
    };
    BaseAdapter baseAdapter = new BaseAdapter() {

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            arg1 = LayoutInflater.from(MainActivity.this).inflate(
                    R.layout.item, arg2, false);
            TextView tv = (TextView) arg1.findViewById(R.id.item_text);
            tv.setText(nameList.get(arg0));
            return arg1;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return nameList.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return nameList.size();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        final BluetoothDevice device = deviceList.get(arg2);
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                connectToServerSocket(device,
                        UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));
            }
        }.start();

    }



    private void connectToServerSocket(BluetoothDevice device, UUID uuid) {
        try {
            BluetoothDevice deviceCur = null;
            if (bAtapter != null && bAtapter.isEnabled()) {
                Set<BluetoothDevice> bondeds = bAtapter.getBondedDevices();
                for (BluetoothDevice device1 : bondeds) {
                    //                Log.e(TAG, BaseDevice.getName() + " " + BaseDevice.getAddress() + " BondState " + BaseDevice
                    //                    .getBondState());
                    Log.e("SSS", device1.getName()  + "  " + device1.getAddress());
//                    devices.put(device.getAddress(), device);
//                    //                }
                    deviceCur = device1;
                }
            }

//            BluetoothSocket clientSocket = deviceCur
//                    .createRfcommSocketToServiceRecord(uuid);
            BluetoothSocket clientSocket = deviceCur
                    .createInsecureRfcommSocketToServiceRecord(uuid);
            transferSocket = clientSocket;
            // Block until server connection accepted.
            clientSocket.connect();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_LONG)
                            .show();
                }
            });
            // Start listening for messages.

            listenForMessages(clientSocket, incoming);

            // Add a reference to the socket used to send messages.

        } catch (IOException e) {
            Log.e("BLUETOOTH", "Blueooth client I/O Exception", e);
        }
    }

    private void sendMessage(BluetoothSocket socket, String message) {
        OutputStream outStream;
        try {
            outStream = socket.getOutputStream();

            // Add a stop character.
            byte[] byteArray = (message + " ").getBytes();
            byteArray[byteArray.length - 1] = 0;

            outStream.write(byteArray);
        } catch (IOException e) {

        }
    }

    private boolean listening = false;

    private void listenForMessages(BluetoothSocket socket,
                                   final StringBuilder incoming) {
        listening = true;

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        try {
            InputStream instream = socket.getInputStream();
            int bytesRead = -1;

            while (listening) {
                bytesRead = instream.read(buffer);
                if (bytesRead != -1) {
                    String result = "";
                    while ((bytesRead == bufferSize)
                            && (buffer[bufferSize - 1] != 0)) {
                        result = result + new String(buffer, 0, bytesRead - 1);
                        bytesRead = instream.read(buffer);
                    }
                    result = result + new String(buffer, 0, bytesRead - 1);
                    incoming.append(result);
                    Log.i("caohaidemo", "服务器说：" + incoming.toString());
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            tv.setText(incoming.toString());
                        }
                    });
                }

            }
            // socket.close();
        } catch (IOException e) {

        } finally {
        }
    }
}
