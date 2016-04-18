package edu.illinois.models;

import java.util.List;

import org.joda.time.DateTime;

/**
 * Created by banumuthukumar on 4/2/16.
 * This class is used for a map detail object which is the components obtained
 * after SQL query to create a heatmap
 */


public class MapDetail {
	
	public long id;
	public float latitude;
	public float longitude;
	public int sentiment;
	
	public MapDetail() {
		// TODO Auto-generated constructor stub
	}
	
	public MapDetail(long id, float lat, float lon, int sentiment)
	{
		this.id = id;
		latitude = lat;
		longitude = lon;
		this.sentiment = sentiment;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
	

}
