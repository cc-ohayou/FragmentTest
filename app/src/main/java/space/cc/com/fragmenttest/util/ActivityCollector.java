package space.cc.com.fragmenttest.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
   /**
    *关闭所有的活动
    *@author CF
    *created at 2018/10/21/021  21:26
    */
    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        //杀掉当前进程
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
