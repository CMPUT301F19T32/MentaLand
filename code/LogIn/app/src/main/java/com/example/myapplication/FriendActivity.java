package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity implements AddFriendFrag.OnFragmentInteractionListener{
    ListView moodFriendList;
    ArrayAdapter<Mood> moodFrArrayAdapter;
    ArrayList<Mood> moodFrArrayList;
    private int index;
    private String user;
    private Mood mood;
    ArrayList<String> friendList;
    ArrayList<Request> requestsList;
    String toast1 = "Already Friend";
    String toast2 = "Already Sent Earlier";
    String toast3 = "Already Sent";

    private  Request request;
    //friendList get from fireStore
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Intent toFriend = getIntent();
        user = toFriend.getStringExtra("user");
        moodFriendList = findViewById(R.id.friendLIstVeiw);
        moodFrArrayList = new ArrayList<>();
        friendList = new ArrayList<>();

        //some sample info (delete later)
        final String []moods = {"Happy", "Angry", "Sad", "Sad"};
        String []date = {"2019-11-05", "2018-08-06", "2015-10-17", "2015-09-18"};
        String []username = {"Zhao", "Qian", "Li", "Sun"};
        String []time = {"12:00","11:45","15:20","04:20"};
        String []something = {"1", "2", "3", "4"};
        Geolocation geolocation1;
        Geolocation geolocation2;
        Geolocation geolocation3;
        Geolocation geolocation4;
        double a = 53.484310;
        double b = -113.506133;
        double c =  53.488036;
        double d =  -113.514072;
        double e = 53.486592;
        double f = -113.502130;
        geolocation1 = new Geolocation(a,b);
        geolocation2 = new Geolocation(c,d);
        geolocation3 = new Geolocation(e,f);
        geolocation4 = null;
        Geolocation[]geolocations = {geolocation1,geolocation2,geolocation3,geolocation4};


        for (int i = 00; i < moods.length; i++) {
            moodFrArrayList.add((new Mood(moods[i],something[i],something[i],time[i], date[i],something[i],username[i],geolocations[i])));
        }



        for (int i = 0; i<friendList.size();i++){
            //find keyword = friendList.get(i)
            //find keyword  moodList.get(0)  mood = moodList.get(0)
            //moodFriendList.add()mood
        }


        moodFrArrayAdapter = new CustomeFriendList(this, moodFrArrayList);
        moodFriendList.setAdapter(moodFrArrayAdapter);
        Button requestButton = findViewById(R.id.request);
        final Button map = findViewById(R.id.map_fr);
        Button addFriend = findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = moodFrArrayList.size() + 1;
                new AddFriendFrag(user).show(getSupportFragmentManager(),"addFriend");

            }
        });

        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent mapIntent  = new Intent(FriendActivity.this, FriendMoodMap.class );
                Bundle bundle = new Bundle();
                bundle.putSerializable("mood",moodFrArrayList);
                //Log.e("ff",moodDataList.toString());
                mapIntent.putExtras(bundle);
                startActivity(mapIntent);




            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent requestIntent  = new Intent(FriendActivity.this, RequestActivity.class );
                requestIntent.putExtra("user",user);
                startActivity(requestIntent);
            }
        });
        moodFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mood = moodFrArrayAdapter.getItem(position);
                Intent viewIntent  = new Intent(FriendActivity.this, ViewActivity.class );
                viewIntent.putExtra("username",mood.getUsername());
                viewIntent.putExtra("emotion",mood.getEmotionstr());
                viewIntent.putExtra("reason",mood.getReason());
                viewIntent.putExtra("social",mood.getSocialState());
                startActivity(viewIntent);
                /// add new activity relatide to homepage
            }
        });
    }

    @Override
    public void onOkPress(Request request) {
        /*

        for (int i = 0; i < friendList.size(); i++){
            if (request.getReciveName() == friendList.get(i)){
                Toast.makeText(getApplicationContext(),toast1,Toast.LENGTH_LONG);
                return;
            }
        }
        for (int i = 0; i < requestsList.size(); i++){
            if ((requestsList.get(i).getSentName().toString() == user) &(requestsList.get(i).getReciveName() == request.getReciveName())){
                Toast.makeText(getApplicationContext(),toast2,Toast.LENGTH_LONG);
                return;
            }

        }
        */
        // if not found keyword username toast3
        //Request update to firestore
        final Request requestInner = request;
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Account");

        final DocumentReference ReceiverRef = db.collection("Account").document(request.getReciveName());
        ReceiverRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        ReceiverRef
                                .update("requestList", FieldValue.arrayUnion(requestInner));
                        //Toast.makeText(getActivity(), "request sent successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        //Log.d(TAG, "The user does not exist!");

                        Toast.makeText(getApplicationContext(), "The user does not exist!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                /** else {
                 Log.d(TAG, "Failed with: ", task.getException());
                 } **/

            }
        });


    }


}
