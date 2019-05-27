package com.digywood.tms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.tms.AsynTasks.AsyncCheckInternet_WithOutProgressBar;
import com.digywood.tms.AsynTasks.BagroundTask;
import com.digywood.tms.AsynTasks.DownloadFileAsync;
import com.digywood.tms.DBHelper.DBHelper;
import com.digywood.tms.Pojo.LessonUnitPoint;
import com.digywood.tms.Pojo.SingleDWDQues;
import com.digywood.tms.Pojo.SingleOrganization;
import com.digywood.tms.Pojo.SingleTest;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_Lesson_Unit_Point_Display extends AppCompatActivity {


    String TAG = this.getClass().getName();
    AppEnvironment appEnvironment;
    UserMode userMode;
    String BASE_URL = "";
    String studentid = "", enrollid = "", courseid = "", paperid = "", subjectid = "", lessonid = "", lessonunitid = "", lessonunitpointid = "", orgid = "", studentname = "", number = "", email = "";
    DBHelper myhelper;
    ArrayList<LessonUnitPoint> al_lesson_unit_points = new ArrayList<>();
    int current_pos = 0;
    LessonUnitPoint cur_lessson_ubit_point;

    ImageView iv_record_start, iv_record_play, iv_prev, iv_play, iv_next, iv_logo_file;
    TextView tv_record_status, tv_recording_time, tv_record_play_status;
    Button btn_test_start;

    PlayerView playerView;
    SimpleExoPlayer player, inner_player;

    long playbackPosition = 0;
    boolean playWhenReady = false;
    int currentWindow = 0;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};


    private MediaRecorder recorder = null;
    private MediaPlayer media_player = null;

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    boolean mplayStatus = true;
    boolean recordingStatus = false;
    boolean recordingFlag = false;

    ArrayList<String> al_media_urls = new ArrayList<>();

    int hour, minute, seconds;
    Timer t;

    ArrayList<String> downloadedList = new ArrayList<>();
    ArrayList<String> downloadfileList = new ArrayList<>();
    ArrayList<String> groupIds = new ArrayList<>();
    ArrayList<SingleDWDQues> missFileData = new ArrayList<>();
    ArrayList<SingleDWDQues> chapterFileList = new ArrayList<>();
    ArrayList<String> finalUrls = new ArrayList<>();
    ArrayList<String> finalNames = new ArrayList<>();
    ArrayList<String> localPathList = new ArrayList();

    String groupdata = "", filedata = "";
    SharedPreferences restoredprefs;
    String restoredsname = "", serverId = "", finalUrl = "", finalAssetUrl = "", localpath = "";

    boolean practice_flag = false, conversation_Flag = false;
    boolean media_player_playing_flag = false;
    ProgressBar progressBar;

    DBHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_unit_point_display);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= 21) {

            final Drawable upArrow = getApplicationContext().getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

        appEnvironment = ((MyApplication) getApplication()).getAppEnvironment();//getting App Environment
        userMode = ((MyApplication) getApplication()).getUserMode();//getting User Mode

        dbHelper = new DBHelper(this);

        Intent cmgintent = getIntent();
        if (cmgintent != null) {
            studentid = cmgintent.getStringExtra("studentid");
            studentname = cmgintent.getStringExtra("sname");
            number = cmgintent.getStringExtra("number");
            email = cmgintent.getStringExtra("email");
            orgid = cmgintent.getStringExtra("orgid");
            enrollid = cmgintent.getStringExtra("enrollid");
            courseid = cmgintent.getStringExtra("courseid");
            paperid = cmgintent.getStringExtra("paperid");
            lessonid = cmgintent.getStringExtra("lessonid");
            lessonunitid = cmgintent.getStringExtra("lessonunitid");
            lessonunitpointid = cmgintent.getStringExtra("lessonunitpointid");
        }

        Log.e(TAG, "orgid:" + orgid);
        SingleOrganization sig_org = dbHelper.getOrganization(orgid);
        Log.e(TAG, "orgName:" + sig_org.getCtr_Name());
        Log.e(TAG, "logofile:" + sig_org.getCtr_logo_file());

        restoredprefs = getSharedPreferences("SERVERPREF", MODE_PRIVATE);
        restoredsname = restoredprefs.getString("servername", "main_server");

        if (restoredsname.equalsIgnoreCase("main_server")) {
            finalUrl = URLClass.hosturl;
            finalAssetUrl = URLClass.downloadjson;
        } else {
            serverId = myhelper.getServerId(restoredsname);
            finalUrl = "http://" + serverId + URLClass.loc_hosturl;
            finalAssetUrl = "http://" + serverId + URLClass.loc_downloadjson;
        }


        BASE_URL = getString(R.string.base_url);

        playerView = findViewById(R.id.video_view);
        iv_record_start = findViewById(R.id.iv_record_start);
        iv_record_play = findViewById(R.id.iv_record_play);
        tv_record_status = findViewById(R.id.tv_record_status);
        tv_recording_time = findViewById(R.id.tv_recording_time);
        tv_record_play_status = findViewById(R.id.tv_record_play_status);
        iv_prev = findViewById(R.id.iv_prev);
        iv_play = findViewById(R.id.iv_play);
        iv_next = findViewById(R.id.iv_next);
        btn_test_start = findViewById(R.id.btn_test_start);
        progressBar = findViewById(R.id.progress_bar);
        iv_logo_file = findViewById(R.id.iv_logo_file);


        byte[] blob_logo_file = Base64.decode(sig_org.getCtr_logo_file(), Base64.DEFAULT);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap img_bmp = BitmapFactory.decodeByteArray(blob_logo_file, 0, blob_logo_file.length, options);
        iv_logo_file.setImageBitmap(img_bmp);

        iv_record_play.setEnabled(false);

        myhelper = new DBHelper(this);
        al_lesson_unit_points = myhelper.getLessonUnitpoints(courseid, subjectid, paperid, lessonid, lessonunitid);
        Log.e(TAG, "al_lesson_unit_points.size():" + al_lesson_unit_points.size());


        for (int i = 0; i < al_lesson_unit_points.size(); i++) {
            LessonUnitPoint lessonUnitPoint = al_lesson_unit_points.get(i);
            if (lessonUnitPoint.getTms_lup_id().equalsIgnoreCase(lessonunitpointid)) {
                current_pos = i;
            }
            String next_uri = getUrlfromLessonUnitPoints(i);
            al_media_urls.add(next_uri);
        }

        cur_lessson_ubit_point = al_lesson_unit_points.get(current_pos);

        if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("TEST")) {
            // btn_test_start.setVisibility(View.VISIBLE);
            btn_test_start.setEnabled(true);
            btn_test_start.setBackgroundColor(Color.GREEN);
            btn_test_start.setText("START TEST");
        } else if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("FLASH")) {
            //btn_test_start.setVisibility(View.VISIBLE);
            btn_test_start.setEnabled(true);
            btn_test_start.setText("START TEST");
            btn_test_start.setBackgroundColor(Color.GREEN);
        } else {
            //btn_test_start.setVisibility(View.INVISIBLE);
            btn_test_start.setEnabled(false);
        }

        if (player != null) {
            player.release();
            player = null;
        }
        playVideo(current_pos);
        iv_play.setImageDrawable(getDrawable(R.drawable.ic_pause));

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        iv_record_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (practice_flag) {
                    if (media_player_playing_flag) {
                        onRecord(mStartRecording);
                        if (mStartRecording) {
                            iv_record_start.setImageDrawable(getDrawable(R.drawable.ic_record_stop));
                            tv_record_status.setText("Stop Recording");
                            iv_record_start.setVisibility(View.VISIBLE);
                            iv_record_play.setEnabled(false);
                        } else {
                            tv_record_status.setText("Start Recording");
                            iv_record_start.setImageDrawable(getDrawable(R.drawable.ic_record_start));
                            iv_record_play.setEnabled(true);
                        }
                        mStartRecording = !mStartRecording;
                    } else {
                        Toast.makeText(Activity_Lesson_Unit_Point_Display.this, "You need to complete the video to practice..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Activity_Lesson_Unit_Point_Display.this, "Not a practice point", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_record_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordingStatus) {
                    onPlay(mStartPlaying);
                    if (mStartPlaying) {
                        iv_record_play.setImageDrawable(getDrawable(R.drawable.ic_speaker_play_stop1));
                        tv_record_play_status.setText("Stop Playing");
                    } else {
                        tv_record_play_status.setText("Start Playing");
                        iv_record_play.setImageDrawable(getDrawable(R.drawable.ic_speaker_play));
                    }
                    mStartPlaying = !mStartPlaying;
                }
            }
        });

        /*media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
                tv_record_play_status.setText("Start Playing");
                iv_record_play.setImageDrawable(getDrawable(R.drawable.ic_speaker_play));
            }
        });*/

        iv_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_pos == 0) {
                    Toast.makeText(Activity_Lesson_Unit_Point_Display.this, "You don't have any previous videos...", Toast.LENGTH_SHORT).show();
                } else {
                    current_pos = current_pos - 1;
                    recordingStatus = false;
                    /*player.seekTo(current_pos);
                    player.setPlayWhenReady(true);*/

                    cur_lessson_ubit_point = al_lesson_unit_points.get(current_pos);

                    if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("TEST")) {
                        //btn_test_start.setVisibility(View.VISIBLE);
                        btn_test_start.setEnabled(true);
                        btn_test_start.setBackgroundColor(Color.GREEN);
                        btn_test_start.setText("START TEST");
                    } else if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("FLASH")) {
                        // btn_test_start.setVisibility(View.VISIBLE);
                        btn_test_start.setEnabled(true);
                        btn_test_start.setText("START TEST");
                        btn_test_start.setBackgroundColor(Color.GREEN);
                    } else {
                        //btn_test_start.setVisibility(View.INVISIBLE);
                        btn_test_start.setEnabled(false);
                    }

                    if (player != null) {
                        player.release();
                        player = null;
                    }
                    playVideo(current_pos);
                }
            }
        });
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*player.seekTo(current_pos);
                player.setPlayWhenReady(true);*/
                /*if (player != null) {
                    player.release();
                    player = null;
                }
                playVideo(current_pos);*/
                if (player != null) {
                    if (mplayStatus) {
                        iv_play.setImageDrawable(getDrawable(R.drawable.ic_play));
                        player.setPlayWhenReady(false);
                    } else {
                        iv_play.setImageDrawable(getDrawable(R.drawable.ic_pause));
                        player.setPlayWhenReady(true);
                    }
                    mplayStatus = !mplayStatus;
                } else {
                    playVideo(current_pos);
                }

            }
        });
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current_pos == al_lesson_unit_points.size() - 1) {
                    Toast.makeText(Activity_Lesson_Unit_Point_Display.this, "You don't have any next videos...", Toast.LENGTH_SHORT).show();
                } else {
                    current_pos = current_pos + 1;
                    recordingStatus = false;
                    /*player.seekTo(current_pos);
                    player.setPlayWhenReady(true);*/

                    cur_lessson_ubit_point = al_lesson_unit_points.get(current_pos);

                    if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("TEST")) {
                        //btn_test_start.setVisibility(View.VISIBLE);
                        btn_test_start.setEnabled(true);
                        btn_test_start.setBackgroundColor(Color.GREEN);
                        btn_test_start.setText("START TEST");
                    } else if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("FLASH")) {
                        //btn_test_start.setVisibility(View.VISIBLE);
                        btn_test_start.setEnabled(true);
                        btn_test_start.setText("START TEST");
                        btn_test_start.setBackgroundColor(Color.GREEN);
                    } else {
                        // btn_test_start.setVisibility(View.INVISIBLE);
                        btn_test_start.setEnabled(false);
                    }

                    if (player != null) {
                        player.release();
                        player = null;
                    }
                    playVideo(current_pos);
                }

            }
        });

        btn_test_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    missFileData.clear();

                    courseid = cur_lessson_ubit_point.getTms_lup_course_id();
                    subjectid = cur_lessson_ubit_point.getTms_lup_subject_id();
                    paperid = cur_lessson_ubit_point.getTms_lup_paper_id();

                    final String testid = cur_lessson_ubit_point.getTms_lup_test_id();
                    final Context mycontext = Activity_Lesson_Unit_Point_Display.this;

                    final String tPath = URLClass.mainpath + enrollid + "/" + courseid + "/" + subjectid + "/" + paperid + "/" + testid + "/ENC/";
                    Log.e("ALUP_Display", "tPath:" + tPath);

                    File file = new File(tPath + testid + ".json");
                    if (!file.exists()) {
                        showAlert("Test File " + testid + " is not found! \n Do You want to download test data ? ", testid, mycontext);
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader(getTheDecriptedJson(tPath + testid + ".json")));
//                        BufferedReader br = new BufferedReader(new FileReader(tPath + testid + ".json"));
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        filedata = sb.toString();
                        parseJson(filedata);
                        br.close();

                        int seccount = myhelper.getPtuSecCount(testid);
                        if (seccount > 0) {

                            if (downloadfileList.size() != 0) {

                                ArrayList<String> missingfList = new ArrayList<>();

                                for (int i = 0; i < chapterFileList.size(); i++) {

                                    SingleDWDQues sdq = chapterFileList.get(i);

                                    File myFile1 = new File(URLClass.mainpath + enrollid + "/" + courseid + "/" + sdq.getSubjectId() + "/" + sdq.getPaperId() + "/" + sdq.getChapterId() + "/ENC/" + sdq.getFileName());
                                    if (myFile1.exists()) {

                                    } else {
                                        missingfList.add(downloadfileList.get(i));
                                        missFileData.add(sdq);
                                    }
                                }


                                if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("TEST")) {

                                    if (missingfList.size() != 0) {
                                        StringBuilder sbm = new StringBuilder();
                                        sbm.append("The following files are missing...Do You want to Download?..\n");
                                        for (int i = 0; i < missingfList.size(); i++) {
                                            sbm.append(missingfList.get(i) + " , " + "\n");
                                        }
                                        showReportAlert(sbm.toString(), testid, mycontext);
                                    } else {
                                        myhelper.DestroyPracticeRecord("attempt_data", testid);

                                        int count = myhelper.getTestAttempCount(testid, studentid);
                                        Log.e("Attempt Count", "" + count);
                                        Cursor c = myhelper.getAttempt(myhelper.getLastTestAttempt(testid, studentid));

                                        if (c.getCount() > 0) {
                                            c.moveToLast();
                                            if (c.getInt(c.getColumnIndex("Attempt_Status")) != 2) {
                                                myhelper.DeleteAttempt(myhelper.getLastTestAttempt(testid, studentid));
                                            }
                                        }
                                        try {
                                            Log.e("dataexec", getExternalPath(mycontext, testid, "BASE"));
                                            String fullTest = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, testid, "BASE") + testid + ".json"), "UTF-8");
                                            JSONParser myparser = new JSONParser(fullTest, getExternalPath(mycontext, testid, "ATTEMPT"), "PRACTICE", mycontext);
                                            final String attempt = new String(SaveJSONdataToFile.bytesFromFile(getExternalPath(mycontext, testid, "ATTEMPT") + testid + ".json"), "UTF-8");
                                            Log.e("FULLTEST:---", "sample:--" + fullTest);
                                            Log.e("attempt:---", "sample:--" + studentid);
                                            if (!userMode.mode()) {
                                                Intent i = new Intent(mycontext, Activity_Lesson_Unit_Point_Display_PracticeTest.class);
                                                i.putExtra("studentid", studentid);
                                                i.putExtra("sname", studentname);
                                                i.putExtra("number", number);
                                                i.putExtra("email", email);
                                                i.putExtra("orgid", orgid);
                                                i.putExtra("json", attempt);
                                                i.putExtra("test", testid);
                                                i.putExtra("status", "NEW");
                                                i.putExtra("lessonid", lessonid);
                                                i.putExtra("lessonunitid", lessonunitid);
                                                i.putExtra("lessonunitpointid", lessonunitpointid);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                new AsyncCheckInternet_WithOutProgressBar(mycontext, new INetStatus() {
                                                    @Override
                                                    public void inetSatus(Boolean netStatus) {
                                                        if (netStatus) {
                                                            Intent i = new Intent(mycontext, Activity_Lesson_Unit_Point_Display_PracticeTest.class);
                                                            i.putExtra("studentid", studentid);
                                                            i.putExtra("sname", studentname);
                                                            i.putExtra("number", number);
                                                            i.putExtra("email", email);
                                                            i.putExtra("orgid", orgid);
                                                            i.putExtra("json", attempt);
                                                            i.putExtra("test", testid);
                                                            i.putExtra("status", "NEW");
                                                            i.putExtra("lessonid", lessonid);
                                                            i.putExtra("lessonunitid", lessonunitid);
                                                            i.putExtra("lessonunitpointid", lessonunitpointid);
                                                            startActivity(i);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(mycontext, "No internet,Please Check Your Connection", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).execute();
                                            }
                                        } catch (IOException | ClassNotFoundException | NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else if (cur_lessson_ubit_point.getTms_lup_test().equalsIgnoreCase("FLASH")) {

                                    if (missingfList.size() != 0) {
                                        StringBuilder sbm = new StringBuilder();
                                        sbm.append("The following files are missing...Do You want to Download?..\n");
                                        for (int i = 0; i < missingfList.size(); i++) {
                                            sbm.append(missingfList.get(i) + " , " + "\n");
                                        }
                                        showReportAlert(sbm.toString(), testid, mycontext);
                                    } else {

                                        JSONParser myparser = new JSONParser(filedata, tPath + "/flashAttempts/", "FLASH", mycontext);

                                        if (!userMode.mode()) {
                                            Intent i = new Intent(mycontext, Activity_Lesson_Unit_Point_Display_FlashCardActivity.class);
                                            i.putExtra("studentid", studentid);
                                            i.putExtra("sname", studentname);
                                            i.putExtra("number", number);
                                            i.putExtra("email", email);
                                            i.putExtra("orgid", orgid);
                                            i.putExtra("testId", testid);
                                            i.putExtra("testPath", tPath);
                                            i.putExtra("lessonid", lessonid);
                                            i.putExtra("lessonunitid", lessonunitid);
                                            i.putExtra("lessonunitpointid", lessonunitpointid);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            new AsyncCheckInternet_WithOutProgressBar(mycontext, new INetStatus() {
                                                @Override
                                                public void inetSatus(Boolean netStatus) {
                                                    if (netStatus) {
                                                        Intent i = new Intent(mycontext, Activity_Lesson_Unit_Point_Display_FlashCardActivity.class);
                                                        i.putExtra("studentid", studentid);
                                                        i.putExtra("sname", studentname);
                                                        i.putExtra("number", number);
                                                        i.putExtra("email", email);
                                                        i.putExtra("orgid", orgid);
                                                        i.putExtra("testId", testid);
                                                        i.putExtra("testPath", tPath);
                                                        i.putExtra("lessonid", lessonid);
                                                        i.putExtra("lessonunitid", lessonunitid);
                                                        i.putExtra("lessonunitpointid", lessonunitpointid);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(mycontext, "No internet,Please Check Your Connection", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).execute();
                                        }

                                    }

                                } else {

                                }
                            } else {

                            }
                        } else {
                            showAlert("Test Configuration is not Available for " + testid + " \n Please download test data to fetch configuration ");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TestActivity1-----", e.toString());
                }


            }
        });

    }

    private String getUrlfromLessonUnitPoints(int pos) {
        LessonUnitPoint lessonUnitPoint = al_lesson_unit_points.get(pos);
        String cid = lessonUnitPoint.getTms_lup_course_id();
        String sid = lessonUnitPoint.getTms_lup_subject_id();
        String pid = lessonUnitPoint.getTms_lup_paper_id();
        String lid = lessonUnitPoint.getTms_lup_lesson_id();
        String luid = lessonUnitPoint.getTms_lup_lu_id();
        String lupid = lessonUnitPoint.getTms_lup_id();
        String media = lessonUnitPoint.getTms_lup_exp_media_gen();

        //String URL = BASE_URL + cid + "/" + sid + "/" + pid + "/" + lid + "/" + luid + "/" + lupid + "/" + media;
        String URL = BASE_URL + cid + "/" + sid + "/" + pid + "/" + lid + "/" + luid + "/" + media;
        Log.e(TAG, "current_pos:" + current_pos);
        Log.e(TAG, "URL:" + URL);
        return URL;
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        //player.setPlayWhenReady(playWhenReady);
        //player.seekTo(currentWindow, playbackPosition);

        //Uri uri = Uri.parse(getString(R.string.media_url_mp4));
        //Uri uri = Uri.parse(URL);
        MediaSource mediaSource = buildMediaSource(al_media_urls);
        player.prepare(mediaSource, true, false);
        player.prepare(mediaSource);
        player.seekTo(current_pos, C.TIME_UNSET);
        player.setPlayWhenReady(true);

        playerView.setControllerShowTimeoutMs(0);
        /*long duration=player.getDuration();
        playerView.setControllerShowTimeoutMs((int)duration);*/

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                super.onTimelineChanged(timeline, manifest, reason);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                super.onTracksChanged(trackGroups, trackSelections);
                player.setPlayWhenReady(false);
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);

                Log.e(TAG, "playbackState:" + playbackState);

                /*if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_NEXT) {
                    //do something
                    player.setPlayWhenReady(true);
                }
                if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS) {
                    //do something else
                    player.setPlayWhenReady(true);
                }*/

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                super.onRepeatModeChanged(repeatMode);
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                super.onShuffleModeEnabledChanged(shuffleModeEnabled);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                super.onPositionDiscontinuity(reason);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                super.onPlaybackParametersChanged(playbackParameters);
            }

            @Override
            public void onSeekProcessed() {
                super.onSeekProcessed();
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                super.onTimelineChanged(timeline, manifest);
            }
        });

    }

    private void playVideo(int position) {

        LessonUnitPoint lup = al_lesson_unit_points.get(position);
        String lup_practice_yes_no = lup.getTms_lup_practice_yes_no();
        if (lup_practice_yes_no.equalsIgnoreCase("YES")) {
            practice_flag = true;
        } else if (lup_practice_yes_no.equalsIgnoreCase("YESC")) // YESC=Conversation_falg
        {
            practice_flag = true;
            conversation_Flag = true;
        } else {
            practice_flag = false;
        }


        progressBar.setVisibility(View.VISIBLE);

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        Uri uri = Uri.parse(al_media_urls.get(position));

        // This is the MediaSource representing the media to be played.
        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("TMS")).
                createMediaSource(uri);
        ;
        //player.prepare(mediaSource, true, false);
        player.prepare(mediaSource);
        //player.seekTo(current_pos, C.TIME_UNSET);
        player.setPlayWhenReady(true);
        iv_play.setImageDrawable(getDrawable(R.drawable.ic_pause));

        playerView.setControllerShowTimeoutMs(0);
        /*long duration=player.getDuration();
        playerView.setControllerShowTimeoutMs((int)duration);*/

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                super.onTimelineChanged(timeline, manifest, reason);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                super.onTracksChanged(trackGroups, trackSelections);
                //player.setPlayWhenReady(false);
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);

                Log.e(TAG, "playbackState:" + playbackState);
                media_player_playing_flag = false;
                if (playbackState == Player.STATE_ENDED) {
                    if (player != null) {
                        /*playbackPosition = player.getCurrentPosition();
                        currentWindow = player.getCurrentWindowIndex();
                        playWhenReady = player.getPlayWhenReady();*/
                        player.release();
                        player = null;
                    }
                    iv_play.setImageDrawable(getDrawable(R.drawable.ic_play));
                    media_player_playing_flag = true;
                }

                /*if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_NEXT) {
                    //do something
                    player.setPlayWhenReady(true);
                }
                if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS) {
                    //do something else
                    player.setPlayWhenReady(true);
                }*/

                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                super.onRepeatModeChanged(repeatMode);
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                super.onShuffleModeEnabledChanged(shuffleModeEnabled);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
                super.onPositionDiscontinuity(reason);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                super.onPlaybackParametersChanged(playbackParameters);
            }

            @Override
            public void onSeekProcessed() {
                super.onSeekProcessed();
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                super.onTimelineChanged(timeline, manifest);
            }
        });

    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }

        if (inner_player != null) {
            playbackPosition = inner_player.getCurrentPosition();
            currentWindow = inner_player.getCurrentWindowIndex();
            playWhenReady = inner_player.getPlayWhenReady();
            inner_player.release();
            inner_player = null;
        }
    }

    private MediaSource buildMediaSource(ArrayList<String> al_media_urls) {

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Activity_Lesson_Unit_Point_Display.this,
                Util.getUserAgent(Activity_Lesson_Unit_Point_Display.this, "TMS"));

        // This is the MediaSource representing the media to be played.
        /*MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);*/

        MediaSource[] mediaSources = new MediaSource[al_media_urls.size()];
        for (int m = 0; m < al_media_urls.size(); m++) {
            Uri uri = Uri.parse(al_media_urls.get(m));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
            mediaSources[m] = videoSource;
        }

        return new ConcatenatingMediaSource(mediaSources);

        /*return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("TMS")).
                createMediaSource(uri);*/
    }

    @Override
    public void onStart() {
        super.onStart();
        /*if (Util.SDK_INT > 23) {
            initializePlayer();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        /*if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }

        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (media_player != null) {
            media_player.release();
            media_player = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void diplayTheRecordTiming() {

        t = new Timer("hello", true);
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                tv_recording_time.post(new Runnable() {

                    public void run() {
                        seconds++;
                        if (seconds == 60) {
                            seconds = 0;
                            minute++;
                        }
                        if (minute == 60) {
                            minute = 0;
                            hour++;
                        }
                        /*tv_recording_time.setText(""
                                + (hour > 9 ? hour : ("0" + hour)) + " : "
                                + (minute > 9 ? minute : ("0" + minute))
                                + " : "
                                + (seconds > 9 ? seconds : "0" + seconds));*/
                        tv_recording_time.setText(""
                                + (minute > 9 ? minute : ("0" + minute))
                                + " m : "
                                + (seconds > 9 ? seconds : "0" + seconds)
                                + " s");

                    }
                });

            }
        }, 1000, 1000);

    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {

        if(conversation_Flag){
            media_player = new MediaPlayer();
            try {
                media_player.setDataSource(fileName);
                media_player.prepare();
                media_player.start();
            } catch (IOException e) {
                Log.e(TAG, "prepare() failed");
            }

            media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    media_player.stop();
                    tv_record_play_status.setText("Start Playing");
                    iv_record_play.setImageDrawable(getDrawable(R.drawable.ic_speaker_play));
                    mStartPlaying = !mStartPlaying;
                }
            });
        }else {

        /*int video_player_index=player.getCurrentPeriodIndex()-1;
        Log.e(TAG, "getCurrentPeriodIndex:" + video_player_index);*/
            LessonUnitPoint lup = al_lesson_unit_points.get(current_pos);
            String cid = lup.getTms_lup_course_id();
            String sid = lup.getTms_lup_subject_id();
            String pid = lup.getTms_lup_paper_id();
            String lid = lup.getTms_lup_lesson_id();
            String luid = lup.getTms_lup_lu_id();
            String lupid = lup.getTms_lup_id();
            String media = lup.getTms_lup_prct_media_gen();
            if (!media.equalsIgnoreCase("null")) {
                //String URL = BASE_URL + cid + "/" + sid + "/" + pid + "/" + lid + "/" + luid + "/" + lupid + "/" + media;
                String URL = BASE_URL + cid + "/" + sid + "/" + pid + "/" + lid + "/" + luid + "/" + media;
                Log.e(TAG, "mp3 url:" + URL);

                DefaultTrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                inner_player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector, loadControl);

                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(URL), dataSourceFactory, extractorsFactory, null, null);
                inner_player.prepare(mediaSource);
                inner_player.setPlayWhenReady(true);

                inner_player.addListener(new Player.EventListener() {
                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                    }

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {

                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playbackState == Player.STATE_ENDED) {
                            media_player = new MediaPlayer();
                            try {
                                media_player.setDataSource(fileName);
                                media_player.prepare();
                                media_player.start();
                            } catch (IOException e) {
                                Log.e(TAG, "prepare() failed");
                            }

                            media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    media_player.stop();
                                    tv_record_play_status.setText("Start Playing");
                                    iv_record_play.setImageDrawable(getDrawable(R.drawable.ic_speaker_play));
                                    mStartPlaying = !mStartPlaying;
                                }
                            });
                        }
                        if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_NEXT) {
                            //do something
                        }
                        if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS) {
                            //do something else
                        }
                    }

                    @Override
                    public void onRepeatModeChanged(int repeatMode) {

                    }

                    @Override
                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {

                    }

                    @Override
                    public void onPositionDiscontinuity(int reason) {

                    }

                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                    }

                    @Override
                    public void onSeekProcessed() {

                    }
                });

            /*try {
                MediaPlayer O_mediaPlayer = new MediaPlayer();
                O_mediaPlayer.setDataSource(URL);
                O_mediaPlayer.prepare();
                O_mediaPlayer.start();
                O_mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        media_player = new MediaPlayer();
                        try {
                            media_player.setDataSource(fileName);
                            media_player.prepare();
                            media_player.start();
                        } catch (IOException e) {
                            Log.e(TAG, "prepare() failed");
                        }

                        media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                media_player.stop();
                                tv_record_play_status.setText("Start Playing");
                                iv_record_play.setImageDrawable(getDrawable(R.drawable.ic_speaker_play));
                                mStartPlaying = !mStartPlaying;
                            }
                        });
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "prepare() failed");
            }*/
            } else {
                media_player = new MediaPlayer();
                try {
                    media_player.setDataSource(fileName);
                    media_player.prepare();
                    media_player.start();
                } catch (IOException e) {
                    Log.e(TAG, "prepare() failed");
                }

                media_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        media_player.stop();
                        tv_record_play_status.setText("Start Playing");
                        iv_record_play.setImageDrawable(getDrawable(R.drawable.ic_speaker_play));
                        mStartPlaying = !mStartPlaying;
                    }
                });
            }
        }

    }

    private void stopPlaying() {
        try {
            if (media_player != null) {
                media_player.release();
                media_player = null;
            }

            if (inner_player != null) {
                playbackPosition = inner_player.getCurrentPosition();
                currentWindow = inner_player.getCurrentWindowIndex();
                playWhenReady = inner_player.getPlayWhenReady();
                inner_player.release();
                inner_player = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRecording() {
        recordingStatus = true;
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        recorder.start();
        diplayTheRecordTiming();

        if (conversation_Flag) {

            progressBar.setVisibility(View.VISIBLE);

            LessonUnitPoint lup = al_lesson_unit_points.get(current_pos);
            String cid = lup.getTms_lup_course_id();
            String sid = lup.getTms_lup_subject_id();
            String pid = lup.getTms_lup_paper_id();
            String lid = lup.getTms_lup_lesson_id();
            String luid = lup.getTms_lup_lu_id();
            String lupid = lup.getTms_lup_id();
            String media = lup.getTms_lup_prct_media_gen();
            if (!media.equalsIgnoreCase("null")) {
                //String URL = BASE_URL + cid + "/" + sid + "/" + pid + "/" + lid + "/" + luid + "/" + lupid + "/" + media;
                String URL = BASE_URL + cid + "/" + sid + "/" + pid + "/" + lid + "/" + luid + "/" + media;

                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(this),
                        new DefaultTrackSelector(), new DefaultLoadControl());

                playerView.setPlayer(player);

                Uri uri = Uri.parse(URL);

                // This is the MediaSource representing the media to be played.
                MediaSource mediaSource = new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("TMS")).
                        createMediaSource(uri);
                ;
                //player.prepare(mediaSource, true, false);
                player.prepare(mediaSource);
                //player.seekTo(current_pos, C.TIME_UNSET);
                player.setPlayWhenReady(true);
                iv_play.setImageDrawable(getDrawable(R.drawable.ic_pause));

                playerView.setControllerShowTimeoutMs(0);
        /*long duration=player.getDuration();
        playerView.setControllerShowTimeoutMs((int)duration);*/

                player.addListener(new Player.DefaultEventListener() {
                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                        super.onTimelineChanged(timeline, manifest, reason);
                    }

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                        super.onTracksChanged(trackGroups, trackSelections);
                        //player.setPlayWhenReady(false);
                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {
                        super.onLoadingChanged(isLoading);
                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        super.onPlayerStateChanged(playWhenReady, playbackState);

                        Log.e(TAG, "playbackState:" + playbackState);
                        media_player_playing_flag = false;
                        if (playbackState == Player.STATE_ENDED) {
                            if (player != null) {
                        /*playbackPosition = player.getCurrentPosition();
                        currentWindow = player.getCurrentWindowIndex();
                        playWhenReady = player.getPlayWhenReady();*/
                                player.release();
                                player = null;
                            }
                            iv_play.setImageDrawable(getDrawable(R.drawable.ic_play));
                            media_player_playing_flag = true;
                        }

                /*if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_NEXT) {
                    //do something
                    player.setPlayWhenReady(true);
                }
                if (playbackState == PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS) {
                    //do something else
                    player.setPlayWhenReady(true);
                }*/

                        if (playbackState == ExoPlayer.STATE_BUFFERING) {
                            progressBar.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onRepeatModeChanged(int repeatMode) {
                        super.onRepeatModeChanged(repeatMode);
                    }

                    @Override
                    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                        super.onShuffleModeEnabledChanged(shuffleModeEnabled);
                    }

                    @Override
                    public void onPlayerError(ExoPlaybackException error) {
                        super.onPlayerError(error);
                    }

                    @Override
                    public void onPositionDiscontinuity(int reason) {
                        super.onPositionDiscontinuity(reason);
                    }

                    @Override
                    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                        super.onPlaybackParametersChanged(playbackParameters);
                    }

                    @Override
                    public void onSeekProcessed() {
                        super.onSeekProcessed();
                    }

                    @Override
                    public void onTimelineChanged(Timeline timeline, Object manifest) {
                        super.onTimelineChanged(timeline, manifest);
                    }
                });

            }
        }
    }

    private void stopRecording() {

        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        recorder = null;

        if (t != null) {
            t.cancel();
            t = null;
            hour = 0;
            minute = 0;
            seconds = 0;
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent intent = new Intent(Activity_Lesson_Unit_Point_Display.this, Activity_Lesson_Unit_Points.class);
            intent.putExtra("studentid",studentid);
            intent.putExtra("enrollid",enrollid);
            intent.putExtra("courseid",courseid);
            intent.putExtra("paperid",paperid);
            intent.putExtra("lessonid",lessonid);
            intent.putExtra("lessonunitid",lessonunitid);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/

    /*@Override
    public void onBackPressed() {
        exitByBackKey();
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        exitByBackKey();
        return super.onSupportNavigateUp();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        Intent intent = new Intent(Activity_Lesson_Unit_Point_Display.this, Activity_Lesson_Unit_Points.class);
        intent.putExtra("studentid", studentid);
        intent.putExtra("sname", studentname);
        intent.putExtra("number", number);
        intent.putExtra("email", email);
        intent.putExtra("orgid", orgid);
        intent.putExtra("enrollid", enrollid);
        intent.putExtra("courseid", courseid);
        intent.putExtra("paperid", paperid);
        intent.putExtra("lessonid", lessonid);
        intent.putExtra("lessonunitid", lessonunitid);
        startActivity(intent);
        this.finish();
    }

    private void parseJson(String json) {

        downloadfileList.clear();
        chapterFileList.clear();
        localPathList.clear();

        JSONArray secArray, quesArray, optionsArray, additionsArray;
        JSONObject mainObj, secObj, singlequesObj, optionsObj, additionsObj;
        try {
            mainObj = new JSONObject(json);
            Log.e("JSON--", mainObj.getString("ptu_test_ID"));

            secArray = mainObj.optJSONArray("Sections");

            for (int d = 0; d < secArray.length(); d++) {

                secObj = secArray.getJSONObject(d);
                quesArray = secObj.optJSONArray("Questions");

                for (int i = 0; i < quesArray.length(); i++) {

                    singlequesObj = quesArray.getJSONObject(i);

                    String chapterid = singlequesObj.getString("qbm_ChapterID");
                    String paperid = singlequesObj.getString("qbm_Paper_ID");
                    String subid = singlequesObj.getString("qbm_SubjectID");

                    if (singlequesObj.getString("qbm_group_flag").equalsIgnoreCase("YES")) {

                        if (groupIds.contains(singlequesObj.getString("gbg_id"))) {

                        } else {
                            groupIds.add(singlequesObj.getString("gbg_id"));
                        }

                        if (downloadfileList.contains(singlequesObj.getString("gbg_media_file"))) {

                        } else {
                            downloadfileList.add(singlequesObj.getString("gbg_media_file"));
                            chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, singlequesObj.getString("gbg_media_file")));
                        }

                    } else {

                    }

                    if (downloadfileList.contains(singlequesObj.getString("qbm_image_file"))) {

                    } else {
                        downloadfileList.add(singlequesObj.getString("qbm_image_file"));
                        chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, singlequesObj.getString("qbm_image_file")));
                    }


                    if (!singlequesObj.getString("qbm_QAdditional_Image").equalsIgnoreCase("NULL")) {
                        if (downloadfileList.contains(singlequesObj.getString("qbm_QAdditional_Image"))) {

                        } else {
                            downloadfileList.add(singlequesObj.getString("qbm_QAdditional_Image"));
                            chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, singlequesObj.getString("qbm_QAdditional_Image")));
                        }
                    }

                    if (downloadfileList.contains(singlequesObj.getString("qbm_Review_Images"))) {

                    } else {
                        downloadfileList.add(singlequesObj.getString("qbm_Review_Images"));
                        chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, singlequesObj.getString("qbm_Review_Images")));
                    }

                    if (downloadfileList.contains(singlequesObj.getString("qbm_flash_image"))) {

                    } else {
                        downloadfileList.add(singlequesObj.getString("qbm_flash_image"));
                        chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, singlequesObj.getString("qbm_flash_image")));
                    }

                    optionsArray = singlequesObj.getJSONArray("Options");
                    for (int j = 0; j < optionsArray.length(); j++) {

                        optionsObj = optionsArray.getJSONObject(j);
                        if (downloadfileList.contains(optionsObj.getString("qbo_media_file"))) {

                        } else {
                            downloadfileList.add(optionsObj.getString("qbo_media_file"));
                            chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, optionsObj.getString("qbo_media_file")));
                        }
                    }

                    additionsArray = singlequesObj.getJSONArray("Review");
                    for (int k = 0; k < additionsArray.length(); k++) {

                        additionsObj = additionsArray.getJSONObject(k);
                        if (downloadfileList.contains(additionsObj.getString("qba_media_file"))) {

                        } else {
                            downloadfileList.add(additionsObj.getString("qba_media_file"));
                            chapterFileList.add(new SingleDWDQues(chapterid, paperid, subid, additionsObj.getString("qba_media_file")));
                        }

                    }

                }

            }

            if (groupIds.size() != 0) {
                for (int i = 0; i < groupIds.size(); i++) {
                    if (i == 0) {
                        groupdata = "'" + groupIds.get(i) + "'";
                    } else {
                        groupdata = groupdata + ",'" + groupIds.get(i) + "'";
                    }
                }
            } else {
                Log.e("ListofPractiseTests----", "No Groups Available in Test");
            }

            Log.e("JSONPARSE---", "" + groupdata);

            Log.e("JSONPARSE---", "" + downloadfileList.size());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("JSONPARSE---", e.toString());
        }
    }

    private String getExternalPath(Context context, String testid, String type) {
        DBHelper dataObj = new DBHelper(context);
        Cursor cursor = dataObj.getSingleStudentTests(studentid, testid);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                enrollid = cursor.getString(cursor.getColumnIndex("sptu_entroll_id"));
                courseid = cursor.getString(cursor.getColumnIndex("sptu_course_id"));
                subjectid = cursor.getString(cursor.getColumnIndex("sptu_subjet_ID"));
                paperid = cursor.getString(cursor.getColumnIndex("sptu_paper_ID"));
            }
        }

        Log.e("path_vars", enrollid + " " + courseid + " " + subjectid + " " + paperid + " " + testid);
        String path = enrollid + "/" + courseid + "/" + subjectid + "/" + paperid + "/" + testid + "/ENC/";
        String photoPath = URLClass.mainpath + path;
        String attemptPath = URLClass.mainpath + path + "Attempt/";
        String jsonPath = URLClass.mainpath + path;
        if (type.equals("BASE")) {
            return jsonPath;
        } else
            return attemptPath;
    }

    public void downloadTestFile(final String testid, final Context mycontext) {
        finalUrls.clear();
        finalNames.clear();
        filedata = "";

        HashMap<String, String> hmap = new HashMap<>();
        hmap.put("studentid", studentid);
        hmap.put("enrollid", enrollid);
        hmap.put("testid", testid);
        hmap.put("status", "STARTED");
        final String startdttm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
        hmap.put("date", startdttm);
        new BagroundTask(finalUrl + "updatePTestStartStatus.php", hmap, mycontext, new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try {
                    Log.e("PracticeTestAdapter", "Respnse from server : " + json);
                    if (json.equalsIgnoreCase("Updated")) {

                        long updateFlag = myhelper.updatePTestStartStatus(studentid, enrollid, testid, "STARTED", startdttm);
                        if (updateFlag > 0) {
                            Log.e("PracticeTestAdapter", "Updated Locally");
                        } else {
                            Log.e("PracticeTestAdapter", "Unable to Update Locally");
                        }

                        Cursor mycursor = myhelper.checkPractiseTest(studentid, testid);
                        if (mycursor.getCount() > 0) {
                            while (mycursor.moveToNext()) {

                                courseid = mycursor.getString(mycursor.getColumnIndex("sptu_course_id"));
                                subjectid = mycursor.getString(mycursor.getColumnIndex("sptu_subjet_ID"));
                                paperid = mycursor.getString(mycursor.getColumnIndex("sptu_paper_ID"));

                            }
                        } else {
                            mycursor.close();
                            Log.e("PracticeTestAdapter", "Test Is Not Available in Table..");
                        }

                        String path = courseid + "/" + subjectid + "/" + paperid + "/" + testid + "/ENC/";

                        String downloadjsonpath = finalAssetUrl + "courses/" + path + testid + ".json";

                        //tfiledwdpath=finalAssetUrl+"courses/"+path;

                        localpath = enrollid + "/" + courseid + "/" + subjectid + "/" + paperid + "/" + testid + "/ENC/";

                        File myFile1 = new File(URLClass.mainpath + localpath + testid + ".json");
                        if (myFile1.exists()) {
                            Log.e("PracticeTestAdapter", "Json File is Not Available, Please download it..");
                        } else {
                            finalUrls.add(downloadjsonpath);
                            localPathList.add(URLClass.mainpath + localpath);
                            finalNames.add(testid + ".json");
                        }

                        if (finalUrls.size() != 0) {
                            new DownloadFileAsync(mycontext, localPathList, finalUrls, finalNames, new IDownloadStatus() {
                                @Override
                                public void downloadStatus(String status) {

                                    try {
                                        if (status.equalsIgnoreCase("Completed")) {
                                            dwdImgFiles(testid, mycontext);
                                        } else {
                                            Toast.makeText(mycontext, "Unable to download Base json file", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {

                                        e.printStackTrace();
                                        Log.e("PracticeTestAdapter", e.toString());
                                    }
                                }
                            }).execute();
                        } else {
                            dwdImgFiles(testid, mycontext);
                        }
                    } else {
                        Toast.makeText(mycontext, "Test Status is not updated...", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("PracticeTestAdapter", e.toString());
                }

            }
        }).execute();
    }

    public void dwdImgFiles(final String testId, final Context mycontext) {
        finalUrls.clear();
        finalNames.clear();
        localPathList.clear();

        filedata = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getTheDecriptedJson(URLClass.mainpath + localpath + testId + ".json")));
//            BufferedReader br = new BufferedReader(new FileReader(URLClass.mainpath+localpath+testId+".json"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            filedata = sb.toString();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TestActivity1-----", e.toString());
        }

        parseJson(filedata);

        getTestConfig(testId, groupdata, mycontext);

        if (downloadfileList.size() != 0) {

            for (int i = 0; i < chapterFileList.size(); i++) {

                SingleDWDQues sdq = chapterFileList.get(i);

                File myFile1 = new File(URLClass.mainpath + enrollid + "/" + courseid + "/" + sdq.getSubjectId() + "/" + sdq.getPaperId() + "/" + sdq.getChapterId() + "/ENC/" + sdq.getFileName());
                if (myFile1.exists()) {

                } else {

                    String tPath = finalAssetUrl + "courses/" + courseid + "/" + sdq.getSubjectId() + "/" + sdq.getPaperId() + "/";
                    finalUrls.add(tPath + sdq.getChapterId() + "/ENC/" + sdq.getFileName());
                    finalNames.add(sdq.getFileName());
                    localPathList.add(URLClass.mainpath + enrollid + "/" + courseid + "/" + sdq.getSubjectId() + "/" + sdq.getPaperId() + "/" + sdq.getChapterId() + "/ENC/");
                }
            }

        } else {
            updatePTestEndStatus(testId, "DOWNLOADED", mycontext);
            Log.e("LearningActivity----", "No Downloaded Images for test");
        }

        if (finalNames.size() != 0) {

            new DownloadFileAsync(mycontext, localPathList, finalUrls, finalNames, new IDownloadStatus() {
                @Override
                public void downloadStatus(String status) {

                    try {
                        if (status.equalsIgnoreCase("Completed")) {

                            updatePTestEndStatus(testId, "DOWNLOADED", mycontext);

                        } else {

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        Log.e("DownloadFile----", e.toString());
                    }
                }
            }).execute();

        } else {

            updatePTestEndStatus(testId, "DOWNLOADED", mycontext);
            Toast.makeText(mycontext, "All Downloaded", Toast.LENGTH_SHORT).show();

        }
    }

    public void updatePTestEndStatus(final String testId, final String status, final Context mycontext) {
        HashMap<String, String> hmap = new HashMap<>();
        hmap.put("studentid", studentid);
        hmap.put("enrollid", enrollid);
        hmap.put("testid", testId);
        hmap.put("status", status);
        final String endddtm = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance(TimeZone.getDefault()).getTime());
        hmap.put("date", endddtm);
        new BagroundTask(finalUrl + "updatePTestEndStatus.php", hmap, mycontext, new IBagroundListener() {
            @Override
            public void bagroundData(String json) {
                try {

                    Log.e("UploadStatus---", json);
                    if (json.equalsIgnoreCase("Updated")) {
                        long updateFlag = myhelper.updatePTestEndStatus(studentid, enrollid, testId, status, endddtm);
                        if (updateFlag > 0) {
                            Log.e("LocalStatusUpdate---", "Updated Locally");
                        } else {
                            Log.e("LocalStatusUpdate---", "Unable to Update Locally");
                        }

                        Toast.makeText(mycontext, "All Downloaded", Toast.LENGTH_SHORT).show();
                    } else {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ListofPractiseTests----", e.toString());
                }
            }
        }).execute();
    }


    private void showAlert(String messege, final String testid, final Context mycontext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mycontext, R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + messege + "</font>"))
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        downloadTestFile(testid, mycontext);

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    private void showAlert(String messege) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Lesson_Unit_Point_Display.this, R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + messege + "</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    private void getTestConfig(final String testid, String groupidData, Context mycontext) {
        Log.e("PractiseTestAdapter", "****** Welcome to test config ******");
        HashMap<String, String> hmap = new HashMap<>();
        hmap.put("testId", testid);
        hmap.put("groupiddata", groupidData);
        Log.e("PractiseTestAdapter", "groupiddata:" + groupidData);
        new BagroundTask(finalUrl + "getTestConfig.php", hmap, mycontext, new IBagroundListener() {
            @Override
            public void bagroundData(String json) {

                Log.e("PractiseTestAdapter", "Resopnse from server:" + json);
                JSONArray groupArray, quesConfigArray, groupConfigArray, sectionArray;
                JSONObject groupObj = null, qconObj = null, gquesconObj = null, sectionObj = null;

                try {
                    Log.e("PractiseTestAdapter", json);

                    JSONObject myObj = new JSONObject(json);

                    Object obj1 = myObj.get("qbgroup");

                    if (obj1 instanceof JSONArray) {
//                        long Qgroupdelcount=myhelper.deleteTestGroups(testid);
//                        Log.e("groupdelcount---",""+Qgroupdelcount);
                        groupArray = myObj.getJSONArray("qbgroup");
                        if (groupArray != null && groupArray.length() > 0) {
                            Log.e("groupLength---", "" + groupArray.length());
                            int p = 0, q = 0, r = 0, s = 0;
                            for (int i = 0; i < groupArray.length(); i++) {

                                groupObj = groupArray.getJSONObject(i);

                                Cursor mycursor = myhelper.checkQuesGroup(groupObj.getString("qbg_ID"));

                                if (mycursor.getCount() > 0) {
                                    long updateFlag = myhelper.updateQuesGroup(groupObj.getString("qbg_ID"), testid, groupObj.getString("qbg_media_type"), groupObj.getString("qbg_media_file"),
                                            groupObj.getString("qbg_text"), groupObj.getInt("qbg_no_questions"), Integer.parseInt(groupObj.getString("qbg_no_pick")), groupObj.getString("qbg_status"),
                                            groupObj.getString("qbg_created_by"), groupObj.getString("qbg_created_dttm"), groupObj.getString("qbg_mod_by"), groupObj.getString("qbg_mod_dttm"), groupObj.getString("qbg_type"));
                                    //Log.e("PractiseTestAdapter","No Of Pick"+groupObj.getString("qbg_no_pick"));
                                    if (updateFlag > 0) {
                                        r++;
                                    } else {
                                        s++;
                                    }
                                } else {
                                    long insertFlag = myhelper.insertQuesGroup(groupObj.getInt("qbg_key"), groupObj.getString("qbg_ID"), testid, groupObj.getString("qbg_media_type"), groupObj.getString("qbg_media_file"),
                                            groupObj.getString("qbg_text"), groupObj.getInt("qbg_no_questions"), Integer.parseInt(groupObj.getString("qbg_no_pick")), groupObj.getString("qbg_status"),
                                            groupObj.getString("qbg_created_by"), groupObj.getString("qbg_created_dttm"), groupObj.getString("qbg_mod_by"), groupObj.getString("qbg_mod_dttm"), groupObj.getString("qbg_type"));
                                    //Log.e("PractiseTestAdapter","No Of Pick"+groupObj.getString("qbg_no_pick"));
                                    if (insertFlag > 0) {
                                        p++;
                                    } else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("BackGroundTask--", "Inserted: " + p + "  Updated:  " + r);
                        } else {
                            Log.e("QGroups--", "Empty Json Array: ");
                        }
                    } else {
                        Log.e("QGroups--", "No Question Groups: ");
                    }

                    Object obj2 = myObj.get("ques_config");

                    if (obj2 instanceof JSONArray) {
//                        long Qconfigcount=myhelper.deleteQuesConfig(testid);
//                        Log.e("QuesConDelCount---",""+Qconfigcount);
                        quesConfigArray = myObj.getJSONArray("ques_config");
                        if (quesConfigArray != null && quesConfigArray.length() > 0) {
                            Log.e("QuesConLength---", "" + quesConfigArray.length());
                            int p = 0, q = 0, r = 0, s = 0;
                            for (int i = 0; i < quesConfigArray.length(); i++) {

                                qconObj = quesConfigArray.getJSONObject(i);

                                Cursor mycursor = myhelper.checkQuesConfig(qconObj.getString("testId"), qconObj.getString("subcategoryId"));

                                if (mycursor.getCount() > 0) {

                                    long updateFlag = myhelper.updateQuesConfig(qconObj.getString("courseId"), qconObj.getString("subjectId"), qconObj.getString("paperId"),
                                            qconObj.getString("testId"), qconObj.getString("categoryId"), qconObj.getString("subcategoryId"), qconObj.getInt("avail_count"), qconObj.getInt("pickup_count"), qconObj.getInt("min_pickup_count"), qconObj.getString("ques_configstatus"));
                                    if (updateFlag > 0) {
                                        r++;
                                    } else {
                                        s++;
                                    }

                                } else {

                                    long insertFlag = myhelper.insertQuesConfig(qconObj.getInt("ques_configkey"), qconObj.getString("courseId"), qconObj.getString("subjectId"), qconObj.getString("paperId"),
                                            qconObj.getString("testId"), qconObj.getString("categoryId"), qconObj.getString("subcategoryId"), qconObj.getInt("avail_count"), qconObj.getInt("pickup_count"), qconObj.getInt("min_pickup_count"), qconObj.getString("ques_configstatus"));
                                    if (insertFlag > 0) {
                                        p++;
                                    } else {
                                        q++;
                                    }

                                }
                            }
                            Log.e("BackGroundTask--", "Inserted: " + p + "  Updated:  " + r);
                        } else {
                            Log.e("QuesConfig--", "Empty Json Array: ");
                        }
                    } else {
                        Log.e("QuesConfig--", "No QuesConfig: ");
                    }

                    Object obj3 = myObj.get("groupques_config");

                    if (obj3 instanceof JSONArray) {
//                        long Gconfigdelcount=myhelper.deleteGroupsConfig(testid);
//                        Log.e("groupcondelcount---",""+Gconfigdelcount);
                        groupConfigArray = myObj.getJSONArray("groupques_config");
                        if (groupConfigArray != null && groupConfigArray.length() > 0) {
                            Log.e("groupconLength---", "" + groupConfigArray.length());
                            int p = 0, q = 0, r = 0, s = 0;
                            for (int i = 0; i < groupConfigArray.length(); i++) {

                                gquesconObj = groupConfigArray.getJSONObject(i);

                                Cursor mycursor = myhelper.checkGroupConfig(gquesconObj.getString("testId"), gquesconObj.getString("sectionId"), gquesconObj.getString("groupType"));

                                if (mycursor.getCount() > 0) {
                                    long updateFlag = myhelper.updateGroupConfig(gquesconObj.getString("courseId"), gquesconObj.getString("subjectId"), gquesconObj.getString("paperId"),
                                            gquesconObj.getString("testId"), gquesconObj.getString("sectionId"), gquesconObj.getString("groupType"), gquesconObj.getInt("groupavail_count"),
                                            gquesconObj.getInt("grouppickup_count"), gquesconObj.getString("groupques_configstatus"));
                                    if (updateFlag > 0) {
                                        r++;
                                    } else {
                                        s++;
                                    }
                                } else {
                                    long insertFlag = myhelper.insertGroupConfig(gquesconObj.getInt("groupques_configKey"), gquesconObj.getString("courseId"), gquesconObj.getString("subjectId"), gquesconObj.getString("paperId"),
                                            gquesconObj.getString("testId"), gquesconObj.getString("sectionId"), gquesconObj.getString("groupType"), gquesconObj.getInt("groupavail_count"),
                                            gquesconObj.getInt("grouppickup_count"), gquesconObj.getString("groupques_configstatus"));
                                    if (insertFlag > 0) {
                                        p++;
                                    } else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("BackGroundTask--", "Inserted: " + p + "  Updated:  " + r);
                        } else {
                            Log.e("GroupConfig--", "Empty Json Array: ");
                        }
                    } else {
                        Log.e("GroupConfig--", "No GroupConfig: ");
                    }

                    Object obj4 = myObj.get("sections");

                    if (obj4 instanceof JSONArray) {
//                        long sectiondelcount=myhelper.deletePtuSections(testid);
//                        Log.e("sectiondelcount---",""+sectiondelcount);
                        sectionArray = myObj.getJSONArray("sections");
                        if (sectionArray != null && sectionArray.length() > 0) {
                            Log.e("sectionLength---", "" + sectionArray.length());
                            int p = 0, q = 0, r = 0, s = 0;
                            for (int i = 0; i < sectionArray.length(); i++) {

                                sectionObj = sectionArray.getJSONObject(i);

                                Cursor mycursor = myhelper.checkPtuSection(sectionObj.getString("Ptu_ID"), sectionObj.getString("Ptu_section_ID"));

                                if (mycursor.getCount() > 0) {

                                    long updateFlag = myhelper.updatePtuSection(sectionObj.getString("Ptu_ID"), sectionObj.getInt("Ptu_section_sequence"), sectionObj.getString("Ptu_section_ID"),
                                            sectionObj.getString("Ptu_section_paper_ID"), sectionObj.getString("Ptu_section_subject_ID"), sectionObj.getString("Ptu_section_course_ID"), sectionObj.getInt("Ptu_section_min_questions"),
                                            sectionObj.getInt("Ptu_section_max_questions"), sectionObj.getInt("Ptu_sec_tot_groups"), sectionObj.getInt("Ptu_sec_no_groups"), sectionObj.getString("Ptu_section_status"), sectionObj.getString("Ptu_section_name"));
                                    if (updateFlag > 0) {
                                        r++;
                                    } else {
                                        s++;
                                    }
                                } else {

                                    long insertFlag = myhelper.insertPtuSection(sectionObj.getInt("Ptu_section_key"), sectionObj.getString("Ptu_ID"), sectionObj.getInt("Ptu_section_sequence"), sectionObj.getString("Ptu_section_ID"),
                                            sectionObj.getString("Ptu_section_paper_ID"), sectionObj.getString("Ptu_section_subject_ID"), sectionObj.getString("Ptu_section_course_ID"), sectionObj.getInt("Ptu_section_min_questions"),
                                            sectionObj.getInt("Ptu_section_max_questions"), sectionObj.getInt("Ptu_sec_tot_groups"), sectionObj.getInt("Ptu_sec_no_groups"), sectionObj.getString("Ptu_section_status"), sectionObj.getString("Ptu_section_name"));
                                    if (insertFlag > 0) {
                                        p++;
                                    } else {
                                        q++;
                                    }
                                }
                            }
                            Log.e("BackGroundTask--", "Inserted: " + p + "  Updated:  " + r);
                        } else {
                            Log.e("Sections--", "Empty Json Array: ");
                        }
                    } else {
                        Log.e("Sections--", "No Sections: ");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ListofPractiseTests---", e.toString());
                }

            }
        }).execute();
    }

    private void showReportAlert(String messege, final String testId, final Context myContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext, R.style.ALERT_THEME);
        builder.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + messege + "</font>"))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        dwdImgFiles(testId, myContext);
                    }
                })
                .setNegativeButton("Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        JSONObject finalObj = JSONEncode(testId, missFileData);
                        HashMap<String, String> hmap = new HashMap<>();
                        hmap.put("MissingFiles", finalObj.toString());
                        try {
                            new BagroundTask(finalUrl + "insertmissingFileInfo.php", hmap, Activity_Lesson_Unit_Point_Display.this, new IBagroundListener() {
                                @Override
                                public void bagroundData(String json) {
                                    Log.d("ja", "comes:" + json);
                                    if (json.equals("Inserted")) {
                                        Toast.makeText(Activity_Lesson_Unit_Point_Display.this, "Report Submitted", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Activity_Lesson_Unit_Point_Display.this, "failed report,try later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.warning);
        alert.show();
    }

    private JSONObject JSONEncode(String testId, ArrayList<SingleDWDQues> finalList) {
        JSONObject job = new JSONObject();
        JSONArray MissingList = new JSONArray();
        try {
            JSONObject MissingFile;
            for (int i = 0; i < finalList.size(); i++) {

                SingleDWDQues singleDWDQues = finalList.get(i);

                MissingFile = new JSONObject();
                MissingFile.put("testId", testId);
                MissingFile.put("subjectId", singleDWDQues.getSubjectId());
                MissingFile.put("paperId", singleDWDQues.getPaperId());
                MissingFile.put("chapterId", singleDWDQues.getChapterId());
                MissingFile.put("fileName", singleDWDQues.getFileName());

                MissingList.put(MissingFile);
            }
            job.put("MissingFiles", MissingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return job;
    }

    private InputStream getTheDecriptedJson(String json_file_path) {
        InputStream is = null;
        try {
            File f = new File(json_file_path);
            if (f.exists()) {
                is = EncryptDecrypt.decryptJson(new FileInputStream(f));
            } else {
                Log.e("PracticeTestAdapter", "file is not found:" + json_file_path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return is;
    }
}
