package com.android.server;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.IPackageNameService;
import android.os.RemoteException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class PackageNameService extends IPackageNameService.Stub {
    public static final String SERVICE_NAME = "packagename.service";

    private final Context context;

    public static void inject() {
        XposedBridge.hookAllMethods(XposedHelpers.findClass("android.app.ActivityThread", null),
                "systemMain",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();

                        XposedBridge.hookAllMethods(
                                XposedHelpers.findClass("com.android.server.am.ActivityManagerService", loader),
                                "setSystemProcess",
                                new XC_MethodHook() {
                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) {
                                        try {
                                            register(getContext(param.thisObject));
                                        } catch (Throwable ex) {
                                            XposedBridge.log("FakeGApps: Adding the package name service failed.");
                                        }
                                    }
                                }
                        );
                    }
                }
        );
    }

    private static void register(Context context) {
        try {
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
            Method addService = ServiceManager.getDeclaredMethod("addService", String.class, IBinder.class);
            addService.invoke(null, SERVICE_NAME, new PackageNameService(context));
        } catch (Throwable ex) {
            XposedBridge.log("FakeGApps: Adding the package name service failed.");
        }
    }

    private static Context getContext(Object object) throws NoSuchFieldException, IllegalAccessException {
        Field mContext = object.getClass().getDeclaredField("mContext");
        mContext.setAccessible(true);
        return (Context) mContext.get(object);
    }

    public PackageNameService(Context context) {
        this.context = context;
    }

    @Override
    public String getPackageName(int uid) throws RemoteException {
        String[] packages = context.getPackageManager().getPackagesForUid(uid);
        if (packages != null) {
            return packages[0];
        }
        return null;
    }
}
