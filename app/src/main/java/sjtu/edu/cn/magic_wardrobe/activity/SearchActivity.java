package sjtu.edu.cn.magic_wardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import sjtu.edu.cn.magic_wardrobe.R;
import sjtu.edu.cn.magic_wardrobe.model.OnceSearchParams;
import sjtu.edu.cn.magic_wardrobe.model.PostureParams;
import sjtu.edu.cn.magic_wardrobe.utils.SocketCommunicationUtil;
import sjtu.edu.cn.magic_wardrobe.utils.ToastUtil;
import sjtu.edu.cn.magic_wardrobe.utils.ViewUtil;
import sjtu.edu.cn.magic_wardrobe.widget.PostureView;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.layout_search)
    LinearLayout mainLayout;
    @BindView(R.id.img_posture)
    ImageView imgPosture;
    @BindView(R.id.view_posture)
    PostureView viewPosture;
    @BindView(R.id.btn_posture_analysis)
    Button btnPostureAnalysis;
    @BindView(R.id.btn_once_search)
    Button btnOnceSearch;
    @BindView(R.id.edit_h)
    EditText editH;
    @BindView(R.id.edit_s)
    EditText editS;
    @BindView(R.id.edit_v)
    EditText editV;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_icon)
    ImageView toolbarIcon;


    public static final String TAG = "SearchActivity";

    public static final int POSTURE_HEIGHT = 230;


    private String filePath;
    private String onlinePath;
    private Context context;
    private PostureParams params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        filePath = intent.getStringExtra(UploadActivity.FILE_PATH);
        onlinePath = intent.getStringExtra(UploadActivity.ONLINE_PATH);

        context = this;

        initView();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    private void initView() {
        toolbarTitle.setText("Magic Wardrobe");
        toolbarIcon.setOnClickListener((v) -> {
            onBackPressed();
        });

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        imgPosture.setImageBitmap(bitmap);

        double scalar = (double) ViewUtil.dpToPx(POSTURE_HEIGHT) / bitmap.getHeight();

        btnPostureAnalysis.setOnClickListener((view) -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        params = SocketCommunicationUtil.postureAnalysis(onlinePath);
                        viewPosture.setParams(context,
                                ViewUtil.getScreenWidth() / 2,
                                (int) (scalar * bitmap.getWidth()),
                                ViewUtil.dpToPx(300),
                                imgPosture.getDrawable().getIntrinsicWidth() * options.inSampleSize,
                                imgPosture.getDrawable().getIntrinsicHeight() * options.inSampleSize,
                                params);
                        btnOnceSearch.setEnabled(true);
                        btnOnceSearch.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } catch (Exception e) {
                        ToastUtil.showLong("Connection Error");
                        e.printStackTrace();
                    }
                }
            }).start();
        });

        btnOnceSearch.setOnClickListener((view) -> {
            try {
                OnceSearchParams onceSearchParams = SocketCommunicationUtil.onceSearch(params, onlinePath);
                // TODO:
                ToastUtil.showLong("Once Search Success");
            } catch (Exception e) {
                ToastUtil.showLong("Connection Error");
                e.printStackTrace();
            }
        });

        mainLayout.setOnClickListener((View v) -> {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            editH.clearFocus();
            editS.clearFocus();
            editV.clearFocus();
        });
    }

    private void sendCommand(String cmd) {

    }
}