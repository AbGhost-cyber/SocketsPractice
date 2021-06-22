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
    private final String TAG = getClass().getSimpleName();
    private ConnectionResult result;
    private Socket clientSocket;


    public ChatModel(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    private void setResult(ConnectionResult result) {
        this.result = result;
    }

    private ConnectionResult getResult() {
        return result;
    }

    private int getPort() {
        return port;
    }

    private String getIpAddress() {
        return ipAddress;
    }

    private Socket getSocket() {
        return clientSocket;
    }

    private void setSocket(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void onChatConnect(chatConnectionListener chatConnectionListener) {
        new Thread(() -> {
            try {
                clientSocket = new Socket(getIpAddress(), getPort());
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

                ConnectionResult result = new ConnectionResult(dataOutputStream,
                        dataInputStream, View.TEXT_ALIGNMENT_TEXT_START);
                if (chatConnectionListener != null) {
                    chatConnectionListener.onSuccess("Connected" + "\n", result);
                    setResult(result);
                    setSocket(clientSocket);

                    String fromServer;
                    while ((fromServer = dataInputStream.readUTF()) != null) {
                        chatConnectionListener.onSuccess("bot\uD83D\uDC7D: " + fromServer + "\n", result);
                        if (fromServer.equals("Bye."))
                            break;
                    }
                    Log.d(TAG, "fetchChats: called");
                }
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
            if (message != null && chatConnectionListener != null) {
                chatConnectionListener.onSuccess("you\uD83D\uDE0A: " + message + "\n", getResult());
                try {
                    getResult().dO.writeUTF(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    chatConnectionListener.onFailure(e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void onDisconnect(chatConnectionListener chatConnectionListener) {
        new Thread(() -> {
            try {
                if (getResult().dO != null && getResult().dI != null && getSocket() != null) {
                    getResult().dO.close();
                    getResult().dI.close();
                    getSocket().close();
                }
                chatConnectionListener.onSuccess("Chat Disconnected", result);
            } catch (IOException e) {
                e.printStackTrace();
                chatConnectionListener.onFailure(e.getMessage());
            }
        }).start();
    }
}
