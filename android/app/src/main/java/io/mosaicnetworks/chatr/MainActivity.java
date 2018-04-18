package io.mosaicnetworks.chatr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static io.mosaicnetworks.chatr.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    BabbleNode _babbleNode;

    List<Message> messageList = new ArrayList<Message>();
    MessageListAdapter messageAdapter;
    RecyclerView messageRecycler;

    public String senderID = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        //setup the listview where messages will be displayed
        initRecyclerView();

        //setup editView where messages are entered
        initEditView();

        //create and start the Babble node
        _babbleNode = new BabbleNode(this);
        _babbleNode.Run();
    }

    public void initRecyclerView() {

        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(layoutManager);

        messageAdapter = new MessageListAdapter(this, senderID, messageList);
        messageRecycler.setAdapter(messageAdapter);

    }

    public void initEditView() {
        EditText editText = (EditText) findViewById(R.id.edittext_chatbox);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage(v);
                    handled = true;
                }
                return handled;
            }
        });
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.edittext_chatbox);
        String content = editText.getText().toString();

        //encode the message_received
        Message message = new Message("phone", content);
        byte[] messageBytes = message.Encode();

        //submit the transaction to Babble
        _babbleNode.SubmitTx(messageBytes);
    }

    public void displayMessage(Message message) {
        messageList.add(message);
        messageAdapter.notifyDataSetChanged();
        messageRecycler.scrollToPosition(messageList.size() -1);
    }
}


