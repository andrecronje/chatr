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
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.mosaicnetworks.chatr.db.AppDatabase;
import io.mosaicnetworks.chatr.db.Contact;

public class ContactsActivity extends AppCompatActivity implements ContactsListAdapter.ItemClickListener{

    AppDatabase db;
    Random rand = new Random();
    List<Contact> contactsList = new ArrayList<>();
    ContactsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "contacts-database").allowMainThreadQueries().build();

        contactsList = db.contactDao().getAll();
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

    /** Called when the user taps the plus button to add a new contact - Starts QR scanner*/
    public void addContact(View view) {

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
                Gson gson = new Gson();

                try {
                    Contact contact = gson.fromJson(result.getContents(), Contact.class);
                    Log.d("myTag", contact.getPubKeyHex());
                    db.contactDao().insertAll(contact);
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    contactsList.add(contact);
                    adapter.notifyDataSetChanged();
                } catch (JsonSyntaxException e) {
                    Toast.makeText(this, "Invalid code: " + result.getContents(), Toast.LENGTH_LONG).show();
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(this, "Contact with same public key already exists", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
