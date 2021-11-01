package ra.enterwind.lapaskumala.activities.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.utils.EndPoints;

public class RegisterFotoActivity extends AppCompatActivity {

    private static final String TAG = RegisterFotoActivity.class.getSimpleName();
    Context mContext = RegisterFotoActivity.this;

    @BindView(R.id.camera) CameraView cameraKitView;
    @BindView(R.id.photoFrame) ImageView photoFrame;
    @BindView(R.id.shutterButton) ImageView shutterButton;
    @BindView(R.id.galeriButton) ImageView galeriButton;
    @BindView(R.id.shutterButtonOk) ImageView shutterButtonOk;
    @BindView(R.id.captureButton) ImageView captureButton;

    public static final int IMAGE_GALLERY_REQUEST = 20;
    Bitmap bitmap;
    SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_auth_register_foto);
        ButterKnife.bind(this);
        initDialog();
        cameraKitView.addCameraKitListener(cameraListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle InstanceState) {
        super.onSaveInstanceState(InstanceState);
        InstanceState.clear();
    }

    private void initDialog() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Tunggu Sebentar...");
        dialog.setCancelable(false);
    }


    private CameraKitEventListener cameraListener = new CameraKitEventListener() {
        @Override
        public void onEvent(CameraKitEvent cameraKitEvent) {
            Log.d(TAG, "onEvent: " + cameraKitEvent);
        }

        @Override
        public void onError(CameraKitError cameraKitError) {
            Log.d(TAG, "onError: " + cameraKitError);
        }

        @Override
        public void onImage(CameraKitImage cameraKitImage) {

            byte[] jpeg = cameraKitImage.getJpeg();
            bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);

            shutterButton.setVisibility(View.GONE);
            galeriButton.setVisibility(View.GONE);
            shutterButtonOk.setVisibility(View.VISIBLE);
            captureButton.setVisibility(View.VISIBLE);

            cameraKitView.setVisibility(View.GONE);
            photoFrame.setVisibility(View.VISIBLE);
            photoFrame.setImageBitmap(bitmap);
        }

        @Override
        public void onVideo(CameraKitVideo cameraKitVideo) {
            Log.d(TAG, "cameraKitVideo");
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.start();
    }

    @Override
    protected void onPause() {
        cameraKitView.stop();
        super.onPause();
    }

    @OnClick(R.id.shutterButton) void capturePhoto() {
        cameraKitView.captureImage();
    }

    @OnClick(R.id.captureButton) void capturePhotoAgain() {
        shutterButton.setVisibility(View.VISIBLE);
        galeriButton.setVisibility(View.VISIBLE);
        shutterButtonOk.setVisibility(View.GONE);
        captureButton.setVisibility(View.GONE);

        cameraKitView.setVisibility(View.VISIBLE);
        photoFrame.setVisibility(View.GONE);
    }

    @OnClick(R.id.galeriButton) void openGaleri() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_GALLERY_REQUEST) {
            Uri imageUri = data.getData();
            InputStream inputStream;

            try {
                inputStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);

                shutterButton.setVisibility(View.GONE);
                galeriButton.setVisibility(View.GONE);
                shutterButtonOk.setVisibility(View.VISIBLE);
                captureButton.setVisibility(View.VISIBLE);

                cameraKitView.setVisibility(View.GONE);
                photoFrame.setVisibility(View.VISIBLE);

                photoFrame.setImageBitmap(bitmap);

                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.shutterButtonOk) void nextStep() {
        dialog.show();
        String url = EndPoints.URL_REGIS;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                if (response.equals("success")){
                    dialog.dismiss();
                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Berhasil!")
                            .setContentText("Berhasil Buat Akun.")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(RegisterFotoActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }).show();
                } else if(response.equals("registered")) {
                    dialog.dismiss();
                    Log.d(TAG, "response" + response.toString());
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Maaf, NIK atau Username sudah digunakan!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    finish();
                                }
                            })
                            .show();
                }
                else {
                    dialog.dismiss();
                    Log.d(TAG, "response" + response.toString());
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Galat!")
                            .setContentText("Maaf, NIK atau Username sudah digunakan!")
                            .show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d(TAG, "response" + error.toString());
                if (error instanceof NoConnectionError){
                    Toast.makeText(mContext, "Sakit ", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("nik", "12345");
                params.put("nama", getIntent().getStringExtra("nama"));
                params.put("phone", "0123456");
                params.put("alamat", "solong durian");
                params.put("username", getIntent().getStringExtra("username"));
                params.put("password", getIntent().getStringExtra("pass"));
                params.put("foto", bitmap == null ? "null" : getStringImage(bitmap));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 500000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 500000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
