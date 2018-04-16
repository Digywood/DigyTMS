package com.digywood.tms;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.GroupQues;
import com.digywood.tms.Pojo.SingleGroupConfig;
import com.digywood.tms.Pojo.SingleOptions;
import com.digywood.tms.Pojo.SingleSubcatConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class JSONParser extends AppCompatActivity{

    ArrayList<String> quesIdList=new ArrayList<>();
    ArrayList<String> groupList=new ArrayList<>();
    ArrayList<String> groupcompList=new ArrayList<>();
    ArrayList<String> groupcloseList=new ArrayList<>();
    ArrayList<String> subcatList=new ArrayList<>();
    ArrayList<String> gquesList=new ArrayList<>();
    ArrayList<String> quesList=new ArrayList<>();
    ArrayList<String> freeSubcatList=new ArrayList<>();
    ArrayList<String> groupQuesIdList=new ArrayList<>();
    ArrayList<String> finalgroupList=new ArrayList<>();
    ArrayList<String> testQuesList=new ArrayList<>();
    ArrayList<GroupQues> groupmainList=new ArrayList<>();
    ArrayList<SingleGroupConfig> groupConfigList=new ArrayList<>();
    ArrayList<SingleSubcatConfig> scConfigList=new ArrayList<>();
    JSONArray ja_sections =new JSONArray();
    JSONArray ja_questions =new JSONArray();
    JSONArray ja_options =new JSONArray();
    JSONArray ja_additions =new JSONArray();

    ArrayList<String> testfimages=new ArrayList<>();
    JSONObject mainObj=null,secObj=null,quesObj=null,optionObj=null,additionsObj=null;
    Random r = new Random();
    int seqno=0;
    DBHelper myhelper=new DBHelper(this);
    int count=0,maxcount=0,quescount=10,totalsubcatCount=22,testqcount=0;
    JSONObject cmainObj,csecObj,cquesObj,coptionObj,cadditionsObj;
    String section="",sectionid="",testid="",dwdpath="";

    public JSONParser(String JSON,String dwdPath){

        this.dwdpath=dwdPath;
        quesIdList.clear();

        testfimages.add("Q001.PNG");
        testfimages.add("Q002.PNG");
        testfimages.add("Q004.PNG");
        testfimages.add("Q007.PNG");
        testfimages.add("Q008.PNG");
        testfimages.add("Q010.PNG");

//        scConfigList.add(new SingleSubcatConfig("SSCE000101",5,3));
//        scConfigList.add(new SingleSubcatConfig("SSCE000102",5,2));
//        scConfigList.add(new SingleSubcatConfig("SSCR000101",5,3));
//        scConfigList.add(new SingleSubcatConfig("SSCR000201",3,2));
//        scConfigList.add(new SingleSubcatConfig("SSCR000301",4,2));
//        scConfigList.add(new SingleSubcatConfig("SSCM000101",4,2));
//        scConfigList.add(new SingleSubcatConfig("SSCM000201",3,2));
//        scConfigList.add(new SingleSubcatConfig("SSCM000301",5,3));

        try{
            mainObj=new JSONObject(JSON);

            cmainObj=new JSONObject();
            testid=mainObj.getString("Ptu_test_ID");
            cmainObj.put("sptu_ID",mainObj.getString("Ptu_test_ID"));

            scConfigList.clear();
            scConfigList=myhelper.getSubcatData(testid);

            Log.e("JSON--",mainObj.getString("Ptu_test_ID"));

            ja_sections=mainObj.getJSONArray("Sections");

            JSONArray cja_sections=new JSONArray();

            Log.e("sectionArray Length---",""+ja_sections.length());
            for(int i=0;i<ja_sections.length();i++){
                groupQuesIdList.clear();
                groupmainList.clear();
                groupConfigList.clear();
//                groupList.clear();
                groupcompList.clear();
                groupcloseList.clear();
                quesIdList.clear();
                testQuesList.clear();
                subcatList.clear();
                freeSubcatList.clear();
                secObj=ja_sections.getJSONObject(i);
                csecObj=new JSONObject();
                section=secObj.getString("Ptu_section_name");
                sectionid=secObj.getString("Ptu_section_ID");
                csecObj.put("Ptu_section_ID",secObj.getString("Ptu_section_ID"));
                csecObj.put("Ptu_section_name",secObj.getString("Ptu_section_name"));
                ja_questions=secObj.getJSONArray("Questions");
                Log.e("QuesArray Length---",""+ja_questions.length());


                for(int q=0;q<ja_questions.length();q++){

                    quesObj=ja_questions.getJSONObject(q);

                    String flag=quesObj.getString("qbm_group_flag");

                    if(flag.equalsIgnoreCase("No")){
                        quesIdList.add(quesObj.getString("qbm_ID"));
                        if(subcatList.contains(quesObj.getString("qbm_Sub_CategoryID"))){

                        }else{
                            subcatList.add(quesObj.getString("qbm_Sub_CategoryID"));
                        }

                    }else {
                        groupQuesIdList.add(quesObj.getString("qbm_ID"));

                        String grouptype=quesObj.getString("gbg_type");
                        if(grouptype.equalsIgnoreCase("COMPREHENSION")){
                            if(groupcompList.contains(quesObj.getString("gbg_id"))){

                            }else{
                                groupConfigList.add(new SingleGroupConfig(testid,sectionid,grouptype,1));
                                groupcompList.add(quesObj.getString("gbg_id"));
                            }
                        }else{
                            if(groupcloseList.contains(quesObj.getString("gbg_id"))){

                            }else{
                                groupConfigList.add(new SingleGroupConfig(testid,sectionid,grouptype,1));
                                groupcloseList.add(quesObj.getString("gbg_id"));
                            }
                        }


//                        if(groupList.contains(quesObj.getString("gbg_id"))){
//
//                        }else{
//                            groupList.add(quesObj.getString("gbg_id"));
//                        }
                        groupmainList.add(new GroupQues(quesObj.getString("gbg_id"),quesObj.getString("qbm_ID")));
                    }

                }


                finalgroupList.clear();
                if(groupcompList.size()>0){
                    int finalcompgroupcount=myhelper.getCompGroupCount(testid,sectionid,"Comprehention");
                    finalgroupList=getFinalGroups(groupcompList,finalcompgroupcount);
                    if(finalgroupList.size()>0){
                        for(int d=0;d<finalgroupList.size();d++){

                            gquesList.clear();
                            for(int q=0;q<ja_questions.length();q++){

                                quesObj=ja_questions.getJSONObject(q);

                                String flag=quesObj.getString("qbm_group_flag");

                                if(flag.equalsIgnoreCase("No")){

                                }else {
                                    if(finalgroupList.get(d).equalsIgnoreCase(quesObj.getString("gbg_id"))){
                                        gquesList.add(quesObj.getString("qbm_ID"));
                                    }else{

                                    }
                                }

                            }

                            Log.e("GroupId: --",""+finalgroupList.get(d));

                            int groupquescount=myhelper.getGroupQPickCount(finalgroupList.get(d));

//                            int groupquescount=1;

                            if(gquesList.size()>=groupquescount){
                                getGroupQues(gquesList,groupquescount);
                            }else{
                                Log.e("GroupConfig","Check"+"-Group"+finalgroupList.get(d));
                            }

                        }
                    }else{
                        Log.e("GroupConfig: --","Empty Comprehention Groups");
                    }
                }else{
                    Log.e("GroupConfig: --","Empty Comprehention Groups");
                }


                finalgroupList.clear();
                if(groupcloseList.size()>0){
                    int finalclosegroupcount=myhelper.getCloseGroupCount(testid,sectionid,"Closure");
                    finalgroupList=getFinalGroups(groupcloseList,finalclosegroupcount);
                    if(finalgroupList.size()>0){
                        for(int d=0;d<finalgroupList.size();d++){

                            gquesList.clear();
                            for(int q=0;q<ja_questions.length();q++){

                                quesObj=ja_questions.getJSONObject(q);

                                String flag=quesObj.getString("qbm_group_flag");

                                if(flag.equalsIgnoreCase("No")){

                                }else {
                                    if(finalgroupList.get(d).equalsIgnoreCase(quesObj.getString("gbg_id"))){
                                        gquesList.add(quesObj.getString("qbm_ID"));
                                    }else{

                                    }
                                }

                            }

                            Log.e("GroupId: --",""+finalgroupList.get(d));

                            int groupquescount=myhelper.getGroupQPickCount(finalgroupList.get(d));

//                            int groupquescount=6;

                            if(gquesList.size()>=groupquescount){
                                getGroupQues(gquesList,groupquescount);
                            }else{
                                Log.e("GroupConfig","Check"+"-Group"+finalgroupList.get(d));
                            }

                        }
                    }else{
                        Log.e("GroupConfig: --","Empty Closure Groups");
                    }
                }else{
                    Log.e("GroupConfig: --","Empty Closure Groups");
                }


                //SubCategory Wise Questions Scheduling.....

                Log.e("SubcatSize-----",""+subcatList.size());

                for(int d=0;d<subcatList.size();d++){

                    quesList.clear();

                    String subcatid=subcatList.get(d);

                    for(int q=0;q<ja_questions.length();q++){

                        quesObj=ja_questions.getJSONObject(q);

                        String flag=quesObj.getString("qbm_group_flag");

                        if(flag.equalsIgnoreCase("No")){

                            if(subcatid.equalsIgnoreCase(quesObj.getString("qbm_Sub_CategoryID"))){
                                quesList.add(quesObj.getString("qbm_ID"));
                            }else{

                            }

                        }else {

                        }

                    }

                    for(int p=0;p<scConfigList.size();p++){

                        SingleSubcatConfig singleSubcatConfig=scConfigList.get(p);
                        if(singleSubcatConfig.getSubcatid().equalsIgnoreCase(subcatid)){
                            count=singleSubcatConfig.getMandatepick();
                            maxcount=singleSubcatConfig.getMaxpick();
                        }

                    }

                    if(quesList.size()>=count){
                        getSubCatQues(quesList,count,maxcount);
                    }else{
                        Log.e("SubCatConfig","Check "+"- SubCategory- - "+subcatid);
                    }

                }

                int tempcount=quescount-testQuesList.size();

                Log.e("JSONPARSE",freeSubcatList.size()+" : "+tempcount);
                if(freeSubcatList.size()>=tempcount){
                    Log.e("JSONPARSE","Subcat Config is Ok");
                }else{
                    Log.e("JSONPARSE","Subcat Config is not proper");
                }

                JSONArray cja_questions=new JSONArray();

                if(testQuesList.size()<quescount){

                    if(freeSubcatList.size()>=quescount-testQuesList.size()){
                        getShuffledQues(freeSubcatList,quescount-testQuesList.size());
                    }else{
                        Log.e("FreeAvailQues","problem in picking required extra ques.");
                    }

                }else{

                }


                //Ques Object Creation

                for(int q=0;q<ja_questions.length();q++){

                    if(cja_questions.length()<quescount){
                        quesObj=ja_questions.getJSONObject(q);

                        String flag=quesObj.getString("qbm_group_flag");

                        if(flag.equalsIgnoreCase("No")){


                            if(testQuesList.contains(quesObj.get("qbm_ID"))){

                                int g=r.nextInt(testfimages.size());
                                seqno++;
                                cquesObj=new JSONObject();
                                cquesObj.put("qbm_ID",quesObj.get("qbm_ID"));
                                cquesObj.put("qbm_SequenceId",seqno);
                                cquesObj.put("qbm_ReferenceID",quesObj.get("qbm_ReferenceID"));
                                cquesObj.put("qbm_Description",quesObj.get("qbm_Description"));
                                cquesObj.put("qbm_SubjectID",quesObj.get("qbm_SubjectID"));
                                cquesObj.put("qbm_Paper_ID",quesObj.get("qbm_Paper_ID"));
                                cquesObj.put("qbm_ChapterID",quesObj.get("qbm_ChapterID"));
                                cquesObj.put("qbm_Sub_CategoryID",quesObj.get("qbm_Sub_CategoryID"));
                                cquesObj.put("qbm_level",quesObj.get("qbm_level"));
                                cquesObj.put("qbm_Type",quesObj.get("qbm_Type"));
                                cquesObj.put("qbm_marks",quesObj.get("qbm_marks"));
                                cquesObj.put("qbm_negative_applicable",quesObj.get("qbm_negative_applicable"));
                                cquesObj.put("qbm_negative_mrk",quesObj.get("qbm_negative_mrk"));
                                cquesObj.put("qbm_question_type",quesObj.get("qbm_question_type"));
                                cquesObj.put("qbm_text_applicable",quesObj.get("qbm_text_applicable"));
                                cquesObj.put("qbm_text",quesObj.get("qbm_text"));
                                cquesObj.put("qbm_image_file",quesObj.get("qbm_image_file"));
                                cquesObj.put("qbm_video_file",quesObj.get("qbm_video_file"));
                                cquesObj.put("qbm_media_type",quesObj.get("qbm_media_type"));
                                cquesObj.put("qbm_answer",quesObj.get("qbm_answer"));
                                cquesObj.put("qbm_group_flag",quesObj.get("qbm_group_flag"));
                                cquesObj.put("qbm_review_flag",quesObj.get("qbm_review_flag"));
                                cquesObj.put("qbm_Review_Type",quesObj.get("qbm_Review_Type"));
                                cquesObj.put("qbm_Review_Images",quesObj.get("qbm_Review_Images"));
                                cquesObj.put("qbm_review_Video",quesObj.get("qbm_review_Video"));
                                cquesObj.put("qbm_Additional_Images_num",quesObj.get("qbm_Additional_Images_num"));
                                cquesObj.put("qbm_Additional_Image_ref",quesObj.get("qbm_Additional_Image_ref"));
                                cquesObj.put("gbg_id",quesObj.get("gbg_id"));
                                cquesObj.put("gbg_type",quesObj.get("gbg_type"));
                                cquesObj.put("gbg_media_type",quesObj.get("gbg_media_type"));
                                cquesObj.put("gbg_media_file",quesObj.get("gbg_media_file"));
                                cquesObj.put("gbg_text",quesObj.get("gbg_text"));
                                cquesObj.put("qbm_jumbling_flag",quesObj.get("qbm_jumbling_flag"));
                                cquesObj.put("qbm_flash_image",testfimages.get(g));
                                cquesObj.put("gbg_no_questions",quesObj.get("gbg_no_questions"));

                                ja_options=ja_questions.getJSONObject(q).getJSONArray("Options");
                                JSONArray cja_options =new JSONArray();
                                ArrayList<SingleOptions> opList=new ArrayList<>();
                                for(int p=0;p<ja_options.length();p++){
                                    optionObj=ja_options.getJSONObject(p);
                                    try {

                                        opList.add(new SingleOptions(optionObj.getString("qbo_id"),optionObj.getString("qbo_seq_no"),optionObj.getString("qbo_type"),optionObj.getString("qbo_text"),optionObj.getString("qbo_media_type"),optionObj.getString("qbo_media_file"),optionObj.getString("qbo_answer_flag")));

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                if(opList.size()!=0){
                                    Collections.shuffle(opList);
                                    for(int b=0;b<opList.size();b++){
                                        SingleOptions singleOptions=opList.get(b);
                                        coptionObj = new JSONObject();
                                        coptionObj.put("qbo_id",singleOptions.getQbo_id());
                                        coptionObj.put("qbo_seq_no",singleOptions.getQbo_seq_no());
                                        coptionObj.put("qbo_type",singleOptions.getQbo_type());
                                        coptionObj.put("qbo_text",singleOptions.getQbo_text());
                                        coptionObj.put("qbo_media_type",singleOptions.getQbo_media_type());
                                        coptionObj.put("qbo_media_file",singleOptions.getQbo_media_file());
                                        coptionObj.put("qbo_answer_flag",singleOptions.getQbo_answer_flag());
                                        cja_options.put(coptionObj);
                                    }
                                }

                                cquesObj.put("Options",cja_options);
                                JSONArray cja_additions=new JSONArray();
                                ja_additions=quesObj.getJSONArray("additions");
                                for(int p=0;p<ja_additions.length();p++){
                                    additionsObj=ja_additions.getJSONObject(p);
                                    cadditionsObj=new JSONObject();
                                    cadditionsObj.put("qba_sequence",additionsObj.get("qba_sequence"));
                                    cadditionsObj.put("qba_type",additionsObj.get("qba_type"));
                                    cadditionsObj.put("qba_media_file",additionsObj.get("qba_media_file"));
                                    cja_additions.put(cadditionsObj);
                                }
                                cquesObj.put("additions",cja_additions);
                                cja_questions.put(cquesObj);

                            }else{

                            }

                        }else {

                            if(testQuesList.contains(quesObj.getString("qbm_ID"))){

                                int g=r.nextInt(testfimages.size());
                                seqno++;
                                cquesObj=new JSONObject();
                                cquesObj.put("qbm_ID",quesObj.get("qbm_ID"));
                                cquesObj.put("qbm_SequenceId",seqno);
                                cquesObj.put("qbm_ReferenceID",quesObj.get("qbm_ReferenceID"));
                                cquesObj.put("qbm_Description",quesObj.get("qbm_Description"));
                                cquesObj.put("qbm_SubjectID",quesObj.get("qbm_SubjectID"));
                                cquesObj.put("qbm_Paper_ID",quesObj.get("qbm_Paper_ID"));
                                cquesObj.put("qbm_ChapterID",quesObj.get("qbm_ChapterID"));
                                cquesObj.put("qbm_Sub_CategoryID",quesObj.get("qbm_Sub_CategoryID"));
                                cquesObj.put("qbm_level",quesObj.get("qbm_level"));
                                cquesObj.put("qbm_Type",quesObj.get("qbm_Type"));
                                cquesObj.put("qbm_marks",quesObj.get("qbm_marks"));
                                cquesObj.put("qbm_negative_applicable",quesObj.get("qbm_negative_applicable"));
                                cquesObj.put("qbm_negative_mrk",quesObj.get("qbm_negative_mrk"));
                                cquesObj.put("qbm_question_type",quesObj.get("qbm_question_type"));
                                cquesObj.put("qbm_text_applicable",quesObj.get("qbm_text_applicable"));
                                cquesObj.put("qbm_text",quesObj.get("qbm_text"));
                                cquesObj.put("qbm_image_file",quesObj.get("qbm_image_file"));
                                cquesObj.put("qbm_video_file",quesObj.get("qbm_video_file"));
                                cquesObj.put("qbm_media_type",quesObj.get("qbm_media_type"));
                                cquesObj.put("qbm_answer",quesObj.get("qbm_answer"));
                                cquesObj.put("qbm_group_flag",quesObj.get("qbm_group_flag"));
                                cquesObj.put("qbm_review_flag",quesObj.get("qbm_review_flag"));
                                cquesObj.put("qbm_Review_Type",quesObj.get("qbm_Review_Type"));
                                cquesObj.put("qbm_Review_Images",quesObj.get("qbm_Review_Images"));
                                cquesObj.put("qbm_review_Video",quesObj.get("qbm_review_Video"));
                                cquesObj.put("qbm_Additional_Images_num",quesObj.get("qbm_Additional_Images_num"));
                                cquesObj.put("qbm_Additional_Image_ref",quesObj.get("qbm_Additional_Image_ref"));
                                cquesObj.put("gbg_id",quesObj.get("gbg_id"));
                                cquesObj.put("gbg_type",quesObj.get("gbg_type"));
                                cquesObj.put("gbg_media_type",quesObj.get("gbg_media_type"));
                                cquesObj.put("gbg_media_file",quesObj.get("gbg_media_file"));
                                cquesObj.put("gbg_text",quesObj.get("gbg_text"));
                                cquesObj.put("qbm_jumbling_flag",quesObj.get("qbm_jumbling_flag"));
                                cquesObj.put("qbm_flash_image",testfimages.get(g));
                                cquesObj.put("gbg_no_questions",quesObj.get("gbg_no_questions"));

                                ja_options=ja_questions.getJSONObject(q).getJSONArray("Options");
                                JSONArray cja_options =new JSONArray();
                                ArrayList<SingleOptions> opList=new ArrayList<>();
                                for(int p=0;p<ja_options.length();p++){
                                    optionObj=ja_options.getJSONObject(p);
                                    try {

                                        opList.add(new SingleOptions(optionObj.getString("qbo_id"),optionObj.getString("qbo_seq_no"),optionObj.getString("qbo_type"),optionObj.getString("qbo_text"),optionObj.getString("qbo_media_type"),optionObj.getString("qbo_media_file"),optionObj.getString("qbo_answer_flag")));

                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Log.e("JSONParser----","OptionsParse:  "+e.toString());
                                    }
                                }

                                Log.e("QOptionSize----",""+opList.size());
                                if(opList.size()!=0){
                                    Collections.shuffle(opList);
                                    for(int b=0;b<opList.size();b++){
                                        SingleOptions singleOptions=opList.get(b);
                                        coptionObj = new JSONObject();
                                        coptionObj.put("qbo_id",singleOptions.getQbo_id());
                                        coptionObj.put("qbo_seq_no",singleOptions.getQbo_seq_no());
                                        coptionObj.put("qbo_type",singleOptions.getQbo_type());
                                        coptionObj.put("qbo_text",singleOptions.getQbo_text());
                                        coptionObj.put("qbo_media_type",singleOptions.getQbo_media_type());
                                        coptionObj.put("qbo_media_file",singleOptions.getQbo_media_file());
                                        coptionObj.put("qbo_answer_flag",singleOptions.getQbo_answer_flag());
                                        cja_options.put(coptionObj);
                                    }
                                }

                                cquesObj.put("Options",cja_options);
                                JSONArray cja_additions=new JSONArray();
                                ja_additions=quesObj.getJSONArray("additions");
                                for(int p=0;p<ja_additions.length();p++){
                                    additionsObj=ja_additions.getJSONObject(p);
                                    cadditionsObj=new JSONObject();
                                    cadditionsObj.put("qba_sequence",additionsObj.get("qba_sequence"));
                                    cadditionsObj.put("qba_type",additionsObj.get("qba_type"));
                                    cadditionsObj.put("qba_media_file",additionsObj.get("qba_media_file"));
                                    cja_additions.put(cadditionsObj);
                                }
                                cquesObj.put("additions",cja_additions);
                                cja_questions.put(cquesObj);

                            }else{

                            }

                        }
                    }else {

                    }
                }

                csecObj.put("Questions",cja_questions);

                testqcount=testqcount+cja_questions.length();
                Log.e("QSIZE",""+section+" : "+cja_questions.length());

                cja_sections.put(csecObj);

            }

            cmainObj.put("Sections",cja_sections);

            Log.e("SLength",""+cja_sections.length());

            File file = new File(dwdpath+"sample.json");
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] bytes = cmainObj.toString().getBytes("UTF-8");
            FileOutputStream out = new FileOutputStream(dwdpath+"sample.json");
            out.write(bytes);
            out.close();

            Log.e("TestQSize---",""+testqcount);

        }catch (Exception e){
            e.printStackTrace();
            Log.e("JSONPARSE---",e.toString()+" : "+e.getStackTrace()[0].getLineNumber());
        }
    }

    public ArrayList<String> getFinalGroups(ArrayList<String> grouplist,int count) {

        ArrayList<String> finalList=new ArrayList<>();

        Collections.shuffle(groupList);

        for(int i=0;i<count;i++){
            finalList.add(grouplist.get(i));
        }

        return finalList;
    }

    public void getGroupQues(ArrayList<String> grouplist,int count) {

        ArrayList<String> finalList=new ArrayList<>();

        while (finalList.size()<count){
            int p=r.nextInt(grouplist.size());
            if(testQuesList.contains(grouplist.get(p))){

            }else{
                testQuesList.add(grouplist.get(p));
                finalList.add(grouplist.get(p));
            }
        }

    }

    public void getSubCatQues(ArrayList<String> queslist,int count,int maxcount) {

        ArrayList<String> finalList=new ArrayList<>();
        ArrayList<String> freeList=new ArrayList<>();

        while (finalList.size()<count){
            int p=r.nextInt(queslist.size());
            if(testQuesList.contains(queslist.get(p))){

            }else{
                testQuesList.add(queslist.get(p));
                finalList.add(queslist.get(p));
            }
        }


        for(int i=0;i<queslist.size();i++){

            if(freeList.size()<maxcount-count){
                if(finalList.contains(queslist.get(i))){

                }else{
                    freeSubcatList.add(queslist.get(i));
                    freeList.add(queslist.get(i));
                }
            }else{

            }
        }

        Log.e("SubCatQuesSize",""+finalList.size());

    }

    public void getShuffledQues(ArrayList<String> quesList,int count) {

        ArrayList<String> finalList=new ArrayList<>();

        while (finalList.size()<count){
            int p=r.nextInt(quesList.size());
            if(testQuesList.contains(quesList.get(p))){

            }else{
                testQuesList.add(quesList.get(p));
                finalList.add(quesList.get(p));
            }
        }
    }

}
