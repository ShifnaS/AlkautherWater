package com.example.alkautherwater.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alkautherwater.R;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{


    //The view objects
    private EditText editTextName, editTextEmail, editTextMobile,
            editTextDob, editTextAge;

    private Button buttonSubmit;

    //defining AwesomeValidation object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //initializing awesomevalidation object
        /*
         * The library provides 3 types of validation
         * BASIC
         * COLORATION
         * UNDERLABEL
         * */

        //initializing view objects
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        editTextDob = (EditText) findViewById(R.id.editTextDob);
        editTextAge = (EditText) findViewById(R.id.editTextAge);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);




        buttonSubmit.setOnClickListener(this);
    }

    private boolean isEmpty(EditText editTextName)
    {
        CharSequence txt=editTextName.getText().toString().trim();
        return(TextUtils.isEmpty(txt));
    }



    private void submitForm() {

            if(isEmpty(editTextName))
            {
                editTextName.setError("Invalid name");
            }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSubmit) {
            submitForm();
        }
    }
}
