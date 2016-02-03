package sky.example.myprotect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import java.util.Locale;


public class Maplocation extends Activity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private final int maxResult = 3;
    private String addressList[] = new String[maxResult];
    private ArrayAdapter<String> adapter;
    private TextView output;
    private Geocoder geocoder;
    private EditText lat, lon;
    private final int MAX_RECORDS = 10;
    private LocationManager manager;
    private Location currentLocation;
    private String best;
    private int index = 0, count = 0;
    private double[] GPLat = new double[MAX_RECORDS];
    private double[] GPLng = new double[MAX_RECORDS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lat = (EditText) findViewById(R.id.txtLat);
        lon = (EditText) findViewById(R.id.txtLong);
        output = (TextView) findViewById(R.id.output);
        geocoder = new Geocoder(this, Locale.TAIWAN);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        }

    public void button1_Click(View view) {

        float latitude = Float.parseFloat(lat.getText().toString());
        float longitude = Float.parseFloat(lon.getText().toString());
        try {

            List<Address> listAddress = geocoder.getFromLocation(latitude, longitude, maxResult);

            if (listAddress != null) {
                Spinner spinner = (Spinner) findViewById(R.id.addresslist);

                for (int j = 0; j < maxResult; j++) addressList[j] = "N/A";
                int index = 0;
                for (int j = 0; j < maxResult; j++) {
                    Address findAddress = listAddress.get(j);

                    StringBuilder strAddress = new StringBuilder();

                    for (int i = 0; i < findAddress.getMaxAddressLineIndex(); i++) {
                        String str = findAddress.getAddressLine(i);
                        strAddress.append(str).append("\n");
                    }
                    if (strAddress.length() > 0) {
                        addressList[index++] = strAddress.toString();
                    }
                }

                adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, addressList);
                adapter.setDropDownViewResource(android.R.layout.
                        simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            } else {
                output.setText("注意：沒有回傳地址資料!");
            }
        } catch (Exception ex) {
            output.setText("錯誤：" + ex.toString());
        }
    }

    public void button2_Click(View view) {

        EditText address = (EditText) findViewById(R.id.txtAddress);
        String addressName = address.getText().toString();
        try {

            List<Address> listGPSAddress = geocoder.getFromLocationName(addressName, 1);

            if (listGPSAddress != null) {
                double latitude = listGPSAddress.get(0).getLatitude();
                double longitude = listGPSAddress.get(0).getLongitude();
                output.setText("緯度： " + latitude +
                        "\n 經度： " + longitude);
                lat.setText(String.valueOf(latitude));
                lon.setText(String.valueOf(longitude));
            }
        } catch (Exception ex) {
            output.setText("錯誤：" + ex.toString());
        }
    }

    public void button3_Click(View view) {

        float latitude = Float.parseFloat(lat.getText().toString());
        float longitude = Float.parseFloat(lon.getText().toString());

        String uri = String.format("geo:%f,%f?z=18", latitude, longitude);

        Intent geoMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(geoMap);
    }

    public void button4_Click(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("關於");
        builder.setMessage("開發人:曾建成\n" +
                "啟動版本：Android4.0版以上適用\n" +
                "聲明：demo作品");
        builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
            }
        });
        builder.show();
    }

}