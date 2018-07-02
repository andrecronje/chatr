package io.mosaicnetworks.chatr.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Contact implements Serializable{
    @PrimaryKey
    @NonNull
    @SerializedName("PubKeyHex")
    private String pubKeyHex;

    @SerializedName("NetAddr")
    @ColumnInfo(name = "net_addr")
    private String netAddr;

    @SerializedName("UserName")
    @ColumnInfo(name = "user_name")
    private String userName;

    public String getPubKeyHex() {
        return pubKeyHex;
    }

    public void setPubKeyHex(String pubKeyHex) {
        this.pubKeyHex = pubKeyHex;
    }

    public String getNetAddr() {
        return netAddr;
    }

    public void setNetAddr(String netAddr) {
        this.netAddr = netAddr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
