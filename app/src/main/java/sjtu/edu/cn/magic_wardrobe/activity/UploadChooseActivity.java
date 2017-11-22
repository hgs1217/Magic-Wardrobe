package sjtu.edu.cn.magic_wardrobe.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

public class UploadChooseActivity extends BaseActivity {

    @BindView(R.id.btn_capture_image)
    Button btnCapturePicture;
    @BindView(R.id.btn_choose_image)
    Button btnChooseImage;
    @BindView(R.id.btn_record_video)
    Button btnRecordVideo;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_icon)
    ImageView toolbarIcon;

    // LogCat tag
    private static final String TAG = "UploadChooseActivity";

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 101;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final int LOAD_PIC = 3;
    public static final int LOAD_VIDEO = 4;

    public static final String IS_IMAGE = "IS_IMAGE";
    public static final String FILE_PATH = "FILE_PATH";


    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        if (!isDeviceSupportCamera()) {
            ToastUtil.showLong("Sorry! Your device doesn't support camera");
            finish();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_choose;
    }

    private void initView() {
        toolbarTitle.setText("Magic Wardrobe");
        toolbarIcon.setOnClickListener((v) -> {
            onBackPressed();
        });
        btnCapturePicture.setOnClickListener((v) -> {
            captureImage();
        });
        btnRecordVideo.setOnClickListener((v) -> {
            recordVideo();
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

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                launchUploadActivity(true);

            } else if (resultCode == RESULT_CANCELED) {
                ToastUtil.showShort("User cancelled image capture");

            } else {
                ToastUtil.showShort("Sorry! Failed to capture image");
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {
                ToastUtil.showShort("User cancelled video recording");

            } else {
                ToastUtil.showShort("Sorry! Failed to record video");
            }

        } else if (requestCode == LOAD_PIC) {

            if (data != null) {
                Uri selectedImage = data.getData();
                String imgPath = StorageUtil.imageGetPath(this, selectedImage);
                launchUploadActivity(true, imgPath);
            }
        }
    }

    private void launchUploadActivity(boolean isImage) {
        launchUploadActivity(isImage, fileUri.getPath());
    }

    private void launchUploadActivity(boolean isImage, String path) {
        Intent i = new Intent(UploadChooseActivity.this, UploadActivity.class);
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
}
