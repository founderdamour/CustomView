package cn.andy.study.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import cn.andy.study.customview.R;

/**
 * 圆形进度条界面
 * <p>
 * Created by yangzhizhong
 */

public class ProgressRingActivity extends Activity implements View.OnClickListener {

    private Button back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_ring);
        initView();
        initListener();
    }

    private void initView() {
        back = findViewById(R.id.back);
    }

    private void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }

    }
}
