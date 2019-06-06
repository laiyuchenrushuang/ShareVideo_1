package video.hc.com.videodemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import video.hc.com.videodemo.R;
import video.hc.com.videodemo.base.BaseFragment;

/**
 * Created by ly on 2019/6/6.
 */

public class VideoFragment extends BaseFragment {
    @BindView(R.id.gv_video)
    GridView gv_video;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.video_fragment,null);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public View getLayoutView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.video_fragment,null);
    }

    @Override
    public void initView() {

    }
}
