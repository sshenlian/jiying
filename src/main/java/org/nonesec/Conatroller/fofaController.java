package org.nonesec.Conatroller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

public class fofaController {
    public String fofaUser,fofaAPIKEY;

    public void getConfig() throws IOException {
        File file = new File("config.txt");
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            this.fofaUser = reader.readLine().replace("fofaUser = ","");
            this.fofaAPIKEY = reader.readLine().replace("fofaAPIKEY = ","");
        }
    }

    public String[][] getSearchResults(String Url) {
        try{
        URL url = new URL(Url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        // 获取响应状态码
        int responseCode = urlConnection.getResponseCode();
        System.out.println(urlConnection);
        System.out.println(responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 读取响应内容
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String collect = in.lines().collect(Collectors.joining());
                JSONObject parse = (JSONObject) JSON.parse(collect);
                String results = parse.getString("results");
                JSONArray jsonArray = JSON.parseArray(results);
                String[][] urlsArray = (String[][]) jsonArray.stream()
                        .map(obj -> ((JSONArray) obj).stream().map(Object::toString).toArray(String[]::new))
                        .toArray(String[][]::new);
                System.out.println(Arrays.deepToString(urlsArray));
                return urlsArray;
        }

        }catch (Exception e){
            System.out.println(e);
        }
        return new String[0][];
    }


}
