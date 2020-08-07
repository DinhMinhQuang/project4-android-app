package fpt.aptech.project4_android_app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class Notification extends Application {
    public static final String CHANNEL_CANCEL = "cancel";
    public static final String ACCEPTED_ORDER = "accept";
    public static final String DELIVERY_ORDER = "delivery";
    public static final String COMPLETED_ORDER = "delivery";
    public static final String CHANNEL_CANCEL_BY_USER = "cancel_byUser";
    public static final String BACK_TO_YOUR_ORDER = "backToYourOrder";

    @Override
    public void onCreate() {
        super.onCreate();
        createNoti();
    }
    private void createNoti(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel CancelChanel = new NotificationChannel(
                    CHANNEL_CANCEL,
                    "Cancel Chanel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationChannel AcceptChanel = new NotificationChannel(
                    ACCEPTED_ORDER,
                    "Accept Chanel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationChannel DeliveryChanel = new NotificationChannel(
                    DELIVERY_ORDER,
                    "Delivery Chanel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationChannel CompleteChanel = new NotificationChannel(
                    COMPLETED_ORDER,
                    "Complete Chanel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationChannel CancelByUser = new NotificationChannel(
                    CHANNEL_CANCEL_BY_USER,
                    "Cancel By User",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationChannel BackToYourOrder = new NotificationChannel(
                    BACK_TO_YOUR_ORDER,
                    "Back Yo Your Order",
                    NotificationManager.IMPORTANCE_HIGH
            );
            AcceptChanel.setDescription("Food Tap Delivery");
            CompleteChanel.setDescription("Food Tap Delivery");
            DeliveryChanel.setDescription("Food Tap Delivery");
            CancelChanel.setDescription("Food Tap Delivery");
            CancelByUser.setDescription("Food Tap Delivery");
            BackToYourOrder.setDescription("Food Tap Delivery");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(AcceptChanel);
            manager.createNotificationChannel(CompleteChanel);
            manager.createNotificationChannel(DeliveryChanel);
            manager.createNotificationChannel(CancelChanel);
            manager.createNotificationChannel(CancelByUser);
            manager.createNotificationChannel(BackToYourOrder);
        }
    }

}
