package com.mobile.urbanfix.urban_fix.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.Serializable;

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
        void initAlert(Context context);
        void setupSpinner(Activity activity, Spinner spinner);
        void setUrgency(int urgency);
        void setKindOfProblem(int position, String kindOfProblem);
        void setDescription(String description, TextInputLayout descriptionTextInputLayout, Context context);
        void startGPS(Context context);
        void dispachTakePhotoIntent(Fragment fragment, MainMVP.IAlertView view);
        void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void cancelAlert(Fragment fragment);
        void finishAlert();
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

    interface ILoginView extends IView {
        void emailIsEmpty();
        void passwordIsEmpty();
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
        void onLocationDefined(String location);
    }

    interface IOnGpsPickupUserLocation extends Serializable {
        void onFailedGetUserLocation(Context context);
        void onSuccessGetUserLocation(String location);
    }

}
