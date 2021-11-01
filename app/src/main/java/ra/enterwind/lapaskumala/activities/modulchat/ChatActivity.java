package ra.enterwind.lapaskumala.activities.modulchat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import ra.enterwind.lapaskumala.R;
import ra.enterwind.lapaskumala.activities.BaseCallActivity;
import ra.enterwind.lapaskumala.activities.modulcall.OutGoingActivity;
import ra.enterwind.lapaskumala.adapter.ChatAdapter;
import ra.enterwind.lapaskumala.models.Chat;
import ra.enterwind.lapaskumala.utils.Constants;
import ra.enterwind.lapaskumala.utils.RtcUtils;
import ra.enterwind.lapaskumala.utils.SessionManager;

public class ChatActivity extends BaseCallActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    SessionManager sessionManagerl;
    String username, poto, namanya, notif, pasang;
    ImageView imageView;
    @BindView(R.id.getNama) TextView txtNama;
    @BindView(R.id.resikeclChat) RecyclerView recyclerView;

    List<Chat> chatList;
    ChatAdapter adapter;

    @Override
    public void onCreate(Bundle tes) {
        super.onCreate(tes);
        setContentView(R.layout.activity_main_chat);
        ButterKnife.bind(this);

        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);
        txtNama.setText(getIntent().getStringExtra("nama"));
        poto = getIntent().getStringExtra("foto");
        namanya = getIntent().getStringExtra("nama");
        pasang = getIntent().getStringExtra("pasang");
        notif = getIntent().getStringExtra("one_id");
        Toast.makeText(this, ""+notif, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, ""+pasang, Toast.LENGTH_SHORT).show();
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        Firebase.setAndroidContext(this);
        sessionManagerl = new SessionManager(getApplicationContext());
        HashMap<String, String> detail = sessionManagerl.getUserDetail();
        username = detail.get(SessionManager.KEY_NAME);

        //Chat With diganti Adapter dari database
        // Username Get Session Manager
        reference1 = new Firebase("https://belajarcrud-e3fba.firebaseio.com/message/" + username + "_" + getIntent().getStringExtra("nama"));
        reference2 = new Firebase("https://belajarcrud-e3fba.firebaseio.com/message/" + getIntent().getStringExtra("nama") + "_" + username);

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", username);
                    map.put("time", currentTime);
                    new SendNotification(messageText, username, notif);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

      reference1.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              chatList.clear();
              for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                  Chat chat = snapshot.getValue(Chat.class);
                  chatList.add(chat);
              }

              adapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(FirebaseError firebaseError) {

          }
      });


    }


    @Override
    public RtmClient rtmClient() {
        return application().rtmClient();
    }

    @OnClick(R.id.imgVideoCalll) void onCall() {
//        Intent aa = new Intent(ChatActivity.this, OutGoingActivity.class);
//        aa.putExtra("nama", getIntent().getStringExtra("nama"));
//        startActivity(aa);

            final String peer = pasang;
            Set<String> peerSet = new HashSet<>();
            peerSet.add(peer);

            String uid = String.valueOf(ChatActivity.this.
                    application().config().getUserId());
            String channel = RtcUtils.channelName(uid, peer);
            new SendNotification("Ada Panggilan", username, notif);
            ChatActivity.this.gotoCallingInterface(peer, channel, Constants.ROLE_CALLER, poto,  namanya);
    }

}
