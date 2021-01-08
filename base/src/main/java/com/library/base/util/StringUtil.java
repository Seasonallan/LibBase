package com.library.base.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 火龙裸 on 2020/3/12.
 */

public class StringUtil {
    /**
     * 判断手机号码是否合理
     *
     * @param phoneNum
     */
    public static boolean judgePhoneNum(String phoneNum) {
        return isMatchLength(phoneNum, 11) && isMobileNO(phoneNum);
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    private static boolean isMatchLength(String str, int length) {
        return !str.isEmpty() && str.length() == length;
    }

    /**
     * 获取邮箱隐示显示
     */
    public static String getEmailCipher(String pre, String email) {
        return getEmailCipher(pre, email, -1);
    }

    /**
     * 获取邮箱隐示显示
     */
    public static String getContractCipher(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        if (id.length() > 4) {
            return id.substring(id.length() - 4);
        }
        return id;
    }

    /**
     * 获取邮箱隐示显示
     */
    public static String getEmailCipher(String pre, String email, int size) {
        if (TextUtils.isEmpty(email)) {
            return "";
        }
        int index = email.indexOf("@");
        StringBuilder stringBuffer = new StringBuilder();
        if (index < 0) {
            if (!TextUtils.isEmpty(pre)) {
                stringBuffer.append("+");
                stringBuffer.append(pre);
                stringBuffer.append(" ");
            }
            if (email.length() >= 7) {
                stringBuffer.append(email.substring(0, 3));
                stringBuffer.append("****");
//                if (size > 0){
//                    for (int i = 0; i < size; i++) {
//                        stringBuffer.append("*");
//                    }
//                }else{
//                    for (int i = 3; i < email.length() - 4; i++) {
//                        stringBuffer.append("*");
//                    }
//                }
                stringBuffer.append(email.substring(email.length() - 4));
            } else {
                stringBuffer.append(email);
            }
        } else {
            String preString = email.substring(0, index);
            if (preString.length() > 3) {
                stringBuffer.append(preString.substring(0, 3));
//                if (size > 0){
//                    for (int i = 0; i < size; i++) {
//                        stringBuffer.append("*");
//                    }
//                }else{
//                    for (int i = 0; i < preString.length() - 3; i++) {
//                        stringBuffer.append("*");
//                    }
//                }
                stringBuffer.append("****");
            } else {
                if (preString.length() > 0) {
                    stringBuffer.append(preString.substring(0, 1));
                } else {
                    stringBuffer.append(preString);
                }
                stringBuffer.append("****");
            }
            stringBuffer.append(email.substring(index));
        }
        return stringBuffer.toString();
    }

    //数字
    public static final String REG_NUMBER = ".*\\d+.*";
    //特殊符号
    public static final String REG_SPECIAL = ".*[`~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";
    //小写字母
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //大写字母
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";

    /**
     * 是否是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 验证密码格式
     */
    public static boolean isPasswordValid(String password) {
        //密码为空或者长度小于8位则返回false
        if (password == null || password.length() < 8 || password.length() > 16) return false;
        if (true) {
            return true;
        }
        int i = 0;
        if (password.matches(REG_NUMBER)) i++;
        if (password.matches(REG_LOWERCASE)) i++;
        if (password.matches(REG_UPPERCASE)) i++;
        //if (password.matches(REG_SYMBOL)) i++;

        if (i < 3) return false;
        return true;
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * @param name
     * @return 中文  名字必须是中文和点
     */
    public static boolean isName(String name) {
        name = name.trim();
        if (name.length() == 0) {
            return false;
        }
        name = name.replaceAll("\\.", "");
        name = name.replaceAll("。", "");
        if (name.length() == 0) {
            return true;
        }
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$");
            Matcher m = p.matcher(name);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机格式
     */
    private static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";//
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(mobileNums) && mobileNums.matches(telRegex);
    }

    public static int getLengthForInputStr(String inputStr) {

        int orignLen = inputStr.length();

        int resultLen = 0;

        String temp = null;

        for (int i = 0; i < orignLen; i++) {

            temp = inputStr.substring(i, i + 1);

            try {
                // 3 bytes to indicate chinese word,1 byte to indicate english word ,in utf-8 encode
                if (temp.getBytes("utf-8").length == 3) {
                    resultLen += 2;
                } else {
                    resultLen++;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return resultLen;
    }

    private static String nums[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

    private static String pos_units[] = {"", "十", "百", "千"};

    private static String weight_units[] = {"", "万", "亿"};

    /**
     * 数字转汉字【新】
     *
     * @param num
     * @return
     */
    public static String numberToChinese(int num) {
        if (num == 0) {
            return "零";
        }

        int weigth = 0;//节权位
        String chinese = "";
        String chinese_section = "";
        boolean setZero = false;//下一小节是否需要零，第一次没有上一小节所以为false
        while (num > 0) {
            int section = num % 10000;//得到最后面的小节
            if (setZero) {//判断上一小节的千位是否为零，是就设置零
                chinese = nums[0] + chinese;
            }
            chinese_section = sectionTrans(section);
            if (section != 0) {//判断是都加节权位
                chinese_section = chinese_section + weight_units[weigth];
            }
            chinese = chinese_section + chinese;
            chinese_section = "";

            setZero = (section < 1000) && (section > 0);
            num = num / 10000;
            weigth++;
        }
        if ((chinese.length() == 2 || (chinese.length() == 3)) && chinese.contains("一十")) {
            chinese = chinese.substring(1);
        }
        if (chinese.indexOf("一十") == 0) {
            chinese = chinese.replaceFirst("一十", "十");
        }

        return chinese;
    }

    /**
     * 将每段数字转汉子
     *
     * @param section
     * @return
     */
    public static String sectionTrans(int section) {
        StringBuilder section_chinese = new StringBuilder();
        int pos = 0;//小节内部权位的计数器
        boolean zero = true;//小节内部的置零判断，每一个小节只能有一个零。
        while (section > 0) {
            int v = section % 10;//得到最后一个数
            if (v == 0) {
                if (!zero) {
                    zero = true;//需要补零的操作，确保对连续多个零只是输出一个
                    section_chinese.insert(0, nums[0]);
                }
            } else {
                zero = false;//有非零数字就把置零打开
                section_chinese.insert(0, pos_units[pos]);
                section_chinese.insert(0, nums[v]);
            }
            pos++;
            section = section / 10;
        }

        return section_chinese.toString();
    }


    /**
     * 格式化
     *
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }


    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        if (indent == 1) {
            sb.append("  ");
        } else {
            for (int i = 0; i <= indent; i++) {
                sb.append("  ");
            }
        }
    }

}

