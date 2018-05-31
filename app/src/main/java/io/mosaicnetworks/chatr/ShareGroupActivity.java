package io.mosaicnetworks.chatr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ShareGroupActivity extends AppCompatActivity {

    public String groupName;
    public Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_group);

        // Get the Intent that started this activity and extract the name string
        Intent intent = getIntent();
        groupName = intent.getStringExtra("GROUP_NAME");

        Config _config = Config._new(this);

        try {
            group = _config.getGroupByName(groupName);
        } catch (NoSuchFieldException e) {
            throw new ArithmeticException(String.format("Could not find group '%s'", groupName));
        }

        displayQRCode();
    }

    private void displayQRCode() {
        Gson gson = new Gson();
        String content = gson.toJson(group);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = (ImageView) findViewById(R.id.imageView2);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch(Exception e) {
            throw new ArithmeticException(String.format("Unexpected error occurred while generating QR code. %s",  e.toString()));
        }

    }

    /** Called when the user taps the tick button */
    public void sealGroup(View view) {

        Intent intent = new Intent(this, GroupDetailsActivity.class);
        intent.putExtra("GROUP_NAME", groupName);
        startActivity(intent);

    }
}
