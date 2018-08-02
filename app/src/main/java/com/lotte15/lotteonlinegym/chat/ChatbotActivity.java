package com.lotte15.lotteonlinegym.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.lotte15.lotteonlinegym.R;

import java.util.HashMap;
import java.util.Map;

public class ChatbotActivity extends AppCompatActivity{
    private static final String TAG = ChatbotActivity.class.getSimpleName();
    private TextView chatDisplayTV;
    private EditText userStatementET;
    private final String IBM_USERNAME = "ab977afd-fe17-438a-b76a-3a5f66e4b993";
    private final String IBM_PASSWORD = "JEvrHWaE81Tu";
    private final String IBM_WORKSPACE_ID = "33ebdb21-2fec-4ec4-80ff-d34d6cbfdf34";
    private  Conversation conversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        setTitle("챗봇");
        chatDisplayTV = findViewById(R.id.tv_chat_display);
        userStatementET = findViewById(R.id.et_user_statement);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Watson-Learning-Opt-Out", "true");

        conversation = new Conversation("2018-07-10");
        conversation.setUsernameAndPassword(IBM_USERNAME,IBM_PASSWORD);
        conversation.setDefaultHeaders(headers);

        userStatementET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_DONE) {
                    //show the user statement
                    final String userStatement = userStatementET.getText().toString();
                    chatDisplayTV.append(
                            Html.fromHtml("<p><b>YOU:</b> " + userStatement + "</p>")
                    );
                    userStatementET.setText("");

                    InputData input = new InputData.Builder(userStatement).build();

                    MessageOptions options = new MessageOptions.Builder(IBM_WORKSPACE_ID)
                            .input(input)
                            .build();


                    conversation
                            .message(options)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    final String botStatement = response.getOutput().getText().get(0);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            chatDisplayTV.append(
                                                    Html.fromHtml("<p><b>BOT:</b> " +
                                                            botStatement + "</p>")
                                            );
                                        }
                                    });
                                    Log.e(TAG,"response :"+response);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            });
                }
                return false;
            }
        });
    }
}
