package io.mosaicnetworks.chatr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class GroupDetailsActivity extends AppCompatActivity {

    BabbleNode _babbleNode;
    List<Message> messageList = new ArrayList<Message>();
    byte[] stateHash = "Genesis Hash".getBytes();
    public String myName;
    MessageListAdapter messageAdapter;
    RecyclerView messageRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        // Get the Intent that started this activity and extract the name string
        Intent intent = getIntent();
        String groupName = intent.getStringExtra("GROUP_NAME");

        setTitle(groupName);
        getMyName(groupName);
        initRecyclerView();
        initEditView();

        //create and start the Babble node
        _babbleNode = new BabbleNode(groupName, this);
        _babbleNode.Run();
    }

    private void getMyName(String groupName) {
        Config config = Config._new(this);
        try {
            myName = config.getGroupByName(groupName).myName;
        } catch (NoSuchFieldException e) {
            throw new ArithmeticException(String.format("Could not find group '%s'", groupName));
        }
    }

    public void initRecyclerView() {

        messageRecycler = (RecyclerView) findViewById(R.id.recyclerview_message_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(layoutManager);

        // Moves scroll position when soft keyboard is displayed
        messageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right,int bottom, int oldLeft, int oldTop,int oldRight, int oldBottom)
            {

                messageRecycler.scrollToPosition(messageRecycler.getAdapter().getItemCount() - 1);

            }
        });

        messageAdapter = new MessageListAdapter(this, myName, messageList);
        messageRecycler.setAdapter(messageAdapter);

    }

    public void initEditView() {
        EditText editText = (EditText) findViewById(R.id.editText2);
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
        EditText editText = (EditText) findViewById(R.id.editText2);
        String content = editText.getText().toString();

        //delete text after sending
        editText.getText().clear();

        //encode the message
        Message message = new Message(myName, content);
        byte[] messageBytes = message.Encode();
        Toast.makeText(this, "Sending message...", Toast.LENGTH_LONG).show();

        //submit the transaction to Babble
        _babbleNode.SubmitTx(messageBytes);
    }


    public void processMessage(Message message) {
        messageList.add(message);
        stateHash = hashFromTwoHashes(stateHash, message.Hash());
    }

    public byte[] hashFromTwoHashes(byte[] a, byte[] b) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] tempHash = new byte[a.length + b.length];
            System.arraycopy(a, 0, tempHash, 0, a.length);
            System.arraycopy(b, 0, tempHash, 0, b.length);
            return digest.digest(tempHash);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void updateUI() {
        messageAdapter.notifyDataSetChanged();
        messageRecycler.scrollToPosition(messageList.size() -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        _babbleNode.Shutdown();
    }
}

