package com.grst.hotelapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grst.hotelapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PopAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> spinnerValue;
	private Context context;

	private String reportStatus;

	public PopAdapter(Context con, ArrayList<HashMap<String, String>> spinnerValue, String reportStatus) {
		this.context = con;
		this.spinnerValue = spinnerValue;
		this.reportStatus = reportStatus;
	}

	@Override
	public int getCount() {
		return spinnerValue.size();
	}

	@Override
	public Object getItem(int position) {
		return spinnerValue.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.pomenu_item, null);
			holder = new ViewHolder();

			convertView.setTag(holder);

			holder.spinnerName = (TextView) convertView
					.findViewById(R.id.spinner_name);
			holder.spinnerID = (TextView) convertView
					.findViewById(R.id.spinner_id);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, String> t = spinnerValue.get(position);

		if(null != reportStatus && reportStatus.equals(t.get("id"))){
			holder.spinnerName.setBackgroundColor(context.getResources().getColor(R.color.base_color));
		}

		holder.spinnerName.setText(t.get("name"));
		holder.spinnerID.setText(t.get("id"));

		return convertView;
	}

	private final class ViewHolder {
		TextView spinnerName;
		TextView spinnerID;
	}
}
