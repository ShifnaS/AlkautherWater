package infogateway.alkautherwater.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import infogateway.alkautherwater.activity.Products;
import infogateway.alkautherwater.api.APIService;
import infogateway.alkautherwater.api.APIUrl;
import infogateway.alkautherwater.model.Results;
import infogateway.alkautherwater.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener{

    private EditText et_name,et_email,et_msg;
    private Button bt_cancel,bt_send;
    private Toolbar toolbar;
    //defining AwesomeValidation object
    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_contact_us, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar=root.findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Address");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


        et_name=root.findViewById(R.id.name);
        et_email=root.findViewById(R.id.email);
        et_msg=root.findViewById(R.id.message);
        bt_cancel=root.findViewById(R.id.cancel);
        bt_send=root.findViewById(R.id.send);
        bt_send.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);


        return root;
    }



    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.send:
                    String name= et_name.getText().toString().trim();
                    String email= et_email.getText().toString().trim();
                    String phone= "";
                    String message= et_msg.getText().toString().trim();
                    //process the data further
                    if(name.equals(""))
                    {
                        et_name.setError("name is required");

                    }

                    else if(isEmail(et_email)==false)
                    {
                        et_email.setError("enter valid email");
                    }

                    else if(message.equals(""))
                    {
                        et_msg.setError("message is required");

                    }
                    else
                    {
                        contactUs(name,email,phone,message);

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
    private String getCurrDate()
    {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        return  formattedDate;
    }
    private void contactUs(String name,String email,String phone,String msg) {

        String date=getCurrDate();
        Toast.makeText(getContext(), ""+date, Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<Results> call = service.contactUs(name, email, phone, msg,date);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                try {
                 //   Toast.makeText(getActivity(), "data "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if(response.body().getMessage().trim().equals("Successfully"))
                    {
                        Toast.makeText(getContext(), "Succesfully added", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getContext(), Products.class);
                        startActivity(i);
                        getActivity().finish();
                    }
                    else
                    {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
    private void clearAll() {

        et_name.setText("");
        et_email.setText("");
        et_msg.setText("");

        et_name.setError(null);
        et_email.setError(null);
        et_msg.setError(null);

    }
    private boolean isEmail(EditText editText)
    {
        CharSequence txt=editText.getText().toString().trim();
        return(!TextUtils.isEmpty(txt)&&Patterns.EMAIL_ADDRESS.matcher(txt).matches());
    }

}
