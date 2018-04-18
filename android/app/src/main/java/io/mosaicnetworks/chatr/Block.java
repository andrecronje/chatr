package io.mosaicnetworks.chatr;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Block implements Serializable {
    @SerializedName("RoundReceived")
    public int RoundReceived;

    @SerializedName("Transactions")
    public byte[][] Transactions;
}
