package com.digywood.tms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.Adapters.ServerAdapter;
import com.digywood.tms.AsynTasks.AsyncCheckInternet;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.SingleServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListofServers extends AppCompatActivity {

    ArrayList<SingleServer> serverList=new ArrayList<>();
    LinearLayoutManager myLayoutManager;
    SharedPreferences.Editor editor;
    RecyclerView rv_servers;
    TextView tv_emptyservers;
    Button btn_setserver;
    ServerAdapter sAdp;
    DBHelper myhelper;
    String studentid="",snumber="",selectedserver="",spersonname="",email="",serverId="",finalUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_servers);

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

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            studentid=cmgintent.getStringExtra("studentid");
            snumber=cmgintent.getStringExtra("number");
            spersonname=cmgintent.getStringExtra("sname");
            email=cmgintent.getStringExtra("email");
        }

        rv_servers=findViewById(R.id.rv_serverlist);
        tv_emptyservers=findViewById(R.id.tv_emptyserverdata);
        btn_setserver=findViewById(R.id.btn_setserver);

        btn_setserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    selectedserver=sAdp.getSelectedServerName();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofServers",e.toString());
                }

                if(selectedserver.equalsIgnoreCase("No_Selection")){
                    Toast.makeText(getApplicationContext(),"Please Set Server",Toast.LENGTH_SHORT).show();
                }else{

                    if(selectedserver.equalsIgnoreCase("main_server")){
                        editor = getSharedPreferences("SERVERPREF", MODE_PRIVATE).edit();
                        editor.putString("servername",selectedserver);
                        editor.apply();
                        finish();
                        Intent i=new Intent(getApplicationContext(),DashBoardNavActivity.class);
                        i.putExtra("connection",selectedserver);
                        i.putExtra("studentid",studentid);
                        i.putExtra("number",snumber);
                        i.putExtra("sname",spersonname);
                        i.putExtra("email",email);
                        startActivity(i);
                    }else{
//                        serverId=myhelper.getServerId(selectedserver);
//                        finalUrl="http://"+serverId+URLClass.loc_hosturl;
//
//                        hmap.clear();
//                        hmap.put("email",snumber);
//                        Log.e("FINALURL:--",""+finalUrl);
//                        new BagroundTask(finalUrl+"checkUserExist.php",hmap,ListofServers.this,new IBagroundListener() {
//                            @Override
//                            public void bagroundData(String json) {
//                                try{
//                                    if(json!=null){
//                                        Toast.makeText(getApplicationContext(),"Server:   "+selectedserver,Toast.LENGTH_SHORT).show();
//                                        editor = getSharedPreferences("SERVERPREF", MODE_PRIVATE).edit();
//                                        editor.putString("servername",selectedserver);
//                                        editor.apply();
//                                        finish();
//                                        Toast.makeText(getApplicationContext(),"Connection Ok",Toast.LENGTH_SHORT).show();
//                                    }else{
//                                        Toast.makeText(getApplicationContext(),"This Connection is not available now",Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }catch (Exception e){
//
//                                    e.printStackTrace();
//                                    Log.e("MainActivity----",e.toString());
//
//                                }
//                            }
//                        }).execute();

                        editor = getSharedPreferences("SERVERPREF", MODE_PRIVATE).edit();
                        editor.putString("servername",selectedserver);
                        editor.apply();
                        finish();
                        Intent i=new Intent(getApplicationContext(),DashBoardNavActivity.class);
                        i.putExtra("connection",selectedserver);
                        i.putExtra("studentid",studentid);
                        i.putExtra("number",snumber);
                        i.putExtra("sname",spersonname);
                        i.putExtra("email",email);
                        startActivity(i);

                    }
                }
            }
        });

        myhelper=new DBHelper(this);

        getLocalServersDataFromLocal();

    }

    public void getLocalServersDataFromLocal(){
        serverList.clear();

        Cursor mycursor=myhelper.getAvailLocalServers();
        if(mycursor.getCount()>0){
            serverList.add(new SingleServer("","","main_server"));
            while (mycursor.moveToNext()){
                serverList.add(new SingleServer(mycursor.getString(mycursor.getColumnIndex("branchId")),mycursor.getString(mycursor.getColumnIndex("serverId")),mycursor.getString(mycursor.getColumnIndex("serverName"))));
            }
        }else{
            mycursor.close();
        }

        if (serverList.size() != 0) {
            Log.e("serverlist.size()", "comes:" + serverList.size());
            tv_emptyservers.setVisibility(View.GONE);
            btn_setserver.setVisibility(View.VISIBLE);
            sAdp = new ServerAdapter(serverList,ListofServers.this);
            myLayoutManager = new LinearLayoutManager(ListofServers.this,RecyclerView.VERTICAL,false);
            rv_servers.setLayoutManager(myLayoutManager);
            rv_servers.setItemAnimator(new DefaultItemAnimator());
            rv_servers.setAdapter(sAdp);
        } else {
            rv_servers.setAdapter(null);
            tv_emptyservers.setText("No Tests Attempt History");
            tv_emptyservers.setVisibility(View.VISIBLE);
            btn_setserver.setVisibility(View.INVISIBLE);
        }

    }

    public void getLocalServersData(){
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("studentId",studentid);
        new BagroundTask(URLClass.hosturl +"getAllLocalServers.php",hmap,ListofServers.this, new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try{
                    Log.e("ListofServers---",json);
                    if(json.equalsIgnoreCase("Servers_Not_Exist")){
                        Toast.makeText(getApplicationContext(),"No Local Servers Exist",Toast.LENGTH_SHORT).show();
                    }else{
                        JSONArray ja_locservers=new JSONArray(json);
                        JSONObject serverObj=null;
                        int p=0,q=0,r=0,s=0;
                        if(ja_locservers!=null && ja_locservers.length()>0){
                            for(int i=0;i<ja_locservers.length();i++){
                                serverObj=ja_locservers.getJSONObject(i);

                                Cursor mycursor=myhelper.checkLocServerRecord(serverObj.getInt("serverKey"));

                                if(mycursor.getCount()>0){
                                    long updateFlag=myhelper.updateServerRecord(serverObj.getInt("serverKey"),serverObj.getString("orgId"),serverObj.getString("branchId"),serverObj.getString("serverName"),
                                            serverObj.getString("serverId"),serverObj.getString("status"),serverObj.getString("createdBy"),serverObj.getString("createdDttm"),serverObj.getString("modifiedBy"),serverObj.getString("modifiedDttm"));
                                    if(updateFlag>0){
                                        r++;
                                    }else{
                                        s++;
                                    }
                                }else{
                                    long insertFlag=myhelper.insertServerRecord(serverObj.getInt("serverKey"),serverObj.getString("orgId"),serverObj.getString("branchId"),serverObj.getString("serverName"),
                                            serverObj.getString("serverId"),serverObj.getString("status"),serverObj.getString("createdBy"),serverObj.getString("createdDttm"),serverObj.getString("modifiedBy"),serverObj.getString("modifiedDttm"));
                                    if(insertFlag>0){
                                        p++;
                                    }else{
                                        q++;
                                    }
                                }
                            }
                            Log.e("ListofServers---","Inserted:--"+p+"  Updated:--"+r);
                        }else{
                            Log.e("ListofServers---","Empty Servers Json Array");
                        }
                    }
                    finish();
                    startActivity(getIntent());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofServers---",e.toString());
                }
            }
        }).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listservers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_syncservers) {

            new AsyncCheckInternet(ListofServers.this, new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        getLocalServersData();
                    }else{
                        Toast.makeText(getApplicationContext(),"No internet,Please Check your connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }).execute();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {

        Intent i=new Intent(getApplicationContext(),DashBoardNavActivity.class);
        i.putExtra("studentid",studentid);
        i.putExtra("sname",spersonname);
        i.putExtra("number",snumber);
        i.putExtra("email",email);
        startActivity(i);
        this.finish();

        return super.onSupportNavigateUp();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent i=new Intent(getApplicationContext(),DashBoardNavActivity.class);
            i.putExtra("studentid",studentid);
            i.putExtra("sname",spersonname);
            i.putExtra("number",snumber);
            i.putExtra("email",email);
            startActivity(i);
            this.finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
