package cn.tanjiajun.sdk.component.custom.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.regex.Pattern;

import cn.tanjiajun.sdk.component.R;

/**
 * 价格TextView，可设置分隔符，分割位数
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class PriceTextView extends TextView {

    private static final int DEFAULT_GROUP_LENGTH = 3;  //默认每组的长度
    private static final String DEFAULT_SEPARATOR = ","; //默认分隔符

    private boolean isSeparator = true; //是否有分割符，包括空格；

    private static String separator = DEFAULT_SEPARATOR;

    private static int groupLength = DEFAULT_GROUP_LENGTH;

    public PriceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PriceTextView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PriceTextView);

            separator = typedArray.getString(R.styleable.PriceTextView_separator);
            groupLength = typedArray.getInteger(R.styleable.PriceTextView_group_length, groupLength);

            if (separator == null) {
                separator = DEFAULT_SEPARATOR;
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null && isNumber(text.toString().trim())) { // 是否为纯数字
            String decimal = null;  //小数点部分
            if (text.toString().contains(".")) {//包含小数点
                String[] textArr = text.toString().split("\\.");
                text = textArr[0];
                decimal = textArr[1];
            }

            StringBuilder inputSB = new StringBuilder(text);

            if (text.length() > groupLength) { //长度大于单位长度


                //从text后面遍历，每groupLength长度添加separator
                for (int i = text.length() - groupLength; i > 0; i = i - groupLength) {
                    if (i - 1 >= 0 && text.charAt(i - 1) >= '0' && text.charAt(i - 1) <= '9') {
                        inputSB.insert(i, separator);
                    }
                }
            }

            if (!TextUtils.isEmpty(decimal)) {//小数点部位不为空则拼接到整型部分后
                inputSB.append(".");
                inputSB.append(decimal);
            }

            text = inputSB.toString();
        } else {
            text = "暂无";
        }

        super.setText(text, type);
    }

    public boolean isNumber(String str) {
        Pattern intPattern = Pattern.compile("^[-+]?[0-9]*");//整数样式匹配
        Pattern floatPattern = Pattern.compile("^[-+]?[0-9]+(.[0-9]{0,2})?$"); //小数样式匹配
        return intPattern.matcher(str).matches() || floatPattern.matcher(str).matches();
    }

    public static void setSeparator(String separator) {
        PriceTextView.separator = separator;
    }

    public static void setGroupLength(int groupLength) {
        PriceTextView.groupLength = groupLength;
    }

    public static String getSeparator() {
        return separator;
    }

    public static int getGroupLength() {
        return groupLength;
    }

    public boolean isSeparator() {
        return isSeparator;
    }

}
