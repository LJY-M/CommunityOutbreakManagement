package com.example.communityoutbreakmanagement;

import android.net.Uri;
import android.provider.BaseColumns;

public class TemperatureRecordsContract {

    public static final String AUTHORITY = "com.example.communityoutbreakmanagement";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +AUTHORITY);

    public static final String PATH_TEMPERATURE_RECORDS = "temperatureRecords";

    public static final class TemperatureRecordsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEMPERATURE_RECORDS).build();

        public static final String TABLE_NAME = "temperatureRecords";
        public static final String COLUMN_HOUSE_NUMBER = "houseNumber";
        public static final String COLUMN_RESIDENT_NAME = "residentName";
        public static final String COLUMN_RESIDENT_TEMPERATURE = "residentTemperature";
        public static final String COLUMN_RECORDS_TIME = "recordsTime";
    }
}
