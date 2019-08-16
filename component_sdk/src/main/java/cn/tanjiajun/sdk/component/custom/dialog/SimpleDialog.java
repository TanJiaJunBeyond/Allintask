package cn.tanjiajun.sdk.component.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cn.tanjiajun.sdk.component.R;

/**
 * 简易对话框
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class SimpleDialog extends Dialog {
    public SimpleDialog(Context context, int theme) {
        super(context, theme);
    }

    public SimpleDialog(Context context) {
        super(context);
    }

    public SimpleDialog(Context context, int xLeftTop, int yLeftTop, int theme) {
        super(context, theme);

        WindowManager.LayoutParams windowManagerLayoutParams = getWindow().getAttributes();

        windowManagerLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManagerLayoutParams.x = xLeftTop;
        windowManagerLayoutParams.y = yLeftTop;

        windowManagerLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;

        private int width;
        private int height;
        private int xLeftTop;
        private int yLeftTop;

        private SimpleDialog dialog;

        private View contentView;

        public Builder(Context context) {
            this(context, -1, -1, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }

        public Builder(Context context, int width, int height) {
            this(context, -1, -1, width, height);
        }

        public Builder(Context context, int xLeftTop, int yLeftTop, int width, int height) {
            this.context = context;
            this.xLeftTop = xLeftTop;
            this.yLeftTop = yLeftTop;
            this.width = width;
            this.height = height;
        }

        /**
         * Set a custom content view for the Dialog.
         * <p>
         * If a message is set, the contentView is not
         * <p>
         * added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Create the custom dialog
         */
        public SimpleDialog create() {

            // instantiate the dialog with the custom Theme
            if (-1 == xLeftTop || -1 == yLeftTop) {
                dialog = new SimpleDialog(context, R.style.basic_dialog_style);
            } else {
                dialog = new SimpleDialog(context, xLeftTop, yLeftTop, R.style.basic_dialog_style);
            }

            // 解决Dialog圆角出现黑色棱角的问题
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.simple_dialog, null);
            dialog.addContentView(layout, new LayoutParams(this.width, this.height));

            if (contentView != null) {
                // if no message set  
                // add the contentView to the dialog body  
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            return dialog;
        }
    }

}
