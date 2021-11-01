package ra.enterwind.lapaskumala.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.activities.modulchat.ChatActivity;
import ra.enterwind.lapaskumala.models.Pengguna;
import ra.enterwind.lapaskumala.utils.SessionManager;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    public Activity mContext;
    public List<Pengguna> penggunaList;
    SessionManager sessionManager;
    String nama;

    public UserAdapter(Activity mContext, List<Pengguna> penggunaList){
        this.mContext = mContext;
        this.penggunaList = penggunaList;
        sessionManager = new SessionManager(mContext);
        HashMap<String, String> detail = sessionManager.getUserDetail();
        nama = detail.get(SessionManager.KEY_NAME);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Pengguna pengguna = penggunaList.get(position);
        if (pengguna.getNama().equals(nama)){
            holder.nama.setText("Me");
            Picasso.with(mContext).load(pengguna.getFoto()).fit().into(holder.foto);
        } else {
            holder.nama.setText(pengguna.getNama());
            Picasso.with(mContext).load(pengguna.getFoto()).fit().into(holder.foto);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent aa = new Intent(mContext, ChatActivity.class);
                    aa.putExtra("nama", pengguna.getNama());
                    aa.putExtra("foto", pengguna.getFoto());
                    aa.putExtra("one_id", pengguna.getOne_id());
                    aa.putExtra("pasang", pengguna.getPeer_id());
                    mContext.startActivity(aa);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return penggunaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.contactClick) RelativeLayout relativeLayout;
        @BindView(R.id.contactNama) TextView nama;
        @BindView(R.id.contactImage) ImageView foto;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
