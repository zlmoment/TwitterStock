package com.hackecho.TwitterStock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetBScore {
	String startTime = "10/03/2014 08:30:00";
	String endTime = "10/03/2014 15:00:00";
	static List<String> intervals = new ArrayList<String>();
	static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	public static void main(String[] args) throws Exception {
		GetBScore bs = new GetBScore();
		bs.createIntervals(10);
		try {
			String fileName = "INPUT";
			int lineNumber = countLines(new File(fileName));
			String[] output = new String[lineNumber];
			String[] input = new String[lineNumber];
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			for (int i = 0; i < lineNumber; i++) {
				line = bufferedReader.readLine();
				input[i] = line;
			}
			int pos = 0, neg = 0;
			int j = 0;
			Date min = dateFormat.parse(intervals.get(j));
			Date max = dateFormat.parse(intervals.get(j+1));
			Date thisDate;
			for (int i = 0; i < lineNumber; i++) {
				line = input[i];
				String[] lineArray = line.split("	");
				thisDate = dateFormat.parse(lineArray[0]);
				if (thisDate.before(max) && thisDate.after(min)) {
					if (lineArray[1].trim().equals("0")) {
						neg++;
					} else {
						pos++;
					}
					output[i] = line;
				} else {
					j++;
					min = dateFormat.parse(intervals.get(j));
					max = dateFormat.parse(intervals.get(j+1));
					//System.out.println(thisDate);
					while (!(thisDate.before(max) && thisDate.after(min))){
						j++;
						
						min = dateFormat.parse(intervals.get(j));
						max = dateFormat.parse(intervals.get(j+1));
					}
					double score = Math.log((double)(1+pos)/(1+neg));
					if (i == 0) {
						output[i] = line;
						if (lineArray[1].trim().equals("0")) {
							neg++;
						} else {
							pos++;
						}
						continue;
					} else {
						output[i-1] = output[i-1] + "	" + score;
						output[i] = line;
					}
					pos = 0;
					neg = 0;
					if (lineArray[1].trim().equals("0")) {
						neg++;
					} else {
						pos++;
					}
				}
			}

			bufferedReader.close();
			
			for (String s : output) {
				System.out.println(s);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createIntervals(int interval) throws Exception {
		intervals.add(startTime);
		Date startDate = dateFormat.parse(startTime);
		Date endDate = dateFormat.parse(endTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MINUTE, interval);
		Date currDate = cal.getTime();
		while (currDate.before(endDate)) {
			String newTime = dateFormat.format(cal.getTime());
			intervals.add(newTime);
			cal.setTime(currDate);
			cal.add(Calendar.MINUTE, interval);
			currDate = cal.getTime();
		}
		intervals.add(endTime);
	}

	public static void getResult(boolean[] result) {
		for (int i = 0; i < result.length; i++) {
			if (result[i] == false) {
				System.out.println(i + 1);
			}
		}
	}

	public static int countLines(File aFile) throws IOException {
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new FileReader(aFile));
			while ((reader.readLine()) != null)
				;
			return reader.getLineNumber();
		} catch (Exception ex) {
			return -1;
		} finally {
			if (reader != null)
				reader.close();
		}
	}

}
