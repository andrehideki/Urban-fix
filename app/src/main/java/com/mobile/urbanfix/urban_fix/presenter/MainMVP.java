package com.mobile.urbanfix.urban_fix.presenter;


public interface MainMVP {

    interface ILoginPresenter {
        void doLogin();
        void startForgotPasswordView();
        void startRegisterView();
        void startMainView();
    }
    interface ILoginCallbackPresenter {
       void onSuccessLogin();
       void onFailedLogin();
    }


    interface ILoginView {
        void cleanFields();
        String[] getFieldsValues();
        void showMessage( String msg );
    }

    interface IUserLoginModel {
        void login( String email, String password );
    }


}
