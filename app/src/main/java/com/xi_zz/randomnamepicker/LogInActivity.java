package com.xi_zz.randomnamepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
	private EditText mEmailEditText;
	private EditText mPasswordEditText;

	private FirebaseAuth mAuth = FirebaseAuth.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);

		mEmailEditText = (EditText) findViewById(R.id.edit_text_email);
		mPasswordEditText = (EditText) findViewById(R.id.edit_text_password);

	}

	public void logIn(View view) {
		String email = mEmailEditText.getText().toString();
		String password = mPasswordEditText.getText().toString();

		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (!task.isSuccessful()) {
							Toast.makeText(LogInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(LogInActivity.this, task.getResult().getUser().getEmail() + R.string.login_sucess,
									Toast.LENGTH_SHORT).show();
							startActivity(new Intent(LogInActivity.this, SplashActivity.class));
							finish();
						}

					}
				});
	}

	public void signUp(View view) {
		String email = mEmailEditText.getText().toString();
		String password = mPasswordEditText.getText().toString();

		mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (!task.isSuccessful()) {
							Toast.makeText(LogInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(LogInActivity.this, task.getResult().getUser().getEmail() + R.string.signup_sucess,
									Toast.LENGTH_SHORT).show();
							startActivity(new Intent(LogInActivity.this, SplashActivity.class));
							finish();
						}

					}
				});
	}

}
