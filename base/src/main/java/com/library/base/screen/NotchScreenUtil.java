package com.library.base.screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;

import com.library.base.LogConsole;

import java.lang.reflect.Method;

/**
 * 屏幕判断
 */
public final class NotchScreenUtil {

    /**
     * 华为start
     */
    // 判断是否是华为刘海屏
    public static boolean hasNotchInScreenAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (Boolean) get.invoke(HwNotchSizeUtil);
            LogConsole.e("NotchScreenUtil", "this Huawei device has notch in screen？" + ret);
        } catch (ClassNotFoundException e) {
            LogConsole.e("NotchScreenUtil", "hasNotchInScreen ClassNotFoundException", e);
        } catch (NoSuchMethodException e) {
            LogConsole.e("NotchScreenUtil", "hasNotchInScreen NoSuchMethodException", e);
        } catch (Exception e) {
            LogConsole.e("NotchScreenUtil", "hasNotchInScreen Exception", e);
        }
        return ret;
    }

    /**
     * 获取华为刘海的高
     *
     * @param context
     * @return
     */
    public static int getNotchSizeAtHuawei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);

        } catch (ClassNotFoundException e) {
            LogConsole.e("NotchScreenUtil", "getNotchSize ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            LogConsole.e("NotchScreenUtil", "getNotchSize NoSuchMethodException");
        } catch (Exception e) {
            LogConsole.e("NotchScreenUtil", "getNotchSize Exception");
        }
        return ret[1];
    }

    /**
     * 华为end
     */

    /**
     * Oppo start
     */
    public static boolean hasNotchInScreenAtOppo(Context context) {
        boolean hasNotch = context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        LogConsole.e("NotchScreenUtil", "this OPPO device has notch in screen？" + hasNotch);
        return hasNotch;
    }

    public static int getNotchSizeAtOppo() {
        return 80;
    }

    /**
     * Oppo end
     */

    /**
     * vivo start
     */
    public static final int NOTCH_IN_SCREEN_VOIO = 0x00000020;// 是否有凹槽
    public static final int ROUNDED_IN_SCREEN_VOIO = 0x00000008;// 是否有圆角

    public static boolean hasNotchInScreenAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (Boolean) get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO);
            LogConsole.e("NotchScreenUtil", "this VIVO device has notch in screen？" + ret);
        } catch (ClassNotFoundException e) {
            LogConsole.e("NotchScreenUtil", "hasNotchInScreen ClassNotFoundException", e);
        } catch (NoSuchMethodException e) {
            LogConsole.e("NotchScreenUtil", "hasNotchInScreen NoSuchMethodException", e);
        } catch (Exception e) {
            LogConsole.e("NotchScreenUtil", "hasNotchInScreen Exception", e);
        }
        return ret;
    }

    public static int getNotchSizeAtVivo(Context context) {
        return dp2px(context, 32);
    }

    /**
     * vivo end
     */


    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    private static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */

    public final static int DEVICE_BRAND_OPPO = 0x0001;
    public final static int DEVICE_BRAND_HUAWEI = 0x0002;
    public final static int DEVICE_BRAND_VIVO = 0x0003;


    @SuppressLint("DefaultLocale")
    public static int getDeviceBrand() {
        String brand = android.os.Build.BRAND.trim().toUpperCase();
        if (brand.contains("HUAWEI")) {
            LogConsole.e("device brand", "HUAWEI");
            return DEVICE_BRAND_HUAWEI;
        } else if (brand.contains("OPPO")) {
            LogConsole.e("device brand", "OPPO");
            return DEVICE_BRAND_OPPO;
        } else if (brand.contains("VIVO")) {
            LogConsole.e("device brand", "VIVO");
            return DEVICE_BRAND_VIVO;
        }
        return 0;
    }

}