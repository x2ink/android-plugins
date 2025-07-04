package ink.x2.apkinfo;

import android.os.Build;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class GetApkInfo extends UniModule {
    @UniJSMethod(uiThread = true)
    public void get(JSONObject options, UniJSCallback callback) throws Exception {
        if(callback != null) {
            JSONObject data = new JSONObject();
            try {
                File file = new File(options.getString("path"));
                ApkFile apkFile = new ApkFile(file);
                ApkMeta apkMeta = apkFile.getApkMeta();
                data.put("ApkName",apkMeta.getLabel());
                if(getImgFileToBase642(apkMeta.getIcon(),options.getString("path"))=="失败"){
                    data.put("Icon",null);
                }else{
                    data.put("Icon","data:image/png;base64,"+getImgFileToBase642(apkMeta.getIcon(),options.getString("path")));
                }
                data.put("PackageName",apkMeta.getPackageName());
                data.put("VersionName",apkMeta.getVersionName());
                data.put("size",file.length());
                callback.invoke(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //  拷贝图标
    public static String getImgFileToBase642(String Icon, String Apk) throws Exception {
        ZipInputStream zin = null;
        Base64.Encoder encode = null;
        byte[] buffer = new byte[0];
        try {
            //  访问apk 里面的文件
            ZipFile zf = new ZipFile(Apk);
            InputStream in = new BufferedInputStream(new FileInputStream(Apk));
            zin = new ZipInputStream(in);
            ZipEntry ze;
            int temp=0;
            try {
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals(Icon)) {
                    temp++;
                    //  拷贝出图标
                    InputStream inStream = zf.getInputStream(ze);
                    //创建一个Buffer字符串
                    buffer = null;
                    int count = 0;
                    while (count == 0) {
                        count = inStream.available();
                    }
                    buffer = new byte[count];
                    inStream.read(buffer);
                    encode = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        encode = Base64.getEncoder();
                    }
                }
            }
            } catch (Exception e) {
                return "失败";
            }
            if(temp==0){
                return "失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zin.closeEntry();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return encode.encodeToString(buffer);
        }
        return null;
    }
}