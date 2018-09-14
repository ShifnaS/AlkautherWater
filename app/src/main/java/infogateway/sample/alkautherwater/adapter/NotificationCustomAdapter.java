package infogateway.sample.alkautherwater.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;




import java.util.Collections;
import java.util.List;

import infogateway.sample.alkautherwater.R;
import infogateway.sample.alkautherwater.model.Notification;

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
        Notification currentData=data.get(position);
        myHolder.tv_title.setText(currentData.getTitle());
        myHolder.tv_message.setText( currentData.getMessage());
        myHolder.tv_date.setText(currentData.getDate());
        myHolder.tv_date.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_message;
        TextView tv_date;



        public  ViewHolder(View itemView) {
            super(itemView);
            tv_title= (TextView) itemView.findViewById(R.id.title);
            tv_message = (TextView) itemView.findViewById(R.id.message);
            tv_date = (TextView) itemView.findViewById(R.id.date);

        }

    }
}
