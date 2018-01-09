package com.mobile.urbanfix.urban_fix.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.LoginPresenter;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, MainMVP.ILoginView {

    private EditText emailLoginEditText, passwordLoginEditText;
    private Button loginButton, forgotPasswordButton, registerButton;
    private MainMVP.ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startMVP();
        emailLoginEditText = (EditText) findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = (EditText) findViewById(R.id.passwordLoginEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener( this );
        forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener( this );
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch( id ) {
            case R.id.loginButton:
                loginPresenter.doLogin();
                break;
            case R.id.registerButton:
                loginPresenter.startRegisterView();
                break;
            case R.id.forgotPasswordButton:
                loginPresenter.startForgotPasswordView();
                break;
        }
    }

    @Override
    public void cleanFields() {
        emailLoginEditText.setText("");
        passwordLoginEditText.setText("");
    }

    @Override
    public String[] getFieldsValues() {
        String[] nameAndPassword = {    emailLoginEditText.getText().toString(),
                                        passwordLoginEditText.getText().toString()};
        return nameAndPassword;
    }

    @Override
    public void showMessage(String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG ).show();
    }

    private void startMVP() {
        this.loginPresenter = new LoginPresenter( this );
    }
}
