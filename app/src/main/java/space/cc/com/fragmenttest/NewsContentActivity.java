package space.cc.com.fragmenttest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class NewsContentActivity extends AppCompatActivity {


    public  static void actionStart(Context context, String title, String content){
        Intent intent=new Intent(context,NewsContentActivity.class);
        intent.putExtra("news_title",title );
        intent.putExtra("news_content",content );
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        //获取传入的新闻标题
        String newsTitle=getIntent().getStringExtra("news_title");
        //获取传入新闻内容
        String newsContent=getIntent().getStringExtra("news_content");
        NewsContentFragment fragment= (NewsContentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.news_content_fragment);
        //刷新fragment的界面
        fragment.refresh(newsTitle,newsContent);

    }
}
