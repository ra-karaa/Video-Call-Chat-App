package ra.enterwind.lapaskumala.activities.moduleberita;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.adapter.BeritaAdapter;
import ra.enterwind.lapaskumala.models.Berita;
import ra.enterwind.lapaskumala.utils.EndPoints;

public class BeritaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = BeritaActivity.class.getSimpleName();
    private Context mContext = BeritaActivity.this;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.kosong) LinearLayout kosong;
    @BindView(R.id.shimmer_view_container) ShimmerFrameLayout shimmer;

    public List<Berita> beritaList;
    public BeritaAdapter adapter;
    SweetAlertDialog dialog;


    @Override
    protected void onCreate(Bundle tes) {
        super.onCreate(tes);
        setContentView(R.layout.activity_main_berita);
        ButterKnife.bind(this);

        beritaList = new ArrayList<>();
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setRefreshing(true);
        init();
    }

    private void init() {
        adapter = new BeritaAdapter(this, beritaList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fetchBerita();
    }

    private void fetchBerita() {
        shimmer.startShimmer();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EndPoints.URL_BERITA, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: "+ response);
                if (response.length() == 0){
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    kosong.setVisibility(View.VISIBLE);
                    swipeRefresh.setRefreshing(false);
                } else {
                    beritaList.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            Berita berita = new Berita(
                                    object.getString("id"),
                                    object.getString("judul"),
                                    object.getString("kontent"),
                                    object.getString("image"),
                                    object.getString("tanggal"),
                                    object.getString("komen")
                            );
                            beritaList.add(berita);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.INVISIBLE);
                    swipeRefresh.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+ error);
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Kesalahan Teknis!")
                        .setContentText("Mohon hubungi tim teknis kami.")
                        .show();
                shimmer.stopShimmer();
                shimmer.setVisibility(View.INVISIBLE);
                swipeRefresh.setRefreshing(false);
            }
        });
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                finish();
                startActivity(getIntent());
                overridePendingTransition(R.anim.fadein, R.anim.fade_out);
            }
        }, 1000);
    }

    @OnClick(R.id.btnBack) void onBack(){
        finish();
    }
}
