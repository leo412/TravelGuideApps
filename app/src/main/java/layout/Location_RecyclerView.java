package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.travelguideapps.LocationListRecyclerViewAdapter;
import com.example.user.travelguideapps.MapsActivity;
import com.example.user.travelguideapps.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Location_RecyclerView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Location_RecyclerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Location_RecyclerView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "HI";
    private static final String ARG_PARAM2 = "First";
    private  View fragmentpoilist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LocationListRecyclerViewAdapter Locationadapter;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    public Location_RecyclerView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Location_RecyclerView.
     */
    // TODO: Rename and change types and number of parameters
    public static Location_RecyclerView newInstance(String param1, String param2) {
        Location_RecyclerView fragment = new Location_RecyclerView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);






        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location__recycler_view, container, false);
   //     TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
      //  tvLabel.setText(page + " -- " + title);

        List<LinkedHashMap<String, String>> nearbyPlacesList= MapsActivity.getNearbyPlacesList();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView)  view.findViewById(R.id.poilistRecyclerView);
        //When button is clicked, recycler view is called and nearbyplaceslist is being inserted into the system
        Locationadapter = new LocationListRecyclerViewAdapter(getActivity(), nearbyPlacesList);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.setAdapter(Locationadapter);
        Locationadapter.notifyDataSetChanged();


     //   view = (ConstraintLayout)  inflater.inflate(R.layout.activity_maps, container, false);

        final ExpandableLayout expandableLayout
                = (ExpandableLayout) MapsActivity.view.findViewById(R.id.expandable_layout);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);




            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                int pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                if(dx>0)
                {


                    System.out.println("Scrolled Right");

                }
                else if(dx < 0)
                {
                    System.out.println("Scrolled Left");

                }
                else {

                    System.out.println("No Horizontal Scrolled");
                }

                if(dy>0)
                {
                    expandableLayout.collapse();

                    System.out.println("Scrolled Downwards");
                }
//                else if(dy < 0)
//                {
//                    System.out.println("Scrolled Upwards");
//                    expandableLayout.expand();
//
//                }
                else   if (pastVisibleItems  == 0) {

                //    expandableLayout.expand();

                }

                  
                else {

                    System.out.println("No Vertical Scrolled");
                }
            }




        });









        return view;
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
