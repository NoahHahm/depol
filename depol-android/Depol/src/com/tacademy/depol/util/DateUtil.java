package com.tacademy.depol.util;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

public class DateUtil {


	private static final int SECOND_MILLIS = 1000;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

	
	public static void disableDayField(DatePickerDialog datePickerDialog) {
		try {

			Field[] f = datePickerDialog.getClass().getDeclaredFields();
			for (Field dateField : f) {
				if (dateField.getName().equals("mDatePicker")) {
					dateField.setAccessible(true);

					DatePicker datePicker = (DatePicker) dateField
							.get(datePickerDialog);

					Field datePickerFields[] = dateField.getType()
							.getDeclaredFields();

					for (Field datePickerField : datePickerFields) {
						if ("mDayPicker".equals(datePickerField.getName())
								|| "mDaySpinner".equals(datePickerField
										.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							try {
								((View) dayPicker).setVisibility(View.GONE);
							} catch (ClassCastException e) {
								e.printStackTrace();
							}

						}						
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void disableMonthDayField(DatePickerDialog datePickerDialog) {
		try {

			Field[] f = datePickerDialog.getClass().getDeclaredFields();
			for (Field dateField : f) {
				if (dateField.getName().equals("mDatePicker")) {
					dateField.setAccessible(true);

					DatePicker datePicker = (DatePicker) dateField
							.get(datePickerDialog);

					Field datePickerFields[] = dateField.getType()
							.getDeclaredFields();

					for (Field datePickerField : datePickerFields) {
						if ("mDayPicker".equals(datePickerField.getName())
								|| "mDaySpinner".equals(datePickerField
										.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							try {
								((View) dayPicker).setVisibility(View.GONE);
							} catch (ClassCastException e) {
								e.printStackTrace();
							}

						}
						
						if ("mMonthPicker".equals(datePickerField.getName())
								|| "mMonthSpinner".equals(datePickerField
										.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							try {
								((View) dayPicker).setVisibility(View.GONE);
							} catch (ClassCastException e) {
								e.printStackTrace();
							}

						}
						
						
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String convertYYYYMM(int year, int month) {
		return String.format("%d.%02d", year, month);
	}
	
	public static String convertYYYY(int year) {
		return String.format("%d", year);
	}
	

	public static String getTimeAgo(long time, Context ctx) {
	    if (time < 1000000000000L) {
	        // if timestamp given in seconds, convert to millis
	        time *= 1000;
	    }
	    Calendar c = Calendar.getInstance(Locale.KOREAN); 
	    //long now = getCurrentTime(ctx);
	    long now = c.getTimeInMillis();
	    if (time > now || time <= 0) {
	        return null;
	    }

	    // TODO: localize
	    final long diff = now - time;
	    if (diff < MINUTE_MILLIS) {
	        return "0 minutes ago";
	    } else if (diff < 2 * MINUTE_MILLIS) {
	        return "a minute ago";
	    } else if (diff < 50 * MINUTE_MILLIS) {
	        return diff / MINUTE_MILLIS + " minutes ago";
	    } else if (diff < 90 * MINUTE_MILLIS) {
	        return "an hour ago";
	    } else if (diff < 24 * HOUR_MILLIS) {
	        return diff / HOUR_MILLIS + " hours ago";
	    } else if (diff < 48 * HOUR_MILLIS) {
	        return "yesterday";
	    } else {
	        return diff / DAY_MILLIS + " days ago";
	    }
	}
	
}
