package sjtu.edu.cn.magic_wardrobe.activity;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by HgS_1217_ on 2017/11/10.
 */

public abstract class BaseActivity extends Activity {
    /**
     * Base Activity for all activities, in order to put redundant codes.
     */

    private CompositeSubscription compositeSubscription;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
    }

    abstract protected int getContentViewId();

    public CompositeSubscription getCompositeSubscription() {
        if (this.compositeSubscription == null) {
            this.compositeSubscription = new CompositeSubscription();
        }

        return this.compositeSubscription;
    }


    public void addSubscription(Subscription s) {
        if (this.compositeSubscription == null) {
            this.compositeSubscription = new CompositeSubscription();
        }

        this.compositeSubscription.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.compositeSubscription != null) {
            this.compositeSubscription.unsubscribe();
        }
    }
}
