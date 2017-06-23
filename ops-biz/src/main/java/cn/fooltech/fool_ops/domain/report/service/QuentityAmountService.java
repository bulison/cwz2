package cn.fooltech.fool_ops.domain.report.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.fiscal.entity.FiscalAccountingSubject;
import cn.fooltech.fool_ops.domain.fiscal.service.FiscalAccountingSubjectService;
import cn.fooltech.fool_ops.domain.report.repository.QuentityAmountRepository;
import cn.fooltech.fool_ops.domain.report.vo.QuentityAmountDetailVo;
import cn.fooltech.fool_ops.domain.report.vo.SubjectVo;
import cn.fooltech.fool_ops.utils.NumberUtil;
import cn.fooltech.fool_ops.utils.SecurityUtil;

/**
 * 数量金额明细账
 * @author xjh
 *
 */
@Service
public class QuentityAmountService {

	@Autowired
	private QuentityAmountRepository qaRepo;
	
	@Autowired
	private FiscalAccountingSubjectService subjectService;
	
	/**
	 * 转换VO
	 * @param data
	 * @return
	 */
	public QuentityAmountDetailVo getVo(Object[] data){
		if (data == null)
			return null;
		QuentityAmountDetailVo detail = new QuentityAmountDetailVo();
		try {
			detail.setVoucherDate(data[0]==null?"":data[0].toString());
			detail.setVoucherWordNum(data[1]==null?"":data[1].toString());
			detail.setVoucherNum(data[2]==null?"":data[2].toString());
			detail.setVoucherResume(data[3]==null?"":data[3].toString());
			detail.setSubjectCode(data[4]==null?"":data[4].toString());
			detail.setSubjectName(data[5]==null?"":data[5].toString());
			
			detail.setDebitUnit(data[6]==null?"":data[6].toString());
			detail.setDebitQuentity(data[7]==null?"":NumberUtil.stripTrailingZeros(data[7].toString(), 2));
			detail.setDebitUnitPrice(data[8]==null?"":NumberUtil.stripTrailingZeros(data[8].toString(), 2));
			detail.setDebitAmount(data[9]==null?"":NumberUtil.stripTrailingZeros(data[9].toString(), 2));
			
			detail.setCreditUnit(data[10]==null?"":data[10].toString());
			detail.setCreditQuentity(data[11]==null?"":NumberUtil.stripTrailingZeros(data[11].toString(), 2));
			detail.setCreditUnitPrice(data[12]==null?"":NumberUtil.stripTrailingZeros(data[12].toString(), 2));
			detail.setCreditAmount(data[13]==null?"":NumberUtil.stripTrailingZeros(data[13].toString(), 2));
			
			detail.setEndUnit(data[14]==null?"":data[14].toString());
			detail.setEndQuentity(data[15]==null?"":NumberUtil.stripTrailingZeros(data[15].toString(), 2));
			detail.setEndUnitPrice(data[16]==null?"":NumberUtil.stripTrailingZeros(data[16].toString(), 2));
			detail.setEndAmount(data[17]==null?"":NumberUtil.stripTrailingZeros(data[17].toString(), 2));
			
			detail.setEndDirection(data[18]==null?"":data[18].toString());
			detail.setEndAbsAmount(data[19]==null?"":NumberUtil.stripTrailingZeros(data[19].toString(), 2));
			return detail;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 转换VOS
	 * @param datas
	 * @return
	 */
	public List<QuentityAmountDetailVo> getVos(List<Object[]> datas){
		
		List<QuentityAmountDetailVo> vos = Lists.newArrayList();
		for(Object[] data:datas){
			vos.add(getVo(data));
		}
		return vos;
	}
	
	/**
	 * 分页查询
	 * @param paramater
	 * @param vo
	 * @return
	 */
	public List<QuentityAmountDetailVo> queryData(QuentityAmountDetailVo vo, PageParamater paramater){
		String startPeriodId = vo.getStartPeriodId();
		if(StringUtils.isBlank(startPeriodId)){
			return Collections.EMPTY_LIST;
		}
		
		String endPeriodId = vo.getEndPeriodId();
		if(StringUtils.isBlank(endPeriodId)){
			return Collections.EMPTY_LIST;
		}
		
		String curSubjectCode = vo.getCurSubjectCode();
		String voucherStatus = vo.getVoucherStatus();
		Integer level = vo.getLevel();
		
		// 分页
		int pageSize = paramater.getRows();
		int pageNo = paramater.getPage();
		int first = (pageNo - 1) * pageSize;
		
		return getVos(qaRepo.getData(startPeriodId, endPeriodId, curSubjectCode, voucherStatus, level, first, pageSize));
	}
	
	/**
	 * 查询数据总数
	 * @param paramater
	 * @param vo
	 * @return
	 */
	public Long countData(QuentityAmountDetailVo vo){
		String startPeriodId = vo.getStartPeriodId();
		if(StringUtils.isBlank(startPeriodId)){
			return 0L;
		}
		String endPeriodId = vo.getStartPeriodId();
		if(StringUtils.isBlank(endPeriodId)){
			return 0L;
		}
		String curSubjectCode = vo.getCurSubjectCode();
		String voucherStatus = vo.getVoucherStatus();
		Integer level = vo.getLevel();
		return qaRepo.countData(startPeriodId, endPeriodId, curSubjectCode, voucherStatus, level);
	}
	
	/**
	 * 根据起始编号和结束编号查找
	 * @param start
	 * @param end
	 * @return
	 */
	public List<SubjectVo> querySubject(String start,String end, Integer level){
		List<SubjectVo> vos = Lists.newArrayList();
		String accId = SecurityUtil.getFiscalAccountId();
		List<FiscalAccountingSubject> subjects = subjectService.queryByLimit(start, end, level, accId);
		for(FiscalAccountingSubject subject:subjects){
			SubjectVo vo = new SubjectVo();
			vo.setCode(subject.getCode());
			vo.setName(subject.getName());
			vos.add(vo);
		}
		return vos;
	}
}
