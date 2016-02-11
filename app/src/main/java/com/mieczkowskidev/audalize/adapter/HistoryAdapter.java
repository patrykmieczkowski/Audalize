package com.mieczkowskidev.audalize.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mieczkowskidev.audalize.R;
import com.mieczkowskidev.audalize.model.DataResources;

import java.util.List;

/**
 * Created by Patryk Mieczkowski on 06.02.16
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.EventHolder> {

    private Context context;
    private List<DataResources> dataResourcesList;

    public HistoryAdapter(Context context, List<DataResources> dataResourcesList) {
        this.context = context;
        this.dataResourcesList = dataResourcesList;
    }

    @Override
    public HistoryAdapter.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        return new EventHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.EventHolder holder, int position) {

        holder.mainText.setText(dataResourcesList.get(position).getName().replaceAll(".mp4", ""));
        holder.additionalText.setText(dataResourcesList.get(position).getCreated());
//        SimpleDateFormat format = new SimpleDateFormat("dd.MMM.yyyy hh:mm:ss");
//        try {
//            Date date = format.parse(dataResourcesList.get(position).getCreated());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return dataResourcesList.size();
    }


    public static class EventHolder extends RecyclerView.ViewHolder {

        TextView mainText, additionalText;

        public EventHolder(View itemView) {
            super(itemView);

            mainText = (TextView) itemView.findViewById(R.id.history_title_text);
            additionalText = (TextView) itemView.findViewById(R.id.history_synchronize_date_text);

        }
    }
}
