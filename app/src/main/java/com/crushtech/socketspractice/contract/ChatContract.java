package com.crushtech.socketspractice.contract;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface ChatContract {
    interface ChatView {
        void progressState(int visibility);

        void setData(String value, int align);

        void enableSending(boolean enabled);
    }

    interface ChatModel {
        void onChatConnect(chatConnectionListener chatConnectionListener);
        void onDisconnect(chatConnectionListener chatConnectionListener);

        void sendMessage(chatConnectionListener chatConnectionListener, String message);

        interface chatConnectionListener {
            void onSuccess(String message, ConnectionResult result);

            void onFailure(String message);

        }
    }

    interface ChatPresenter {
        void performConnectionToServer();

        void onDestroy();

        void sendMessageToServer(String message);
        void disconnectServer();
    }

    class ConnectionResult {
        DataOutputStream dO;
        DataInputStream dI;
        int alignment;

        public ConnectionResult(DataOutputStream dO, DataInputStream dI, int alignment) {
            this.dO = dO;
            this.dI = dI;
            this.alignment = alignment;
        }
    }
}
