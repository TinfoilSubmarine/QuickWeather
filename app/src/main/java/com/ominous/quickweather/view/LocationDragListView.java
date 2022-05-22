/*
 *     Copyright 2019 - 2022 Tyler Williamson
 *
 *     This file is part of QuickWeather.
 *
 *     QuickWeather is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     QuickWeather is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with QuickWeather.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ominous.quickweather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ominous.quickweather.R;
import com.ominous.quickweather.data.WeatherDatabase;
import com.ominous.quickweather.dialog.LocationDialog;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LocationDragListView extends DragListView {
    private LocationDragAdapter adapter;

    public LocationDragListView(Context context) {
        this(context, null, 0);
    }

    public LocationDragListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocationDragListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TextView textView = new TextView(context, null, R.style.current_weather_text);
        textView.setText(context.getString(R.string.text_no_location));
        textView.setId(R.id.text_no_location);

        DragListView.LayoutParams layoutParams = new DragListView.LayoutParams(DragListView.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        this.addView(textView, layoutParams);
    }

    public void setAdapterFromList(List<WeatherDatabase.WeatherLocation> locationList) {
        adapter = new LocationDragAdapter(locationList);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                setNoLocationTextVisibility();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                setNoLocationTextVisibility();
            }
        });

        setNoLocationTextVisibility();

        this.setAdapter(adapter, false);
        this.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setNoLocationTextVisibility() {
        this.findViewById(R.id.text_no_location).setVisibility((adapter.getItemCount() == 0) ? View.VISIBLE : View.INVISIBLE);
    }

    public void addLocation(WeatherDatabase.WeatherLocation location) {
        int position = adapter.getItemCount();

        adapter.addItem(position, location);
        adapter.notifyItemInserted(position);
    }

    public List<WeatherDatabase.WeatherLocation> getItemList() {
        return adapter == null ? new ArrayList<>() : adapter.getItemList();
    }

    private class LocationDragAdapter extends DragItemAdapter<WeatherDatabase.WeatherLocation, LocationViewHolder> {
        LocationDragAdapter(List<WeatherDatabase.WeatherLocation> locationList) {
            super();

            setItemList(locationList);
        }

        @Override
        public long getUniqueItemId(int position) {
            return mItemList.get(position).hashCode();
        }

        @NonNull
        @Override
        public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new LocationViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_location, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(final @NonNull LocationViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder, position);

            viewHolder.locationTextView.setText(mItemList.get(position).name);
            viewHolder.buttonEdit.setVisibility(mItemList.get(position).isCurrentLocation ? GONE : VISIBLE);
        }
    }

    private class LocationViewHolder extends DragItemAdapter.ViewHolder implements View.OnClickListener {
        final TextView locationTextView;
        final ImageView buttonClear;
        final ImageView buttonEdit;
        final LocationDialog locationDialog = new LocationDialog(getContext(), new LocationDialog.OnLocationChosenListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onLocationChosen(String location, double latitude, double name) {
                int position = getAdapterPosition();

                WeatherDatabase.WeatherLocation weatherLocation = (WeatherDatabase.WeatherLocation) getAdapter().getItemList().get(position);

                getAdapter().getItemList().set(position,
                        new WeatherDatabase.WeatherLocation(
                                weatherLocation.id,
                                latitude,
                                name,
                                location,
                                weatherLocation.isSelected,
                                weatherLocation.isCurrentLocation,
                                weatherLocation.order));
                getAdapter().notifyItemChanged(position);
            }

            @Override
            public void onGeoCoderError(Throwable throwable) {

            }
        });

        LocationViewHolder(View itemView) {
            super(itemView, R.id.button_drag, false);

            locationTextView = itemView.findViewById(R.id.textview_location);
            buttonClear = itemView.findViewById(R.id.button_clear);
            buttonEdit = itemView.findViewById(R.id.button_edit);

            buttonClear.setOnClickListener(this);
            buttonEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (v.getId() == R.id.button_clear) {
                getAdapter().removeItem(position);
                getAdapter().notifyItemRemoved(position);
                getAdapter().notifyItemRangeChanged(position, getAdapter().getItemCount());//Android bug: need to explicitly tell the adapter
            } else {
                locationDialog.showEditDialog((WeatherDatabase.WeatherLocation) getAdapter().getItemList().get(position));
            }
        }
    }
}
