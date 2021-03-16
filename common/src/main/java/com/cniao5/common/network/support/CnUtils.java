package com.cniao5.common.network.support;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.EncryptUtils;
import com.cniao5.common.network.config.CnConfigKtKt;


public class CnUtils {
    private CnUtils() {

    }

    /**
     * 把中文转成Unicode码
     */
    public static String chinaToUnicode(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result.append("\\u").append(Integer.toHexString(chr1));
            } else {
                result.append(str.charAt(i));
            }
        }
        return result.toString();
    }

    /**
     * 判断是否为中文字符
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    //unicode转中文
    public static String decodeUnicode(final String unicode) {
        StringBuilder string = new StringBuilder();

        String[] hex = unicode.split("\\\\u");

        for (String s : hex) {

            try {
                // 汉字范围 \u4e00-\u9fa5 (中文)
                if (s.length() >= 4) {//取前四个，判断是否是汉字
                    String chinese = s.substring(0, 4);
                    try {
                        int chr = Integer.parseInt(chinese, 16);
                        boolean isChinese = isChinese((char) chr);
                        //转化成功，判断是否在  汉字范围内
                        if (isChinese) {//在汉字范围内
                            // 追加成string
                            string.append((char) chr);
                            //并且追加  后面的字符
                            String behindString = s.substring(4);
                            string.append(behindString);
                        } else {
                            string.append(s);
                        }
                    } catch (NumberFormatException e1) {
                        string.append(s);
                    }

                } else {
                    string.append(s);
                }
            } catch (NumberFormatException e) {
                string.append(s);
            }
        }

        return string.toString();
    }

    /**
     * 解析返回的data数据
     * 字段、参数、返回值可以合法为空
     */
    @Nullable
    public static String decodeData(@Nullable String dataStr) {
        //java代码，无自动null判断，需要自行处理
        if (dataStr != null) {
            return new String(EncryptUtils.decryptBase64AES(dataStr.getBytes(), CnConfigKtKt.ENT_CONFIG_APPKEY.getBytes(), "" +
                    "AES/CBC/PKCS7Padding", "J#y9sJesy*5HmqLq".getBytes()));
        } else {
            return null;
        }
    }

}
