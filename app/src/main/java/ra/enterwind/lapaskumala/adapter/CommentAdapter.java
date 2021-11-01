package ra.enterwind.lapaskumala.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    public List<Comment> commentListl;
    public Activity mContext;

    public CommentAdapter(Activity mContext, List<Comment>commentList){
        this.mContext = mContext;
        this.commentListl = commentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_comment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Comment comment = commentListl.get(position);
        holder.nama.setText(comment.getMember());
        holder.isi.setText(comment.getIsi());
        holder.waktu.setText(comment.getWaktu());
        Picasso.with(mContext).load(comment.getMember_foto()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return commentListl.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.getMemberNama) TextView nama;
        @BindView(R.id.getImageMember) ImageView imageView;
        @BindView(R.id.getIsiComment) TextView isi;
        @BindView(R.id.getWaktu) TextView waktu;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
