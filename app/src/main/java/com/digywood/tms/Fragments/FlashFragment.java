package com.digywood.tms.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.PaperDashActivity;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class FlashFragment extends Fragment implements OnChartValueSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DBHelper myhelper;
    Button btn_fdetails;
    int totptestcount=0,attemptpcount=0;
    private PieChart mChart;
    float attemptpercent=0.0f;
    Double min=0.0,max=0.0,avg=0.0;
    String enrollid="",courseid="";

    ArrayList<SingleEnrollment> enrollPojos=new ArrayList<>();
    ArrayList<String> enrollIds=new ArrayList<>();
    ArrayAdapter<String> enrollAdp;

    Spinner sp_enrollids;
    TextView tv_ftottests,tv_fattempted,tv_ftestsasplan,tv_fpercent,tv_fmax,tv_fmin,tv_favg,tv_fRAGattempt,tv_fRAGAVGscore;

    protected String[] mParties = new String[] {
            "Completed", "Left", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P"
    };

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

        sp_enrollids=view.findViewById(R.id.sp_fenrollids);

        myhelper=new DBHelper(getActivity());

        mChart=view.findViewById(R.id.chart2);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Cursor mycursor=myhelper.getAllEnrolls();
        Log.e("CursorCount---",""+mycursor.getCount());
        if(mycursor.getCount()>0){
            while(mycursor.moveToNext()){
                String enrollidId=mycursor.getString(mycursor.getColumnIndex("Enroll_ID"));
                String courseId=mycursor.getString(mycursor.getColumnIndex("Enroll_course_ID"));
                enrollIds.add(enrollidId);
                enrollPojos.add(new SingleEnrollment(enrollidId,courseId));
            }
            enrollAdp= new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,enrollIds);
            enrollAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_enrollids.setAdapter(enrollAdp);
        }else{
            mycursor.close();
        }

        if(enrollIds.size()>0){
            totptestcount=myhelper.getPTestsCount(sp_enrollids.getSelectedItem().toString());
            tv_ftottests.setText(""+totptestcount);
            Cursor mycur=myhelper.getFlashSummary(sp_enrollids.getSelectedItem().toString());
            if(mycur.getCount()>0){
                while (mycur.moveToNext()){
                    attemptpcount=mycur.getInt(mycur.getColumnIndex("attemptfcount"));
                    min=mycur.getDouble(mycur.getColumnIndex("minscore"));
                    max=mycur.getDouble(mycur.getColumnIndex("maxscore"));
                    avg=mycur.getDouble(mycur.getColumnIndex("avgscore"));
                    tv_fattempted.setText(""+attemptpcount);
                    tv_fmax.setText(""+round(max,1));
                    tv_fmin.setText(""+round(min,1));
                    tv_favg.setText(""+round(avg,1));
                }
            }else{
                mycur.close();
            }
        }

        sp_enrollids.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SingleEnrollment singleEnrollment=enrollPojos.get(position);
                enrollid=singleEnrollment.getEnrollid();
                courseid=singleEnrollment.getEnrollcourseid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_fdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getActivity(), PaperDashActivity.class);
                i.putExtra("courseid",courseid);
                i.putExtra("testtype","FLASH");
                startActivity(i);
            }
        });

        attemptpercent=(Float.parseFloat(String.valueOf(attemptpcount))/totptestcount)*100;

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        Double d = new Double(avg);
        int i = d.intValue();

        mChart.setCenterText(generateCenterSpannableText(String.valueOf(i)));

        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

//        // add a selection listener
//        mChart.setOnChartValueSelectedListener(getActivity());

        setData(attemptpercent,100);

//        mChart.animateY(1400,Easing.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

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

    private void setData(float percent, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<>();

//        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
//        // the chart.
//        for (int i = 0; i < count; i++) {
//            entries.add(new PieEntry((float) (Math.random() * mult) + mult / 5, mParties[i % mParties.length]));
//        }

        entries.add(new PieEntry(percent,"Completed"));
        entries.add(new PieEntry(range-percent,"Left"));


        PieDataSet dataSet = new PieDataSet(entries,"Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());

        colors.add(Color.rgb(100, 196, 125));
        colors.add(Color.rgb(67, 65, 64));
        colors.add(Color.rgb(0, 0, 255));

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText(String value) {
        SpannableString s = new SpannableString("Performance \n"+value+"%");
        return s;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
