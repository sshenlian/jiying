package org.nonesec.Conatroller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

public class DecodeController{

    public String Base64ToSting(String enText){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(enText.getBytes());
        return new String(decode);
    }

    public String UnicodeToString(String enText){
        StringBuilder stringBuilder = new StringBuilder();
        String[] split = enText.toString().split("\\\\u");
        for (int i = 1; i < split.length; i++) {
            int i1 = Integer.parseInt(split[i], 16);
            stringBuilder.append((char) i1);
        }
        return  stringBuilder.toString();
    }

    public String URLCodeToString(String enText) throws UnsupportedEncodingException {
        return URLDecoder.decode(enText,"UTF-8");
    }


}
