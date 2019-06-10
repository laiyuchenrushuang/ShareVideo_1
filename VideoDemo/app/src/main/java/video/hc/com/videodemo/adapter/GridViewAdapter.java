package video.hc.com.videodemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
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
    public View getView(int position, View convertView, ViewGroup parent) {

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

        Map<String, String> map = listmap.get(position);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg_waiting);
        if (map != null) {
            holder.textView.setText(map.get("theme") + "");
            holder.imageView.setImageBitmap(bitmap);
        }else {
            holder.textView.setText("null");
            holder.imageView.setImageBitmap(bitmap);
        }

        Log.d("lylog", " sssssss");


//      new Thread(new ImageTask(map)).start();

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    private class ImageTask implements Runnable {
        Map<String, String> map;

        public ImageTask(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public void run() {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever(); //获取网络视频
            String url = map.get("url");
            Log.d("lylog", " url11 = " + url);
            retriever.setDataSource(url, new HashMap<String, String>());

        }
    }
}
