package space.cc.com.fragmenttest.adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import space.cc.com.fragmenttest.fragment.DynamicFragment;
import space.cc.com.fragmenttest.fragment.OperationListFragment;

/**
     * @author  CF
     * @date   2019/1/10
     * @description
     *
     */
public class TabLayoutAdapter  extends FragmentPagerAdapter{


    private String [] title ;
    private Activity activity ;
    public TabLayoutAdapter(FragmentManager fm, String[] titles, Activity activity) {
        super(fm);
        this.title=titles;
        this.activity=activity;
    }



    @Override
    public Fragment getItem(int position) {
            return  initFragment(position);
    }

    private Fragment initFragment(int position) {
        switch(position){
            case 0:
                return  OperationListFragment.newInstance(title[position],activity);
            case 1:
                return  (DynamicFragment.newInstance(title[position]));
            default:
                return  OperationListFragment.newInstance(title[position],activity);

        }
    }

    @Override
    public int getCount() {
        return title.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}