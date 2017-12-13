package cn.andy.study.customview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.andy.study.customview.R;
import cn.andy.study.customview.utils.Util;

/**
 * 统计EditText输入内容
 * <p>
 * Created by yangzhizhong
 */

public class EditTextCount extends RelativeLayout {

    public static final int PERCENTAGE = 0; // 显示形式： 11/100
    public static final int SINGULAR = 1;  // 显示形式： 11

    private EditText etContent;//文本框
    private TextView tvNum;//字数显示TextView

    private int maxNum;
    private int type;

    public EditTextCount(Context context) {
        this(context, null);
    }

    public EditTextCount(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextCount(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_edit_text_view, this, true);
        etContent = findViewById(R.id.etContent);
        tvNum = findViewById(R.id.tvNum);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextCount);
        setText(typedArray.getString(R.styleable.EditTextCount_text));
        setTextColor(typedArray.getColor(R.styleable.EditTextCount_text_color, Color.BLACK));
        setHint(typedArray.getString(R.styleable.EditTextCount_hint));
        setHintTextColor(typedArray.getColor(R.styleable.EditTextCount_hint_color, Color.rgb(155, 155, 155)));
        setMinHeight(Util.px2dip(context, typedArray.getDimensionPixelOffset(R.styleable.EditTextCount_min_height, 200)));
        setMaxNum(typedArray.getInt(R.styleable.EditTextCount_max_length, 100), typedArray.getInt(R.styleable.EditTextCount_type, 0));
        typedArray.recycle();
    }

    private void setText(String text) {
        etContent.setText(text);
        etContent.setSelection(etContent.getText().length());
    }

    private void setTextColor(int color) {
        etContent.setTextColor(color);
    }

    private void setHint(String hint) {
        etContent.setHint(hint);
    }

    private void setHintTextColor(int color) {
        etContent.setHintTextColor(color);
    }

    private void setMinHeight(int minHeight) {
        etContent.setMinHeight(minHeight);
    }

    private void setMaxNum(int maxNum, @Type int type) {
        this.maxNum = maxNum;
        this.type = type;
        setTextCount();
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)});
        setContentListener(etContent, maxNum);
    }

    private void setContentListener(EditText etContent, int maxNum) {
        etContent.addTextChangedListener(textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher() {

        private int editEnd;
        private int editStart;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = etContent.getSelectionStart();
            editEnd = etContent.getSelectionEnd();
            etContent.removeTextChangedListener(textWatcher);
            while (calculateLength(etContent.getText()) > maxNum) {
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // 恢复监听器
            etContent.addTextChangedListener(textWatcher);
            setTextCount();
        }
    };

    @SuppressLint("SetTextI18n")
    private void setTextCount() {
        switch (type) {
            case PERCENTAGE:
                tvNum.setText(calculateLength(etContent.getText()) + "/" + maxNum);
                break;
            case SINGULAR:
                tvNum.setText(String.valueOf(maxNum));
                break;
        }
    }

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param charSequence 文字内容
     * @return
     */
    public static long calculateLength(CharSequence charSequence) {
        double len = 0;
        for (int i = 0; i < charSequence.length(); i++) {
            int tmp = (int) charSequence.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 1;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    @IntDef({PERCENTAGE, SINGULAR})
    @interface Type {
    }
}
