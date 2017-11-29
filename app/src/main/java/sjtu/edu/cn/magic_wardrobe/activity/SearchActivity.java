package sjtu.edu.cn.magic_wardrobe.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sjtu.edu.cn.magic_wardrobe.R;
import sjtu.edu.cn.magic_wardrobe.model.PostureParams;
import sjtu.edu.cn.magic_wardrobe.model.response.ImageInfo;
import sjtu.edu.cn.magic_wardrobe.model.response.OnceSearchResponse;
import sjtu.edu.cn.magic_wardrobe.model.response.PostureResponse;
import sjtu.edu.cn.magic_wardrobe.network.NetworkAPI;
import sjtu.edu.cn.magic_wardrobe.network.NetworkFailureHandler;
import sjtu.edu.cn.magic_wardrobe.network.RetrofitClient;
import sjtu.edu.cn.magic_wardrobe.utils.ToastUtil;
import sjtu.edu.cn.magic_wardrobe.utils.ViewUtil;
import sjtu.edu.cn.magic_wardrobe.widget.PostureView;
import sjtu.edu.cn.magic_wardrobe.widget.SearchRecyclerViewAdapter;

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
    @BindView(R.id.recycler_view_search_once)
    RecyclerView recyclerViewSearch;
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
    private SearchRecyclerViewAdapter adapter;
    private List<String> imgPaths = new ArrayList<>();

    BitmapFactory.Options options;
    Bitmap bitmap;
    double scalar;
    private NetworkAPI api;

    private SearchRecyclerViewAdapter.OnItemClickListener searchOnItemClickListener = new
            SearchRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(String path) {
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        filePath = intent.getStringExtra(UploadActivity.FILE_PATH);
        onlinePath = intent.getStringExtra(UploadActivity.ONLINE_PATH);

        context = this;
        api = RetrofitClient.getNetworkAPI();

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

        options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        imgPosture.setImageBitmap(bitmap);
        scalar = (double) ViewUtil.dpToPx(POSTURE_HEIGHT) / bitmap.getHeight();

        btnPostureAnalysis.setOnClickListener((view) -> {
            postureAnalysis();
        });

        btnOnceSearch.setOnClickListener((view) -> {
            onceSearch();
        });

        mainLayout.setOnClickListener((View v) -> {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            editH.clearFocus();
            editS.clearFocus();
            editV.clearFocus();
        });

        initRecyclerView(imgPaths);
    }

    private void initRecyclerView(List<String> paths) {
        adapter = new SearchRecyclerViewAdapter(context, paths);
        adapter.setOnItemClickListener(searchOnItemClickListener);
        recyclerViewSearch.setAdapter(adapter);
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
    }

    private void postureAnalysis() {
        addSubscription(api.postureAnalysis(-1, -1, onlinePath)
                .flatMap(NetworkFailureHandler.httpFailureFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> ((PostureResponse) response).getPostureParams())
                .subscribe(params -> {
                    ToastUtil.showLong("Posture Response Success");
                    this.params = params;

                    viewPosture.setParams(context,
                            ViewUtil.getScreenWidth() / 2,
                            (int) (scalar * bitmap.getWidth()),
                            ViewUtil.dpToPx(300),
                            imgPosture.getDrawable().getIntrinsicWidth() * options.inSampleSize,
                            imgPosture.getDrawable().getIntrinsicHeight() * options.inSampleSize,
                            params);
                    btnOnceSearch.setEnabled(true);
                    btnOnceSearch.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                }, NetworkFailureHandler.basicErrorHandler));
    }

    private void onceSearch() {
        addSubscription(api.onceSearch(-1, -1, onlinePath, params.getX(), params.getY(),
                params.getWidth(), params.getHeight(), 3)
                .flatMap(NetworkFailureHandler.httpFailureFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> ((OnceSearchResponse) response).getOnceSearchParams())
                .subscribe(params -> {
                    ToastUtil.showLong("Once Search Success");
                    imgPaths.clear();
                    List<ImageInfo> infos = params.getImgInfos();
                    for (ImageInfo info : infos) {
                        imgPaths.add(info.getImgUrl());
                    }
                    initRecyclerView(imgPaths);
                    editH.setText(String.valueOf(params.getHue()));
                    editS.setText(String.valueOf(params.getSaturation()));
                    editV.setText(String.valueOf(params.getValue()));
                }, NetworkFailureHandler.basicErrorHandler));
    }
}
