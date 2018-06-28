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

    @SerializedName("Name")
    @ColumnInfo(name = "name")
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
