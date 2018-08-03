package com.lotte15.lotteonlinegym.chat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lotte15.lotteonlinegym.R;
import com.lotte15.lotteonlinegym.model.ChatModel;
import com.lotte15.lotteonlinegym.model.UserModel;
import com.lotte15.lotteonlinegym.util.GlobalApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;


/**
 * Created by Hyeongpil on 2018. 7. 30..
 */

public class ChattingActivity extends AppCompatActivity {
    final static String TAG = ChattingActivity.class.getSimpleName();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ButterKnife.bind(this);
        setTitle("채팅방");

        init();

    }

    private void init() {
        recyclerView = (RecyclerView)findViewById(R.id.recycler_chatting);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());

        Log.e(TAG,"firebase : "+FirebaseDatabase.getInstance().getReference());
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<ChatModel> chatModels = new ArrayList<>();
        private String uid;
        private ArrayList<String> destinationUsers = new ArrayList<>();


        public ChatRecyclerViewAdapter(){
            Log.e(TAG,"adapter 진입 :"+FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid));
            uid = GlobalApplication.getGlobalApplicationContext().getCurrentUser().getName();
            Log.e(TAG,"uid : "+uid);
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e(TAG,"onDataChange 진입 : "+dataSnapshot);
                    chatModels.clear();
                    for (DataSnapshot item :dataSnapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                        Log.e(TAG,"chatModels.add ");
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG,"onCancelled 진입 : "+databaseError);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatting,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destinationUid = null;


            Log.e(TAG,"chatModels :"+chatModels );
            //채팅방에 있는 유저들 체크
            for(String user : chatModels.get(position).users.keySet()){
                Log.e(TAG,"유저체크 진입 : des user :"+user );
                if(!user.equals(uid)){
//                    destinationUid = user;
                    destinationUid = "1"; // todo 하드코딩지우기
                    destinationUsers.add(destinationUid);

                }
            }
            destinationUid = "1"; // todo 하드코딩지우기
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.e(TAG,"ondatachange 진입 : "+dataSnapshot.getValue());
//                    //프로필 이미지
//                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
//
//                    //타이틀
//                    customViewHolder.tv_title.setText(userModel.getName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //마지막 메시지 가져오기 메시지를 내림차순으로 정렬 후 키값 가져옴
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);
//            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
//            customViewHolder.tv_lastMessage.setText(chatModels.get(position).comments.get(lastMessageKey).message);
            customViewHolder.tv_lastMessage.setText("디파짓 5000원 방");

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", "2");

                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(),R.anim.fromright, R.anim.toleft);
                    startActivity(intent,activityOptions.toBundle());
                }
            });

            if(position == 0){
                customViewHolder.tv_title.setText("턱걸이 하루 20개");
                Glide.with(customViewHolder.itemView.getContext())
                            .load(R.drawable.people)
                            .apply(new RequestOptions().circleCrop())
                            .into(customViewHolder.iv_chatting);
            } else if (position == 1){
                customViewHolder.tv_title.setText("물 1.5L 마시는 방");
                Glide.with(customViewHolder.itemView.getContext())
                        .load(R.drawable.people22)
                        .apply(new RequestOptions().circleCrop())
                        .into(customViewHolder.iv_chatting);
            }
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }


        private class CustomViewHolder extends RecyclerView.ViewHolder{
            public ImageView iv_chatting;
            public TextView tv_title;
            public TextView tv_lastMessage;

            public CustomViewHolder(View view){
                super(view);
                iv_chatting = (ImageView) view.findViewById(R.id.iv_chat_item_image);
                tv_title = (TextView) view.findViewById(R.id.tv_chat_item_title);
                tv_lastMessage = (TextView) view.findViewById(R.id.tv_chat_item_lastMessage);
            }
        }
    }
}
