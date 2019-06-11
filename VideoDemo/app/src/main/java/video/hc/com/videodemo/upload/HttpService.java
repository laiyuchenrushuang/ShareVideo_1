package video.hc.com.videodemo.upload;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import video.hc.com.videodemo.enity.VideoBean;
import video.hc.com.videodemo.utils.JsonUtils;
import video.hc.com.videodemo.utils.Utils;

/**
 * Created by ly on 2019/5/30.
 */

public class HttpService {
    private static HttpService mHttpService;
    private Context mContext;
    private String uid, token;

    public interface HttpServiceResult {
        void success(Response result);

        void failed(String result);
    }

    public interface HttpServiceLX {
        void isOK(Response result);

        void isNO(String result);
    }

    public interface HttpTokenCallback {
        void tokenCallback(String uid, String token);

        void tokenError(String result);
    }

    private ArrayList<Map<String, String>> hashMaps = new ArrayList<>();
    private ArrayList<Map<String, String>> datalist = new ArrayList<>();

    ArrayList<String> urlList = new ArrayList<>();

    public static HttpService getInstance() {
        if (mHttpService == null) {
            synchronized (HttpService.class) {
                if (mHttpService == null) {
                    mHttpService = new HttpService();
                }
            }
        }
        return mHttpService;
    }

    public void getURLData(Map map, final HttpService.HttpServiceResult callback) {
//        map.put("type", "1");
//        map.put("curPage", "1");
        map.put("pageSize", "4");
        //http://192.168.0.53:8085/jyptdbctl/video/getVideoPage?type=1&curPage=1&pageSize=4
        Log.d("lylog", "getURLData type = " + map.get("type") + " curPage" + map.get("curPage"));
        String url = Utils.NetWorkUtil.BASE_URL + "type=" + map.get("type") + "&" + "curPage=" + map.get("curPage") + "&" + "pageSize=" + map.get("pageSize");

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("lylogNet", " response error1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.success(response);
                } else {
                    callback.failed(response.body().string());
                    Log.d("lylogNet", " response error2");
                }
            }
        });
    }

    public static ArrayList<Map<String, String>> getUrlList() {
        ArrayList<Map<String, String>> urlList = new ArrayList();
        Map<String, String> map = new HashMap<>();
        map.put("id", "0");
//        map.put("url", "http://bjcdn2.vod.migucloud.com/mgc_transfiles/200010145/2019/5/19/2EFBSUWNbMUSHXxvBeSQz/cld640p/video_2EFBSUWNbMUSHXxvBeSQz_cld640p.m3u8");
        map.put("url", "http://eos-beijing-2.cmecloud.cn/vi1/200010145/1M/2NlqT4t0PGV0cgCcQLtb/1M2NlqT4t0PGV0cgCcQLtb.mp4");
        Map<String, String> map1 = new HashMap<>();
        map1.put("id", "1");
        map1.put("url", "http://bjcdn2.vod.migucloud.com/mgc_transfiles/200010145/2019/5/19/1EYo9UNOF95HCK30a3mHY/cld640p/video_1EYo9UNOF95HCK30a3mHY_cld640p.m3u8");
        urlList.add(map);
        urlList.add(map1);
        //getAllurlonline();
        return urlList;
    }

    private static void getAllurlonline() {

        String url = "http://192.168.0.53:8085/jyptdbctl/video/getVideoPage?&curPage=1&pageSize=100";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("lylogNet", " getLXdata response error1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {


                } else {

                }

            }
        });//得到Response 对象
    }


    public ArrayList<Map<String, String>> getLXdata(final HttpService.HttpServiceLX callback) {
        String url = "http://192.168.0.53:8085/jyptdbctl/admin/code/selectCodeByDMLB?xtlb=50&dmlb=0011";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("lylogNet", " getLXdata response error1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.isOK(response);
                    Log.d("lylogss", " onResponse");

                } else {
                    callback.isNO(response.body().string());
                    Log.d("lylogNet", " getLXdata response error2");
                }

            }
        });//得到Response 对象
        return hashMaps;
    }

    public void getPostData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Map<String, String> smallMap = new HashMap<String, String>();
                smallMap.put("uname", "ydy_1YGaWM");
                smallMap.put("passwd", "4629zDZQVHycfNnb");
                JsonParser parser = new JsonParser();
                JsonElement s = parser.parse(smallMap.toString());
                Log.i("lylogsssa", s.toString());

                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = RequestBody.create(JSON, "{\"uname\"=\"ydy_1YGaWM\",\"passwd\"=\"4629zDZQVHycfNnb\"}");
                Request request = new Request.Builder()
                        .url("http://bj.migucloud.com/a0/user.auth")
                        .post(requestBody)
                        .build();


                try {
                    Response response = okHttpClient.newCall(request).execute();
                    //判断请求是否成功
                    if (response.isSuccessful()) {
                        //打印服务端返回结果
                        Log.i("lylogsssa", response.body().string());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void getTokenData(final HttpService.HttpTokenCallback callback, final Context context) {
        this.mContext = context;
        String url = "http://192.168.0.53:8085/jyptdbctl/video/getUidToken";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.showToast(context, "Token 获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JsonParser parser = new JsonParser();
                JsonObject jsons = (JsonObject) parser.parse(result);
                String code = jsons.get("code").getAsString();
                if ("0".equals(code)) {
                    JsonObject dataJson = jsons.get("data").getAsJsonObject();
                    uid = dataJson.get("uid").getAsString();
                    token = dataJson.get("token").getAsString();
                    Log.d("lylogr", " uid = " + uid + "\n" + " token =" + token);
                    callback.tokenCallback(uid, token);
                } else {
                    Utils.showToast(context, "token state = 1");
                }

            }
        });
    }

    public void getTrueUrl(final Handler mHandler, final Context context, String uid, String token, String maplistURL) {
        //http://bj.migucloud.com/vod2/v1/download_spotviurl?uid=200010145&token=6ce31a05b3acad52c5a040325e9402d9160fd5d7f091422ad2303ea48caede6c91b35e82a3004b8c1861842b8d&vid=1M2NlqT4t0PGV0cgCcQLtb&vtype=0,1,2
        String url = Utils.NetWorkUtil.BASE_URL_TRUE + "uid=" + uid + "&token=" + token + "&vid=" + maplistURL + "&vtype=0,1,2";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.showToast(context, "获取真实的URL失败");
                Message msg = new Message();
                msg.what = Utils.URL_FAILED;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                String urlTrue = JsonUtils.getIncetence().getTrueUrl(result);
                Log.d("lylogurl","urlTrue = "+urlTrue);
                Message msg = new Message();
                msg.what = Utils.URL_SUCCESS;
                msg.obj = urlTrue;
                mHandler.sendMessage(msg);
            }
        });
    }
}
