package amnesiascheduler.ephraim.com.amnesiascheduler;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RingerActivity extends AppCompatActivity {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private String uid, rid;
    private TextView purpose, ringdate;
    private Button btn;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringer);

        uid = getIntent().getStringExtra("uid");
        rid = getIntent().getStringExtra("rid");

        openHelper = new SQLDBHelper(RingerActivity.this);
        db = openHelper.getWritableDatabase();
        purpose = (TextView) findViewById(R.id.ra_purpose_tv);
        ringdate = (TextView) findViewById(R.id.ra_ringdate_tv);
        btn = (Button) findViewById(R.id.ra_off_btn);

        String query = String.format("SELECT * FROM %s WHERE %s = ?", SQLDBHelper.REMINDER, "reminderid");
        String[] args = {rid};
        Cursor cursor = db.rawQuery(query, args);
        cursor.moveToNext();

        purpose.setText(cursor.getString(cursor.getColumnIndex("purpose")));
        ringdate.setText(cursor.getString(cursor.getColumnIndex("ringdate")));

        mp = MediaPlayer.create(this, R.raw.ring);
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                mp.release();

                ContentValues cv = new ContentValues();
                cv.put("status", "off");
                String[] args = {rid};
                db.update(SQLDBHelper.REMINDER, cv, "reminderid = ?", args);

                Intent intent = new Intent(RingerActivity.this, HomeActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        mp.stop();
        mp.release();

        Intent intent = new Intent(RingerActivity.this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}
