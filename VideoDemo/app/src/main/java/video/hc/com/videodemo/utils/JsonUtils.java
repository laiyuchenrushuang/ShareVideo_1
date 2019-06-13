package video.hc.com.videodemo.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import video.hc.com.videodemo.enity.VideoBean;

/**
 * Created by ly on 2019/6/9.
 */

public class JsonUtils {
    static JsonUtils jsonUtils;

    public static JsonUtils getIncetence() {
        if (jsonUtils == null) {
            jsonUtils = new JsonUtils();
            return jsonUtils;
        }
        return jsonUtils;
    }

    public ArrayList<Map<String, String>> mapLXlist = new ArrayList<>();
    public ArrayList<Map<String, String>> maplist = new ArrayList<>();

    public ArrayList<Map<String, String>> getMapData(String result) {
        JsonParser parser = new JsonParser();  //创建JSON解析器
        JsonObject object = (JsonObject) parser.parse(result);
        JsonObject data = (JsonObject) object.get("data");
        JsonArray listarray = data.getAsJsonArray("list");
        Log.d("lylogr", " listarray = " + listarray.toString());
        for (int i = 0; i < listarray.size(); i++) {
            Map<String, String> map = new HashMap<>();
            JsonObject subObject = listarray.get(i).getAsJsonObject();
            map.put("url", subObject.get("url").getAsString());
            Log.d("lylogr", " subObject = " + subObject.get("url").getAsString());
            map.put("theme", subObject.get("theme").getAsString());
            map.put("count", data.get("count").getAsString());
            map.put("pageNo", data.get("pageNo").getAsString());
            maplist.add(map);
        }
        return maplist;
    }

    public ArrayList<Map<String, String>> getLXMapData(String result) {
        JsonParser parser = new JsonParser();
        JsonObject jsons = (JsonObject) parser.parse(result);
        JsonArray array = jsons.getAsJsonArray("data");
        for (int i = 0; i < array.size(); i++) {
            Map<String, String> map = new HashMap<>();
            JsonObject subObject = array.get(i).getAsJsonObject();
            map.put("type", subObject.get("dmz").getAsString());
            map.put("lx", subObject.get("dmsm1").getAsString());
            mapLXlist.add(map);
        }
        return mapLXlist;
    }


    public String getTrueUrl(String result) {
        JsonParser parser = new JsonParser();
        JsonObject jsons = (JsonObject) parser.parse(result);

        return jsons.get("result").getAsString();
    }

    public ArrayList<String> geturlList(String result) {
        ArrayList<String> urllist = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject jsons = (JsonObject) parser.parse(result);
        JsonObject jsonObject = jsons.get("data").getAsJsonObject();
        JsonArray array = jsonObject.get("list").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            JsonElement url = array.get(i).getAsJsonObject().get("url");
            urllist.add(url.toString());
        }
        Log.i("lylog", "   urllist = " + urllist.toString());

        return urllist;
    }
}
