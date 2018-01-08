package com.vinay.wizdem.spacestationfinder.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinay.wizdem.spacestationfinder.R;
import com.vinay.wizdem.spacestationfinder.model.flyby.Response;

import java.util.List;

/**
 * Created by vinay_1 on 1/6/2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    List<Response> model;

    public MainAdapter(List<Response> responses) {
        model = responses;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        holder.time_stamp.setText(model.get(position).getRisetime().toString());
        holder.duration.setText(model.get(position).getDuration().toString()+" "+"Sec");
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time_stamp, duration;
        public ViewHolder(View itemView) {
            super(itemView);

            time_stamp = (TextView)itemView.findViewById(R.id.time_stamp);
            duration = (TextView)itemView.findViewById(R.id.duration);
        }
    }
}
