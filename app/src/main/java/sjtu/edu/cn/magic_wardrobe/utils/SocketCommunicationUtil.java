package sjtu.edu.cn.magic_wardrobe.utils;

import android.util.Log;

import sjtu.edu.cn.magic_wardrobe.model.OnceSearchParams;
import sjtu.edu.cn.magic_wardrobe.model.PostureParams;
import sjtu.edu.cn.magic_wardrobe.network.SocketClient;


/**
 * Created by HgS_1217_ on 2017/11/15.
 */


/**
 * 1.姿势检测
 * foreground -> background:2;gender;type;imgurl;
 * background -> foreground:x;y;width;height;
 * <p>
 * 2.一次检索
 * foreground -> background:0;gender;type;x1;y1;w;h;imgUrl;alo=3
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
 * 0;-1;-1;122;89;123;247;E:\DB_JD\女装_毛呢大衣\1237957043.jpg;1;2;5;1;2;3;1;1;8;
 */

public class SocketCommunicationUtil {


    public static final String TAG = "SocketCommunication";


    public static PostureParams postureAnalysis(int gender, int type, String imgUrl) {
        String[] s = new String[]{"2", String.valueOf(gender), String.valueOf(type), imgUrl};
        String cmd = "";
        for (String sub : s) {
            cmd += (sub + ";");
        }

        ToastUtil.showLong("Analyzing");
        SocketClient client = new SocketClient(Config.SOCKET_SITE, Config.SOCKET_PORT);
        String reply = client.sendMsg(cmd);
        Log.i(TAG, reply);
        client.closeSocket();
        return new PostureParams(reply);
    }

    public static PostureParams postureAnalysis(String imgUrl) {
        return postureAnalysis(-1, -1, imgUrl);
    }

    public static OnceSearchParams onceSearch(int gender, int type, PostureParams params, String imgUrl) {
        String[] s = new String[]{"0", String.valueOf(gender), String.valueOf(type),
                String.valueOf(params.getX()), String.valueOf(params.getY()),
                String.valueOf(params.getWidth()), String.valueOf(params.getHeight()),
                imgUrl, "1", "2", "5", "1", "2", "3", "1", "1", "8"};
        String cmd = "";
        for (String sub : s) {
            cmd += (sub + ";");
        }

        ToastUtil.showLong("Searching");
        SocketClient client = new SocketClient(Config.SOCKET_SITE, Config.SOCKET_PORT);
        String reply = client.sendMsg(cmd);
        Log.i(TAG, reply);
        client.closeSocket();
        return new OnceSearchParams(reply);
    }

    public static OnceSearchParams onceSearch(PostureParams params, String imgUrl) {
        return onceSearch(-1, -1, params, imgUrl);
    }
}
