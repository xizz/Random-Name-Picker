package com.xi_zz.randomnamepicker.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.xi_zz.randomnamepicker.model.People;
import com.xi_zz.randomnamepicker.model.Person;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Util {
	public static final String KEY_PEOPLE_STR = "people_string";
	public static final String KEY_PERSON = "person";
	public static final String KEY_PERSONS = "persons";
	public static final String KEY_PHOTO = "photo";

	public static People sPeople = new People(new ArrayList<Person>());
	public static ValueEventListener sValueEventListener;

	static {
		sValueEventListener = new ValueEventListener() {
			@Override
			public void onDataChange(final DataSnapshot dataSnapshot) {
				if (dataSnapshot.getValue(People.class) != null)
					sPeople = dataSnapshot.getValue(People.class);
			}

			@Override
			public void onCancelled(final DatabaseError databaseError) {}
		};
	}

	public static String bitmapToByteString(Bitmap bitmap) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
		byte[] byteArray = byteStream.toByteArray();
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}

	public static Bitmap byteStringToBitmap(String byteString) {
		byte[] imageAsByte = Base64.decode(byteString.getBytes(), Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
	}
}
