package io.mosaicnetworks.chatr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.zxing.integration.android.IntentIntegrator;

public class JoinGroupActivity extends AppCompatActivity {

    public Config config;
    public String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        // Get the Intent that started this activity and extract the name string
        Intent intent = getIntent();
        myName = intent.getStringExtra("MY_NAME");
        config = Config._new(this);

        displayQRCode();
    }

    private void displayQRCode() {
        Peer peer = new Peer();
        peer.pubKeyHex = config.PublicKey;
        peer.netAddr = config.NodeAddr;
        peer.name = myName;

        Gson gson = new Gson();
        String content = gson.toJson(peer);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = (ImageView) findViewById(R.id.imageView);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {
            throw new ArithmeticException(String.format("Unexpected error occurred while generating QR code. %s",  e.toString()));
        }
    }

    /** Called when the user taps the tick button - starts QR scanner*/
    public void getGroup(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan code");
        integrator.initiateScan();
    }

    // Get results of QR scanner:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                Gson gson = new Gson();

                try {
                    // create group object from qr string and add to list of groups
                    Group group = gson.fromJson(result.getContents(), Group.class);
                    group.myName = myName;
                    config.addGroup(group);

                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    // Start the group details activity
                    Intent intent = new Intent(this, GroupDetailsActivity.class);
                    intent.putExtra("GROUP_NAME", group.groupName);
                    startActivity(intent);

                } catch (JsonSyntaxException e) {
                    Toast.makeText(this, "Invalid code: " + result.getContents(), Toast.LENGTH_LONG).show();

                }
            }
        }
    }
}


