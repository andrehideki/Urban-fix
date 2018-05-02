package com.mobile.urbanfix.urban_fix.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.DAO;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.ForgotPasswordActivity;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.view.RegisterActivity;


public class LoginPresenter implements MainMVP.ILoginPresenter {

    private MainMVP.ILoginView view;
    private Person person;
    private User user;
    public ProgressDialog dialog;


    public LoginPresenter(MainMVP.ILoginView view) {
        this.view = view;
        this.person = Person.getInstance();
        this.user = User.getInstance();
    }

    @Override
    public void doLogin(final String email, final String password,
                        final boolean rememberUser, final AppCompatActivity activity) {
        Log.i("Script", "Verificando campos");
        if(verifyValues(email, password)) {

            final Context context = view.getContext();
            Log.i("Script", "Campos válidos. Tentando realizar Login");
            dialog = new ProgressDialog(context);
            dialog.setMessage(activity.getString(R.string.login_progressdialog_message));
            dialog.show();

            user.setEmail(email);
            user.setPassword(password);

            user.doLogin((Activity) context, new User.LoginUserCallback() {
                @Override
                public void onLoginSuccess() {
                    Log.i("Script","Login realizado com sucesso");
                    dialog.setMessage(context.getString(R.string.login_finding_user));
                    tryToFindPerson();
                    if(rememberUser) saveUser(email, password, rememberUser, (AppCompatActivity) context);
                }

                @Override
                public void onFailedLogin() {
                    Log.i("Script","Falha ao realizar Login...");
                    view.showMessage(context.getString(R.string.login_failed_login));
                    dialog.cancel();
                }
            });
        }
    }

    private void tryToFindPerson() {
        person.find(user.getUID(), new DAO.DAOCallback<Person>() {
            @Override
            public void onObjectFinded(Person result) {
                Context context = view.getContext();
                person.setName(result.getName());
                person.setCpf(result.getCpf());
                person.setBirthDate(result.getBirthDate());
                person.setnAlertsDone(result.getnAlertsDone());
                view.showMessage(context.getString(R.string.login_welcome_user));
                dialog.cancel();
                openMainView(context);

                Log.i("Script", person.toString());
            }

            @Override
            public void onObjectInserted() {

            }

            @Override
            public void onObjectUpdated() {

            }

            @Override
            public void onObjectDeleted() {

            }

            @Override
            public void onFailedTask() {
                view.showMessage(view.getContext().getString(R.string.login_failed_find_on_database));
                dialog.cancel();
            }
        });
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
    public void openMainView(Context activity) {
        Intent i = new Intent(activity, MainActivity.class);
        activity.startActivity(i);
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
