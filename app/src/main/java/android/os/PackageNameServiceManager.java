package android.os;

import java.lang.reflect.Method;
import de.robv.android.xposed.XposedBridge;

public final class PackageNameServiceManager {
    private static PackageNameServiceManager manager;

    private IPackageNameService service;

    public static PackageNameServiceManager getService() {
        if (manager == null) {
            manager = new PackageNameServiceManager();
        }

        return manager;
    }

    private PackageNameServiceManager() {
        try {
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getDeclaredMethod("getService", String.class);
            service = IPackageNameService.Stub.asInterface((IBinder) getService.invoke(null, "packagename.service"));
        } catch (Throwable ex) {
            XposedBridge.log("FakeGApps: Unable to get a reference to the package name service");
        }
    }

    public String getPackageName(int uid) {
        try {
            return service.getPackageName(uid);
        } catch (RemoteException e) { e.printStackTrace(); }

        return null;
    }
}