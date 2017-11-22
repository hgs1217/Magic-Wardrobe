package sjtu.edu.cn.magic_wardrobe.utils;

import android.app.Activity;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class ViewUtil {

    public static int getWindowRotation(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getRotation();
    }

    public static int getScreenWidth() {
        return App.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return App.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * dp -> px 单位转化
     **/
    public static int pxToDp(int px) {
        double density = App.getInstance().getResources().getDisplayMetrics().density;
        double dpFloat = px / density;
        return (int) (dpFloat + 0.5);
    }

    public static int dpToPx(int dp) {
        double density = App.getInstance().getResources().getDisplayMetrics().density;
        double dpFloat = dp * density;
        return (int) (dpFloat + 0.5);
    }
}
