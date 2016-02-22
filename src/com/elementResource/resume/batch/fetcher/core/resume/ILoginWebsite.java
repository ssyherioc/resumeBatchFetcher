package com.elementResource.resume.batch.fetcher.core.resume;

import java.util.List;
import java.util.Map;


public interface ILoginWebsite {

	public void getJobListJson(String userName, String pwd);
	public void getCandIdListJsonByJobId(String userName, String pwd, String jobId, String candType);
	public void getCandInfoListJsonByURLList(String userName, String pwd, List<String> urlList);
	public Map<String, Object> getResultMap();
}