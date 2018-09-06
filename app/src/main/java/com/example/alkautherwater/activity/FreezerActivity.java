package com.example.alkautherwater.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alkautherwater.R;
import com.example.alkautherwater.adapter.GalleryAdapter;
import com.example.alkautherwater.api.APIService;
import com.example.alkautherwater.api.APIUrl;
import com.example.alkautherwater.app.Config;
import com.example.alkautherwater.model.Image;
import com.example.alkautherwater.model.Result;
import com.example.alkautherwater.model.Results;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FreezerActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> productsList;
    ArrayList<Integer> productidList;
    EditText et_customerName,et_phone,et_address;
    TextView from_date,to_date;
    Button bt_cancel,bt_confirm;
    private boolean isReached = false;
    private ProgressDialog progress;
    private Calendar calendar;

    private int year, month, day;
    private int toyear, tomonth, today;
    //DatePickerDialog.OnDateSetListener from_dateListener,to_dateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Freezer and Cold Water Supply");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_freezer);
        et_customerName=findViewById(R.id.cname);
        et_phone=findViewById(R.id.phone);
        et_address=findViewById(R.id.address);
        from_date=findViewById(R.id.fromdate);
        to_date=findViewById(R.id.todate);


        productsList=new ArrayList<String>();
        productidList=new ArrayList<Integer>();
        bt_cancel=findViewById(R.id.cancel);
        bt_confirm=findViewById(R.id.confirm);

        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        from_date.setOnClickListener(this);
        to_date.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        toyear = calendar.get(Calendar.YEAR);
        tomonth = calendar.get(Calendar.MONTH);
        today = calendar.get(Calendar.DAY_OF_MONTH);

        et_address.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if edittext has 10chars & this is not called yet, add new line
                if(et_address.getText().length() == 50 && !isReached) {
                    et_address.append("\n");
                    isReached = true;
                }
                // if edittext has less than 10chars & boolean has changed, reset
                if(et_address.getText().length() < 50 && isReached) isReached = false;
            }
        });
    }





    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch(id){
            case 0:
                return new DatePickerDialog(this,
                        from_dateListener, year, month, day);
            case 1:
                return new DatePickerDialog(this,
                        to_dateListener, toyear, tomonth, today);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener from_dateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    from_date.setText(new StringBuilder().append(arg3).append("/")
                            .append(arg2+1).append("/").append(arg1));
                }
            };

    private DatePickerDialog.OnDateSetListener to_dateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    to_date.setText(new StringBuilder().append(arg3).append("/")
                            .append(arg2+1).append("/").append(arg1));
                }
            };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.confirm:
                bt_confirm.setEnabled(false);

                String customer_name= et_customerName.getText().toString().trim();
                String phone= et_phone.getText().toString().trim();
                String address= et_address.getText().toString().trim();
                String fromdate= from_date.getText().toString().trim();
                String todate= to_date.getText().toString().trim();

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String regId = pref.getString("regId", null);
                Log.e("fid",""+regId);
                // Toast.makeText(this, ""+regId, Toast.LENGTH_SHORT).show();
                //  et_quantity.setText(regId);
                 if(customer_name.equals(""))
                {
                    et_customerName.setError("customer name is required");

                }

                else if(phone.length()!=8||phone.equals(""))
                {
                    et_phone.setError("enter valid phone number");
                }

                else if(address.equals(""))
                {
                    et_address.setError("address is required");

                }
                else
                {
                    placeOrder(todate,customer_name,phone,address,regId,fromdate);
                }
                break;

            case R.id.cancel:
                clearAll();
                break;
            case R.id.fromdate:
                showDialog(0);

                break;
            case R.id.todate:
                showDialog(1);

                break;
            default:
                Log.e("default","default case");
                break;

        }
    }
    private void clearAll() {

        et_customerName.setText("");
        et_address.setText("");
        et_phone.setText("");

        et_customerName.setError(null);
        et_address.setError(null);
        et_phone.setError(null);

    }
    private String getCurrDate()
    {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        return  formattedDate;
    }
    private void placeOrder(String todate, String customer_name, String phone, String address,String regId,String fromdate)
    {
        String date=getCurrDate();

        progress=new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<Results> call = service.buyFreezer(todate,customer_name,phone,address,fromdate,regId,date);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                try {
                    // Toast.makeText(getApplicationContext(), "data "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if(response.body().getMessage().trim().equals("Successfully"))
                    {
                        Toast.makeText(FreezerActivity.this, "Your order is placed successfully", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(), Products.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed to make order", Toast.LENGTH_SHORT).show();
                    }
                    bt_confirm.setEnabled(true);
                    progress.dismiss();
                }
                catch (Exception e) {
                    bt_confirm.setEnabled(true);
                    progress.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                bt_confirm.setEnabled(true);
                progress.dismiss();
                Log.e("MyTag", "requestFailed", t);
            }
        });
    }
}
