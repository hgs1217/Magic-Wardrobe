package sjtu.edu.cn.magic_wardrobe.network;

import android.text.TextUtils;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import sjtu.edu.cn.magic_wardrobe.model.response.HttpResponse;
import sjtu.edu.cn.magic_wardrobe.utils.ToastUtil;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class NetworkFailureHandler {
    /**
     * 处理网络故障的控制类
     */

    public static void onError(Throwable e) {
        if (e instanceof HttpException) {
            ToastUtil.showShort("Error code " + ((HttpException) e).code());
        } else {
            if (TextUtils.isEmpty(e.getMessage())) {
                ToastUtil.showShort("Network failure");
            } else {
                ToastUtil.showShort(e.getMessage());
            }
        }
        e.printStackTrace();
    }

    public static final Action1<Throwable> basicErrorHandler = throwable -> onError(throwable);

    public static final Func1<HttpResponse, Observable<HttpResponse>> httpFailureFilter =
            httpResponse -> {
                if (httpResponse.getError() == 0) {
                    return Observable.just(httpResponse);
                } else {
                    ToastUtil.showShort(httpResponse.getMsg());
                    return Observable.error(new Exception(httpResponse.getMsg()));
                }
            };
}
