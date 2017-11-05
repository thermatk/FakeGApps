package com.thermatk.android.xf.fakegapps;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Binder;
import android.os.Build;
import android.os.PackageNameServiceManager;
import android.util.Log;

import com.android.server.PackageNameService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public final class NonSystemLocationProviders implements IXposedHookZygoteInit {
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("android.app.ActivityThread", null),
                    "systemMain",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            ClassLoader loader = Thread.currentThread().getContextClassLoader();
                            register(loader);
                        }
                    }
            );
        }
    }

    private static void register(ClassLoader loader) {
        final Class<?> serviceWatcher = XposedHelpers.findClass("com.android.server.ServiceWatcher", loader);
        XposedBridge.hookAllMethods(serviceWatcher, "getSignatureSets", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.args[0];
                List<String> initialPackageNames = (List<String>) param.args[1];
                PackageManager pm = context.getPackageManager();
                ArrayList<HashSet<Signature>> sigSets = new ArrayList<>();
                for (int i = 0, size = initialPackageNames.size(); i < size; i++) {
                    String pkg = initialPackageNames.get(i);
                    try {
                        HashSet<Signature> set = new HashSet<>();
                        Signature[] sigs = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES).signatures;
                        set.addAll(Arrays.asList(sigs));
                        sigSets.add(set);
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.w("ServiceWatcher", pkg + " not found");
                    }
                }
                param.setResult(sigSets);
            }
        });
    }
}
