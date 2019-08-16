package cn.tanjiajun.sdk.common.utils;

/**
 * 文本格式工具类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class TextFormatUtils {

    private static String DOUBLE_BLANK = "\u3000\u3000";
    private static String PARAGRAPH_SYMBOL = "\n\n";

    public static String format(String contentStr) {
        StringBuffer buf = new StringBuffer();
        String paragraphs[] = contentStr.split(PARAGRAPH_SYMBOL);
        for (int i = 0; i < paragraphs.length; i++) {
            buf.append(DOUBLE_BLANK + paragraphs[i] + PARAGRAPH_SYMBOL);
        }

        return buf.toString();
    }

}
