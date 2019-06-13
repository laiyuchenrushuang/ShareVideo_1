package video.hc.com.videodemo.base;


import android.app.Application;
import android.util.Log;

import video.hc.com.videodemo.utils.CrashCollectHandler;

/**
 * Created by ly on 2019/6/12.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lylog","ssss1");
        CrashCollectHandler.getInstance().init(this);
    }
}
