package com.xi_zz.randomnamepicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PeopleViewHolder extends RecyclerView.ViewHolder {

	@BindView(R2.id.card_view)
	CardView mCardView;
	@BindView(R2.id.person_photo)
	ImageView mImageView;
	@BindView(R2.id.person_name)
	TextView mTextView;

	private AppCompatActivity mActivity;

	public PeopleViewHolder(View itemView, AppCompatActivity activity) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		mActivity = activity;
	}

	public void bind(Person person) {
		mTextView.setText(person.name);
		if (!TextUtils.isEmpty(person.photo))
			mImageView.setImageBitmap(byteStringToBitmap(person.photo));
	}

	@OnClick(R.id.card_view)
	public void itemClicked() {
		mActivity.getSupportFragmentManager()
				.beginTransaction()
				.addToBackStack(null)
				.replace(R.id.container, new PersonFragment())
				.commit();
	}

	private Bitmap byteStringToBitmap(String byteString) {
		byte[] imageAsByte = Base64.decode(byteString.getBytes(), Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
	}
}

