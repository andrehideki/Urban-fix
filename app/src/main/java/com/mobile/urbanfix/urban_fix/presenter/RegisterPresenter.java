package com.mobile.urbanfix.urban_fix.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.view.RegisterActivity;

public class RegisterPresenter implements   MainMVP.IRegisterPresenter,
                                            MainMVP.ICallbackPresenter{

    private MainMVP.IRegisterView view;
    private User user;

    public RegisterPresenter(MainMVP.IRegisterView view) {
        this.view = view;
    }

    @Override
    public void registerUser() {
        String values[] = view.getFieldsValues();
        if(verifyFields(values)) {
            User user = createUserWithValues(values);
            user.insert(user, this);
        } else
            view.showMessage(( (RegisterActivity) view).getString(R.string.register_fields_empty));
    }

    private User createUserWithValues(String values[]) {
        /*
        * 0 - nome
        * 1 - ultimo nome
        * 2 - cpf
        * 3 - aniversário
        * 4 - email
        * 5 - senha
        * */
        user = User.getInstance();
        user.setName(values[0] + " " + values[1]);
        user.setCpf(values[2]);
        user.setBirthDate(values[3]);
        user.setEmail(values[4]);
        user.setPassword(values[5]);

        return user;
    }

    private boolean verifyFields(String values[]) {
        int i=0;
        for(;i<values.length && !(values[i].isEmpty());i++);
        if(i == values.length)
            return true;
        return false;
    }

    public void openMainView() {
        Intent intent = new Intent( ((RegisterActivity) view), MainActivity.class);
        ((RegisterActivity) view).startActivity(intent);
    }

    @Override
    public void onSuccessTask(Constants task, Object o) {
        if(task == Constants.NEW_USER) {
            Context context = view.getContext();
            view.showMessage(context.getString(R.string.register_user_succesful));
            Log.i("Script", "Usuário registrado com sucesso. Tentado realizar login...");
            User.doLogin(user.getEmail(), user.getPassword(),
                    (Activity) view.getContext(), this);
        } else if(task == Constants.DO_LOGIN) {
            Log.i("Script","Login realizado com sucesso, abrindo tela principal");
            view.finishView();
            openMainView();
            User u = User.getInstance();
            Log.i("Script",u.toString());
        }
    }

    @Override
    public void onFailedTask(Constants task) {
        if(task == Constants.NEW_USER) {
            view.showMessage(((RegisterActivity) view).getString(R.string.register_failed_create_user));
            Log.i("Script", "Falha ao registrar usuário.");
        } else if(task == Constants.DO_LOGIN) {
            Log.i("Script","Falha ao realizar login...");
            Context context = view.getContext();
            view.showMessage(context.getString(R.string.login_failed_login));
        }
    }
}
