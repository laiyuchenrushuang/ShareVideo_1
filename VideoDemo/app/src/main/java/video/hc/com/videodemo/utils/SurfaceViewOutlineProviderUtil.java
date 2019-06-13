package video.hc.com.videodemo.utils;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewOutlineProvider;


/**
 * Created by ly on 2019/6/3.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SurfaceViewOutlineProviderUtil extends ViewOutlineProvider {
    private float mRadius;
    public SurfaceViewOutlineProviderUtil(float radius) {
        this.mRadius = radius;
    }
    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);
    }
}
