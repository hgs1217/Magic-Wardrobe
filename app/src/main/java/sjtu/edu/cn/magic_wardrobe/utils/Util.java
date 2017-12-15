package sjtu.edu.cn.magic_wardrobe.utils;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by HgS_1217_ on 2017/12/15.
 */

public class Util {

    public static final String TAG = "Util";

    public static double[] rgbstrToHsv(String rgb) {
        int R = Integer.parseInt(rgb.substring(0, 2), 16);
        int G = Integer.parseInt(rgb.substring(2, 4), 16);
        int B = Integer.parseInt(rgb.substring(4, 6), 16);
        return rgbToHsv(R, G, B);
    }

    public static double[] rgbToHsv(int R, int G, int B) {
        double r, g, b, cmax, cmin, delta;
        double H = 0, S, V;

        r = R / 255.0;
        g = G / 255.0;
        b = B / 255.0;
        Double[] rgb = new Double[] {r, g, b};
        List<Double> rgbList = Arrays.asList(rgb);

        cmax = Collections.max(rgbList);
        cmin = Collections.min(rgbList);
        delta = cmax - cmin;

        if (delta < 0.00001) {
            H = 0;
        } else if (cmax == r) {
            H = 60 * (((g-b)/delta) % 6);
        } else if (cmax == g) {
            H = 60 * (((b-r)/delta) + 2);
        } else if (cmax == b) {
            H = 60 * (((r-g)/delta) + 4);
        }

        if (cmax == 0) {
            S = 0;
        } else {
            S = delta / cmax;
        }

        V = cmax;
        return new double[] {H, S, V};
    }

    public static int[] hsvToRgba(double H, double S, double V) {
        double r = 0, g = 0, b = 0, c, x, m;
        int R, G, B, i = (int) (H / 60);

        c = V * S;
        x = c * (1 - Math.abs((H / 60) % 2 - 1));
        m = V - c;

        switch (i) {
            case 0:
                r = c;
                g = x;
                b = 0;
                break;
            case 1:
                r = x;
                g = c;
                b = 0;
                break;
            case 2:
                r = 0;
                g = c;
                b = x;
                break;
            case 3:
                r = 0;
                g = x;
                b = c;
                break;
            case 4:
                r = x;
                g = 0;
                b = c;
                break;
            case 5:
                r = c;
                g = 0;
                b = x;
                break;
            default:
                Log.e(TAG, "HSV Convertion with illegal value");
        }

        R = (int) ((r + m) * 255);
        G = (int) ((g + m) * 255);
        B = (int) ((b + m) * 255);

        return new int[] {R, G, B};
    }
}
