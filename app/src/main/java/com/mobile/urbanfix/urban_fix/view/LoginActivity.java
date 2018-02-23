package com.mobile.urbanfix.urban_fix.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.LoginPresenter;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, MainMVP.IView {

    private EditText emailLoginEditText, passwordLoginEditText;
    private TextView forgotPasswordTextView;
    private CheckBox rememberLoginCheckBox;
    private Button loginButton, registerButton;
    private MainMVP.ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startMVP();
        emailLoginEditText = (EditText) findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = (EditText) findViewById(R.id.passwordLoginEditText);
        rememberLoginCheckBox = (CheckBox) findViewById(R.id.rememberLoginCheckBox);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener( this );
        forgotPasswordTextView = (TextView) findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener( this );
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener( this );
        loginPresenter.fillFields(emailLoginEditText, passwordLoginEditText, rememberLoginCheckBox,
                this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch( id ) {
            case R.id.loginButton: {
                loginPresenter.doLogin( emailLoginEditText.getText().toString(),
                        passwordLoginEditText.getText().toString(),
                        rememberLoginCheckBox.isChecked(), this);
                break;
            }
            case R.id.registerButton:
                loginPresenter.openRegisterView();
                break;
            case R.id.forgotPasswordTextView:
                loginPresenter.openForgotPasswordView();
                break;
        }
    }


    @Override
    public void showMessage(String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG ).show();
    }

    private void startMVP() {
        this.loginPresenter = new LoginPresenter( this );
    }
}
