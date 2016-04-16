package edu.illinois.models;

/**
 * Created by banumuthukumar on 4/2/16.
 * This class is used for a map detail object which is the components obtained
 * after SQL query to create a heatmap
 */


public class MapDetail {
	
	public int _id;
	public float _latitude;
	public float _longitude;
	public int _sentiment;
	
	public MapDetail() {
		// TODO Auto-generated constructor stub
	}
	
	public MapDetail(int id, float lat, float lon, int sentiment)
	{
		_id = id;
		_latitude = lat;
		_longitude = lon;
		_sentiment = sentiment;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public float get_latitude() {
		return _latitude;
	}

	public void set_latitude(float _latitude) {
		this._latitude = _latitude;
	}

	public float get_longitude() {
		return _longitude;
	}

	public void set_longitude(float _longitude) {
		this._longitude = _longitude;
	}

	public int get_sentiment() {
		return _sentiment;
	}

	public void set_sentiment(int _sentiment) {
		this._sentiment = _sentiment;
	}
	

}
