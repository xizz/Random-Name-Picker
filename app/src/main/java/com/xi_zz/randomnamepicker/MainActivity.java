package com.xi_zz.randomnamepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private TextView mNameView;
	private ImageView mImageView;
	private List<Person> mNames;
	private List<Person> mCopy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNameView = (TextView) findViewById(R.id.name_text);
		mImageView = (ImageView) findViewById(R.id.image);
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

//		Toast.makeText(this, "" + mNames.size(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void displayRandomName(View view) {
		if (mCopy.isEmpty())
			mCopy.addAll(mNames);

		int randomNum = (int) (Math.random() * mCopy.size());
		Person person = mCopy.remove(randomNum);

		mNameView.setText(person.name);
		if (person.image != 0) {
			mImageView.setImageResource(person.image);
		} else {
			mImageView.setImageResource(R.mipmap.ic_launcher);
		}
	}
}
