package infogateway.alkautherwater.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import infogateway.alkautherwater.activity.NotificationActivity;
import infogateway.alkautherwater.helper.SQLiteOperations;
import infogateway.alkautherwater.model.Notification;
import infogateway.alkautherwater.R;

/**
 * Created by admin on 03-Nov-17.
 */

public class NotificationCustomAdapter extends RecyclerView.Adapter<NotificationCustomAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<Notification> data= Collections.emptyList();

    public NotificationCustomAdapter(Context context, List<Notification> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.content_notifications, parent,false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHolder myHolder= (ViewHolder) holder;
        final Notification currentData=data.get(position);
        myHolder.tv_title.setText(currentData.getTitle());
        myHolder.tv_message.setText( currentData.getMessage());
        myHolder.tv_date.setText(currentData.getDate());
        myHolder.tv_date.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=currentData.getId();
                Toast.makeText(context, ""+id, Toast.LENGTH_SHORT).show();
                SQLiteOperations sqLiteOperations=new SQLiteOperations(context);
                sqLiteOperations.deleteNotification(id);
                Intent i=new Intent(context,NotificationActivity.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_message;
        TextView tv_date;
        Button bt_delete;



        public  ViewHolder(View itemView) {
            super(itemView);
            tv_title= (TextView) itemView.findViewById(R.id.title);
            tv_message = (TextView) itemView.findViewById(R.id.message);
            tv_date = (TextView) itemView.findViewById(R.id.date);
            bt_delete = (Button) itemView.findViewById(R.id.delete);

        }

    }
}
