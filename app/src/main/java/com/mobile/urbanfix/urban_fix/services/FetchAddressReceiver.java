package com.mobile.urbanfix.urban_fix.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.MainMVP;

import java.util.ArrayList;

public class FetchAddressReceiver extends BroadcastReceiver {

    private static MainMVP.IOnGpsPickupUserLocationAndPossibleAddresses presenter;
    public static final String ACTION = "ADDRESSESHASBEENDOWNLOADED";

    public static void setPrensenter(MainMVP.IOnGpsPickupUserLocationAndPossibleAddresses presenter) {
        FetchAddressReceiver.presenter = presenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        int result = b.getInt(FetchAddressSevice.RESULT);
        if (result == FetchAddressSevice.RESULT_OK) {
            ArrayList<String> posibleAddresseList = b.getStringArrayList(FetchAddressSevice.ADDRESSES_LIST);
            posibleAddresseList.add(0, context.getString(R.string.select_your_address));
            presenter.onSuccessGetUserAddresses(posibleAddresseList);
        } else {
            presenter.onFailedGetUserAddresses(context);
        }
    }
}
