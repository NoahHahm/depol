package com.tacademy.depol.util;

import com.tacademy.depol.ApplicationContext;
import com.tacademy.depol.R;

public class ProfileUtil {
	
	public final static int NORMAL = 1;
	public final static int JOB_JUNTER = 2;
	public final static int FREELANCER = 3;
	public final static int PARTTIME = 4;
	
	public static String convertStatus(int status) {
		switch(status) {
		case NORMAL: 
			return ApplicationContext.getContext().getResources().getString(R.string.job_normal);
		case JOB_JUNTER:
			return ApplicationContext.getContext().getResources().getString(R.string.job_hunter);
		case FREELANCER:
			return ApplicationContext.getContext().getResources().getString(R.string.job_free);
		case PARTTIME:
			return ApplicationContext.getContext().getResources().getString(R.string.job_parttime);
		}
		return "";
	}
}
