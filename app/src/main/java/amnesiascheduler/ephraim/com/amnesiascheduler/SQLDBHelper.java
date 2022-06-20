package amnesiascheduler.ephraim.com.amnesiascheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME="amnesiascheduler.db";
    private static int DATABASE_VERSION=1;
    private SQLiteDatabase db;

    String createUsers = "create table users(email TEXT PRIMARY KEY, role TEXT, password TEXT," +
            "fullname TEXT, mobile TEXT)";
    String createReminder = "create table reminder(reminderid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "purpose TEXT, user TEXT, setdate TEXT, ringdate TEXT, status TEXT)";

    public static String USERS = "users";
    public static String REMINDER = "reminder";

    public SQLDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db=sqLiteDatabase;
        sqLiteDatabase.execSQL(createUsers);
        sqLiteDatabase.execSQL(createReminder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + USERS);
        sqLiteDatabase.execSQL("drop table " + REMINDER);
        onCreate(sqLiteDatabase);
    }
}
