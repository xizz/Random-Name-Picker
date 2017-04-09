package com.xi_zz.randomnamepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
	private FirebaseDatabase database = FirebaseDatabase.getInstance();
	private FirebaseAuth auth = FirebaseAuth.getInstance();
	private FirebaseAuth.AuthStateListener authListener;

	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		authListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user == null)
					startActivity(new Intent(MainActivity.this, LogInActivity.class));
				else {
					if (savedInstanceState == null)
						getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
				}
			}
		};
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
				auth.signOut();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		auth.addAuthStateListener(authListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		auth.removeAuthStateListener(authListener);
	}
}
