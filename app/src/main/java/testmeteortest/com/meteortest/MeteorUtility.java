package testmeteortest.com.meteortest;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by Kunal on 23-04-2016.
 */
public class MeteorUtility implements MeteorCallback {



    static MeteorUtility meteorUtility;

    public static MeteorUtility getInstance(){
       return meteorUtility == null?meteorUtility = new MeteorUtility():meteorUtility;
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
        System.out.println("Data added to <" + collectionName + "> in document <" + documentID + ">");
        System.out.println("    Added: " + fieldsJson);

        Log.v("TAG", "DataBase information " + collectionName);
    }

    @Override
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        System.out.println("Data changed in <" + collectionName + "> in document <" + documentID + ">");
        System.out.println("    Updated: " + updatedValuesJson);
        System.out.println("    Removed: " + removedValuesJson);
    }

    @Override
    public void onDataRemoved(String collectionName, String documentID) {
        System.out.println("Data removed from <" + collectionName + "> in document <" + documentID + ">");
    }


    @Override
    public void onDisconnect() {
        System.out.println("Disconnected");
    }





}
