package kingsleyjohn.com.ven10;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Home extends AppCompatActivity implements MessageListenerInterface {

    static int PERMISSIONS_REQUEST_READ_SMS = 12736;
    static int PERMISSIONS_REQUEST_READ_CONTACT = 2899;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestReadMessagePermission();
    }

    @Override
    public void messageReceived(String message) {
        //just for testing the application
        Log.e("hello", message);
    }

    private void requestReadMessagePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.RECEIVE_SMS
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSIONS_REQUEST_READ_SMS);
        } else {
            //registerListener();
            requestReadContactPermission();
        }
    }

    private void requestReadContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACT);
        } else {
            registerListener();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_SMS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //registerListener();
            requestReadContactPermission();
        }else if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Please accept permissions else app will not perform well", Toast.LENGTH_LONG).show();
        }

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACT && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            registerListener();
        }else if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Please accept permissions else app will not perform well", Toast.LENGTH_LONG).show();
        }
    }

    private void registerListener(){
        //Register message listener
        MessageReceiveClass.bindListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerForOreoDevices();
        }
    }

    private void registerForOreoDevices(){
            MessageReceiveClass smsReceiver = new MessageReceiveClass();
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);
    }
}
