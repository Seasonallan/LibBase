package com.library.base.adapt;
@Deprecated
public class AdaptScreenDesignConfig {
    private static int designWidth;
    private static int designHeight;

    public static void setDesignWidth(int designWidth) {
        AdaptScreenDesignConfig.designWidth = designWidth;
    }

    public static void setDesignHeight(int designHeight) {
        AdaptScreenDesignConfig.designHeight = designHeight;
    }

    public static int getDesignWidth() {
        return designWidth;
    }

    public static int getDesignHeight() {
        return designHeight;
    }
}
