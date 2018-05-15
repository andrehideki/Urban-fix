package com.mobile.urbanfix.urban_fix.view.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.presenter.PersonAlertsPresenter;
import com.mobile.urbanfix.urban_fix.R;

import java.util.ArrayList;

public class PersonAlertsFragment extends Fragment implements MainMVP.IPersonAlertsView {

    private TextView userNameTextView, numberOfAlertsTextView;
    private RecyclerView problemsRecyclerView;
    private MainMVP.IPersonAlertsPresenter presenter;
    private UserAlertsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_alerts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startMVP();

        userNameTextView = (TextView) view.findViewById(R.id.userNameTextView);
        numberOfAlertsTextView = (TextView) view.findViewById(R.id.numberOfAlertsTextView);
        problemsRecyclerView = (RecyclerView) view.findViewById(R.id.problemsRecyclerView);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new UserAlertsAdapter(presenter.getPersonAlerts());
        problemsRecyclerView.setAdapter(adapter);
        problemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter.configureUserInformations();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setUserName(String userName) {
        this.userNameTextView.setText(userName);
    }

    @Override
    public void setNumberOfAlerts(int numberOfAlerts) {
        this.numberOfAlertsTextView.setText(String.format("%d", numberOfAlerts));
    }

    @Override
    public void onNewAlertFinded(int position) {
        problemsRecyclerView.getAdapter().notifyItemInserted(position);
    }

    @Override
    public void onAlertRemoved(int position) {
        problemsRecyclerView.getAdapter().notifyItemRemoved(position);
    }

    @Override
    public void showAlertDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ProblemDialogFragment dialogFragment = new ProblemDialogFragment();
        dialogFragment.show(fm,null);
    }

    public class UserAlertsAdapter extends RecyclerView.Adapter<UserAlertsAdapter.UserAlertsViewHolder> {

        private ArrayList<Problem> myAlertsList;

        public UserAlertsAdapter(ArrayList<Problem> myAlertsList) {
            this.myAlertsList = myAlertsList;
        }

        class UserAlertsViewHolder extends RecyclerView.ViewHolder {
            TextView dateTextView, kindOfProblemTextView, statusTextView, descriptionTextView;

            public UserAlertsViewHolder(View itemView) {
                super(itemView);
                dateTextView = itemView.findViewById(R.id.datetextView);
                kindOfProblemTextView = itemView.findViewById(R.id.kindOfProblemTextView);
                statusTextView = itemView.findViewById(R.id.textView3);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            }
        }


        @NonNull
        @Override
        public UserAlertsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_user_reported_alert,
                    parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = problemsRecyclerView.getChildAdapterPosition(v);
                    presenter.onItemClicked(position);
                }
            });
            return new UserAlertsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserAlertsViewHolder holder, int position) {
            Problem problem = myAlertsList.get(position);

            holder.dateTextView.setText(problem.getDate());
            holder.descriptionTextView.setText(problem.getDescription());
            holder.kindOfProblemTextView.setText(problem.getKindOfProblem());
        }

        @Override
        public int getItemCount() {
            return myAlertsList != null ? myAlertsList.size() : 0;
        }

    }

    private void startMVP() {
        this.presenter = new PersonAlertsPresenter(this);
    }

}
