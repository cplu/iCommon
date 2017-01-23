package com.windfindtech.icommon.util;

import com.windfindtech.icommon.iCommon;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Created by cplu on 2015/3/31.
 */

public class MD5 {
    private static HashMap<String, String> s_cached = new HashMap<String, String>();

    public static String getMD5(String val) {
        String result = s_cached.get(val);
        if(result != null){
            return result;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes(Charset.forName(iCommon.DEFAULT_NETWORK_ENCODING)));
            byte[] m = md5.digest();//加密
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : m) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            result = hexString.toString();
            s_cached.put(val, result);
            return result;
        }
        catch (Exception e){
            return "";
        }
    }

//    private static String getString(byte[] b){
//        StringBuffer sb = new StringBuffer();
//        for(int i = 0; i < b.length; i ++){
//            sb.append(b[i]);
//        }
//        return sb.toString();
//    }
//
//    public static void main(String[] args){
//
//    }
}
