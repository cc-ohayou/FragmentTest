package space.cc.com.fragmenttest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.List;
import java.util.Map;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.adapter.BookAdapter;
import space.cc.com.fragmenttest.litepals.Book;

public class ShowBooksActivity extends BaseActivity {
    List<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_books);
        initData();
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        /*LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //设置布局的排列方向 默认是纵向的此处改为横向排列 这样就可以横向滚动了
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);*/

        //瀑布流布局 构造参数 第一个3 是指有三列 流动方向垂直
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);;
        recyclerView.setLayoutManager(layoutManager);
        //由于图片不规则统一造成展示不规则的原因 此处将item的宽高都根据屏幕自适应调整
        int standardHeight=getViewIemHeight(4,ShowBooksActivity.this);
//        int itemWidth=getViewIemWidth(5);
        //传输数据到适配器
//        RecyclerFruitAdapter adapter=new RecyclerFruitAdapter(fruits,standardHeight,itemWidth);
        //瀑布流时垂直布局尽量不指定高度 水平布局尽量不指定宽度 不然不大容易看出布局特性
        BookAdapter adapter=new BookAdapter(books,standardHeight);
        //设置view的适配器从而获取到转化过后可使用的数据
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
                books= LitePal.findAll(Book.class);

       /*
       太笨拙不建议使用
       Cursor cursor=LitePal.findBySQL("select * from book where price>? order by pages desc","10");
        while(cursor.moveToNext()){
            cursor.getInt()
        }*/

        //        books=LitePal.findAll(Book.class);
        /*LitePal.select("name","author")
                .where("price>?","400")
                .order("pages desc")
                .limit(10)
                .offset(10)
                .find(Book.class);*/
        /**
         * 常见查询方法
         * LitePal.findFirst();
         * LitePal.findLast()
         * LitePal.count();
         * LitePal.average() 求平均
         *  LitePal.findBySQL()
         *  只获取指定列
         *          LitePal.select("name","author").find(Book.class);
         LitePal.where().find(Book.class);
         LitePal.order("price desc").find(Book.class);
         查询前三条
         LitePal.limit(3).find(Book.class);
         从第二条开始的三条 偏移量1
         LitePal.limit(3).offset(1).find(Book.class);

         组合使用实例
         LitePal.select("name","author")
         .where("price>?","400")
         .order("pages desc")
         .limit(10)
         .offset(10)
         .find(Book.class);
         */
    }




   public static void startShowBookActivity(Context context, Map<String, String> map){
       startAction(context,map,ShowBooksActivity.class);
    }

}
