package com.digywood.tms.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digywood.tms.Adapters.FlashAttemptAdapter;
import com.digywood.tms.Adapters.TestAttemptAdapter;
import com.digywood.tms.AttemptDataActivity;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleFlashAttempt;
import com.digywood.tms.Pojo.SingleTestAttempt;
import com.digywood.tms.R;

import java.util.ArrayList;

import static com.digywood.tms.AttemptDataActivity.round;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestAttemptFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestAttemptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestAttemptFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tv_testid,tv_minscore,tv_maxscore,tv_avgscore,tv_emptytestdata;
    RecyclerView rv_tattemptdata;
    DBHelper dataObj;
    String testId="";
    TestAttemptAdapter tAdp;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleTestAttempt> tattemptList=new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TestAttemptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestAttemptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestAttemptFragment newInstance(String param1, String param2) {
        TestAttemptFragment fragment = new TestAttemptFragment();
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
        View view = inflater.inflate(R.layout.fragment_test_attempt, container, false);
        testId = getActivity().getIntent().getStringExtra("testId");
        dataObj= new DBHelper(getActivity().getApplicationContext());
        tv_testid=view.findViewById(R.id.tv_ftestid);
        tv_testid.setText(testId);
        tv_minscore=view.findViewById(R.id.tv_minpercent);
        tv_maxscore=view.findViewById(R.id.tv_maxpercent);
        tv_avgscore=view.findViewById(R.id.tv_avgpercent);
        rv_tattemptdata=view.findViewById(R.id.rv_testattempts);
        tv_emptytestdata=view.findViewById(R.id.tv_testemptydata);

        getTestAttemptData(testId);
        return view;
    }

    public void getTestAttemptData(String testId){

        Double minscore=0.0,maxscore=0.0,avgscore=0.0;
        Cursor cur=dataObj.getTestRawData(testId);
        if(cur.getCount()>0){
            while (cur.moveToNext()){
                minscore=cur.getDouble(cur.getColumnIndex("minscore"));
                maxscore=cur.getDouble(cur.getColumnIndex("maxscore"));
                avgscore=cur.getDouble(cur.getColumnIndex("avgscore"));
            }
        }else{
            cur.close();
        }

        tv_minscore.setText(String.valueOf(minscore)+" %");
        tv_maxscore.setText(String.valueOf(maxscore)+" %");
        Double avgpercent=round(avgscore,2);
        tv_avgscore.setText(String.valueOf(avgpercent)+" %");
        Cursor mycursor=dataObj.getTestAttemptData(testId);
        Log.e("tadapter", ""+ mycursor.getCount());

        if(mycursor.getCount()>0){
            while(mycursor.moveToNext()){
                tattemptList.add(new SingleTestAttempt(mycursor.getInt(mycursor.getColumnIndex("Attempt_ID")),mycursor.getInt(mycursor.getColumnIndex("Attempt_Confirmed")),mycursor.getInt(mycursor.getColumnIndex("Attempt_Skipped")),mycursor.getInt(mycursor.getColumnIndex("Attempt_Bookmarked")),mycursor.getInt(mycursor.getColumnIndex("Attempt_UnAttempted")),mycursor.getDouble(mycursor.getColumnIndex("Attempt_Score")),mycursor.getDouble(mycursor.getColumnIndex("Attempt_Percentage"))));
            }
        }else{
            mycursor.close();
        }

        if (tattemptList.size() != 0) {
//            Log.e("Advtlist.size()", "comes:" + tattemptList.size());
            tv_emptytestdata.setVisibility(View.GONE);
            tAdp = new TestAttemptAdapter(tattemptList,getActivity().getApplicationContext());
            myLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false);
            rv_tattemptdata.setLayoutManager(myLayoutManager);
            rv_tattemptdata.setItemAnimator(new DefaultItemAnimator());
            Log.e("tadapter", ""+tAdp.getItemCount());
            rv_tattemptdata.setAdapter(tAdp);
        } else {
            rv_tattemptdata.setAdapter(null);
            tv_emptytestdata.setText("No Practice Test History");
            tv_emptytestdata.setVisibility(View.VISIBLE);
        }
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
       /* if (context instanceof OnFragmentInteractionListener) {
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
