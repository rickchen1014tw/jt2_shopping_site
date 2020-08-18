package com.jt.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//當程序將json串轉化為對象時，如果遇到未知屬性則忽略(找不到setXxx方法時)
@JsonIgnoreProperties(ignoreUnknown = true)
class User {
	private Integer id;
	private String name;
	private Integer age;
	private String gender;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", gender=" + gender + "]";
	}
	
}