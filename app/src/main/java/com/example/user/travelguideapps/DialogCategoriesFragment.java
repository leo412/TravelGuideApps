package com.example.user.travelguideapps;

/**
 * Created by User on 6/5/2017.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.LocationDataAdapter;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.SelectedLocationListRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DialogCategoriesFragment extends Fragment  {

    private EditText mEditText;


    private Context mContext;
    private LayoutInflater mInflater;
    private  ArrayList mDataSource;
    private static List<LinkedHashMap<String,String>> mDataSourceforSend;
    private SelectedLocationListRecyclerViewAdapter Locationadapter;

    private int Position;
    private static RecyclerView.ViewHolder holder2;

    public DialogCategoriesFragment(Context context, ArrayList items) {
        mContext = context;
        Log.d("A", "LocationDataadap(List)"+items);
        //     mDataSourceforSend=items;
        mDataSource = items;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);




    }
    // Empty constructor required for DialogFragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_dialog_categories, container);
       // mEditText = (EditText) view.findViewById(R.id.username);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dataRecyclerView);
        final ArrayList test=new ArrayList();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users").child(LoginActivity.getUserID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap post = (HashMap) dataSnapshot.getValue();
                System.out.println("Whatispostbefore " + post);

         if(post==null){
             test.add("newewt");
             test.add("newewt23");
             test.add("newewt45");

             System.out.println("Whatispost1 " + post);


         }else{
             ArrayList a= new ArrayList();
for(Object key:post.keySet()){

test.add(key);

}




         }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }


        });



        LocationDataAdapter adapter= new LocationDataAdapter(getActivity(),test);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);

        // set this instance as callback for editor action
    //    mEditText.setOnEditorActionListener(this);
    //    mEditText.requestFocus();
      //  getDialog().getWindow().setSoftInputMode(
     //           WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
     //   getDialog().setTitle("Please enter username");

        return view;
    }

}