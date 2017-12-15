package cn.andy.study.customview.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.andy.study.customview.utils.GeometryUtil;

/**
 * 粘性控件
 * <p>
 * Created by yangzhizhong
 */
public class GooView extends View {

    private Paint mPaint;  // 画笔
    private Path mPath;   // 指点路径

    private PointF mControlPoint; // 控制点
    private PointF mDragCenter;   // 拖拽点
    private PointF mStickCenter;  // 固定点
    private float mDragRadius;    // 拖拽点半径
    private float mStickRadius;   // 固定点半径

    private PointF[] mDragPoints;
    private PointF[] mStickPoints;

    private int mFarestDistance = 200;  // 最远距离

    public GooView(Context context) {
        this(context, null);
    }

    public GooView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GooView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setColor(Color.RED);

        mPath = new Path();

        mControlPoint = new PointF(250, 400);
        mDragCenter = new PointF(200, 200);
        mStickCenter = new PointF(200, 200);

        mDragRadius = 20;  // 拖拽圆半径
        mStickRadius = 16; // 固定圆半径

        mDragPoints = new PointF[2];
        mDragPoints[0] = new PointF(150, 350);
        mDragPoints[1] = new PointF(150, 450);

        mStickPoints = new PointF[2];
        mStickPoints[0] = new PointF(350, 350);
        mStickPoints[1] = new PointF(350, 450);

        getAllPoints();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!isDismiss) {
            canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, mPaint);

            if (!isOutOfRange) {
                canvas.drawCircle(mStickCenter.x, mStickCenter.y, mStickRadius, mPaint);
                mPath.reset();
                mPath.moveTo(mStickPoints[0].x, mStickPoints[0].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints[0].x,
                        mDragPoints[0].y);
                mPath.lineTo(mDragPoints[1].x, mDragPoints[1].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, mStickPoints[1].x,
                        mStickPoints[1].y);
                mPath.close();

                canvas.drawPath(mPath, mPaint);
            }
        }


        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mStickCenter.x, mStickCenter.y, mFarestDistance, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                isOutOfRange = false;
                isDismiss = false;
                updateDragCenter(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                updateDragCenter(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (isOutOfRange) {
                    //代表曾经断掉过
                    float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                    if (distance > mFarestDistance) {
                        //消失
                        isDismiss = true;
                    } else {
                        //回到原点
                        mDragCenter.set(mStickCenter);
                    }
                } else {
                    //代表从来都没有离开拖拽范围   0~1.0
                    ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
                    animator.setDuration(1000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float percent = animation.getAnimatedFraction();//产生中间值的百分比
                            PointF tempPoint = GeometryUtil.getPointByPercent(mDragCenter, mStickCenter, percent);
                            updateDragCenter(tempPoint.x, tempPoint.y);
                        }
                    });
                    animator.start();
                }

                invalidate();

                break;
        }
        return true;
    }

    private boolean isOutOfRange = false;  // 是否超出范围
    private boolean isDismiss = false;  // 是否消失

    // 更新一下拖拽点
    private void updateDragCenter(float x, float y) {
        mDragCenter.set(x, y);
        getAllPoints();


        float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
        if (distance > mFarestDistance) {
            //超出区域
            isOutOfRange = true;
        }

        // 按拖拽距离来改变固定圆的半径
        float percent = distance / mFarestDistance;
        mStickRadius = 16 + ((6 - 16) * percent);

        invalidate();
    }

    public void getAllPoints() {
        // 先得到控制点的坐标
        mControlPoint = GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);

        // 计算出斜率
        Double lineK = null;
        if (mStickCenter.x != mDragCenter.x) {
            lineK = (double) ((mStickCenter.y - mDragCenter.y) / (mStickCenter.x - mDragCenter.x));
        }
        // 获取附着点
        mDragPoints = GeometryUtil.getIntersectionPoints(mDragCenter,
                mDragRadius, lineK);
        mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter,
                mStickRadius, lineK);

    }
}
