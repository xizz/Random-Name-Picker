package com.xi_zz.randomnamepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

	private FirebaseAuth mAuth = FirebaseAuth.getInstance();
	private FirebaseAuth.AuthStateListener mAuthListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user == null)
					startActivity(new Intent(SplashActivity.this, LogInActivity.class));
				else
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
			}
		};
	}


	@Override
	protected void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		mAuth.removeAuthStateListener(mAuthListener);
	}
}
