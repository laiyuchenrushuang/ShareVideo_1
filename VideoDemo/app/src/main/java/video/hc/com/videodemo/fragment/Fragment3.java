package video.hc.com.videodemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import video.hc.com.videodemo.NewActivity;
import video.hc.com.videodemo.R;
import video.hc.com.videodemo.ToolActivity;
import video.hc.com.videodemo.base.BaseFragment;

/**
 * Created by ly on 2019/6/6.
 */

public class Fragment3 extends BaseFragment {
    @BindView(R.id.bt_url)
    ImageView bt_url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View getLayoutView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment3, null);
    }

    @Override
    public void initView() {
        bt_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadThirdWeb();
            }
        });
    }

    private void loadThirdWeb() {
        Intent intent = new Intent();
        intent.setClass(getContext(), ToolActivity.class);
        intent.putExtra("url", "http://jtzzlm.cdjg.chengdu.gov.cn/cdjj/newcwtapi/newindex?id=888&code=oSthY1dlwOraHfs-6P4bArh6JpwY&state=STATE");
        startActivity(intent);
    }
}
