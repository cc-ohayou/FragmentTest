package space.cc.com.fragmenttest.domain.util;

import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by sunxx on 2017/12/14.
 * 日志文件
 */
public  class LogUtil {
    public static String TIRE_TAG="1";
    public static String PHY_TAG="2";
    public static String STATE_TAG="3";
    public static boolean isDebug=true;
    public final static String FILE_PATH = "/com.cc.space/data";//包名文件夹
    public static File file;
    public  static FileOutputStream fos;
    public static LogUtil logs;
    public LogUtil() {
    }
    public static synchronized LogUtil getInstance(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_PATH + "/log/a.txt";
        try {
            file = new File(path);
            fos = new FileOutputStream(file, true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (logs==null){
            logs=new LogUtil();
        }
        return logs;
    }
    //写入日志文件
    public  void writeEvent(String tag,String msg){
        if (!isDebug){
            return;
        }
        if (fos==null){
            return;
        }
        try
        {
            fos.write(((System.currentTimeMillis())+"|"+tag+"|"+msg+"\r").getBytes());
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeEvent(String tag, String s, Exception e) {
        StringBuilder sb=new StringBuilder(s);
        for (int i = 0; i < e.getStackTrace().length; i++) {
            sb.append("\n").append(e.getStackTrace()[i].toString());
        }
        writeEvent(tag,sb.toString());
    }
}
