package sjtu.edu.cn.magic_wardrobe.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sjtu.edu.cn.magic_wardrobe.utils.Config;

/**
 * Created by HgS_1217_ on 2017/11/10.
 */

public class RetrofitClient {
    /**
     * Retrofit client for network communication
     */

    private static String BASE_URL = Config.BASE_URL;

    private static NetworkAPI networkAPI;

    public static NetworkAPI getNetworkAPI() {
        if (networkAPI == null) {
            Executor executor = Executors.newCachedThreadPool();
            final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateTypeAdapter()).create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .callbackExecutor(executor)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(OkHttpProvider.getInstance())
                    .build();

            networkAPI = retrofit.create(NetworkAPI.class);
        }
        return networkAPI;
    }
}
