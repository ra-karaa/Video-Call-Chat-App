package ra.enterwind.lapaskumala.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.activities.BaseCallActivity;
import ra.enterwind.lapaskumala.activities.HomeActivity;
import ra.enterwind.lapaskumala.activities.moduleberita.BeritaActivity;
import ra.enterwind.lapaskumala.agora.Config;
import ra.enterwind.lapaskumala.utils.EndPoints;
import ra.enterwind.lapaskumala.utils.SessionManager;

public class LoginActivity extends BaseCallActivity {

    private Context mContext = LoginActivity.this;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.etUsername) EditText username;
    @BindView(R.id.etPassword) EditText password;
    String user, pass;
    SweetAlertDialog dialog;
    SessionManager sessionManager;
    String one_id, peer_id;

    @Override
    public void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_auth_login);
        ButterKnife.bind(this);
        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Toast.makeText(mContext, "Berapa " + userId, Toast.LENGTH_SHORT).show();
                one_id = userId;
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);

        setIdentifier();

        sessionManager = new SessionManager(getApplicationContext());
    }

    private void setIdentifier() {
        Toast.makeText(mContext, ""+config().getUserId(), Toast.LENGTH_SHORT).show();
        peer_id = config().getUserId();
    }

    @OnClick(R.id.btnMasuk) void onMasuk(){
        loading();
        user = username.getText().toString().trim();
        pass = password.getText().toString().trim();
        if(!user.isEmpty() && !pass.isEmpty()){
            cekUser(user, pass);
        } else {
            dialog.dismissWithAnimation();
            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Galat!")
                    .setContentText("Harap lengkapi seluruh inputan!")
                    .show();
        }

    }

    private void cekUser(String user, String pass) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_LOGIN + user + "/" + pass + "/" + one_id + "/" + peer_id , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.equals("gagal")){
                    dialog.dismissWithAnimation();
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Username dan Password Salah!")
                            .show();
                } else {
                    String[] list = response.split(",");
                    SharedPreferences sharedPreferences = getSharedPreferences(SessionManager.DATASTREAMPREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(SessionManager.DATASTREAMID, list[1]);
                    editor.apply();
                    switch (list[1]){
                        case "2":
                            sessionManager.createLogin(list[0], list[1], list[2], list[3], list[4], list[5], list[6], list[7], list[8]);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                            break;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+ error);
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Galat!")
                        .setContentText("Mohon Hubungi Tim Teknis!")
                        .show();
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loading() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();

    }

    @OnClick(R.id.textRegister) void onDaftar(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

}
