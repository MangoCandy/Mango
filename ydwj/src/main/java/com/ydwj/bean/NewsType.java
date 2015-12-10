package com.ydwj.bean;

import java.util.List;

public class NewsType {
	int typeId;
	String typeName;
	List<Sontype> sontypes;
	int point;
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public List<Sontype> getSontypes() {
		return sontypes;
	}
	public void setSontypes(List<Sontype> sontypes) {
		this.sontypes = sontypes;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
