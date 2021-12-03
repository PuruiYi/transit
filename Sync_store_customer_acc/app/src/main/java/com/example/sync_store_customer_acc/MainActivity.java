package com.example.sync_store_customer_acc;

import static java.sql.DriverManager.println;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ListView storeListView;


    //refer condition in database to TextView storeListCondition
    //DatabaseReference storeRootRef = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference storeConditionRef = storeRootRef.child("stores");

    public static final String default_text = "No available stores found";
    public static final String storeKey = "name";
    public static final String ownerKey = "owner";
    public static final String addressKey = "address";
    public static final String TAG = "stores";
    private List<HashMap<String,String>> stores = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter sa;
    private CollectionReference mColRef = FirebaseFirestore.getInstance().collection("stores");

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //storeListView = (ListView)findViewById(R.id.storeListView);


        //Use an Adapter to link data to Views
        sa = new SimpleAdapter(this, stores,
                R.layout.list_format,
                new String[] { "line 1","line 2","line 3" },
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c});
    }


    @Override
    protected void onStart() {
        super.onStart();
        //getMultipleDocs();
        listeningChanges();
    }


    public void listeningChanges() {
        db.collection("stores")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (stores.size() != 0)
                            stores.clear();


                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("name") != null || doc.get("owner") != null || doc.get("address") != null) {

                                HashMap<String, String> temp;
                                temp = new HashMap<String, String>();
                                temp.put("line 1", doc.getString(storeKey));
                                temp.put("line 2", doc.getString(ownerKey));
                                temp.put("line 3", doc.getString(addressKey));
                                doc.getData();

                                stores.add(temp);
                                ((ListView) findViewById(R.id.storeListView)).setAdapter(sa);

                            }
                        }

                    }
                });
    }
/*
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    String storeText = documentSnapshot.getString(storeKey);
                    String ownerText = documentSnapshot.getString(ownerKey);
                    String addressText = documentSnapshot.getString(addressKey);

                    documentSnapshot.getData();
                    storeListTextView.setText(storeText + ownerText + addressText);
                } else if (error != null) {
                    Log.w(TAG, "Got an exception!", error);
                }
            }
        });
    }
*/
    public void getMultipleDocs() {
        db.collection("stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String,String> temp;
                                temp = new HashMap<String,String>();
                                //String storeText = document.getString(storeKey);
                                //String ownerText = document.getString(ownerKey);
                                //String addressText = document.getString(addressKey);
                                temp.put("line 1", document.getString(storeKey));
                                temp.put("line 2", document.getString(ownerKey));
                                temp.put("line 3", document.getString(addressKey));
                                document.getData();
                                stores.add(temp);
                                ((ListView)findViewById(R.id.storeListView)).setAdapter(sa);

                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });
        }

        public void view_item(View view){
            Intent intent = new Intent(this, DisplayItemActivity.class);
            startActivity(intent);
        }
}






