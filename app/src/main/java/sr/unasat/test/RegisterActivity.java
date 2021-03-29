package sr.unasat.test;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    Button mButtonGenerate;


    TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        mTextUsername = (EditText)findViewById(R.id.edittext_username);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText)findViewById(R.id.edittext_cnf_password);
        mButtonRegister = (Button)findViewById(R.id.button_register);
        mButtonGenerate = (Button)findViewById(R.id.button_generate);
        mTextViewLogin = (TextView)findViewById(R.id.textview_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });

        NetworkTask nt = new NetworkTask();
        nt.execute();


        mButtonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask nt = new NetworkTask();
                nt.execute();
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();

                if (user != null && user != "" && pwd != null && pwd != "") {
                    if (pwd.equals(cnf_pwd)) {
                        boolean is_existent = db.checkUser(user, pwd);
                        if (is_existent){
                            Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!is_existent ){
                            long val = db.addUser(user, pwd);

                            if (val > 0) {
                                Toast.makeText(RegisterActivity.this, "You have registered", Toast.LENGTH_SHORT).show();
                                Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(moveToLogin);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registeration Error", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    Toast.makeText(RegisterActivity.this, "Username and Password is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public void clear(View view) {
        mTextCnfPassword.setText(null);
        mTextPassword.setText(null);
        mTextUsername.setText(null);
    }

    class NetworkTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url = new URL("https://randomuser.me/api/");
                System.out.println(url.getHost());
                InputStream is = url.openStream();
                byte[] buffer = new byte[4096];
                StringBuilder sb = new StringBuilder("");


                while (is.read(buffer) != -1) {
                    sb.append(new String(buffer));
                }

                Log.i("apiresponse", sb.toString());

                try {
                    JSONObject obj = new JSONObject(sb.toString());
                    JSONArray results = obj.getJSONArray("results");
                    JSONObject user = results.getJSONObject(0);
                    JSONObject loginObj = user.getJSONObject("login");
                    String username = loginObj.getString("username");
                    publishProgress(username, 0);
                    String password = loginObj.getString("password");
                    publishProgress(password, 1);


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            Toast.makeText(RegisterActivity.this, values[0].toString(), Toast.LENGTH_SHORT).show();
            switch (Integer.parseInt(values[1] + "")) {
                case 0:

                    mTextUsername.setText(values[0].toString());
                    break;
                case 1:
                    mTextPassword.setText(values[0].toString());
                    mTextCnfPassword.setText(values[0].toString());
                    break;
            }
        }
    }

}