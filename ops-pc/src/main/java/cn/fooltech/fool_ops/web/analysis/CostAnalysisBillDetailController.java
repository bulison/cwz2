package cn.fooltech.fool_ops.web.analysis;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.analysis.entity.CostAnalysisBilldetail;
import cn.fooltech.fool_ops.domain.analysis.service.CostAnalysisBilldetailService;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBillVo;
import cn.fooltech.fool_ops.domain.analysis.vo.CostAnalysisBilldetailVo;
import cn.fooltech.fool_ops.domain.basedata.service.TransportPriceService;
import cn.fooltech.fool_ops.domain.basedata.vo.TransportPriceVo;

/**
 * <p>
 * 成本分析明细网页控制器
 * </p>
 * 
 * @author cwz
 * @date 2016-12-21
 */
@Controller
@RequestMapping("/costAnalysisBillDetail")
public class CostAnalysisBillDetailController {
	private Logger logger = LoggerFactory.getLogger(CostAnalysisBillDetailController.class);
	@Autowired
	private CostAnalysisBilldetailService service;
	@Autowired
	private TransportPriceService transportPriceService;
	/**
	 * 管理界面
	 */
	@RequestMapping("/manage")
	public String manage() {
		return "/costAnalysis/manage";
	}

	@RequestMapping(value = "/query")
	@ResponseBody
	public Page<CostAnalysisBilldetailVo> query(CostAnalysisBilldetailVo vo, PageParamater paramater) {
		Page<CostAnalysisBilldetailVo> page = service.query(vo, paramater);
		return page;
	}
	/**
	 * 	查询其他运输公司在有效期内的报价记录
	 * @param vo
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value = "/findOtherCompany")
	@ResponseBody
	public Page<TransportPriceVo> findOtherCompany(TransportPriceVo vo,PageParamater paramater){
		Page<TransportPriceVo> page = transportPriceService.findOtherCompany(vo, paramater);
		return page;
	}
	/**
	 * 查询运输公司在有效期内的报价记录
	 * @param accId				账套id
	 * @param deliveryPlaceId	发货地ID  关联场地表
	 * @param receiptPlaceId	收货地ID  关联场地表	
	 * @param transportTypeId	运输方式ID(关联辅助属性运输方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param shipmentTypeId	装运方式ID(关联辅助属性装运方式)
	 * @param transportUnitId	运输单位id(关联辅助属性运输计价单位)	 
	 * @return
	 */
	@RequestMapping(value = "/findByCompany")
	@ResponseBody
	public TransportPriceVo findByCompany(String deliveryPlaceId,String receiptPlaceId,String transportTypeId,String shipmentTypeId,String supplierId,String priceUnitId,String transportUnitId){
		TransportPriceVo vo = transportPriceService.findByCompany(deliveryPlaceId, receiptPlaceId, transportTypeId, shipmentTypeId, supplierId,priceUnitId,transportUnitId);
		return vo;
	}

	/**
	 * 新增、编辑
	 * 
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public RequestResult save(CostAnalysisBilldetailVo vo) {
		RequestResult result = service.save(vo);
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param fid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public RequestResult delete(String fid) {
		return service.delete(fid);
	}

}
