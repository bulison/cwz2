package cn.fooltech.fool_ops.eureka.rateService.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.eureka.rateService.dao.CustomerIncomeAnalysisDao;
import cn.fooltech.fool_ops.eureka.rateService.vo.CapitalPlanChangeLogTemVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.CapitalPlanDetailTemVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.CustomerIncomeAnalysisVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.PayMentTemVo;
import cn.fooltech.fool_ops.eureka.rateService.vo.WarehouseBillTemVo;

@Service
public class CustomerIncomeAnalysisService {
	@Autowired
	private CustomerIncomeAnalysisDao dao;
	//客户收益分析
	public List<CustomerIncomeAnalysisVo> customerIncomeAnalysisProcessing(String endDate,String startDate,String orgId,String accId,String customerId,String category,String area,Integer page,Integer rows){
		List<CustomerIncomeAnalysisVo> list=dao.customerIncomeAnalysisProcessing(endDate, startDate, orgId, accId,customerId,category,area,page,rows);
		BigDecimal rate=dao.findRate(orgId);
		double x=rate.doubleValue()+1d; //公式中的1+β
		for(int j=0;j<list.size();j++){
			List<WarehouseBillTemVo> wbtvs=dao.findWarehouseBillByCustomerId(list.get(j).getCustomerId(),accId);
			List<CapitalPlanDetailTemVo> cpdtvs=dao.findPlanDateByCustomerId(list.get(j).getCustomerId(),accId);
			for(CapitalPlanDetailTemVo cpdtv:cpdtvs){
				if(cpdtv.getPaymentDate().compareTo(new Date())<0){
					WarehouseBillTemVo wbvo=dao.findWarehouseBillById(cpdtv.getRelationId());
					if(wbvo!=null){
						wbtvs.add(wbvo);
					}
					
				}
			}
			for(int i=0;i<wbtvs.size();i++){
				int tna=0;
				int tnb=0;
				BigDecimal cna=BigDecimal.ZERO;
				BigDecimal cnb=BigDecimal.ZERO;
				//计算实际天数
				List<PayMentTemVo> pmtvs=dao.findPayMentByWareHouseBillId(wbtvs.get(i).getFid(),accId);
				BigDecimal npa=BigDecimal.ZERO;
				BigDecimal npb=BigDecimal.ZERO;
				List<BigDecimal> ctna=new ArrayList<BigDecimal>();
				List<BigDecimal> ctnb=new ArrayList<BigDecimal>();
				BigDecimal ta=BigDecimal.ZERO;
				BigDecimal tb=BigDecimal.ZERO;
				int tn=0;
				for(PayMentTemVo pmtv:pmtvs){
					BigDecimal amount=pmtv.getAmount();
					BigDecimal freeAmount=pmtv.getFreeAmount();
					if(amount==null) amount=BigDecimal.ZERO;
					if(freeAmount==null) freeAmount=BigDecimal.ZERO;
					cna=amount.add(freeAmount);
					tna=daysBetween(pmtv.getWareHouseBillDate(),pmtv.getPaymentBillDate());
					npa=npa.add(cna.multiply(new BigDecimal(Math.pow(x, -tna))));
					
					ctna.add(cna.multiply(new BigDecimal(tna)));
				}
				//对未完成收款的单据进行处理
				if(wbtvs.get(i).getTotalAmount().compareTo(wbtvs.get(i).getFreeAmount().add(wbtvs.get(i).getTotalPayAmount()))!=0){
					List<CapitalPlanDetailTemVo> planDeatils=wbtvs.get(i).getPlanDetailList();
					for(CapitalPlanDetailTemVo planDeatil:planDeatils){
						cna=subtract(planDeatil.getBillAmount(),(planDeatil.getPaymentAmount()));
						if(planDeatil.getPaymentDate().compareTo(new Date())<=0){
							tna=daysBetween(wbtvs.get(i).getBillDate(),getTomorrow(new Date()));
						}else{
							tna=daysBetween(wbtvs.get(i).getBillDate(),planDeatil.getPaymentDate());
						}
						npa=npa.add(cna.multiply(new BigDecimal(Math.pow(x, -tna))));
						ctna.add(cna.multiply(new BigDecimal(tna)));
					}	
				}
				for(BigDecimal b:ctna){
					if(npa.doubleValue()!=0){
						ta=ta.add((b.divide(npa,10,RoundingMode.CEILING)));
					}
					
				}
				wbtvs.get(i).setTa(ta);
				//计算预计天数
				List<CapitalPlanDetailTemVo> capitalplanDeatils=wbtvs.get(i).getPlanDetailList();
				for(CapitalPlanDetailTemVo capitalplanDeatil:capitalplanDeatils){
					tnb=daysBetween(wbtvs.get(i).getBillDate(),capitalplanDeatil.getOrgPaymentDate());
					cnb=capitalplanDeatil.getBillAmount();
					ctnb.add(cnb.multiply(new BigDecimal(tnb)));
					npb=npb.add(cnb.multiply(new BigDecimal(Math.pow(x, -tnb))));
					
				}
				for(BigDecimal b:ctnb){
					if(npb.doubleValue()!=0){
						tb=tb.add((b.divide(npb,10,RoundingMode.CEILING)));
					}
				}
				wbtvs.get(i).setTb(tb);
				wbtvs.get(i).setTy(ta.subtract(tb));
				List<CapitalPlanDetailTemVo> planDeatils=wbtvs.get(i).getPlanDetailList();
				int tns=0;//每一张明细单对应的计划延迟个数
				for(CapitalPlanDetailTemVo planDeatil:planDeatils){
					
					List<CapitalPlanChangeLogTemVo> pclts=dao.findPlanChangeLogByPlanDetailId(planDeatil.getFid(),accId);
					for(CapitalPlanChangeLogTemVo pclt:pclts){
						if(pclt.getChangeType()==1){
							if(pclt.getPrePaymentDate().compareTo(pclt.getPaymentDate())<0){
								tns++;
							}
						}
					}
					
				}
				//延迟次数
				if(planDeatils.size()!=0){
					tn=tns/planDeatils.size();
				}else{
					tn=0;
				}
				
				if(wbtvs.get(i).getTy().compareTo(BigDecimal.ZERO)>0&&tn==0){
					wbtvs.get(i).setTn(1);
				}else{
					wbtvs.get(i).setTn(tn);
				}
			}
			//信用度计算
			BigDecimal a=BigDecimal.ZERO;//公式的上半部分
			BigDecimal b=BigDecimal.ZERO;//公式的下半部分
			for(WarehouseBillTemVo wbtv:wbtvs){

				double tn=wbtv.getTn();
				BigDecimal jn=wbtv.getTotalAmount();
				BigDecimal ty=wbtv.getTy();
				BigDecimal tb=wbtv.getTb();;
				a=a.add(new BigDecimal(Math.pow((tn+6)/7,tn-1)*(Math.pow(x,ty.doubleValue()))).multiply(ty).multiply(jn));
				b=b.add(jn.multiply(tb.multiply(new BigDecimal(Math.pow(x,tb.doubleValue())))));
			}
			BigDecimal credit;
			if(b.compareTo(BigDecimal.ZERO)==0){
				credit=BigDecimal.ZERO;
			}else{
				credit=a.divide(b, 8, RoundingMode.CEILING);
			}
			credit=BigDecimal.ONE.subtract(credit);
			credit=credit.multiply(new BigDecimal(100));
			list.get(j).setCredit(credit);
		}
		//最后把根据客户类别ID,地区ID搜索出客户类别名和地区名
		for(CustomerIncomeAnalysisVo vo:list){
			vo.setAreaName(dao.findAttrNameById(vo.getArea()));
			vo.setCategoryName(dao.findAttrNameById(vo.getCategory()));
		}
		return list;
	}
	//客户收益明细
	public List<WarehouseBillTemVo> customerIncomeAnalysisDetail(String CustomerId,String orgId,String accId,String startDate,String endDate){
		List<WarehouseBillTemVo> wbtvs=dao.findWarehouseBillByCustomerIdAndDate(CustomerId,startDate,endDate,accId);
		List<CapitalPlanDetailTemVo> cpdtvs=dao.findPlanDateByCustomerIdAndDate(CustomerId,startDate,endDate,accId);
		for(CapitalPlanDetailTemVo cpdtv:cpdtvs){
			if(cpdtv.getPaymentDate().compareTo(new Date())<0){
				WarehouseBillTemVo wbvo=dao.findWarehouseBillById(cpdtv.getRelationId());
				if(wbvo!=null){
					wbtvs.add(wbvo);
				}
				
			}
		}
		return calculatewarehouseBill(wbtvs,orgId,accId);
	}
	//计算仓库单据明细
	public List<WarehouseBillTemVo> calculatewarehouseBill(List<WarehouseBillTemVo> wbtvs,String orgId,String accId){
		BigDecimal rate=dao.findRate(orgId);
		double x=rate.doubleValue()+1d; //公式中的1+β
		for(int i=0;i<wbtvs.size();i++){
			int tna=0;
			int tnb=0;
			BigDecimal cna=BigDecimal.ZERO;
			BigDecimal cnb=BigDecimal.ZERO;
			//计算实际天数
			List<PayMentTemVo> pmtvs=dao.findPayMentByWareHouseBillId(wbtvs.get(i).getFid(),accId);
			BigDecimal npa=BigDecimal.ZERO;
			BigDecimal npb=BigDecimal.ZERO;
			List<BigDecimal> ctna=new ArrayList<BigDecimal>();
			List<BigDecimal> ctnb=new ArrayList<BigDecimal>();
			BigDecimal ta=BigDecimal.ZERO;
			BigDecimal tb=BigDecimal.ZERO;
			int tn=0;
			for(PayMentTemVo pmtv:pmtvs){
				BigDecimal amount=pmtv.getAmount();
				BigDecimal freeAmount=pmtv.getFreeAmount();
				if(amount==null) amount=BigDecimal.ZERO;
				if(freeAmount==null) freeAmount=BigDecimal.ZERO;
				cna=amount.add(freeAmount);
				tna=daysBetween(pmtv.getWareHouseBillDate(),pmtv.getPaymentBillDate());
				npa=npa.add(cna.multiply(new BigDecimal(Math.pow(x, -tna))));
				
				ctna.add(cna.multiply(new BigDecimal(tna)));
			}
			//对未完成收款的单据进行处理
			if(wbtvs.get(i).getTotalAmount().compareTo(wbtvs.get(i).getFreeAmount().add(wbtvs.get(i).getTotalPayAmount()))!=0){
				List<CapitalPlanDetailTemVo> planDeatils=wbtvs.get(i).getPlanDetailList();
				for(CapitalPlanDetailTemVo planDeatil:planDeatils){
					cna=subtract(planDeatil.getBillAmount(),(planDeatil.getPaymentAmount()));
					if(planDeatil.getPaymentDate().compareTo(new Date())<=0){
						tna=daysBetween(wbtvs.get(i).getBillDate(),getTomorrow(new Date()));
					}else{
						tna=daysBetween(wbtvs.get(i).getBillDate(),planDeatil.getPaymentDate());
					}
					npa=npa.add(cna.multiply(new BigDecimal(Math.pow(x, -tna))));
					ctna.add(cna.multiply(new BigDecimal(tna)));
				}	
			}
			for(BigDecimal b:ctna){
				if(npa.doubleValue()!=0){
					ta=ta.add((b.divide(npa,10,RoundingMode.CEILING)));
				}
				
			}
			wbtvs.get(i).setTa(ta);
			//计算预计天数
			List<CapitalPlanDetailTemVo> capitalplanDeatils=wbtvs.get(i).getPlanDetailList();
			for(CapitalPlanDetailTemVo capitalplanDeatil:capitalplanDeatils){
				tnb=daysBetween(wbtvs.get(i).getBillDate(),capitalplanDeatil.getOrgPaymentDate());
				cnb=capitalplanDeatil.getBillAmount();
				ctnb.add(cnb.multiply(new BigDecimal(tnb)));
				npb=npb.add(cnb.multiply(new BigDecimal(Math.pow(x, -tnb))));
				
			}
			for(BigDecimal b:ctnb){
				if(npb.doubleValue()!=0){
					tb=tb.add((b.divide(npb,10,RoundingMode.CEILING)));
				}
			}
			wbtvs.get(i).setTb(tb);
			wbtvs.get(i).setTy(ta.subtract(tb));
			List<CapitalPlanDetailTemVo> planDeatils=wbtvs.get(i).getPlanDetailList();
			int tns=0;//每一张明细单对应的计划延迟个数
			for(CapitalPlanDetailTemVo planDeatil:planDeatils){
				
				List<CapitalPlanChangeLogTemVo> pclts=dao.findPlanChangeLogByPlanDetailId(planDeatil.getFid(),accId);
				for(CapitalPlanChangeLogTemVo pclt:pclts){
					if(pclt.getChangeType()==1){
						if(pclt.getPrePaymentDate().compareTo(pclt.getPaymentDate())<0){
							tns++;
						}
					}
				}
				
			}
			//延迟次数
			if(planDeatils.size()!=0){
				tn=tns/planDeatils.size();
			}else{
				tn=0;
			}
			
			if(wbtvs.get(i).getTy().compareTo(BigDecimal.ZERO)>0&&tn==0){
				wbtvs.get(i).setTn(1);
			}else{
				wbtvs.get(i).setTn(tn);
			}
			wbtvs.get(i).setCostAmount(dao.findWareHouseBillDetailCostAmount(wbtvs.get(i).getFid(),accId));
			wbtvs.get(i).setCost(dao.findCostByWareHouseBillId(wbtvs.get(i).getFid(),accId));
			wbtvs.get(i).setProfit(subtract(wbtvs.get(i).getTotalAmount(),add(wbtvs.get(i).getCostAmount(),(wbtvs.get(i).getCost()))));
			wbtvs.get(i).setNoPayAmount(subtract(wbtvs.get(i).getTotalAmount(),add(wbtvs.get(i).getFreeAmount(),(wbtvs.get(i).getTotalPayAmount()))));
		}
		return wbtvs;
	}

		//求日期之差
    public static int daysBetween(Date smdate, Date bdate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
    //获取明天的日期
    public Date getTomorrow(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);
        return calendar.getTime(); 
    }
    /**
     * 两数相减，防止空
     */
    public BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
        if (num1 == null) num1 = BigDecimal.ZERO;
        if (num2 == null) num2 = BigDecimal.ZERO;
        return num1.subtract(num2);
    }
    /**
     * 两数相加，防止空
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
        if (num1 == null) num1 = BigDecimal.ZERO;
        if (num2 == null) num2 = BigDecimal.ZERO;
        return num2.add(num1);
    }
}
