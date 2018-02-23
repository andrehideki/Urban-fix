package com.mobile.urbanfix.urban_fix.presenter;

import android.content.Intent;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.view.RegisterActivity;

public class RegisterPresenter implements   MainMVP.IRegisterPresenter,
                                            MainMVP.ICallbackPresenter {

    private MainMVP.IRegisterView view;

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
        User user = User.getInstance();
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
    public void onSuccessTask() {
        view.showMessage(( (RegisterActivity) view).getString(R.string.register_user_succesful));
        ((RegisterActivity) view).finish();
        openMainView();
    }

    @Override
    public void onFailedTask() {
        view.showMessage(( (RegisterActivity) view).getString(R.string.register_failed_create_user));
    }
}
