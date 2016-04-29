# Implicit Intents Samples

Implicit intents samples suchs as capture photo, share image, send email, etc.

Just an example of CodePath - [Common Implicit Intents Page on CodePath](https://github.com/codepath/android_guides/wiki/Common-Implicit-Intents)

## Screenshots
![MainLayout](https://github.com/SteveCampos/ImplicitIntents/blob/master/Screenshot_2016-04-29-12-56-06.png)
![SendEmail](https://github.com/SteveCampos/ImplicitIntents/blob/master/Screenshot_2016-04-29-13-05-00.png)
![ShareTextUsing](https://github.com/SteveCampos/ImplicitIntents/blob/master/Screenshot_2016-04-29-13-05-09.png)

## Sample Code

### Launch Browser

```java
    private void launchURL(String URL){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        }
    }

```

### Capture Photo and Share


```java
	
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

    //SHARE IMAGE CAPTURED
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

    private void sharingContent(Uri uri){
        Intent sharingContent = new Intent(Intent.ACTION_SEND);
        sharingContent.setType("image/jpg");
        sharingContent.putExtra(Intent.EXTRA_STREAM, uri);
        if (sharingContent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(sharingContent, "Share image using"));
        }
    }
```




