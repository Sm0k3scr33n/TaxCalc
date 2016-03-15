package com.example.android.taxcalc;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private int zipCode = 0;
    public String zip;
    public String amountStr;
    private Spinner spinner1;
    private Button btnSubmit;
    public String item;
    public Double itemTax = 0.00;
    public Double state = 0.00;
    public Double grandTotal = 0.00;
    public Double amount = 0.00;
    public Double itemTaxTotal = 0.00;
    public Double zipTaxTotal = 0.00;

    GoogleMap mMap;

    private GoogleApiClient mLocationClient;
    private static DecimalFormat df2 = new DecimalFormat(".##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        if (servicesOK()) {

            mLocationClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationClient.connect();

        } else {
            Toast.makeText(this, "Services are not working", Toast.LENGTH_SHORT).show();
        }
    }


    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public void addListenerOnButton() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);

    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView parent, View view, int pos, long id) {
            item = parent.getItemAtPosition(pos).toString();
            TextView rrt = (TextView) findViewById(R.id.itemDisplay);
            rrt.setText(item);

            String t = "Click TaxCalc";

            TextView itd = (TextView) findViewById(R.id.taxRateTotal);
            itd.setText(t);

            String zct = Double.toString(state);
            TextView srt = (TextView) findViewById(R.id.taxDisplayZip);
            srt.setText(t);

            TextView stext = (TextView) findViewById(R.id.totalStateAmount);
            stext.setText(t);

            TextView ittext = (TextView) findViewById(R.id.totalItemAmount);
            ittext.setText(t);

            TextView total = (TextView) findViewById(R.id.totalAmount);
            total.setText(t);
        }


        @Override
        public void onNothingSelected(AdapterView parent) {

        }

    }


    public void geoLocate(View v) throws IOException, ParseException {

//        reset of values.  final product would be optimized
        String t = "Click TaxCalc";

        TextView itd = (TextView) findViewById(R.id.taxRateTotal);
        itd.setText(t);

        String zct = Double.toString(state);
        TextView srt = (TextView) findViewById(R.id.taxDisplayZip);
        srt.setText(t);

        TextView stext = (TextView) findViewById(R.id.totalStateAmount);
        stext.setText(t);

        TextView ittext = (TextView) findViewById(R.id.totalItemAmount);
        ittext.setText(t);

        TextView total = (TextView) findViewById(R.id.totalAmount);
        total.setText(t);

//        checking connectivity status

        if (AppStatus.getInstance(this).isOnline()) {

            Location currentLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mLocationClient);
            Geocoder gc = new Geocoder(this);
            List<Address> addresses = gc.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            String locality = addresses.get(0).getLocality();
            String zip = addresses.get(0).getPostalCode();

            TextView text = (TextView) findViewById(R.id.zipDisplay);
            text.setText(zip);
            zipCode = ((Number) NumberFormat.getInstance().parse(zip)).intValue();
            Toast.makeText(this, "You are in " + locality + " , " + zipCode, Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(this, "The Use GPS will not work unless connected to Internet", Toast.LENGTH_SHORT).show();
        }

    }


    public void inputZip(View view) throws ParseException {
        TextView et = (TextView) findViewById(R.id.editText1);
        String zip = et.getText().toString();

        //        reset of values.  final product would be optimized
        String t = "Click TaxCalc";

        TextView itd = (TextView) findViewById(R.id.taxRateTotal);
        itd.setText(t);

        String zct = Double.toString(state);
        TextView srt = (TextView) findViewById(R.id.taxDisplayZip);
        srt.setText(t);

        TextView stext = (TextView) findViewById(R.id.totalStateAmount);
        stext.setText(t);

        TextView ittext = (TextView) findViewById(R.id.totalItemAmount);
        ittext.setText(t);

        TextView total = (TextView) findViewById(R.id.totalAmount);
        total.setText(t);

        // error handling if nothing is entered

        if (zip.equals("")) {

            TextView text = (TextView) findViewById(R.id.zipDisplay);
            text.setText("Enter Zip");
            zipCode = 0;
        } else {
            TextView text = (TextView) findViewById(R.id.zipDisplay);
            text.setText(zip);
            zipCode = ((Number) NumberFormat.getInstance().parse(zip)).intValue();
        }

    }


    public void inputAmount(View view) throws ParseException {
        TextView et = (TextView) findViewById(R.id.editText2);
        String amountStr = et.getText().toString();

        //        reset of values.  final product would be optimized
        String t = "Click TaxCalc";

        TextView itd = (TextView) findViewById(R.id.taxRateTotal);
        itd.setText(t);

        String zct = Double.toString(state);
        TextView srt = (TextView) findViewById(R.id.taxDisplayZip);
        srt.setText(t);

        TextView stext = (TextView) findViewById(R.id.totalStateAmount);
        stext.setText(t);

        TextView ittext = (TextView) findViewById(R.id.totalItemAmount);
        ittext.setText(t);

        TextView total = (TextView) findViewById(R.id.totalAmount);
        total.setText(t);

        // error handling if nothing is entered

        if (amountStr.equals("")) {

            TextView text = (TextView) findViewById(R.id.amountDisplay);
            text.setText("Enter Sale Amount");
            amount = 0.0;
        } else {
            amount = Double.parseDouble(amountStr);
            TextView text = (TextView) findViewById(R.id.amountDisplay);
            text.setText("$" + amountStr);

        }
    }

    public boolean servicesOK() {

//        used during testing, disabled in final product

        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
//                Toast.makeText(this, "Location Services Work!", Toast.LENGTH_SHORT).show();
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog =
                    GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
//                Toast.makeText(this, "Cannot connect to location Services", Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    public void calcGo(View view) throws ParseException {

//        hard coding of zipcode taxes.  This would become a separate class in a final product
        if (zipCode < 10001) {
            state = .01;
        } else if (zipCode < 20001) {
            state = .02;
        } else if (zipCode < 30001) {
            state = .03;
        } else if (zipCode < 40001) {
            state = .04;
        } else if (zipCode < 50001) {
            state = .05;
        } else {
            state = .06;

        }

        TextView et = (TextView) findViewById(R.id.itemDisplay);
        String its = et.getText().toString();

//        hardcoding of item tax.  this would be a separate class in final product

        if (item.equals("Clothes")) {
            itemTax = .01;
        } else if (item.equals("Food")) {
            itemTax = .02;
        } else if (item.equals("Electronics")) {
            itemTax = .03;
        } else {
            itemTax = .00;

        }
        zipTaxTotal = amount * state;
        itemTaxTotal = amount * itemTax;
        grandTotal = amount + itemTaxTotal + zipTaxTotal;

        String zipTaxTotalF = String.format("%.2f", zipTaxTotal);
        String itemTaxTotalF = String.format("%.2f", itemTaxTotal);
        String grandTotalF = String.format("%.2f", grandTotal);

        String it = Double.toString(itemTax);
        TextView itd = (TextView) findViewById(R.id.taxRateTotal);
        itd.setText(it + "%");

        String zct = Double.toString(state);
        TextView srt = (TextView) findViewById(R.id.taxDisplayZip);
        srt.setText(zct + "%");

        TextView text = (TextView) findViewById(R.id.totalStateAmount);
        text.setText("$ " + zipTaxTotalF);

        TextView ittext = (TextView) findViewById(R.id.totalItemAmount);
        ittext.setText("$ " + itemTaxTotalF);

        TextView total = (TextView) findViewById(R.id.totalAmount);
        total.setText("$ " + grandTotalF);
    }


    @Override
    public void onConnected(Bundle bundle) {
//        Toast.makeText(this, "OnConnected Location Services are Working", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
