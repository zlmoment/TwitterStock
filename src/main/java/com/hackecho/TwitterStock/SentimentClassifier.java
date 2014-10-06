package com.hackecho.TwitterStock;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.hackecho.mysql.TwitterDB;
import com.hackecho.sentiment140.*;

public class SentimentClassifier {

	public static void main(String[] args) throws Exception {
		String tableName = "starbucks_1003";
		Connection connect = null;
		Statement statement = null;
		ResultSet result = null;
		final String sql = "select * from " + tableName;
		try {
			TwitterDB db = new TwitterDB();
			connect = TwitterDB.getConnection();
			statement = connect.createStatement();
			result = statement.executeQuery(sql);
			Sentiment140Data[] datas = new Sentiment140Data[10];
			Connector connector = new Connector();
			int i = 0;
			while (result.next()) {
				String id = result.getString("id");
				String text = preProcessing(result.getString("content"));
				datas[i] = new Sentiment140Data(id, text, 0);
				if (i == 9) {
					datas = connector.send(datas);
					System.out.println("========");
					for (Sentiment140Data data : datas) {
						System.out.println("Id: " + data.id + " polarity: " + data.polarity);
						String sentimentSql = "UPDATE " + tableName + " SET polarity=" + data.polarity + " WHERE id=" + data.id;
						//System.out.println(sentimentSql);
						db.update(sentimentSql);
					}
					i = 0;
					continue;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connect.close();
		}
	}

	private static String preProcessing(String original) {
		return original.replaceAll("((www\\.[\\s]+)|(https?://[^\\s]+))", "")
				.replaceAll("@[^\\s]+", "").replaceAll("#([^\\s]+)", "\1")
				.replace('"', ' ').replace(',', ' ').replace('.', ' ')
				.replace('!', ' ').replace('?', ' ').replace(':', ' ')
				.replace('*', ' ').replace('\'', ' ')
				.replaceAll("[0-9][a-zA-Z0-9]*", "");
	}

}
