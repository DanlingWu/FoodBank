package com.findfoodbank.rest.foodbank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class FoodBank {
	/**
	 * Order matters here for data initialisation - has to be alphabetical order
	 */
	@Id
	@GeneratedValue
	private Long id;
	private String address;

	/** for stoting the values correctly in mysql decimal type, 10,6 recommended by Google Maps API **/
	@Column(precision = 10, scale = 6)
	private BigDecimal latitude;
	@Column(precision = 10, scale = 6)
	private BigDecimal longitude;

	private String name;

	public FoodBank() {
		super();
	}

	public FoodBank(Long id, String name, String address, BigDecimal la, BigDecimal lo) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.latitude = la;
		this.longitude = lo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

}
