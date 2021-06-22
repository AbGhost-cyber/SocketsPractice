package com.crushtech.socketspractice.contract;

import android.util.Log;
import android.view.View;

public class ChatPresenter implements ChatContract.ChatPresenter, ChatContract.ChatModel.chatConnectionListener {
    private final ChatContract.ChatModel model;
    private ChatContract.ChatView mainView;
    private String TAG = getClass().getSimpleName();

    public ChatPresenter(ChatContract.ChatModel model, ChatContract.ChatView mainView) {
        this.model = model;
        this.mainView = mainView;
    }

    @Override
    public void performConnectionToServer() {
        if (mainView != null) mainView.progressState(View.VISIBLE);
        model.onChatConnect(this);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void sendMessageToServer(String message) {
        if (mainView != null) model.sendMessage(this, message);
    }

    @Override
    public void onSuccess(String message, ChatContract.ConnectionResult result) {
        if (mainView != null) {
            mainView.setData(message, result.alignment);
            mainView.enableSending(true);
            mainView.progressState(View.INVISIBLE);
        }
        Log.d(TAG, "onSuccess: called");
    }

    @Override
    public void onFailure(String message) {
        if (mainView != null) {
            mainView.setData(message, View.TEXT_ALIGNMENT_CENTER);
            mainView.enableSending(false);
            mainView.progressState(View.INVISIBLE);
        }
        Log.d(TAG, "onFailure: called");
    }
}
