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

    interface IView {
        void showMessage(String msg);
        Context getContext();
        void finishView();
    }


    /* *
    * [ Login contracts ]
    * */
    interface ILoginPresenter {
        String EMAIL_KEY = "email";
        String PASSWORD_KEY = "passwd";
        String REMEMBER_KEY = "remember";
        void onCreateLoginActivity();
        void doLogin(String email, String password, boolean remember);
        void onForgotPasswordButtonClicked();
        void onRegisterButtonClicked();
    }

    interface ILoginView extends IView {
        void emailIsEmpty();
        void passwordIsEmpty();
        void openForgotPasswordView();
        void openRegisterView();
        void openMainView();
        void fillUserInformations(String email, String password, boolean rememberUser);
    }




    /* *
     * [ ForgotPassoword contracts ]
     * */
    interface IForgotPasswordView extends IView{
        void showFailedMessage();
        void showSuccessMessage();
    }

    interface IForgotPasswordPresenter {
        void sendRecoverPasswordMessage(String email);
    }



    /* *
     * [ Register contracts ]
     * */
    interface IRegisterView extends IView {
        void showCreatingUserDialog();
        void showInsertingUserIntoDBDialog();
        void showThanksDialog();
        void onInsertingUserIntoDBFailed();
        void finishDialog();
    }

    interface IRegisterPresenter {
        void registerUser(String name, String lastName, String cpf, String birthday, String email, String password);

    }



    /* *
     * [ Alert contracts ]
     * */
    interface IAlertPresenter {
        int LOW = 6, MODERATE =12, CRITICAL =18;
        void initAlert();
        void setUrgency(int urgency);
        void setKindOfProblem(int position, String kindOfProblem);
        void setAddress(int position, String address);
        void setDescription(String description);
        void startGPS();
        void dispachTakePhotoIntent();
        void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void cancelAlert(Fragment fragment);
        void finishAlert(Activity activity);
    }

    interface IAlertView extends IView {
        void setupPhotoImageView(Bitmap bitmap);
        void onAddressHasBeenFetched(ArrayList<String> addressesList);
        void changeUrgencyStatus(String urgencyStatus);
        void showDescriptionError();
        void startForResult(Intent i, int request);
    }




    /* *
     * [ My Alerts contracts ]
     * */
    interface IPersonAlertsPresenter {
        void configureUserInformations();
        ArrayList<Problem> getPersonAlerts();
        void onItemClicked(int postion);
    }

    interface IPersonAlertsView {
        void setUserName(String userName);
        void setNumberOfAlerts(int numberOfAlerts);
        void onNewAlertFinded(int position);
        void onAlertRemoved(int position);
        void showAlertDialog();
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
        void onStart();
        void tryToLoadComments();
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
        String getProjectString(int id);
        void onCommentsLoaded(List<Comment> comments);
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


}
