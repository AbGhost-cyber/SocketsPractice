package com.crushtech.socketspractice;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.crushtech.socketspractice.contract.ChatContract;
import com.crushtech.socketspractice.contract.ChatModel;
import com.crushtech.socketspractice.contract.ChatPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity implements ChatContract.ChatView {
    private MaterialTextView tvMessages;
    private MaterialButton btnConnect, btnSend;
    private AppCompatEditText etIp, etPort, etMessage;
    private ChatPresenter presenter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        btnConnect.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etIp.getText()) || TextUtils.isEmpty(String.valueOf(etPort.getText()))) {
                Toast.makeText(this, "Some details are wrong, please confirm", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter = new ChatPresenter(new ChatModel(etIp.getText().toString(),
                    Integer.parseInt(etPort.getText().toString())), this);

            presenter.performConnectionToServer();
        });
        btnSend.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etMessage.getText())) {
                Toast.makeText(this, "please enter a text", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.sendMessageToServer(etMessage.getText().toString());
            //move to the next message
            tvMessages.scrollTo(1, 2);
        });
    }

    private void initViews() {
        tvMessages = findViewById(R.id.tvMessages);
        //scroll
        tvMessages.setMovementMethod(new ScrollingMovementMethod());
        btnConnect = findViewById(R.id.btnConnect);
        btnSend = findViewById(R.id.btnSend);
        etIp = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        etMessage = findViewById(R.id.etMessage);
        progressBar = findViewById(R.id.bar);
    }

    @Override
    public void progressState(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void enableSending(boolean enabled) {
        runOnUiThread(() -> btnSend.setEnabled(enabled));
    }

    @Override
    public void setData(String value, int align) {
        runOnUiThread(() -> {
            tvMessages.append("\n" + value);
            tvMessages.setTextAlignment(align);
            etMessage.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}