package io.mosaicnetworks.chatr;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;

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

        String from = "";
        String content = "";

        if (tx.indexOf("|") == -1) {
            content = tx;
        }else {
            from = tx.substring(0, tx.indexOf("|"));
            content = tx.substring(tx.indexOf("|") +2, tx.length());
        }
        return new Message(from, content);
    }

    public byte[] Hash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(this.Encode());
            return hash;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }


}
