package space.cc.com.fragmenttest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
     * @description
     * @author CF
     * created at 2018/11/4/004  22:08
     */
public class LeftFragment extends Fragment {

    @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState){
        //加载左边的碎片布局
        View view=inflater.inflate(R.layout.left_fragment,container,false);
        return  view;
    }


}
