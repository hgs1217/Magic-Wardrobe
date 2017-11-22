package sjtu.edu.cn.magic_wardrobe.utils;

import android.widget.Toast;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class ToastUtil {

    public static void showShort(int resId) {
        Toast.makeText(App.getInstance(), resId, Toast.LENGTH_SHORT).show();
    }


    public static void showShort(String message) {
        Toast.makeText(App.getInstance(), message, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(int resId) {
        Toast.makeText(App.getInstance(), resId, Toast.LENGTH_LONG).show();
    }


    public static void showLong(String message) {
        Toast.makeText(App.getInstance(), message, Toast.LENGTH_LONG).show();
    }
}
