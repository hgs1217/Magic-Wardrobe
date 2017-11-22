package sjtu.edu.cn.magic_wardrobe.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import sjtu.edu.cn.magic_wardrobe.R;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_mode_upload)
    Button btnUpload;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_icon)
    ImageView toolbarIcon;


    private static final int PERMISSION_REQUEST_CODE = 100;

    // All the permissions which must be requested in the app after Android 6.0
    private String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private AlertDialog dialog;


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
        btnUpload.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, UploadChooseActivity.class);
            startActivity(intent);
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == PERMISSION_REQUEST_CODE) {
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
        }
    }
}
