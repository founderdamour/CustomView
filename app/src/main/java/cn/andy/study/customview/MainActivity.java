package cn.andy.study.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.andy.study.customview.activity.EditTextCountActivity;
import cn.andy.study.customview.activity.ProgressRingActivity;
import cn.andy.study.customview.activity.ProgressRingUpgradeActivity;
import cn.andy.study.customview.activity.UiSeekBarActivity;
import cn.andy.study.customview.viewinject.FindViewById;
import cn.andy.study.customview.viewinject.ViewInjecter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button progressRingBtn;
    private Button progressRingUpgradeBtn;
    private Button seekBar;

    @FindViewById(R.id.edit_text_count)
    private Button editTextCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjecter.inject(this);
        initView();
        initListener();
    }

    private void initView() {
        progressRingBtn = findViewById(R.id.progress_ring);
        progressRingUpgradeBtn = findViewById(R.id.progress_ring_upgrade);
        seekBar = findViewById(R.id.ui_seek_bar);

    }

    private void initListener() {
        progressRingBtn.setOnClickListener(this);
        progressRingUpgradeBtn.setOnClickListener(this);
        seekBar.setOnClickListener(this);
        editTextCount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress_ring:
                startActivity(new Intent(getBaseContext(), ProgressRingActivity.class));
                break;
            case R.id.progress_ring_upgrade:
                startActivity(new Intent(getBaseContext(), ProgressRingUpgradeActivity.class));
                break;
            case R.id.ui_seek_bar:
                startActivity(new Intent(getBaseContext(), UiSeekBarActivity.class));
                break;
            case R.id.edit_text_count:
                startActivity(new Intent(getBaseContext(), EditTextCountActivity.class));
                break;
        }
    }
}
