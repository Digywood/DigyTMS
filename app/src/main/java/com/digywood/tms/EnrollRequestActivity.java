package com.digywood.tms;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.Adapters.DataAdaper;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.Pojo.SingleBatch;
import com.digywood.tms.Pojo.SingleBatchdata;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

public class EnrollRequestActivity extends AppCompatActivity {

    Spinner sp_org,sp_course,sp_branch,sp_batch;
    ArrayList<String> orgids;
    ArrayList<String> orgnames;
    ArrayList<String> courseids;
    ArrayList<String> coursenames;
    ArrayList<String> branchids;
    ArrayList<String> branchnames;
    ArrayList<String> batchids;
    ArrayList<String> batchnames;
    ArrayList<Double> batchfeeamounts;
    ArrayList<Double> batchtaxes;
    ArrayList<SingleBatch> batchList;
    ArrayList<SingleBatchdata> batchdataList;
    TextView tv_amount,tv_tax,tv_total,tv_emptybatchdata;
    Random random;
    RecyclerView rv_batchdata;
    Button btn_reqenroll;
    DataAdaper dAdp;
    LinearLayoutManager myLayoutManager;
    String startdate="",enddate="",studentid="",batchid="";
    String orgid="",courseid="",branchid="",feeamount="",feetax="",totalfee="",RandomAudioFileName="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    ArrayAdapter<String> orgAdp,courseAdp,branchAdp,batchAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_request);

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

        sp_org=findViewById(R.id.sp_organisations);
        sp_course=findViewById(R.id.sp_courses);
        sp_branch=findViewById(R.id.sp_branches);
        sp_batch=findViewById(R.id.sp_batches);
        tv_amount=findViewById(R.id.tv_amount);
        tv_tax=findViewById(R.id.tv_tax);
        rv_batchdata=findViewById(R.id.rv_listofbatchusers);
        tv_emptybatchdata=findViewById(R.id.tv_batchemptydata);
        tv_total=findViewById(R.id.tv_totalamount);
        btn_reqenroll=findViewById(R.id.btn_enrollreq);
        orgids=new ArrayList<>();
        orgnames=new ArrayList<>();
        courseids=new ArrayList<>();
        coursenames=new ArrayList<>();
        branchids=new ArrayList<>();
        branchnames=new ArrayList<>();
        batchids=new ArrayList<>();
        batchnames=new ArrayList<>();
        batchtaxes=new ArrayList<>();
        batchfeeamounts=new ArrayList<>();
        batchList=new ArrayList<>();
        batchdataList=new ArrayList<>();

