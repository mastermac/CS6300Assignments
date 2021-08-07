package edu.gatech.seclass.sdpencryptor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText missive;
    private EditText a;
    private EditText b;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        missive = (EditText) findViewById(R.id.missiveID);
        a = (EditText) findViewById(R.id.aParamID);
        b = (EditText) findViewById(R.id.bParamID);
        result = (TextView) findViewById(R.id.encryptedMissiveID);
        a.setText("1");
        b.setText("1");

    }

    public void encipher(View view) {
        if (view.getId()!=R.id.encipherButtonID){
            return;
        }
        String finalresult;
        String m = missive.getText().toString();
        int inta = Integer.parseInt(a.getText().toString());
        int intb = Integer.parseInt(b.getText().toString());
        boolean pass=true;
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9, 11, 15,
                17, 19, 21, 23, 25));
        if (TextUtils.isEmpty(missive.getText())||(m.matches("[^a-zA-Z]+"))){
            missive.setError("Invalid Missive");
            pass=false;
        }
        if (TextUtils.isEmpty(a.getText())|| !numbers.contains(inta)){
            a.setError("Invalid Parameter A");
            pass=false;
        }
        if (TextUtils.isEmpty(b.getText())||intb>26||intb<1){
            b.setError("Invalid Parameter B");
            pass=false;
        }
        if (!pass){
            result.setText("");
        }else{
            finalresult = encrypt(m, inta, intb);
            result.setText(finalresult);
        }


    }

    public String encrypt(String m, int inta, int intb) {
        char[] ch = m.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char temp = ch[i];
            if (Character.isLetter(temp)) {
                if (Character.isLowerCase(temp)) {
                    int n = ((temp - 'a') * inta + intb) % 26;
                    ch[i] = (char) (n + 65);
                } else {
                    int n = ((temp - 'A') * inta + intb) % 26;
                    ch[i] = (char) (n + 97);
                }

            }
        }
        return String.valueOf(ch);
    }
}