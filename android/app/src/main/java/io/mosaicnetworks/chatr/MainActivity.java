package io.mosaicnetworks.chatr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static io.mosaicnetworks.chatr.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BabbleNode babbleNode = new BabbleNode(this);
        babbleNode.Run();

        setContentView(activity_main);
    }
}
