package com.example.alkautherwater.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alkautherwater.R;
import com.example.alkautherwater.adapter.GalleryAdapter;
import com.example.alkautherwater.adapter.MyCustomPagerAdapter;
import com.example.alkautherwater.api.APIService;
import com.example.alkautherwater.api.APIUrl;
import com.example.alkautherwater.model.Image;
import com.example.alkautherwater.model.Result;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    int IMAGES[] = { R.drawable.s2, R.drawable.s3, R.drawable.s4};
    MyCustomPagerAdapter myCustomPagerAdapter;
    CirclePageIndicator indicator;


    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    ArrayList<Image> images;
    Button bt_order;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.content_home1, container, false);
        mPager = (ViewPager) root.findViewById(R.id.pager);
        indicator = (CirclePageIndicator) root.findViewById(R.id.indicator);

        recyclerView =  root.findViewById(R.id.recycler_view);
        images=new ArrayList<>();
        pDialog = new ProgressDialog(getContext());
        //getProducts();
        bt_order=root.findViewById(R.id.order);
        bt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              Fragment fragment = new ProductsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
             //   fragmentManager.findFragmentById();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame1, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
        init();
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
                mAdapter = new GalleryAdapter(getContext(), response.body().getItem(),"home");
                RecyclerView.LayoutManager mLayoutManager   = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                // Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                images= response.body().getItem();



                recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getContext(), recyclerView, new GalleryAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", images);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
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

    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager.setAdapter(new MyCustomPagerAdapter(getContext(), ImagesArray));


        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}