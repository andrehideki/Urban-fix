package com.mobile.urbanfix.urban_fix.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.User;


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
    public void onCreateLoginActivity() {
        SharedPreferences preferences = ((Activity)view.getContext()).getPreferences(Context.MODE_PRIVATE);
        String email = preferences.getString(EMAIL_KEY, "");
        String password = preferences.getString(PASSWORD_KEY, "");
        boolean rememberUser = preferences.getBoolean(REMEMBER_KEY, false);

        view.fillUserInformations(email, password, rememberUser);
    }

    @Override
    public void doLogin(final String email, final String password,
                        final boolean rememberUser) {
        Log.i("Script", "Verificando campos");
        if(verifyValues(email, password)) {

            final Context context = view.getContext();
            Log.i("Script", "Campos válidos. Tentando realizar Login");
            dialog = new ProgressDialog(context);
            dialog.setMessage(view.getContext().getString(R.string.login_progressdialog_message));
            dialog.show();

            user.setEmail(email);
            user.setPassword(password);

            user.doLogin((Activity) context, new Callback.SimpleAsync<Void>() {
                @Override
                public void onTaskDone(Void result, boolean success) {
                    if(success) {
                        Log.i("Script","Login realizado com sucesso");
                        dialog.setMessage(context.getString(R.string.login_finding_user));
                        tryToFindPerson();
                        if(rememberUser) saveUser(email, password, rememberUser, (AppCompatActivity) context);
                    } else {
                        Log.i("Script","Falha ao realizar Login...");
                        view.showMessage(context.getString(R.string.login_failed_login));
                        dialog.cancel();
                    }
                }
            });
        }
    }

    private void tryToFindPerson() {
        person.find(user.getUID(), new Callback.SimpleAsync<Person>() {
            @Override
            public void onTaskDone(Person result, boolean success) {
                if(success) {
                    Context context = view.getContext();
                    person.setName(result.getName());
                    person.setCpf(result.getCpf());
                    person.setBirthDate(result.getBirthDate());
                    person.setnAlertsDone(result.getnAlertsDone());
                    view.showMessage(context.getString(R.string.login_welcome_user));
                    dialog.cancel();

                    view.openMainView();

                    Log.i("Script", person.toString());
                } else {
                    view.showMessage(view.getContext().getString(R.string.login_failed_find_on_database));
                    dialog.cancel();
                }
            }
        });
    }

    @Override
    public void onForgotPasswordButtonClicked() {
        view.openForgotPasswordView();
    }

    @Override
    public void onRegisterButtonClicked() {
        view.openRegisterView();
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
