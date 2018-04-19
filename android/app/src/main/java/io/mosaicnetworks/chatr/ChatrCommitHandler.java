package io.mosaicnetworks.chatr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import mobile.CommitHandler;

public class ChatrCommitHandler implements CommitHandler {
    private MainActivity _context;
    private Gson _customGson;

    public ChatrCommitHandler(Context context) {

        _context = (MainActivity)context;

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(byte[].class, new JsonDeserializer<byte[]>() {
            public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return Base64.decode(json.getAsString(), Base64.NO_WRAP);
            }
        });

        _customGson = builder.create();
    }

    @Override
    public byte[] onCommit(final byte[] blockBytes){
        Log.i("Babble", "Received CommitBlock " + blockBytes.toString());

        String strJson = new String(blockBytes);
        Block block = null;
        try {
            block = _customGson.fromJson(strJson, Block.class);
        } catch(Exception ex) {
            Log.e("Babble", "Failed to parse Block", ex);
            return null;
        }

        for (int i = 0; i < block.Body.Transactions.length; i++){
            byte[] tx = block.Body.Transactions[i];
            try {
                Message message = Message.Decode(block.Body.Transactions[i]);
                _context.processMessage(message);
            }catch(Exception ex){
                Log.e("Babble", "Failed to parse Transaction", ex);
            }
        }


        _context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _context.updateUI();
            }
        });


        return _context.stateHash;
    }
}
