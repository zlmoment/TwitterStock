package com.hackecho.sentiment140;

public class Sentiment140Data {
	public String text = null;
	public String id = null;
	public int polarity = 0;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPolarity() {
		return polarity;
	}

	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}

	public Sentiment140Data() {
	}

	public Sentiment140Data(String id, String text, int polarity) {
		this.text = text;
		this.id = id;
		this.polarity = polarity;
	}
}
