package com.example.communityoutbreakmanagement;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String DYNAMIC_EPIDEMIC_URL =
            "https://c.m.163.com/ug/api/wuhan/app/data/list-total";

    final static String FORMAT_PARAM = "mode";
    private static final String format = "json";
    private static final String units = "metric";
    final static String UNITS_PARAM = "units";

    public static URL buildEpidemicUrl() {
        Uri builtUri = Uri.parse(DYNAMIC_EPIDEMIC_URL).buildUpon()
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Epidemic URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static EpidemicData getSimpleEpidemicObjectFromJson(String epidemicJsonStr)
            throws JSONException {

        final String OWM_MSG = "msg";
        final String OWM_DATA = "data";
        final String OWM_DATA_CHINATOTAL = "chinaTotal";
        final String OWM_DATA_CHINATOTAL_TODAY = "today";
        final String OWM_DATA_CHINATOTAL_TOTAL = "total";
        final String OWM_DATA_CHINATOTAL_EXTDATA = "extData";

        final String OWM_TODAY_CONFIRM = "confirm";
        final String OWM_TODAY_HEAL = "heal";
        final String OWM_TODAY_DEAD = "dead";
        final String OWM_TODAY_STORECONFIRM = "storeConfirm";
        final String OWM_TODAY_INPUT = "input";
        final String OWM_EXTDATA_INCRNOSYMPTOM = "incrNoSymptom";

        final String OWM_TOTAL_CONFIRM = "confirm";
        final String OWM_TOTAL_HEAL = "heal";
        final String OWM_TOTAL_DEAD = "dead";
        final String OWM_TOTAL_STORECONFIRM = "storeConfirm";
        final String OWM_TOTAL_INPUT = "input";
        final String OWM_EXTDATA_NOSYMPTOM = "noSymptom";

        JSONObject epidemicJson = new JSONObject(epidemicJsonStr);
        if (epidemicJson.has(OWM_MSG)) {
            String getMSG = epidemicJson.getString(OWM_MSG);

            if (!getMSG.equals("成功"))
                return null;
        }

        JSONObject dataObject = epidemicJson.getJSONObject(OWM_DATA);
        JSONObject chinaTotalObject = dataObject.getJSONObject(OWM_DATA_CHINATOTAL);
        JSONObject todayObject = chinaTotalObject.getJSONObject(OWM_DATA_CHINATOTAL_TODAY);
        JSONObject totalObject = chinaTotalObject.getJSONObject(OWM_DATA_CHINATOTAL_TOTAL);
        JSONObject extDataObject = chinaTotalObject.getJSONObject(OWM_DATA_CHINATOTAL_EXTDATA);

        EpidemicData epidemicData;
        epidemicData = new EpidemicData(
                todayObject.getInt(OWM_TODAY_CONFIRM),todayObject.getInt(OWM_TODAY_HEAL),
                todayObject.getInt(OWM_TODAY_DEAD),todayObject.getInt(OWM_TODAY_STORECONFIRM),
                todayObject.getInt(OWM_TODAY_INPUT),extDataObject.getInt(OWM_EXTDATA_INCRNOSYMPTOM),
                totalObject.getInt(OWM_TOTAL_CONFIRM),totalObject.getInt(OWM_TOTAL_HEAL),
                totalObject.getInt(OWM_TOTAL_DEAD),
                totalObject.getInt(OWM_TOTAL_CONFIRM)-
                        totalObject.getInt(OWM_TOTAL_DEAD)-
                        totalObject.getInt(OWM_TOTAL_HEAL),
                totalObject.getInt(OWM_TOTAL_INPUT),extDataObject.getInt(OWM_EXTDATA_NOSYMPTOM));

        return epidemicData;
    }
}
