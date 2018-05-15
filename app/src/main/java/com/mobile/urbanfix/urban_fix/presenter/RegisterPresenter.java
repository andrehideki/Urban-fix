package com.mobile.urbanfix.urban_fix.presenter;

import android.app.ProgressDialog;

import com.google.firebase.auth.FirebaseUser;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.User;
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
            view.showCreatingUserDialog();

            user = createUserWithValues(email, password);
            person = createPersonWithValues(name + " " + lastName, cpf, birthday);

            tryToRegisterEmail();
        } else
            view.showMessage(( (RegisterActivity) view).getString(R.string.register_fields_empty));
    }

    private void tryToRegisterEmail() {
        user.register(new Callback.SimpleAsync<Void>() {
            @Override
            public void onTaskDone(Void result, boolean success) {
                if(success) {
                    view.showInsertingUserIntoDBDialog();
                    FirebaseUser fUser = ConnectionFactory.getFirebaseUser();
                    User u = User.getInstance();
                    u.setUID(fUser.getUid());
                    tryToInsertPerson();
                } else {
                    view.onInsertingUserIntoDBFailed();
                }
            }
        });
    }

    private void tryToInsertPerson() {
        person.insert(person, new Callback.SimpleAsync<Person>() {
            @Override
            public void onTaskDone(Person result, boolean success) {
                if(success) {
                    view.finishDialog();
                    view.showThanksDialog();
                } else {
                    view.onInsertingUserIntoDBFailed();
                    dialog.cancel();
                }
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
        return i == values.length? true : false;
    }
}
