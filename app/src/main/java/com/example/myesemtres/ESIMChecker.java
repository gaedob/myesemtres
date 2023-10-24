package com.example.myesemtres;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class ESIMChecker {
    public static SubscriptionManager subscriptionManager;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ESIMChecker.context = context;
    }

    public static  Context context;
    // Register receiver.
    static final String ACTION_SWITCH_TO_SUBSCRIPTION = "switch_to_subscription";
    static final String LPA_DECLARED_PERMISSION
            = "com.your.company.lpa.permission.BROADCAST";
    public ESIMChecker(Context context){
        setContext(context);
        subscriptionManager = SubscriptionManager.from(context);
    }


    public static void checkESIMSupport() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (telephonyManager.isMultiSimSupported() == telephonyManager.
                        MULTISIM_ALLOWED) {
                    //SubscriptionManager subscriptionManager;
                    //subscriptionManager = SubscriptionManager.from(context);
                    SubscriptionInfo eSimInfo = null;

                    // Obtén información sobre todos los perfiles del dispositivo
                    if ((subscriptionManager != null) && (subscriptionManager.getActiveSubscriptionInfoList() != null)) {

                        List<SubscriptionInfo> subscriptionInfos = subscriptionManager.getActiveSubscriptionInfoList();  //toArray(new SubscriptionInfo[]{});//.toArray(new SubscriptionInfo[1]);

                        for (SubscriptionInfo info : subscriptionInfos) {
                            if (info.getSimSlotIndex() == 1) { //subscriptionManager.SIM_SLOT_INDEX_EMBEDDED) {
                                eSimInfo = info;
                                break;
                            }
                        }
                    }

                    if (eSimInfo != null) {
                        // El dispositivo es compatible con eSIM y tiene un perfil eSIM activo.
                        Toast.makeText(context, "El dispositivo admite eSIM y tiene un perfil eSIM activo.", Toast.LENGTH_SHORT).show();
                    } else {
                        // El dispositivo es compatible con eSIM, pero no tiene un perfil eSIM activo.
                        Toast.makeText(context, "El dispositivo admite eSIM, pero no tiene un perfil eSIM activo.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    // El dispositivo no admite múltiples tarjetas SIM, lo que significa que no admite eSIM.
                    Toast.makeText(context, "El dispositivo no admite eSIM.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void activarESIM(String codigoQR) {
     //   SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        /*if (subscriptionmanager.getactivesubscriptioninfolist() != null) {
            for (subscriptioninfo subscriptioninfo : subscriptionmanager.getactivesubscriptioninfolist()) {
                if (subscriptioninfo.isembedded()) {
                    // iniciar el proceso de activación de esim
                    subscriptionmanager.downloadsubscription(subscriptioninfo.getsubscriptionid(), codigoqr,
                            new subscriptionmanager.ondownloadsubscriptioncallback() {
                                @override
                                public void oncomplete(int result) {
                                    // proceso de activación completado
                                    if (result == subscriptionmanager.success) {
                                        // activación exitosa
                                    } else {
                                        // activación fallida
                                    }
                                }
                            });
                }
            }
        }*/
    }

}



