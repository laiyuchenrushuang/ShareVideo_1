package video.hc.com.videodemo.utils;

import android.content.Context;
import android.widget.Toast;

public class Utils {


    public static boolean canPlay = true;
    public final static int SLIDE_SCALE = 219;//手感比较好的位置

    public final static int ONCLICK_SCALE_MIN = -1;//手感为了点击有效本来写0的
    public final static int ONCLICK_SCALE_MAX = 50;

    public final static int VIDEO_ERROR = 0x0001;
    public final static int VIDEO_PRE = 0x0002;
    public final static int VIDEO_NEXT = 0x0003;
    public final static int VIDEO_ONCLICK = 0x0004;
    public final static  int FRAGMENT_CALLBACK = 0x0005;
    public final static int URL_FAILED = 0x0006;
    public final static int URL_SUCCESS= 0x0007;

    public static void showToast(Context context, String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

    public class NetWorkUtil {
        public final static String BASE_URL = "http://192.168.0.53:8085/jyptdbctl/video/getVideoPage?";
        public final static String BASE_URL_TRUE = "http://bj.migucloud.com/vod2/v1/download_spotviurl?";

    }

}
