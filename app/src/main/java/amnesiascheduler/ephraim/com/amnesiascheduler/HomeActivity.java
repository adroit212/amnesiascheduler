package amnesiascheduler.ephraim.com.amnesiascheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    GridView grid;
    String[] labels={
            "Set Reminder",
            "Reminder History",
            "Sign Out"
    };

    int[] images={
            R.drawable.alarm,
            R.drawable.reminderhistory,
            R.drawable.quit
    };

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        uid = getIntent().getStringExtra("uid");
        openHelper = new SQLDBHelper(HomeActivity.this);
        db = openHelper.getWritableDatabase();
        GridAdapter adapter=new GridAdapter(HomeActivity.this, labels, images);
        grid=(GridView) findViewById(R.id.main_grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){ //book appointment
                    Intent intent=new Intent(HomeActivity.this, SetReminderActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                    finish();
                }else if(i==1){ //self Test
                    Intent intent=new Intent(HomeActivity.this, ReminderHistoryActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                    finish();
                }else if (i==2){ //logout
                    AlertDialog.Builder alerter=new AlertDialog.Builder(HomeActivity.this);
                    alerter.setTitle("Logout");
                    alerter.setMessage("Logout session!");
                    alerter.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Do nothing
                        }
                    });
                    alerter.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //close application
                            Intent logout_intent = new Intent(HomeActivity.this, SignInActivity.class);
                            startActivity(logout_intent);
                            finish();
                        }
                    });
                    alerter.show();
                }
            }
        });

        Thread logoTimer=new Thread(){
            public void run(){
                try{
                    String rid = checkReminderTimers();
                    while(rid == null){
                        sleep(100);
                        rid = checkReminderTimers();
                    }
                    Intent alarm_intent = new Intent(HomeActivity.this, RingerActivity.class);
                    alarm_intent.putExtra("uid", uid);
                    alarm_intent.putExtra("rid", rid);
                    this.interrupt();
                    startActivity(alarm_intent);
                    finish();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    finish();
                }
            }
        };
        logoTimer.start();

    }

    private String checkReminderTimers(){
        String query = String.format("SELECT * FROM %s WHERE %s = ? and %s = ?", SQLDBHelper.REMINDER, "user","status");
        String[] args = {uid,"on"};
        Cursor cursor = db.rawQuery(query, args);
        int total = cursor.getCount();
        if(total > 0){


            while (cursor.moveToNext()){
                String status = cursor.getString(cursor.getColumnIndex("status"));
                if(status.equalsIgnoreCase("on")){
                    int[] splitted_date = splitDateAndTime(cursor.getString(cursor.getColumnIndex("ringdate")));
                    int day = splitted_date[0];
                    int month = splitted_date[1];
                    int year = splitted_date[2];
                    int hour = splitted_date[3];
                    int minute = splitted_date[4];

                    SimpleDateFormat cd_sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                    String cur_date = cd_sdf.format(new Date());
                    int[] splitted_cur_date = splitDateAndTime(cur_date);

                    int cur_day = splitted_cur_date[0];
                    int cur_month = splitted_cur_date[1];
                    int cur_year = splitted_cur_date[2];
                    int cur_hour = splitted_cur_date[3];
                    int cur_minute = splitted_cur_date[4];

                    if(year == cur_year){
                        if(month == cur_month){
                            if(day == cur_day){
                                if(hour == cur_hour){
                                    if(minute == cur_minute){
                                        String reminderid = String.valueOf(cursor.getInt(cursor.getColumnIndex("reminderid")));
                                        return reminderid;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private int[] splitDateAndTime(String raw_full_date){
        String[] splitted_date = raw_full_date.split(" ");
        String raw_date = splitted_date[0];
        String raw_time = splitted_date[1];

        String[] splitted_raw_date = raw_date.split("/");
        int day = Integer.valueOf(splitted_raw_date[0]);
        int month = Integer.valueOf(splitted_raw_date[1]);
        int year = Integer.valueOf(splitted_raw_date[2]);

        String[] splitted_raw_time = raw_time.split(":");
        int hour = Integer.valueOf(splitted_raw_time[0]);
        int minute = Integer.valueOf(splitted_raw_time[1]);

        int[] result = {
                day,
                month,
                year,
                hour,
                minute
        };

        return result;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alerter=new AlertDialog.Builder(HomeActivity.this);
        alerter.setTitle("Logout");
        alerter.setMessage("Logout session!");
        alerter.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing
            }
        });
        alerter.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close application
                Intent logout_intent = new Intent(HomeActivity.this, SignInActivity.class);
                startActivity(logout_intent);
                finish();
            }
        });
        alerter.show();
    }
}
