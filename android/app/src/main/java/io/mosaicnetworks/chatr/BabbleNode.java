package io.mosaicnetworks.chatr;

import com.google.gson.Gson;

import android.content.Context;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import mobile.Mobile;
import mobile.Node;
import mobile.MobileConfig;

public class BabbleNode {

    private Context _context;
    private Config _config;
    private Node _node;

    public BabbleNode(Context context) {

        _context = context;

        _config = getConfig();
        String peers = gson2String(_config.peers);
        MobileConfig babbleConfig = Mobile.defaultMobileConfig();


        ChatrCommitHandler commitHandler = new ChatrCommitHandler(context);
        ChatrExceptionHandler exceptionHandler = new ChatrExceptionHandler(context);

        _node = Mobile.new_(_config.node_addr,
                peers,
                _config.nodePrivateKey,
                commitHandler,
                exceptionHandler,
                babbleConfig);
    }

    public void Run() {
        if (_node != null)
            _node.run(true);
    }

    Config getConfig() {
        File folder = _context.getExternalFilesDir(null);    //===/storage/sdcard0/Android/data/io.babble.mobile.myapplication/files==
        if (!folder.exists()) {
            throw new ArithmeticException(String.format(String.format("Config folder not found '%s'", folder)));
        }

        File file = new File(folder + "/ConfigBabbleMobile.json");
        if (!file.exists()) {
            throw new  ArithmeticException(String.format("ConfigBabbleMobile.json file not found in '%s'", file));
        }

        try {
            byte[] fileData = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();

            Gson gson = new Gson();
            String strJson = new String(fileData);
            Config res = gson.fromJson(strJson, Config.class);
            return res;
        }catch(Exception e){
            throw new ArithmeticException(String.format("Unexpected error occur while loading json file '%s', %s", file, e.toString()));
        }
    }

    String  gson2String ( Peer[] pears) {
        try {
            Gson gson = new Gson();
            String strJson = gson.toJson(pears);
            return strJson;
        }catch(Exception e){
            throw new ArithmeticException(String.format("Unexpected error occurred while converting json to string. %s",  e.toString()));
        }
    }
}




