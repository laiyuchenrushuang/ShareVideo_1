package video.hc.com.videodemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import video.hc.com.videodemo.R;
import video.hc.com.videodemo.adapter.GridViewAdapter;
import video.hc.com.videodemo.base.BaseFragment;

/**
 * Created by ly on 2019/6/6.
 */

public class Fragment2 extends BaseFragment {
    @BindView(R.id.gv_video)
    GridView gv_video;
    GridViewAdapter gridViewAdapter;
    private ArrayList<Map<String,String>> listdata = new ArrayList();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View getLayoutView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment2,null);
    }

    @Override
    public void initView() {
        Map<String,String> map1 = new HashMap<>();
        map1.put("name","a");
        Map<String,String> map2 = new HashMap<>();
        map2.put("name","b");
        Map<String,String> map3 = new HashMap<>();
        map3.put("name","c");
        Map<String,String> map4 = new HashMap<>();
        map4.put("name","d");

        Map<String,String> map5 = new HashMap<>();
        map5.put("name","e");
        Map<String,String> map6 = new HashMap<>();
        map6.put("name","f");
        listdata.add(map1);
        listdata.add(map2);
        listdata.add(map3);
        listdata.add(map4);
        listdata.add(map5);
        listdata.add(map6);

        gridViewAdapter = new GridViewAdapter(getContext(),listdata);
        gv_video.setAdapter(gridViewAdapter);
    }
}
