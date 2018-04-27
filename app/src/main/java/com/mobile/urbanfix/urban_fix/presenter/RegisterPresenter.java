package com.mobile.urbanfix.urban_fix.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.DAO;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.view.RegisterActivity;

public class RegisterPresenter implements   MainMVP.IRegisterPresenter {

    private MainMVP.IRegisterView view;
    private User user;
    private Person person;
    private ProgressDialog dialog;

    public RegisterPresenter(MainMVP.IRegisterView view) {
        this.view = view;
    }

    @Override
    public void registerUser(String name, String lastName, String cpf,
                             String birthday, String email, String password) {

        String values[] = {name, lastName, cpf, birthday, email, password};
        if(verifyFields(values)) {
            Context context = view.getContext();
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getString(R.string.register_creating_new_user));
            user = createUserWithValues(email, password);
            person = createPersonWithValues(name + " " + lastName, cpf, birthday);

            tryToRegisterEmail();
        } else
            view.showMessage(( (RegisterActivity) view).getString(R.string.register_fields_empty));
    }

    private void tryToRegisterEmail() {
        user.register(new User.RegisterUserCallback() {
            @Override
            public void onUserRegistered() {
                dialog.setMessage(view.getContext().getString(R.string.register_inserting_user_on_db));
                insertPerson();
            }

            @Override
            public void onFailedToRegisterUser() {
                dialog.cancel();
                view.showMessage(view.getContext().getString(R.string.register_failed_create_user));
            }
        });
    }

    private void insertPerson() {
        person.insert(person, new DAO.DAOCallback<Person>() {
            @Override
            public void onObjectFinded(Person result) {

            }

            @Override
            public void onObjectInserted() {
                view.showMessage(view.getContext().getString(R.string.register_user_succesful));
                dialog.cancel();
                showThanksDialog();
            }

            @Override
            public void onObjectUpdated() {

            }

            @Override
            public void onObjectDeleted() {

            }

            @Override
            public void onFailedTask() {
                view.showMessage(view.getContext().getString(R.string.register_failed_create_user));
                dialog.cancel();
            }
        });
    }

    private User createUserWithValues(String email, String password) {
        User user = User.getInstance();
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }

    private Person createPersonWithValues(String name, String cpf, String birthDate) {
        Person person = Person.getInstance();
        person.setBirthDate(birthDate);
        person.setCpf(cpf);
        person.setnAlertsDone(0);
        person.setName(name);

        return person;
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
    public void showThanksDialog() {
        Context context = view.getContext();
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.register_thanks_for_register))
                .setNeutralButton(context.getString(R.string.register_back_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        view.finishView();
                    }
                })
                .create();

        dialog.show();
    }
}
