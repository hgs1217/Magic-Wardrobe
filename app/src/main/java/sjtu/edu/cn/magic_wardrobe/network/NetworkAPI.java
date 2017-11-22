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
import sjtu.edu.cn.magic_wardrobe.model.response.UploadResponse;

/**
 * Created by HgS_1217_ on 2017/11/10.
 */

public interface NetworkAPI {
    /**
     * Network API, the interfaces to server
     */

    // command call
    @FormUrlEncoded
    @POST("post")
    Observable<HttpResponse> postCall(@Field("cmd") String cmd);

    // get cmd call
    @GET("get")
    Observable<HttpResponse> getCall(@Query("cmd") @Nullable String cmd);

    @Multipart
    @POST("upload/img")
    Observable<UploadResponse> uploadImg(@Part MultipartBody.Part image);

    @Multipart
    @POST("upload/video")
    Observable<UploadResponse> uploadVideo(@Part MultipartBody.Part video);
}
