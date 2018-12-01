package space.cc.com.fragmenttest.domain.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xiny on 2015/11/5.
 */
public class XMd5 {

    /**
     * MD5加密
     * @param text
     * @return
     */
    public static String encode(String text){
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for(byte b : result){
                String hex = Integer.toHexString(b&0xff);
                if(hex.length()==1){
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";//can't reach
        }
    }

}
