package com.example.user.travelguideapps.LocationDetails;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.DatePicker.PickerDialogFragment;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Time_Selection_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Time_Selection_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Time_Selection_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static LinkedHashMap<String, Object> placedetail;
    private static LinkedHashMap<String, Object> otherplacedetail;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Time_Selection_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //* @param param1 Parameter 1.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment Time_Selection_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Time_Selection_Fragment newInstance(ArrayList<LinkedHashMap<String, Object>> placedetails, String param2) {
        Time_Selection_Fragment fragment = new Time_Selection_Fragment();

        placedetail = placedetails.get(0);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public static TextView selectedDateText;
    public static TextView selectedStartTime;
    public static TextView selectedEndTimeText;
    public static TextView selectedduration;
    public static TextView timetext;
    public static TextView endingtime;
    public static TextView selecteddurationtext;
    public static ImageView emptyimage;
    public static ImageView emptyclockimage;
    public static TextView emptytext;
    public static ImageButton resetbutton;
    public static ImageButton setstarttimebutton;
    public static ImageButton setdurationbutton;
    private static final String TAG = "Time_Selection_Fragment";


    public static long timeduration;
    public static boolean dialogboolean = false;

    String final_data;
    int finalNumber;
    HashMap waypointwithdatedup;
    ArrayList<HashMap> waypointWithDateListDup;
    PickerAdapter adapter;
    int finalNumber1;
    ArrayList selecteddata = new ArrayList();
    boolean executesave = false;
    LinkedHashMap waypointwithdateafter = new LinkedHashMap();

    public static void setDuration(long duration) {
        Log.d(TAG, "Set Duration");

        timeduration = duration;

    }

    public static void setPositive(boolean b) {
//TO allow separating betwwenn cancel and ok in pickerdialgo
        dialogboolean = b;
        Log.d(TAG, "Set Positive");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");

        dialogboolean = false;

        View view = inflater.inflate(R.layout.fragment_location_time_selected, container, false);
        HashMap waypointwithdate = new LinkedHashMap();
        int number = 0;
        selecteddurationtext = (TextView) view.findViewById(R.id.timedurationtext);
        emptyimage = (ImageView) view.findViewById(R.id.empty_clock);
        emptytext = (TextView) view.findViewById(R.id.empty_time_text);
        selectedStartTime = (TextView) view.findViewById(R.id.SelectedTimeFrom);
        selectedduration = (TextView) view.findViewById(R.id.timeduration);
        endingtime = (TextView) view.findViewById(R.id.endingtime);
        setstarttimebutton = (ImageButton) view.findViewById(R.id.selectstarttimebutton);
        setdurationbutton = (ImageButton) view.findViewById(R.id.setdurationbutton);
        timetext = (TextView) view.findViewById(R.id.time_textview);
        selectedDateText = (TextView) view.findViewById(R.id.SelectedDateText);
        selectedEndTimeText = (TextView) view.findViewById(R.id.endingtimetext);
        resetbutton = (ImageButton) view.findViewById(R.id.resetdatetimebutton);
        emptyclockimage = (ImageView) view.findViewById(R.id.empty_clock);


        //Array List of ArrayList with id and timestamp
        final ArrayList<HashMap> waypointwithdatelist = MapsActivity.getWaypointwithDateList();
        waypointWithDateListDup = waypointwithdatelist;
        Picasso.with(getActivity()).load(R.drawable.time_question_clock_icon).fit().into
                (emptyclockimage);


        //situation: nothing
        //situation: has something, with same place_id  (break when found)

        if (waypointwithdatelist.size() != 0) {
            //if got things
            Log.d(TAG, " if (waypointwithdatelist.size() != 0) {\n");

            for (int i = 0; i < waypointwithdatelist.size(); i++) {

                Log.d(TAG, " for (int i = 0; i < waypointwithdatelist.size(); i++) {\n");
                Log.d(TAG, " waypointwithdatelist" + waypointwithdatelist);


//to get the "saved" data and display it when showing in the view
                //same id : remove image, show the

                if (waypointwithdatelist.get(i).get("place_id").equals(placedetail.get("place_id"))){
//The single placeid, date for 1 location
                    waypointwithdate = waypointwithdatelist.get(i);
                    Log.d(TAG, " waypointwithdate" + waypointwithdate);

                    //Remove picture
                    emptyimage.setVisibility(View.GONE);
                    emptytext.setVisibility(View.GONE);
                    Log.d("haha", "selectedStartTime" + selectedStartTime.getText());


                    //Got Dates, so show the time
                    if ((long) waypointwithdatelist.get(i).get("starttime") != 0) {
                        Log.d("haha", "booooooominsid" + waypointwithdatelist);

                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

                        long timestamp = Long.parseLong(waypointwithdatelist.get(i).get("starttime").toString()) * 1000;
                        Date d = new Date(timestamp);
                        String s = outputFormat.format(d);
                        Log.d("haha", "booooooominsid" + s);

                        selectedStartTime.setText(s);

                    }

//Got duration, so shows
                    if ((long) waypointwithdatelist.get(i).get("duration") != 0) {
                        long timestamp = Long.parseLong(waypointwithdatelist.get(i).get("duration").toString());
                        // timestamp = timestamp / 1000;
                        Date d = new Date(timestamp);
                        long minutes = timestamp % 3600 / 60;
                        long hours = timestamp % 86400 / 3600;
                        long days = timestamp / 86400;

                        selectedduration.setText(days + "days and " + hours + " hours and " + minutes + " minutes");

                        long endtime = Long.parseLong(waypointwithdatelist.get(i).get("starttime").toString()) * 1000 + timestamp * 1000;
                      //  Log.d("haha", "craps" + Long.parseLong(waypointwithdatelist.get(i).get(1).toString()));
                        Log.d("haha", "craps" + endtime);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        Date date = new Date(endtime);
                        String s = outputFormat.format(date);
                        endingtime.setText(s);
                        ;


                    }


                    if (selectedStartTime.getText().equals("N/A")) {
                        Log.d("haha", "selectedStartTime1st" + selectedStartTime.getText());

                        setdurationbutton.setEnabled(false);
                    } else {
                        Log.d("haha", "selectedStartTime2nd" + selectedStartTime.getText());

                        setdurationbutton.setEnabled(true);

                    }

                    setstarttimebutton.setOnClickListener(onClickListener);
                    setdurationbutton.setOnClickListener(onClickListener);
                    number = i;
                    resetbutton.setVisibility(View.VISIBLE);

                    Log.d("haha", "atleastiknow" + waypointwithdatelist);
                    Log.d("Locationselected", "justchecking breaking ");
                    selecteddurationtext.setVisibility(View.VISIBLE);
                    selectedStartTime.setVisibility(View.VISIBLE);
                    selectedduration.setVisibility(View.VISIBLE);
                    endingtime.setVisibility(View.VISIBLE);
                    setstarttimebutton.setVisibility(View.VISIBLE);
                    setdurationbutton.setVisibility(View.VISIBLE);
                    selectedDateText.setVisibility(View.VISIBLE);
                    selectedEndTimeText.setVisibility(View.VISIBLE);
                    break;
                } else {
                    Log.d("Locationselected", "justchecking inside first else ");
                    selecteddurationtext.setVisibility(View.GONE);
                    selectedStartTime.setVisibility(View.GONE);
                    selectedduration.setVisibility(View.GONE);
                    endingtime.setVisibility(View.GONE);
                    setstarttimebutton.setVisibility(View.GONE);
                    setdurationbutton.setVisibility(View.GONE);
                    selectedDateText.setVisibility(View.GONE);
                    selectedEndTimeText.setVisibility(View.GONE);
                    resetbutton.setVisibility(View.GONE);

                }
            }
        } else {
            Log.d("Locationselected", "justchecking second else ");
            resetbutton.setVisibility(View.GONE);

            selecteddurationtext.setVisibility(View.GONE);
            selectedStartTime.setVisibility(View.GONE);
            selectedduration.setVisibility(View.GONE);
            endingtime.setVisibility(View.GONE);
            setstarttimebutton.setVisibility(View.GONE);
            setdurationbutton.setVisibility(View.GONE);
            selectedDateText.setVisibility(View.GONE);
            selectedEndTimeText.setVisibility(View.GONE);
        }
        Log.d("Locationselected", "justchecking after everything ");
        Picasso.with(getActivity()).load(R.drawable.editicon).fit().into
                (setdurationbutton);
        Picasso.with(getActivity()).load(R.drawable.editicon).fit().into
                (setstarttimebutton);
        Picasso.with(getActivity()).load(R.drawable.reset_icon).fit().into
                (resetbutton);

        //WTF?
        //final_data = _data;
        finalNumber = number;

        //Note: waypointwithdate = getWaypointwithdatelist but only single id
        waypointwithdatedup = waypointwithdate;
        finalNumber1 = number;
        //selecteddata = waypointwithdatedup;
        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MapsActivity.removeDate(placedetail.get("place_id").toString());
                //refresh (working)
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(Time_Selection_Fragment.this).attach(Time_Selection_Fragment.this).commit();


            }
        });
        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {


        @Override
        public void onClick(final View v) {
            dialogboolean = false;

            final String[] SelectedDate = new String[1];
            // DataHolderClass.getInstance().setDistributor_id(place_id);
            Calendar now = Calendar.getInstance();
//Select the day you wanna go on, set time ?
            //Set time now?
            final String[] SelectedTime = new String[1];
            final String[] StartSelectedTime = new String[1];
            final Calendar cal = Calendar.getInstance();
            final long[] todaytimstamp = new long[1];
            final long[] selectedtimestamp = new long[1];
            final PickerDialogFragment picker = new PickerDialogFragment();

            long previoustime = 0;
            //NOTE: NOT selected is frm insode
            //waypointwithdatedup is the [id, time, duration] for WHAT you have clicked
            if ((long) waypointwithdatedup.get("starttime") != 0) {
                Log.d("Checking", "Locationtimeselec sssssstimestamo" + "Startrunnign");
//
                previoustime = Long.parseLong(waypointwithdatedup.get("starttime").toString());
            }
            String dateused = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new java.util.Date(previoustime));
            if (previoustime == 0) {

                dateused = "0";
            }
            Log.d("Checking", "checkdateused " + dateused);

            Log.d("Checking", "nowchecking " + waypointwithdatedup);
            waypointwithdateafter = new LinkedHashMap(waypointwithdatedup);
            //        Collections.copy(waypointwithdatedup,waypointwithdateafter);
            Log.d("Checking", "nowchecking " + waypointwithdateafter);


            final TimePickerDialog tpdStart = TimePickerDialog.newInstance(
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePickerDialog view, int hour, int minute, int second) {
                            //    String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                            //    String SelectedDateText=dayOfMonth+" "+monthOfYear+" "+year;
                            String hourText;
                            String minuteText;
                            if (String.valueOf(hour).length() == 1) {

                                hourText = "0" + hour;
                            } else {
                                hourText = Integer.toString(hour);
                            }
                            if (String.valueOf(minute).length() == 1) {

                                minuteText = "0" + minute;
                            } else {

                                minuteText = Integer.toString(minute);

                            }

                            String SelectedTimeText = " " + hourText + ":" + minuteText;

                            StartSelectedTime[0] = SelectedTimeText;
                            try {
                                SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy");
                                SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm");

                                SimpleDateFormat datetimeformat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

                                //TODO: add one more for length of time ?
                                Date selectedStarttime = datetimeformat.parse(SelectedDate[0] + " " + StartSelectedTime[0]);

                                long selectedtimestamptime = selectedStarttime.getTime() / 1000;

                                Date selecteddate = dateformat.parse(SelectedDate[0]);
                                Object[] object = new Object[3];
                                //This is for inserting place id  (final arraydata should be previous data (yup final array is previoys
                                Log.d("qqqqqqqqqqqq", "finalarraydata" + selectedtimestamptime);
                                //  selecteddata.add(selectedtimestamptime);
                                waypointwithdateafter.put("starttime", selectedtimestamptime);

//                                if (selecteddata.size() == 1) {
//                                    selecteddata.add(selectedtimestamptime);
//
//                                } else {
//                                    selecteddata.set(1, selectedtimestamptime);
//
//                                }
                                waypointwithdateafter.put("duration", waypointwithdatedup.get("duration"));

//                                if (selecteddata.size() == 2) {
//                                    selecteddata.add("Not Selected");
//
//                                } else {
//                                    // selecteddata.add("Not Selected");
//                                    selecteddata.set(2, waypointwithdatedup.get(2));
//
//                                }


                                Log.d("haha", "dafaqselect2" + MapsActivity.getWaypointwithDateList());

                                Log.d("qqqqqqqqqqqq", "ZXCfuc" + selecteddata);

                                Log.d("qqqqqqqqqqqq", "ZXC" + waypointWithDateListDup);

                                //I have selecteddata contain id, two different times (new)
                                //need to run through all data (excepet same place id

                                //For every single location/times.. do checking
                                checkIfOpen();
                                //    if (checkIfOpen()) {
                                if (checkoverlap()) {
                                    Log.d("Checking", "enteraftercheckoverlap" + waypointwithdateafter);

                                    DataHolderClass.getInstance2().setDistributor_id2("unselected");
                                    MapsActivity.changeWaypointwithDateList(waypointwithdateafter);


                                    FragmentManager fragmentManager = Time_Selection_Fragment.this.getActivity().getSupportFragmentManager();
                                    //To refresh the time
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.detach(Time_Selection_Fragment.this).attach(Time_Selection_Fragment.this).commit();


                                } else {
                                    //selecteddata = new ArrayList();
                                }
                                //   }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            adapter = new PickerAdapter(getFragmentManager());


                            //     dateTextView.setText(date);
                        }
                    },
                    now.get(Calendar.HOUR),
                    now.get(Calendar.MINUTE),
                    false
            );

            final String finalDateused = dateused;
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            //     String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                            // SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            String dayText;
                            String monthText;
                            boolean executesave = true;


                            if (String.valueOf(dayOfMonth).length() == 1) {
                                dayText = "0" + dayOfMonth;
                            } else {
                                dayText = Integer.toString(dayOfMonth);
                            }
                            if (String.valueOf(monthOfYear).length() == 1) {

                                monthText = "0" + (monthOfYear + 1);
                            } else {

                                monthText = Integer.toString(monthOfYear);

                            }
