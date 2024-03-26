package org.nonesec.Conatroller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

public class EncodeController {

    public String StringToBase64(String enText){
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encode = encoder.encode(enText.getBytes());
        return new String(encode);
    }


    public String StringToUnicode(String enText){
        StringBuilder stringBuilder = new StringBuilder();
        for (char c:enText.toCharArray()){
            stringBuilder.append("\\u").append(String.format("%04x",(int)c));
        }
        return stringBuilder.toString();
    }

    public String StringToURLCode(String enText) throws UnsupportedEncodingException {
        return URLEncoder.encode(enText,"UTF-8");
    }

}
