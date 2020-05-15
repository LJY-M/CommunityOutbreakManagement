package com.example.communityoutbreakmanagement;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {

    double[] locations = new double[2];

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        locations[0] = location.getLongitude();
        locations[1] = location.getLatitude();
    }
    @Override
    public void onProviderDisabled(String provider) {
        //当provider被用户关闭时调用
        Log.i("GpsLocation", "provider被关闭! ");
    }
    @Override
    public void onProviderEnabled(String provider) {
        //当provider被用户开启后调用
        Log.i("GpsLocation", "provider被开启! ");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //当provider的状态在OUT_OF_SERVICE、TEMPORARILY_UNAVAILABLE 和 AVAILABLE 之间发生变化时调用
        Log.i("GpsLocation", "provider状态发生改变!");
    }
}
