package space.cc.com.fragmenttest.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import space.cc.com.fragmenttest.R;

public class DynamicFragment extends BaseFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DynamicFragment newInstance(String tabTitle) {
        DynamicFragment fragment = new DynamicFragment();
    /*        Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dynamic_fragment, container, false);
        TextView textView =  rootView.findViewById(R.id.dynamic_content);
            textView.setText("敬请期待");
        return rootView;
    }
}
