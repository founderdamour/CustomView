package cn.andy.study.customview.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.andy.study.customview.R;
import cn.andy.study.customview.utils.Util;
import cn.andy.study.customview.view.ProgressRingUpgradeView;
import cn.qqtheme.framework.picker.ColorPicker;

/**
 * 圆形进度条升级版
 * <p>
 * Created by yangzhizhong
 */

public class ProgressRingUpgradeActivity extends Activity implements View.OnClickListener {

    private TextView mTvPaintWidth;
    private TextView mTvCurrentNum;
    private SeekBar mSbPaintWidth;
    private SeekBar mSbCurrentNum;
    private ProgressRingUpgradeView progressRingUpgradeView;
    private int mCurrentNum = 8555;//当前步数,默认8555，最高10000
    private ValueAnimator valueAnimator;
    private Button bigCircle;
    private Button smallCircle;
    private Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_ring_upgrade);
        initView();
        setAnimator();
        setPaintWidth();
        setCurrentStep();
        setOnClickListener();
    }

    private void initView() {
        progressRingUpgradeView = findViewById(R.id.progress_ring_upgrade_view);
        back = findViewById(R.id.back);
        mSbPaintWidth = findViewById(R.id.sb);
        mSbCurrentNum = findViewById(R.id.sb_num);
        mTvPaintWidth = findViewById(R.id.tv_paint_width);
        mTvCurrentNum = findViewById(R.id.tv_current_num);
        bigCircle = findViewById(R.id.btn_big_rad);
        smallCircle = findViewById(R.id.btn_small_rad);
    }

    private void setAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, mCurrentNum);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = (float) animation.getAnimatedValue();
                progressRingUpgradeView.setCurrent((int) current);
            }
        });
        valueAnimator.start();
    }

    /**
     * 修改画笔宽度
     */
    private void setPaintWidth() {
        mSbPaintWidth.setProgress(Util.dip2px(ProgressRingUpgradeActivity.this, 10));
        mSbPaintWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvPaintWidth.setText(String.format(getString(R.string.paint_width), Util.px2dip(ProgressRingUpgradeActivity.this, progress)));
                progressRingUpgradeView.setWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valueAnimator.start();
            }
        });
    }

    /**
     * 修改步数
     */
    private void setCurrentStep() {
        mSbCurrentNum.setProgress(mCurrentNum);
        mTvCurrentNum.setText(String.format(getString(R.string.current_step), mCurrentNum));
        mSbCurrentNum.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvCurrentNum.setText(String.format(getString(R.string.current_step), progress));
                mCurrentNum = progress;
                progressRingUpgradeView.setCurrent(mCurrentNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valueAnimator.setFloatValues(mCurrentNum);
                valueAnimator.start();
            }
        });
    }

    private void setOnClickListener() {

        back.setOnClickListener(this);
        bigCircle.setOnClickListener(this);
        smallCircle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_big_rad:
                ColorPicker pickerBig = new ColorPicker(ProgressRingUpgradeActivity.this);
                pickerBig.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
                    @Override
                    public void onColorPicked(int pickedColor) {
                        progressRingUpgradeView.setOutPaintColor(pickedColor);
                        valueAnimator.start();
                    }
                });
                pickerBig.show();
                break;
            case R.id.btn_small_rad:
                ColorPicker pickerSmall = new ColorPicker(ProgressRingUpgradeActivity.this);
                pickerSmall.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
                    @Override
                    public void onColorPicked(int pickedColor) {
                        progressRingUpgradeView.setInPaintColor(pickedColor);
                        valueAnimator.start();
                    }
                });
                pickerSmall.show();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
