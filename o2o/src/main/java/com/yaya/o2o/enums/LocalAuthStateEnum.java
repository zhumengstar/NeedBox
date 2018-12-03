package com.yaya.o2o.enums;

public enum LocalAuthStateEnum {

	LOGINFAIL(-1, "密码或帐号输入有误"),
	SUCCESS(0, "操作成功"),
	ONLY_ONE_LOCALAUTH(-1005,"该用户名已经被注册了哦~"),
	NULL_AUTH_INFO(-1006, "信息不能为空哦~"),
	ONLY_ONE_ACCOUNT(-1007,"您已经有本地帐号了");

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
