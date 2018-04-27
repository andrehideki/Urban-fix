package com.mobile.urbanfix.urban_fix.model;

import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public interface DAO<T> {

    /*
    *   Procura o objeto no banco de dados e retorna via callback
    * */
    void find(String s, DAOCallback<T> callback);
    void insert(T object, DAOCallback<T> callback);
    void update(T object, DAOCallback<T> callback);
    void delete(T object, MainMVP.ICallbackPresenter presenter);

    interface DAOCallback<T> {
        int INSERT =1, UPDATE=2, FIND=3, REMOVE=4;
        void onObjectFinded(T result);
        void onObjectInserted();
        void onObjectUpdated();
        void onObjectDeleted();
        void onFailedTask();
    }
}