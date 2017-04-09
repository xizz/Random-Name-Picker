package com.xi_zz.randomnamepicker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.xi_zz.randomnamepicker.Util.KEY_PHOTO;
import static com.xi_zz.randomnamepicker.Util.sPeople;

public class PersonFragment extends Fragment {
	private static final int REQUEST_PICK_PHOTO = 1;
	private static final int REQUEST_READ_EXTERNAL = 11;
	@BindView(R2.id.name_text)
	EditText mNameText;
	@BindView(R2.id.photo_image)
	ImageView mPhotoImage;

	private Bitmap mBitmap;
	private Person mPerson;
	private Uri mPicUri;

	private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
	private FirebaseAuth mAuth = FirebaseAuth.getInstance();
	private DatabaseReference mPeopleRef = mDatabase.getReference(mAuth.getCurrentUser().getUid() + "/people");


	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Add a Person");
		if (getArguments() != null)
			mPerson = (Person) getArguments().getSerializable(Util.KEY_PERSON);
		if (savedInstanceState != null)
			mBitmap = savedInstanceState.getParcelable(KEY_PHOTO);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_person, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
		if (mBitmap != null)
			mPhotoImage.setImageBitmap(mBitmap);

		if (mPerson != null) {
			((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");
			mNameText.setText(mPerson.name);
			if (mPerson.photo != null)
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
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(Util.KEY_PHOTO, mBitmap);
		super.onSaveInstanceState(outState);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQUEST_PICK_PHOTO) {
			Toast.makeText(getContext(), "Picture Picked", Toast.LENGTH_SHORT).show();
			mPicUri = data.getData();
			if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
				requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
						REQUEST_READ_EXTERNAL);
			else
				requestCrop();
		} else if (requestCode == UCrop.REQUEST_CROP) {
			try {
				decodeUri(UCrop.getOutput(data));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
	                                       @NonNull final int[] grantResults) {
		if (requestCode == REQUEST_READ_EXTERNAL) {
			if (grantResults.length > 0
					&& permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
				requestCrop();
			else
				Toast.makeText(getContext(),
						"You must enable read external permission before picking a photo.",
						Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.pick_image_button)
	public void pickImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_PICK_PHOTO);
	}

	private void deletePerson() {
		sPeople.remove(mPerson);
		mPeopleRef.child(mPerson.id).removeValue();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String peopleString = Util.GSON.toJson(sPeople);
		preferences.edit().putString(Util.KEY_PEOPLE_STR, peopleString).apply();

	}

	private void editPerson() {
		Person temp = sPeople.get(mPerson);
		temp.name = mNameText.getText().toString();
		temp.photo = mBitmap == null ? temp.photo : Util.bitmapToByteString(mBitmap);

		mPeopleRef.child(mPerson.id).setValue(temp);
//		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//		String peopleString = Util.GSON.toJson(sPeople);
//		preferences.edit().putString(Util.KEY_PEOPLE_STR, peopleString).apply();
	}

	private void addPerson() {
		String name = mNameText.getText().toString();
		String imageStr = mBitmap == null ? null : Util.bitmapToByteString(mBitmap);
		Person person = new Person(UUID.randomUUID().toString(), name, imageStr);
		mPeopleRef.child(person.id).setValue(person);
//		sPeople.add(person);
//		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//		String peopleString = Util.GSON.toJson(sPeople);
//		preferences.edit().putString(Util.KEY_PEOPLE_STR, peopleString).apply();
	}

	private void requestCrop() {
		String filename = "temp.png";

		UCrop.Options options = new UCrop.Options();
		options.setActiveWidgetColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
		options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
		options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

		UCrop uCrop = UCrop.of(mPicUri, Uri.fromFile(new File(getContext().getCacheDir(), filename)));
		uCrop.withOptions(options);
		uCrop.withAspectRatio(1, 1);
		uCrop.start(getActivity(), this);
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
