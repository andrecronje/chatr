package io.mosaicnetworks.chatr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
                Message message = Message.Decode(tx);
                _context.displayMessage(message);
            }
        });
    }
}
