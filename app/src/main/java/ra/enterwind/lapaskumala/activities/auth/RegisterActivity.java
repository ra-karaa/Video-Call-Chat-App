package ra.enterwind.lapaskumala.activities.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ra.enterwind.lapaskumala.R;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etNamaRegister) EditText nama;
    @BindView(R.id.etUsernameRegister) EditText username;
    @BindView(R.id.etPasswordRegister) EditText password;

    String na, use, pas;
    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_auth_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnNext) void onLanjut(){
        loading();
        na = nama.getText().toString().trim();
        use = username.getText().toString().trim();
        pas = password.getText().toString().trim();
        if ((!na.isEmpty() && !use.isEmpty() && !pas.isEmpty())){
            ambilFoto();
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Galat!")
                    .setContentText("Harap lengkapi seluruh inputan!")
                    .show();
        }
    }

    private void loading() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void ambilFoto() {
        dialog.dismissWithAnimation();
        Intent aa = new Intent(RegisterActivity.this, RegisterFotoActivity.class);
        aa.putExtra("nama", na);
        aa.putExtra("username", use);
        aa.putExtra("pass", pas);
        startActivity(aa);
    }

    @OnClick(R.id.textLogin) void onLogin(){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
