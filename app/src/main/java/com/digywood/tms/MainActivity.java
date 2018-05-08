package com.digywood.tms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btn_login;
    TextView tv_signup;
    CheckBox cb_remember;
    String studentid,studentname="",semail="";
    DBHelper myhelper;
    EditText et_email,et_password;
    String pwd="",enterpwd;
    JSONParser myparser;
    SharedPreferences mypreferences,myretrievepreferences;
    SharedPreferences.Editor editor;
    HashMap<String,String> hmap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login=findViewById(R.id.btn_login);
        tv_signup=findViewById(R.id.tv_signup);
        et_email=findViewById(R.id.et_email);
        cb_remember=findViewById(R.id.cb_remember);
        et_password=findViewById(R.id.et_password);

        myhelper=new DBHelper(this);

        myretrievepreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        et_email.setText(myretrievepreferences.getString("number",""));
        et_password.setText(myretrievepreferences.getString("password",""));
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        cb_remember.setTypeface(font);
        tv_signup.setTypeface(font);
        btn_login.setTypeface(font);
        mypreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mypreferences.edit();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_email.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Number",Toast.LENGTH_SHORT).show();
                }else {
                    if(et_password.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_SHORT).show();
                    }else {
                        if (cb_remember.isChecked()) {
                            editor.putString("number",et_email.getText().toString());
                            editor.putString("password",et_password.getText().toString());
                            editor.commit();
                        }else{

                        }

                        enterpwd=et_password.getText().toString();
                        Cursor mycursor=myhelper.checkStudent(et_email.getText().toString());
                        if(mycursor.getCount()>0){
                            String localpwd="";
                            while (mycursor.moveToNext()) {
                                localpwd=mycursor.getString(mycursor.getColumnIndex("Student_password"));
                                studentid=mycursor.getString(mycursor.getColumnIndex("StudentID"));
                                studentname=mycursor.getString(mycursor.getColumnIndex("Student_Name"));
                                semail=mycursor.getString(mycursor.getColumnIndex("Student_email"));
                            }
                            if(enterpwd.equalsIgnoreCase(localpwd)){
                                Intent i=new Intent(getApplicationContext(),DashBoardNavActivity.class);
                                i.putExtra("studentid",studentid);
                                i.putExtra("sname",studentname);
                                i.putExtra("email",semail);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            new AsyncCheckInternet(MainActivity.this,new INetStatus() {
                                @Override
                                public void inetSatus(Boolean netStatus) {
                                    if(netStatus){
                                        hmap.clear();
                                        hmap.put("number",et_email.getText().toString());
                                        new BagroundTask(URLClass.hosturl +"checkUserExist.php",hmap,MainActivity.this, new IBagroundListener() {
                                            @Override
                                            public void bagroundData(String json) {
                                                try{
                                                    if(json.equalsIgnoreCase("User_Not_Exist")){
                                                        Toast.makeText(getApplicationContext(),"User_Not_Exist",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        JSONObject jo=null;
                                                        JSONArray ja=new JSONArray(json);
                                                        for (int i=0;i<ja.length();i++){
                                                            jo=ja.getJSONObject(i);
                                                            studentid=jo.getString("StudentID");
                                                            pwd=jo.getString("Student_password");
                                                            studentname=jo.getString("Student_Name");
                                                            semail=jo.getString("Student_email");

                                                            long checkFlag=myhelper.checkStudent(jo.getInt("StudentKey"));
                                                            if(checkFlag>0){
                                                                Log.e("MainActivity----","Student Exists in Local");
                                                            }else{
                                                                long insertFlag=myhelper.insertStudent(jo.getInt("StudentKey"),jo.getString("StudentID"),jo.getString("Student_Name"),jo.getString("Student_gender"),jo.getString("Student_Education"),jo.getString("Student_DOB"),jo.getString("Student_Address01"),jo.getString("Student_Address02"),jo.getString("Student_City"),jo.getString("Student_State"),
                                                                        jo.getString("Student_Country"),jo.getString("Student_Mobile"),jo.getString("Student_email"),jo.getString("Student_password"),jo.getString("Student_mac_id"),jo.getString("Student_Status"),jo.getString("Student_created_by"),jo.getString("Student_created_DtTm"));
                                                                if(insertFlag>0){
                                                                    Log.e("MainActivity----","Student Inserted in Local");
                                                                }else{
                                                                    Log.e("MainActivity----","Local Student Insertion Failed");
                                                                }
                                                            }
                                                        }
                                                        if(enterpwd.equalsIgnoreCase(pwd)){
                                                            Intent i=new Intent(getApplicationContext(),DashBoardNavActivity.class);
                                                            i.putExtra("studentid",studentid);
                                                            i.putExtra("sname",studentname);
                                                            i.putExtra("email",semail);
                                                            startActivity(i);
                                                            finish();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                }catch (Exception e){

                                                    e.printStackTrace();
                                                    Log.e("MainActivity----",e.toString());

                                                }
                                            }
                                        }).execute();
                                    }else {
                                        Toast.makeText(getApplicationContext(),"No internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).execute();

                        }
                    }
                }
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(i);

            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_refresh:
//                new AsyncCheckInternet(MainActivity.this, new INetStatus() {
//                    @Override
//                    public void inetSatus(Boolean netStatus) {
//                        if(netStatus){
//                            new AsyncCheckInternet(MainActivity.this, new INetStatus() {
//                                @Override
//                                public void inetSatus(Boolean netStatus) {
//                                    getStudentAllData();
//                                }
//                            }).execute();
//                        }else{
//                            Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }).execute();
//                return  true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
