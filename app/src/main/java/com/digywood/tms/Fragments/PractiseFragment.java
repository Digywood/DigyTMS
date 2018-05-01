package com.digywood.tms.Fragments;


import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;

import com.digywood.tms.Adapters.TestAttemptAdapter;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleTestAttempt;
import com.digywood.tms.R;

import java.util.ArrayList;

import static com.digywood.tms.AttemptDataActivity.round;

public class PractiseFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DBHelper myhelper;
    int totptestcount=0;

    Button btn_pdetails;

    TextView tv_ptottests,tv_pattempted,tv_ptestsasplan,tv_ppercent,tv_pmax,tv_pmin,tv_pavg,tv_pRAGattempt,tv_pRAGAVGscore;

    private PractiseFragment.OnFragmentInteractionListener mListener;

    public PractiseFragment() {
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
    public static PractiseFragment newInstance(String param1, String param2) {
        PractiseFragment fragment = new PractiseFragment();
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
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_pdash, container, false);

        tv_ptottests=view.findViewById(R.id.tv_ptottests);
        tv_pattempted=view.findViewById(R.id.tv_pattempted);
        tv_ptestsasplan=view.findViewById(R.id.tv_ptestsasplan);
        tv_ppercent=view.findViewById(R.id.tv_ppercent);
        tv_pmax=view.findViewById(R.id.tv_pmax);
        tv_pmin=view.findViewById(R.id.tv_pmin);
        tv_pavg=view.findViewById(R.id.tv_pavg);
        tv_pRAGattempt=view.findViewById(R.id.tv_pRAGattempt);
        tv_pRAGAVGscore=view.findViewById(R.id.tv_pRAGAVGscore);

        btn_pdetails = view.findViewById(R.id.btn_pdetails);

        myhelper=new DBHelper(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totptestcount=myhelper.getPTestsCount();
        tv_ptottests.setText(""+totptestcount);

        Cursor mycur1=myhelper.getPractiseSummary();
        if(mycur1.getCount()>0){
            while (mycur1.moveToNext()){
                int attemptpcount=mycur1.getInt(mycur1.getColumnIndex("attemptpcount"));
                Double min=mycur1.getDouble(mycur1.getColumnIndex("minscore"));
                Double max=mycur1.getDouble(mycur1.getColumnIndex("maxscore"));
                Double avg=mycur1.getDouble(mycur1.getColumnIndex("avgscore"));
                tv_pattempted.setText(""+attemptpcount);
                tv_pmax.setText(""+round(max,1));
                tv_pmin.setText(""+round(min,1));
                tv_pavg.setText(""+round(avg,1));
            }
        }else{
            mycur1.close();
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
