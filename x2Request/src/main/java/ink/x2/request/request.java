package ink.x2.request;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Proxy;
import java.util.Set;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class

request  extends UniModule {
    OkHttpClient client = new OkHttpClient.Builder()
            .proxy(Proxy.NO_PROXY)
            .build();
    String url=null;
    String method=null;
    String data=null;
    JSONObject headers=null;
    @UniJSMethod(uiThread = false)
    public String requests(JSONObject options, UniJSCallback callback) throws IOException{
        if(callback!=null){
            try {
                if(options.containsKey("url")){
                    url=options.getString("url");
                }
                if(options.containsKey("headers")){
                    headers = JSONObject.parseObject(options.getString("headers"));
                }
                if(options.containsKey("data")){
                    data = options.getString("data");
                    Log.i(TAG,options.getString("data"));
                }
                JSONObject result=post();
                JSONObject text= (JSONObject) JSONObject.parseObject(result.getString("data")).getJSONArray("text").get(0);
                String fileId=text.getString("id");
                String passwordRes=getPassword(fileId);
                result.put("password",passwordRes);
                callback.invoke(result);
            }  catch (Exception e) {
            e.printStackTrace();
        }
        }
        return null;
    }
    public String getPassword(String  id) throws IOException {
        JSONObject obj=new JSONObject();
        Request.Builder builder = new Request.Builder();
        if(headers!=null){
            Set<String> headersKeys = headers.keySet();
            for (String key : headersKeys) {
                String value = headers.getString(key);
                builder.addHeader(key,value);
            }
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("task","22")
                .addFormDataPart("file_id",id)
                .build();
        Request request = builder.url("https://pc.woozooo.com/doupload.php").post(requestBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public JSONObject post() throws IOException {
        JSONObject obj=new JSONObject();
        Request.Builder builder = new Request.Builder();
        if(headers!=null){
            Set<String> headersKeys = headers.keySet();
            for (String key : headersKeys) {
                String value = headers.getString(key);
                builder.addHeader(key,value);
            }
        }
        JSONObject formData = JSONObject.parseObject(data);
        String filePath=formData.getString("upload_file");
        File file=new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] bytes = bos.toByteArray();
            bos.close();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("task", formData.getString("task"))
                    .addFormDataPart("folder_id_bb_n", formData.getString("folder_id_bb_n"))
                    .addFormDataPart("upload_file", file.getName(),
                            RequestBody.create(bytes))
                    .build();
        Request request = builder.url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()){
            obj.put("code",500);
            obj.put("msg","请求时出错");
        }else{
            obj.put("code",200);
            obj.put("msg","请求成功");
            obj.put("data",response.body().string());
        }
        return obj;
    }
}