//TODO: selected places not showing pictutessL
//TODO: just reset the duration to zero for easier time?


                            String SelectedDateText = monthText + "-" + dayText + "-" + year;

                            SelectedDate[0] = SelectedDateText;

                            SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy");

                            Date selecteddate = null;
                            try {
                                //TODO:Testing
                                selecteddate = dateformat.parse(SelectedDateText);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            selectedtimestamp[0] = selecteddate.getTime();
                            //Wihtout milliseconds....
                            //    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();
                            //get today date
                            String today = dateformat.format(date);

                            Date todaydate = new Date();
                            try {

                                todaydate = dateformat.parse(today);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            todaytimstamp[0] = todaydate.getTime();

                            Log.d("Checking", "Locationtimeselec ssssss" + todaytimstamp[0]);
                            Log.d("Checking", "Locationtimeselec ssssss" + selectedtimestamp[0]);
                            Log.d("Checking", "Locationtimeselec sssssstimestamo" + finalDateused);

                            Timepoint a = new Timepoint(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
//                            Timepoint b = new Timepoint(Integer.parseInt(dateused.substring(11, 13)), Integer.parseInt(dateused.substring(14, 16)),
//                                    0);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                            //  Date currentdate = new Date(System.currentTimeMillis());
                            // currentdate.getHours();

                            String s = outputFormat.format(System.currentTimeMillis());
                            // endingtime.setText(s);

                            //TODO: 24/12 hours format?
                            //TODO: Get all the waypoint, Convert timestamp to date(maybe no need just do between) and say overlapping.
                            Log.d("Checking", "Locationtimeselec timepointold   " + a);

                            Log.d("Checking", "Locationtimeselec timepoint" + s);

                            if (todaytimstamp[0] == selectedtimestamp[0]) {
//TODO: can set to settimeinterval through settings?
                                //Set mintime now...
                                if (v.getId() == R.id.selectstarttimebutton) {

                                    tpdStart.setMinTime(a);

                                }


                            } else {

                                if (v.getId() == R.id.selectstarttimebutton) {

                                }
                            }
                            //neeeeeeeeeeeeeeeeeeeeeeeee------------------------------------------wwwwwwwwwwwwwwwwwww

                            tpdStart.show(getActivity().getFragmentManager(), "Datepickerdialog");

                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );


            dpd.setTitle("Date for visiting" + Calendar.HOUR);
            if (v.getId() == R.id.selectstarttimebutton) {
                //TODO: just reset the "Select end time?"

                dpd.setMinDate(cal);
            } else {
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date startDate = (Date) formatter.parse(dateused);
                    Calendar calen = Calendar.getInstance();
                    calen.setTime(startDate);
                    dpd.setMinDate(calen);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
//Well this works...
            //     tpdStart.setSelectableTimes([a]);

//            DateTime dt = new DateTime();  // current time
//
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//          Date b=  sdf.format(cal.getTime());


            tpdStart.setTitle("Starting Time");
            //tpdend.setTitle("Ending Time");


            if (v.getId() == R.id.selectstarttimebutton) {

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            } else {
                picker.show(getActivity().getFragmentManager(), "dialog");
                getActivity().getFragmentManager().executePendingTransactions();

            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            //         newFragment.setArguments(args);

            picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Log.d("Checking", "insideondismiss" + waypointwithdateafter + waypointwithdateafter.size());
                    //  waypointwithdateafter.add(selecteddata.get(0));

                    if (dialogboolean) {
                        //   Toast.makeText(getActivity().getApplicationContext(), "MEH" + timeduration, Toast.LENGTH_SHORT).show();
//TODO: the time will reset to 0 if no new item is added..
                        DataHolderClass.getInstance2().setDistributor_id2("unselected");
                        //Problem is selected data...

                        //     if (selecteddata.size() == 1) {
                        Log.d("Checking", "enteraftercheckoverlap before first " + MapsActivity.getWaypointwithDateList());


//                        waypointwithdateafter.add(waypointwithdatedup.get(1));
//                        waypointwithdateafter.add(timeduration);

                        //   } else {
                        //TODO:is it place_id or starttime
                        waypointwithdateafter.put("starttime", waypointwithdatedup.get("starttime"));
                        waypointwithdateafter.put("duration", timeduration);
                        //    }
                        Log.d("Checking", "enteraftercheckoverlap before " + waypointwithdateafter + waypointwithdateafter.size());
                        Log.d("Checking", "enteraftercheckoverlap before 2 " + MapsActivity.getWaypointwithDateList());

                        //      selecteddata.set(1, waypointwithdatedup.get(1));

//OH it run the first date....
                        checkIfOpen();
                        // if (checkIfOpen()) {
                        if (checkoverlap()) {
                            Log.d("Checking", "enteraftercheckoverlap after" + executesave + waypointwithdateafter);

                            MapsActivity.changeWaypointwithDateList(waypointwithdateafter);


                            FragmentManager fragmentManager = Time_Selection_Fragment.this.getActivity().getSupportFragmentManager();
                            //To refresh the time
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(Time_Selection_Fragment.this).attach(Time_Selection_Fragment.this).commit();
                        } else {
                            timeduration = 0;
                            //          waypointwithdateafter.set(2, waypointwithdatedup.get(2));

                            //  selecteddata = new ArrayList();
                        }
                        //     }
                        // checkoverlap();
                        Log.d("Checking", "Locationtimeselec sssssstimestamo" + timeduration);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Duration Not Set", Toast.LENGTH_SHORT).show();


                    }
                    Log.d("haha", "getWaypointwithDateListcheckdataafter dismiss " + MapsActivity.getWaypointwithDateList());

                }


            });
//selecteddata.clear();
        }
    };

    public boolean checkIfOpen() {
        boolean checkisopened = true;

        LinkedHashMap<String, Object> detail = MapsActivity.getWayPointDetails(waypointwithdateafter.get("place_id").toString());

        // arr=(ArrayList)detail.get("opening_hours");
        //ArrayList  arr= (ArrayList) detail.get("opening_hours");
        JSONArray jsonArray;
        try {
            //

            String starttime = new java.text.SimpleDateFormat("EEE HHmm").format(new java.util.Date(Long.parseLong(waypointwithdateafter.get
                    ("starttime")
                    .toString()) * 1000));
            String endtime = new java.text.SimpleDateFormat("EEE HHmm").format(new java.util.Date(((long) waypointwithdateafter.get("starttime")
                    + (long) waypointwithdateafter.get("duration")) * 1000));
            //NOTE: THE selected start, end
            Log.d("LocationTimeselected", "startendtime" + waypointwithdateafter.get("starttime"));

            Log.d("LocationTimeselected", "startendtime" + starttime + endtime);
            int startday = 0;
            int endday = 0;
//
            switch (starttime.substring(0, 3)) {
                case "Mon":
                    startday = 1;
                    break;
                case "Tue":
                    startday = 2;
                    break;
                case "Wed":
                    startday = 3;
                    break;
                case "Thu":
                    startday = 4;
                    break;
                case "Fri":
                    startday = 5;
                    break;
                case "Sat":
                    startday = 6;
                    break;
                case "Sun":
                    startday = 0;
                    break;


            }
            switch (endtime.substring(0, 3)) {
                case "Mon":
                    endday = 1;
                    break;
                case "Tue":
                    endday = 2;
                    break;
                case "Wed":
                    endday = 3;
                    break;
                case "Thu":
                    endday = 4;
                    break;
                case "Fri":
                    endday = 5;
                    break;
                case "Sat":
                    endday = 6;
                    break;
                case "Sun":
                    endday = 0;
                    break;


            }
            ArrayList newStartArray = new ArrayList();


            jsonArray = new JSONArray(detail.get("opening_hours").toString());
            Log.d("LocationTimeselected", "checkIfOpen daysssss" + startday + endday);

            Log.d("LocationTimeselected", "checkIfOpen json" + jsonArray);
            JSONObject startjs = (JSONObject) jsonArray.get(startday);
            JSONObject endjs = (JSONObject) jsonArray.get(endday);
            Log.d("LocationTimeselected", "checkIfOpen jsonobject" + startjs + endjs);

//now got time, days.

            Log.d("LocationTimeselected", "checkIfOpen json" + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {


                Log.d("LocationTimeselected", "hasdaypls json" + jsonArray);
                if (jsonArray.getJSONObject(i).getJSONObject("open").get("day") == "0" && jsonArray.getJSONObject(i).getJSONObject("open").get
                        ("time")
                        == "0000" && jsonArray.length() == 1) {
//OPEN EVERYDAY 24 HOURS
                    break;
                } else {


                    newStartArray.add(jsonArray.getJSONObject(i).getJSONObject("open").get("day"));
                    newStartArray.add(jsonArray.getJSONObject(i).getJSONObject("open").get("time"));
                    newStartArray.add(jsonArray.getJSONObject(i).getJSONObject("close").get("time"));
//Make a new array,
                }


            }

            Log.d("LocationTimeselected", "newStartArray" + newStartArray.size());

//nextstart arrat got all data.....
            SimpleDateFormat timeformat = new SimpleDateFormat("HHmm");
            SimpleDateFormat timeformat2 = new SimpleDateFormat("HH:mm");
//TODO: ........ if both selected landed inside the "Period", it will not call
            for (int i = 0; i < newStartArray.size(); i = i + 3) {

                Log.d("LocationTimeselected", "hasdaypls   start");

                Log.d("LocationTimeselected", "hasdaypls thisone" + starttime.substring(4) + "<" + newStartArray.get(i + 1));
                Log.d("LocationTimeselected", "hasdaypls thisone" + endtime.substring(4) + ">" + newStartArray.get(i + 2));
                Log.d("LocationTimeselected", "hasdaypls thisone" + starttime.substring(4) + ">" + newStartArray.get(i + 1));
                Log.d("LocationTimeselected", "hasdaypls thisone" + endtime.substring(4) + "<" + newStartArray.get(i + 2));

//TODO: Problem for time that start 12:00 am -> etc.... between first day and second daty
                Log.d("LocationTimeselected", "hasdaypls   end");

                if (startday == (int) newStartArray.get(i)) {
                    //The selected> the opening

                    if (Integer.parseInt(starttime.substring(4)) < Integer.parseInt(newStartArray.get(i + 1).toString())) {
                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\n Start Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 1).toString())) + "\nYour Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(starttime.substring(4).toString())));
//
                        Log.d("LocationTimeselected", "hasdaypls" + "1");
                        break;

                    }
                    if (Integer.parseInt(endtime.substring(4)) > Integer.parseInt(newStartArray.get(i + 2).toString())) {


                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\n End Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 2).toString())) + "\nYour End Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(endtime.substring(4).toString())));




                        break;
