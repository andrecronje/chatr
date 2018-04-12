package io.mosaicnetworks.chatr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static io.mosaicnetworks.chatr.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    BabbleNode _babbleNode;
    ArrayList<String> _messages = new ArrayList<String>();
    ArrayAdapter<String> _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        //setup the listview where messages will be displayed
        _adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                _messages);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(_adapter);

        //create and start the Babble node
        _babbleNode = new BabbleNode(this);
        _babbleNode.Run();


    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();

        //encode the message
        byte[] messageBytes = null;
        try {
            messageBytes = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //submit the transaction to Babble
        _babbleNode.SubmitTx(messageBytes);

    }
}
