package amnesiascheduler.ephraim.com.amnesiascheduler;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetReminderActivity extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView sel_date, sel_time;
    private EditText purpose;
    private Button setreminder;
    private String uid;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    private AlarmManager alarmMgr;
    private PendingIntent sAlarmIntent;
    private PendingIntent iAlarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        uid = getIntent().getStringExtra("uid");
        sel_date = (TextView) findViewById(R.id.sr_date_tv);
        sel_time = (TextView) findViewById(R.id.sr_time_tv);
        purpose = (EditText) findViewById(R.id.sr_purpose_et);
        setreminder = (Button) findViewById(R.id.sr_set_btn);
        openHelper = new SQLDBHelper(SetReminderActivity.this);
        db = openHelper.getWritableDatabase();

        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        sel_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(SetReminderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String formated_date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                        + "/" + String.valueOf(year);
                                sel_date.setText(formated_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        sel_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(SetReminderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String formated_date = String.valueOf(i) + ":" + String.valueOf(i1);
                        sel_time.setText(formated_date);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        setreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String purpose_val = String.valueOf(purpose.getText());
                String date_val = String.valueOf(sel_date.getText());
                String time_val = String.valueOf(sel_time.getText());
                String full_date = date_val + " " + time_val;
                SimpleDateFormat sdf = new SimpleDateFormat("dd MM YYYY");
                String set_date = sdf.format(new Date());

                if(!"".equalsIgnoreCase(purpose_val.trim()) && !"".equalsIgnoreCase(date_val.trim())){
                    ContentValues cv = new ContentValues();
                    cv.put("purpose", purpose_val);
                    cv.put("user", uid);
                    cv.put("setdate", set_date);
                    cv.put("ringdate", full_date);
                    cv.put("status", "on");
                    db.insert(SQLDBHelper.REMINDER, null, cv);

                    AlertDialog.Builder alerter=new AlertDialog.Builder(SetReminderActivity.this);
                    alerter.setTitle("Success!");
                    alerter.setMessage("Reminder set successfully!");
                    alerter.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            Intent intent = new Intent(SetReminderActivity.this,
                                    HomeActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    alerter.show();
                }else{
                    Toast.makeText(SetReminderActivity.this, "Empty field detected!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SetReminderActivity.this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}
