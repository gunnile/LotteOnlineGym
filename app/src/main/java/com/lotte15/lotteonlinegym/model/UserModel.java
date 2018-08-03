package com.lotte15.lotteonlinegym.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hyeongpil on 2018-04-10.
 */

public class UserModel implements Serializable {
    //@SerialzedName JSON으로 serialize 될 때 매칭되는 이름을 명시하는 목적으로 사용되는 field 마킹 어노테이션이다.

//    public String profileImageUrl;
    @SerializedName("Uid")
    public String uid;
    @SerializedName("Name")
    public String name;
    @SerializedName("Gender")
    public String gender;
    @SerializedName("Height")
    public int height;
    @SerializedName("Weight")
    public int weight;
    @SerializedName("Exr")
    public String exr; // 원하는 운동 종류
    @SerializedName("Point")
    public String point; // 캐시
    @SerializedName("Phy")
    public String phy; // 피지컬 점수

//    public String getProfileImageUrl() {
//        return profileImageUrl;
//    }
//
//    public void setProfileImageUrl(String profileImageUrl) {
//        this.profileImageUrl = profileImageUrl;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getExr() {
        return exr;
    }

    public void setExr(String exr) {
        this.exr = exr;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPhy() {
        return phy;
    }

    public void setPhy(String phy) {
        this.phy = phy;
    }
}
