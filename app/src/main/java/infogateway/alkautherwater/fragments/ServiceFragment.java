package infogateway.alkautherwater.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import infogateway.alkautherwater.activity.FreezerActivity;
import infogateway.alkautherwater.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment {


    public ServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_service, container, false);
        Button button=root.findViewById(R.id.order_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getContext(), FreezerActivity.class);
                startActivity(i);
            }
        });
        return root;
    }

}
