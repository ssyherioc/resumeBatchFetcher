package com.elementResource.resume.batch.fetcher.fetchValueRule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.omg.CORBA.INTERNAL;

import com.elementResource.resume.batch.fetcher.util.StringUtil;


public class BirthDtRule implements IRule {

	@Override
	public String getValue(String... value) {
		if (!StringUtil.isNull(value[0])) {
			String age = value[0];
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR); 
			int ageInt = Integer.parseInt(age);
			ageInt = year - ageInt;
			SimpleDateFormat sf = new SimpleDateFormat("yyyy");
			SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date d = sf.parse(ageInt+"");
				return sf2.format(d);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}

	
	public static void main(String args[]) {
		System.out.println(new BirthDtRule().getValue("24 岁"));
	}
}
