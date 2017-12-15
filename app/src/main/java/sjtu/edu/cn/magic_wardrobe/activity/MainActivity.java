package sjtu.edu.cn.magic_wardrobe.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import sjtu.edu.cn.magic_wardrobe.R;
import sjtu.edu.cn.magic_wardrobe.utils.Config;
import sjtu.edu.cn.magic_wardrobe.utils.StorageUtil;
import sjtu.edu.cn.magic_wardrobe.utils.ToastUtil;

import static sjtu.edu.cn.magic_wardrobe.activity.SearchActivity.TAG;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_capture_image)
    Button btnCapturePicture;
    @BindView(R.id.btn_choose_image)
    Button btnChooseImage;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_icon)
    ImageView toolbarIcon;


    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 101;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 102;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final int LOAD_PIC = 3;
    public static final int LOAD_VIDEO = 4;

    public static final String IS_IMAGE = "IS_IMAGE";
    public static final String FILE_PATH = "FILE_PATH";


    // All the permissions which must be requested in the app after Android 6.0
    private String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private AlertDialog dialog;
    private Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions();

        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    private void initView() {
        toolbarIcon.setImageDrawable(null);
        toolbarTitle.setText("Magic Wardrobe");
        btnCapturePicture.setOnClickListener((v) -> {
            captureImage();
        });
        btnChooseImage.setOnClickListener((v) -> {
            chooseImg();
        });
    }

    private boolean isDeviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private void chooseImg() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, LOAD_PIC);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    for (int i = 0; i < permissions.length; ++i) {
                        int per = ContextCompat.checkSelfPermission(this, permissions[i]);
                        if (per != PackageManager.PERMISSION_GRANTED) {
                            showDialogTipUserGoToAppSetting();
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }
                }
                break;
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    launchUploadActivity(true);
                } else if (resultCode == RESULT_CANCELED) {
                    ToastUtil.showShort("User cancelled image capture");
                } else {
                    ToastUtil.showShort("Sorry! Failed to capture image");
                }
                break;
            case CAMERA_CAPTURE_VIDEO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    launchUploadActivity(false);
                } else if (resultCode == RESULT_CANCELED) {
                    ToastUtil.showShort("User cancelled video recording");
                } else {
                    ToastUtil.showShort("Sorry! Failed to record video");
                }
                break;
            case LOAD_PIC:
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String imgPath = StorageUtil.imageGetPath(this, selectedImage);
                    launchUploadActivity(true, imgPath);
                }
                break;
        }
    }

    private void launchUploadActivity(boolean isImage) {
        launchUploadActivity(isImage, fileUri.getPath());
    }

    private void launchUploadActivity(boolean isImage, String path) {
        Intent i = new Intent(MainActivity.this, UploadActivity.class);
        i.putExtra(FILE_PATH, path);
        i.putExtra(IS_IMAGE, isImage);
        startActivity(i);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_SAVE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_SAVE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        Log.d(TAG, mediaStorageDir.getPath());

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        Log.d(TAG, mediaFile.getPath());

        return mediaFile;
    }

    /**
     * ------------ Permission request after Android 6.0 ----------------------
     * */

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < permissions.length; ++i) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= 23) {
                for (int i = 0; i < grantResults.length; ++i) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        boolean b = shouldShowRequestPermissionRationale(permissions[i]);
                        if (!b) {
                            showDialogTipUserGoToAppSetting();
                        } else {
                            finish();
                        }
                    }
                }
            }
        }
    }

    private void showDialogTipUserGoToAppSetting() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("Permission is denied")
                .setMessage("Please set the permission requested in the app.")
                .setPositiveButton("Goto", (DialogInterface dialog, int which) -> {
                    goToAppSetting();
                })
                .setNegativeButton("Cancel", (DialogInterface dialog, int which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }
}
