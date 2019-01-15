package space.cc.com.fragmenttest.domain;

import com.facebook.android.crypto.keychain.AndroidConceal;
import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.CryptoConfig;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.keychain.KeyChain;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import space.cc.com.fragmenttest.MyApplication;
import space.cc.com.fragmenttest.domain.util.StringUtils;

/**
 * Conceal加密工具
 * Created by yaojian on 2017/9/18 14:25
 */

public class ConcealUtils {

    public static final String ENTITY = "ssc_app";

    /**
     * 加密
     * @param content
     */
    public static String encrypt(String content){
        if(StringUtils.isEmpty(content))
            return "";
//        return Base64.encodeToString(encrypt(content.getBytes(),ENTITY), Base64.DEFAULT);
        return content;
    }

    /**
     * 加密
     * @param content
     */
    public static byte[] encrypt(byte[] content){
        return encrypt(content,ENTITY);
    }

    /**
     * 加密
     * @param content
     */
    public static byte[] encrypt(byte[] content, String entity){
        byte[] ciphertext = null;
        KeyChain keyChain = new SharedPrefsBackedKeyChain(MyApplication.getAppContext(), CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        if (!crypto.isAvailable()) {
            return ciphertext;
        }
        try {
            ciphertext = crypto.encrypt(content, Entity.create(entity));
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ciphertext;
    }

    /**
     * 解密
     * @param ciphertext
     */
    public static String decrypt(String ciphertext){
        if (StringUtils.isEmpty(ciphertext))
            return "";
        return    ciphertext;
//        return new String(decrypt(Base64.decode(ciphertext, Base64.DEFAULT),ENTITY));
    }

    /**
     * 解密
     * @param ciphertext
     */
    public static byte[] decrypt(byte[] ciphertext){
        return decrypt(ciphertext,ENTITY);
    }

    /**
     * 解密
     * @param ciphertext
     */
    public static byte[] decrypt(byte[] ciphertext, String entity){
        byte[] content = null;
        KeyChain keyChain = new SharedPrefsBackedKeyChain(MyApplication.getAppContext(), CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        try {
            content = crypto.decrypt(ciphertext, Entity.create(entity));
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 文件加密
     */
    public static void encrypt(File file, File outFile){
        encrypt(file,outFile,ENTITY);
    }

    /**
     * 文件加密
     */
    public static void encrypt(File file, File outfile, String entity){
        KeyChain keyChain = new SharedPrefsBackedKeyChain(MyApplication.getAppContext(), CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);
        if (!crypto.isAvailable()) {
            return;
        }
        try {
            OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(file));
            OutputStream outputStream = crypto.getCipherOutputStream(fileStream, Entity.create(entity));
            FileInputStream fis = new FileInputStream(outfile);

            int i=0;
            byte[] buffer = new byte[1024];
            while ((i = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, i);
                outputStream.flush();
            }

            fis.close();
            outputStream.close();
            fileStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件解密
     */
    public static void decrypt(File file, File outfile){
        decrypt(file,outfile,ENTITY);
    }

    /**
     * 文件解密
     */
    public static void decrypt(File file, File outfile, String entity){
        KeyChain keyChain = new SharedPrefsBackedKeyChain(MyApplication.getAppContext(), CryptoConfig.KEY_256);
        Crypto crypto = AndroidConceal.get().createDefaultCrypto(keyChain);

        try {

            FileOutputStream out = new FileOutputStream(outfile);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            FileInputStream fileStream = new FileInputStream(file);

            InputStream inputStream = crypto.getCipherInputStream(fileStream,Entity.create(entity));

            int read;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
                bos.flush();
            }

            out.close();
            inputStream.close();
            fileStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
