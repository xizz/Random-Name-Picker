package com.xi_zz.randomnamepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

	private FirebaseAuth mAuth = FirebaseAuth.getInstance();

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null)
			getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu_log_out, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sign_out:
				mAuth.signOut();
				Util.sPeople.clear();
				PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
				startActivity(new Intent(this, SplashActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
