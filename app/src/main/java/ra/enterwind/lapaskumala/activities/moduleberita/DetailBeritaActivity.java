package ra.enterwind.lapaskumala.activities.moduleberita;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.adapter.CommentAdapter;
import ra.enterwind.lapaskumala.models.Comment;
import ra.enterwind.lapaskumala.utils.EndPoints;
import ra.enterwind.lapaskumala.utils.SessionManager;

public class DetailBeritaActivity extends AppCompatActivity {

    private static final String TAG = DetailBeritaActivity.class.getSimpleName();

    @BindView(R.id.getImage) ImageView ima;
    @BindView(R.id.getJudulBerita) TextView judul;
    @BindView(R.id.getIsiBerita) TextView isi;
    @BindView(R.id.getTanggal) TextView tanggal;
    @BindView(R.id.getImageUser) ImageView imageView;
    @BindView(R.id.resikelComment) RecyclerView recyclerView;
    @BindView(R.id.etIsi) EditText buat;

    SessionManager sessionManager;
    public CommentAdapter adapter;
    public List<Comment> commentList;

    String id, id_member, buat_isi;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_main_berita_detail);
        ButterKnife.bind(this);
        commentList = new ArrayList<>();

        initIntent();
        initUser();
        initComment();
    }

    private void initComment() {
        adapter = new CommentAdapter(this, commentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        fetch_comment();
    }

    private void fetch_comment() {
        ProgressDialog progressDialog = ProgressDialog.show(this, "", "");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(EndPoints.URL_DETAIL_BERITA + id + "/detail", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: "+ response);
                if (response.length() == 0){
                    progressDialog.dismiss();
                    Toast.makeText(DetailBeritaActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    commentList.clear();
                    for (int i=0; i<response.length(); i++){
                        try {
                            JSONObject object = response.getJSONObject(i);
                            final Comment comment = new Comment(
                                    object.getString("judul"),
                                    object.getString("isi"),
                                    object.getString("member"),
                                    object.getString("member_foto"),
                                    object.getString("waktu")
                            );
                            commentList.add(comment);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d(TAG, "onErrorResponse: "+ error);
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

    private void initUser() {
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> get = sessionManager.getUserDetail();
        Picasso.with(DetailBeritaActivity.this).load(get.get(SessionManager.KEY_FOTO)).fit().into(imageView);
        id_member = get.get(SessionManager.KEY_ID);
    }

    private void initIntent() {
        id = getIntent().getStringExtra("id");
        Picasso.with(DetailBeritaActivity.this).load(getIntent().getStringExtra("image")).fit().into(ima);
        judul.setText(getIntent().getStringExtra("judul"));
        isi.setText(getIntent().getStringExtra("kontent"));
        tanggal.setText(getIntent().getStringExtra("tanggal"));
    }

    @OnClick(R.id.btnSend) void onComment(){
        buat_isi = buat.getText().toString().trim();
        if (!buat_isi.isEmpty()){
            push();
        } else {
            Toast.makeText(this, "Tong ", Toast.LENGTH_SHORT).show();
        }
    }

    private void push() {
        String url = EndPoints.URL_COMMENT + id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+ response);
                if (response.equals("hangup")){
                    Toast.makeText(DetailBeritaActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    buat.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            overridePendingTransition(R.anim.fadein, R.anim.fade_out);
                            finish();
                            startActivity(getIntent());
                        }
                    }, 1000);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+ error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", id_member.trim());
                params.put("isi", buat_isi.trim());
                return  params;
            }
        };
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


    @OnClick(R.id.btnBack) void onBack(){
        finish();
    }
}
