package com.mobile.urbanfix.urban_fix.model;

public interface Callback {
    interface SimpleAsync<T> {
        void onTaskDone(T result, boolean success);
    }


    interface FetchList<T> {
        void onItemAdded(T result);
        void onItemRemoved(T result);
        void onFailed();
    }
}
