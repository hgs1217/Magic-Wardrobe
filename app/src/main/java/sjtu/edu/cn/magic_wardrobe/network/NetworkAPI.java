package sjtu.edu.cn.magic_wardrobe.network;

import android.support.annotation.Nullable;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;
import sjtu.edu.cn.magic_wardrobe.model.response.HttpResponse;
import sjtu.edu.cn.magic_wardrobe.model.response.OnceSearchResponse;
import sjtu.edu.cn.magic_wardrobe.model.response.PostureResponse;
import sjtu.edu.cn.magic_wardrobe.model.response.UploadResponse;

/**
 * Created by HgS_1217_ on 2017/11/10.
 */

/**
 * 1.姿势检测
 * foreground -> background:2;gender;type;imgurl;
 * background -> foreground:x;y;width;height;
 * <p>
 * 2.一次检索
 * foreground -> background:0;gender;type;x1,y1,w,h;imgUrl;alo=3
 * background -> foreground:h;s;v;9;attr1;attr2;attr3;...attr9;img1addr;width1;height1;img2addr;width2;height2;...
 * <p>
 * 3.二次检索
 * foreground -> background:1;gender;style;selectedArea;imgUrl;char1;char2;char3;...;attr1;attr2;...;color;subPlane;subPlane_addr;sub_main_area;sub_replace_area;
 * background -> foreground:img1addr?width?height?x1?y1?x2?y2;img2addr?width?height?x1?y1?x2?y2;....
 * Note:
 * 1.area:x1;y1;w;h
 * 2.subPlane_addr:what after "/challenge_db/" conntected to "E:/"
 * 3.h;s;v 0~1浮点数
 * <p>
 * <p>
 * 0;-1;-1;122;89;123;247;E:\DB_JD\女装_毛呢大衣\1237957043.jpg;3;
 */

public interface NetworkAPI {
    /**
     * Network API, the interfaces to server
     */

    // command call
    @FormUrlEncoded
    @POST("post")
    Observable<HttpResponse> postCall(@Field("cmd") String cmd);

    @Multipart
    @POST("upload/img")
    Observable<UploadResponse> uploadImg(@Part MultipartBody.Part image);

    @Multipart
    @POST("upload/video")
    Observable<UploadResponse> uploadVideo(@Part MultipartBody.Part video);

    @GET("/socket/posture")
    Observable<PostureResponse> postureAnalysis(@Query("gender") @Nullable Integer gender,
                                                @Query("type")   @Nullable Integer type,
                                                @Query("imgUrl") @Nullable String imgUrl);

    @GET("/socket/once_search")
    Observable<OnceSearchResponse> onceSearch(@Query("gender") @Nullable Integer gender,
                                              @Query("type")   @Nullable Integer type,
                                              @Query("imgUrl") @Nullable String imgUrl,
                                              @Query("x") @Nullable Integer x,
                                              @Query("y")   @Nullable Integer y,
                                              @Query("width") @Nullable Integer width,
                                              @Query("height") @Nullable Integer height,
                                              @Query("alo") @Nullable Integer alo);
}
