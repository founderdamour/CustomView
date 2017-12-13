package cn.andy.study.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;

import cn.andy.study.customview.R;

/**
 * Created by yangzhizhong
 */

public class UiSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    //进度条指示文字后缀
    private String numTextFormat = "%";

    private String numText;
    //进度条指示文字的大小吗默认20px
    private int numTextSize = 20;
    //进度条指示文字的背景
    private int numBackground;
    //numbackground对应的bitmap
    private int numTextColor;
    private Bitmap bm;
    //bitmap对应的宽高
    private float bmpWidth, bmpHeight;
    //构建画笔和文字
    Paint bmPaint;
    //文本的宽可能不准
    private float numTextWidth;
    //测量seekbar的规格
    private Rect rect_seek;
    //测量thum的规格
    private Rect rect_thum;

    //show 在top还是bottom
    private int type = Gravity.TOP;
    private Paint.FontMetrics fm;
    //特别说明这个scale比例是滑动的指示器小箭头部分占全部图片的比列，为了使其文字完全居中
    private double numScale = 0.16;

    public UiSeekBar(Context context) {
        this(context, null);
    }

    public UiSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UiSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initBm();
        initPaint();
        setPadding();
    }

    /**
     * 初始化属性
     *
     * @param context 内容提供者
     * @param attrs   属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UiSeekBar);
        numTextFormat = array.getString(R.styleable.UiSeekBar_numTextFormat);
        numBackground = array.getResourceId(R.styleable.UiSeekBar_numBackground, R.drawable.shows);
        numTextSize = array.getDimensionPixelSize(R.styleable.UiSeekBar_numTextSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        numTextColor = array.getColor(R.styleable.UiSeekBar_numTextColor, Color.WHITE);
        type = array.getInt(R.styleable.UiSeekBar_numType, Gravity.TOP);
        numScale = Double.parseDouble(array.getString(R.styleable.UiSeekBar_numScale) == null ? numScale + "" : array.getString(R.styleable.UiSeekBar_numScale));
        numTextFormat = numTextFormat == null ? "%" : numTextFormat;
        array.recycle();
    }

    /**
     * 初始化bm
     */
    private void initBm() {

        bm = BitmapFactory.decodeResource(getResources(), numBackground);
        //注意判断是否是null
        if (bm != null) {
            bmpWidth = bm.getWidth();
            bmpHeight = bm.getHeight();
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //抗锯齿
        bmPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bmPaint.setTypeface(Typeface.DEFAULT);
        bmPaint.setTextSize(numTextSize);
        bmPaint.setColor(numTextColor);
    }

    /**
     * 由于view没有默认的padding需要设置预留显示图标
     */
    private void setPadding() {
        switch (type) {
            case Gravity.TOP:
                setPadding((int) Math.ceil(bmpWidth) / 2, (int) Math.ceil(bmpHeight), (int) Math.ceil(bmpWidth) / 2, 0);
                break;
            case Gravity.BOTTOM:
                setPadding((int) Math.ceil(bmpWidth) / 2, 0, (int) Math.ceil(bmpWidth) / 2, (int) Math.ceil(bmpHeight));
                break;
        }
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            fm = bmPaint.getFontMetrics();
            numText = (getProgress() * 100 / getMax()) + numTextFormat;
            numTextWidth = bmPaint.measureText(numText);
            rect_seek = this.getProgressDrawable().getBounds();
            float thum_height = 0;
            //api必须大于16
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rect_thum = this.getThumb().getBounds();
                thum_height = rect_thum.height();
            }
            //计算bitmap左上的位置坐标
            float bm_x = rect_seek.width() * getProgress() / getMax();
            //计算文字的中心位置在bitmap
            float text_x = rect_seek.width() * getProgress() / getMax() + (bmpWidth - numTextWidth) / 2;
            //还应该减去文字的高度
            float text_y = bmpHeight / 2;
            float text_center = bmpHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
            switch (type) {
                case Gravity.TOP:
                    canvas.drawBitmap(bm, bm_x, 0, bmPaint);
                    canvas.drawText(numText, text_x, (float) (bmpHeight / 2 - (fm.descent - (fm.descent - fm.ascent) / 2) - (bmpHeight * numScale) / 2), bmPaint);
                    break;
                case Gravity.BOTTOM:
                    canvas.drawBitmap(bm, bm_x, rect_thum.height(), bmPaint);
                    canvas.drawText(numText, text_x, (float) (thum_height + (bmpHeight / 2 - (fm.descent - (fm.descent - fm.ascent) / 2) + bmpHeight * numScale / 2)), bmPaint);
                    break;
            }
            //设置文本的位置
        } catch (Exception e) {
            //为什么要try因为你的参数可能没有填
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);
    }


    public String getNumText() {
        return numText;
    }

    public void setNumText(String numText) {
        this.numText = numText;
        invalidate();
    }

    public int getNumTextSize() {
        return numTextSize;
    }

    public void setNumTextSize(int numTextSize) {
        this.numTextSize = numTextSize;

    }

    public int getNumbackground() {
        return numBackground;
    }

    public void setNumbackground(int numBackground) {
        this.numBackground = numBackground;
    }

    public int getNumTextColor() {
        return numTextColor;
    }

    public void setNumTextColor(int numTextColor) {
        this.numTextColor = numTextColor;
    }

}
