package com.example.alkautherwater.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class FreezerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    Spinner sp;
    ArrayList<String> productsList;
    ArrayList<Integer> productidList;
    ArrayList<Image> images;
    EditText et_quantity,et_customerName,et_phone,et_address;
    Button bt_cancel,bt_confirm;
    int product_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Buy Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_freezer);
        sp=findViewById(R.id.pname);
        et_quantity=findViewById(R.id.quantity);
        et_customerName=findViewById(R.id.cname);
        et_phone=findViewById(R.id.phone);
        et_address=findViewById(R.id.address);
        productsList=new ArrayList<String>();
        productidList=new ArrayList<Integer>();
        getProducts();
        sp.setOnItemSelectedListener(this);
        bt_cancel=findViewById(R.id.cancel);
        bt_confirm=findViewById(R.id.confirm);

        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Image image=images.get(i);
        product_id = image.getProduct_id();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
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

    private void getProducts() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<Result> call = service.getProduct();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                images= response.body().getItem();
                for(int i=0;i<images.size();i++)
                {
                    Image image=images.get(i);
                    productsList.add( image.getProductname());
                    productidList.add(image.getProduct_id());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, productsList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.confirm:
                String quantity= et_quantity.getText().toString().trim();
                String customer_name= et_customerName.getText().toString().trim();
                String phone= et_phone.getText().toString().trim();
                String address= et_address.getText().toString().trim();

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                String regId = pref.getString("regId", null);
                Log.e("fid",""+regId);
                // Toast.makeText(this, ""+regId, Toast.LENGTH_SHORT).show();
                //  et_quantity.setText(regId);
                if(quantity.equals(""))
                {
                    et_quantity.setError("quantity is required");

                }
                else  if(customer_name.equals(""))
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
                    placeOrder(quantity,customer_name,phone,address,regId);
                }
                break;

            case R.id.cancel:
                clearAll();

                break;
            default:
                Log.e("default","default case");
                break;

        }
    }
    private void clearAll() {

        et_customerName.setText("");
        et_quantity.setText("");
        et_address.setText("");
        et_phone.setText("");

        et_customerName.setError(null);
        et_quantity.setError(null);
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
    private void placeOrder(String quantity, String customer_name, String phone, String address,String regId)
    {
     //   Toast.makeText(this, ""+product_id, Toast.LENGTH_SHORT).show();

        String date=getCurrDate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<Results> call = service.buyProduct(product_id,quantity,customer_name,phone,address,date,regId);
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
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.e("MyTag", "requestFailed", t);
            }
        });
    }
}
