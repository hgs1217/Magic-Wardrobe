package sjtu.edu.cn.magic_wardrobe.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class UploadResponse extends HttpResponse {

    @SerializedName("fileName")
    private String fileName;
    @SerializedName("path")
    private String path;

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }
}
