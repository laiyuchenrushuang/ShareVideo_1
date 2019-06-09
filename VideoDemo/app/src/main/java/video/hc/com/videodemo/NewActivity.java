package video.hc.com.videodemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import video.hc.com.videodemo.adapter.MyPagerAdapter;
import video.hc.com.videodemo.base.BaseFragment;
import video.hc.com.videodemo.base.BeseActivity;
import video.hc.com.videodemo.fragment.Fragment1;
import video.hc.com.videodemo.fragment.Fragment2;
import video.hc.com.videodemo.fragment.Fragment3;

/**
 * Created by ly on 2019/6/6.
 */

public class NewActivity extends BeseActivity implements View.OnClickListener {
    @BindView(R.id.vp_video)
    ViewPager vp_video;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    private MyPagerAdapter vpAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);

        initView();
        initEvent();
    }

    private void initEvent() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    private void initView() {
        FragmentManager fm=getSupportFragmentManager();
        ArrayList<Fragment> listf = new ArrayList<>();
        Fragment1 f1 = new Fragment1();
        Fragment2 f2 = new Fragment2();
        Fragment3 f3 = new Fragment3();
        listf.add(f1);
        listf.add(f2);
        listf.add(f3);
        vpAdapter = new MyPagerAdapter(fm,listf);
        vp_video.setAdapter(vpAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                vp_video.setCurrentItem(0);
                break;
            case R.id.button2:
                vp_video.setCurrentItem(1);
                break;
            case R.id.button3:
                vp_video.setCurrentItem(2);
                break;
        }
    }
}
