package ra.enterwind.lapaskumala.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.activities.modulchat.ChooseUser;
import ra.enterwind.lapaskumala.activities.moduleberita.BeritaActivity;
import ra.enterwind.lapaskumala.activities.modulegaleri.GaleriActivity;
import ra.enterwind.lapaskumala.utils.EndPoints;
import ra.enterwind.lapaskumala.utils.SessionManager;

public class HomeActivity extends BaseCallActivity {

    private Context mContext = HomeActivity.this;
    private static final String TAG = HomeActivity.class.getSimpleName();

    @BindView(R.id.txtWelcome) TextView welcome;
    @BindView(R.id.carouselView) CarouselView carouselView;

    ArrayList<String> judul = new ArrayList<String>();
    ArrayList<String> subjudul = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();


    SessionManager sessionManager;
    HashMap<String, String> HashMapForURL ;
    String nama;

    @Override
    public void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_main_home);
        ButterKnife.bind(this);

        Toast.makeText(mContext, ""+config().getUserId(), Toast.LENGTH_SHORT).show();
        initSession();
        welcomeMessage();
        initCarousel();
    }

    private void welcomeMessage() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String date = simpleDateFormat.format(calendar.getTime());
        int waktu = Integer.parseInt(date);

        if (waktu <= 11){
            welcome.setText("Selamat Pagi, " + nama + "!");
        } else if (waktu <= 15){
            welcome.setText("Selamat Siang, " + nama + "!");
        } else if (waktu <= 18){
            welcome.setText("Selamat Sore, " + nama + "!");
        } else if (waktu <= 23){
            welcome.setText("Selamat Malam, " + nama + "!");
        }
    }

    private void initSession() {
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> detail = sessionManager.getUserDetail();
        nama = detail.get(SessionManager.KEY_NAME);
    }

    private void initCarousel() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                EndPoints.URL_BERITA, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: " + response);
                        if(response.length() > 0) {
                            for(int i = 0; i<response.length(); i++) {
                                JSONObject json = null;
                                try {
                                    json = response.getJSONObject(i);
                                    judul.add(json.getString("judul"));
                                    subjudul.add(json.getString("judul"));
                                    image.add(json.getString("image"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            carouselView.setViewListener(viewListener);
                            carouselView.setPageCount(judul.toArray().length);
                            carouselView.setSlideInterval(4000);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error);
                        Toast.makeText(mContext, "Terjadi kesalahan pada server.", Toast.LENGTH_SHORT).show();
                    }
                });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {return 50000;}
            @Override
            public int getCurrentRetryCount() {return 50000;}
            @Override
            public void retry(VolleyError error) throws VolleyError {}
        });
        Volley.newRequestQueue(this).add(jsonObjReq);
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_slider, null);

            TextView tvJudul = (TextView) customView.findViewById(R.id.tvJudul);
            TextView tvDesc = (TextView) customView.findViewById(R.id.tvDesc);
            ImageView ivPhoto = (ImageView) customView.findViewById(R.id.ivPhoto);

            Picasso.with(mContext).load(image.get(position)).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(ivPhoto);
            tvJudul.setText(judul.get(position));
            tvDesc.setText(subjudul.get(position));

            return customView;
        }
    };

    @OnClick(R.id.cardBerita) void onBerita(){
        startActivity(new Intent(HomeActivity.this, BeritaActivity.class));
    }

    @OnClick(R.id.cardThumbnail) void onGaleri(){
        startActivity(new Intent(HomeActivity.this, GaleriActivity.class));
    }

    @OnClick(R.id.btnChat) void onChat(){
        startActivity(new Intent(HomeActivity.this, ChooseUser.class));
    }


}
