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
import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.AlertComment;
import com.mobile.urbanfix.urban_fix.model.Comment;
import com.mobile.urbanfix.urban_fix.model.DAO;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class AlertDialogPresenter implements MainMVP.IProblemDialogPresenter, MainMVP.ICallbackPresenter {

    private MainMVP.IProblemDialogView view;
    private final int ONE_MEGABYTE = 1024 * 1024;
    private Problem problem;
    private AlertComment alertComment;

    public AlertDialogPresenter(MainMVP.IProblemDialogView view) {
        this.view = view;
    }

    @Override
    public void setInformations(Problem problem, TextView kindOfProblemTextView, TextView dateTextView,
                                TextView statusTextView, TextView addressTextView,
                                TextView descriptionTextView, TextView urgencyTextView,
                                ImageView problemPhotoImageView) {
        kindOfProblemTextView.setText(problem.getKindOfProblem());
        dateTextView.setText(problem.getDate());
        statusTextView.setText(problem.getStatus());
        addressTextView.setText(problem.getLocation().getAddress());
        descriptionTextView.setText(problem.getDescription());
        urgencyTextView.setText(problem.getUrgency());

        view.setAlertInformations(problem.getKindOfProblem(), problem.getDate(), problem.getStatus(),
                problem.getLocation().getAddress(), problem.getDescription(), problem.getUrgency());
        this.problem = problem;
        getProblemPhotoAssync(problem, this);
    }

    @Override
    public void loadComments() {
        alertComment = new AlertComment(problem.getId());
        alertComment.find(alertComment.getAlertId(), new DAO.DAOCallback<AlertComment>() {
            @Override
            public void onObjectFinded(AlertComment result) {
                alertComment.setComments(result.getComments());
                view.onCommentsLoaded(alertComment.getComments());
            }

            @Override
            public void onFailedTask() {
                alertComment.setComments(new ArrayList<Comment>(0));
                view.onCommentsLoaded(alertComment.getComments());
                Logger.logI("Sem nenhum alerta no banco de dados");
            }

            @Override
            public void onObjectInserted() {}

            @Override
            public void onObjectUpdated() {}

            @Override
            public void onObjectDeleted() {}
        });
    }

    @Override
    public void onInsertCommentClicked() {
        view.showDialogComment();
    }

    @Override
    public void onCancelCommentClicked() {
        view.closeDialogComment();
    }

    @Override
    public void onCommentClicked(String comment) {
        String name = Person.getInstance().getName();
        int index = alertComment.getCommentsSize();
        alertComment.insertComment(new Comment(index, name, comment));
        view.closeDialogComment();
        view.onCommentInserted(index);

        tryToInsertCommentsIntoDatabase();
    }

    private void tryToInsertCommentsIntoDatabase() {
        alertComment.insert(alertComment, new DAO.DAOCallback<AlertComment>() {
            @Override
            public void onObjectFinded(AlertComment result) {

            }

            @Override
            public void onObjectInserted() {
                Logger.logI("Comentário inserirido com sucesso");
            }

            @Override
            public void onObjectUpdated() {

            }

            @Override
            public void onObjectDeleted() {

            }

            @Override
            public void onFailedTask() {
                Logger.logI("Falhou em inserir o comentário");
            }
        });
    }

    @Override
    public void onSuccessTask(Constants task, Object object) {
        Log.i("Script", "Imagem baixada com sucesso");
        if(task == Constants.DOWNLOAD_IMAGE) {
            byte[] byteArray = (byte[]) object;
            Bitmap photoBitMap = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
            view.setAlertPhoto(photoBitMap);
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