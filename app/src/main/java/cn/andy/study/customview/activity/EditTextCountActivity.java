package cn.andy.study.customview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.andy.study.customview.R;
import cn.andy.study.customview.viewinject.OnClick;
import cn.andy.study.customview.viewinject.ViewInjecter;

/**
 * Created by yangzhizhong
 */

public class EditTextCountActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_count);
        ViewInjecter.inject(this);
    }

    @OnClick(R.id.back)
    private void onBackClick(View view) {
        finish();
    }
}
