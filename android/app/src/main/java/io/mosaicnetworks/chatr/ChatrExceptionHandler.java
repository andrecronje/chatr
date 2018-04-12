package io.mosaicnetworks.chatr;

import android.content.Context;
import android.util.Log;

import mobile.ExceptionHandler;

public class ChatrExceptionHandler implements ExceptionHandler {
    private MainActivity _context;

    public ChatrExceptionHandler(Context context) {
        _context = (MainActivity)context;
    }

    @Override
    public void onException(final String msg){
        _context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("Babble", "Received Exception " + msg);
            }
        });
    }
}
