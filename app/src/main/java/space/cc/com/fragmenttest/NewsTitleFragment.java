package space.cc.com.fragmenttest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import space.cc.com.fragmenttest.domain.bizobject.News;

public class NewsTitleFragment extends Fragment {

    private boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.news_title_frag,container,false);
        RecyclerView newsTitleRecycleView=view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        newsTitleRecycleView.setLayoutManager(layoutManager);
        NewsAdapter adapter=new NewsAdapter(initNews());
        newsTitleRecycleView.setAdapter(adapter);
        return view;
    }

    private List<News> initNews() {
     List<News> newsList=new ArrayList<>();
        for (int i = 0; i <50 ; i++) {
            News news=new News();
            news.setTitle("this is news title "+i);
            news.setContent(getRandomLengthName("this is news title "+i));
            newsList.add(news);
        }
        return newsList;
    }

    /**
     * @description
     * @author CF
     * created at 2018/10/31/031  0:15
     */
    private String getRandomLengthName(String name) {
        Random random=new Random();
        int length=random.nextInt(20)+1;
        StringBuilder sb=new StringBuilder(name);
        for(int i=0;i<length;i++){
            sb.append(name);
        }
        return sb.toString();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content_layout)!=null){
            isTwoPane=true;
        }else{
            isTwoPane=false;
        }

    }



    class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<News> newsList;
        public NewsAdapter(List<News> news){
            this.newsList=news;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder=new ViewHolder(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news=newsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        //双页模式 刷新NewsContentFragment中的内容
                        NewsContentFragment contentFragment= (NewsContentFragment) getFragmentManager()
                                .findFragmentById(R.id.news_content_fragment);
                        contentFragment.refresh(news.getTitle(),news.getContent());
                    }else{
                        //如果是单页模式只启动一个新的content的activity
                        NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());

                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                News news=newsList.get(position);
                holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                newsTitleText=itemView.findViewById(R.id.news_title);
            }
        }
    }
}
