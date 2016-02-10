package com.mieczkowskidev.audalize.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mieczkowskidev.audalize.R;
import com.mieczkowskidev.audalize.model.MediaFile;

import java.util.List;

/**
 * Created by Patryk Mieczkowski on 06.02.16
 */
public class AllFilesListAdapter extends RecyclerView.Adapter<AllFilesListAdapter.EventHolder> {

    private Context context;
    private List<MediaFile> list;

    public AllFilesListAdapter(Context context, List<MediaFile> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AllFilesListAdapter.EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_files_list_item, parent, false);
        return new EventHolder(v);
    }

    @Override
    public void onBindViewHolder(AllFilesListAdapter.EventHolder holder, int position) {

        holder.mainText.setText(list.get(position).getTitle().replaceAll(".mp4", ""));
        holder.additionalText.setText(list.get(position).getPath());
        holder.checkBox.setChecked(list.get(position).isChecked());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class EventHolder extends RecyclerView.ViewHolder {

        TextView mainText, additionalText;
        CheckBox checkBox;
        RelativeLayout mainLayout;

        public EventHolder(View itemView) {
            super(itemView);

            mainText = (TextView) itemView.findViewById(R.id.main_text);
            additionalText = (TextView) itemView.findViewById(R.id.additional_text);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
        }
    }
}
