package id.cnn.cnnindonesiatv;

import android.content.Context;
import android.content.pm.PackageManager;

public class Utils {
    public static boolean hasPermission(final Context context, final String permission){
        return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(permission,context.getPackageName());
    }
}
