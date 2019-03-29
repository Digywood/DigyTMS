package com.digywood.tms;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class RegistrationActivity extends AppCompatActivity {

    EditText et_name,et_addressline1,et_addressline2,et_city,et_state,et_country,et_number,et_email,et_password,et_cnf_password;
    Switch sw_gender;
    TextView tv_male,tv_female,tv_dob;
    int yyyy,mm,dd,qulifypos;
    String gender="M",dob="";
    Spinner sp_qualification;
    Button btn_calender,btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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

        et_name=findViewById(R.id.et_name);
        tv_dob=findViewById(R.id.tv_dob);
        et_addressline1=findViewById(R.id.et_address1);
        et_addressline2=findViewById(R.id.et_address2);
        et_city=findViewById(R.id.et_city);
        et_state=findViewById(R.id.et_state);
        et_country=findViewById(R.id.et_country);
        et_number=findViewById(R.id.et_phnumber);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        et_cnf_password=findViewById(R.id.et_cnf_password);
        sw_gender=findViewById(R.id.sw_gender);
        tv_male=findViewById(R.id.tv_male);
        tv_male.setTextColor(this.getResources().getColor(R.color.colorAccent));
        tv_female=findViewById(R.id.tv_female);
        sp_qualification=findViewById(R.id.sp_qualification);

        btn_calender=findViewById(R.id.btn_calender);
        btn_submit=findViewById(R.id.btn_submit);

        btn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                yyyy = c.get(Calendar.YEAR);
                mm = c.get(Calendar.MONTH);
                dd = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                                tv_dob.setText(year +"-"+(monthOfYear + 1)+"-"+dayOfMonth);
                                dob=""+year +"-"+(monthOfYear + 1)+"-"+dayOfMonth;

                            }
                        },yyyy,mm,dd);
                datePickerDialog.show();
            }
        });

        sp_qualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                qulifypos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    final HashMap<String,String> hmap=new HashMap<>();
                    hmap.put("name",et_name.getText().toString());
                    hmap.put("gender",gender);
                    hmap.put("education",String.valueOf(qulifypos));
                    hmap.put("dob",tv_dob.getText().toString());
                    hmap.put("address1",et_addressline1.getText().toString());
                    hmap.put("address2",et_addressline2.getText().toString());
                    hmap.put("city",et_city.getText().toString());
                    hmap.put("state",et_state.getText().toString());
                    hmap.put("country",et_country.getText().toString());
                    hmap.put("mobile",et_number.getText().toString());
                    hmap.put("email",et_email.getText().toString());


                    /*try {
                        //Log.e("RegistrationActivity","Original Password:"+et_password.getText().toString());
                        String enc_pwd=CryptUtil.encrypt(et_password.getText().toString());
                        hmap.put("password",enc_pwd);
                        //Log.e("RegistrationActivity","Encrypt Password:"+enc_pwd);
                        //Log.e("RegistrationActivity","Decrypt Password:"+CryptUtil.decrypt(enc_pwd));
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }*/


                    hmap.put("password",et_password.getText().toString());
                    String androidid=Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
                    hmap.put("macid",androidid);
                    String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd:HH-mm").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
                    hmap.put("createtime",timeStamp);

                    new AsyncCheckInternet(RegistrationActivity.this,new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            new BagroundTask(URLClass.hosturl+"insertStudentDetails.php", hmap, RegistrationActivity.this, new IBagroundListener() {
                                @Override
                                public void bagroundData(String json) {
                                    try{

                                        Log.e("RegResponse--",json);
                                        if(json.equalsIgnoreCase("User_Exist")){
                                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(i);
                                            finish();
                                            Toast.makeText(getApplicationContext(),"User Already Exist,Please Log in",Toast.LENGTH_SHORT).show();

                                        }else{
                                            if(json.equalsIgnoreCase("Inserted")){

                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                                                builder.setMessage("Registration Successfully Done...")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                dialog.cancel();

                                                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                                                startActivity(i);
                                                                finish();

                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                //Setting the title manually
                                                alert.setTitle("Registration");
                                                alert.setIcon(R.drawable.info);
                                                alert.show();


                                                //Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Log.e("RegistationActivity--",e.toString());
                                    }
                                }
                            }).execute();
                        }
                    }).execute();
                }else{

                }
            }
        });

        sw_gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(sw_gender.isChecked()){
                    tv_male.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                    tv_female.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                    gender="F";
                }else{
                    tv_male.setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
                    tv_female.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                    gender="M";
                }
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        String name = et_name.getText().toString();
        String addressline = et_addressline1.getText().toString();
        String area = et_addressline2.getText().toString();
        String city = et_city.getText().toString();
        String state = et_state.getText().toString();
        String country = et_country.getText().toString();
        String contactnumber = et_number.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String cnf_password = et_cnf_password.getText().toString();

        if (name.isEmpty()) {
            et_name.setError("Please enter a valid name");
            valid = false;
        } else {
            et_name.setError(null);
        }

        if (addressline.isEmpty()) {
            et_addressline1.setError("Please enter a valid adrress");
            valid = false;
        } else {
            et_addressline1.setError(null);
        }

        if (area.isEmpty()) {
            et_addressline2.setError("Please enter a valid area");
            valid = false;
        } else {
            et_addressline2.setError(null);
        }

        if (city.isEmpty()) {
            et_city.setError("Please enter a valid city");
            valid = false;
        } else {
            et_city.setError(null);
        }

        if (state.isEmpty()) {
            et_state.setError("Please enter a valid state");
            valid = false;
        } else {
            et_state.setError(null);
        }

        if (country.isEmpty()) {
            et_country.setError("Please enter a valid country");
            valid = false;
        } else {
            et_country.setError(null);
        }

        if (email.isEmpty() || !email.contains("@")) {
            et_email.setError("enter a valid email address");
            valid = false;
        } else {
            et_email.setError(null);
        }

        if (contactnumber.isEmpty() || contactnumber.length()!=10) {
            et_number.setError("enter a valid number");
            valid = false;
        } else {
            et_number.setError(null);
        }

        if(dob.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(),"Please Choose a valid Date of Birth",Toast.LENGTH_SHORT).show();
            valid=false;
        }else{

        }

        if(valid){
            if (password.isEmpty() || password.length() < 6) {
                showAlert("password should be greater than or equal to 6 alphanumeric characters");
                valid=false;
            } else {

            }
        }

        if(!password.equalsIgnoreCase(cnf_password)){
            Toast.makeText(getApplicationContext(),"Password and Confirm Paasword should be same",Toast.LENGTH_SHORT).show();
            valid=false;
        }

        return valid;
    }

    public  void showAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setMessage(messege)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
