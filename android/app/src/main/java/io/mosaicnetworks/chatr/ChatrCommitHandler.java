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
    public void onCommit(final byte[] blockBytes){
        _context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("Babble", "Received CommitBlock " + blockBytes);

                String strJson = new String(blockBytes);
                Block block = _customGson.fromJson(strJson, Block.class);

                Log.i("Babble", "Received CommitBlock " + block);

                for (int i = 0; i < block.Transactions.length; i++){
                    Message message = Message.Decode(block.Transactions[i]);
                    _context.displayMessage(message);
                }
            }
        });
    }
}
