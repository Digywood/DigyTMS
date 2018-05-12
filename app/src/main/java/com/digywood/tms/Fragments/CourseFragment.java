package com.digywood.tms.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digywood.tms.Adapters.EnrollAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rv_enroll;
    TextView tv_emptyenroll;
    String studentid="",studentname="";
    DBHelper myhelper;
    Random random ;
    ArrayList<String> enrollids;
    ArrayList<String> enrollcourseids;
    HashMap<String,String> hmap=new HashMap<>();
    ArrayList<SingleEnrollment> enrollList;
    LinearLayoutManager myLayoutManager;
    EnrollAdapter eAdp;

    private FlashAttemptFragment.OnFragmentInteractionListener mListener;

    public CourseFragment() {
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
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
        View view = inflater.inflate(R.layout.activity_courselist, container, false);
        rv_enroll=view.findViewById(R.id.rv_clistofenrolls);
        tv_emptyenroll=view.findViewById(R.id.tv_cenrollemptydata);
        enrollids=new ArrayList<>();
        enrollcourseids=new ArrayList<>();
        enrollList=new ArrayList<>();

        myhelper=new DBHelper(getActivity());
        random=new Random();

        Intent cmgintent=getActivity().getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
        }

        getEnrollsFromLocal();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    public void getEnrollsFromLocal(){

        Cursor mycursor=myhelper.getStudentEnrolls();
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()) {
                String coursename=myhelper.getCoursenameById(mycursor.getString(mycursor.getColumnIndex("Enroll_course_ID")));
                enrollList.add(new SingleEnrollment(mycursor.getString(mycursor.getColumnIndex("Enroll_ID")),mycursor.getString(mycursor.getColumnIndex("Enroll_batch_ID")),mycursor.getString(mycursor.getColumnIndex("Enroll_org_id")),mycursor.getString(mycursor.getColumnIndex("Enroll_course_ID")),coursename,"2018-05-10","2018-05-10",10));
            }
        }else{
            mycursor.close();
        }

        if (enrollList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + enrollList.size());
            tv_emptyenroll.setVisibility(View.GONE);
            eAdp = new EnrollAdapter(enrollList,getActivity());
            myLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
            rv_enroll.setLayoutManager(myLayoutManager);
            rv_enroll.setItemAnimator(new DefaultItemAnimator());
            rv_enroll.setAdapter(eAdp);
        } else {
            tv_emptyenroll.setText("No Enrollments for student");
            tv_emptyenroll.setVisibility(View.VISIBLE);
        }
    }

}
