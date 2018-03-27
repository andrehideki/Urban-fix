package com.mobile.urbanfix.urban_fix.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.io.Serializable;
import java.util.ArrayList;

public interface MainMVP {

    interface ILoginPresenter {
        void doLogin(String email, String password, boolean remember, AppCompatActivity activity);
        void fillFields(EditText emailEditText, EditText passwordEditText, CheckBox
                rememberUserCheckBox, AppCompatActivity activity);
        void openForgotPasswordView(AppCompatActivity activity);
        void openRegisterView(AppCompatActivity activity);
        void openMainView(AppCompatActivity activity);
    }

    interface IForgotPasswordPresenter {
        void sendRecoverPasswordMessage();
    }

    interface IRegisterPresenter {
        void registerUser();
        void openMainView();
    }

    interface ICallbackPresenter {
        void onSuccessTask(Constants task, Object object);
        void onFailedTask(Constants task);
    }

    interface ICallbackListOfAlerts {
        void onListOfAlertsChanged();
    }

    interface IAlertPresenter {
        void initAlert(Context context);
        void setUrgency(int urgency);
        void setKindOfProblem(int position, String kindOfProblem);
        void setAddress(int position, String address);
        void setDescription(String description, TextInputLayout descriptionTextInputLayout, Context context);
        void startGPS(Context context);
        void dispachTakePhotoIntent(Fragment fragment, MainMVP.IAlertView view);
        void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void cancelAlert(Fragment fragment);
        void finishAlert(Activity activity);
    }

    interface IMyAlertsPresenter {
        void getUserAlerts();
        void configureUserInformations();
        void setupMyAlertsList(RecyclerView myAlerts, Context context);
    }

    interface IMapsPresenter {
        void openAlertFragment(AppCompatActivity activity);
        void getLocationPermissions(Activity activity);
        void startLocationListener(Activity activity);
        void stopLocationListener();
        void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults);
        void initMap();
        void loadAlertsOnMap();
    }

    interface IMainPresenter {
        void initializeUser();
        void openMapView(AppCompatActivity activity);
        void openAlertView(AppCompatActivity activity);
        void openNoticesView(AppCompatActivity activity);
        void openStatisticsView(AppCompatActivity activity);
        void doLogoff(AppCompatActivity activity);
    }

    interface IProblemDialogPresenter {
        void setInformations(Problem problem, TextView kindOfProblemTextView, TextView dateTextView,
                             TextView statusTextView, TextView locationTextView, TextView descriptionTextView,
                             TextView urgencyTextView, ImageView problemPhotoImageView);
    }

    interface IView {
        void showMessage(String msg);
        Context getContext();
        void finishView();
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

    interface IAlertView extends IView {
        void setupPhotoImageView(Bitmap bitmap);
        void onLocationDefined(String location);
        void onAddressHasBeenFetched(ArrayList<String> addressesList);
    }

    interface IMyAlertsView {
        void setUserName(String userName);
        void setNumberOfAlerts(int numberOfAlerts);
    }

    interface IProblemDialogView {
        void setImageBitmap(Bitmap bitmap);
    }

    interface IOnGpsPickupUserLocationAndPossibleAddresses extends Serializable {
        void onFailedGetUserLocation(Context context);
        void onSuccessGetUserLocation(String location);
        void onSuccessGetUserAddresses(ArrayList<String> possibleAddressesList);
        void onFailedGetUserAddresses(Context context);
    }

    interface IMapsView extends IView {
        FragmentManager getCurrentFragmentManager();
    }

}
