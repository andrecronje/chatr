package io.mosaicnetworks.chatr;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Message {
    public String from;
    public String content;

    public Message(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public byte[] Encode() {
        String tx = this.from + "||" + this.content;
        return tx.getBytes(Charset.forName("UTF-8"));
    }

    public static Message Decode(byte[] rawTx) {
        String tx = "";
        try {
            tx = new String(rawTx, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String from = tx.substring(0, tx.indexOf("|"));
        String content = tx.substring(tx.indexOf("|") +2, tx.length());
        return new Message(from, content);
    }


}