////
                    }
                    if (Integer.parseInt(starttime.substring(4)) > Integer.parseInt(newStartArray.get(i + 2).toString())) {
                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\n Start Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 2).toString())) + "\nYour Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(starttime.substring(4).toString())));
                        Log.d("LocationTimeselected", "hasdaypls" + "1");
                        break;

                    }
                    if (Integer.parseInt(endtime.substring(4)) < Integer.parseInt(newStartArray.get(i + 1).toString())) {
                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\n End Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 1).toString())) + "\nYour End Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(endtime.substring(4).toString())));
                        Log.d("LocationTimeselected", "hasdaypls  2222" + "Success i gueess?");
                        break;

                    }


                }
                if (endday == (int) newStartArray.get(i)) {
                    //The selected> the opening

                    if (Integer.parseInt(starttime.substring(4)) < Integer.parseInt(newStartArray.get(i + 1).toString())) {
                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\n Start Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 1).toString())) + "\nYour Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(starttime.substring(4).toString())));
                        break;

                    }
                    if (Integer.parseInt(endtime.substring(4)) > Integer.parseInt(newStartArray.get(i + 2).toString())) {
                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\n End Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 2).toString())) + "\nYour End Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(endtime.substring(4).toString())));
                        Log.d("LocationTimeselected", "hasdaypls    4444" + "Success i gueess?");
                        break;

                    }
                    if (Integer.parseInt(starttime.substring(4)) > Integer.parseInt(newStartArray.get(i + 2).toString())) {
                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\nStart Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 2).toString())) + "\nYour Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(starttime.substring(4).toString())));
                        Log.d("LocationTimeselected", "hasdaypls" + "1");
                        break;

                    }
                    if (Integer.parseInt(endtime.substring(4)) < Integer.parseInt(newStartArray.get(i + 1).toString())) {
                        checkisopened = showSelectionDialog(getActivity(), "The location is not opened at the time selected!\n End Time: " +
                                " " + timeformat2.format(timeformat.parse(newStartArray.get(i + 1).toString())) + "\nYour End Selected Time: " +
                                "" + timeformat2.format(timeformat.parse(endtime.substring(4).toString())));
                        Log.d("LocationTimeselected", "hasdaypls  2222" + "Success i gueess?");
                        break;

                    }
                }

            }


            Log.d("LocationTimeselected", "finaljson" + newStartArray);

            Log.d("LocationTimeselected", "finaljson" + jsonArray);


            JSONObject openjs = (JSONObject) startjs.get("open");
            JSONObject closejs = (JSONObject) endjs.get("close");

            String openingtime = openjs.get("time").toString();
            String closingtime = closejs.get("time").toString();
