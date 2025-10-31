package com.x2.phoneapplist;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class AppList extends UniModule {
    @UniJSMethod(uiThread = false)
    public void search(String value,UniJSCallback callback) throws Exception{
        if (mUniSDKInstance.getContext() != null) {
            PackageManager packageManager = getPackageManager();
            List<PackageInfo> list = packageManager.getInstalledPackages(0);
            int index=0;
            int count=0;
            for (PackageInfo p : list) {
                AppInfo appinfo = new AppInfo();
                PackageInfo info = packageManager.getPackageInfo(p.applicationInfo.packageName, PackageManager.GET_ACTIVITIES);
                appinfo.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
                appinfo.setPackage(p.applicationInfo.packageName);
                if(appinfo.getName().contains(value)||appinfo.getPackage().contains(value)){
                    count++;
                    appinfo.setFlags((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM));
                    appinfo.setIcon("data:image/png;base64,"+Bitmap2StrByBase64(drawableToBitmap(p.applicationInfo.loadIcon(packageManager))).replaceAll("\n",""));
                    appinfo.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
                    appinfo.setPackage(p.applicationInfo.packageName);
                    appinfo.setVersion(info.versionName);
                    appinfo.setSize(new File(info.applicationInfo.sourceDir).length());
                    appinfo.setFirstDate(info.firstInstallTime);
                    appinfo.setLastDate(info.lastUpdateTime);
                    appinfo.setPath("file://" + info.applicationInfo.sourceDir);
                    callback.invokeAndKeepAlive(appinfo);
                }
                if(index==list.size()-1&&count==0){
                    callback.invokeAndKeepAlive(null);
                }
                index++;
            }
        }
    }

    private PackageManager getPackageManager() {
        Activity activity = (Activity) mUniSDKInstance.getContext();
        PackageManager packageManager = activity.getPackageManager();
        return packageManager;
    }

    @UniJSMethod(uiThread = false)
    public void allapp(UniJSCallback callback) throws Exception {
        if (mUniSDKInstance.getContext() != null) {
            PackageManager packageManager = getPackageManager();
            List<PackageInfo> list = packageManager.getInstalledPackages(0);
            for (PackageInfo p : list) {
                AppInfo appinfo = new AppInfo();
                PackageInfo info = packageManager.getPackageInfo(p.applicationInfo.packageName, PackageManager.GET_ACTIVITIES);
                appinfo.setFlags((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM));
                appinfo.setIcon("data:image/png;base64,"+Bitmap2StrByBase64(drawableToBitmap(p.applicationInfo.loadIcon(packageManager))).replaceAll("\n",""));
                appinfo.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
                appinfo.setPackage(p.applicationInfo.packageName);
                appinfo.setVersion(info.versionName);
                appinfo.setSize(new File(info.applicationInfo.sourceDir).length());
                appinfo.setFirstDate(info.firstInstallTime);
                appinfo.setLastDate(info.lastUpdateTime);
                appinfo.setPath("file://" + info.applicationInfo.sourceDir);
                callback.invokeAndKeepAlive(appinfo);
            }
        }
    }
    @UniJSMethod(uiThread = false)
    public void userapp(UniJSCallback callback) throws Exception {
        if (mUniSDKInstance.getContext() != null) {
            PackageManager packageManager = getPackageManager();
            List<PackageInfo> list = packageManager.getInstalledPackages(0);
            for (PackageInfo p : list) {
                AppInfo appinfo = new AppInfo();
                PackageInfo info = packageManager.getPackageInfo(p.applicationInfo.packageName, PackageManager.GET_ACTIVITIES);
                if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                {
                    Log.i("APPName",packageManager.getApplicationLabel(p.applicationInfo).toString());
                    appinfo.setFlags((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM));
                    appinfo.setIcon("data:image/png;base64,"+Bitmap2StrByBase64(drawableToBitmap(p.applicationInfo.loadIcon(packageManager))).replaceAll("\n",""));
                    appinfo.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
                    appinfo.setPackage(p.applicationInfo.packageName);
                    appinfo.setVersion(info.versionName);
                    appinfo.setSize(new File(info.applicationInfo.sourceDir).length());
                    appinfo.setFirstDate(info.firstInstallTime);
                    appinfo.setLastDate(info.lastUpdateTime);
                    appinfo.setPath("file://" + info.applicationInfo.sourceDir);
                    callback.invokeAndKeepAlive(appinfo);
                }
            }
        }
    }
    static void go(Activity activity,Intent appIntent){
        try{
            activity.startActivity(appIntent);
        }catch (ActivityNotFoundException ex){
            ex.printStackTrace();
            Intent allFileIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            activity.startActivity(allFileIntent);
        }
    }
    static Bitmap drawableToBitmap(Drawable drawable)
    {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
    public String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
