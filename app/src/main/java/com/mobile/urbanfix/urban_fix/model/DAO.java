package com.mobile.urbanfix.urban_fix.model;

import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public interface DAO<T> {
    T find(String s, MainMVP.ICallbackPresenter presenter);
    void insert(T object, MainMVP.ICallbackPresenter presenter);
    void update(T object, MainMVP.ICallbackPresenter presenter);
    void delete(T object, MainMVP.ICallbackPresenter presenter);
}