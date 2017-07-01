package com.grst.hotelapp.module;

import cn.bmob.v3.BmobObject;

public class RoomType  extends BmobObject {

	private String name;
	private float price;
	private float discountPrice;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(float discountPrice) {
		this.discountPrice = discountPrice;
	}
}
