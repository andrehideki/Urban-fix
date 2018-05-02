package com.mobile.urbanfix.urban_fix.presenter;

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.MainMVP;
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
    public void sendRecoverPasswordMessage(final String email) {
        if(!email.isEmpty()) {
            User user = User.getInstance();
            user.setEmail(email);
            user.sendPassword(new User.SendPasswordCallback() {
                @Override
                public void onSendSuccess() {
                    view.showMessage(view.getContext().getString(R.string.forgot_password_msg_sended) + email);
                }

                @Override
                public void onFailedToSend() {
                    view.showMessage(view.getContext().getString(R.string.forgot_password_email_failed));
                }
            });
        } else view.showMessage(((ForgotPasswordActivity) view).
                        getString(R.string.forgot_password_email_is_empty));
    }

    @Override
    public void onSuccessTask(Constants task, Object object) {
        if(task == Constants.SEND_PASSWORD) {
            view.showMessage(((ForgotPasswordActivity) view).
                    getString(R.string.forgot_password_msg_sended));
        }
    }

    @Override
    public void onFailedTask(Constants task) {
        if(task == Constants.SEND_PASSWORD) {
            view.cleanFields();
            view.showMessage(((ForgotPasswordActivity) view).
                    getString(R.string.forgot_password_email_failed));
        }
    }
}
