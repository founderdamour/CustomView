package cn.andy.study.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.andy.study.customview.activity.ProgressRingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button progressRingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        progressRingBtn = findViewById(R.id.progress_ring);

    }

    private void initListener() {
        progressRingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress_ring:
                startActivity(new Intent(getBaseContext(), ProgressRingActivity.class));
                break;
        }
    }
}
