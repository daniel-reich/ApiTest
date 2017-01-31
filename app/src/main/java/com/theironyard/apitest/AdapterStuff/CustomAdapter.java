package com.theironyard.apitest.AdapterStuff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.theironyard.apitest.Entities.Station;
import com.theironyard.apitest.R;
import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> {

    private ArrayList<Station> stations;
    private LayoutInflater inflator;
    private ItemClickCallback itemClickDeleteButtonCallback;
    private ItemClickCallback itemClickRingtoneButtonCallback;
    private ItemClickCallback itemSwitchCallback;
    private ItemClickCallback itemCheckCallback;
    private ArrayList<Integer> expanded = new ArrayList<>();



    public interface ItemClickCallback{
        void onItemDeleteButtonClick(int p);
        void onSwitch(int p, boolean isChecked);
        void onCheck(int p, boolean isChecked);
        void onItemRingtoneButtonClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback){
        this.itemClickDeleteButtonCallback = itemClickCallback;
        this.itemClickRingtoneButtonCallback = itemClickCallback;
        this.itemSwitchCallback = itemClickCallback;
        this.itemCheckCallback = itemClickCallback;
    }

    public CustomAdapter (ArrayList<Station> stations, Context context){
        this.inflator = LayoutInflater.from(context);
        this.stations = stations;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_row, parent, false);

        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        Station station = stations.get(position);
        holder.nameText.setText(station.getStationName());
        holder.addressText.setText(station.getStationAddress());
        holder.onSwitch.setChecked(station.isChecked());
        holder.lineText.setText(station.getStationLine() + "line");
        holder.ringtoneText.setText(station.getRingtoneName());
        holder.vibrateCheckBox.setChecked(station.isVibrate());

        if (expanded.contains(position)) {
            holder.expandableView.setVisibility(View.VISIBLE);

        } else {
            holder.expandableView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    class CustomHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private TextView nameText;
        private TextView addressText;
        private ImageButton deleteButton;
        private Switch onSwitch;
        private View container;
        private RelativeLayout expandableView;
        private ImageButton expandButton;
        private TextView lineText;
        private TextView ringtoneText;
        private CheckBox vibrateCheckBox;



        public CustomHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.nameText);
            addressText = (TextView) itemView.findViewById(R.id.addressText);
            lineText = (TextView) itemView.findViewById(R.id.lineText);
            ringtoneText = (TextView) itemView.findViewById(R.id.ringtoneText);
            deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);
            onSwitch = (Switch) itemView.findViewById(R.id.onSwitch);
            container = itemView.findViewById(R.id.custom_row_layout);
            expandableView = (RelativeLayout) itemView.findViewById(R.id.expandable_view);
            expandButton = (ImageButton) itemView.findViewById(R.id.expandButton);
            vibrateCheckBox = (CheckBox) itemView.findViewById(R.id.vibrateCheckBox);

            expandButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            ringtoneText.setOnClickListener(this);
            onSwitch.setOnCheckedChangeListener(this);
            vibrateCheckBox.setOnCheckedChangeListener(this);


        }

        @Override
        public void onClick(View v) {
            if (v == deleteButton) {
                expanded.remove(expanded.indexOf(getAdapterPosition()));
                itemClickDeleteButtonCallback.onItemDeleteButtonClick(getAdapterPosition());
            } else if (v == expandButton) {

                if (expanded.contains(getAdapterPosition())){
                    expanded.remove(expanded.indexOf(getAdapterPosition()));
                    notifyItemChanged(getAdapterPosition());
                } else if (!expanded.contains(getAdapterPosition())){
                    expanded.add(getAdapterPosition());
                    notifyItemChanged(getAdapterPosition());
                }
            } else if (v == ringtoneText){
                itemClickRingtoneButtonCallback.onItemRingtoneButtonClick(getAdapterPosition());
            }

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView == onSwitch){
                itemSwitchCallback.onSwitch(getAdapterPosition(), isChecked);
            } else if (buttonView == vibrateCheckBox){
                itemCheckCallback.onCheck(getAdapterPosition(), isChecked);
            }


        }
    }

}
