package com.android.server;

import android.content.Context;
import android.os.IBinder;
import android.os.IPackageNameService;
import android.os.RemoteException;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class PackageNameService extends IPackageNameService.Stub {
    private final Context context;

    public static void inject() {
        XposedBridge.hookAllMethods(
            XposedHelpers.findClass("com.android.server.am.ActivityManagerService", null),
            "main",
            new XC_MethodHook() {
                @Override
                protected final void afterHookedMethod(final MethodHookParam param) {
                    Context context = (Context) param.getResult();
                    try {
                        Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
                        Method addService = ServiceManager.getDeclaredMethod("addService", String.class, IBinder.class);
                        addService.invoke(null, "packagename.service", new PackageNameService(context));
                    } catch (Throwable ex) {
                        XposedBridge.log("FakeGApps: Adding the package name service failed.");
                    }
                }
            }
        );
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
