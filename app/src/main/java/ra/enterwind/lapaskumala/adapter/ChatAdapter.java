package ra.enterwind.lapaskumala.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.models.Chat;
import ra.enterwind.lapaskumala.utils.SessionManager;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> chatList;
    private Activity mContext;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    SessionManager sessionManager;
    String nama;


    public ChatAdapter(List<Chat> chatList, Activity mContext) {
        this.chatList = chatList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        sessionManager = new SessionManager(mContext);
        HashMap<String , String> user = sessionManager.getUserDetail();
        nama = user.get(SessionManager.KEY_NAME);
        if (chatList.get(position).getUser().equals(nama)){
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_chat_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_chat_left, parent, false);
            return  new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Chat chat = chatList.get(position);
        holder.message.setText(chat.getMessage());
        holder.waktu.setText(chat.getTime());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtMessage) TextView message;
        @BindView(R.id.timestamp) TextView waktu;
        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
