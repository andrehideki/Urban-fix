package com.mobile.urbanfix.urban_fix.view.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.Sentiments;
import com.mobile.urbanfix.urban_fix.model.Comment;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.AlertDialogPresenter;

import java.util.ArrayList;
import java.util.List;

public class AlertDialogFragment extends DialogFragment implements MainMVP.IAlertDialogView {

    private static Problem problem;
    private ImageView problemPhotoImageView;
    private static MainMVP.IAlertDialogPresenter presenter;
    private AlertDialog commentDialog;
    private TextView kindOfProblemTextView, dateTextView, descriptionTextView, addressTextView,
            statusTextView, urgencyTextView;
    private CommentsAdapter adapter;
    private RecyclerView commentsRecyclerView;



    public static AlertDialogFragment newInstance(Problem problem) {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        AlertDialogFragment.problem = problem;
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_problem, container);
        TextView insertCommentTextView;
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        startMVP();

        kindOfProblemTextView = view.findViewById(R.id.kindOfProblemTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        statusTextView = view.findViewById(R.id.statusTextView);
        urgencyTextView = view.findViewById(R.id.urgencyTextView);
        insertCommentTextView = view.findViewById(R.id.insertCommentTextView);
        problemPhotoImageView = view.findViewById(R.id.problemPhotoImageView);

        insertCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onInsertCommentClicked();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void setAlertInformations(String kindOfProblem, String date, String status, String address,
                                     String description, String urgency) {
        kindOfProblemTextView.setText(kindOfProblem);
        dateTextView.setText(date);
        statusTextView.setText(status);
        addressTextView.setText(address);
        descriptionTextView.setText(description);
        urgencyTextView.setText(urgency);
    }

    @Override
    public void setAlertPhoto(Bitmap bitmap) {
        problemPhotoImageView.setImageBitmap(bitmap);
    }

    @Override
    public void showDialogComment() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_comment, null);
        final AppCompatEditText commentEditText = v.findViewById(R.id.commentEditText);
        commentDialog = new AlertDialog.Builder(getContext())
                .setView(v)
                .setPositiveButton(getString(R.string.problem_comment_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = commentEditText.getText().toString();
                        presenter.onCommentClicked(comment);
                    }
                })
                .setNegativeButton(getString(R.string.problem_cancel_comment_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onCancelCommentClicked();
                    }
                })
                .create();

        commentDialog.show();
    }

    @Override
    public void closeDialogComment() {
        commentDialog.cancel();
    }

    @Override
    public void onCommentInserted(int position) {
        Logger.logI("Inserirdo na posição : " + position);
        adapter.notifyItemInserted(position);
    }

    @Override
    public String getProjectString(int id) {
        return getString(id);
    }

    @Override
    public void onCommentsLoaded(List<Comment> comments) {
        adapter = new CommentsAdapter((ArrayList<Comment>) comments);
        commentsRecyclerView.setAdapter(adapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void startMVP() {
        this.presenter = new AlertDialogPresenter(this);
    }

    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

        private ArrayList<Comment> comments;

        public CommentsAdapter(ArrayList<Comment> comments) {
            this.comments = comments;
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CommentViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.item_comment, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            Comment comment = comments.get(position);
            holder.personNameTextView.setText(comment.getPersonName());
            holder.commentTextView.setText(comment.getContent());
            holder.sentimentImageView.setImageResource(Sentiments.getId(comment.getSentiment()));
        }

        @Override
        public int getItemCount() {
            return comments != null ? comments.size() : 0;
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {
            TextView personNameTextView, commentTextView;
            ImageView sentimentImageView;
            public CommentViewHolder(View itemView) {
                super(itemView);
                personNameTextView = itemView.findViewById(R.id.personNameTextView);
                commentTextView = itemView.findViewById(R.id.commentTextView);
                sentimentImageView = itemView.findViewById(R.id.sentimentImageView);
            }
        }
    }

}
