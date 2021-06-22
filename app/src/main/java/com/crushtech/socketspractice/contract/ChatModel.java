package com.crushtech.socketspractice.contract;

import android.util.Log;
import android.view.View;

import com.crushtech.socketspractice.contract.ChatContract.ConnectionResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatModel implements ChatContract.ChatModel {
    private final String ipAddress;
    private final int port;
    private String TAG = getClass().getSimpleName();
    private ConnectionResult result;


    public ChatModel(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void setResult(ConnectionResult result) {
        this.result = result;
    }

    public ConnectionResult getResult() {
        return result;
    }

    public int getPort() {
        return port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public void onChatConnect(chatConnectionListener chatConnectionListener) {
        new Thread(() -> {
            try {
                Socket clientSocket = new Socket(getIpAddress(), getPort());
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

                ConnectionResult result = new ConnectionResult(dataOutputStream,
                        dataInputStream, View.TEXT_ALIGNMENT_TEXT_START);
                chatConnectionListener.onSuccess("Connected" + "\n", result);
                setResult(result);

                String fromServer;
                while ((fromServer = dataInputStream.readUTF()) != null) {
                    chatConnectionListener.onSuccess("bot\uD83D\uDC7D: " + fromServer + "\n", result);
                    if (fromServer.equals("Bye."))
                        break;
                }
                Log.d(TAG, "fetchChats: called");
            } catch (IOException e) {
                e.printStackTrace();
                chatConnectionListener.onFailure(e.getMessage());
                Log.d(TAG, "fetchChats: error");
            }
        }).start();
    }

    @Override
    public void sendMessage(chatConnectionListener chatConnectionListener, String message) {
        new Thread(() -> {
            if (message != null) {
                chatConnectionListener.onSuccess("you: " + message + "\n", getResult());
                try {
                    getResult().dO.writeUTF(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    chatConnectionListener.onFailure(e.getMessage());
                }
            }
        }).start();
    }


}
