package sjtu.edu.cn.magic_wardrobe.network;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class ProgressRequestBody extends RequestBody {

    private final RequestBody requestBody;

    private final UploadProgressListener progressListener;

    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, UploadProgressListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (null == bufferedSink) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long writtenBytesCount = 0L;
            long totalBytesCount = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                writtenBytesCount += byteCount;
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                Observable.just(writtenBytesCount).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        progressListener.onProgress(writtenBytesCount, totalBytesCount);
                    }
                });
            }
        };
    }

    public interface UploadProgressListener {
        void onProgress(long currentBytesCount, long totalBytesCount);
    }
}
