package com.mobile.urbanfix.urban_fix.presenter;

import android.content.Intent;
import android.util.Log;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.ForgotPasswordActivity;
import com.mobile.urbanfix.urban_fix.view.LoginActivity;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.view.RegisterActivity;

public class LoginPresenter implements MainMVP.ILoginPresenter, MainMVP.ILoginCallbackPresenter {

    private MainMVP.ILoginView view;

    public LoginPresenter( MainMVP.ILoginView view ) {
        this.view = view;
    }

    @Override
    public void doLogin() {
        String[] values = view.getFieldsValues();
        String email = values[0];
        String password = values[1];

        if( verifyValues(email, password) ) {
            User.doLogin( email, password,  ( (LoginActivity) view ), this );
        }
        else
            view.showMessage(((LoginActivity) view).getString(R.string.login_empty_fields));
    }


    @Override
    public void startForgotPasswordView() {
        Intent i = new Intent( ((LoginActivity) view), ForgotPasswordActivity.class );
        ((LoginActivity) view).startActivity(i);
    }

    @Override
    public void startRegisterView() {
        Intent i = new Intent( ((LoginActivity) view ), RegisterActivity.class );
        ( (LoginActivity) view ).startActivity(i);
    }

    @Override
    public void startMainView() {
        Intent i = new Intent(( (LoginActivity) view), MainActivity.class);
        ( (LoginActivity) view).startActivity(i);
    }

    private boolean verifyValues( String email, String password ) {
        return !email.trim().isEmpty() && !password.trim().isEmpty();
    }

    @Override
    public void onSuccessLogin() {
        startMainView();
        ( (LoginActivity) view).finish();
    }

    @Override
    public void onFailedLogin() {
        view.showMessage(( (LoginActivity) view).getString(R.string.login_failed_login));
    }
}
