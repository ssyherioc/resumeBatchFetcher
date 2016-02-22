/**
 * 
 */
package com.elementResource.resume.batch.fetcher.util;


/**
 * @author qianeryu
 *
 */
public class DataTypeUtil {

	public static Object getValue(String jsonValue, String dataType) {
		if (StringUtil.isNull(dataType) || Constant.DATA_TYPE_STRING.equals(dataType)) {
			return jsonValue;
		} else {
			if (Constant.DATA_TYPE_INT.equals(dataType)) {
				try {
					int index = jsonValue.indexOf(".");
					String v = null;
					if (-1 == index) {
						v = jsonValue;
					} else {
						v = jsonValue.substring(0, index);
					}
					if (v.equals("")) {
						return 0;
					}
					return Integer.parseInt(v);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (Constant.DATA_TYPE_FLOAT.equals(dataType)) {
				try {
					Float f = Float.parseFloat(jsonValue);
					return f;
				} catch (Exception e) {}
			}else if (Constant.DATA_TYPE_BOOL.equals(dataType)) {
				try {
					Boolean b = Boolean.parseBoolean(jsonValue);
					return b;
				} catch (Exception e) {} 
			}
			else {
				return null;
			}
		}
		return null;
	}
	
	public static void main(String args[]) {
		System.out.println(getValue("2335.5332", "int"));
		System.out.println(getValue("555.5", "float"));
		System.out.println(getValue("555x.5", "float"));
	}
	
}
