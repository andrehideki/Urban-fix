package com.mobile.urbanfix.urban_fix.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mobile.urbanfix.urban_fix.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressSevice extends IntentService {

    public static final String FETCH_ADDRESS_SERVICE = "FetchAddressSevice";
    public static final String RESULT = "Result";
    public static final String ADDRESSES_LIST = "PossibleAddressesList";
    public static final int RESULT_OK = 1;
    public static final int RESULT_FAILED = 2;

    public FetchAddressSevice() {
        super(FETCH_ADDRESS_SERVICE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null) return;
        double[] geoInformations = intent.getDoubleArrayExtra(User.ADDRESS);
        if(geoInformations == null) return;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(geoInformations[0], geoInformations[1], 5);
            if(addresses == null) {
                deliverToReceiver(RESULT_FAILED, null);
            } else {
                deliverToReceiver(RESULT_OK, getAddressesInformation(addresses));
            }
        } catch (IOException e) {
            Log.e("Script", e.getMessage());
            deliverToReceiver(RESULT_FAILED, null);
        }
    }

    private void deliverToReceiver(int resultCode, ArrayList<String> addresses) {
        Bundle b = new Bundle();
        if(resultCode == RESULT_OK) {
            b.putInt(RESULT, RESULT_OK);
            b.putStringArrayList(ADDRESSES_LIST, addresses);
        } else {
            b.putInt(RESULT, RESULT_FAILED);
        }
        sendBroadcast(new Intent(this, FetchAddressReceiver.class).putExtras(b).
                setAction(FetchAddressReceiver.ACTION));
    }

    private ArrayList<String> getAddressesInformation(List<Address> addresses) {
        ArrayList<String> addressesInformationsList = new ArrayList<>();
        for (Address a: addresses) {
            addressesInformationsList.add(a.getAddressLine(0));
        }
        return addressesInformationsList;
    }
}
