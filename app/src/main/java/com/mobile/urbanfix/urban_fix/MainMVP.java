package com.mobile.urbanfix.urban_fix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mobile.urbanfix.urban_fix.model.Comment;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface MainMVP {

    /* *
    * [ Login contracts ]
    * */
    interface ILoginPresenter {
        String EMAIL_KEY = "email";
        String PASSWORD_KEY = "passwd";
        String REMEMBER_KEY = "remember";
        void doLogin(String email, String password, boolean remember, AppCompatActivity activity);
        void fillFields(EditText emailEditText, EditText passwordEditText, CheckBox
                rememberUserCheckBox, AppCompatActivity activity);//Refatorar
        void openForgotPasswordView(AppCompatActivity activity);//Refatorar
        void openRegisterView(AppCompatActivity activity);//Refatorar
        void openMainView(Context activity);//Refatorar
    }

    interface ILoginView extends IView {
        void emailIsEmpty();
        void passwordIsEmpty();
    }





    interface IForgotPasswordPresenter {
        void sendRecoverPasswordMessage(String email);
    }

    interface IRegisterPresenter {
        void registerUser(String name, String lastName, String cpf, String birthday, String email, String password);
        void showThanksDialog();
    }

    interface ICallbackPresenter {
        void onSuccessTask(Constants task, Object object);
        void onFailedTask(Constants task);
    }

    interface ICallbackListOfAlerts {
        void onListOfAlertsChanged();
    }



    interface IAlertPresenter {
        int LOW = 6, MODERATE =12, CRITICAL =18;
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

    interface IAlertView extends IView {
        void setupPhotoImageView(Bitmap bitmap);
        void onAddressHasBeenFetched(ArrayList<String> addressesList);
        void changeUrgencyStatus(String urgencyStatus);
    }


    interface IMyAlertsPresenter {
        void getUserAlerts();
        void configureUserInformations();
        void setupMyAlertsList(RecyclerView myAlerts, Context context);
    }



    /* *
     * [ Maps contracts ]
     * */
    interface IMapsPresenter {
        void openAlertFragment();
        void getLocationPermissions(Activity activity);
        void startLocationListener(Activity activity);
        void stopLocationListener();
        void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults);
        void initMap();
        void loadAlertsOnMap();
    }

    interface IMapsView extends IView {
        FragmentManager getCurrentFragmentManager();
        void showMapsView();
    }



    /*
    * Problem dialog contracts
    * */
    interface IProblemDialogPresenter {
        void setInformations(Problem problem, TextView kindOfProblemTextView, TextView dateTextView,
                             TextView statusTextView, TextView locationTextView, TextView descriptionTextView,
                             TextView urgencyTextView, ImageView problemPhotoImageView);
        void loadComments();
        void onInsertCommentClicked();
        void onCancelCommentClicked();
        void onCommentClicked(String comment);
    }

    interface IProblemDialogView {
        void setAlertInformations(String kindOfProblem, String date, String status, String address,
                                  String description, String urgency);
        void setAlertPhoto(Bitmap bitmap);
        void showDialogComment();
        void closeDialogComment();
        void onCommentInserted(int position);
        void onCommentsLoaded(List<Comment> comments);
    }



    interface IView {
        void showMessage(String msg);
        Context getContext();
        void finishView();
    }

    interface IForgotPasswordView extends IView{
        void cleanFields();
        String getEmail();
    }

    interface IRegisterView extends IView{
        String[] getFieldsValues();
    }

    /* *
     * [ Main contracts ]
     * */
    interface IMainPresenter {
        void openMapView();
        void openAlertView();
        void openNoticesView();
        void onBackStackChagend(int cont);
        void onNavigationItemSelected(int itemId);
        void onBackPressed();
        void doLogoff();
    }

    interface IMainView extends IView {
        void showMapView();
        void showAlertView();
        void showNoticesView();
        void showLogoffDialog();
        void closeCurrentView();
    }



    interface IMyAlertsView {
        void setUserName(String userName);
        void setNumberOfAlerts(int numberOfAlerts);
    }



    interface IOnGpsPickupUserLocationAndPossibleAddresses extends Serializable {
        void onFailedGetUserLocation(Context context);
        void onSuccessGetUserLocation(LatLng latLng);
        void onSuccessGetUserAddresses(ArrayList<String> possibleAddressesList);
        void onFailedGetUserAddresses(Context context);
    }



}
