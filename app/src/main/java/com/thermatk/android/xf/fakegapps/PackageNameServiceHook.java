package com.thermatk.android.xf.fakegapps;

import de.robv.android.xposed.IXposedHookZygoteInit;
import com.android.server.PackageNameService;

public final class PackageNameServiceHook implements IXposedHookZygoteInit {
    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        PackageNameService.inject();
    }
}
