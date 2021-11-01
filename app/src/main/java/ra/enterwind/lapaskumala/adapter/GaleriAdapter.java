package ra.enterwind.lapaskumala.adapter;

import android.app.Activity;
import android.media.Image;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.models.Berita;

public class GaleriAdapter extends RecyclerView.Adapter<GaleriAdapter.MyViewHolder> {

    public Activity mContext;
    public List<Berita> beritaList;

    public GaleriAdapter(Activity mContext, List<Berita> beritaList){
        this.mContext = mContext;
        this.beritaList = beritaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_galeri_thumbnail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Berita berita = beritaList.get(position);
        Picasso.with(mContext).load(berita.getImage())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.thumbnail) ImageView thumbnail;
        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GaleriAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Activity context, final RecyclerView recyclerView, final GaleriAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }


        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
}
