package io.mosaicnetworks.chatr;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShareMyContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_my_contact);

        displayQRCode();
    }

    private void displayQRCode() {
        Peer peer = new Peer();
        peer.pubKeyHex = "123";
        peer.netAddr = "123";
        peer.name = "abc";

        Gson gson = new Gson();
        String content = gson.toJson(peer);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = (ImageView) findViewById(R.id.imageView6);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {
            throw new ArithmeticException(String.format("Unexpected error occurred while generating QR code. %s",  e.toString()));
        }
    }
}
