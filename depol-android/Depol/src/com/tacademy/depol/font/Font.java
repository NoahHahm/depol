package com.tacademy.depol.font;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

public class Font {
	
	private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();


	public final static String NANUM_BARUN_GOTHIC_BOLD = "fonts/NanumBarunGothicBold.ttf";
	public final static String NANUM_BARUN_GOTHIC = "fonts/NanumBarunGothic.ttf";
	public final static String ROBOTO_LITGT = "fonts/Roboto-Light.ttf";
	public final static String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
	public final static String ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
	public final static String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
	public final static String ROBOTO_THIN = "fonts/Roboto-Thin.ttf";
	
    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

}