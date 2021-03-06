package com.mobile.urbanfix.urban_fix.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.LoginPresenter;
import com.mobile.urbanfix.urban_fix.MainMVP;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, MainMVP.ILoginView {

    private TextInputEditText emailLoginEditText, passwordLoginEditText;
    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;
    private TextView forgotPasswordTextView;
    private CheckBox rememberLoginCheckBox;
    private Button loginButton, registerButton;
    private MainMVP.ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startMVP();
        emailLoginEditText =  findViewById(R.id.emailLoginEditText);
        emailLoginEditText.setOnClickListener(this);
        passwordLoginEditText =  findViewById(R.id.passwordLoginEditText);
        passwordLoginEditText.setOnClickListener(this);
        rememberLoginCheckBox =  findViewById(R.id.rememberLoginCheckBox);
        loginButton =  findViewById(R.id.loginButton);
        loginButton.setOnClickListener( this );
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener( this );
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener( this );

        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        loginPresenter.onCreateLoginActivity();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch( id ) {
            case R.id.loginButton: {
                loginPresenter.doLogin( emailLoginEditText.getText().toString(),
                        passwordLoginEditText.getText().toString(),
                        rememberLoginCheckBox.isChecked());
                break;
            }
            case R.id.registerButton:
                loginPresenter.onRegisterButtonClicked();
                break;
            case R.id.forgotPasswordTextView:
                loginPresenter.onForgotPasswordButtonClicked();
                break;
            case R.id.emailLoginEditText: {
                emailTextInputLayout.setErrorEnabled(false);
                break;
            }
            case R.id.passwordLoginEditText: {
                passwordTextInputLayout.setErrorEnabled(false);
                break;
            }
        }
    }

    private void startMVP() {
        this.loginPresenter = new LoginPresenter( this );
    }

    @Override
    public void emailIsEmpty() {
        emailTextInputLayout.setErrorEnabled(true);
        emailTextInputLayout.setError(getString(R.string.login_email_is_empty));
    }

    @Override
    public void passwordIsEmpty() {
        passwordTextInputLayout.setErrorEnabled(true);
        passwordTextInputLayout.setError(getString(R.string.login_password_is_empty));
    }

    @Override
    public void openForgotPasswordView() {
        Intent i = new Intent(this, ForgotPasswordActivity.class );
        startActivity(i);
    }

    @Override
    public void openRegisterView() {
        Intent i = new Intent(this, RegisterActivity.class );
        startActivity(i);
    }

    @Override
    public void openMainView() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void fillUserInformations(String email, String password, boolean rememberUser) {
        emailLoginEditText.setText(email);
        passwordLoginEditText.setText(password);
        rememberLoginCheckBox.setChecked(rememberUser);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishView() {
        finish();
    }
}
