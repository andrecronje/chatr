package io.mosaicnetworks.chatr;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable{

    @SerializedName("Peers")
    //public Peer[] Peers;
    List<Peer> Peers = new ArrayList<Peer>();

    @SerializedName("Name")
    public String groupName;

    @SerializedName("MyName")
    public String myName;

    public void addPeer(Peer peer) {
        Peers.add(peer);

    }

}

