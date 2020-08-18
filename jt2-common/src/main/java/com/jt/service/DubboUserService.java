package com.jt.service;

import com.jt.pojo.User;

public interface DubboUserService {
	//Dubbo接口方法實現用戶新增
	void saveUser(User user);

	String findUserByUP(User user);

}
