package com.crushtech.socketspractice;

import android.content.Context;
import android.os.Bundle;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.crushtech.socketspractice.contract.ChatContract;
import com.crushtech.socketspractice.contract.ChatModel;
import com.crushtech.socketspractice.contract.ChatPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ChatContract.ChatView {
    private MaterialTextView tvMessages;
    private MaterialButton btnConnect, btnSend;
    private AppCompatEditText etIp, etPort, etMessage;
    private ChatPresenter presenter;
    private ProgressBar progressBar;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        btnConnect.setOnClickListener(v -> {
            if (!isConnected) {
                if (TextUtils.isEmpty(etIp.getText()) || TextUtils.isEmpty(String.valueOf(etPort.getText()))) {
                    Toast.makeText(this, "Some details are wrong, please confirm", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter = new ChatPresenter(new ChatModel(Objects.requireNonNull(etIp.getText()).toString(),
                        Integer.parseInt(Objects.requireNonNull(etPort.getText()).toString())), this);

                presenter.performConnectionToServer();
            } else {
                presenter.disconnectServer();
            }
        });
        btnSend.setOnClickListener(v -> {
            if (TextUtils.isEmpty(etMessage.getText())) {
                Toast.makeText(this, "please enter a text", Toast.LENGTH_SHORT).show();
                return;
            }
            presenter.sendMessageToServer(Objects.requireNonNull(etMessage.getText()).toString());
            hideKeyboard();
        });

    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }


    private void initViews() {
        tvMessages = findViewById(R.id.tvMessages);
        //make text scrollable
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
        runOnUiThread(() -> progressBar.setVisibility(visibility));
    }

    @Override
    public void enableSending(boolean enabled) {
        runOnUiThread(() -> {
            btnSend.setEnabled(enabled);
            //if send is enabled then we are connected, this is called everytime an IO is successful
            setConnected(enabled);
            btnConnect.setText(isConnected ? getString(R.string.connected) : getString(R.string.connect_to_server));
            if (isConnected) {
                btnConnect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_cloud_done_24, 0);
            } else {
                btnConnect.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_cloud_24, 0);
            }
        });
    }

    @Override
    public void setData(String value, int align) {
        runOnUiThread(() -> {
            //scroll and focus current text magic
            SpannableString spannableString = new SpannableString("\n" + value);
            Selection.setSelection(spannableString, spannableString.length());
            tvMessages.append(spannableString);
            tvMessages.setTextAlignment(align);
            etMessage.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}