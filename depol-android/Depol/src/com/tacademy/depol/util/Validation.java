package com.tacademy.depol.util;

public class Validation {
	
	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
}
