package sjtu.edu.cn.magic_wardrobe.model;

import java.util.List;

/**
 * Created by HgS_1217_ on 2017/12/15.
 */

public class SearchAttribute {

    public static final int MODE_SHAPE = 0;
    public static final int MODE_PATTERN = 1;
    public static final int MODE_MATERIAL = 2;
    public static final int MODE_COLLAR = 3;
    public static final int MODE_SLEEVE = 4;
    public static final int MODE_PLACKET = 5;
    public static final int MODE_STYLE = 6;
    public static final int MODE_AGE = 7;
    public static final int MODE_CATEGORY = 8;

    public static final int GENDER_FEMALE = 0;
    public static final int GENDER_MALE = 1;

    public static final int SHAPE_SLIM = 1;
    public static final int SHAPE_STANDARD = 2;
    public static final int SHAPE_LOOSE = 3;
    public static final int SHAPE_OTHERS = 4;
    public static final int SHAPE_CLOAK = 5;
    public static final int SHAPE_SHAWL = 6;

    public static final int PATTERN_PLAID = 1;
    public static final int PATTERN_SOLID = 2;
    public static final int PATTERN_DOT = 3;
    public static final int PATTERN_STRIPE = 4;
    public static final int PATTERN_FLOWER = 5;
    public static final int PATTERN_OTHERS = 6;
    public static final int PATTERN_ANIMAL = 7;

    public static final int MATERIAL_SILK = 1;
    public static final int MATERIAL_LINEN = 2;
    public static final int MATERIAL_COTTON = 3;
    public static final int MATERIAL_DOWN = 4;
    public static final int MATERIAL_WOOLEN = 5;
    public static final int MATERIAL_OTHERS = 6;

    public static final int COLLAR_NOTCHED = 1;
    public static final int COLLAR_VNECK = 2;
    public static final int COLLAR_SHIRT = 3;
    public static final int COLLAR_HIGH = 4;
    public static final int COLLAR_ROUND = 5;
    public static final int COLLAR_OTHERS = 6;
    public static final int COLLAR_HOODED = 7;

    public static final int SLEEVE_SHORT = 1;
    public static final int SLEEVE_LONG = 2;
    public static final int SLEEVE_SLEEVELESS = 3;
    public static final int SLEEVE_MIDDLE_LENGTH = 4;
    public static final int SLEEVE_OTHERS = 5;

    public static final int PLACKET_OTHERS = 1;
    public static final int PLACKET_ZIP = 2;
    public static final int PLACKET_DOUBLE_BREASTED = 3;
    public static final int PLACKET_SINGLE_BREASTED = 4;

    public static final int STYLE_JAPANESE_OR_SOUTH_KOREAN = 1;
    public static final int STYLE_BUSINESS = 2;
    public static final int STYLE_OTHERS = 3;
    public static final int STYLE_SWEET = 4;
    public static final int STYLE_ELEGANT = 5;
    public static final int STYLE_CHARMING = 6;
    public static final int STYLE_EUROPEAN = 7;
    public static final int STYLE_PUNK = 8;
    public static final int STYLE_MILITARY = 9;

    public static final int AGE_TEENAGER_OR_YOUTH = 1;
    public static final int AGE_MIDDLE_AGED_OR_ELDERLY = 2;

    public static final int CATEGORY_JACKET = 1;
    public static final int CATEGORY_VEST = 2;
    public static final int CATEGORY_SUIT = 3;
    public static final int CATEGORY_WINDBREAKER = 4;
    public static final int CATEGORY_SHIRT = 5;
    public static final int CATEGORY_TSHIRT = 6;
    public static final int CATEGORY_EIDERDOWN = 7;
    public static final int CATEGORY_COAT = 8;

    public static final String[] contentShape = new String[] {
            "Slim", "Standard", "Loose", "Others", "Cloak", "Shawl"};
    public static final String[] contentPattern = new String[] {
            "Plaid", "Solid", "Dot", "Stripe", "Flower", "Others", "Animal"};
    public static final String[] contentMaterial = new String[] {
            "Silk", "Linen", "Cotton", "Down", "Woolen", "Others"};
    public static final String[] contentCollar = new String[] {
            "Notched", "V-Neck", "Shirt", "High", "Round", "Others", "Hooded"};
    public static final String[] contentSleeve = new String[] {
            "Short", "Long", "Sleeveless", "Middle-length", "Others"};
    public static final String[] contentPlacket = new String[] {
            "Others", "Zip", "Double-breasted", "Single-breasted"};
    public static final String[] contentStyle = new String[] {
            "Japanese/Korean", "Business", "Others", "Sweet", "Elegant", "Charming",
            "European", "Punk", "Military"};
    public static final String[] contentAge = new String[] {
            "Teenager/Youth", "Middle-aged/Elderly"};
    public static final String[] contentCategory = new String[] {
            "Jacket", "Vest", "Suit", "Windbreaker", "Shirt", "T-shirt", "Eiderdown", "Coat"};

    private int shape;
    private int pattern;
    private int material;
    private int collar;
    private int sleeve;
    private int placket;
    private int style;
    private int age;
    private int category;

    public SearchAttribute(List<Integer> attrList) {
        shape = attrList.get(0);
        pattern = attrList.get(1);
        material = attrList.get(2);
        collar = attrList.get(3);
        sleeve = attrList.get(4);
        placket = attrList.get(5);
        style = attrList.get(6);
        age = attrList.get(7);
        category = attrList.get(8);
    }

    public String getContent(int mode) {
        switch (mode) {
            case MODE_SHAPE:
                return contentShape[shape-1];
            case MODE_PATTERN:
                return contentPattern[pattern-1];
            case MODE_MATERIAL:
                return contentMaterial[material-1];
            case MODE_COLLAR:
                return contentCollar[collar-1];
            case MODE_SLEEVE:
                return contentSleeve[sleeve-1];
            case MODE_PLACKET:
                return contentPlacket[placket-1];
            case MODE_STYLE:
                return contentStyle[style-1];
            case MODE_AGE:
                return contentAge[age-1];
            case MODE_CATEGORY:
                return contentCategory[category-1];
        }
        return "Unknown";
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public int getPattern() {
        return pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public int getCollar() {
        return collar;
    }

    public void setCollar(int collar) {
        this.collar = collar;
    }

    public int getSleeve() {
        return sleeve;
    }

    public void setSleeve(int sleeve) {
        this.sleeve = sleeve;
    }

    public int getPlacket() {
        return placket;
    }

    public void setPlacket(int placket) {
        this.placket = placket;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
