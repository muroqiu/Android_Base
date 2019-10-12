package com.example.mybloothserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText mEditText;
    private StringBuffer sb;
    private TextView tv;
    private StringBuilder incoming = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.et_id);
        sb = new StringBuffer();
        tv = (TextView) findViewById(R.id.tv_id);
        startServerSocket(BluetoothAdapter.getDefaultAdapter());
    }

    public void click(View view) {

        if (transferSocket != null) {
            String str = mEditText.getText().toString();
            sendMessage(transferSocket, "服务器说：" + str + "\n");
        }

    }

    private BluetoothSocket transferSocket;

    private UUID startServerSocket(BluetoothAdapter bluetooth) {
        UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
        String name = "bluetoothserver";

        try {
            final BluetoothServerSocket btserver = bluetooth
                    .listenUsingInsecureRfcommWithServiceRecord(name, uuid);

            Thread acceptThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        // Block until client connection established.
                        final BluetoothSocket serverSocket = btserver.accept();
                        transferSocket = serverSocket;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "连接成功",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        // Start listening for messages.

                        listenForMessages(serverSocket, incoming);
                        // Add a reference to the socket used to send messages.

                    } catch (IOException e) {
                        Log.e("BLUETOOTH", "Server connection IO Exception", e);
                    }
                }
            });
            acceptThread.start();
        } catch (IOException e) {
            Log.e("BLUETOOTH", "Socket listener IO Exception", e);
        }
        return uuid;
    }

    /**
     * Listing 16-8: Sending and receiving strings using Bluetooth Sockets
     */
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
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            tv.setText(incoming.toString());
                        }
                    });
                }
                // socket.close();
            }
            socket.close();
        } catch (IOException e) {

        } finally {
        }
    }

}
