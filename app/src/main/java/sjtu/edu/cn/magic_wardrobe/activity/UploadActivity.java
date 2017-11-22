package sjtu.edu.cn.magic_wardrobe.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sjtu.edu.cn.magic_wardrobe.R;
import sjtu.edu.cn.magic_wardrobe.model.response.UploadResponse;
import sjtu.edu.cn.magic_wardrobe.network.NetworkAPI;
import sjtu.edu.cn.magic_wardrobe.network.NetworkFailureHandler;
import sjtu.edu.cn.magic_wardrobe.network.ProgressRequestBody;
import sjtu.edu.cn.magic_wardrobe.network.RetrofitClient;
import sjtu.edu.cn.magic_wardrobe.utils.StorageUtil;
import sjtu.edu.cn.magic_wardrobe.utils.ToastUtil;

import static sjtu.edu.cn.magic_wardrobe.activity.UploadChooseActivity.LOAD_PIC;

public class UploadActivity extends BaseActivity {

    @BindView(R.id.text_percentage)
    TextView txtPercentage;
    @BindView(R.id.img_preview)
    ImageView imgPreview;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.video_preview)
    VideoView vidPreview;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.btn_posture)
    Button btnPosture;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_icon)
    ImageView toolbarIcon;


    private static final String TAG = "UploadActivity";

    public static final String FILE_PATH = "FILE_PATH";
    public static final String ONLINE_PATH = "ONLINE_PATH";


    private String filePath;
    private String onlinePath;
    private boolean isImage = true;
    private NetworkAPI api;

    private ProgressRequestBody.UploadProgressListener uploadProgressListener = new ProgressRequestBody.UploadProgressListener() {
        @Override
        public void onProgress(long currentBytesCount, long totalBytesCount) {
            progressBar.setMax((int) totalBytesCount);
            progressBar.setProgress((int) currentBytesCount);
            txtPercentage.setText(
                    String.valueOf((int) (currentBytesCount * 100.0 / totalBytesCount)) + "%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        filePath = intent.getStringExtra(UploadChooseActivity.FILE_PATH);
        isImage = intent.getBooleanExtra(UploadChooseActivity.IS_IMAGE, true);

        api = RetrofitClient.getNetworkAPI();

        initView();

        if (filePath != null) {
            previewMedia(isImage);
        } else {
            ToastUtil.showShort("Sorry, file path is missing!");
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload;
    }

    private void initView() {
        toolbarTitle.setText("Upload");
        toolbarIcon.setOnClickListener((v) -> {
            onBackPressed();
        });
        btnUpload.setOnClickListener((v) -> {
            File file = new File(filePath);
            if (isImage) {
                uploadImg(file);
            } else {
                uploadVideo(file);
            }
        });
        imgPreview.setOnClickListener((v) -> {
            chooseImg();
        });
        btnPosture.setOnClickListener((v) -> {
            Intent i = new Intent(UploadActivity.this, SearchActivity.class);
            i.putExtra(FILE_PATH, filePath);
            i.putExtra(ONLINE_PATH, onlinePath);
            startActivity(i);
        });
    }

    private void chooseImg() {
        btnPosture.setEnabled(false);
        btnPosture.setBackgroundColor(getResources().getColor(R.color.gray));

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, UploadChooseActivity.LOAD_PIC);
    }

    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            vidPreview.start();
        }
    }

    private void uploadImg(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(),
                new ProgressRequestBody(requestBody, uploadProgressListener));

        ToastUtil.showShort("上传中");
        addSubscription(api.uploadImg(body)
                .flatMap(NetworkFailureHandler.httpFailureFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> ((UploadResponse) response).getPath())
                .subscribe(path -> {
                    onlinePath = path;
                    ToastUtil.showLong("File name: " + onlinePath);
                    btnPosture.setEnabled(true);
                    btnPosture.setBackgroundColor(getResources().getColor(R.color.btn_bg));

                }, NetworkFailureHandler.basicErrorHandler));
    }

    private void uploadVideo(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(),
                new ProgressRequestBody(requestBody, uploadProgressListener));

        ToastUtil.showShort("上传中");
        addSubscription(api.uploadVideo(body)
                .flatMap(NetworkFailureHandler.httpFailureFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> ((UploadResponse) response).getPath())
                .subscribe(path -> {
                    onlinePath = path;
                    ToastUtil.showLong("File name: " + onlinePath);
                    btnPosture.setEnabled(true);
                    btnPosture.setBackgroundColor(getResources().getColor(R.color.btn_bg));

                }, NetworkFailureHandler.basicErrorHandler));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOAD_PIC) {

            if (data != null) {
                Uri selectedImage = data.getData();
                filePath = StorageUtil.imageGetPath(this, selectedImage);
                previewMedia(true);
            }
        }
    }
}
