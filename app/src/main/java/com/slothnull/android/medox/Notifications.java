package com.slothnull.android.medox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Notifications extends Activity {

    private static final String TAG = "Notifications";
    public ListView notificationList;
    public ArrayAdapter arrayAdapter;
    public List<String> arrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationList = (ListView)findViewById(R.id.notificationList);
        refreshList();
        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), arrayList.get(position), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void refreshList(){
        String name;
        String message;
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        ValueEventListener notificationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Notification notification = dataSnapshot.getValue(Notification.class);
                if (notification != null) {
                    arrayList.add( notification.time  +":  "  + notification.title + "\n" + notification.message);
                    // ...
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(UID).child("notification")
                .addValueEventListener(notificationListener);
        //add to list here


        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        notificationList.setAdapter(arrayAdapter);
    }
}
