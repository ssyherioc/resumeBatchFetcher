/**
 * 
 */
package com.elementResource.resume.batch.fetcher.entity;

import org.dom4j.Attribute;

import com.elementResource.resume.batch.fetcher.util.StringUtil;


/**
 * @author qianeryu
 * 
 */
public class JsonElement implements Cloneable{

	private String location;

	private String mapName;

	private String jsonName;

	private boolean createNewRootMap;

	private String ruleClasses;
	
	private boolean isEnum = false;
	
	private String dataType = "string";
	
	private String fid;
	
	private boolean isSplit = false; // Y N
	
	private boolean isOnlyMap = false;

	public JsonElement() {
	}

	public JsonElement clone() {
		try {
			return (JsonElement) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public JsonElement(String location, String mapName, String jsonName,
			boolean createNewRootMap, String ruleClasses, boolean isEnum, String dataType, String fid, boolean isSplit, boolean isOnlyMap) {
		this.location = location;
		this.mapName = mapName;
		this.jsonName = jsonName;
		this.ruleClasses = ruleClasses;
		this.createNewRootMap = createNewRootMap;
		this.isEnum = isEnum;
		this.dataType = dataType;
		this.fid = fid;
		this.isSplit = isSplit;
		this.isOnlyMap = isOnlyMap;
	}

	public static JsonElement parseJsonElement(org.dom4j.Element xmlJsonElement) {
		Attribute locationAttr = xmlJsonElement.attribute("loc");
		Attribute mapNameAttr = xmlJsonElement.attribute("mapName");
		Attribute jsonNameAttr = xmlJsonElement.attribute("jsonName");
		Attribute ruleClassesAttr = xmlJsonElement.attribute("ruleClasses");
		Attribute createNewRootMapAttr = xmlJsonElement.attribute("createNewRootMap");
		Attribute isEnumAttr = xmlJsonElement.attribute("isEnum");
		Attribute dataTypeAttr = xmlJsonElement.attribute("dataType");
		Attribute fidAttr = xmlJsonElement.attribute("fid");
		Attribute isSplitAttr = xmlJsonElement.attribute("isSplit");
		Attribute isOnlyMapAttr = xmlJsonElement.attribute("isOnlyMap");

		String location = (locationAttr == null) ? null : locationAttr.getValue();
		String mapName = (mapNameAttr == null) ? null : mapNameAttr.getValue();
		String jsonName = (jsonNameAttr == null) ? null : jsonNameAttr.getValue();
		String ruleClasses = (ruleClassesAttr == null) ? null : ruleClassesAttr.getValue();
		boolean createNewRootMap = false;
		if (createNewRootMapAttr != null && null != createNewRootMapAttr.getValue()) {
			String b = createNewRootMapAttr.getValue();
			if ("true".equals(b.trim().toLowerCase())) {
				createNewRootMap = true;
			}
		}
		boolean isEnum = false;
		if (isEnumAttr != null && !StringUtil.isNull(isEnumAttr.getValue()) && "Y".equals(isEnumAttr.getValue().toUpperCase())) {
			isEnum = true;
		}
		
		String dataType = "string";
		if (dataTypeAttr != null && !StringUtil.isNull(dataTypeAttr.getValue())) {
			dataType = dataTypeAttr.getValue();
		}
		
		String fid = null;
		if (null != fidAttr && !StringUtil.isNull(fidAttr.getValue())) {
			fid = fidAttr.getValue();
		}
		
		boolean isSplit = false;
		if (null != isSplitAttr && !StringUtil.isNull(isSplitAttr.getValue())) {
			isSplit = ("Y".equals(isSplitAttr.getValue()));
		}
		
		boolean isOnlyMap = false;
		if (null != isOnlyMapAttr && !StringUtil.isNull(isOnlyMapAttr.getValue())) {
			isOnlyMap = ("Y".equals(isOnlyMapAttr.getValue()));
		}
		
		JsonElement jsonElement = new JsonElement(location, mapName, jsonName, createNewRootMap, ruleClasses, isEnum, dataType, fid, isSplit, isOnlyMap);
		return jsonElement;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the mapName
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * @param mapName
	 *            the mapName to set
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	/**
	 * @return the jsonName
	 */
	public String getJsonName() {
		return jsonName;
	}

	/**
	 * @param jsonName
	 *            the jsonName to set
	 */
	public void setJsonName(String jsonName) {
		this.jsonName = jsonName;
	}

	/**
	 * @return the ruleClasses
	 */
	public String getRuleClasses() {
		return ruleClasses;
	}

	/**
	 * @param ruleClasses
	 *            the ruleClasses to set
	 */
	public void setRuleClasses(String ruleClasses) {
		this.ruleClasses = ruleClasses;
	}

	/**
	 * @return the createNewRootMap
	 */
	public boolean isCreateNewRootMap() {
		return createNewRootMap;
	}

	/**
	 * @param createNewRootMap
	 *            the createNewRootMap to set
	 */
	public void setCreateNewRootMap(boolean createNewRootMap) {
		this.createNewRootMap = createNewRootMap;
	}

	public boolean isEnum() {
		return isEnum;
	}

	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the fid
	 */
	public String getFid() {
		return fid;
	}

	/**
	 * @param fid the fid to set
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}

	/**
	 * @return the isSplit
	 */
	public boolean isSplit() {
		return isSplit;
	}

	/**
	 * @param isSplit the isSplit to set
	 */
	public void setSplit(boolean isSplit) {
		this.isSplit = isSplit;
	}

	/**
	 * @return the isOnlyMap
	 */
	public boolean isOnlyMap() {
		return isOnlyMap;
	}

	/**
	 * @param isOnlyMap the isOnlyMap to set
	 */
	public void setOnlyMap(boolean isOnlyMap) {
		this.isOnlyMap = isOnlyMap;
	}

}
