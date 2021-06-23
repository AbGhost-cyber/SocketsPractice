package com.crushtech.socketspractice.contract;

import android.util.Log;
import android.view.View;

import com.crushtech.socketspractice.contract.ChatContract.ConnectionResult;

import static com.crushtech.socketspractice.contract.ChatContract.ChatModel.chatConnectionListener;
/**
 * @author UDO ABUNDANCE
 *         Date: 2021/6/22
 *         Class description:
 */
public class ChatPresenter implements ChatContract.ChatPresenter, chatConnectionListener {
    private final ChatContract.ChatModel model;
    private ChatContract.ChatView mainView;
    private final String TAG = getClass().getSimpleName();

    public ChatPresenter(ChatContract.ChatModel model, ChatContract.ChatView mainView) {
        this.model = model;
        this.mainView = mainView;
    }

    @Override
    public void performConnectionToServer() {
        if (mainView != null) mainView.progressState(View.VISIBLE);
        model.onServerResponse(this);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void sendMessageToServer(String message) {
        if (mainView != null) model.onClientResponse(this, message);
    }

    @Override
    public void onSuccess(String message, ConnectionResult result) {
        if (mainView != null) {
            mainView.setData(message, result.alignment);
            mainView.enableSending(true);
            mainView.progressState(View.INVISIBLE);
        }
        Log.d(TAG, "onSuccess: called");
    }

    @Override
    public void onFailure(String message) {
        handleFailure(message);
        Log.d(TAG, "onFailure: called");
    }


    @Override
    public void disconnectServer() {
        if (mainView != null) model.onDisconnect(new chatConnectionListener() {
            @Override
            public void onSuccess(String message, ConnectionResult result) {
                if (mainView != null) {
                    mainView.setData(message, result.alignment);
                    mainView.enableSending(false);
                    mainView.progressState(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(String message) {
                handleFailure(message);
            }
        });
    }

    private void handleFailure(String message) {
        if (mainView != null) {
            mainView.setData(message, View.TEXT_ALIGNMENT_CENTER);
            mainView.enableSending(false);
            mainView.progressState(View.INVISIBLE);
        }
    }
}
