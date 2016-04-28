package com.developmentpatterns.steve.protectingimplicitintents;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String APP_TAG = "ImplicintIntentsSamples";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private String photoFileName = "photo.jpg";
    private final static String PHONE_NUMBER = "992067184";
    private final static String URL = "http://www.google.com";
    private String APP_PACKAGE_NAME = "com.rupture.jairsteve.rupture";
    private String MAIL_TO = "js7campos@gmail.com";
    private Double LATITUDE = 37.7749;
    private Double LONGITUDE = -122.4194;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button buttonCapturePhoto = (Button) findViewById(R.id.buttonCapturePhoto);
        Button buttonGoogleMaps = (Button) findViewById(R.id.buttonGoogleMaps);
        Button buttonLaunchWebsite = (Button) findViewById(R.id.buttonLaunchWebsite);
        Button buttonPhoneCall = (Button) findViewById(R.id.buttonPhoneCall);
        Button buttonPlayStore = (Button) findViewById(R.id.buttonPlayStore);
        Button buttonSendEmail = (Button) findViewById(R.id.buttonSendEmail);
        Button buttonSharingContent = (Button) findViewById(R.id.buttonSharingContent);
        Button buttonSms = (Button) findViewById(R.id.buttonSms);

        buttonCapturePhoto.setOnClickListener(this);
        buttonGoogleMaps.setOnClickListener(this);
        buttonLaunchWebsite.setOnClickListener(this);
        buttonPhoneCall.setOnClickListener(this);
        buttonPlayStore.setOnClickListener(this);
        buttonSendEmail.setOnClickListener(this);
        buttonSharingContent.setOnClickListener(this);
        buttonSms.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCapturePhoto:
                    capturePhoto();
                break;
            case R.id.buttonSendEmail:
                    sendEmail(MAIL_TO);
                break;
            case R.id.buttonGoogleMaps:
                    openGoogleMaps(LATITUDE, LONGITUDE);
                break;
            case R.id.buttonSharingContent:
                    sharingText("Fuck off World!");
                    break;
            case R.id.buttonSms:
                sendSms(getActivity(),"Some message", Collections.singletonList(PHONE_NUMBER));
                break;
            case R.id.buttonPlayStore:
                showAppPlayStore(APP_PACKAGE_NAME);
                break;
            case R.id.buttonPhoneCall:
                phoneCall(PHONE_NUMBER);
                break;
            case R.id.buttonLaunchWebsite:
                launchURL(URL);
                break;
            default:
                Snackbar.make(v, "Not implemented yet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }

    private void sharingText(String textMessage){
        // Create the text message with a string
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
        sendIntent.setType("text/plain");

        // Verify that the intent will resolve to an activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sendIntent, "Share text using"));
        }

    }
    private void showAppPlayStore(String appPackageName){

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appPackageName));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void phoneCall(String PHONE_NUMBER){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + PHONE_NUMBER));
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        }
    }

    private AppCompatActivity getActivity(){
        return this;
    }


    public static boolean sendSms(Context context, String text, List<String> numbers) {

        String numbersStr = TextUtils.join(",", numbers);

        Uri uri = Uri.parse("sms:" + numbersStr);

        Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra("sms_body", text);
        intent.putExtra("address", numbersStr);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_SENDTO);
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            if(defaultSmsPackageName != null) {
                intent.setPackage(defaultSmsPackageName);
            }
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setType("vnd.android-dir/mms-sms");
        }


        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }

        return true;
    }

    private void launchURL(String URL){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        }
    }

    private void sharingContent(Uri uri){

        Intent sharingContent = new Intent(Intent.ACTION_SEND);
        sharingContent.setType("image/jpg");
        sharingContent.putExtra(Intent.EXTRA_STREAM, uri);
        if (sharingContent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sharingContent, "Share image using"));
        }
        startActivity(Intent.createChooser(sharingContent, "Share image using"));
    }

    private void openGoogleMaps(Double latitude, Double longitude){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //LATITUD Y LONGITUD DE SAN FRANCISCO
        String data = String.format("geo:%s,%s", latitude, longitude);
        intent.setData(Uri.parse(data));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void sendEmail(String mailTo){
        String uriText  =
                "mailto:" + mailTo+
                        "?subject="+ Uri.encode("Subject here!")+
                        "&body="+ Uri.encode("Some text here!");
        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivity(Intent.createChooser(sendIntent, "Send email"));
        }

    }

    //More info: https://github.com/codepath/android_guides/wiki/Accessing-the-Camera-and-Stored-Media
    private void capturePhoto() {

        //GET THE CURRENT TIME IN MILLIS, TO PREVENT TO REPEAT FILE NAME
        //long time= System.currentTimeMillis();
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            Log.d(APP_TAG, "EXTERNAL STORAGE IS AVALAIBLE : true");
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                if (ivPreview != null) {
                    ivPreview.setImageBitmap(takenImage);
                }
                sharingContent(takenPhotoUri);

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