        random=new Random();

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
        }

        new AsyncCheckInternet(EnrollRequestActivity.this,new INetStatus() {
            @Override
            public void inetSatus(Boolean netStatus) {
                if(netStatus){
                    getOrganisations();
                }else{
                    Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();

        sp_org.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    orgid="";
                    courseid="";
                    branchid="";
                    batchid="";
                    courseids.clear();
                    coursenames.clear();
                    sp_course.setAdapter(null);
                    branchids.clear();
                    branchnames.clear();
                    sp_branch.setAdapter(null);
                    batchnames.clear();
                    batchList.clear();
                    batchdataList.clear();
                    sp_batch.setAdapter(null);
                    tv_tax.setText(null);
                    tv_total.setText(null);
                    tv_amount.setText(null);
                }else{
                    orgid=orgids.get(position-1);
                    courseid="";
                    branchid="";
                    batchid="";
                    courseids.clear();
                    coursenames.clear();
                    branchids.clear();
                    branchnames.clear();
                    batchnames.clear();
                    batchList.clear();
                    batchdataList.clear();
                    sp_batch.setAdapter(null);
                    tv_tax.setText(null);
                    tv_total.setText(null);
                    tv_amount.setText(null);
                    getCoursesandBranchs(orgids.get(position-1));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    courseid="";
                    batchid="";
                    batchnames.clear();
                    batchList.clear();
                    batchdataList.clear();
                    sp_batch.setAdapter(null);
                }else{
                    courseid=courseids.get(position-1);
                    batchid="";
                    getBatches();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    branchid="";
                    batchid="";
                    batchnames.clear();
                    batchList.clear();
                    batchdataList.clear();
                    sp_batch.setAdapter(null);
                }else{
                    batchid="";
                    branchid=branchids.get(position-1);
                    getBatches();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    Toast.makeText(getApplicationContext(),"Please Choose A valid Batch",Toast.LENGTH_SHORT).show();
                    tv_tax.setText(null);
                    tv_total.setText(null);
                    tv_amount.setText(null);
                }else{
                    SingleBatch sb=batchList.get(position-1);

                    Double amount=sb.getFeeamount();
                    feeamount=String.valueOf(sb.getFeeamount());
                    feetax=String.valueOf(sb.getFeetax());
                    totalfee=String.valueOf(amount+amount*((sb.getFeetax()/100)));
                    tv_tax.setText(feetax+"%");
                    tv_total.setText(totalfee);
                    tv_amount.setText(feeamount);

                    startdate=sb.getStartdate();
                    enddate=sb.getEnddate();

                    batchid=sb.getId();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_reqenroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    new AsyncCheckInternet(EnrollRequestActivity.this,new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            insertEnrollRequest();
                        }
                    }).execute();
                }else{

                }
            }
        });

    }

    public void setData(){
        if (batchdataList.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + batchdataList.size());
            tv_emptybatchdata.setVisibility(View.GONE);
            dAdp = new DataAdaper(batchdataList,EnrollRequestActivity.this);
            myLayoutManager = new LinearLayoutManager(EnrollRequestActivity.this, LinearLayoutManager.VERTICAL,false);
            rv_batchdata.setLayoutManager(myLayoutManager);
            rv_batchdata.setItemAnimator(new DefaultItemAnimator());
            rv_batchdata.setAdapter(dAdp);
        } else {
            rv_batchdata.setAdapter(null);
            tv_emptybatchdata.setText("No Facilitators for branch");
            tv_emptybatchdata.setVisibility(View.VISIBLE);
        }
    }

    public void getOrganisations(){
        orgids.clear();
        orgnames.clear();
        HashMap<String,String> hmap=new HashMap<>();
        new BagroundTask(URLClass.hosturl +"getAllOrgainsations.php", hmap, EnrollRequestActivity.this,new IBagroundListener() {
         @Override
         public void bagroundData(String json) {
             try{
                 Log.e("EnrollReqActivity---",json);
                 if(json.equalsIgnoreCase("Organisations_Not_Exist")){
                     Toast.makeText(getApplicationContext(),"No Organisations",Toast.LENGTH_SHORT).show();
                 }else{
                     JSONObject myObj=null;
                     JSONArray ja=new JSONArray(json);
                     orgnames.add("Select");
                     for(int i=0;i<ja.length();i++){

                         myObj=ja.getJSONObject(i);
                         orgids.add(myObj.getString("Ctr_orga_id"));
                         orgnames.add(myObj.getString("Ctr_Name"));

                     }
                 }
                 orgAdp= new ArrayAdapter(EnrollRequestActivity.this,android.R.layout.simple_spinner_item,orgnames);
                 orgAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                 sp_org.setAdapter(orgAdp);
             }catch (Exception e){
                 e.printStackTrace();
                 Log.e("EnrollReqActivity--",e.toString());
             }
         }
     }).execute();
    }

    public void getCoursesandBranchs(String orgid){
        courseids.clear();
        coursenames.clear();
        branchids.clear();
        branchnames.clear();
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("OrgId",orgid);
        new BagroundTask(URLClass.hosturl +"getCoursesandBranchesByOrg.php",hmap,EnrollRequestActivity.this, new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                JSONObject courseObj,branchObj;
                JSONArray ja_course_table,ja_branch_table;

                try{
                    Log.e("EnrollReqActivity---",json);
                    JSONObject myObj=new JSONObject(json);

                    Object obj1=myObj.get("courses");

                    if (obj1 instanceof JSONArray)
                    {
                        ja_course_table=myObj.getJSONArray("courses");
                        if(ja_course_table!=null && ja_course_table.length()>0){
                            Log.e("courseLength---",""+ja_course_table.length());
                            coursenames.add("Select");
                            for(int i=0;i<ja_course_table.length();i++){

                                courseObj=ja_course_table.getJSONObject(i);
                                courseids.add(courseObj.getString("Course_ID"));
                                coursenames.add(courseObj.getString("Course_Name"));
                            }
                        }else{
                            Log.e("Courses--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Courses--","No Courses: ");
                    }

                    Object obj2=myObj.get("branches");

                    if (obj2 instanceof JSONArray)
                    {
                        ja_branch_table=myObj.getJSONArray("branches");
                        if(ja_branch_table!=null && ja_branch_table.length()>0){
                            Log.e("branchLength---",""+ja_branch_table.length());
                            branchnames.add("Select");
                            for(int i=0;i<ja_branch_table.length();i++){

                                branchObj=ja_branch_table.getJSONObject(i);
                                branchids.add(branchObj.getString("br_Branch_ID"));
                                branchnames.add(branchObj.getString("br_Name"));

                            }
                        }else{
                            Log.e("Branches--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("Branches--","No Branches: ");
                    }

                    courseAdp= new ArrayAdapter(EnrollRequestActivity.this,android.R.layout.simple_spinner_item,coursenames);
                    courseAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_course.setAdapter(courseAdp);

                    branchAdp= new ArrayAdapter(EnrollRequestActivity.this,android.R.layout.simple_spinner_item,branchnames);
                    branchAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_branch.setAdapter(branchAdp);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("EnrollReqActivity--",e.toString());
                }
            }
        }).execute();
    }

    public void getBatches(){
        batchnames.clear();
        batchList.clear();
        batchdataList.clear();
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("OrgId",orgid);
        hmap.put("CourseId",courseid);
        hmap.put("BranchId",branchid);
        Log.e("EnrollReqActivity---","ORG: "+orgid+"  CID: "+courseid+"  BID: "+branchid);
        new BagroundTask(URLClass.hosturl+"getBatches.php",hmap,EnrollRequestActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                JSONArray ja_batch_table,ja_batchdata_table;
                JSONObject batchObj,batchdataObj;
                try{
                    Log.e("EnrollReqActivity---",json);
                    JSONObject myObj=new JSONObject(json);

                    Object obj1=myObj.get("batches");

                    if (obj1 instanceof JSONArray)
                    {
                        ja_batch_table=myObj.getJSONArray("batches");
                        if(ja_batch_table!=null && ja_batch_table.length()>0){
                            Log.e("batchLength---",""+ja_batch_table.length());
                            batchnames.add("Select");
                            for(int i=0;i<ja_batch_table.length();i++){

                                batchObj=ja_batch_table.getJSONObject(i);
                                batchnames.add(batchObj.getString("Batch_Name"));
//                            batchfeeamounts.add(batchObj.getDouble("batch_Fee_Amount"));
//                            batchtaxes.add(batchObj.getDouble("batch_Fee_tax_percentage"));
                                batchList.add(new SingleBatch(batchObj.getString("Batch_ID"),batchObj.getString("Batch_Name"),batchObj.getString("batch_start_Date"),batchObj.getString("batch_end_Date"),batchObj.getDouble("batch_Fee_Amount"),batchObj.getDouble("batch_Fee_tax_percentage")));

                            }
                        }else{
                            Log.e("Batches--","Empty Json Array: ");
                        }
                    }
                    else {
                        tv_amount.setText(null);
                        Toast.makeText(getApplicationContext(),"No Batches",Toast.LENGTH_SHORT).show();
                        Log.e("Batches--","No Batches: ");
                    }

                    batchAdp= new ArrayAdapter(EnrollRequestActivity.this,android.R.layout.simple_spinner_item,batchnames);
                    batchAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_batch.setAdapter(batchAdp);

                    Object obj2=myObj.get("batchdata");

                    if (obj2 instanceof JSONArray)
                    {
//                        long subdelcount=myhelper.deleteAllSubjects();
//                        Log.e("subdelcount---",""+subdelcount);
                        ja_batchdata_table=myObj.getJSONArray("batchdata");
                        if(ja_batchdata_table!=null && ja_batchdata_table.length()>0){
                            Log.e("batchdataLength---",""+ja_batchdata_table.length());
                            for(int i=0;i<ja_batchdata_table.length();i++){

                                batchdataObj=ja_batchdata_table.getJSONObject(i);
                                batchdataList.add(new SingleBatchdata(batchdataObj.getString("batch_plannername"),batchdataObj.getString("batch_plannernumber"),batchdataObj.getString("batch_plannermail")));
//                                long insertFlag=myhelper.insertSubject(batchdataObj.getInt("Subject_key"),batchdataObj.getString("Course_ID"),batchdataObj.getString("Subject_ID"),batchdataObj.getString("Subject_Name"),
//                                        batchdataObj.getString("Subject_ShortName"),batchdataObj.getInt("Subject_Seq_no"),batchdataObj.getString("Subject_Type"),batchdataObj.getString("Subject_status"));
//                                if(insertFlag>0){
//                                    p++;
//                                }else {
//                                    q++;
//                                }
                            }
//                            Log.e("BackGroundTask--","Inserted: "+p);
                        }else{
                            Log.e("BatchData--","Empty Json Array: ");
                        }
                    }
                    else {
                        Log.e("BatchData--","No BatchData: ");
                    }

                    setData();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("EnrollReqActivity--",e.toString());
                }
            }
        }).execute();
    }

    public void insertEnrollRequest(){

        HashMap<String,String> hashmap=new HashMap<>();
        hashmap.put("StudentId",studentid);
        hashmap.put("CourseId",courseid);
        hashmap.put("BatchId",batchid);
        new BagroundTask(URLClass.hosturl +"checkEnrollment.php",hashmap,EnrollRequestActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("EnrollReqActivity----","json_comes:-"+json);
                    if(json.equalsIgnoreCase("Enroll_Exist")){
                        showAlert("Enrollment Already Exist with provided Branch and Course,Please Use Existing One");
                    }else{

                        HashMap<String,String> hmap=new HashMap<>();
                        hmap.put("StudentId",studentid);
                        hmap.put("OrgId",orgid);
                        hmap.put("CourseId",courseid);
                        hmap.put("BranchId",branchid);
                        hmap.put("BatchId",batchid);
                        hmap.put("startDate",startdate);
                        hmap.put("endDate",enddate);
                        String androidid= Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
                        hmap.put("DeviceId",androidid);
                        hmap.put("StartDate",timeStamp);
                        hmap.put("feetype","INR");
                        hmap.put("feeamount",feeamount);
                        hmap.put("feetax",feetax);
                        hmap.put("totalfee",totalfee);
                        hmap.put("activationkey",CreateRandomString(8));
                        new BagroundTask(URLClass.hosturl+"insertEnrollRequest.php",hmap,EnrollRequestActivity.this,new IBagroundListener() {
                            @Override
                            public void bagroundData(String json) {
                                try{
                                    Log.e("EnrollReqActivity---",json);
                                    if(json.equalsIgnoreCase("Inserted")){
                                        Toast.makeText(getApplicationContext(),"Request Generated Succesfully",Toast
                                                .LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Request Generation Failed",Toast
                                                .LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Log.e("EnrollReqActivity--",e.toString());
                                }
                            }
                        }).execute();

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("EnrollReqActivity----",""+e.toString());
                }
            }
        }).execute();

    }

    public boolean validate() {
        boolean valid = true;

        String org = sp_org.getSelectedItem().toString();
        String course=null,branch=null,batch=null;
        if(courseids.size()!=0){
            course = sp_course.getSelectedItem().toString();
        }else{
            course="";
        }
        if(branchids.size()!=0){
            branch = sp_branch.getSelectedItem().toString();
        }else{
            branch="";
        }
        if(batchnames.size()!=0){
            batch = sp_batch.getSelectedItem().toString();
        }else{
            batch="";
        }

        if (org.isEmpty() || org.equalsIgnoreCase("Select")) {
            Toast.makeText(getApplicationContext(),"Please Choose Valid Organisation",Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            if (course.isEmpty() || course.equalsIgnoreCase("Select")) {
                Toast.makeText(getApplicationContext(),"Please Choose Valid Course",Toast.LENGTH_SHORT).show();
                valid = false;
            } else {
                if (branch.isEmpty() || branch.equalsIgnoreCase("Select")) {
                    Toast.makeText(getApplicationContext(),"Please Choose Valid Branch",Toast.LENGTH_SHORT).show();
                    valid = false;
                } else {
                    if (batch.isEmpty() || batch.equalsIgnoreCase("Select")) {
                        Toast.makeText(getApplicationContext(),"Please Choose Valid Batch",Toast.LENGTH_SHORT).show();
                        valid = false;
                    } else {

                    }
                }
            }
        }

        return valid;
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

    public String CreateRandomString(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    public  void showAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(EnrollRequestActivity.this);
        builder.setMessage(messege)
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Info!");
        alert.setIcon(R.drawable.info);
        alert.show();
    }

}
