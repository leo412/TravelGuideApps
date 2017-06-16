package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.travelguideapps.LocationDetailsActivity;
import com.example.user.travelguideapps.R;

import java.util.LinkedHashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static LinkedHashMap<String, String> placedetail;
    private String mParam2;
    private static TextView place_address;
    private static TextView place_phone;
    private static TextView place_website;



    Button setplacebutton;
     Button removeplacebutton;
    private OnFragmentInteractionListener mListener;

    public LocationDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    // * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationDetails newInstance(List<LinkedHashMap<String, String>> placedetails, String param2) {
        LocationDetails fragment = new LocationDetails();
        placedetail=placedetails.get(0);
        Log.d(TAG, "Yeeeeeeeppp"+placedetails);


     //   Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
     //   args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
       //     mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_location_details, container, false);
        place_address=(TextView)view.findViewById(R.id.textViewAdress);
        place_address.setText( placedetail.get("formatted_address"));
        place_phone=(TextView)view.findViewById(R.id.textViewPhoneNumber);
        place_phone.setText( placedetail.get("formatted_phone_number"));
        place_website=(TextView)view.findViewById(R.id.textViewWebsite);
        place_website.setText( placedetail.get("website"));



        //View mainview=inflater.inflate(R.layout.content_place_details, container, false);

   //     setplacebutton=(Button) mainview.findViewById(R.id.addLocation);
     //   removeplacebutton=(Button) mainview.findViewById(R.id.removeLocation);
        removeplacebutton= LocationDetailsActivity.removeplacebutton;

        setplacebutton= LocationDetailsActivity.setplacebutton;
        setplacebutton.setEnabled(true);

        removeplacebutton.setEnabled(true);
        return view ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {


        super.onAttach(context);



//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
