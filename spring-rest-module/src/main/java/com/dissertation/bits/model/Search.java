package com.dissertation.bits.model;

public class Search {
	private String tenantName;
	private double cost;
	private int clicks;
	private double cpc;
	private double avg_position;
	private int impressions;
	private double quote;
	private int leads;

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public double getCpc() {
		return cpc;
	}

	public void setCpc(double cpc) {
		this.cpc = cpc;
	}

	public double getAvg_position() {
		return avg_position;
	}

	public void setAvg_position(double avg_position) {
		this.avg_position = avg_position;
	}

	public int getImpressions() {
		return impressions;
	}

	public void setImpressions(int impressions) {
		this.impressions = impressions;
	}

	public double getQuote() {
		return quote;
	}

	public void setQuote(double quote) {
		this.quote = quote;
	}

	public int getLeads() {
		return leads;
	}

	public void setLeads(int leads) {
		this.leads = leads;
	}

	@Override
	public String toString() {
		String defaultString = super.toString();
		String manmadeString = " costing: " + getCost() + " and cpc: " + getCpc();
		return defaultString + manmadeString;
	}
}
