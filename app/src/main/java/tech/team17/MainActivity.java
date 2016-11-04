package tech.team17;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import tech.team17.gcm.RegistrationIntentService;
import tech.team17.util.Utility;

public class MainActivity extends AppCompatActivity {


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String myLoginEmailAddress = getLoginEmailAddress();
        TextView loginInformation = (TextView)findViewById(R.id.login_email);
        if(myLoginEmailAddress != null || !myLoginEmailAddress.equals("")){
            Utility user_token = new Utility();
            String s = user_token.user_token;
            SharedPreferences sp = getApplicationContext().getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
            String id = sp.getString(s, null);

            loginInformation.setText("Welcome!!! You have logged in as " + myLoginEmailAddress + " " +id);
        }else {
            loginInformation.setText("Your login email is missing");
        }

        if(checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
    private String getLoginEmailAddress(){
        String storedEmail = "";
        Intent mIntent = getIntent();
        Bundle mBundle = mIntent.getExtras();
        if(mBundle != null){
            storedEmail = mBundle.getString("EMAIL");
        }
        return storedEmail;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
