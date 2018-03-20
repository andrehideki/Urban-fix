package com.mobile.urbanfix.urban_fix.presenter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.Problem;

public class ProblemDialogPresenter implements MainMVP.IProblemDialogPresenter, MainMVP.ICallbackPresenter {

    private MainMVP.IProblemDialogView view;
    public final int ONE_MEGABYTE = 1024 * 1024;

    public ProblemDialogPresenter(MainMVP.IProblemDialogView view) {
        this.view = view;
    }

    @Override
    public void setInformations(Problem problem, TextView kindOfProblemTextView, TextView dateTextView,
                                TextView statusTextView, TextView locationTextView,
                                TextView descriptionTextView, TextView urgencyTextView,
                                ImageView problemPhotoImageView) {
        kindOfProblemTextView.setText(problem.getKindOfProblem());
        dateTextView.setText(problem.getDate());
        statusTextView.setText(problem.getStatus());
        locationTextView.setText(problem.getLocation());
        descriptionTextView.setText(problem.getDescription());
        urgencyTextView.setText(problem.getUrgency() + "");
        getProblemPhotoAssync(problem, this);
    }

    @Override
    public void onSuccessTask(Constants task, Object object) {
        Log.i("Script", "Imagem baixada com sucesso");
        if(task == Constants.DOWNLOAD_IMAGE) {
            byte[] byteArray = (byte[]) object;
            Bitmap photoBitMap = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
            view.setImageBitmap(photoBitMap);
        }
    }

    @Override
    public void onFailedTask(Constants task) {
        Log.e("Script","Ocorreu um erro ao tentar baixar a imagem" );
    }

    private void getProblemPhotoAssync(Problem problem, final MainMVP.ICallbackPresenter callback) {
        final String photoId = problem.getPhotoId();
            new Thread() {
                public void run() {
                    StorageReference storageReference = ConnectionFactory.getFirebaseStorageReference();
                    storageReference.child(photoId).getBytes(ONE_MEGABYTE).
                            addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    callback.onSuccessTask(Constants.DOWNLOAD_IMAGE, bytes);
                                }
                            }).
                            addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Script", e.getMessage());
                                    callback.onFailedTask(Constants.DOWNLOAD_IMAGE);
                                }
                            });
                }
            }.start();
    }
}
