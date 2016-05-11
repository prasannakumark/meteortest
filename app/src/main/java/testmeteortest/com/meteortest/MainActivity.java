package testmeteortest.com.meteortest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

public class MainActivity extends Activity implements MeteorCallback {

    private List<UserModel> userModelList;
    private MoviesAdapter moviesAdapter;
    private ImageView sendMessageButton;
    private EditText enterEditTextMessage;

    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userModelList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclervview);
        sendMessageButton = (ImageView) findViewById(R.id.send_button);
        enterEditTextMessage = (EditText) findViewById(R.id.message_edittext);
        moviesAdapter = new MoviesAdapter(userModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(moviesAdapter);

        // enable logging of internal events for the library
        Meteor.setLoggingEnabled(true);

        // register the callback that will handle events and receive messages
        RegisterActivity.mMeteor.addCallback(this);

        final Database database = RegisterActivity.mMeteor.getDatabase();

        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                Collection collection = RegisterActivity.mMeteor.getDatabase().getCollection
                        (RegisterActivity.DATABASE_NAME);
                String[] collectionNames = RegisterActivity.mMeteor.getDatabase().getCollectionNames();
                int numCollections = RegisterActivity.mMeteor.getDatabase().count();

                for (int i = 0; i < collection.find().length; i++) {
                    UserModel userModel = new UserModel();
                    Document userDocument = ((Document[]) collection.find())[i];
                    userModel.setName(userDocument.getField("name").toString());
                    userModel.setAddress(userDocument.getField("address").toString());
                    userModel.setId(userDocument.getId().toString());
                    userModelList.add(userModel);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();

                moviesAdapter.notifyDataSetChanged();
            }
        }.execute();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enterEditTextMessage.getText().toString().length() == 0)
                    return;
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Map<String, Object> insertValues = new HashMap<String, Object>();
                insertValues.put("name", getIntent().getExtras().getString("name"));
                insertValues.put("address", enterEditTextMessage.getText().toString());
                RegisterActivity.mMeteor.insert(RegisterActivity.DATABASE_NAME, insertValues, new ResultListener() {
                    @Override
                    public void onSuccess(String result) {
                        progressDialog.dismiss();
                        enterEditTextMessage.setText("");
                    }

                    @Override
                    public void onError(String error, String reason, String details) {
                        progressDialog.dismiss();
                        enterEditTextMessage.setText("");
                        Toast.makeText(MainActivity.this, "error " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onConnect(boolean signedInAutomatically) {

        System.out.println("Connected");
    }

    @Override
    public void onException(Exception e) {
        System.out.println("Exception");
        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataAdded(String collectionName, String documentID, String fieldsJson) {
        Document userDocument = RegisterActivity.mMeteor.getDatabase().getCollection(collectionName).getDocument(documentID);
        UserModel userModel = new UserModel();
        userModel.setName(userDocument.getField("name").toString());
        userModel.setAddress(userDocument.getField("address").toString());
        userModel.setId(userDocument.getId().toString());
        userModelList.add(userModel);
        moviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        System.out.println("Data changed in <" + collectionName + "> in document <" + documentID + ">");
        System.out.println("    Updated: " + updatedValuesJson);
        System.out.println("    Removed: " + removedValuesJson);
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {
        for (int i = 0; i < userModelList.size(); i++) {
            if (userModelList.get(i).getId().equals(documentID)) {
                userModelList.remove(i);
                break;
            }
        }
        moviesAdapter.notifyDataSetChanged();
        Toast.makeText(this, "One record is deleted" + documentID, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDisconnect() {
        System.out.println("Disconnected");
    }

    @Override
    public void onDestroy() {
        // RegisterActivity.mMeteor.disconnect();
        //RegisterActivity.mMeteor.removeCallback(this);
        super.onDestroy();
    }
}