package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity {

    ListView moodList;
    ArrayAdapter<Mood> moodAdapter;
    ArrayList<Mood> moodDataList;

    String filter="all";
    LinearLayout filterLayout;
    ArrayList<Mood> filterDataList;
    Button filter_button;
    Button filter_angry;
    Button filter_sad;
    Button filter_happy;
    Button filter_all;
    static int chk=0;
    Button map_button;
    FloatingActionButton add_button;
    FloatingActionButton deleteMoodButton;
    FirebaseFirestore db;
    TextView test;
    String TAG = "sample";
    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    Button followed;
    ArrayList<Request> req;

    Button myProfileButton;
    ArrayList<String> mood ;
    ArrayList<String> date ;
    private int moodIndex;
    //Account acc;
    ArrayList<Account> accountList;
    Map<String, Object> m;
    ArrayList<Object> acc;
    Object []mm;
    ArrayList<String>sent;
    ArrayList<String>receive;
    ArrayList<String>message;
    String trr;
    int[] num;
    List<String> emotionList = new ArrayList<>();
    List<String> latitudeList = new ArrayList<>();
    List<String> longitudeList = new ArrayList<>();
    List<String> reasonList = new ArrayList<>();
    List<String> socialList = new ArrayList<>();
    List<String> timeList = new ArrayList<>();

    StringBuilder socialf = new StringBuilder("");
    StringBuilder timef = new StringBuilder("");
    StringBuilder emotionf = new StringBuilder("");
    StringBuilder latitudef = new StringBuilder("");
    StringBuilder longitudef = new StringBuilder("");
    StringBuilder reasonf = new StringBuilder("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent tohome = getIntent();
        final String usernameMain = tohome.getStringExtra("username");
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Account").document(usernameMain);



        filter_button = findViewById(R.id.filter_button);
        filterLayout = findViewById(R.id.filter_layout);
        filter_angry = findViewById(R.id.filter_angry);
        filter_happy = findViewById(R.id.filter_happy);
        filter_sad = findViewById(R.id.filter_sad);
        filter_all = findViewById(R.id.filter_all);

        add_button = findViewById(R.id.button);
        deleteMoodButton= findViewById(R.id.delete_mood_button);
        /*
        db.collection("Account").document(usernameMain).collection("moodHistory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                socialf.append(document.get("socialState"));
                                socialList.add(socialf.toString());
                                socialf.setLength(0);
                                timef.append(document.get("time"));
                                timeList.add(timef.toString());
                                timef.setLength(0);
                                emotionf.append(document.get("emotionState"));
                                emotionList.add(emotionf.toString());
                                emotionf.setLength(0);
                                latitudef.append(document.get("latitude"));
                                latitudeList.add(latitudef.toString());
                                latitudef.setLength(0);
                                longitudef.append(document.get("longitude"));
                                longitudeList.add(latitudef.toString());
                                longitudef.setLength(0);
                                reasonf.append(document.get("reason"));
                                reasonList.add(reasonf.toString());
                                reasonf.setLength(0);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        for (int i = 0; i < emotionList.size(); i++) {
                            System.out.println("******************************************\n");
                            moodDataList.add((new Mood(emotionList.get(i),reasonList.get(i),timeList.get(i),socialList.get(i),usernameMain,latitudeList.get(i),longitudeList.get(i))));
                        }
                        moodAdapter.notifyDataSetChanged();
                    }
                });


         */
        final CollectionReference collectionReference =  db.collection("Account").document(usernameMain).collection("moodHistory");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    moodDataList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String emotionState = (String) doc.getData().get("emotionState");
                        String latitude = (String) doc.getData().get("latitude");
                        String longitude = (String) doc.getData().get("longitude");
                        String reason = (String) doc.getData().get("reason");
                        String socialState = (String) doc.getData().get("socialState");
                        String time = (String) doc.getData().get("time");
                        if(filter.equals("angry")){
                            if (emotionState.equals("angry")){
                                moodDataList.add(new Mood(emotionState, reason, time, socialState, usernameMain, latitude, longitude));
                            }
                        }
                        if(filter.equals("sad")){
                            if (emotionState.equals("sad")){
                                moodDataList.add(new Mood(emotionState, reason, time, socialState, usernameMain, latitude, longitude));
                            }
                        }
                        if(filter.equals("happy")){
                            if (emotionState.equals("happy")){
                                moodDataList.add(new Mood(emotionState, reason, time, socialState, usernameMain, latitude, longitude));
                            }
                        }
                        if(filter.equals("all")){
                            moodDataList.add(new Mood(emotionState, reason, time, socialState, usernameMain, latitude, longitude));
                        }
                    }
                    for (int i =0; i < moodDataList.size(); i++){
                        Collections.sort(moodDataList, new Comparator<Mood>() {
                            public int compare(Mood first, Mood second)  {
                                return second.getTime().compareTo(first.getTime());
                            }
                        });
                    }
                    moodAdapter.notifyDataSetChanged();
                }
            }
        });
        TextView name= findViewById(R.id.uname);
        name.setText(usernameMain);
        map_button = findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (int j = 0; j < moodList.getChildCount(); j++) {
                    moodList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }


                Intent mapIntent  = new Intent(HomePage.this, MyHistotyMoodMap.class );
                Bundle bundle = new Bundle();
                bundle.putSerializable("mood",moodDataList);
                mapIntent.putExtras(bundle);
                startActivity(mapIntent);


            }
        });

        moodList = findViewById(R.id.mood_list);
        moodDataList = new ArrayList<>();

        moodAdapter = new myMoodList(HomePage.this,moodDataList);
        moodList.setAdapter(moodAdapter);

        final Intent edit = new Intent(HomePage.this, EditMoodActivity.class);
        moodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                moodIndex=-1;
                String selectTime = moodDataList.get(position).getTime();
                Bundle extras = new Bundle();
                extras.putString("key",selectTime);
                extras.putString("user",usernameMain);
                edit.putExtras(extras);
                startActivity(edit);
                for (int j = 0; j < moodList.getChildCount(); j++) {
                    moodList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
                return true;
            }
        });

        /*
        // Enable the selection of multiple items
        moodList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Change background of colours of selected rows to indicate which are selected
                for (int i = 0; i < moodList.getChildCount(); i++) {
                    if (moodList.isItemChecked(i)){
                        moodList.getChildAt(i).setBackgroundColor(Color.LTGRAY);
                    } else {
                        moodList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        deleteMoodButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int i;
                SparseBooleanArray selectedMoods = moodList.getCheckedItemPositions();

                for (i = moodList.getCount(); i >= 0; i--){
                    if (selectedMoods.get(i)){
                        db.collection("Account").document(usernameMain).collection("moodHistory").document(moodDataList.get(i).getTime())
                                .delete();
                    }
                }
                moodList.clearChoices();

            }
        });

         */

        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                moodIndex=i;
                for (int j = 0; j < moodList.getChildCount(); j++) {
                    if (j==i){
                        moodList.getChildAt(j).setBackgroundColor(Color.LTGRAY);

                    } else {
                        moodList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        });

        deleteMoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moodIndex!=-1) {
                    db.collection("Account").document(usernameMain).collection("moodHistory").document(moodDataList.get(moodIndex).getTime())
                            .delete();
                    moodIndex = -1;
                }

                moodAdapter.notifyDataSetChanged();
                moodList.setAdapter(moodAdapter);

            }
        });







        /*
        moodList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                db.collection("Account").document(usernameMain).collection("moodHistory").document(moodDataList.get(index).getTime())
                        .delete();
                return true;
            }
        });

         */




        followed = findViewById(R.id.followed_button);
        followed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < moodList.getChildCount(); j++) {
                    moodList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
                Intent intentf = new Intent(HomePage.this,FriendActivity.class);
                intentf.putExtra("user",usernameMain);
                HomePage.this.startActivity(intentf);
            }
        });
        //Switch to AddMoodActivity Activity after click "Add Mood" button
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < moodList.getChildCount(); j++) {
                    moodList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
                Intent intentc = new Intent(HomePage.this, AddMoodActivity.class);
                intentc.putExtra("user",usernameMain);
                HomePage.this.startActivity(intentc);
                moodList.clearChoices();
            }
        });

        /**** My Profile Button ***/
        myProfileButton = findViewById(R.id.profile_button);
        myProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0; j < moodList.getChildCount(); j++) {
                    moodList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
                Intent intentMyProfile = new Intent(HomePage.this, MyProfileActivity.class);
                intentMyProfile.putExtra("usernameMyProfile",usernameMain);
                HomePage.this.startActivity(intentMyProfile);
            }
        });



        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterLayout.setVisibility(View.VISIBLE);
                for (int j = 0; j < moodList.getChildCount(); j++) {
                    moodList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        filter_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filter="sad";
                final Mood moodhistory =new Mood("0","0","0","0","0","0","0");
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").set(moodhistory);
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").delete();
                filter_button.setText("SAD");
                filterLayout.setVisibility(View.INVISIBLE);



            }
        });


        filter_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filter="angry";
                final Mood moodhistory =new Mood("0","0","0","0","0","0","0");
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").set(moodhistory);
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").delete();
                filter_button.setText("ANGRY");
                filterLayout.setVisibility(View.INVISIBLE);

            }
        });
        filter_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filter="happy";
                final Mood moodhistory =new Mood("0","0","0","0","0","0","0");
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").set(moodhistory);
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").delete();
                filter_button.setText("HAPPY");
                filterLayout.setVisibility(View.INVISIBLE);

            }
        });


        filter_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter="all";
                final Mood moodhistory =new Mood("0","0","0","0","0","0","0");
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").set(moodhistory);
                db.collection("Account").document(usernameMain).collection("moodHistory").document("0").delete();
                filter_button.setText("Filter");
                filterLayout.setVisibility(View.INVISIBLE);

            }
        });



    }



}
