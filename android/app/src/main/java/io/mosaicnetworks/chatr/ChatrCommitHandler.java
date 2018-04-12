package io.mosaicnetworks.chatr;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;

import mobile.CommitHandler;

public class ChatrCommitHandler implements CommitHandler {
    private MainActivity _context;

    public ChatrCommitHandler(Context context) {
        _context = (MainActivity)context;
    }

    @Override
    public void onCommit(final byte[] tx){
        _context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("Babble", "Received CommitTx " + tx);

                String message = null;
                try {
                    message = new String(tx, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                _context._messages.add(message);
                _context._adapter.notifyDataSetChanged();
            }
        });
    }
}
