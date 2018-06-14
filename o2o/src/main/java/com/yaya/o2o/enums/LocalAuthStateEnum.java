package com.yaya.o2o.enums;

public enum LocalAuthStateEnum {

	SUCCESS(1, "操作成功");

	private int state;

	private String stateInfo;

	private LocalAuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static LocalAuthStateEnum stateOf(int state) {
		for (LocalAuthStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

}
