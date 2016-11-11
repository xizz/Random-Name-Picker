package com.xi_zz.randomnamepicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class PersonFragment extends Fragment {
	private static final int REQUEST_PICK_PHOTO = 1;
	@BindView(R2.id.name_text)
	EditText mNameText;
	@BindView(R2.id.photo_image)
	ImageView mPhotoImage;

	private Bitmap mBitmap;

	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add a Person");
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_person, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.menu_person, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				getActivity().onBackPressed();
				return true;
			case R.id.save:
				if (TextUtils.isEmpty(mNameText.getText())) {
					Toast.makeText(getContext(), "You must enter a name.", Toast.LENGTH_SHORT).show();
				} else {
					addPerson();
					getActivity().onBackPressed();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void addPerson() {
		String name = mNameText.getText().toString();

		String imageStr = mBitmap == null ? null : Util.bitmapToByteString(mBitmap);
		Person person = new Person(name, imageStr);
		MainFragment.sNames.add(person);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		People people = new People();
		people.peopleList = MainFragment.sNames;
		String peopleString = Util.GSON.toJson(people);
		preferences.edit().putString(Util.KEY_PEOPLE, peopleString).apply();

	}


	@OnClick(R.id.pick_image_button)
	public void pickImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_PICK_PHOTO);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		Toast.makeText(getContext(), "Picture Picked", Toast.LENGTH_SHORT).show();
		if (requestCode == REQUEST_PICK_PHOTO) {
			try {
				decodeUri(data.getData());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void decodeUri(Uri uri) throws FileNotFoundException {
		// Get the dimensions of the View
		double targetW = 400;
		double targetH = 600;

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();

		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri), null, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = (int) Math.ceil(Math.min(photoW / targetW, photoH / targetH));

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;

		mBitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri), null, bmOptions);

		mPhotoImage.setImageBitmap(mBitmap);
	}
}
