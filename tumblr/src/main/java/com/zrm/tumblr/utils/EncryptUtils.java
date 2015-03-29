package com.zrm.tumblr.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
/**
 * 提供部分加密方法
 */
public class EncryptUtils {

    public static String TAG = EncryptUtils.class.getSimpleName();

    /**
     * 对字符串进行MD5进行加密处理
     * @param msg 待加密的字符串
     * @return 加密后字符串
     */
    public static String encryptMD5(String msg){
        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(msg.getBytes("UTF-8"));
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = null;
        if(bytes != null){
            sign = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    sign.append("0");
                }
                sign.append(hex);
            }
        }

        return sign == null?null:sign.toString();
    }

    /**
     * 基本加密处理
     * @param msg
     * @param salt
     * @return
     */
    public static String encrypt(String msg,String salt){
        return encryptMD5(new StringBuffer().append(salt).append(msg).append(salt).toString());
    }

    /**
     * 盐值的原理非常简单，就是先把密码和盐值指定的内容合并在一起，再使用md5对合并后的内容进行演算，
     * 这样一来，就算密码是一个很常见的字符串，再加上用户名，最后算出来的md5值就没那么容易猜出来了。
     * 因为攻击者不知道盐值的值，也很难反算出密码原文。
     * @param msg
     * @return
     */
    public static String encryptSalt(String msg,String salt) {
        return encrypt(msg, salt);
    }
    /**
     * SHA（Secure Hash Algorithm，安全散列算法）是消息摘要算法的一种，被广泛认可的MD5算法的继任者。
     * SHA算法家族目前共有SHA-0、SHA-1、SHA-224、SHA-256、SHA-384和SHA-512五种算法，
     * 通常将后四种算法并称为SHA-2算法
     * @param msg
     * @return
     */
    public static String encryptSHA(String msg) {
        String salt = getSaltSHA1();
        StringBuilder sb = new StringBuilder();
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(msg.getBytes());
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        }catch(Exception e){
        }
        return sb.toString();
    }
    /**
     * PBKDF2加密
     * @param msg
     * @return
     */
    public static String encryptPBKDF2(String msg) {
        try {
            int iterations = 1000;
            char[] chars = msg.toCharArray();
            byte[] salt = getSalt().getBytes();
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + toHex(salt) + toHex(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 转化十六进制
     * @param array
     * @return
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0" +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    /**
     * <ul>
     * <li>SHA-1 (Simplest one – 160 bits Hash)</li>
     * <li>SHA-256 (Stronger than SHA-1 – 256 bits Hash)</li>
     * <li>HA-384 (Stronger than SHA-256 – 384 bits Hash)</li>
     * <li>SHA-512 (Stronger than SHA-384 – 512 bits Hash)</li>
     * </ul>
     * @return
     */
    private static String getSaltSHA1(){
        SecureRandom sr;
        byte[] salt = new byte[16];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return salt.toString();
    }
    /**
     * 盐值的原理非常简单，就是先把密码和盐值指定的内容合并在一起，再使用md5对合并后的内容进行演算，
     * 这样一来，就算密码是一个很常见的字符串，再加上用户名，最后算出来的md5值就没那么容易猜出来了。
     * 因为攻击者不知道盐值的值，也很难反算出密码原文。
     * @return
     */
    public static String getSalt(){
        return StringUtils.remove(UUID.randomUUID().toString(), "-").substring(0, 16);
    }
    public static void main(String[] args) {
        String salt = getSalt();
        for(int i=0;i<10;i++){
            System.out.println(EncryptUtils.encryptSalt("123456", salt));
        }


        /*System.out.println(EncryptUtils.encryptMD5("123456"));


        System.out.println(EncryptUtils.encryptSHA("123456"));
        System.out.println(EncryptUtils.encryptPBKDF2("123456"));*/
    }




    /**
     * 签名生成算法
     * @param params 请求参数集，所有参数必须已转换为字符串类型
     * @param secret 签名密钥
     * @return 签名
     * @throws java.io.IOException
     */
    public static String getSignature(Map<String,String> params, String secret)
    {
        return getSignature(params,secret,null);
    }

    /**
     * 除exceptKey以外所有key都参与签名
     * @param params
     * @param secret
     * @param exceptKey
     * @return
     */
    public static String getSignature(Map<String,String> params, String secret,String exceptKey)
    {
        if(exceptKey != null){
            params.remove(exceptKey);
        }

        // 先将参数以其参数名的字典序升序进行排序
        Set<Map.Entry<String, String>> entrys = new TreeMap<String, String>(params).entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            /*if(exceptKey != null && exceptKey.equals(param.getKey())){
                continue;
            }*/
            basestring.append(param.getKey()).append("=").append(param.getValue());
        }
        basestring.append(secret);

        return encryptMD5(basestring.toString());
    }

    public static boolean verifySig(HashMap<String,String> params, String secret,String exceptKey,String sig){
        if(sig == null){
            return false;
        }
        String god = getSignature(params,secret,exceptKey);
        Logger.v(TAG,"----->"+god);
        return sig.equals(god);
    }




}
