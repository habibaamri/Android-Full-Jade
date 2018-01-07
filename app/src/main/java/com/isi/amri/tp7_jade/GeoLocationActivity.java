package com.isi.amri.tp7_jade;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class GeoLocationActivity extends AppCompatActivity implements LocationListener {
    public double longitude ;
    public double latitude;
    private LocationManager locationManager;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_location);

    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    String provider=LocationManager.GPS_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
    }
        locationManager.requestLocationUpdates(provider,
            1000, 0, this);



    }

    @Override
    public void onLocationChanged(final Location location) {

        longitude = location.getLongitude();
        latitude= location.getAltitude();
        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
       //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

        intent = new Intent();
        intent.putExtra("longitude", longitude+"");
        intent.putExtra("latitude", latitude+"");
        setResult(RESULT_OK,intent);
        finish();

    }
    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
