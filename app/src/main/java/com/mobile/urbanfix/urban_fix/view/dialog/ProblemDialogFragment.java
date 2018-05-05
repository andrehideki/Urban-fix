package com.mobile.urbanfix.urban_fix.view.dialog;


import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Comment;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.AlertDialogPresenter;

import java.util.ArrayList;
import java.util.List;

public class ProblemDialogFragment extends DialogFragment implements MainMVP.IProblemDialogView {

    private static Problem problem;
    private ImageView problemPhotoImageView;
    private static MainMVP.IProblemDialogPresenter presenter;
    private CommentDialog commentDialog;
    private CommentsAdapter adapter;
    private RecyclerView commentsRecyclerView;



    public static ProblemDialogFragment newInstance(Problem problem) {
        ProblemDialogFragment dialogFragment = new ProblemDialogFragment();
        ProblemDialogFragment.problem = problem;
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
        TextView kindOfProblemTextView, dateTextView, descriptionTextView, addressTextView,
        statusTextView, urgencyTextView, insertCommentTextView;
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

        presenter.setInformations(problem, kindOfProblemTextView, dateTextView, statusTextView, addressTextView,
                descriptionTextView, urgencyTextView, problemPhotoImageView);

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
        presenter.loadComments();
    }

    @Override
    public void setAlertInformations(String kindOfProblem, String date, String status, String address, String description, String urgency) {

    }

    @Override
    public void setAlertPhoto(Bitmap bitmap) {
        problemPhotoImageView.setImageBitmap(bitmap);
    }

    @Override
    public void showDialogComment() {
        commentDialog = new CommentDialog();
        commentDialog.show(getFragmentManager(), null);
    }

    @Override
    public void closeDialogComment() {
        commentDialog.dismiss();
    }

    @Override
    public void onCommentInserted(int position) {
        Logger.logI("Inserirdo na posição : " + position);
        adapter.notifyItemInserted(position);
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

    @SuppressLint("ValidFragment")
    static class CommentDialog extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.dialog_comment, container);
            Button cancelButton, commentButton;
            final AppCompatEditText commentEditText;

            commentEditText = v.findViewById(R.id.commentEditText);
            cancelButton = v.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onCancelCommentClicked();
                }
            });
            commentButton = v.findViewById(R.id.commentButton);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = commentEditText.getText().toString();
                    presenter.onCommentClicked(comment);
                }
            });
            return v;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
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
        }

        @Override
        public int getItemCount() {
            return comments != null ? comments.size() : 0;
        }

        class CommentViewHolder extends RecyclerView.ViewHolder {
            TextView personNameTextView, commentTextView;
            public CommentViewHolder(View itemView) {
                super(itemView);
                personNameTextView = itemView.findViewById(R.id.personNameTextView);
                commentTextView = itemView.findViewById(R.id.commentTextView);
            }
        }

        public void setComments(ArrayList<Comment> comments) {
            this.comments = comments;
        }
    }

}
