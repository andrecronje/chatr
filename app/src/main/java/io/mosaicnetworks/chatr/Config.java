package io.mosaicnetworks.chatr;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mobile.Mobile;

public class Config {

    @SerializedName("Groups")
    public List<Group> Groups = new ArrayList<Group>();

    //ex: "-----BEGIN EC PRIVATE KEY-----\nMhellHcCAQEEIP7PIEnr/7RUSLc55XP44GTsAxsSg/AzekqHqXDQxfGKoAoGCCqGSM49AwEHoUQDQgAEpM2p+b0DxYTEJaPtGiaM2oVjtixMbx5S6BrVUDREuH8A4rNKrAWohJFZHHG6U5w15y9KaTYntoB5Cq/mS0x6ww==\n-----END EC PRIVATE KEY-----"
    @SerializedName("PrivateKey")
    public String PrivateKey;

    @SerializedName("PublicKey")
    public String PublicKey;

    // IP:Port to bind Babble
    @SerializedName("NodeAddr")
    public String NodeAddr;

    //"inmem" or "badger"
    @SerializedName("StoreType")
    public String StoreType = "inmem";

    //File containing the Store DB
    @SerializedName("StorePath")
    public String StorePath = "";

    //Heartbeat timeout in milliseconds (1000)
    @SerializedName("Heartbeat")
    public int Heartbeat = 1000;

    //TCP timeout milliseconds (1000)
    @SerializedName("TCPTimeout")
    public int TCPTimeout = 1000;

    //Max number of pooled connections (2)
    @SerializedName("MaxPool")
    public int MaxPool = 2;

    //Number of items in LRU cache (500)
    @SerializedName("CacheSize")
    public int CacheSize = 500;

    //Max Events per sync (1000)
    @SerializedName("SyncLimit")
    public int SyncLimit = 1000;

    private transient Context context;

    private Config(Context _context) {

        String keyPair = Mobile.getPrivPublKeys();    //publicKey[!@#$%^]privateKey
        String[] separated =  keyPair.split("=!@#@!=");
        PublicKey = separated[0].trim();
        PrivateKey = separated[1].trim();

        NodeAddr = Utils.getIPAddress(true) + ":6666";
        context = _context;

    }

    public static Config _new(Context _context) {
        // Try to load config file from storage, create new config if that fails

        Config config;

        try {
            config = getConfigFromFile(_context);
            config.context = _context;
        } catch (ArithmeticException e) {
            config = new Config(_context);
        }

        return config;
    }

    static Config getConfigFromFile(Context context) {
        // Loads config from file

        File folder = context.getExternalFilesDir(null);    //===/storage/sdcard0/Android/data/io.mosaicnetworks.chatr/files==
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
        } catch(Exception e){
            throw new ArithmeticException(String.format("Unexpected error occurred while loading json file '%s', %s", file, e.toString()));
        }
    }


    private void writeConfig() {
        // Write config to file, overwriting any existing file

        File folder = context.getExternalFilesDir(null);    //===/storage/sdcard0/Android/data/io.mosaicnetworks.chatr/files==
        if (!folder.exists()) {
            throw new ArithmeticException(String.format(String.format("Config folder not found '%s'", folder)));
        }

        File file = new File(folder + "/ConfigBabbleMobile.json");

        try {
            Gson gson = new Gson();
            String strJson = gson.toJson(this);

            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(strJson);
            writer.flush();
            writer.close();

        }catch(Exception e){
            throw new ArithmeticException(String.format("Unexpected error occurred while writing json file '%s', %s", file, e.toString()));
        }

    }

    public void addGroup(Group group) {
        Groups.add(group);
        writeConfig();
    }

    public Group getGroupByName(String findName) throws NoSuchFieldException {

        Iterator<Group> iterator = Groups.iterator();
        Group group;
        while (iterator.hasNext()) {
            group = iterator.next();
            if (group.groupName.equals(findName)) {
                return group;
            }
        }

        throw new NoSuchFieldException(String.format("Could not find group '%s'", findName));
    }

    public void deleteGroups() {
        if (Groups != null) {
            Groups.clear();
            NodeAddr = Utils.getIPAddress(true) + ":6666";
            writeConfig();
        }
    }
}
