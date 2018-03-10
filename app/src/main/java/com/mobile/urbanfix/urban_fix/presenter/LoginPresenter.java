package com.mobile.urbanfix.urban_fix.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.ForgotPasswordActivity;
import com.mobile.urbanfix.urban_fix.view.LoginActivity;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.view.RegisterActivity;

public class LoginPresenter implements MainMVP.ILoginPresenter,
        MainMVP.ICallbackPresenter {

    private MainMVP.ILoginView view;
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "passwd";
    public static final String REMEMBER_KEY = "remember";
    public ProgressDialog dialog;

    public LoginPresenter(MainMVP.ILoginView view) {
        this.view = view;
    }

    @Override
    public void doLogin(String email, String password, boolean rememberUser, AppCompatActivity activity) {
        Log.i("Script", "Verificando campos");
        if(verifyValues(email, password)) {
            Log.i("Script", "Campos válidos. Tentando realizar Login");
            User.doLogin( email, password,  activity, this );
            dialog = new ProgressDialog(activity);
            dialog.setMessage(activity.getString(R.string.login_progressdialog_message));
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            if(rememberUser)
                saveUser(email, password, rememberUser, activity);
        }
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
    public void openForgotPasswordView(AppCompatActivity activity) {
        Intent i = new Intent(activity, ForgotPasswordActivity.class );
        activity.startActivity(i);
    }

    @Override
    public void openRegisterView(AppCompatActivity activity) {
        Intent i = new Intent( activity, RegisterActivity.class );
        activity.startActivity(i);
    }

    @Override
    public void openMainView(AppCompatActivity activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
    }

    @Override
    public void onSuccessTask(Constants task, Object user) {
        if(task == Constants.FIND_USER) {
            Log.i("Script", "Usuário carregado do banco de dados. Abrindo tela principal");
            User findedUser = (User) user;
            User.setInstance(findedUser);
            Log.i("Script", "Inforamações do usuário: " + User.getInstance().toString());
            Context context = view.getContext();
            dialog.dismiss();
            view.showMessage(context.getString(R.string.login_welcome_user));
            openMainView((AppCompatActivity) view.getContext());
            view.finishView();
        } else if(task == Constants.DO_LOGIN) {
            Log.i("Script","Login realizado com sucesso");
            Context context = view.getContext();
            view.showMessage(context.getString(R.string.login_success));
            Log.i("Script", "Buscando informações do usuário");
            User u = User.getInstance();
            u.find(ConnectionFactory.getFirebaseUser().getUid(),this);
        }
    }

    @Override
    public void onFailedTask(Constants task) {
        if(task == Constants.FIND_USER) {
            Log.i("Script", "Falha ao encontrar o usuário no banco de dados.");
            Context context = view.getContext();
            view.showMessage(context.getString(R.string.login_failed_find_on_database));
        } else if(task == Constants.DO_LOGIN) {
            Log.i("Script","Falha ao realizar Login...");
            Context context = view.getContext();
            dialog.dismiss();
            view.showMessage(context.getString(R.string.login_failed_login));
        }
    }

    private boolean verifyValues( String email, String password ) {
        if(email.trim().isEmpty()) {
            view.emailIsEmpty();
            return false;
        } else if(password.trim().isEmpty()) {
            view.passwordIsEmpty();
            return false;
        }
        return true;
    }

    private void saveUser(String email, String password, boolean remember, AppCompatActivity activity) {
        Log.i("Script", "Salvando informações do usuário");
        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_KEY, email);
        editor.putString(PASSWORD_KEY, password);
        editor.putBoolean(REMEMBER_KEY, remember);
        editor.commit();
    }
}
