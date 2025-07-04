package ink.x2.applist;

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
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.common.UniModule;

public class AppList extends UniModule {
    List<AppInfo> Alllist = new ArrayList<>();
    List<AppInfo> Userlist = new ArrayList<>();
    @UniJSMethod(uiThread = false)
    public List<AppInfo> search(String value) throws Exception{
        if (mUniSDKInstance.getContext() != null) {
            Activity activity = (Activity) mUniSDKInstance.getContext();
            Intent appIntent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            appIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    go(activity,appIntent);
                }
            }
            PackageManager packageManager = activity.getPackageManager();
            List<PackageInfo> list = packageManager.getInstalledPackages(0);
            List<AppInfo> applist = new ArrayList<>();
            for (PackageInfo p : list) {
                AppInfo appinfo = new AppInfo();
                PackageInfo info = packageManager.getPackageInfo(p.applicationInfo.packageName, PackageManager.GET_ACTIVITIES);
                appinfo.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
                appinfo.setPackage(p.applicationInfo.packageName);
                if(appinfo.getName().contains(value)||appinfo.getPackage().contains(value)){
                    appinfo.setFlags((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM));
                    appinfo.setIcon("data:image/png;base64,"+Bitmap2StrByBase64(drawableToBitmap(p.applicationInfo.loadIcon(packageManager))).replaceAll("\n",""));
                    appinfo.setName(packageManager.getApplicationLabel(p.applicationInfo).toString());
                    appinfo.setPackage(p.applicationInfo.packageName);
                    appinfo.setVersion(info.versionName);
                    appinfo.setSize(new File(info.applicationInfo.sourceDir).length());
                    appinfo.setFirstDate(info.firstInstallTime);
                    appinfo.setLastDate(info.lastUpdateTime);
                    appinfo.setPath("file://" + info.applicationInfo.sourceDir);
                    applist.add(appinfo);
                }
            }
            return applist;
        }
        return null;
    }
    @UniJSMethod(uiThread = false)
    public List<AppInfo> getlist(String type){
        Log.i("type",type);
        if(type.equals("all")){
            List<AppInfo> list=Alllist;
            Alllist.clear();
            return list;
        }else{
            List<AppInfo> list=Userlist;
            Userlist.clear();
            return list;
        }
    }
    @UniJSMethod(uiThread = false)
    public List<AppInfo> allapp() throws Exception {
        if (mUniSDKInstance.getContext() != null) {
            Activity activity = (Activity) mUniSDKInstance.getContext();
            Intent appIntent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            appIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    go(activity,appIntent);
                }
            }
            PackageManager packageManager = activity.getPackageManager();
            List<PackageInfo> list = packageManager.getInstalledPackages(0);
            List<AppInfo> applist = new ArrayList<>();
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
                applist.add(appinfo);
                Alllist.add(appinfo);
            }
            return applist;
        }
        return null;
    }
    @UniJSMethod(uiThread = false)
    public List<AppInfo> userapp() throws Exception {
        if (mUniSDKInstance.getContext() != null) {
            Log.i("获取app","");
            Activity activity = (Activity) mUniSDKInstance.getContext();
            Intent appIntent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            appIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    go(activity,appIntent);
                }
            }
            PackageManager packageManager = activity.getPackageManager();
            List<PackageInfo> list = packageManager.getInstalledPackages(0);
            List<AppInfo> applist = new ArrayList<>();
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
                    applist.add(appinfo);
                    Userlist.add(appinfo);
                }
            }
            return applist;
        }
        return null;
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
