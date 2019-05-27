package com.digywood.tms.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Adapters.EnrollAdapter;
import com.digywood.tms.AppEnvironment;
import com.digywood.tms.AsynTasks.AsyncCheckInternet_WithOutProgressBar;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.INetStatus;
import com.digywood.tms.MyApplication;
import com.digywood.tms.PaperActivity;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.R;
import com.digywood.tms.RecyclerTouchListener;
import com.digywood.tms.UserMode;

import java.util.ArrayList;
import java.util.Random;

public class EnrollFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rv_enroll;
    TextView tv_emptyenroll;
    String studentid="",studentname="",number,email;
    DBHelper myhelper;
    Random random ;
    ArrayList<String> enrollids;
    ArrayList<String> enrollcourseids;
    ArrayList<SingleEnrollment> enrollList;
    LinearLayoutManager myLayoutManager;
//    FloatingActionButton fab_reqenrollments;
    EnrollAdapter eAdp;

    AppEnvironment appEnvironment;
    UserMode userMode;

    private FlashAttemptFragment.OnFragmentInteractionListener mListener;

    public EnrollFragment() {
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
    public static EnrollFragment newInstance(String param1, String param2) {
        EnrollFragment fragment = new EnrollFragment();
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
        View view = inflater.inflate(R.layout.activity_enrolllist, container, false);

        appEnvironment = ((MyApplication) getActivity().getApplication()).getAppEnvironment();//getting App Environment
        userMode = ((MyApplication) getActivity().getApplication()).getUserMode();//getting User Mode

        rv_enroll=view.findViewById(R.id.rv_elistofenrolls);
        tv_emptyenroll=view.findViewById(R.id.tv_eenrollemptydata);
        enrollids=new ArrayList<>();
        enrollcourseids=new ArrayList<>();
        enrollList=new ArrayList<>();

        myhelper=new DBHelper(getActivity());
        random=new Random();

        Intent cmgintent=getActivity().getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            studentname=cmgintent.getStringExtra("sname");
            number=cmgintent.getStringExtra("number");
            email=cmgintent.getStringExtra("email");
        }

        rv_enroll.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rv_enroll, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onClick(View view, final int position) {
                if (!userMode.mode()) {
                    SingleEnrollment singleEnrollment=enrollList.get(position);
                    Intent intent=new Intent(getContext(), PaperActivity.class);
                    intent.putExtra("studentid",studentid);
                    intent.putExtra("enrollid",singleEnrollment.getEnrollid());
                    intent.putExtra("courseid",singleEnrollment.getCourseid());
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    new AsyncCheckInternet_WithOutProgressBar(getContext(), new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus)
                            {
                                SingleEnrollment singleEnrollment=enrollList.get(position);
                                Intent intent=new Intent(getContext(),PaperActivity.class);
                                intent.putExtra("studentid",studentid);
                                intent.putExtra("enrollid",singleEnrollment.getEnrollid());
                                intent.putExtra("courseid",singleEnrollment.getCourseid());
                                startActivity(intent);
                                getActivity().finish();
                            }else {
                                Toast.makeText(getContext(), "No internet,Please Check Your Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
        Cursor mycursor=myhelper.getStudentEnrolls(studentid);
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
            myLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
            rv_enroll.setLayoutManager(myLayoutManager);
            rv_enroll.setItemAnimator(new DefaultItemAnimator());
            rv_enroll.setAdapter(eAdp);
        } else {
            tv_emptyenroll.setText("No Enrollments for student");
            tv_emptyenroll.setVisibility(View.VISIBLE);
        }
    }

}
