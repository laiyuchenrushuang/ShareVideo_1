package video.hc.com.videodemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import video.hc.com.videodemo.R;
import video.hc.com.videodemo.adapter.GridViewAdapter;
import video.hc.com.videodemo.base.BaseFragment;

/**
 * Created by ly on 2019/6/6.
 */

public class Fragment1 extends BaseFragment {
    @BindView(R.id.gv_video)
    GridView gv_video;
    @BindView(R.id.rb_video_lx)
    RadioGroup radioGroup;
    GridViewAdapter gridViewAdapter;
    private int position;
    private ArrayList<Map<String,String>> listdata = new ArrayList();
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
        return inflater.inflate(R.layout.fragment1, null);
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
        listdata.add(map1);
        listdata.add(map2);
        listdata.add(map3);
        listdata.add(map4);

        gridViewAdapter = new GridViewAdapter(getContext(),listdata);
        gv_video.setAdapter(gridViewAdapter);
        addview(radioGroup);
    }

    private void addview(RadioGroup radioGroup) {
        final List<String> list = getListSize();

        for (int i = 0; i < list.size() - 1; i++) {

            RadioButton button = new RadioButton(getContext());
            button.setButtonDrawable(0);
            final String s = list.get(i).toString();
            button.setText(s);
            if (i == 0) {
                button.setTextColor(Color.YELLOW);
                button.setBackground(getResources().getDrawable(R.drawable.item_lx_button));
            }
            radioGroup.addView(button);
            
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int position) {
                    Log.d("lylog","onCheckedChanged i = "+position);
                    setIndex(position);
                    RadioButton radioButton = (RadioButton) (radioGroup.getChildAt(0));
                    if(position != 1){
                        setBackground(false,radioButton);
                    }

                }
            });


            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d("lylog","onCheckedChanged getIndex() = "+getIndex());

                    if (b) {
                        setBackground(true,(RadioButton) compoundButton);
                    }else {
                        setBackground(false,(RadioButton) compoundButton);
                    }
                }
            });
        }
    }

    private void setBackground(boolean flag, RadioButton radioButton) {
        if(flag){
            radioButton.setTextColor(Color.YELLOW);
            radioButton.setBackground(getResources().getDrawable(R.drawable.item_lx_button));
        }else{
            radioButton.setTextColor(Color.WHITE);
            radioButton.setBackground(null);
        }
    }

    private void setIndex(int position) {
        this.position = position;
    }
    private int getIndex() {
       return this.position;
    }
    private List<String> getListSize() {
        List<String> list = new ArrayList<String>();
        list.add("视频类型一");
        list.add("视频类型二");
        list.add("视频类型三");
        list.add("视频类型四");
        list.add("视频类型五");
        list.add("视频类型六");
        list.add("视频类型七");
        return list;
    }

}
