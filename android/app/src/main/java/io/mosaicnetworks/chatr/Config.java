package io.mosaicnetworks.chatr;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("Peers")
    public Peer[] Peers;

    //ex: "-----BEGIN EC PRIVATE KEY-----\nMHcCAQEEIP7PIEnr/7RUSLc55XP44GTsAxsSg/AzekqHqXDQxfGKoAoGCCqGSM49AwEHoUQDQgAEpM2p+b0DxYTEJaPtGiaM2oVjtixMbx5S6BrVUDREuH8A4rNKrAWohJFZHHG6U5w15y9KaTYntoB5Cq/mS0x6ww==\n-----END EC PRIVATE KEY-----"
    @SerializedName("PrivateKey")
    public String PrivateKey;

    // IP:Port to bind Babble
    @SerializedName("NodeAddr")
    public String NodeAddr;

    //"inmem" or "badger"
    @SerializedName("StoreType")
    public String StoreType;

    //File containing the Store DB
    @SerializedName("StorePath")
    public String StorePath;

    //Heartbeat timeout in milliseconds (1000)
    @SerializedName("Heartbeat")
    public int Heartbeat;

    //TCP timeout milliseconds (1000)
    @SerializedName("TCPTimeout")
    public int TCPTimeout;

    //Max number of pooled connections (2)
    @SerializedName("MaxPool")
    public int MaxPool;

    //Number of items in LRU cache (500)
    @SerializedName("CacheSize")
    public int CacheSize;

    //Max Events per sync (1000)
    @SerializedName("SyncLimit")
    public int SyncLimit;

}
