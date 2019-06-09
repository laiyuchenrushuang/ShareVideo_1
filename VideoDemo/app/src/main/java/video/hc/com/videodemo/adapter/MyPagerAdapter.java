package video.hc.com.videodemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import video.hc.com.videodemo.base.BaseFragment;

public class MyPagerAdapter extends FragmentPagerAdapter{
    private FragmentManager fragmetnmanager;  //创建FragmentManager
    private List<Fragment> listfragment; //创建一个List<Fragment>

    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> listf) {
        super(fm);
        this.fragmetnmanager=fm;
        this.listfragment=listf;
    }

    @Override
    public Fragment getItem(int position) {
        return listfragment.get(position);
    }

    @Override
    public int getCount() {
        return listfragment.size();
    }
}
