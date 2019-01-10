package space.cc.com.fragmenttest.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
/**
     * @author  CF
     * @date   2019/1/10
     * @description
     *
     */
public class TabLayoutAdapter  extends FragmentPagerAdapter{


    private String [] title ;
    private List<Fragment> fragmentList;
    public TabLayoutAdapter(FragmentManager fm, String[] titles, List<Fragment> fragmentList) {
        super(fm);
        this.title=titles;
        this.fragmentList = fragmentList;
    }
    @Override
    public Fragment getItem(int position) {

        if(fragmentList.get(position)==null){
            initFragment(position);
        }

        return fragmentList.get(position);
    }

    private void initFragment(int position) {
        switch(position){
            case 0:

                break;

        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}