package space.cc.com.fragmenttest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
     * @description
     * @author CF
     * created at 2018/11/6/006  0:17
     */
public class NewsContentFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.news_content_frag, container, false);
        return view;
    }

    public  void refresh(String newsTitle,String newsContent) {
        View newsLayout = view.findViewById(R.id.news_layout);
        newsLayout.setVisibility(View.VISIBLE);
        TextView title=view.findViewById(R.id.news_title);
        TextView content=view.findViewById(R.id.news_content);
        //刷新标题
        title.setText(newsTitle);
        //刷新内容
        content.setText(newsContent);
    }






}
