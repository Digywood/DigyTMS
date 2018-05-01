package com.digywood.tms.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


    Button btn_next,btn_pdetails,btn_fdetails,btn_adetails;
    TextView tv_studentid,tv_sname;

    TextView tv_ptottests,tv_pattempted,tv_ptestsasplan,tv_ppercent,tv_pmax,tv_pmin,tv_pavg,tv_pRAGattempt,tv_pRAGAVGscore;
    TextView tv_ftottests,tv_fattempted,tv_ftestsasplan,tv_fpercent,tv_fmax,tv_fmin,tv_favg,tv_fRAGattempt,tv_fRAGAVGscore;
    TextView tv_atottests,tv_aattempted,tv_atestsasplan,tv_apercent,tv_amax,tv_amin,tv_aavg,tv_aRAGattempt,tv_aRAGAVGscore;

    Spinner sp_enrollid,sp_coursename;

    DBHelper myhelper;

    ArrayList<String> courseIds=new ArrayList<>();
    ArrayList<String> enrollIds=new ArrayList<>();
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

        Intent cmgintent=getActivity().getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
        }

        tv_studentid=view.findViewById(R.id.tv_dstudentid);
        tv_studentid.setText(studentid);
        tv_sname=view.findViewById(R.id.tv_dsname);
        tv_sname.setText(studentname);

        sp_enrollid=view.findViewById(R.id.sp_denrollid);
        sp_coursename=view.findViewById(R.id.sp_dcoursename);

        tv_ptottests=view.findViewById(R.id.tv_ptottests);
        tv_pattempted=view.findViewById(R.id.tv_pattempted);
        tv_ptestsasplan=view.findViewById(R.id.tv_ptestsasplan);
        tv_ppercent=view.findViewById(R.id.tv_ppercent);
        tv_pmax=view.findViewById(R.id.tv_pmax);
        tv_pmin=view.findViewById(R.id.tv_pmin);
        tv_pavg=view.findViewById(R.id.tv_pavg);
        tv_pRAGattempt=view.findViewById(R.id.tv_pRAGattempt);
        tv_pRAGAVGscore=view.findViewById(R.id.tv_pRAGAVGscore);

        tv_ftottests=view.findViewById(R.id.tv_ftottests);
        tv_fattempted=view.findViewById(R.id.tv_fattempted);
        tv_ftestsasplan=view.findViewById(R.id.tv_ftestsasplan);
        tv_fpercent=view.findViewById(R.id.tv_fpercent);
        tv_fmax=view.findViewById(R.id.tv_fmax);
        tv_fmin=view.findViewById(R.id.tv_fmin);
        tv_favg=view.findViewById(R.id.tv_favg);
        tv_fRAGattempt=view.findViewById(R.id.tv_fRAGattempt);
        tv_fRAGAVGscore=view.findViewById(R.id.tv_fRAGAVGscore);

        tv_atottests=view.findViewById(R.id.tv_atottests);
        tv_aattempted=view.findViewById(R.id.tv_aattempted);
        tv_atestsasplan=view.findViewById(R.id.tv_atestsasplan);
        tv_apercent=view.findViewById(R.id.tv_apercent);
        tv_amax=view.findViewById(R.id.tv_amax);
        tv_amin=view.findViewById(R.id.tv_amin);
        tv_aavg=view.findViewById(R.id.tv_aavg);
        tv_aRAGattempt=view.findViewById(R.id.tv_aRAGattempt);
        tv_aRAGAVGscore=view.findViewById(R.id.tv_aRAGAVGscore);

        btn_next =view.findViewById(R.id.btn_mylearnings);
        btn_pdetails = view.findViewById(R.id.btn_pdetails);
        btn_fdetails = view.findViewById(R.id.btn_fdetails);
        btn_adetails = view.findViewById(R.id.btn_adetails);

        myhelper=new DBHelper(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Cursor mycursor=myhelper.getAllCourseIds();
        Log.e("CursorCount---",""+mycursor.getCount());
        courseIds.add("Select");
        if(mycursor.getCount()>0){
            while(mycursor.moveToNext()){
                String courseId=mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                courseIds.add(courseId);
            }
            courseAdp= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,courseIds);
            courseAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_coursename.setAdapter(courseAdp);
        }else{
            mycursor.close();
        }

        sp_coursename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enrollIds.clear();
                enrollIds.add("Select");
                Cursor mycursor=myhelper.getCourseEnrollments(sp_coursename.getSelectedItem().toString());
                if(mycursor.getCount()>0){
                    while (mycursor.moveToNext()){
                        String enrollId=mycursor.getString(mycursor.getColumnIndex("sptu_entroll_id"));
                        enrollIds.add(enrollId);
                    }
                    enrollAdp= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,enrollIds);
                    enrollAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_enrollid.setAdapter(enrollAdp);
                }else{
                    mycursor.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_pdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Practise Test",Toast.LENGTH_SHORT).show();
            }
        });

        btn_fdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Flash Card",Toast.LENGTH_SHORT).show();
            }
        });

        btn_adetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Assessment Test",Toast.LENGTH_SHORT).show();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),LandingActivity.class);
                i.putExtra("studentid",studentid);
                i.putExtra("studentname",studentname);
                startActivity(i);
            }
        });

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

}
