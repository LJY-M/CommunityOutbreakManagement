package com.example.communityoutbreakmanagement;

import android.provider.BaseColumns;

public class ResidentContract {

    public static final class ResidentEntry implements BaseColumns {
        public static final String TABLE_NAME = "resident";
        public static final String COLUMN_HOUSE_NUMBER = "houseNumber";
        public static final String COLUMN_RESIDENT_NAME = "residentName";
        public static final String COLUMN_RESIDENT_PASSWORD = "residentPassword";
        public static final String COLUMN_RESIDENT_PHONE = "residentPhone";
    }
}
