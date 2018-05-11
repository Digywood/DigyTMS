package com.digywood.tms.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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

public class AssessmentFragment extends Fragment implements OnChartValueSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_atottests,tv_aattempted,tv_atestsasplan,tv_apercent,tv_amax,tv_amin,tv_aavg,tv_aRAGattempt,tv_aRAGAVGscore,tv_coursename;

    DBHelper myhelper;

    String enrollid="",courseid="";

    Button btn_adetails;
    public PieChart mChart;

    ArrayList<SingleEnrollment> enrollPojos=new ArrayList<>();
    ArrayList<String> enrollIds=new ArrayList<>();
    ArrayAdapter<String> enrollAdp;
    Spinner sp_enrollids;

    private AssessmentFragment.OnFragmentInteractionListener mListener;

    public AssessmentFragment() {
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
    public static AssessmentFragment newInstance(String param1, String param2) {
        AssessmentFragment fragment = new AssessmentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_adash,container,false);

        tv_atottests=view.findViewById(R.id.tv_atottests);
        tv_aattempted=view.findViewById(R.id.tv_aattempted);
        tv_atestsasplan=view.findViewById(R.id.tv_atestsasplan);
        tv_apercent=view.findViewById(R.id.tv_apercent);
        tv_amax=view.findViewById(R.id.tv_amax);
        tv_amin=view.findViewById(R.id.tv_amin);
        tv_aavg=view.findViewById(R.id.tv_aavg);
        tv_aRAGattempt=view.findViewById(R.id.tv_aRAGattempt);
        tv_aRAGAVGscore=view.findViewById(R.id.tv_aRAGAVGscore);
        tv_coursename=view.findViewById(R.id.tv_acoursename);

        btn_adetails = view.findViewById(R.id.btn_adetails);
        sp_enrollids=view.findViewById(R.id.sp_aenrollid);

        myhelper=new DBHelper(getActivity());

        mChart=view.findViewById(R.id.chart3);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

//        mChart.setCenterText(generateCenterSpannableText());

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

        setData(60,100);

//        mChart.animateY(1400,Easing.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

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

        sp_enrollids.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position,long id) {

                SingleEnrollment singleEnrollment=enrollPojos.get(position);
                enrollid=singleEnrollment.getDenrollid();
                courseid=singleEnrollment.getDcourseid();
                String cname=myhelper.getCoursenameById(courseid);
                tv_coursename.setText(cname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_adetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"No further action",Toast.LENGTH_SHORT).show();
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
