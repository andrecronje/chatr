package io.mosaicnetworks.chatr;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import io.mosaicnetworks.chatr.db.AppDatabase;
import io.mosaicnetworks.chatr.db.Contact;

public class AddContactActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        db = AppDatabase.getInstance(getApplicationContext());
    }

    /**
     * Called when the user taps the tick button - Starts QR scanner
     */
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
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Gson gson = new Gson();

                try {
                    Contact contact = gson.fromJson(result.getContents(), Contact.class);
                    db.contactDao().insertAll(contact);
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    // Start the contacts activity
                    Intent intent = new Intent(this, ContactsActivity.class);
                    startActivity(intent);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(this, "Invalid code: " + result.getContents(), Toast.LENGTH_LONG).show();
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(this, "A contact with same public key already exists", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}