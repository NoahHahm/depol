package com.tacademy.depol.data;

import java.io.Serializable;

public class AbilityInfo implements Serializable {
	public int pk;
	public String program;
	public int level;
	
	public AbilityInfo(String string) {
		this.program = string;
	}
}
