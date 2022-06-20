package amnesiascheduler.ephraim.com.amnesiascheduler;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText email, pass, cpass, mobile, fullname;
    private Button signup_btn;
    private TextView signin;

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        openHelper = new SQLDBHelper(SignUpActivity.this);
        db = openHelper.getWritableDatabase();
        email = (EditText) findViewById(R.id.su_email_et);
        pass = (EditText) findViewById(R.id.su_pass_et);
        cpass = (EditText) findViewById(R.id.su_cpass_et);
        mobile = (EditText) findViewById(R.id.su_mobile_et);
        fullname = (EditText) findViewById(R.id.su_fullname_et);
        signup_btn = (Button) findViewById(R.id.su_su_btn);
        signin = (TextView) findViewById(R.id.su_si_tv);

        //go to sign in activity
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //register the details
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_val = email.getText().toString();
                String pass_val = pass.getText().toString();
                String cpass_val = cpass.getText().toString();
                String mobile_val = mobile.getText().toString();
                String fullname_val = fullname.getText().toString();

                if(!"".equals(email_val.trim()) && !"".equals(pass_val.trim()) &&
                        !"".equals(cpass_val.trim()) && !"".equals(mobile_val.trim()) &&
                        !"".equals(fullname_val.trim())){
                    if(cpass_val.equals(pass_val)){
                        ContentValues user_cv = new ContentValues();
                        user_cv.put("email", email_val);
                        user_cv.put("role", "mother");
                        user_cv.put("password", pass_val);
                        user_cv.put("fullname",fullname_val);
                        user_cv.put("mobile",mobile_val);

                        db.insert(SQLDBHelper.USERS, null, user_cv); //store in users table

                        AlertDialog.Builder alerter=new AlertDialog.Builder(SignUpActivity.this);
                        alerter.setTitle("Success!");
                        alerter.setMessage("Registration successful!");
                        alerter.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                Intent intent = new Intent(SignUpActivity.this,
                                        SignInActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alerter.show();
                    }else{
                        Toast.makeText(SignUpActivity.this, "Password mismatch!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this, "Empty field detected!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alerter=new AlertDialog.Builder(SignUpActivity.this);
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
