package cn.andy.study.customview.activity;

import android.app.Activity;
import android.os.Bundle;

import cn.andy.study.customview.view.GooView;

/**
 * Created by yangzhizhong
 */

public class GooViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GooView gooView = new GooView(this);
        setContentView(gooView);

    }

}
