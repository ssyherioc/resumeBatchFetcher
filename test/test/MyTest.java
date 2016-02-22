package test;

import com.elementResource.resume.batch.fetcher.BatchFetcher;
import com.elementResource.resume.batch.fetcher.util.Constant;
import com.elementResource.resume.batch.fetcher.entity.FetchType;

public class MyTest {
	public static void main(String[] args){
		//当前支持三种功能
		//1、获取所有职位的列表
		//String result =BatchFetcher.getJobIdList(FetchType.getEnumByName(Constant.WEBSITE_LIEPIN), "htxk", "huazhu2016");
		
		//2、根据职位Id以及候选人类型获取候选人列表
		//候选人类型共分为5种，分别如下：
		//1)Constant.CAND_TYPE_UNDEAL——未处理
		//2)Constant.CAND_TYPE_TARGET——目标人选
		//3)Constant.CAND_TYPE_UNDETERMINED——待定
		//4)Constant.CAND_TYPE_UNSUIT——不合适
		//5)Constant.CAND_TYPE_FILTER——被过滤简历
		//String result =BatchFetcher.getCandIdListByJobId(FetchType.getEnumByName(Constant.WEBSITE_LIEPIN), "2363705", Constant.CAND_TYPE_UNDETERMINED, "htxk", "huazhu2016");
		
		//3、根据职位Id以及候选人类型获取所有符合条件的候选人详细信息
		String result =BatchFetcher.getCandInfoListJsonByJobId(FetchType.getEnumByName(Constant.WEBSITE_LIEPIN), "4687030", Constant.CAND_TYPE_UNDEAL, "htxk", "huazhu2016");
		System.out.println(result);
		
		/****当前测试下来，发现识别验证码的程序成功率比较低，之前测试好多都是手动输入进行的测试****/
	}
}
