package com.hackecho.TwitterStock;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;

import com.hackecho.mysql.TwitterDB;

public class Main {
	public static void main(String[] args) throws TwitterException {
		final TwitterDB db = new TwitterDB();
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				User user = status.getUser();
				String username = status.getUser().getScreenName();
				long tweetId = status.getId();
				String place = status.getPlace() == null ? "" : status.getPlace().getFullName();
				Date date = status.getCreatedAt();
				String profileLoc = user.getLocation();
				GeoLocation geoLoc = status.getGeoLocation();
				String geoLocStr = geoLoc == null ? "" : String.valueOf(geoLoc.getLatitude()) + "," + String.valueOf(geoLoc.getLongitude());
				String content = status.getText();
				HashtagEntity[] hashtagsArray = status.getHashtagEntities();
				String hashtags = "";
				for (HashtagEntity he : hashtagsArray) {
					hashtags += (he.getText() + ",");
				}
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				String sql = "INSERT INTO data(username, tweetId, place, date, profileLoc, geoLoc, content, hashtags)" 
				                  + " VALUES ('" + username + "', "
						          +          "'" + String.valueOf(tweetId) + "', "
						          +          "'" + place + "', "
						          +          "'" + df.format(date) + "', "
						          +          "'" + profileLoc + "', "
						          +          "'" + geoLocStr + "', "
						          +          "'" + content.replace('\'', ' ') + "', "
						          +          "'" + hashtags.replace('\'', ' ')
						          +          "')";
				try {
					db.insert(sql);
					System.out.println("==> Successfully inserted a record.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:"
						+ statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(listener);
		FilterQuery fq = new FilterQuery();
		String keywords[] = { "Starbucks" };
		fq.track(keywords);
		String[] lang = { "en" };
		fq.language(lang);
		twitterStream.filter(fq);
//		try {
//			twitterStream.filter(fq);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			twitterStream.cleanUp();
//		}
	}
}