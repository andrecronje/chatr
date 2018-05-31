package io.mosaicnetworks.chatr;


import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import mobile.Mobile;
import mobile.Node;
import mobile.MobileConfig;

public class BabbleNode {

    private Node _node;
    public Group group;

    public BabbleNode(String groupName, Context context) {

        Config _config = Config._new(context);

        MobileConfig babbleConfig = new MobileConfig(
                _config.Heartbeat,
                _config.TCPTimeout,
                _config.MaxPool,
                _config.CacheSize,
                _config.SyncLimit,
                _config.StoreType,
                _config.StorePath
        );

        ChatrCommitHandler commitHandler = new ChatrCommitHandler(context);
        ChatrExceptionHandler exceptionHandler = new ChatrExceptionHandler(context);

        try {
            group = _config.getGroupByName(groupName);
        }catch (NoSuchFieldException e) {
            throw new ArithmeticException(String.format("Could not find group '%s'", groupName));
        }

        Peer[] Peers = new Peer[group.Peers.size()];
        group.Peers.toArray(Peers);
        String peers = gson2String(Peers);

        _node = Mobile.new_(
                _config.PrivateKey,
                _config.NodeAddr,
                peers,
                commitHandler,
                exceptionHandler,
                babbleConfig);
    }

    public void Run() {
        if (_node != null)
            _node.run(true);
    }

    public void Shutdown() {
        if (_node != null)
            _node.shutdown();
    }

    public void SubmitTx(byte[] tx) {
        _node.submitTx(tx);
    }

    String  gson2String ( Peer[] peers) {
        try {
            Gson gson = new Gson();
            String strJson = gson.toJson(peers);
            return strJson;
        }catch(Exception e){
            throw new ArithmeticException(String.format("Unexpected error occurred while converting json to string. %s",  e.toString()));
        }
    }
}




