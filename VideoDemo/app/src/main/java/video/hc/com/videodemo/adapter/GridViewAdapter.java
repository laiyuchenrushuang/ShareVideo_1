package video.hc.com.videodemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import video.hc.com.videodemo.R;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Map<String, String>> listmap;

    private LayoutInflater layoutInflater;
    public GridViewAdapter(Context context, ArrayList<Map<String, String>> listdata) {
        this.context = context;
        this.listmap = listdata;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listmap.size();
    }

    @Override
    public Object getItem(int position) {
        return listmap.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.video_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_gridView_item);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_gridView_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Map<String,String> map = listmap.get(position);
        if (map != null) {
            holder.textView.setText(map.get("theme")+"");
            holder.imageView.setBackgroundResource(R.mipmap.bg_waiting);
        }
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("lylog"," click convertView +"+position);
//                String url = listmap.get(position).get("url");
//            }
//        });
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
