package com.digywood.tms.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.digywood.tms.Adapters.DashPagerAdapter;
import com.digywood.tms.Adapters.PagerAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.DbActivity;
import com.digywood.tms.LandingActivity;
import com.digywood.tms.R;
import java.util.ArrayList;

public class DashBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    Button btn_pdetails,btn_fdetails,btn_adetails;
    TextView tv_studentid,tv_sname;

    Spinner sp_enrollid,sp_coursename;

    DBHelper myhelper;

    int totptestcount=0;

    ArrayList<String> courseIds=new ArrayList<>();
    ArrayAdapter<String> courseAdp,enrollAdp;
    String studentid="",enrollid,courseid,studentname="";

    private FlashAttemptFragment.OnFragmentInteractionListener mListener;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FlashAttemptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        View view = inflater.inflate(R.layout.activity_db, container, false);

        TabLayout tabLayout =view.findViewById(R.id.dash_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Practise Tests"));
        tabLayout.addTab(tabLayout.newTab().setText("Flashcard Tests"));
        tabLayout.addTab(tabLayout.newTab().setText("Assessment Tests"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        final ViewPager viewPager = view.findViewById(R.id.dash_viewpager);
        final DashPagerAdapter adapter = new DashPagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Intent cmgintent=getActivity().getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
        }


//        sp_enrollid=view.findViewById(R.id.sp_denrollid);
//        sp_coursename=view.findViewById(R.id.sp_dcoursename);


        myhelper=new DBHelper(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Cursor mycursor=myhelper.getAllCourseIds();
//        Log.e("CursorCount---",""+mycursor.getCount());
//        courseIds.add("Select");
//        if(mycursor.getCount()>0){
//            while(mycursor.moveToNext()){
//                String courseId=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
//                courseIds.add(courseId);
//            }
//            courseAdp= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,courseIds);
//            courseAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp_coursename.setAdapter(courseAdp);
//        }else{
//            mycursor.close();
//        }
//
//        totptestcount=myhelper.getPTestsCount();
//
//        tv_ftottests.setText(""+totptestcount);
//        tv_ptottests.setText(""+totptestcount);
//
//        Cursor mycur=myhelper.getFlashSummary();
//        if(mycur.getCount()>0){
//            while (mycur.moveToNext()){
//                int attemptpcount=mycur.getInt(mycur.getColumnIndex("attemptfcount"));
//                Double min=mycur.getDouble(mycur.getColumnIndex("minscore"));
//                Double max=mycur.getDouble(mycur.getColumnIndex("maxscore"));
//                Double avg=mycur.getDouble(mycur.getColumnIndex("avgscore"));
//                tv_fattempted.setText(""+attemptpcount);
//                tv_fmax.setText(""+round(max,1));
//                tv_fmin.setText(""+round(min,1));
//                tv_favg.setText(""+round(avg,1));
//            }
//        }else{
//            mycur.close();
//        }
//
//        Cursor mycur1=myhelper.getPractiseSummary();
//        if(mycur1.getCount()>0){
//            while (mycur1.moveToNext()){
//                int attemptpcount=mycur1.getInt(mycur1.getColumnIndex("attemptpcount"));
//                Double min=mycur1.getDouble(mycur1.getColumnIndex("minscore"));
//                Double max=mycur1.getDouble(mycur1.getColumnIndex("maxscore"));
//                Double avg=mycur1.getDouble(mycur1.getColumnIndex("avgscore"));
//                tv_pattempted.setText(""+attemptpcount);
//                tv_pmax.setText(""+round(max,1));
//                tv_pmin.setText(""+round(min,1));
//                tv_pavg.setText(""+round(avg,1));
//            }
//        }else{
//            mycur1.close();
//        }
//
//        sp_coursename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                enrollIds.clear();
//                enrollIds.add("Select");
//                Cursor mycursor=myhelper.getCourseEnrollments(sp_coursename.getSelectedItem().toString());
//                if(mycursor.getCount()>0){
//                    while (mycursor.moveToNext()){
//                        String enrollId=mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
//                        enrollIds.add(enrollId);
//                    }
//                    enrollAdp= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,enrollIds);
//                    enrollAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    sp_enrollid.setAdapter(enrollAdp);
//                }else{
//                    mycursor.close();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        btn_pdetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Practise Test",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btn_fdetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Flash Card",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btn_adetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Assessment Test",Toast.LENGTH_SHORT).show();
//            }
//        });

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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
