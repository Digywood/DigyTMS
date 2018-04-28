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
import com.digywood.tms.AttemptDataActivity;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleFlashAttempt;
import com.digywood.tms.R;

import java.util.ArrayList;

import static com.digywood.tms.FlashCardActivity.round;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FlashAttemptFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FlashAttemptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FlashAttemptFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DBHelper myhelper;
    String testId="";
    RecyclerView rv_fattemptdata;
    TextView tv_emptyflashdata;
    FlashAttemptAdapter fAdp;
    TextView tv_testid,tv_minscore,tv_maxscore,tv_avgscore;
    LinearLayoutManager myLayoutManager;
    ArrayList<SingleFlashAttempt> fattemptList=new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FlashAttemptFragment() {
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
    public static FlashAttemptFragment newInstance(String param1, String param2) {
        FlashAttemptFragment fragment = new FlashAttemptFragment();
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
        View view = inflater.inflate(R.layout.fragment_flash_attempt, container, false);
        myhelper=new DBHelper(getActivity().getApplicationContext());
        tv_testid=view.findViewById(R.id.tv_ftestid);
        tv_testid.setText(testId);
        tv_minscore=view.findViewById(R.id.tv_minpercent);
        tv_maxscore=view.findViewById(R.id.tv_maxpercent);
        tv_avgscore=view.findViewById(R.id.tv_avgpercent);
        rv_fattemptdata=view.findViewById(R.id.rv_flashattempts);
        tv_emptyflashdata=view.findViewById(R.id.tv_flashemptydata);
        getFlashAttemptData(testId);
        return view;
    }

    public void getFlashAttemptData(String testId){

        Double minscore=0.0,maxscore=0.0,avgscore=0.0;
        Cursor cur=myhelper.getTestFlashData(testId);
        if(cur.getCount()>0){
            while (cur.moveToNext()){
                minscore=cur.getDouble(cur.getColumnIndex("min_flashScore"));
                maxscore=cur.getDouble(cur.getColumnIndex("max_flashScore"));
                avgscore=cur.getDouble(cur.getColumnIndex("avg_flashScore"));
            }
        }else{
            cur.close();
        }

        tv_minscore.setText(String.valueOf(minscore)+" %");
        tv_maxscore.setText(String.valueOf(maxscore)+" %");
        Double avgpercent=round(avgscore,2);
        tv_avgscore.setText(String.valueOf(avgpercent)+" %");

        Cursor mycursor=myhelper.getFlashTestData(testId);
        if(mycursor.getCount()>0){
            while(mycursor.moveToNext()){
                fattemptList.add(new SingleFlashAttempt(mycursor.getString(mycursor.getColumnIndex("startDttm")),mycursor.getInt(mycursor.getColumnIndex("iknowCount")),mycursor.getInt(mycursor.getColumnIndex("donknowCount")),mycursor.getInt(mycursor.getColumnIndex("skipCount")),mycursor.getDouble(mycursor.getColumnIndex("percentageObtain"))));
            }
        }else{
            mycursor.close();
        }
        if (fattemptList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + fattemptList.size());
            tv_emptyflashdata.setVisibility(View.GONE);
            fAdp = new FlashAttemptAdapter(fattemptList,getActivity().getApplicationContext());
            myLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL,false);
            rv_fattemptdata.setLayoutManager(myLayoutManager);
            rv_fattemptdata.setItemAnimator(new DefaultItemAnimator());
            rv_fattemptdata.setAdapter(fAdp);
        } else {
            rv_fattemptdata.setAdapter(null);
            tv_emptyflashdata.setText("No Flash Attempt History");
            tv_emptyflashdata.setVisibility(View.VISIBLE);
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
