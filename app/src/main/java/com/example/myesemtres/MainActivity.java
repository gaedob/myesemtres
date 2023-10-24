package com.example.myesemtres;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.accessibilityservice.AccessibilityService;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;
import android.telephony.euicc.EuiccManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.provider.Settings.Secure;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //private AccessibilityService context;
    public static final String TAG = "MainActivity";

    private final int REQUEST_PERMISSION_PHONE_STATE=1;
    //private TextView texto;
    //EuiccManager mgr = (EuiccManager) context.getSystemService(Context.EUICC_SERVICE);
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button boton = (Button) findViewById(R.id.button2);
        setIdetificador(this);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarMensaje(v);
                showPhoneStatePermission();
                // Intent i = new Intent(MainActivity.this, Ventana2.class);
                // startActivity(i);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    private void showPhoneStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
           // int REQUEST_PERMISSION_PHONE_STATE = 1;
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_PHONE_STATE, REQUEST_PERMISSION_PHONE_STATE);
            } else {
                requestPermission(Manifest.permission.READ_PHONE_STATE, REQUEST_PERMISSION_PHONE_STATE);
            }
        } else {
            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }
    public void setIdetificador(Context context) {
        TextView texto = (TextView) findViewById(R.id.IdDispositivo);
        String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        Log.i(TAG, "androidId: " + androidId);
        texto.setText(androidId);
    }

    public void cambiarMensaje(View v) {
        TextView texto = (TextView) findViewById(R.id.textView);

        boolean esimSupported = false;

        if (isOnline(this)) {
            texto.setText("está habilitado");
        } else {
            texto.setText("No está habilitado");
        }

       //esimSupported =  ESIMCheckerTwo.isESIMSupported(this);

        ESIMChecker eSIMChecker;
        eSIMChecker = new ESIMChecker(this);

        eSIMChecker.checkESIMSupport();
        if (esimSupported){
            Log.i(TAG, "androidId: ESIMCheckerTwo Soportado");
            texto.setText("Está Soportado");
        }else {
            Log.i(TAG, "androidId: ESIMCheckerTwo Noooooooooo");
            texto.setText("No Soportado");
        }

        SubscriptionManager subscriptionManager = SubscriptionManager.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           // texto.setText("return ESIM subscriptionManager");
            Log.i(TAG, "androidId: ESIMCheckerTwo Noooooooooo");
            return;
        }
        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        if (subscriptionInfoList != null) {
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                // Verifica si la suscripción es una eSIM
                if (subscriptionInfo.isEmbedded()) {
                    // Esta suscripción es una eSIM
                    int subscriptionId = subscriptionInfo.getSubscriptionId();
                    // Realiza operaciones con la eSIM, como activación o desactivación
                }
            }
        }




    }

    public boolean isOnline(Context context) {
        Log.i("isOnline", "isOnline");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            Log.i("isOnline", "mgr nulo");

            if (context != null) {
                try {
                    Log.i("isOnline", context.getOpPackageName());
                    EuiccManager mgr = (EuiccManager) context.getSystemService(Context.EUICC_SERVICE);
                    if (mgr == null) {
                        Log.i("isOnline", "mgr nulo");
                        return false;
                    } else {
                        Log.i("isOnline", "No nulo");
                    }

                    Log.i("isOnline", "No nulo");
                    if (!mgr.isEnabled()) {
                        Log.i("isOnline", "Está habilitadooooo");
                        return false;
                    } else {
                        EuiccInfo info = mgr.getEuiccInfo();
                        String osVer = info.getOsVersion();
                     /*  String eid = mgr.getEid();

                        if (eid == null)
                            return false;

                        Log.i( "isOnline", eid);*/


                        Log.i("isOnline", osVer);
                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("isOnline", e.toString());
                }
            }
        }
        return false;
    }

    public boolean isESIMSupported(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // Verificar si el dispositivo es compatible con eSIM
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
        if ((telephonyManager != null) && (telephonyManager.
                MULTISIM_ALLOWED == telephonyManager.isMultiSimSupported())) {
            // Verificar si hay al menos una eSIM activa
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (subscriptionManager != null) {
                SubscriptionInfo activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1);//SubscriptionManager.SIM_SLOT_INDEX_EMBEDDED);
                return activeSubscriptionInfo != null;
            }
        }

        return false;
    }

    private DownloadableSubscription createDownloadableSubscription() {
        // Crea y devuelve un objeto DownloadableSubscription con la información del perfil de eSIM
        // proporcionado por el operador.
        // Aquí deberías implementar la lógica para obtener la información del perfil de eSIM.
        // Consulta la documentación del operador para obtener detalles sobre el formato de la información del perfil.
        // Ejemplo:
        // DownloadableSubscription subscription = new DownloadableSubscription.Builder()
        //         .setConfirmationCode("123456")
        //         .setCarrierName("Operador de Telefonía")
        //         .setAccessRules(AccessRules.ACCESSRULE_DEFAULT)
        //         .build();
        // return subscription;
        return null; // Devuelve el objeto DownloadableSubscription con la información del perfil de eSIM.
    }

}

