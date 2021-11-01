package ra.enterwind.lapaskumala.activities.modulegaleri;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.adapter.GaleriAdapter;
import ra.enterwind.lapaskumala.models.Berita;
import ra.enterwind.lapaskumala.utils.EndPoints;

public class GaleriActivity extends AppCompatActivity {

    private String TAG = GaleriActivity.class.getSimpleName();
    private ArrayList<Berita> beritaArrayList;
    private ProgressDialog pDialog;
    private GaleriAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_main_galeri);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        beritaArrayList = new ArrayList<>();
        mAdapter = new GaleriAdapter(this, beritaArrayList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new GaleriAdapter.RecyclerTouchListener(this, recyclerView, new GaleriAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", beritaArrayList);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                GaleriFragment newFragment = GaleriFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
    }

    private void fetchImages() {
        pDialog.setMessage("Loading Images...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(EndPoints.URL_BERITA,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();

                        beritaArrayList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Berita image = new Berita();
                                image.setImage(object.getString("image"));
                                beritaArrayList.add(image);

                            } catch (JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    @OnClick(R.id.btnBack) void onBack(){
        finish();
    }

}
