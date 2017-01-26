package com.xi_zz.randomnamepicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xi_zz.randomnamepicker.Util.sPeople;

public class MainFragment extends Fragment {

	@BindView(R2.id.name_text)
	TextView mNameView;
	@BindView(R2.id.image)
	ImageView mImageView;

	private List<Person> mCopy;
	private SharedPreferences mPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);

		mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

//		sPeople.add(new Person("Anna Xinrui Lu", R.drawable.anna));
//		sPeople.add(new Person("Benjamin Antell", R.drawable.ben));
//		sPeople.add(new Person("Sylvia Chengcheng Wang", 0));
//		sPeople.add(new Person("ConsuelaImani Esseboom", R.drawable.consuela));
//		sPeople.add(new Person("Dadriaunna Williams", 0));
//		sPeople.add(new Person("Elyse Warren", 0));
//		sPeople.add(new Person("Emma Lunder", 0));
//		sPeople.add(new Person("Ginger Jiajing Chen", 0));
//		sPeople.add(new Person("Jianing Ge", 0));
//		sPeople.add(new Person("Jingshan Wang", R.drawable.jingshan));
//		sPeople.add(new Person("Cathy Jingyuan Liao", R.drawable.cathy));
//		sPeople.add(new Person("John Park", R.drawable.john));
//		sPeople.add(new Person("Jamie Julliene Gatchalian", 0));
//		sPeople.add(new Person("Katie Behrmann", R.drawable.katie));
//		sPeople.add(new Person("Lindsay Baer", 0));
//		sPeople.add(new Person("Maureen Mengqi Ding", R.drawable.maureen));
//		sPeople.add(new Person("Michael Tarnow", 0));
//		sPeople.add(new Person("Ningel Bhuta", R.drawable.ningel));
//		sPeople.add(new Person("Rachel Miller", R.drawable.rachel));
//		sPeople.add(new Person("Rocio Elena Conde-Fuentes", R.drawable.rocio));
//		sPeople.add(new Person("Steve Zhao", R.drawable.steve));
//		sPeople.add(new Person("Iris Wang Xi", R.drawable.iris));
//		sPeople.add(new Person("Wanshu Wang", 0));
//		sPeople.add(new Person("Elsa YungYung Lee", R.drawable.elsa));
//		sPeople.add(new Person("Vivienne Yuzhou Wang", R.drawable.vivienne));
//		sPeople.add(new Person("Sean Zhenzhen Ma", R.drawable.sean));

		mCopy = new ArrayList<>(sPeople.peopleList);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
	                         @Nullable final Bundle savedInstanceState) {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
		String peopleString = mPreferences.getString(Util.KEY_PEOPLE_STR, null);
		if (TextUtils.isEmpty(peopleString))
			return;
		sPeople = Util.GSON.fromJson(peopleString, People.class);

	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add:
				getFragmentManager()
						.beginTransaction()
						.addToBackStack(null)
						.replace(R.id.container, new PersonFragment())
						.commit();
				return true;
			case R.id.settings:
				getFragmentManager()
						.beginTransaction()
						.addToBackStack(null)
						.replace(R.id.container, new SettingsFragment())
						.commit();
				return true;
			case R.id.list:
				getFragmentManager()
						.beginTransaction()
						.addToBackStack(null)
						.replace(R.id.container, new PeopleListFragment())
						.commit();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@OnClick(R2.id.next_name_button)
	public void displayRandomName() {
		if (sPeople.size() == 0) {
			Toast.makeText(getContext(), "You don't have anyone on file.", Toast.LENGTH_SHORT).show();
			return;
		}

		boolean callEveryone = mPreferences.getBoolean(getString(R.string.everyone_gets_called), false);
		if (!callEveryone) {
			int randomNum = (int) (Math.random() * sPeople.size());
			setDisplay(sPeople.get(randomNum));
		} else {
			if (mCopy.isEmpty())
				mCopy.addAll(sPeople.peopleList);

			int randomNum = (int) (Math.random() * mCopy.size());
			setDisplay(mCopy.remove(randomNum));
		}
	}

	private void setDisplay(Person person) {
		mNameView.setText(person.name);
		if (person.image != 0) {
			mImageView.setImageResource(person.image);
		} else if (person.photo != null) {
			mImageView.setImageBitmap(Util.byteStringToBitmap(person.photo));
		} else {
			mImageView.setImageResource(R.drawable.ic_profile);
		}
	}
}
