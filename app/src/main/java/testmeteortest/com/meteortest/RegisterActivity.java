package testmeteortest.com.meteortest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by Kunal on 23-04-2016.
 */
public class RegisterActivity extends Activity {
    public static Meteor mMeteor;
    static String DATABASE_NAME = "chating";
    private EditText editTextName;
    private Button buttonInsertRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        editTextName = (EditText) findViewById(R.id.input_name);
        buttonInsertRecord = (Button) findViewById(R.id.insert_button);


        buttonInsertRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(editTextName.getText().toString().length() == 0) {
                    Toast.makeText(v.getContext(), "Please enter name ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intentActivity = new Intent(RegisterActivity.this,MainActivity.class);
                intentActivity.putExtra("name",editTextName.getText().toString());
                startActivity(intentActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMeteor();
    }

    private void initMeteor() {
        // enable logging of internal events for the library
        Meteor.setLoggingEnabled(true);

        mMeteor = new Meteor(this, "ws://192.168.2.218:3000//websocket", new InMemoryDatabase());

        // register the callback that will handle events and receive messages
       // mMeteor.addCallback(RegisterActivity.this);

        // establish the connection
        mMeteor.connect();
    }
}
