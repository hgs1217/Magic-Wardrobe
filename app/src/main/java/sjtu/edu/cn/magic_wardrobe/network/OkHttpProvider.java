package sjtu.edu.cn.magic_wardrobe.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by HgS_1217_ on 2017/11/10.
 */

public class OkHttpProvider {

    private static OkHttpClient client;

    public static OkHttpClient getInstance() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT))
                    .build();
        }
        return client;
    }
}
