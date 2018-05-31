package io.mosaicnetworks.chatr;

import com.google.gson.annotations.SerializedName;

        import java.io.Serializable;
        import java.util.Map;


public class Block implements Serializable {

    public class BlockBody implements Serializable {
        @SerializedName("Index")
        public int Index;

        @SerializedName("RoundReceived")
        public int RoundReceived;

        @SerializedName("StateHash")
        public byte[] StateHash;

        @SerializedName("Transactions")
        public byte[][] Transactions;
    }

    @SerializedName("Body")
    public BlockBody Body;

    @SerializedName("Signatures")
    public Map<String, String> Transactions;
}