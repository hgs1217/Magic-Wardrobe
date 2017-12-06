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
import android.widget.FrameLayout;
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
import sjtu.edu.cn.magic_wardrobe.widget.ViewDragFrameLayout;

import static sjtu.edu.cn.magic_wardrobe.utils.ViewUtil.dpToPx;

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
    @BindView(R.id.mark_posture_1)
    ImageView markPosture1;
    @BindView(R.id.mark_posture_2)
    ImageView markPosture2;
    @BindView(R.id.mark_posture_3)
    ImageView markPosture3;
    @BindView(R.id.mark_posture_4)
    ImageView markPosture4;
    @BindView(R.id.drag_posture)
    ViewDragFrameLayout dragPosture;
    @BindView(R.id.recycler_view_search_once)
    RecyclerView recyclerViewSearch;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_icon)
    ImageView toolbarIcon;


    public static final String TAG = "SearchActivity";

    public static final int POSTURE_HEIGHT = 230;
    public static final int POSTURE_MARK_SIZE = 14;


    private String filePath;
    private String onlinePath;
    private Context context;
    private PostureParams params;
    private SearchRecyclerViewAdapter adapter;
    private List<String> imgPaths = new ArrayList<>();

    private BitmapFactory.Options options;
    private Bitmap bitmap;
    private double scalar;
    private NetworkAPI api;
    private int markOffset;

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
        bitmap = BitmapFactory.decodeFile(filePath, options);
        imgPosture.setImageBitmap(bitmap);
        scalar = (double) dpToPx(POSTURE_HEIGHT) / imgPosture.getDrawable().getIntrinsicHeight();

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

        dragPosture.setSearchActivity(this);
    }

    private void initRecyclerView(List<String> paths) {
        adapter = new SearchRecyclerViewAdapter(context, paths);
        adapter.setOnItemClickListener(searchOnItemClickListener);
        recyclerViewSearch.setAdapter(adapter);
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
    }

    private void initPostureMark() {
        markPosture1.setVisibility(View.VISIBLE);
        markPosture2.setVisibility(View.VISIBLE);
        markPosture3.setVisibility(View.VISIBLE);
        markPosture4.setVisibility(View.VISIBLE);

        markOffset = ViewUtil.dpToPx(POSTURE_MARK_SIZE / 2);
    }

    private void setPostureMark() {
        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) markPosture1.getLayoutParams();
        lp1.setMargins(viewPosture.getPoint1X() - markOffset, viewPosture.getPoint1Y() - markOffset, 0, 0);
        markPosture1.setLayoutParams(lp1);
        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) markPosture2.getLayoutParams();
        lp2.setMargins(viewPosture.getPoint2X() - markOffset, viewPosture.getPoint2Y() - markOffset, 0, 0);
        markPosture2.setLayoutParams(lp2);
        FrameLayout.LayoutParams lp3 = (FrameLayout.LayoutParams) markPosture3.getLayoutParams();
        lp3.setMargins(viewPosture.getPoint3X() - markOffset, viewPosture.getPoint3Y() - markOffset, 0, 0);
        markPosture3.setLayoutParams(lp3);
        FrameLayout.LayoutParams lp4 = (FrameLayout.LayoutParams) markPosture4.getLayoutParams();
        lp4.setMargins(viewPosture.getPoint4X() - markOffset, viewPosture.getPoint4Y() - markOffset, 0, 0);
        markPosture4.setLayoutParams(lp4);
    }

    private void postureAnalysis() {
        ToastUtil.showShort("Posture Analyzing...");
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
                            (int) (scalar * imgPosture.getDrawable().getIntrinsicWidth()),
                            dpToPx(POSTURE_HEIGHT),
                            imgPosture.getDrawable().getIntrinsicWidth(),
                            imgPosture.getDrawable().getIntrinsicHeight(),
                            params);

                    initPostureMark();
                    setPostureMark();
                    btnOnceSearch.setEnabled(true);
                    btnOnceSearch.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                }, NetworkFailureHandler.basicErrorHandler));
    }

    private void onceSearch() {
        ToastUtil.showShort("Once Searching...");
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

    public void setPostureMark(int markNum, int top, int left) {
        int height, width;
        switch (markNum) {
            case 1:
                height = params.getHeight() + params.getY() - top;
                width = params.getWidth() + params.getX() - left;
                params = new PostureParams(left, top, width, height);
                break;
            case 2:
                height = params.getHeight() + params.getY() - top;
                width = left - params.getX();
                params = new PostureParams(params.getX(), top, width, height);
                break;
            case 3:
                height = top - params.getY();
                width = params.getWidth() + params.getX() - left;
                params = new PostureParams(left, params.getY(), width, height);
                break;
            case 4:
                height = top - params.getY();
                width = left - params.getX();
                params = new PostureParams(params.getX(), params.getY(), width, height);
                break;
        }
        viewPosture.setPostureLoc(params);
        setPostureMark();
    }
}
