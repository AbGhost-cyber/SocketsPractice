package com.crushtech.socketspractice.contract;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @author UDO ABUNDANCE
 *         Date: 2021/6/22
 *         Class description:
 */
public interface ChatContract {
    interface ChatView {
        //progress bar visibility
        void progressState(int visibility);

        //set chat data and align
        void setData(String value, int align);

        //enable chat send
        void enableSending(boolean enabled);
    }

    interface ChatModel {
        //initializes the first connection and listen to server
        void onServerResponse(chatConnectionListener chatConnectionListener);
        //handles disconnection made by the client
        void onDisconnect(chatConnectionListener chatConnectionListener);
        //handles message sending from client
        void onClientResponse(chatConnectionListener chatConnectionListener, String message);

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
//the result of the connection class
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
