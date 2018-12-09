package space.cc.com.fragmenttest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.Random;

import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.util.StringUtil;
import space.cc.com.fragmenttest.litepals.Book;

public class LitePalTestActivity extends BaseActivity implements View.OnClickListener{

    private Random random=new Random();
    private CheckBox updateCondition;
    private EditText price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lite_pal_test);
        setButOnclickListenerByRid(R.id.create_db_litePal,this);
        setButOnclickListenerByRid(R.id.add_but_litePal,this);
        setButOnclickListenerByRid(R.id.update_but_litePal,this);
        setButOnclickListenerByRid(R.id.del_but_litePal,this);
        setButOnclickListenerByRid(R.id.query_but_litePal,this);
        updateCondition=findViewById(R.id.update_condition);
        price=findViewById(R.id.condition_price);
    }

    @Override
    public void requestPermission() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_db_litePal:
                //litePal初始化数据库就是这么简单 会根据assets目录下的litepal.xml文件
                // 读取初始化的数据库信息 表信息等
                Connector.getDatabase();
                toastSimple("init db info");
                break;
            case R.id.add_but_litePal:
                Book book=new Book();
                book.setAuthor("cc");
                book.setId(System.currentTimeMillis());
                book.setName("hahah");
                book.setPages(random.nextInt(200));
                String priceText=price.getText().toString();
                double priceDouble= StringUtil.isEmpty(priceText)?10.00:Double.parseDouble(priceText);
                book.setPrice(priceDouble);
                boolean flag=book.save();
                toastSimple("add db data flag="+flag);
                break;
            case R.id.update_but_litePal:
                Book bookUpdate=new Book();
                bookUpdate.setPrice(20.00);
                //save方法 如果满足条件的数据已经存在则进行更新 否则添加 只能更新同一对象 限制性较大
//                bookUpdate.save();
//                bookUpdate.updateAll();
                //把作者是cc价格大于30的书籍价格改为20
                boolean biggerThanMode  = updateCondition.isChecked();
                String sqlCondition="author=?  and price<?";
                if(biggerThanMode){
                    sqlCondition=" author=?  and price>?";
                }
                String priceCondition=price.getText().toString();
                int cou=bookUpdate.updateAll(sqlCondition,"cc",  priceCondition);
                String mode=biggerThanMode==true?"bigger than":"small than";
                toastSimple("updateAll cou="+cou+",mode="+mode);
                break;
            case R.id.del_but_litePal:
                boolean biggerThanModeSign  = updateCondition.isChecked();
                String sqlConditionDel="  price<?";
                if(biggerThanModeSign){
                    sqlConditionDel="   price>?";
                }
                String priceConditionDel=price.getText().toString();
                int delCou=LitePal.deleteAll(Book.class,sqlConditionDel,priceConditionDel);
                toastSimple("deleteAll cou="+delCou+",sqlCondition="+sqlConditionDel);

                break;
            case R.id.query_but_litePal:

                ShowBooksActivity.startShowBookActivity(getBaseContext(),null);
                break;
            default :
                break;
        }
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
