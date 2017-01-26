package com.xi_zz.randomnamepicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Util {
	public static final String KEY_PEOPLE_STR = "people_string";
	public static final String KEY_PERSON = "person";
	public static final Gson GSON = new Gson();

	public static People sPeople = new People(new ArrayList<Person>());

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
