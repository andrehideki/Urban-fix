package com.mobile.urbanfix.urban_fix.presenter;

import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.User;

public class ForgotPasswordPresenter implements MainMVP.IForgotPasswordPresenter {

    private MainMVP.IForgotPasswordView view;

    public ForgotPasswordPresenter(MainMVP.IForgotPasswordView view) {
        this.view = view;
    }

    @Override
    public void sendRecoverPasswordMessage(final String email) {
        if(!email.isEmpty()) {
            User user = User.getInstance();
            user.setEmail(email);
            user.sendPassword(new Callback.SimpleAsync<Void>() {
                @Override
                public void onTaskDone(Void result, boolean success) {
                    if(success)
                        view.showSuccessMessage();
                    else
                        view.showFailedMessage();
                }
            });
        }
    }

}
