package space.cc.com.fragmenttest.domain.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类描述：
 * 创建人：姚健
 * 创建时间：2016/12/1 10:08
 */

public class JsonUtil {

    public static boolean getJsontoBoolean(String string, String key) {
        boolean b = false;
        JSONObject json = null;
        try {
            json = new JSONObject(string);
            if (json.has(key)) {
                b = json.getBoolean(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static String getJsontoString(String string, String key) {
        String s = "";
        JSONObject json = null;
        try {
            json = new JSONObject(string);
            if (json.has(key)) {
                s = json.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            s = "";
        }
        return s;
    }

    public static int getJsontoInt(String string, String key){
        int i = 0;
        JSONObject json = null;
        try {
            json = new JSONObject(string);
            if (json.has(key)) {
                i = json.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static double getJsontoDouble(String string, String key){
        double i = 0;
        JSONObject json = null;
        try {
            json = new JSONObject(string);
            if (json.has(key)) {
                i = json.getDouble(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static long getJsontoLong(String string, String key){
        long i = 0;
        JSONObject json = null;
        try {
            json = new JSONObject(string);
            if (json.has(key)) {
                i = json.getLong(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return i;
    }
}
