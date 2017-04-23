package com.xi_zz.randomnamepicker.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xi_zz.randomnamepicker.R;
import com.xi_zz.randomnamepicker.model.Person;

import static com.xi_zz.randomnamepicker.tool.Util.sPeople;


public class PeopleAdapter extends RecyclerView.Adapter<PeopleViewHolder> {

	private AppCompatActivity mActivity;

	public PeopleAdapter(AppCompatActivity activity) {
		mActivity = activity;
	}

	@Override
	public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_person, parent, false);
		return new PeopleViewHolder(view, mActivity);
	}

	@Override
	public void onBindViewHolder(PeopleViewHolder holder, int position) {
		Person person = sPeople.get(position);
		holder.bind(person);
	}

	@Override
	public int getItemCount() {
		return sPeople.size();
	}
}

