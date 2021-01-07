package com.library.base.language;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.webkit.WebView;

import androidx.recyclerview.R;

import com.library.base.GlobalContext;

import java.util.ArrayList;
import java.util.Locale;


/**
 * 多语言切换
 */

public class LanguageUtil {

    /**
     * 设置本地化语言
     *
     * @param context
     * @param locale
     */
    private static void setLocale(Context context, Locale locale) {
        try {
            // 解决webview所在的activity语言没有切换问题
            new WebView(context).destroy();
        }catch (Exception e){}
        // 切换语言
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, dm);

    }

    /**
     * 根据type获取locale
     *
     * @param type
     * @return
     */
    private static Locale getLocaleByType(int type) {
        Locale locale;
        // 应用用户选择语言
        switch (type) {
            case 0:
                //由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LocaleList localeList = LocaleList.getDefault();
                    int spType = getLanguageType();
                    // 如果app已选择不跟随系统语言，则取第二个数据为系统默认语言
                    if (spType != 0 && localeList.size() > 1) {
                        locale = localeList.get(1);
                    } else {
                        locale = localeList.get(0);
                    }
                } else {
                    locale = Locale.getDefault();
                }
                break;
            case 3:
                locale = Locale.ENGLISH;
                break;
            case 2:
                locale = Locale.TAIWAN;
                break;
            case 4:
                locale = Locale.JAPAN;
                break;
            case 5:
                locale = Locale.FRANCE;
                break;
            default:
                locale = Locale.CHINESE;
                break;
        }
        return locale;
    }


    /**
     * 1 中文 2 英文 3 繁体
     *
     * @return
     */
    public static String getLanguageHttpCode() {
        switch (getLanguageType()) {
            case 0:
                Locale locale;
                //由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LocaleList localeList = LocaleList.getDefault();
                    int spType = getLanguageType();
                    // 如果app已选择不跟随系统语言，则取第二个数据为系统默认语言
                    if (spType != 0 && localeList.size() > 1) {
                        locale = localeList.get(1);
                    } else {
                        locale = localeList.get(0);
                    }
                } else {
                    locale = Locale.getDefault();
                }

                String language = locale.getLanguage();
                if (language.equals("en")) {
                    return "2";
                } else if (language.equals("zh")) {
                    String country = locale.toString();
                    if (country.endsWith("Hant")) {
                        return "3";
                    }
                }else if (language.equals("ja")){
                    return "4";
                }
                if (locale.equals(Locale.ENGLISH) || locale.equals(Locale.US) || locale.equals(Locale.UK)) {
                    return "2";
                } else if (locale.equals(Locale.TAIWAN)) {
                    return "3";
                }else if (locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE)){
                    return "4";
                }else if (locale.equals(Locale.FRANCE) || locale.equals(Locale.FRENCH)){
                    return "5";
                }
                break;
            case 3:
                return "2";
            case 2:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
        }
        return "1";
    }

    /**
     * 根据sp数据设置本地化语言
     *
     * @param context
     */
    public static void setLocale(Context context) {
        if (sLocale == null){
            sLocale = getLocaleByType(getLanguageType());
        }
        setLocale(context, sLocale);
    }

    private static Locale sLocale;

    /**
     * sp存储本地语言类型
     *
     * @param type
     */
    public static void putLanguageType(int type) {
        sLocale = null;
        GlobalContext.app().getSharedPreferences("language",
                Context.MODE_PRIVATE).edit().putInt("language", type).commit();
    }

    /**
     * sp获取本地存储语言类型
     *
     * @return
     */
    public static int getLanguageType() {
        return GlobalContext.app().getSharedPreferences("language",
                Context.MODE_PRIVATE).getInt("language", 0);
    }

    public static ArrayList<String> getSupportLanguages(Context context) {
        ArrayList languages = new ArrayList<>();
        languages.add(context.getResources().getString(R.string.auto));
        languages.add("中文简体");
        languages.add("中文繁體");
        languages.add("English");
        languages.add("日本語");
        languages.add("Français");
        //languages.add(context.getResources().getString(R.string.english));
        //languages.add(context.getResources().getString(R.string.japan));
        //languages.add(context.getResources().getString(R.string.french));
        return languages;
    }

    /**
     * sp获取本地存储语言类型
     *
     * @return
     */
    public static String getLanguageDesc() {
        switch (getLanguageType()) {
            case 0:
                Locale locale;
                //由于API仅支持7.0，需要判断，否则程序会crash(解决7.0以上系统不能跟随系统语言问题)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    LocaleList localeList = LocaleList.getDefault();
                    int spType = getLanguageType();
                    // 如果app已选择不跟随系统语言，则取第二个数据为系统默认语言
                    if (spType != 0 && localeList.size() > 1) {
                        locale = localeList.get(1);
                    } else {
                        locale = localeList.get(0);
                    }
                } else {
                    locale = Locale.getDefault();
                }
                String language = locale.getLanguage();
                if (language.equals("en")) {
                    return "en";
                } else if (language.equals("zh")) {
                    String country = locale.toString();
                    if (country.endsWith("Hant")) {
                        return "zh_TW";
                    }
                }else if (language.equals("ja")){
                    return "jp";
                }
                if (locale.equals(Locale.ENGLISH) || locale.equals(Locale.US) || locale.equals(Locale.UK)) {
                    return "en";
                } else if (locale.equals(Locale.TAIWAN)) {
                    return "zh_TW";
                }else if (locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE)){
                    return "jp";
                }else if (locale.equals(Locale.FRANCE) || locale.equals(Locale.FRENCH)){
                    return "fr";
                }
                break;
            case 3:
                return "en";
            case 2:
                return "zh_TW";
            case 4:
                return "jp";
            case 5:
                return "fr";
        }
        return "zh_CN";
    }
}

