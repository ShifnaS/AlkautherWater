package com.example.alkautherwater.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alkautherwater.R;
import com.example.alkautherwater.api.APIService;
import com.example.alkautherwater.api.APIUrl;
import com.example.alkautherwater.model.Results;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuyProductActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_productName,et_price,et_quantity,et_customerName,et_email,et_phone,et_pincode,et_address;
    Button bt_cancel,bt_confirm;
    int product_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setTitle("Buy Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i=getIntent();
        product_id=i.getIntExtra("id",0);
        String product_name=i.getStringExtra("product_name");
        String product_price=i.getStringExtra("product_price");

        et_productName=findViewById(R.id.pname);
        et_price=findViewById(R.id.price);
        et_quantity=findViewById(R.id.quantity);
        et_customerName=findViewById(R.id.cname);
        et_phone=findViewById(R.id.phone);
        et_pincode=findViewById(R.id.pincode);
        et_address=findViewById(R.id.address);

        et_productName.setText(product_name);
        et_price.setText(product_price+" OMR (per unit)");

        bt_cancel=findViewById(R.id.cancel);
        bt_confirm=findViewById(R.id.confirm);

        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.confirm:
                String quantity= et_quantity.getText().toString().trim();
                String customer_name= et_customerName.getText().toString().trim();
                String phone= et_phone.getText().toString().trim();
                String pincode= et_pincode.getText().toString().trim();
                String address= et_address.getText().toString().trim();
                if(quantity.equals("")||customer_name.equals("")||phone.equals("")||pincode.equals("")||address.equals(""))
                {
                    Toast.makeText(this, "please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    placeOrder(quantity,customer_name,phone,pincode,address);
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
        et_pincode.setText("");
        et_address.setText("");
        et_phone.setText("");

    }

    private void placeOrder(String quantity, String customer_name, String phone, String pincode, String address) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<Results> call = service.buyProduct(product_id,quantity,customer_name,phone,pincode,address);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                try {
                    Toast.makeText(getApplicationContext(), "data "+response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
