package com.example.myesemtres;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class ESIMCheckerTwo {

    public static boolean isESIMSupported(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);

            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                // Verifica si hay al menos una tarjeta SIM, incluyendo eSIM
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                try {


                    //  SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfoList().get(1);

                    if (subscriptionManager.getActiveSubscriptionInfoCount() > 1) return true;
                    else return false;
                    //return subscriptionInfo.isEmbedded();
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.i("isESIMSupported", e.toString());
                }

            }
        }

        return false;
    }
}