//            if (Long.parseLong(starttime.substring(3, 8)) < Long.parseLong(openingtime)) {
//
//
//            }

            //Between open and closed..


            try {

                Log.d("LocationTimeselected", "openstarttime" + new SimpleDateFormat("HHmm").parse(openingtime));

                SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
//TODO: This only works in malaysia, with unknown reason setting default causes the data to have 30 mins differences
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                long opentime = sdf.parse(openingtime).getTime() / 1000;
                long closetime = sdf.parse(closingtime).getTime() / 1000;


            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("LocationTimeselected", "checkIfOpen booleeeanananan" + checkisopened);

        Log.d("LocationTimeselected", "checkIfOpen" + detail.get("opening_hours"));

        // Log.d("LocationTimeselected", "date" + date);


        return checkisopened;
    }

    //TODO: set confirmation dialog?
    public boolean showSelectionDialog(final Context context, String message) {
        final boolean[] b = {false};
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
//        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        alertDialog.dismiss();
//                    }
//
//                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        b[0] = true;
                    }
                });

        alertDialog.show();


        return b[0];

    }

    //    public void showDialogFragment(DialogFragment newFragment) {
//        // DialogFragment.show() will take care of adding the fragment
//        // in a transaction. We also want to remove any currently showing
//        // dialog, so make our own transaction and take care of that here.
//        FragmentTransaction ft = fragmentManager.beginTransaction();
//        Fragment prev = fragmentManager.findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        // save transaction to the back stack
//        ft.addToBackStack("dialog");
//        newFragment.show(ft, "dialog");
//    }
    public boolean checkoverlap() {
        Log.d("haha", "dafaqcheckingoverlap" + MapsActivity.getWaypointwithDateList());
//
        //THIS just run to see of dialog open/ save the data
        executesave = true;
        ArrayList<LinkedHashMap<String, Object>> overlaparray = new ArrayList();
        StringBuilder overlaplist = new StringBuilder();
        for (int i = 0; i < waypointWithDateListDup.size(); i++) {
            boolean next = true;

            //Checking this shit
            //Cannot enter date into existing period
            //Cannot enter duration that cause overlap into single date or existing period
            long selecteddate = 0;
            long selectedduration = 0;
            long selectedenddate = 0;
            if ((long) waypointwithdateafter.get("starttime") != 0) {
                selecteddate = (long) waypointwithdateafter.get("starttime");

            }

            if ((long) waypointwithdateafter.get("duration") != 0) {
                selectedduration = (long) waypointwithdateafter.get("duration");
                selectedenddate = selecteddate + selectedduration;
            }
            long previousselecteddate = 0;
            long previousselectedduration = 0;
            long previousselectedenddate = 0;


            if ((long) waypointWithDateListDup.get(i).get("starttime") != 0) {
                previousselecteddate = (long) waypointWithDateListDup.get(i).get("starttime");
            }
            if ((long) waypointWithDateListDup.get(i).get("duration") != 0) {

                previousselectedduration = (long) waypointWithDateListDup.get(i).get("duration");
                previousselectedenddate = previousselecteddate + previousselectedduration;

            }
            System.out.println("datex  " + waypointWithDateListDup);

            System.out.println("checkdate1  " + selecteddate);
            System.out.println("checkdate2  " + selectedenddate);
            System.out.println("checkdate3  " + previousselecteddate);
            System.out.println("checkdate4  " + previousselectedenddate);

            String startdate = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new java.util.Date(previousselecteddate * 1000));
            String enddate;
            if (previousselectedenddate != 0) {
                enddate = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm").format(new java.util.Date(previousselectedenddate * 1000));
            } else {

                enddate = "N/A";

            }
            //To pick only not equals place id
            if (!waypointwithdateafter.get("place_id").equals(waypointWithDateListDup.get(i).get("place_id"))) {

//require equals so that can compare long
                if ((long) waypointWithDateListDup.get(i).get("starttime") != 0) {
                    if ((long) waypointwithdateafter.get("starttime") != 0) {
                        if (selecteddate >= previousselecteddate && selecteddate <=
                                previousselectedenddate) {
                            //1>3
                            //1<4
//1503870720
//1503763440--1503871440

                            //3<1<4
                            //  1-----A
                            //3----4

                            //3<2<4
                            //  A-----2
                            //      3----4

                            //1<3<2
                            //  1-----2
                            //      3----A
                            //Impossible.
                            //         1-----2
                            //      A----4

                            //...WHew is 4 between?
                            next = false;
                            executesave = false;
                            System.out.println("checkdate  fail 1 " + MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get("place_id")
                                    .toString()));
                            overlaplist.append(MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get("place_id").toString()).get
                                    ("place_name")
                                    .toString()
                                    + previousselecteddate + "--" + previousselectedenddate);

                            otherplacedetail = MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get("place_id").toString());
                            otherplacedetail.put("selectedstartdate", startdate);
                            otherplacedetail.put("selectedenddate", enddate);
                            overlaparray.add(otherplacedetail);

                            //        MapsActivity.showDialog(getContext(), "The chosen time is overlapping with others locations!");

                        }
                    }
                    if ((long) waypointwithdateafter.get("duration") != 0) {

                        if (selectedenddate >= previousselecteddate) {

                            //      1----2
                            //          3--4
                            if (selectedenddate <=
                                    previousselectedenddate && next) {
                                executesave = false;
                                next = false;

                                //        MapsActivity.showDialog(getContext(), "The chosen time is overlapping with others locations!");
                                System.out.println("checkdate  fail 2 " + MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get
                                        ("place_id")
                                        .toString()));
                                overlaplist.append(MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get("place_id").toString()).get
                                        ("place_name").toString()
                                        + previousselecteddate + "--" + previousselectedenddate);
                                //   overlaparray.add(MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get(0).toString()).get
                                // ("place_name")+": " +
                                //         ""+startdate+"---"+enddate);
                                otherplacedetail = MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get("place_id").toString());
                                otherplacedetail.put("selectedstartdate", startdate);
                                otherplacedetail.put("selectedenddate", enddate);
                                overlaparray.add(otherplacedetail);


                            } else if (previousselecteddate >= selecteddate && next) {
                                executesave = false;
                                next = false;

                                //      MapsActivity.showDialog(getContext(), "The chosen time is overlapping with others locations!");
                                System.out.println("checkdate  fail 3 " + MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get
                                        ("place_id")
                                        .toString()));
                                overlaplist.append(MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get("place_id").toString()).get
                                        ("place_name").toString()
                                        + previousselecteddate + "--" + previousselectedenddate);


                                otherplacedetail = MapsActivity.getWayPointDetails(waypointWithDateListDup.get(i).get("place_id").toString());
                                otherplacedetail.put("selectedstartdate", startdate);
                                otherplacedetail.put("selectedenddate", enddate);
                                overlaparray.add(otherplacedetail);

                            }


                        }


                    }
                }


                System.out.println("checkdate  fail final" + overlaplist.toString());


            }

        }
        if (!executesave) {


            dialog_overlap_Fragment d = new dialog_overlap_Fragment();
            // d.show(getFragmentManager(),"ASD");
            //  dialog.se("Details");
            // dialog.setContentView(R.layout.details_popup);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_overlap_, null);
            alertDialog.setView(convertView);
            alertDialog.setMessage("There are conflictiong between selected data/time and database, data cannot be updated");
            alertDialog.setTitle("Warning!");
            Dialog dialog = alertDialog.create();


            alertDialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            RecyclerView rv = (RecyclerView) convertView.findViewById(R.id.overlapdialogrecycler);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));

            rv.setAdapter(new dialog_overlap_adapter(overlaparray, dialog));
            rv.setHasFixedSize(true);

            dialog.show();
//            Window window = dialog.getWindow();
//            window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            // MapsActivity.showDialog(getContext(), "These chosen time is overlapping with others locations!"+overlaplist.toString());

        }
        System.out.println("boooooin picker  checkif excet " + executesave);
        Log.d("haha", "dafaqcheckingoverlap" + MapsActivity.getWaypointwithDateList());

        return executesave;
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

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment timePickerFragment;
        Fragment datePickerFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            timePickerFragment = new Fragment();
            datePickerFragment = new Fragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return timePickerFragment;
                case 1:
                default:
                    return datePickerFragment;
            }
        }

        int getTitle(int position) {
            switch (position) {
                case 0:
                    return R.string.tab_title_time;
                case 1:
                default:
                    return R.string.tab_title_date;
            }
        }
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
