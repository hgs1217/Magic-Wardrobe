package sjtu.edu.cn.magic_wardrobe.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sjtu.edu.cn.magic_wardrobe.R;
import sjtu.edu.cn.magic_wardrobe.model.OnceSearchParams;
import sjtu.edu.cn.magic_wardrobe.model.PostureParams;
import sjtu.edu.cn.magic_wardrobe.model.SearchAttribute;
import sjtu.edu.cn.magic_wardrobe.model.response.ImageInfo;
import sjtu.edu.cn.magic_wardrobe.model.response.OnceSearchResponse;
import sjtu.edu.cn.magic_wardrobe.model.response.PostureResponse;
import sjtu.edu.cn.magic_wardrobe.network.NetworkAPI;
import sjtu.edu.cn.magic_wardrobe.network.NetworkFailureHandler;
import sjtu.edu.cn.magic_wardrobe.network.RetrofitClient;
import sjtu.edu.cn.magic_wardrobe.utils.ToastUtil;
import sjtu.edu.cn.magic_wardrobe.utils.Util;
import sjtu.edu.cn.magic_wardrobe.utils.ViewUtil;
import sjtu.edu.cn.magic_wardrobe.widget.PostureView;
import sjtu.edu.cn.magic_wardrobe.widget.SearchRecyclerViewAdapter;
import sjtu.edu.cn.magic_wardrobe.widget.ViewDragFrameLayout;

import static sjtu.edu.cn.magic_wardrobe.utils.ViewUtil.dpToPx;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.img_posture)
    ImageView imgPosture;
    @BindView(R.id.view_posture)
    PostureView viewPosture;
    @BindView(R.id.btn_posture_analysis)
    Button btnPostureAnalysis;
    @BindView(R.id.btn_once_search)
    Button btnOnceSearch;
    @BindView(R.id.color_search)
    ImageView colorSearch;
    @BindView(R.id.text_h)
    TextView textH;
    @BindView(R.id.text_s)
    TextView textS;
    @BindView(R.id.text_v)
    TextView textV;
    @BindView(R.id.text_age)
    TextView textAge;
    @BindView(R.id.text_category)
    TextView textCategory;
    @BindView(R.id.text_collar)
    TextView textCollar;
    @BindView(R.id.text_material)
    TextView textMaterial;
    @BindView(R.id.text_pattern)
    TextView textPattern;
    @BindView(R.id.text_placket)
    TextView textPlacket;
    @BindView(R.id.text_shape)
    TextView textShape;
    @BindView(R.id.text_sleeve)
    TextView textSleeve;
    @BindView(R.id.text_style)
    TextView textStyle;
    @BindView(R.id.layout_color)
    LinearLayout layoutColor;
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
    private int currentColor;

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
        currentColor = Color.argb(255, 0, 0, 0);

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
        layoutColor.setOnClickListener((view) -> {
            ColorPickerDialogBuilder
                    .with(context)
                    .setTitle("Choose a color")
                    .initialColor(currentColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener((int selectedColor) -> {})
                    .setPositiveButton("ok", (DialogInterface dialog, int selectedColor, Integer[] allColors) -> {
                        currentColor = selectedColor;
                        resetColor(Integer.toHexString(selectedColor));
                    })
                    .setNegativeButton("cancel", (DialogInterface dialog, int which) -> {
                    })
                    .build()
                    .show();
        });

        initRecyclerView(imgPaths);

//        params = new PostureParams(100, 100, 100, 100);
//        btnOnceSearch.setEnabled(true);
//        btnOnceSearch.setBackgroundColor(getResources().getColor(R.color.btn_bg));

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

    private void fillParams(OnceSearchParams pms) {
        SearchAttribute attr = pms.getAttributes();
        textAge.setText(attr.getContent(SearchAttribute.MODE_AGE));
        textCategory.setText(attr.getContent(SearchAttribute.MODE_CATEGORY));
        textCollar.setText(attr.getContent(SearchAttribute.MODE_COLLAR));
        textMaterial.setText(attr.getContent(SearchAttribute.MODE_MATERIAL));
        textPattern.setText(attr.getContent(SearchAttribute.MODE_PATTERN));
        textPlacket.setText(attr.getContent(SearchAttribute.MODE_PLACKET));
        textShape.setText(attr.getContent(SearchAttribute.MODE_SHAPE));
        textSleeve.setText(attr.getContent(SearchAttribute.MODE_SLEEVE));
        textStyle.setText(attr.getContent(SearchAttribute.MODE_STYLE));

        textH.setText(String.valueOf(Math.round(pms.getHue())));
        textS.setText(String.valueOf((double) Math.round(pms.getSaturation() * 100) / 100));
        textV.setText(String.valueOf((double) Math.round(pms.getValue() * 100) / 100));
        int[] rgb = Util.hsvToRgba(pms.getHue(), pms.getSaturation(), pms.getValue());
        currentColor = Color.argb(255, rgb[0], rgb[1], rgb[2]);
        colorSearch.setBackgroundColor(currentColor);
    }

    private void resetColor(String rgba) {
        double[] hsv = Util.rgbstrToHsv(rgba.substring(2));
        textH.setText(String.valueOf(Math.round(hsv[0])));
        textS.setText(String.valueOf((double) Math.round(hsv[1] * 100) / 100));
        textV.setText(String.valueOf((double) Math.round(hsv[2] * 100) / 100));
        colorSearch.setBackgroundColor(currentColor);
    }

    private void setPostureMarkLoc() {
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
                    ToastUtil.showShort("Posture Response Success");
                    this.params = params;

                    viewPosture.setParams(context,
                            ViewUtil.getScreenWidth() / 2,
                            (int) (scalar * imgPosture.getDrawable().getIntrinsicWidth()),
                            dpToPx(POSTURE_HEIGHT),
                            imgPosture.getDrawable().getIntrinsicWidth(),
                            imgPosture.getDrawable().getIntrinsicHeight(),
                            params);

                    initPostureMark();
                    setPostureMarkLoc();
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
                    ToastUtil.showShort("Once Search Success");
                    imgPaths.clear();
                    List<ImageInfo> infos = params.getImgInfos();
                    for (ImageInfo info : infos) {
                        imgPaths.add(info.getImgUrl());
                    }
                    initRecyclerView(imgPaths);
                    fillParams(params);
                }, NetworkFailureHandler.basicErrorHandler));
    }

    public void setPostureMark(int markNum, int top, int left) {
        viewPosture.setPostureMark(markNum, top, left);
        params = viewPosture.getPostureParams();
        setPostureMarkLoc();
    }
}
