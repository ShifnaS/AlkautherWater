package com.example.alkautherwater.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener{

    EditText et_name,et_email,et_phone,et_msg;
    Button bt_cancel,bt_send;
    Toolbar toolbar;
    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_contact_us, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_name=root.findViewById(R.id.name);
        et_email=root.findViewById(R.id.email);
        et_phone=root.findViewById(R.id.phone);
        et_msg=root.findViewById(R.id.message);
        bt_cancel=root.findViewById(R.id.cancel);
        bt_send=root.findViewById(R.id.send);

        bt_send.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);

        toolbar=root.findViewById(R.id.toolbar);
        toolbar.setTitle("Contact Address");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        return root;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.send:
                    String name= et_name.getText().toString().trim();
                    String email= et_email.getText().toString().trim();
                    String phone= et_phone.getText().toString().trim();
                    String message= et_msg.getText().toString().trim();
                    if(!name.equals("")||!email.equals("")||!phone.equals("")||!message.equals(""))
                    {
                        contactUs(name,email,phone,message);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "please enter all fields", Toast.LENGTH_SHORT).show();
                    }
                break;

            case R.id.cancel:

                break;
            default:
                Log.e("default","default case");
                break;

        }
    }
    private void contactUs(String name,String email,String phone,String msg) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<Results> call = service.contactUs(name, email, phone, msg);
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                try {
                      Toast.makeText(getActivity(), "data "+response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
