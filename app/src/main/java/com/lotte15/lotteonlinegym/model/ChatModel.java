package com.lotte15.lotteonlinegym.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hyeongpil on 2018-04-25.
 */

public class ChatModel {

    public Map<String,Boolean> users = new HashMap<>(); //채팅방의 유저들
    public Map<String,Comment> comments = new HashMap<>(); // 채팅방의 내용

    public static class Comment {
        public String uid;
        public String message;
    }
}
