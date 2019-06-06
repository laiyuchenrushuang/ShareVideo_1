package video.hc.com.videodemo.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ly on 2019/6/6.
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder bind;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutView(inflater,container);
        bind = ButterKnife.bind(this,view);
        initView();
        return view;
    }

    public abstract View getLayoutView(LayoutInflater inflater,ViewGroup container);
    public abstract void initView();
    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    public void showToast(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
