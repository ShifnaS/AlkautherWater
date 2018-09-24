package infogateway.alkautherwater.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import infogateway.alkautherwater.api.APIService;
import infogateway.alkautherwater.api.APIUrl;
import infogateway.alkautherwater.app.Config;
import infogateway.alkautherwater.model.Results;
import infogateway.alkautherwater.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuyProductActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_productName,et_quantity,et_customerName,et_phone,et_address;
    Button bt_cancel,bt_confirm;
    int product_id=0;
    private boolean isReached = false;
    private ProgressDialog progress;

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
      //  et_price=findViewById(R.id.price);
        et_quantity=findViewById(R.id.quantity);
        et_customerName=findViewById(R.id.cname);
        et_phone=findViewById(R.id.phone);
        et_address=findViewById(R.id.address);

        et_productName.setText(product_name);
       // et_price.setText(product_price+" OMR (per unit)");

        bt_cancel=findViewById(R.id.cancel);
        bt_confirm=findViewById(R.id.confirm);

        bt_confirm.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);


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

        progress=new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

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
                        Toast.makeText(BuyProductActivity.this, "Order Received we will make a confirmation soon", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(), Products.class);
                        startActivity(i);
                        finish();
                        progress.dismiss();
                    }
                    else
                    {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to make order", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    progress.dismiss();

                }

            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                progress.dismiss();
                Log.e("MyTag", "requestFailed", t);

            }
        });
    }
}
