package space.cc.com.fragmenttest;

import android.os.Bundle;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
public static MainActivity singlelyton=new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Button but01 = findViewById(R.id.but01);
//        but01.setOnClickListener(this);
//        replaceFragment(new RightFragment());

    }

    private void replaceFragment(Fragment fragment) {
         //1、获取碎片管理器
        FragmentManager manager=getSupportFragmentManager();
        //2、开启事务
        FragmentTransaction transaction=manager.beginTransaction();
        //3、替换右边的布局内容为新的fragment布局
//        transaction.replace(R.id.right_layout,fragment);
        //将被替换的fragment加入到返回栈中 这样点击返回不会直接退出
        transaction.addToBackStack(null);
        //4、提交事务
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
              switch(v.getId()) {
                  case R.id.but01:
                      replaceFragment(new RightFragment01());
                  default:
                      break;
              }
    }
}
