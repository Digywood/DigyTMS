package com.digywood.tms;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.EnrollAdapter;
import com.digywood.tms.Adapters.PaperAdapter;
import com.digywood.tms.Adapters.ScrollGridCardAdapter;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleDashPaper;
import com.digywood.tms.Pojo.SingleEnrollment;
import com.digywood.tms.Pojo.SinglePaper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PaperActivity extends AppCompatActivity {

    ArrayList<String> papernameList;
    ArrayList<String> paperidList;
    ArrayList<SinglePaper> paperList;
    GridView papergridView;
    SinglePaper singlePaper;
    Dialog mydialog;
    public static final int RequestPermissionCode = 1;
    String testtype="",courseid="",enrollid="";
    HashMap<String,String> hmap=new HashMap<>();
    DBHelper myhelper;
    LinearLayoutManager myLayoutManager;
    PaperAdapter pAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if(Build.VERSION.SDK_INT>=21) {

            final Drawable upArrow = getApplicationContext().getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

        papergridView=findViewById(R.id.paper_grid);

        paperidList=new ArrayList<>();
        papernameList=new ArrayList<>();
        paperList=new ArrayList<>();

        myhelper=new DBHelper(this);

        final Intent cmgintent=getIntent();
        if(cmgintent!=null){
            enrollid=cmgintent.getStringExtra("enrollid");
            courseid=cmgintent.getStringExtra("courseid");
        }

        papergridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View v,int position, long id) {

                singlePaper=paperList.get(position);

                mydialog = new Dialog(PaperActivity.this);
                mydialog.getWindow();
                mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mydialog.setContentView(R.layout.activity_testpopup);
                mydialog.show();

//                RadioGroup rg_testpopup=mydialog.findViewById(R.id.rg_testpopup);
//                RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
//
//                Button btn_go =mydialog.findViewById(R.id.btn_popgo);
//
//                btn_go.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i=new Intent(getApplicationContext(),ListofPractiseTests.class);
//                        i.putExtra("enrollid",enrollid);
//                        i.putExtra("courseid",courseid);
//                        i.putExtra("paperid",singlePaper.getPaperId());
//                        startActivity(i);
//                    }
//                });

//                Intent i=new Intent(getApplicationContext(),ListofPractiseTests.class);
//                i.putExtra("enrollid",enrollid);
//                i.putExtra("courseid",courseid);
//                i.putExtra("paperid",singlePaper.getPaperId());
//                startActivity(i);
            }
        });

        getPapersFromLocal();

    }

    public void getPapersFromLocal(){
        paperidList.clear();
        papernameList.clear();
        Cursor mycursor=myhelper.getPapersByCourse(courseid);
        if(mycursor.getCount()>0){
            while (mycursor.moveToNext()){
                String paperid=mycursor.getString(mycursor.getColumnIndex("Paper_ID"));
                String papername=mycursor.getString(mycursor.getColumnIndex("Paper_Name"));
                paperidList.add(paperid);
                papernameList.add(papername);
            }
            Log.e("PaperActivity----",""+paperidList.size());
            mycursor.close();
        }else{
            mycursor.close();
        }

        if(paperidList.size()>0){

            paperList.clear();
            int attemptcount=0;
            double min=0.0,max=0.0,avg=0.0,bmin=0.0,bmax=0.0,bavg=0.0;

            for(int i=0;i<paperidList.size();i++){

                int totaltestcount=myhelper.getTestsByPaper(paperidList.get(i));

                Cursor mycur=myhelper.getPractiseSummaryByPaper(paperidList.get(i));
                if(mycur.getCount()>0){
                    while (mycur.moveToNext()){

                        attemptcount=mycur.getInt(mycur.getColumnIndex("attemptpcount"));
                        min=mycur.getDouble(mycur.getColumnIndex("minscore"));
                        max=mycur.getDouble(mycur.getColumnIndex("maxscore"));
                        avg=mycur.getDouble(mycur.getColumnIndex("avgscore"));
                    }
                }else{
                    mycur.close();
                }

                paperList.add(new SinglePaper(paperidList.get(i),papernameList.get(i),min,avg,max));

            }
        }

//        setData();

        pAdp = new PaperAdapter(PaperActivity.this,paperList,enrollid,courseid);
        papergridView.setAdapter(pAdp);

    }

    public void getPapersData(){
        hmap.clear();
        hmap.put("courseid",courseid);
        new BagroundTask(URLClass.hosturl + "getPapersByCourseId.php",hmap,PaperActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("PaperActivity------","json:comes"+json);
                    if(json.equalsIgnoreCase("Papers_Not_Exist")){

                        Toast.makeText(getApplicationContext(),"No Papers Found on this course",Toast.LENGTH_SHORT).show();

                    }else{
                        JSONObject myObj=null;
                        JSONArray ja=new JSONArray(json);
                        papernameList.add("Select");
                        for(int i=0;i<ja.length();i++){
                            myObj=ja.getJSONObject(i);
                            String paperid=myObj.getString("Paper_ID");
                            String papername=myObj.getString("Paper_Name");
                            paperidList.add(paperid);
                            papernameList.add(papername);

                            long checkFlag=myhelper.checkPaper(myObj.getInt("Paper_Key"));
                            if(checkFlag>0){
                                Log.e("PaperActivity----","Paper Already Exists");
                            }else{
                                long insertFlag=myhelper.insertPaper(myObj.getInt("Paper_Key"),myObj.getString("Paper_ID"),myObj.getString("Paper_Seq_no"),myObj.getString("Subject_ID"),
                                        myObj.getString("Course_ID"),myObj.getString("Paper_Name"),myObj.getString("Paper_Short_Name"),myObj.getString("Paper_Min_Pass_Marks"),
                                        myObj.getString("Paper_Max_Marks"));
                                if(insertFlag>0){
                                    Log.e("PaperActivity----","Paper Inserted in Local");
                                }else{
                                    Log.e("PaperActivity----","Local Paper Insertion Failed");
                                }
                            }

                        }
                    }
                    getPapersFromLocal();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("PaperActivity------",e.toString());
                }
            }
        }).execute();
    }

