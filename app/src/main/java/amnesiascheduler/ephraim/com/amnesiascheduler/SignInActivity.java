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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText uname, pass;
    private Button signin_btn;
    private TextView signup;

    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        openHelper = new SQLDBHelper(SignInActivity.this);
        db = openHelper.getWritableDatabase();

        uname =(EditText) findViewById(R.id.si_username_et);
        pass = (EditText) findViewById(R.id.si_password_et);
        signin_btn = (Button) findViewById(R.id.si_si_btn);
        signup = (TextView) findViewById(R.id.si_su_tv);

        //go to sign up activity
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //sign in button action
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname_val = uname.getText().toString();
                String pass_val = pass.getText().toString();

                if(!"".equals(uname_val.trim()) && !"".equals(pass_val.trim())){
                    String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?",
                            SQLDBHelper.USERS, "email", "password");

                    String[] args = {
                            uname_val,
                            pass_val
                    };
                    Cursor cursor = db.rawQuery(query, args);

                    if(cursor.moveToNext()){
                        Intent intent = new Intent(SignInActivity.this,
                                HomeActivity.class);
                        intent.putExtra("uid", uname_val);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(SignInActivity.this, "Incorrect sign in details!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignInActivity.this, "Empty field detected!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alerter=new AlertDialog.Builder(SignInActivity.this);
        alerter.setTitle("Quit");
        alerter.setMessage("Close application!");
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
                System.exit(0);
            }
        });
        alerter.show();
    }
}
