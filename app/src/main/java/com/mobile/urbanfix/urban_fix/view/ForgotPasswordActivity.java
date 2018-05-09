package com.mobile.urbanfix.urban_fix.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.ForgotPasswordPresenter;
import com.mobile.urbanfix.urban_fix.MainMVP;

public class ForgotPasswordActivity extends AppCompatActivity
                                    implements  MainMVP.IForgotPasswordView,
                                                View.OnClickListener {


    private EditText emailForgotPasswordEditText;
    private MainMVP.IForgotPasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        startMVP();
        emailForgotPasswordEditText =  findViewById(R.id.emailForgotPasswordEditText);
        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(this);
    }

    @Override
    public void showMessage(String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG ).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void onClick(View v) {
        presenter.sendRecoverPasswordMessage(emailForgotPasswordEditText.getText().toString());
    }

    @Override
    public void showFailedMessage() {
        Toast.makeText(this, getString(R.string.forgot_password_msg_sended) +
                emailForgotPasswordEditText.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage() {
        Toast.makeText(this, getString(R.string.forgot_password_email_failed) +
                emailForgotPasswordEditText.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void startMVP() {
        presenter = new ForgotPasswordPresenter(this);
    }

}
