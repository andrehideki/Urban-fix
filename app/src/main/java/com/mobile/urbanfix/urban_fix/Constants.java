package com.mobile.urbanfix.urban_fix;


public enum Constants {

    NEW_USER(1), NEW_ALERT(2), UPDATED_USER(3), NEW_PHOTO(4), SEND_PASSWORD(5),
    FIND_USER(6), DO_LOGIN(7), DOWNLOAD_IMAGE(8);

    public int value;

    Constants(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
