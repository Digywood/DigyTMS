package com.digywood.tms.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.R;

public class FlashFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DBHelper myhelper;

    Button btn_fdetails;

    int totptestcount=0;

    TextView tv_ftottests,tv_fattempted,tv_ftestsasplan,tv_fpercent,tv_fmax,tv_fmin,tv_favg,tv_fRAGattempt,tv_fRAGAVGscore;

    private FlashFragment.OnFragmentInteractionListener mListener;

    public FlashFragment() {
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
    public static FlashFragment newInstance(String param1, String param2) {
        FlashFragment fragment = new FlashFragment();
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
        View view = inflater.inflate(R.layout.activity_fdash, container, false);

        tv_ftottests=view.findViewById(R.id.tv_ftottests);
        tv_fattempted=view.findViewById(R.id.tv_fattempted);
        tv_ftestsasplan=view.findViewById(R.id.tv_ftestsasplan);
        tv_fpercent=view.findViewById(R.id.tv_fpercent);
        tv_fmax=view.findViewById(R.id.tv_fmax);
        tv_fmin=view.findViewById(R.id.tv_fmin);
        tv_favg=view.findViewById(R.id.tv_favg);
        tv_fRAGattempt=view.findViewById(R.id.tv_fRAGattempt);
        tv_fRAGAVGscore=view.findViewById(R.id.tv_fRAGAVGscore);


        btn_fdetails = view.findViewById(R.id.btn_fdetails);

        myhelper=new DBHelper(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totptestcount=myhelper.getPTestsCount();

        tv_ftottests.setText(""+totptestcount);

        Cursor mycur=myhelper.getFlashSummary();
        if(mycur.getCount()>0){
            while (mycur.moveToNext()){
                int attemptpcount=mycur.getInt(mycur.getColumnIndex("attemptfcount"));
                Double min=mycur.getDouble(mycur.getColumnIndex("minscore"));
                Double max=mycur.getDouble(mycur.getColumnIndex("maxscore"));
                Double avg=mycur.getDouble(mycur.getColumnIndex("avgscore"));
                tv_fattempted.setText(""+attemptpcount);
                tv_fmax.setText(""+round(max,1));
                tv_fmin.setText(""+round(min,1));
                tv_favg.setText(""+round(avg,1));
            }
        }else{
            mycur.close();
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
