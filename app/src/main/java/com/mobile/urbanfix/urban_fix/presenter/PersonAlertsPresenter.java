package com.mobile.urbanfix.urban_fix.presenter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class PersonAlertsPresenter implements MainMVP.IPersonAlertsPresenter ,
        Callback.FetchList<Problem> {

    private MainMVP.IPersonAlertsView view;
    private ArrayList<Problem> personAlerts;
    private Person person;

    public PersonAlertsPresenter(MainMVP.IPersonAlertsView view) {
        this.view = view;
        this.person = Person.getInstance();
    }

    @Override
    public void configureUserInformations() {
        view.setUserName(person.getName());
        view.setNumberOfAlerts(person.getnAlertsDone());
    }


    @Override
    public ArrayList<Problem> getPersonAlerts() {
        this.personAlerts = new ArrayList<>();

        person.getPersonAlerts(this);

        return personAlerts;
    }

    @Override
    public void onItemClicked(int postion) {
        Problem p = personAlerts.get(postion);
        AlertDialogPresenter.setProblem(p);
        view.showAlertDialog();
    }

    //Chamado a cada alerta encontrado
    @Override
    public void onItemAdded(Problem result) {
        int index = personAlerts.size();
        personAlerts.add(result);
        view.onNewAlertFinded(index);
    }

    //Chamado a cada alerta removido
    @Override
    public void onItemRemoved(Problem result) {
        int index = personAlerts.size();
        personAlerts.remove(result);
        view.onAlertRemoved(index);
    }

    @Override
    public void onFailed() {
        Logger.logE("Falhou em buscar alertas do usuário");
    }
}
