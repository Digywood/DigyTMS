package com.digywood.tms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {
    EditText et_mobile_num,et_pwd,et_cnf_pwd;
    Button btn_submit;
    String TAG="ForgotPassword";
    String final_pwd,otp,final_email;
    DBHelper myhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        et_mobile_num=findViewById(R.id.et_mobile_num);
        et_pwd=findViewById(R.id.et_pwd);
        et_cnf_pwd=findViewById(R.id.et_cnf_pwd);
        btn_submit=findViewById(R.id.btn_submit);

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
        myhelper = new DBHelper(this);



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mobile_num=et_mobile_num.getText().toString();
                String pwd=et_pwd.getText().toString();
                String cnf_pwd=et_cnf_pwd.getText().toString();

                if(!validateDetails(mobile_num,pwd,cnf_pwd))
                {
                    final_pwd=pwd;
                    new AsyncCheckInternet(ForgotPassword.this, new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if (netStatus) {
                                HashMap<String, String> hmap = new HashMap<>();
                                hmap.put("name", mobile_num);
                                hmap.put("subject", "Forgot Password");

                                Random otp  =new Random();

                                StringBuilder builder=new StringBuilder();
                                for(int count=0; count<=5;count++) {
                                    builder.append(otp.nextInt(10));
                                }
                                Log.d("Number", " " + builder.toString());
                                final String OTP=builder.toString();
                                String message = "Hi,\n" +
                                        "\n" +
                                        "You are receiving this e-mail because You have requested to change your password. Your OTP is "+OTP +". If you did not send this request, please ignore this message. Otherwise, please complete your password-reset request by using this OTP.";
                                hmap.put("message", message);
                                hmap.put("mobilenumber", mobile_num);
                                new BagroundTask(URLClass.hosturl + "sendmail.php", hmap, ForgotPassword.this,
                                        new IBagroundListener() {
                                            @Override
                                            public void bagroundData(final String json) {
                                                try {
                                                    String substr = json.substring(0, json.lastIndexOf("<br>"));
                                                    String response=json.replace(substr+"<br>","").trim();
                                                    Log.e(TAG,"emailId:"+response);
                                                    if(json!=null){
                                                        if(json.equals("USER_NOT_EXIST"))
                                                        {
                                                            Toast.makeText(ForgotPassword.this,"The User Is not Available, Please Reagister..",Toast.LENGTH_SHORT).show();
                                                        }else if(json.equals("MAIL_SENT_FAILED")){
                                                            Toast.makeText(ForgotPassword.this,"Somethisng went Wrong,Please try Again..",Toast.LENGTH_SHORT).show();
                                                        }else{

                                                            if(response.equals("MAIL_SENT_FAILED")|| response.equals("")){
                                                                Toast.makeText(ForgotPassword.this,"Somethisng went Wrong,Please try Again..",Toast.LENGTH_SHORT).show();
                                                            }else {

                                                                final_email=response;
                                                                final Dialog dialog = new Dialog(ForgotPassword.this);
                                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                dialog.setCancelable(false);
                                                                dialog.setContentView(R.layout.otp);

                                                                final EditText et_otp = (EditText) dialog.findViewById(R.id.et_otp);
                                                                TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
                                                                ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
                                                                tv_message.setText("The OTP is sent to email-id:"+final_email);


                                                                Button dialogButton = (Button) dialog.findViewById(R.id.btn_otp_submit);
                                                                iv_close.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {

                                                                        String entered_otp=et_otp.getText().toString();
                                                                        if(entered_otp.equals(OTP)){
                                                                            dialog.dismiss();

                                                                            new AsyncCheckInternet(ForgotPassword.this, new INetStatus() {
                                                                                @Override
                                                                                public void inetSatus(Boolean netStatus) {
                                                                                    if (netStatus) {
                                                                                        HashMap<String, String> hmap = new HashMap<>();
                                                                                        hmap.put("mobilenumber", mobile_num);
                                                                                        hmap.put("email", final_email);
                                                                                        hmap.put("password", final_pwd);

                                                                                        if(mobile_num!=null || final_email!=null || final_pwd!=null) {
                                                                                            new BagroundTask(URLClass.hosturl + "updatePassword.php", hmap, ForgotPassword.this, new IBagroundListener() {
                                                                                                @Override
                                                                                                public void bagroundData(String json) {
                                                                                                    if (json.equals("NOT_UPDATED")) {

                                                                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgotPassword.this);
                                                                                                        builder1.setMessage("Password Updation Failed..");
                                                                                                        builder1.setCancelable(true);

                                                                                                        builder1.setPositiveButton(
                                                                                                                "Ok",
                                                                                                                new DialogInterface.OnClickListener() {
                                                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                                                        dialog.cancel();
                                                                                                                    }
                                                                                                                });


                                                                                                        AlertDialog alert11 = builder1.create();
                                                                                                        alert11.show();


                                                                                                    } else if (json.equals("UPDATED")) {

                                                                                                        long res = myhelper.updatePassword(mobile_num, final_email, final_pwd);

                                                                                                        if (res > 0) {
                                                                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgotPassword.this);
                                                                                                            builder1.setMessage("Password Updation Success..");
                                                                                                            builder1.setCancelable(true);

                                                                                                            builder1.setPositiveButton(
                                                                                                                    "Ok",
                                                                                                                    new DialogInterface.OnClickListener() {
                                                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                                                            dialog.cancel();
                                                                                                                            finish();
                                                                                                                        }
                                                                                                                    });


                                                                                                            AlertDialog alert11 = builder1.create();
                                                                                                            alert11.show();
                                                                                                        } else {
                                                                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgotPassword.this);
                                                                                                            builder1.setMessage("Password Updation Failed..");
                                                                                                            builder1.setCancelable(true);

                                                                                                            builder1.setPositiveButton(
                                                                                                                    "Ok",
                                                                                                                    new DialogInterface.OnClickListener() {
                                                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                                                            dialog.cancel();
                                                                                                                        }
                                                                                                                    });


                                                                                                            AlertDialog alert11 = builder1.create();
                                                                                                            alert11.show();
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }).execute();
                                                                                        }

                                                                                    }
                                                                                }
                                                                            }).execute();


                                                                        }else {
                                                                            et_otp.setText("");
                                                                            Toast.makeText(ForgotPassword.this,"You Entered Wrong OTP",Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });

                                                                dialog.show();

                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "No internet,Please Check Your Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).execute();
                }
                Log.e(TAG,"mobile_num:"+mobile_num+",pwd:"+pwd);
            }
        });
    }

    private boolean validateDetails(String mobile_num,String pwd,String cnf_pwd) {
        boolean res=false;

        if(!(mobile_num.length()>0)){
            res=true;
            Toast.makeText(ForgotPassword.this,"Please Enter Mobile Number.",Toast.LENGTH_SHORT).show();
        }else if(mobile_num.length()!=10){
            res=true;
            Toast.makeText(ForgotPassword.this,"Please Enter 10 Digited mobile Number Correctly.",Toast.LENGTH_SHORT).show();
        }else if(!isValidMobile(mobile_num))
        {
            res=true;
            Toast.makeText(ForgotPassword.this,"Please Enter mobile Number Correctly.",Toast.LENGTH_SHORT).show();
        }else if(!(pwd.length()>0)) {
            res = true;
            Toast.makeText(ForgotPassword.this, "Please Enter Password.", Toast.LENGTH_SHORT).show();
        }else if(!(cnf_pwd.length()>0)) {
            res = true;
            Toast.makeText(ForgotPassword.this, "Please Enter Conform Password.", Toast.LENGTH_SHORT).show();
        } else if(!pwd.equals(cnf_pwd)){
            res = true;
            Toast.makeText(ForgotPassword.this, "Password And Conform Password Should be same.", Toast.LENGTH_SHORT).show();
        }
        return  res;
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String phone) {
        /*boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;*/

        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 6 or 7 or 8 or 9.
        // 3) Then contains 9 digits
        //exp (0/91)?[6-9][0-9]{9}

        Pattern p = Pattern.compile("[6-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(phone);
        return (m.find() && m.group().equals(phone));
    }
}
