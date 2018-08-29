package com.example.alkautherwater.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alkautherwater.R;
import com.example.alkautherwater.activity.BuyProductActivity;
import com.example.alkautherwater.adapter.GalleryAdapter;
import com.example.alkautherwater.api.APIService;
import com.example.alkautherwater.api.APIUrl;
import com.example.alkautherwater.model.Image;
import com.example.alkautherwater.model.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductsFragment extends Fragment {
    private String TAG = ProductsFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    ArrayList<Image> images;
    public ProductsFragment() {
        // Required empty public constructor
    }
    // TODO: Rename parameter arguments, choose names that match
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=  inflater.inflate(R.layout.fragment_products, container, false);
        recyclerView =  root.findViewById(R.id.recycler_view);
        images=new ArrayList<>();
        pDialog = new ProgressDialog(getContext());
        getProducts();
        return root;
    }


    private void getProducts() {
        pDialog.setMessage("Loading...");
        pDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<Result> call = service.getProduct();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                pDialog.hide();
                mAdapter = new GalleryAdapter(getContext(), response.body().getItem(),"product");
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                images= response.body().getItem();
                recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getContext(), recyclerView, new GalleryAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Image image=images.get(position);
                        Intent i=new Intent(getContext(), BuyProductActivity.class);
                        i.putExtra("id",image.getProduct_id());
                        i.putExtra("product_name",image.getProductname());
                        i.putExtra("product_price",image.getPrice());
                        getActivity().startActivity(i);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));








            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                pDialog.hide();
            }
        });

    }


}
