package com.xi_zz.randomnamepicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragment extends Fragment {

	@BindView(R2.id.name_text)
	TextView mNameView;
	@BindView(R2.id.image)
	ImageView mImageView;

	private List<Person> mNames;
	private List<Person> mCopy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);

		mNames = new ArrayList<>();

		mNames.add(new Person("Anna Xinrui Lu", R.drawable.anna));
		mNames.add(new Person("Benjamin Antell", R.drawable.ben));
		mNames.add(new Person("Sylvia Chengcheng Wang", 0));
		mNames.add(new Person("ConsuelaImani Esseboom", R.drawable.consuela));
		mNames.add(new Person("Dadriaunna Williams", 0));
		mNames.add(new Person("Elyse Warren", 0));
		mNames.add(new Person("Emma Lunder", 0));
		mNames.add(new Person("Ginger Jiajing Chen", 0));
		mNames.add(new Person("Jianing Ge", 0));
		mNames.add(new Person("Jingshan Wang", R.drawable.jingshan));
		mNames.add(new Person("Cathy Jingyuan Liao", R.drawable.cathy));
		mNames.add(new Person("John Park", R.drawable.john));
		mNames.add(new Person("Jamie Julliene Gatchalian", 0));
		mNames.add(new Person("Katie Behrmann", R.drawable.katie));
		mNames.add(new Person("Lindsay Baer", 0));
		mNames.add(new Person("Maureen Mengqi Ding", R.drawable.maureen));
		mNames.add(new Person("Michael Tarnow", 0));
		mNames.add(new Person("Ningel Bhuta", R.drawable.ningel));
		mNames.add(new Person("Rachel Miller", R.drawable.rachel));
		mNames.add(new Person("Rocio Elena Conde-Fuentes", R.drawable.rocio));
		mNames.add(new Person("Steve Zhao", R.drawable.steve));
		mNames.add(new Person("Iris Wang Xi", R.drawable.iris));
		mNames.add(new Person("Wanshu Wang", 0));
		mNames.add(new Person("Elsa YungYung Lee", R.drawable.elsa));
		mNames.add(new Person("Vivienne Yuzhou Wang", R.drawable.vivienne));
		mNames.add(new Person("Sean Zhenzhen Ma", R.drawable.sean));

		mCopy = new ArrayList<>(mNames);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
	                         @Nullable final Bundle savedInstanceState) {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				getFragmentManager()
						.beginTransaction()
						.addToBackStack(null)
						.replace(R.id.container, new SettingsFragment())
						.commit();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@OnClick(R2.id.next_name_button)
	public void displayRandomName() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		boolean callEveryone = preferences.getBoolean(getString(R.string.everyone_gets_called), false);
		if (!callEveryone) {
			int randomNum = (int) (Math.random() * mNames.size());
			setDisplay(mNames.get(randomNum));
		} else {
			if (mCopy.isEmpty())
				mCopy.addAll(mNames);

			int randomNum = (int) (Math.random() * mCopy.size());
			setDisplay(mCopy.remove(randomNum));
		}
	}

	private void setDisplay(Person person) {
		mNameView.setText(person.name);
		if (person.image != 0) {
			mImageView.setImageResource(person.image);
		} else {
			mImageView.setImageResource(R.mipmap.ic_launcher);
		}
	}
}
