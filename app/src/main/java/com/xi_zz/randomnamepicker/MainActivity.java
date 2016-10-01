package com.xi_zz.randomnamepicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private TextView mNameView;
	private List<String> mNames;
	private List<String> mCopy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNameView = (TextView) findViewById(R.id.name_text);
		mNames = new ArrayList<>();

		mNames.add("Anna Xinrui Lu");
		mNames.add("Benjamin Antell");
		mNames.add("Cheng Cheng Wang");
		mNames.add("ConsuelaImani Esseboom");
		mNames.add("Dadriaunna Williams");
		mNames.add("Elyse Warren");
		mNames.add("Emma Lunder");
		mNames.add("Jiajing Chen");
		mNames.add("Jianing Ge");
		mNames.add("Jingshan Wang");
		mNames.add("Jingyuan Liao");
		mNames.add("John Park");
		mNames.add("Julliene Gatchalian");
		mNames.add("Katie Behrmann");
		mNames.add("Lindsay Baer");
		mNames.add("Mengqi Ding");
		mNames.add("Michael Tarnow");
		mNames.add("Ningel Bhuta");
		mNames.add("Rachel Miller");
		mNames.add("RocioElena Conde-Fuentes");
		mNames.add("Steve Zhao");
		mNames.add("Wang Xi");
		mNames.add("Wanshu Wang");
		mNames.add("YungYungElsa Lee");
		mNames.add("Yuzhou Wang");
		mNames.add("Zhenzhen Ma");

		mCopy = new ArrayList<>(mNames);

		Toast.makeText(this, "" + mNames.size(), Toast.LENGTH_SHORT).show();
	}

	public void displayRandomName(View view) {
		if (mCopy.isEmpty())
			mCopy.addAll(mNames);

		int randomNum = (int) (Math.random() * mCopy.size());
		String newName = mCopy.remove(randomNum);

		mNameView.setText(newName);
	}
}
