package com.playit.playit;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends ActionBarActivity {


    //TextView t;
    private Button b;
    private EditText user, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText)findViewById(R.id.editText1);
        pass = (EditText)findViewById(R.id.editText2);

        b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dbName = "playit.cafywfvb4krw.eu-central-1.rds.amazonaws.com:3306/PlayItDB";
                String hostname = System.getProperty("RDS_HOSTNAME");
                String port = System.getProperty("RDS_PORT");
                String jdbcUrl = "jdbc:mysql://" + dbName + "?user=" + "root" + "&password=" + "toortoor";
                String userbd = null;
                String passbd = null;


                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(jdbcUrl);
                    Statement readStatement = conn.createStatement();
                    ResultSet resultSet = readStatement.executeQuery("SELECT * FROM Persons where Name = '"+user.toString()+"';");
                    resultSet.first();
                    userbd = resultSet.getString("Name");
                    passbd = resultSet.getString("Password");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }



                if (user.toString().equals(userbd) && pass.toString().equals(passbd)) {
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    startActivity(i);
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), "Bad Log In" + userbd + " " + passbd, Toast.LENGTH_SHORT);
                    t.show();
                }
/*
                if(!user.toString().equals("") && !pass.getText().toString().equals("")) {
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    startActivity(i);
                }
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "Bad Log In", Toast.LENGTH_SHORT);
                    t.show();
                }*/
            }
        });
        /*t = (TextView)findViewById(R.id.textView1);
        t.setText("Hola");*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
