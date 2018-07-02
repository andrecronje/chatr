package io.mosaicnetworks.chatr;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import io.mosaicnetworks.chatr.db.AppDatabase;
import io.mosaicnetworks.chatr.db.Contact;

public class ContactsActivity extends AppCompatActivity implements ContactsListAdapter.ItemClickListener{

    AppDatabase db;
    List<Contact> contactsList = new ArrayList<>();
    ContactsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        getApplicationContext().deleteDatabase("contacts-database");
        getApplicationContext().deleteDatabase("my-database");

        //db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "contacts-database").allowMainThreadQueries().build();
        db = AppDatabase.getInstance(getApplicationContext());

        contactsList = db.contactDao().getAll();
        //Log.d("myTag", String.valueOf(contactsList.size()));
        initRecyclerView();

    }

    public void initRecyclerView() {

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvContacts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ContactsListAdapter(this, contactsList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contacts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share_my_contact:
                Intent intent = new Intent(this, ShareMyContactActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user taps the plus button */
    public void addContact(View view) {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);

    }


}
