package sjtu.edu.cn.magic_wardrobe.model;

import java.util.ArrayList;
import java.util.List;

import sjtu.edu.cn.magic_wardrobe.model.response.ImageInfo;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class OnceSearchParams {

    private Double hue;
    private Double saturation;
    private Double value;
    private SearchAttribute attributes;
    private List<ImageInfo> imgInfos;

    public OnceSearchParams(Double h, Double s, Double v, List<Integer> attrs, List<ImageInfo> infos) {
        hue = h;
        saturation = s;
        value = v;
        attributes = new SearchAttribute(attrs);
        imgInfos = new ArrayList<>(infos);
    }

    public Double getHue() {
        return hue;
    }

    public Double getSaturation() {
        return saturation;
    }

    public Double getValue() {
        return value;
    }

    public SearchAttribute getAttributes() {
        return attributes;
    }

    public List<ImageInfo> getImgInfos() {
        return imgInfos;
    }
}
