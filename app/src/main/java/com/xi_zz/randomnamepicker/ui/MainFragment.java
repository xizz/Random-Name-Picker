package com.xi_zz.randomnamepicker.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xi_zz.randomnamepicker.R;
import com.xi_zz.randomnamepicker.R2;
import com.xi_zz.randomnamepicker.model.People;
import com.xi_zz.randomnamepicker.model.Person;
import com.xi_zz.randomnamepicker.tool.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xi_zz.randomnamepicker.tool.Util.DEFAULT_REF;
import static com.xi_zz.randomnamepicker.tool.Util.sPeople;
import static com.xi_zz.randomnamepicker.tool.Util.sValueEventListener;

public class MainFragment extends Fragment {

	@BindView(R2.id.name_text)
	TextView mNameView;
	@BindView(R2.id.image)
	ImageView mImageView;

	private ArrayList<Person> mCopy;
	private SharedPreferences mPreferences;
	private Person mCurrentPerson;
	private int mSize;

	private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	private FirebaseAuth mAuth = FirebaseAuth.getInstance();
	private DatabaseReference mPeopleRef = mDatabase.getReference(mAuth.getCurrentUser().getUid() + DEFAULT_REF);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		if (savedInstanceState != null) {
			mCurrentPerson = (Person) savedInstanceState.getSerializable(Util.KEY_PERSON);
			mCopy = (ArrayList<Person>) savedInstanceState.getSerializable(Util.KEY_PERSONS);
		} else {
			mCopy = new ArrayList<>(sPeople.peopleList);
			sPeople = new People();
		}
		mPeopleRef.addValueEventListener(sValueEventListener);
	}

	@Override
	public void onDestroy() {
		mPeopleRef.removeEventListener(sValueEventListener);
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(Util.KEY_PERSON, mCurrentPerson);
		outState.putSerializable(Util.KEY_PERSONS, mCopy);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
	                         @Nullable final Bundle savedInstanceState) {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (mCurrentPerson != null)
			setDisplay(mCurrentPerson);
	}

	@Override
	public void onResume() {
		super.onResume();
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		if (mSize != sPeople.size()) {
			mCopy = new ArrayList<>(sPeople.peopleList);
			mSize = sPeople.size();
		}
		if (mCurrentPerson != null && mCurrentPerson.photo != null)
			mImageView.setImageBitmap(Util.byteStringToBitmap(mCurrentPerson.photo));
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
			case R.id.sign_out:
				mNameView.setText(R.string.name);
				mImageView.setImageResource(R.drawable.ic_profile);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@OnClick(R2.id.next_name_button)
	public void displayRandomName() {
		if (sPeople.size() == 0) {
			Toast.makeText(getContext(), R.string.no_person, Toast.LENGTH_SHORT).show();
			return;
		}

		boolean callEveryone = mPreferences.getBoolean(getString(R.string.everyone_gets_called), false);
		if (!callEveryone) {
			int randomNum = (int) (Math.random() * sPeople.size());
			mCurrentPerson = sPeople.get(randomNum);
		} else {
			if (mCopy.isEmpty())
				mCopy.addAll(sPeople.peopleList);
			int randomNum = (int) (Math.random() * mCopy.size());
			mCurrentPerson = mCopy.remove(randomNum);
		}
		setDisplay(mCurrentPerson);
	}

	private void setDisplay(@NonNull Person person) {
		mNameView.setText(person.name);
		if (person.photo != null)
			mImageView.setImageBitmap(Util.byteStringToBitmap(person.photo));
		else
			mImageView.setImageResource(R.drawable.ic_profile);
	}
}
