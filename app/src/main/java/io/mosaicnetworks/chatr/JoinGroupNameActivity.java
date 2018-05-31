package io.mosaicnetworks.chatr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class JoinGroupNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group_name);
    }

    /** Called when the user taps the tick button */
    public void joinGroup(View view) {
        Intent intent = new Intent(this, JoinGroupActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText4);
        String myName = editText.getText().toString();
        intent.putExtra("MY_NAME", myName);
        startActivity(intent);

    }
}
