package ra.enterwind.lapaskumala;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import ra.enterwind.lapaskumala.activities.MainActivity;

public class NotifikasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new FirebaseNotificationOpenedHandler(this))
                .init();
        setContentView(R.layout.activity_notifikasi);

    }

    public class FirebaseNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        Context ctx;
        FirebaseNotificationOpenedHandler(Context context) {
            ctx = context;
        }
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            Toast.makeText(ctx, "Halo, saya klik notifikasi ya", Toast.LENGTH_SHORT).show();
            if (data != null) {
                String customKey = data.optString("customkey", null);
                String lagikey = data.optString("lagikey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
                if (lagikey != null)
                    Log.i("OneSignalExample", "lagikey set with value: " + lagikey);
            }
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

}
