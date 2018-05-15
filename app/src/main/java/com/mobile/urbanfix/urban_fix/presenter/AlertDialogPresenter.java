package com.mobile.urbanfix.urban_fix.presenter;


import android.graphics.Bitmap;

import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.Sentiments;
import com.mobile.urbanfix.urban_fix.model.AlertComment;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.Comment;
import com.mobile.urbanfix.urban_fix.services.ConsultSentmentAzureService;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class AlertDialogPresenter implements MainMVP.IProblemDialogPresenter {

    private MainMVP.IProblemDialogView view;
    private static Problem problem;
    private AlertComment alertComment;

    public AlertDialogPresenter(MainMVP.IProblemDialogView view) {
        this.view = view;
    }

    @Override
    public void onStart() {
        if(problem != null) {
            view.setAlertInformations(problem.getKindOfProblem(), problem.getDate(), problem.getStatus(),
                    problem.getLocation().getAddress(), problem.getDescription(), problem.getUrgency());
            getProblemPhotoAssync();
            tryToLoadComments();
        }
    }


    public static void setProblem(Problem problem) {
        AlertDialogPresenter.problem = problem;
    }

    @Override
    public void tryToLoadComments() {
        alertComment = new AlertComment(problem.getId());
        alertComment.find(alertComment.getAlertId(), new Callback.SimpleAsync<AlertComment>() {
            @Override
            public void onTaskDone(AlertComment result, boolean success) {
                if(success) {
                    alertComment.setComments(result.getComments());
                    view.onCommentsLoaded(alertComment.getComments());
                    Logger.logI("Encontrado no banco de dados" + result.toString());
                } else {
                    alertComment.setComments(new ArrayList<Comment>(0));
                    view.onCommentsLoaded(alertComment.getComments());
                    Logger.logI("Sem nenhum comentário no banco de dados");
                }
            }
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
        if(!comment.isEmpty()) {
            String name = Person.getInstance().getName();
            final int index = alertComment.getCommentsSize();
            alertComment.insertComment(new Comment(index, name, comment));
            view.closeDialogComment();
            view.onCommentInserted(index);

            ConsultSentmentAzureService consultSentment = new ConsultSentmentAzureService(
                    new Callback.SimpleAsync<Double>() {
                        @Override
                        public void onTaskDone(Double result, boolean success) {
                            Comment c = alertComment.getComments().get(index);
                            c.setSentiment(getSentiment(result));

                            tryToInsertCommentsIntoDatabase();
                        }
                    });

            consultSentment.execute(view.getProjectString(R.string.azure_key),
                    view.getProjectString(R.string.azure_url),
                    comment);
        }
    }

    private Sentiments.SENTIMENTS getSentiment(double sentiment) {
        if(sentiment < 0.5) {
            return Sentiments.SENTIMENTS.DISSATISFIED;
        } else if(sentiment < 0.7) {
            return Sentiments.SENTIMENTS.NEUTRAL;
        }
        return Sentiments.SENTIMENTS.SATISFIED;
    }

    private void tryToInsertCommentsIntoDatabase() {
        alertComment.insert(alertComment, new Callback.SimpleAsync<AlertComment>() {
            @Override
            public void onTaskDone(AlertComment result, boolean success) {
                if(success) {
                    Logger.logI("Comentário inserido com sucesso");
                } else {
                    Logger.logI("Falhou em inserir o comentário");
                }
            }
        });
    }

    private void getProblemPhotoAssync() {
        problem.donwloadAlertPhoto(problem.getPhotoId(), new Callback.SimpleAsync<Bitmap>() {
            @Override
            public void onTaskDone(Bitmap result, boolean success) {
                if(success) {
                    Logger.logI("Foto baixada com sucesso");
                    view.setAlertPhoto(result);
                } else {
                    Logger.logE("Falha ao baixar foto");
                }
            }
        });
    }
}