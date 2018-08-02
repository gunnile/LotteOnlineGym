package com.lotte15.lotteonlinegym.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lotte15.lotteonlinegym.R;
import com.lotte15.lotteonlinegym.model.ChatModel;
import com.lotte15.lotteonlinegym.model.UserModel;
import com.lotte15.lotteonlinegym.util.GlobalApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyeongpil on 2018-07-30.
 */

public class MessageActivity extends AppCompatActivity {
    final static String TAG = MessageActivity.class.getSimpleName();

    private String destinationUid;
    @BindView(R.id.btn_message_send)
    Button btn_send;
    @BindView(R.id.et_message_chat)
    EditText et_chat;
    @BindView(R.id.recycler_message)
    RecyclerView recyclerView;


    private String uid;
    private String chatRoomUid;
    private List<ChatModel.Comment> comments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        setTitle("채팅");

        init();
//        checkChatRoom();

    }

    private void init() {
        uid = GlobalApplication.getGlobalApplicationContext().getCurrentUser().getName();
        destinationUid = getIntent().getStringExtra("destinationUid");
        chatRoomUid = "asdqwdqwas2"; // todo 하드코딩박아둠
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid,true);
                chatModel.users.put(destinationUid,true);
                //처음 방을 생성할 때 push() 는 이름을 임의적으로 만들어서 채팅방 생성
                if(chatRoomUid == null){
                    btn_send.setEnabled(false); // 성공콜백을 받기 전 빠르게 요청을 보내면 방이 여러개 생성되는것을 방지
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            checkChatRoom();
                        }
                    });
                }else{
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = et_chat.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment);
                    et_chat.setText("");
                    comments.add(comment);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
//        checkChatRoom();
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        recyclerView.setAdapter(new RecyclerViewAdapter());

    }


//    /**
//     *  채팅방 체크, uid에 해당하는 방 목록을 불러와 목적 uid 방이 있으면 그 방을 등록
//     */
//    private void checkChatRoom(){
//        Log.e(TAG,"checkChatRoom");
//        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot item : dataSnapshot.getChildren()){
//                    ChatModel chatModel = item.getValue(ChatModel.class);
//                    if(chatModel.users.containsKey(destinationUid)){
//                        chatRoomUid = item.getKey();
//                        btn_send.setEnabled(true); // 메시지 보내는 버튼 다시 활성화
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        UserModel userModel;
        public RecyclerViewAdapter() {
            comments = new ArrayList<>();
            userModel = GlobalApplication.getGlobalApplicationContext().getCurrentUser();
            getMessageList();

//            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    userModel = dataSnapshot.getValue(UserModel.class);
//                    getMessageList();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

        }


        void getMessageList(){
            Log.e(TAG, "getMessageList 진입");
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e(TAG, "getMessageList onDataChange 진입");
                    comments.clear();

                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "getMessageList onCancelled 진입 : "+databaseError);

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            //내가 보내는 메시지와 상대방이 보내는 메시지를 구분
            if(comments.get(position).uid.equals(uid)){ //내가 보낼 때
                messageViewHolder.tv_message.setText(comments.get(position).message);
                messageViewHolder.tv_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.ll_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.tv_message.setTextSize(20);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageViewHolder.ll_container.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                messageViewHolder.ll_container.setLayoutParams(params);
            }else{ // 상대방이 보낼 때
                messageViewHolder.ll_destination.setVisibility(View.VISIBLE);
                messageViewHolder.tv_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.tv_message.setText(comments.get(position).message);
                messageViewHolder.tv_message.setTextSize(20);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageViewHolder.ll_container.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                messageViewHolder.ll_container.setLayoutParams(params);
            }

        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_message;
            public TextView tv_name;
            public ImageView iv_profile;
            public LinearLayout ll_destination;
            public LinearLayout ll_container;

            public MessageViewHolder(View view) {
                super(view);
                tv_message = (TextView) view.findViewById(R.id.tv_item_meesage);
                tv_name = (TextView) view.findViewById(R.id.tv_message_item_name);
                iv_profile = (ImageView) view.findViewById(R.id.iv_message_item_profile);
                ll_destination = (LinearLayout) view.findViewById(R.id.ll_message_item_destination);
                ll_container = (LinearLayout) view.findViewById(R.id.ll_message_item_container);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }
}
