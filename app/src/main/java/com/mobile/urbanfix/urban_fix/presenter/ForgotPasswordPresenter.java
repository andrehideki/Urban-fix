package com.mobile.urbanfix.urban_fix.presenter;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.ForgotPasswordActivity;

public class ForgotPasswordPresenter implements MainMVP.IForgotPasswordPresenter,
                                                MainMVP.ICallbackPresenter {

    private MainMVP.IForgotPasswordView view;

    public ForgotPasswordPresenter(MainMVP.IForgotPasswordView view) {
        this.view = view;
    }
    @Override
    public void sendRecoverPasswordMessage() {
        String email = view.getEmail();
        if(email!=null) {
            if (!email.isEmpty()) {
                User.sendPassword(email, this);
            } else {
                view.showMessage(((ForgotPasswordActivity) view).
                        getString(R.string.forgot_password_email_is_empty));
            }
        }
    }

    @Override
    public void onSuccessTask() {
        view.showMessage(((ForgotPasswordActivity) view).
                getString(R.string.forgot_password_msg_sended));
    }

    @Override
    public void onFailedTask() {
        view.cleanFields();
        view.showMessage(((ForgotPasswordActivity) view).
                getString(R.string.forgot_password_email_failed));
    }
}
