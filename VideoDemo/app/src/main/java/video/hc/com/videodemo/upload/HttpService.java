package video.hc.com.videodemo.upload;

import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ly on 2019/5/30.
 */

public class HttpService {

    public void getURLData(){
        String url = "http://wwww.baidu.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });//得到Response 对象

    }

    public void getVideoResourceNetWork(MediaPlayer mediaPlayer,String url){
        url = "http://bjcdn2.vod.migucloud.com/mgc_transfiles/200010145/2019/5/19/2EFBSUWNbMUSHXxvBeSQz/cld640p/video_2EFBSUWNbMUSHXxvBeSQz_cld640p.m3u8";
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Map<String,String>> getUrlList() {
        ArrayList<Map<String,String>> urlList = new ArrayList();
        Map<String,String> map = new HashMap<>();
        map.put("id","0");
        map.put("url","http://bjcdn2.vod.migucloud.com/mgc_transfiles/200010145/2019/5/19/2EFBSUWNbMUSHXxvBeSQz/cld640p/video_2EFBSUWNbMUSHXxvBeSQz_cld640p.m3u8");
        Map<String,String> map1 = new HashMap<>();
        map1.put("id","1");
        map1.put("url","http://bjcdn2.vod.migucloud.com/mgc_transfiles/200010145/2019/5/19/1EYo9UNOF95HCK30a3mHY/cld640p/video_1EYo9UNOF95HCK30a3mHY_cld640p.m3u8");
        urlList.add(map);
        urlList.add(map1);
        return urlList;
    }
}
