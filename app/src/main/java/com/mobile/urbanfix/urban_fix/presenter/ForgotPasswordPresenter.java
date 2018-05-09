package com.mobile.urbanfix.urban_fix.presenter;

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.ForgotPasswordActivity;

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
            user.sendPassword(new User.SendPasswordCallback() {
                @Override
                public void onSendSuccess() {
                    view.showSuccessMessage();
                }

                @Override
                public void onFailedToSend() {
                    view.showFailedMessage();
                }
            });
        }
    }

}
