package tw.brad.android.games.mydevicetest;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TelephonyManager tmgr;
    private MyListener myListener;
    private int lastState = -1;
    private ImageView img;

    private SensorManager smgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else{
            init();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        img = (ImageView)findViewById(R.id.img);

        smgr = (SensorManager)getSystemService(SENSOR_SERVICE);

        List<Sensor> sensors= smgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors){
            String name = sensor.getName();
            String v = sensor.getVendor();
            Log.i("brad", name + ":" + v);
        }


//        tmgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        String deviceid = tmgr.getDeviceId();
//        Log.i("brad", "IMEI:" + deviceid);
//        String num = tmgr.getLine1Number();
//        Log.i("brad", "phone:" + num);
//        String IMSI = tmgr.getSubscriberId();
//        Log.i("brad", "IMSI:" + IMSI);

//        myListener = new MyListener();
//        tmgr.listen(myListener,PhoneStateListener.LISTEN_CALL_STATE);

//        String name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
//        String number = ContactsContract.CommonDataKinds.Phone.NUMBER;
//        ContentResolver cr = getContentResolver();  // SQLiteDatabase => db
//        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
//        while (c.moveToNext()){
//            String f1 = c.getString(c.getColumnIndex(name));
//            String f2 = c.getString(c.getColumnIndex(number));
//            Log.i("brad", f1 + " : " + f2);
//        }
//        c.close();


//        c = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
//        c.moveToLast();
//        String file = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
//        Log.i("brad", file);
//
//        Bitmap bmp = BitmapFactory.decodeFile(file);
//        img.setImageBitmap(bmp);



    }

    private class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.i("brad", "got it");
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (state != lastState){
                        Log.i("brad", "idle");
                        lastState = state;
                    }
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    if (state != lastState ){
                        Log.i("brad", "ring:" + incomingNumber);
                        reListen();
                        lastState = state;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("brad", "offhook");
                    break;
                default:
                    Log.i("brad", "other:" + state);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void reListen(){
        //myListener = new MyListener();
        tmgr.listen(myListener,PhoneStateListener.LISTEN_CALL_STATE);

    }


}
