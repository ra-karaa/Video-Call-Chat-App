package ra.enterwind.lapaskumala.activities.modulchat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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
import cn.pedant.SweetAlert.SweetAlertDialog;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.adapter.UserAdapter;
import ra.enterwind.lapaskumala.models.Pengguna;
import ra.enterwind.lapaskumala.utils.EndPoints;

public class ChooseUser extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = ChooseUser.class.getSimpleName();
    private Context mContext = ChooseUser.this;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.shimmer_view_container) ShimmerFrameLayout shimmer;

    public UserAdapter adapter;
    public List<Pengguna> penggunas;
    SweetAlertDialog dialog;


    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_main_chat_user);
        ButterKnife.bind(this);

        penggunas = new ArrayList<>();
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setOnRefreshListener(this);
        init();
    }

    private void init() {
        adapter = new UserAdapter(this, penggunas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fetch();
    }

    private void fetch() {
        shimmer.startShimmer();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EndPoints.URL_MEMBER, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: "+ response);
                if (response.length() == 0){
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
                } else {
                    penggunas.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            final Pengguna pengguna = new Pengguna(
                                    object.getString("id"),
                                    object.getString("nama"),
                                    object.getString("foto"),
                                    object.getString("username"),
                                    object.getString("plain"),
                                    object.getString("one_id"),
                                    object.getString("peer_id")
                            );
                            penggunas.add(pengguna);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
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
                shimmer.setVisibility(View.GONE);
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
}
