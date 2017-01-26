package com.xi_zz.randomnamepicker;

import android.content.ActivityNotFoundException;
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
import static com.xi_zz.randomnamepicker.Util.sPeople;

public class PersonFragment extends Fragment {
	private static final int REQUEST_PICK_PHOTO = 1;
	private static final int REQUEST_CROP_PHOTO = 2;
	@BindView(R2.id.name_text)
	EditText mNameText;
	@BindView(R2.id.photo_image)
	ImageView mPhotoImage;

	private Bitmap mBitmap;
	private Person mPerson;


	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add a Person");
		if (getArguments() != null)
			mPerson = (Person) getArguments().getSerializable(Util.KEY_PERSON);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_person, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		if (mPerson != null) {
			((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");
			mNameText.setText(mPerson.name);
			mPhotoImage.setImageBitmap(Util.byteStringToBitmap(mPerson.photo));
		}
	}

	@Override
	public void onPrepareOptionsMenu(final Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.delete).setVisible(mPerson != null);
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
					if (mPerson != null)
						editPerson();
					else
						addPerson();
					getActivity().onBackPressed();
				}
				return true;
			case R.id.delete:
				deletePerson();
				getActivity().onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void deletePerson() {
		sPeople.peopleList.remove(mPerson);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String peopleString = Util.GSON.toJson(sPeople);
		preferences.edit().putString(Util.KEY_PEOPLE_STR, peopleString).apply();

	}

	private void editPerson() {
		Person temp = sPeople.get(mPerson);
		temp.name = mNameText.getText().toString();
		temp.photo = mBitmap == null ? temp.photo : Util.bitmapToByteString(mBitmap);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String peopleString = Util.GSON.toJson(sPeople);
		preferences.edit().putString(Util.KEY_PEOPLE_STR, peopleString).apply();
	}

	private void addPerson() {
		String name = mNameText.getText().toString();

		String imageStr = mBitmap == null ? null : Util.bitmapToByteString(mBitmap);
		Person person = new Person(name, imageStr);
		sPeople.add(person);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String peopleString = Util.GSON.toJson(sPeople);
		preferences.edit().putString(Util.KEY_PEOPLE_STR, peopleString).apply();

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
			performCrop(data.getData());
		} else if (requestCode == REQUEST_CROP_PHOTO) {
			try {
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				mBitmap = extras.getParcelable("data");
				mPhotoImage.setImageBitmap(mBitmap);
//				decodeUri(data.getData());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void performCrop(Uri picUri) {
		try {
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties here
			cropIntent.putExtra("crop", true);
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 1024);
			cropIntent.putExtra("outputY", 1024);
			// retrieve data on return
			cropIntent.putExtra("scale", true);
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, REQUEST_CROP_PHOTO);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
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
