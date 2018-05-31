package io.mosaicnetworks.chatr;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements GroupsListAdapter.ItemClickListener {

    GroupsListAdapter adapter;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = Config._new(this);
        initRecyclerView();
    }

    public void initRecyclerView() {

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvGroups);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GroupsListAdapter(this, config.Groups);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //setup divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    /** Called when the user taps a group in the groups list */
    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(this, GroupDetailsActivity.class);
        intent.putExtra("GROUP_NAME", adapter.getItem(position).groupName);
        startActivity(intent);
    }

    /** Called when the user taps the new group button */
    public void newGroup(View view) {
        Intent intent = new Intent(this, NewGroupActivity.class);
        startActivity(intent);

    }

    /** Called when the user taps the join group button */
    public void joinGroup(View view) {
        Intent intent = new Intent(this, JoinGroupNameActivity.class);
        startActivity(intent);

    }

    /** Called when the user taps the dustbin button */
    public void deleteGroups(View view) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete All Groups");
        alertDialog.setMessage("Are you sure you want to delete all groups?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGroups();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    private void deleteGroups() {
        config.deleteGroups();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Deleted all groups", Toast.LENGTH_LONG).show();
    }
}