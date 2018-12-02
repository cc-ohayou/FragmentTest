package space.cc.com.fragmenttest.domain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：姚健
 * 创建时间：2016/12/1 10:08
 */

public class JsonUtil {
    public static final List EMPTY_LIST = new ArrayList(0);
    public static final JSONObject EMPTY_JSON_OBJECT = new JSONObject();

    public static boolean getJsontoBoolean(String string, String key) {
        boolean b = false;
        JSONObject json = EMPTY_JSON_OBJECT;
        try {
            json = JSONObject.parseObject(string);
            if (json.containsKey(key)) {
                b = json.getBoolean(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    public static String getJsontoString(String string, String key) {
        String s = "";
        JSONObject json = EMPTY_JSON_OBJECT;
        try {
            json = JSONObject.parseObject(string);
            if (json.containsKey(key)) {
                s = json.getString(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }
  /**
     * @description
     * @author CF
     * created at 2018/12/2/002  21:37
     */
    public static List getJsontoList(String string, String key) {
        String s = "";
        List list =EMPTY_LIST;
        try {
            list=JSON.parseObject(string,List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }
  /**
     * @description
     * @author CF
     * created at 2018/12/2/002  21:36
     */
    public static JSONObject getJsonListFirst(String string, String key) {
        String s = "";
        List<JSONObject> list =EMPTY_LIST;
        try {
            list=JSON.parseObject(string,List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list.size()>0) {
            return list.get(0);
        }
        return EMPTY_JSON_OBJECT;
    }

    public static int getJsontoInt(String string, String key){
        int i = 0;
        JSONObject json = EMPTY_JSON_OBJECT;
        try {
            json = JSONObject.parseObject(string);
            if (json.containsKey(key)) {
                i = json.getIntValue(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return i;
    }

    public static double getJsontoDouble(String string, String key){
        double i = 0;
        JSONObject json = EMPTY_JSON_OBJECT;
        try {
            json = JSONObject.parseObject(string);
            if (json.containsKey(key)) {
                i = json.getDouble(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return i;
    }

    public static long getJsontoLong(String string, String key){
        long i = 0;
        JSONObject json = EMPTY_JSON_OBJECT;
        try {
            json = JSONObject.parseObject(string);
            if (json.containsKey(key)) {
                i = json.getLong(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return i;
    }
}
