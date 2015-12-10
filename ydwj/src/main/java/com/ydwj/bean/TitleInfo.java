package com.ydwj.bean;

import android.graphics.Bitmap;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;

public class TitleInfo {
	public static final String TITLE_TITLENAME="newsTitle";
	public static final String TITLE_USERNAME="username";
	public static final String TITLE_TYPEID="typeid";
	@Column (column = "contentimg")
	String contentimg;
	@Id(column = "id")
	int id;
	@Column(column = "newsTitle")
	String title;
	@Column(column = "url")
	String url;
	@Column(column = "type")
	String type;
	@Column(column = "typeid")
	int typeid;
	@Column(column = "createDate")
	String date;
	@Column(column = "readCount")
	String readCount;
	@Column(column = "username")
	String username;
	Bitmap img;
	public Bitmap getImg() {
		return img;
	}
	public void setImg(Bitmap img) {
		this.img = img;
	}
	public String getContentimg() {
		return contentimg;
	}
	public void setContentimg(String contentimg) {
		this.contentimg = contentimg;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
