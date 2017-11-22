package sjtu.edu.cn.magic_wardrobe.model;

import java.util.List;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class OnceSearchParams {

    private double hue;
    private double saturation;
    private double value;
    private List<Double> attributes;
    private List<ImageInfo> imgInfos;

    public OnceSearchParams(String cmd) {
        String[] substrings = cmd.split(";");
        try {
            hue = Double.parseDouble(substrings[0]);
            saturation = Double.parseDouble(substrings[1]);
            value = Double.parseDouble(substrings[2]);
            int num = Integer.parseInt(substrings[3]);
            for (int i=0; i<num; ++i) {
                attributes.add(Double.parseDouble(substrings[4+i]));
                int k = 4 + num + 3 * i;
                imgInfos.add(new ImageInfo(substrings[k], Integer.parseInt(substrings[k+1]),
                        Integer.parseInt(substrings[k+2])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getValue() {
        return value;
    }

    public List<Double> getAttributes() {
        return attributes;
    }

    public List<ImageInfo> getImgInfos() {
        return imgInfos;
    }
}
