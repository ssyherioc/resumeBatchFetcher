package com.elementResource.resume.batch.fetcher.core;

public interface IBatchFetchResumeJson {
	public String getJobIdListJson(String website, String compName, String compPWD);
	public String getCandIdListJsonByJobId(String website, String jobId, String candType,String compName, String compPWD);
	public String getCandInfoListJsonByJobId(String website, String jobId, String candType,String compName, String compPWD);
	public String getCandInfoJsonByCandURL(String website, String candId, String compName, String compPWD);
}
