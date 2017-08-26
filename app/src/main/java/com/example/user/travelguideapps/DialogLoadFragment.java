package com.example.user.travelguideapps;

/**
 * Created by User on 6/5/2017.
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.LocationDataAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogLoadFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;


    public interface UserNameListener {
        void onFinishUserDialog(String user);
    }

    // Empty constructor required for DialogFragment

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_dialog_categories, container);
       // mEditText = (EditText) view.findViewById(R.id.username);
       // final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dataRecyclerView);
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

             System.out.println("Whatispost1 " + post);


         }else{
             ArrayList a= new ArrayList();
for(Object key:post.keySet()){
    System.out.println("Whatispost2 " + post);

test.add(key);

}
          //   test.add(a);

             System.out.println("Whatispost3 " + post);
             System.out.println("Whatispost3 " + a);


         }


                //Cannot directly update, findways to reset details,,,,
         //       Locationadapter = new SelectedLocationListRecyclerViewAdapter(getActivity(), MapsActivity
         //               .getWayPointDetailsList());

            //    Locationadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }


        });



        LocationDataAdapter adapter= new LocationDataAdapter(test);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

     //   recyclerView.setAdapter(adapter);

        // set this instance as callback for editor action
    //    mEditText.setOnEditorActionListener(this);
    //    mEditText.requestFocus();
      //  getDialog().getWindow().setSoftInputMode(
     //           WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
     //   getDialog().setTitle("Please enter username");

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Return input text to activity
        UserNameListener activity = (UserNameListener) getActivity();
        activity.onFinishUserDialog(mEditText.getText().toString());
        this.dismiss();
        return true;
    }
}