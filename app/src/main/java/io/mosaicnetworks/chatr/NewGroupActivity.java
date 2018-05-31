package io.mosaicnetworks.chatr;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class NewGroupActivity extends AppCompatActivity implements GroupMembersAdapter.ItemClickListener {

    public Config config;
    public Group newGroup = new Group();
    GroupMembersAdapter adapter;
    List<Peer> membersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        config = Config._new(this);

        initRecyclerView();
    }


    public void initRecyclerView() {

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvMembers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GroupMembersAdapter(this, membersList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //setup divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClick(View view, int position) {
        //placeholder
    }

    /** Called when the user taps the tick button */
    public void sealGroup(View view) {

        if (newGroup.Peers.isEmpty()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Add Members");
            alertDialog.setMessage("You need to add at least one member to the group");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        } else {

            finalisePeersList();

            EditText editText2 = (EditText) findViewById(R.id.editText);
            newGroup.groupName = editText2.getText().toString();

            config.addGroup(newGroup);
            Intent intent = new Intent(this, ShareGroupActivity.class);
            intent.putExtra("GROUP_NAME", newGroup.groupName);
            startActivity(intent);

        }

    }

    private void finalisePeersList() {
        // add ourselves to the groups peer list
        Peer peer = new Peer();
        peer.netAddr = config.NodeAddr;
        peer.pubKeyHex = config.PublicKey;

        // add our name
        EditText editText = (EditText) findViewById(R.id.editText3);
        peer.name = editText.getText().toString();

        newGroup.Peers.add(peer);
        newGroup.myName = peer.name;

    }

    /** Called when the user taps the plus button to add a new member - Starts QR scanner*/
    public void addMember(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan code");
        integrator.initiateScan();
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                Gson gson = new Gson();

                try {
                    Peer peer = gson.fromJson(result.getContents(), Peer.class);
                    newGroup.addPeer(peer);
                    membersList.add(peer);
                    adapter.notifyDataSetChanged();
                } catch (JsonSyntaxException e) {
                    Toast.makeText(this, "Invalid code: " + result.getContents(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
