package cn.andy.study.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.andy.study.customview.R;

/**
 * 带指示的SeekBar
 * <p>
 * Created by yangzhizhong
 */

public class UiSeekBarActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_seek_bar);
    }
}
