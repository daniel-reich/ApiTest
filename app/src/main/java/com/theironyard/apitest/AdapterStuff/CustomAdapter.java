package com.theironyard.apitest.AdapterStuff;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

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
    private ItemClickCallback itemDayChangeCallback;
    private ItemClickCallback itemPlusMinusRadioCallback;
    private ArrayList<Integer> expanded = new ArrayList<>();



    public interface ItemClickCallback{
        void onItemDeleteButtonClick(int p);
        void onSwitch(int p, boolean isChecked);
        void onCheck(int p, boolean isChecked);
        void onItemRingtoneButtonClick(int p);
        void onDayOfWeekButtonToggle(int p, int tag, boolean isChecked);
        void onPlusMinusRadioChange(int p, int tag, boolean isChecked);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback){
        this.itemClickDeleteButtonCallback = itemClickCallback;
        this.itemClickRingtoneButtonCallback = itemClickCallback;
        this.itemSwitchCallback = itemClickCallback;
        this.itemCheckCallback = itemClickCallback;
        this.itemDayChangeCallback = itemClickCallback;
        this.itemPlusMinusRadioCallback = itemClickCallback;
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
        holder.sunday.setChecked(station.isSunday());
        holder.monday.setChecked(station.isMonday());
        holder.tuesday.setChecked(station.isTuesday());
        holder.wednesday.setChecked(station.isWednesday());
        holder.thursday.setChecked(station.isThursday());
        holder.friday.setChecked(station.isFriday());
        holder.saturday.setChecked(station.isSaturday());
        holder.timeText.setText(formatTime(station.getHourOfDay(), station.getMinute()));


        if (station.getRadioChanged() == 15){
            holder.radio15.setChecked(true);
        } else if (station.getRadioChanged() == 30){
            holder.radio30.setChecked(true);
        } else {
            holder.radio60.setChecked(true);
        }

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
        private ToggleButton sunday;
        private ToggleButton monday;
        private ToggleButton tuesday;
        private ToggleButton wednesday;
        private ToggleButton thursday;
        private ToggleButton friday;
        private ToggleButton saturday;
        private TextView timeText;
        private RadioButton radio15;
        private RadioButton radio30;
        private RadioButton radio60;



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
            sunday = (ToggleButton) itemView.findViewById(R.id.sunday);
            monday = (ToggleButton) itemView.findViewById(R.id.monday);
            tuesday = (ToggleButton) itemView.findViewById(R.id.tuesday);
            wednesday = (ToggleButton) itemView.findViewById(R.id.wednesday);
            thursday = (ToggleButton) itemView.findViewById(R.id.thursday);
            friday = (ToggleButton) itemView.findViewById(R.id.friday);
            saturday = (ToggleButton) itemView.findViewById(R.id.saturday);
            timeText = (TextView) itemView.findViewById((R.id.timeText));
            radio15 = (RadioButton) itemView.findViewById(R.id.radio15);
            radio30 = (RadioButton) itemView.findViewById(R.id.radio30);
            radio60 = (RadioButton) itemView.findViewById(R.id.radio60);

            expandButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            ringtoneText.setOnClickListener(this);
            onSwitch.setOnCheckedChangeListener(this);
            vibrateCheckBox.setOnCheckedChangeListener(this);
            sunday.setOnCheckedChangeListener(this);
            monday.setOnCheckedChangeListener(this);
            tuesday.setOnCheckedChangeListener(this);
            wednesday.setOnCheckedChangeListener(this);
            thursday.setOnCheckedChangeListener(this);
            friday.setOnCheckedChangeListener(this);
            saturday.setOnCheckedChangeListener(this);
            radio15.setOnCheckedChangeListener(this);
            radio30.setOnCheckedChangeListener(this);
            radio60.setOnCheckedChangeListener(this);



            sunday.setTag(1);
            monday.setTag(2);
            tuesday.setTag(3);
            wednesday.setTag(4);
            thursday.setTag(5);
            friday.setTag(6);
            saturday.setTag(7);
            radio15.setTag(15);
            radio30.setTag(30);
            radio60.setTag(60);

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
            } else if (buttonView == radio15 || buttonView == radio30 || buttonView == radio60){
                itemPlusMinusRadioCallback.onPlusMinusRadioChange(getAdapterPosition(), (Integer)buttonView.getTag(), isChecked);
            } else {
                itemDayChangeCallback.onDayOfWeekButtonToggle(getAdapterPosition(), (Integer)buttonView.getTag(), isChecked);
            }


        }

    }

    public String formatTime(int hour, int minute){
        String time = "";
        String meridian = "am";
        if (hour == 0) {
            hour =12;
        }
        if (hour > 12) {
            hour = hour - 12;
            meridian = "pm";
        }
        if (minute < 10){
            time = time.concat(String.valueOf(hour)+":0"+minute+" "+meridian);
        } else {
            time = time.concat(String.valueOf(hour)+":"+minute+" "+meridian);
        }
        return time;
    }


}
