package video.hc.com.videodemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import video.hc.com.videodemo.base.BeseActivity;

/**
 * Created by ly on 2019/6/6.
 */

public class NewActivity extends BeseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_fragment);
    }
}
