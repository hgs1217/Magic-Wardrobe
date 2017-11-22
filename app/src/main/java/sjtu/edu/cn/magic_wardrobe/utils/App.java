package sjtu.edu.cn.magic_wardrobe.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class App extends Application {

    private static Context context;

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}