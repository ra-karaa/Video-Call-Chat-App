package ra.enterwind.lapaskumala.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.activities.moduleberita.DetailBeritaActivity;
import ra.enterwind.lapaskumala.models.Berita;

public class BeritaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public Activity mContext;
    public List<Berita> beritaList;

    public BeritaAdapter(Activity mContext, List<Berita>beritaList){
        this.mContext = mContext;
        this.beritaList = beritaList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (beritaList.get(position).getId()){
            case "1":
                return 0;

            default:
                return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view1 = layoutInflater.inflate(R.layout.list_berita, parent, false);
        View view2 = layoutInflater.inflate(R.layout.list_berita_child, parent, false);

        switch (viewType){
            case 0:
                return new MyViewHolder(view1);
            case 1:
                return new ViewHolderOne(view2);
                default:
                    return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Berita berita = beritaList.get(position);
        switch (holder.getItemViewType()){
            case 0:
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.judul.setText(berita.getJudul());
            viewHolder.tangga.setText(berita.getTanggal());
            viewHolder.count.setText(berita.getKomen());
            Picasso.with(mContext).load(berita.getImage()).fit().into(viewHolder.foto);
            viewHolder.berita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent aa = new Intent(mContext, DetailBeritaActivity.class);
                    aa.putExtra("id", berita.getId());
                    aa.putExtra("judul", berita.getJudul());
                    aa.putExtra("kontent", berita.getKontent());
                    aa.putExtra("image", berita.getImage());
                    aa.putExtra("tanggal", berita.getTanggal());
                    mContext.startActivity(aa);
                }
            });
            break;

            case 1:
                ViewHolderOne holderOne = (ViewHolderOne) holder;
                holderOne.judul.setText(berita.getJudul());
                holderOne.tangga.setText(berita.getTanggal());
                holderOne.count.setText(berita.getKomen());
                Picasso.with(mContext).load(berita.getImage()).fit().into(holderOne.foto);
                holderOne.berita.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent aa = new Intent(mContext, DetailBeritaActivity.class);
                        aa.putExtra("id", berita.getId());
                        aa.putExtra("judul", berita.getJudul());
                        aa.putExtra("kontent", berita.getKontent());
                        aa.putExtra("image", berita.getImage());
                        aa.putExtra("tanggal", berita.getTanggal());
                        mContext.startActivity(aa);
                    }
                });
                break;
        }
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        @BindView(R.id.getJudulBerita) TextView judul;
        @BindView(R.id.getTanggalBerita) TextView tangga;
        @BindView(R.id.getFotoBerita) ImageView foto;
        @BindView(R.id.cardBerita) CardView berita;
        @BindView(R.id.getCount) TextView count;
        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.getJudulBerita) TextView judul;
        @BindView(R.id.getTanggalBerita) TextView tangga;
        @BindView(R.id.getFotoBerita) ImageView foto;
        @BindView(R.id.cardBerita) CardView berita;
        @BindView(R.id.getCount) TextView count;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
