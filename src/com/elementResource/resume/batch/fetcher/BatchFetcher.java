package com.elementResource.resume.batch.fetcher;

import com.elementResource.resume.batch.fetcher.core.resume.BatchFetchResumeContent;
import com.elementResource.resume.batch.fetcher.entity.FetchType;

public class BatchFetcher {
	
	public static String getJobIdList(FetchType type,String compName, String compPWD) {
		if(type == null){
			throw new RuntimeException("暂未开发！");
		}
		
		return fetchJobIdList(type, compName, compPWD);
	}

	public static String getCandIdListByJobId(FetchType type,String jobId,  String candType, String compName, String compPWD){
		if(type == null){
			throw new RuntimeException("暂未开发！");
		}
		return fetchCandIdList(type, jobId, candType, compName, compPWD);		
	}
	
	public static String getCandInfoByCandURL(FetchType type,String candId, String compName, String compPWD){
		if(type == null){
			throw new RuntimeException("暂未开发！");
		}
		return fetchCandInfo(type, candId, compName, compPWD);		
	}
	
	public static String getCandInfoListJsonByJobId(FetchType type,String jobId, String candType, String compName, String compPWD){
		if(type == null){
			throw new RuntimeException("暂未开发！");
		}
		return fetchCandInfoList(type, jobId, candType, compName, compPWD);		
	}
	
	private static String fetchJobIdList(FetchType type, String compName,
			String compPWD) {
		return new BatchFetchResumeContent().getJobIdListJson(type.getName(), compName, compPWD);
	}
	
	private static String fetchCandIdList(FetchType type, String jobId, String candType,
			String compName, String compPWD) {
		return new BatchFetchResumeContent().getCandIdListJsonByJobId(type.getName(), jobId, candType, compName, compPWD);
	}
	
	private static String fetchCandInfo(FetchType type, String candURL,
			String compName, String compPWD) {
		return new BatchFetchResumeContent().getCandInfoJsonByCandURL(type.getName(), candURL, compName, compPWD);
	}
	
	private static String fetchCandInfoList(FetchType type, String jobId,String candType,
			String compName, String compPWD) {
		return new BatchFetchResumeContent().getCandInfoListJsonByJobId(type.getName(), jobId, candType,compName, compPWD);
	}
}
