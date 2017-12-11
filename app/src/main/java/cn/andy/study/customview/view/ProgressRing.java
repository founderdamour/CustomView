package cn.andy.study.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.andy.study.customview.R;

/**
 * 圆形进度条
 * <p>
 * Created by yangzhizhong
 */

public class ProgressRing extends View {

    private int progressStartColor; // 进度条开始颜色
    private int progressEndColor;   // 进度条结束颜色
    private int bgStartColor;       // 背景开始颜色
    private int bgMidColor;         // 背景中间颜色
    private int bgEndColor;         // 背景结束颜色
    private int progress;           // 进度条
    private float progressWidth;    // 进度条宽度
    private int startAngle;         // 开始角度
    private int sweepAngle;         // 扫描过的角度
    private boolean showAnim;       // 是否显示动画

    private int mMeasureHeight;  // 测量的高度
    private int mMeasureWidth;   // 测量的宽度

    private Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); // 背景画笔
    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); // 进度画笔
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); // 文字画笔

    private RectF pRectF;  // 矩形

    private float unitAngle;   // 扫描过的角度比

    private int curProgress = 0;  // 当前进度条

    public ProgressRing(Context context) {
        this(context, null);
    }

    public ProgressRing(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressRing(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initVariableAndStyle();
    }

    /**
     * 初始化属性
     *
     * @param context context
     * @param attrs   属性
     */
    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressRing);
        progressStartColor = ta.getColor(R.styleable.ProgressRing_pr_progress_start_color, Color.YELLOW);
        progressEndColor = ta.getColor(R.styleable.ProgressRing_pr_progress_end_color, progressStartColor);
        bgStartColor = ta.getColor(R.styleable.ProgressRing_pr_bg_start_color, Color.LTGRAY);
        bgMidColor = ta.getColor(R.styleable.ProgressRing_pr_bg_mid_color, bgStartColor);
        bgEndColor = ta.getColor(R.styleable.ProgressRing_pr_bg_end_color, bgStartColor);
        progress = ta.getInt(R.styleable.ProgressRing_pr_progress, 0);
        progressWidth = ta.getDimension(R.styleable.ProgressRing_pr_progress_width, 8f);
        startAngle = ta.getInt(R.styleable.ProgressRing_pr_start_angle, 150);
        sweepAngle = ta.getInt(R.styleable.ProgressRing_pr_sweep_angle, 240);
        showAnim = ta.getBoolean(R.styleable.ProgressRing_pr_show_anim, true);
        ta.recycle();
    }

    /**
     * 初始化变量及一些样式
     */
    private void initVariableAndStyle() {

        unitAngle = (float) (sweepAngle / 100.0);

        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setStrokeWidth(progressWidth);

        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = getMeasuredWidth();
        mMeasureHeight = getMeasuredHeight();
        if (pRectF == null) {
            float halfProgressWidth = progressWidth / 2;
            pRectF = new RectF(halfProgressWidth + getPaddingLeft(),
                    halfProgressWidth + getPaddingTop(),
                    mMeasureWidth - halfProgressWidth - getPaddingRight(),
                    mMeasureHeight - halfProgressWidth - getPaddingBottom());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!showAnim) {
            curProgress = progress;
        }
        drawBg(canvas);
        drawProgress(canvas);
        drawText(canvas);
        if (curProgress < progress) {
            curProgress++;
            postInvalidate();
        }
    }

    /**
     * 画文字
     *
     * @param canvas 画板
     */
    private void drawText(Canvas canvas) {
        //画文字
        String stepText = curProgress + "分";
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;//文字的起始位置
        //基线
        int baseLine = getHeight() / 2 + textBounds.height() / 3;
        canvas.drawText(stepText, dx, baseLine, mTextPaint);
    }

    /**
     * 只需要画进度之外的背景
     */
    private void drawBg(Canvas canvas) {
        float halfSweep = sweepAngle / 2;
        for (int i = sweepAngle, st = (int) (curProgress * unitAngle); i > st; --i) {
            if (i - halfSweep > 0) {
                bgPaint.setColor(getGradient((i - halfSweep) / halfSweep, bgMidColor, bgEndColor));
            } else {
                bgPaint.setColor(getGradient((halfSweep - i) / halfSweep, bgMidColor, bgStartColor));
            }
            canvas.drawArc(pRectF,
                    startAngle + i,
                    1,
                    false,
                    bgPaint);
        }
    }

    /**
     * 画进度条
     *
     * @param canvas 画板
     */
    private void drawProgress(Canvas canvas) {
        for (int i = 0, end = (int) (curProgress * unitAngle); i <= end; i++) {
            progressPaint.setColor(getGradient(i / (float) end, progressStartColor, progressEndColor));
            canvas.drawArc(pRectF,
                    startAngle + i,
                    1,
                    false,
                    progressPaint);
        }
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(@IntRange(from = 0, to = 100) int progress) {
        this.progress = progress;
        /*
         *  绘制进度
         */
        invalidate();
    }

    /**
     * 获取进度
     *
     * @return 进度
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param fraction   变化的值
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     * @return 有意义的颜色值
     */
    public int getGradient(float fraction, int startColor, int endColor) {
        if (fraction > 1) fraction = 1;
        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaDifference = alphaEnd - alphaStart;
        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaCurrent = (int) (alphaStart + fraction * alphaDifference);
        int redCurrent = (int) (redStart + fraction * redDifference);
        int blueCurrent = (int) (blueStart + fraction * blueDifference);
        int greenCurrent = (int) (greenStart + fraction * greenDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }
}
