package amnesiascheduler.ephraim.com.amnesiascheduler;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ReminderHistoryActivity extends AppCompatActivity {

    private ListView listview;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private String[] titles, ids;
    private boolean flag;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_history);

        uid = getIntent().getStringExtra("uid");
        openHelper = new SQLDBHelper(ReminderHistoryActivity.this);
        db = openHelper.getWritableDatabase();
        listview = (ListView) findViewById(R.id.sh_list_view);
        flag = false;
        getAllSchedules();

        if(flag){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ReminderHistoryActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, titles);
            listview.setAdapter(adapter);

            /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ReminderHistoryActivity.this,
                            .class);
                    intent.putExtra("pid", ids[i]);
                    startActivity(intent);
                    finish();
                }
            });*/
        }

    }

    private void getAllSchedules(){
        String query = String.format("SELECT * FROM %s WHERE %s = ?", SQLDBHelper.REMINDER, "user");
        String[] args = {uid};
        Cursor cursor = db.rawQuery(query, args);
        int total = cursor.getCount();
        if(total > 0){
            flag = true;
            int array_index = 0;
            ids = new String[total];
            titles = new String[total];

            while (cursor.moveToNext()){
                ids[array_index] = String.valueOf(cursor.getInt(cursor.getColumnIndex("reminderid")));
                titles[array_index] = cursor.getString(cursor.getColumnIndex("ringdate")) + "- "
                        + cursor.getString(cursor.getColumnIndex("purpose"));
                array_index++;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReminderHistoryActivity.this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
        finish();
    }
}
