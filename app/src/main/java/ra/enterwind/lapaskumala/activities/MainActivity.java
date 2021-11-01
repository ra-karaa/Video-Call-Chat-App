package ra.enterwind.lapaskumala.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.onesignal.OneSignal;

import java.util.HashMap;

import ra.enterwind.lapaskumala.NotifikasiActivity;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.activities.moduleberita.BeritaActivity;
import ra.enterwind.lapaskumala.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {

            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);
        getSession();
    }

    private void getSession() {
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.chekLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        akses = user.get(SessionManager.KEY_AKSES);

        if (sessionManager.isLoggedIn()){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }
}
