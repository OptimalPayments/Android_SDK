package com.optimalpaymentstestapp.lookup;

import java.io.Serializable;

/**
 * @author Manisha.Rani
 * 
 */
@SuppressWarnings("serial")
public class SearchResults implements Serializable {
	// For card
	private String cardBillingAddressId = "";
	private String cardId = "";
	private String status = "";
	private String holderName = "";
	private String cardBin = "";
	private String lastDigits = "Time";
	private String month = "";
	private String year = "";
	private String cardType = "";
	private String paymentToken = "";
	private String sortBy = "";
	private boolean isDefaultCardIndicator = false;
	// For address
	private String addresseID ="";
	private String street = "";
	private String street2 = "";
	private String city = "";
	private String country = "";
	private String state = "";
	private String zip = "";
	

	public SearchResults() {

	}

	public SearchResults(String cardId, String cardBillingAddressId, String status, String holderName,
			String cardBin, String lastDigits, String month, String year,
			String cardType, String paymentToken, boolean isDefaultCardIndicator) {
		this.cardId =cardId;
		this.cardBillingAddressId = cardBillingAddressId;
		this.status = status;
		this.holderName = holderName;
		this.status = status;
		this.cardBin = cardBin;
		this.lastDigits = lastDigits;
		this.month = month;
		this.year = year;
		this.cardType = cardType;
		this.paymentToken = paymentToken;
		this.isDefaultCardIndicator = isDefaultCardIndicator;
	}

	public SearchResults(String cardId,String cardBillingAddressId, String status, String holderName,
			String cardBin, String lastDigits, String month, String year,
			String cardType, String paymentToken) {
		this.cardId =cardId;
		this.cardBillingAddressId = cardBillingAddressId;
		this.status = status;
		this.holderName = holderName;
		this.status = status;
		this.cardBin = cardBin;
		this.lastDigits = lastDigits;
		this.month = month;
		this.year = year;
		this.cardType = cardType;
		this.paymentToken = paymentToken;

	}
	
	public SearchResults(String addresseID ,String street, String street2, String city,String country, String state, String zip) {
		this.addresseID = addresseID;
		this.street= street;
		this.street2 = street2;
		this.city = city;
		this.country = country;
		this.state = state;
		this. zip =  zip;
	}


	public String getcardBillingAddressId() {
		return cardBillingAddressId;
	}

	public void setcardBillingAddressId(String id) {
		this.cardBillingAddressId = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getCardBin() {
		return cardBin;
	}

	public void setCardBin(String cardBin) {
		this.cardBin = cardBin;
	}

	public String getLastDigits() {
		return lastDigits;
	}

	public void setLastDigits(String lastDigits) {
		this.lastDigits = lastDigits;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public boolean isDefaultCardIndicator() {
		return isDefaultCardIndicator;
	}

	public void setDefaultCardIndicator(boolean isDefaultCardIndicator) {
		this.isDefaultCardIndicator = isDefaultCardIndicator;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street1) {
		this.street2 = street1;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddresseID() {
		return addresseID;
	}

	public void setAddresseID(String addresseID) {
		this.addresseID = addresseID;
	}
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}


}
