package com.mobile.urbanfix.urban_fix.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public interface MainMVP {

    interface ILoginPresenter {
        void doLogin(String email, String password, boolean remember, AppCompatActivity activity);
        void fillFields(EditText emailEditText, EditText passwordEditText, CheckBox
                rememberUserCheckBox, AppCompatActivity activity);
        void openForgotPasswordView();
        void openRegisterView();
        void openMainView();
    }

    interface IForgotPasswordPresenter {
        void sendRecoverPasswordMessage();
    }

    interface IRegisterPresenter {
        void registerUser();
        void openMainView();
    }

    interface ICallbackPresenter {
        void onSuccessTask();
        void onFailedTask();
    }

    interface IAlertPresenter {
        void initAlert();
        void setupSpinner(Activity activity, Spinner spinner);
        void dispachTakePhotoIntent(Fragment fragment, MainMVP.IAlertView view);
        void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void cancelAlert(Fragment fragment);
        void finishAlert(String descriptio, int urgency);
    }

    interface IMainPresenter {
        void openMapView(AppCompatActivity activity);
        void openAccountView(AppCompatActivity activity);
        void openAlertView(AppCompatActivity activity);
        void openNoticesView(AppCompatActivity activity);
        void openStatisticsView(AppCompatActivity activity);
        void doLogoff(AppCompatActivity activity);
    }

    interface IView {
        void showMessage(String msg);
    }

    interface IForgotPasswordView extends IView{
        void cleanFields();
        String getEmail();
    }

    interface IRegisterView extends IView{
        String[] getFieldsValues();
    }

    interface IMainView extends IView {

    }

    interface IAlertView {
        void setupPhotoImageView(Bitmap bitmap);
    }

}