//    public void navigateTestActivity(){
//        if(pos==0){
//            Toast.makeText(getApplicationContext(),"Please Choose Valid Paper",Toast.LENGTH_SHORT).show();
//        }else{
//            if(testtype.equalsIgnoreCase("practise")){
//                Intent i=new Intent(getApplicationContext(),ListofPractiseTests.class);
//                Log.e("JSON---",courseid+paperidList.get(pos-1));
//                i.putExtra("enrollid",enrollid);
//                i.putExtra("courseid",courseid);
//                i.putExtra("paperid",paperidList.get(pos-1));
//                startActivity(i);
//                finish();
//            }else{
//                Intent i=new Intent(getApplicationContext(),ListofAssesmentTests.class);
//                Log.e("JSON---",courseid+paperidList.get(pos-1));
//                i.putExtra("enrollid",enrollid);
//                i.putExtra("courseid",courseid);
//                i.putExtra("paperid",paperidList.get(pos-1));
//                startActivity(i);
//                finish();
//            }
//        }
//    }

    public void setData(){
//        if (paperList.size() != 0) {
//            Log.e("Paperlist.size()", "comes:" + paperList.size());
//            tv_emptypapers.setVisibility(View.GONE);
//            pAdp = new PaperAdapter(paperList,PaperActivity.this,enrollid,courseid);
//            myLayoutManager = new LinearLayoutManager(PaperActivity.this, LinearLayoutManager.VERTICAL,false);
//            rv_papers.setLayoutManager(myLayoutManager);
//            rv_papers.setItemAnimator(new DefaultItemAnimator());
//            rv_papers.setAdapter(pAdp);
//        } else {
//            rv_papers.setAdapter(null);
//            tv_emptypapers.setText("No Papers for course");
//            tv_emptypapers.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(PaperActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                navigateTestActivity();
            }
            else {
                Toast.makeText(PaperActivity.this, "This permission required to use full functionality of application!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
