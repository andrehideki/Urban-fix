package com.mobile.urbanfix.urban_fix.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.ForgotPasswordActivity;
import com.mobile.urbanfix.urban_fix.view.LoginActivity;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.view.RegisterActivity;

public class LoginPresenter implements MainMVP.ILoginPresenter, MainMVP.ICallbackPresenter {

    private MainMVP.IView view;
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "passwd";
    public static final String REMEMBER_KEY = "remember";
    public ProgressDialog dialog;

    public LoginPresenter(MainMVP.IView view ) {
        this.view = view;
    }

    @Override
    public void doLogin(String email, String password, boolean rememberUser, AppCompatActivity activity) {

        if(verifyValues(email, password)) {
            User.doLogin( email, password,  activity, this );
            dialog = new ProgressDialog(activity);
            dialog.setMessage(activity.getString(R.string.login_progressdialog_message));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            if(rememberUser)
                saveUser(email, password, rememberUser, activity);
        }
        else
            view.showMessage(((LoginActivity) view).getString(R.string.login_empty_fields));
    }

    @Override
    public void fillFields(EditText emailEditText, EditText passwordEditText, CheckBox rememberUserCheckBox,
                           AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        emailEditText.setText(sharedPreferences.getString(EMAIL_KEY, ""));
        passwordEditText.setText(sharedPreferences.getString(PASSWORD_KEY, ""));
        rememberUserCheckBox.setChecked(sharedPreferences.getBoolean(REMEMBER_KEY, false));
    }

    @Override
    public void openForgotPasswordView() {
        Intent i = new Intent( ((LoginActivity) view), ForgotPasswordActivity.class );
        ((LoginActivity) view).startActivity(i);
    }

    @Override
    public void openRegisterView() {
        Intent i = new Intent( ((LoginActivity) view ), RegisterActivity.class );
        ( (LoginActivity) view ).startActivity(i);
    }

    @Override
    public void openMainView() {
        Intent i = new Intent(( (LoginActivity) view), MainActivity.class);
        ( (LoginActivity) view).startActivity(i);
    }

    @Override
    public void onSuccessTask() {
        dialog.dismiss();
        openMainView();
    }

    @Override
    public void onFailedTask() {
        dialog.dismiss();
        view.showMessage(( (LoginActivity) view).getString(R.string.login_failed_login));
    }

    private boolean verifyValues( String email, String password ) {
        return !email.trim().isEmpty() && !password.trim().isEmpty();
    }

    private void saveUser(String email, String password, boolean remember, AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_KEY, email);
        editor.putString(PASSWORD_KEY, password);
        editor.putBoolean(REMEMBER_KEY, remember);
        editor.commit();
    }

}
