package cn.fooltech.fool_ops.utils.input;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 拼音工具
 *
 * @author xjh
 */
public class PinyinUtils {

    private final static Joiner joiner = Joiner.on(",");

    /**
     * 获取拼音首字母
     *
     * @param str
     * @param limit 限制字符个数，多余的不取
     * @return
     */
    public static String getPinyinCode(String str, int limit) {
        String pinyin = getPinyinCode(str);
        if (pinyin.length() > limit) {
            pinyin = pinyin.substring(0, limit);
        }
        return pinyin;
    }

    /**
     * 获取拼音首字母
     *
     * @param str
     * @return
     */
    public static String getPinyinCode(String str) {
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

            // UPPERCASE：大写 (ZHONG)
            // LOWERCASE：小写 (zhong)
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

            // WITHOUT_TONE：无音标 (zhong)
            // WITH_TONE_NUMBER：1-4数字表示英标 (zhong4)
            // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常） (zhòng)
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            // WITH_V：用v表示ü (nv)
            // WITH_U_AND_COLON：用"u:"表示ü (nu:)
            // WITH_U_UNICODE：直接用ü (nü)
            format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

            List<String> data = Lists.newArrayList();

            for (int i = 0; i < str.length(); i++) {
                char word = str.charAt(i);
                Set<String> temp = Sets.newHashSet();
                if (isChinese(word)) {
                    String[] pinyins = null;

                    pinyins = PinyinHelper.toHanyuPinyinStringArray(word,
                            format);

                    if (pinyins != null) {
                        for (String pinyin : pinyins) {
                            if (Strings.isNullOrEmpty(pinyin))
                                continue;

                            if (data.size() > 0) {
                                for (String item : data) {
                                    temp.add(item
                                            + pinyin.substring(0, 1)
                                            .toUpperCase());
                                }
                            } else {
                                temp.add(pinyin.substring(0, 1).toUpperCase());
                            }
                        }
                    }
                } else {
                    if (data.size() > 0) {
                        for (String item : data) {
                            temp.add(item + word);
                        }
                    } else {
                        temp.add(word + "");
                    }
                }
                data.clear();
                data.addAll(temp);
            }

            String result = joiner.join(data);
            return result;

        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
            return "";
        }
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    // 只能判断部分CJK字符（CJK统一汉字）
    public static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }
}
