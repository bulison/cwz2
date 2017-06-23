package cn.fooltech.fool_ops.web.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.fooltech.fool_ops.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.report.service.GoodsStockService;
import cn.fooltech.fool_ops.domain.report.vo.GoodsStockDetailVo;
import cn.fooltech.fool_ops.domain.report.vo.GoodsStockVo;
import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
import cn.fooltech.fool_ops.utils.ExcelUtils;

/**
 * <p>
 * 货品库存
 * </p>
 * 
 * @author xjh
 * @version 1.0
 * @date 2015年10月13日
 */
@Controller
@RequestMapping("/report/goodsStock/")
public class GoodsStockController {

	/**
	 * 货品库存服务类
	 */
	@Autowired
	private GoodsStockService webService;

	@RequestMapping(value = "/manage")
	public String manage() {
		return "/report/goodsstock/manage";
	}

	/**
	 * 显示详情
	 */
	@RequestMapping(value = "/window")
	public String window(GoodsStockVo vo, ModelMap model) {
		model.put("data", vo);
		return "/report/goodsstock/detail";
	}

	/**
	 * 详情
	 * 要传的参数：
	 * periodId,warehouseId,goodsId,specId,accountPrice
	 */
	@RequestMapping("/listDetail")
	@ResponseBody
	public List<GoodsStockDetailVo> listDetail(GoodsStockVo vo, PageParamater paramater) throws Exception {
		List<GoodsStockDetailVo> list = webService.getDetailList(vo, paramater);

		BigDecimal lastAccountQuentity = new BigDecimal(vo.getLastAccountQuentity());
		BigDecimal accountPrice = vo.getAccountPrice();

		BigDecimal lastAccountAmount = BigDecimal.ZERO;
		for(GoodsStockDetailVo detail:list){
			double qurntity = Double.parseDouble(detail.getQuentity());
			int type = detail.getType();
			Short moveOut = detail.getMoveOut();
			BigDecimal detailAccountQuentity =  new BigDecimal(detail.getQuentity());
			Integer calTotal = vo.getCalTotal();

			int checkOut = checkInOut(qurntity,type,moveOut);
			if(checkOut==WarehouseBill.IN){
				lastAccountQuentity = lastAccountQuentity.add(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}else if(checkOut==WarehouseBill.OUT){
				lastAccountQuentity = lastAccountQuentity.subtract(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}else if(checkOut==WarehouseBill.MOVE_IN&&(calTotal==null||calTotal==0)){
				lastAccountQuentity = lastAccountQuentity.add(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}else if(checkOut==WarehouseBill.MOVE_OUT&&(calTotal==null||calTotal==0)){
				lastAccountQuentity = lastAccountQuentity.subtract(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}
			detail.setAccountCostAmount(NumberUtil.bigDecimalToStr(NumberUtil.stripTrailingZeros(lastAccountAmount)));
			detail.setAccountQuentity(NumberUtil.bigDecimalToStr(NumberUtil.stripTrailingZeros(lastAccountQuentity)));
		}
		
		//过滤查询条件
		List<GoodsStockDetailVo> results = Lists.newArrayList();
		
		for(GoodsStockDetailVo detail:list){
			
			boolean flag = true;
			if(vo.getBillType()!=null){
				if(detail.getType()==null || detail.getType()!=vo.getBillType()){
					flag = false;
				}
			}
			if(vo.getIntoutTag()!=null){
				double qurntity = Double.parseDouble(detail.getQuentity());
				int type = detail.getType();
				Short moveOut = detail.getMoveOut();
				int inOutCheck = checkShowInOut(qurntity,type,moveOut);
				
				if(inOutCheck!=vo.getIntoutTag()){
					flag = false;
				}
			}
			if(flag==true)results.add(detail);
		}
		
		return results;
	}

	// 调仓单
	public static final int dcd = 21;
	
	/**
	 * 判断是进仓显示还是出仓显示（采购属于进仓、销售属于出仓）
	 * @param quentity
	 * @param type
	 * @param moveOut
	 * @return
	 */
	private int checkShowInOut(double quentity, int type, Short moveOut) {
		// 期初库存
		if (type == 91) {
			return WarehouseBill.IN;
		}
		// 采购入库
		else if (type == 11) {
			return WarehouseBill.IN;
		}
		// 采购退货
		else if (type == 12) {
			return WarehouseBill.IN;
		}
		// 盘点单(盘盈)
		else if (type == 20 && quentity > 0) {
			return WarehouseBill.IN;
		}
		// 盘点单(盘亏)
		else if (type == 20 && quentity < 0) {
			return WarehouseBill.OUT;
		}
		// 报损单
		else if (type == 22) {
			return WarehouseBill.OUT;
		}
		// 生产领料
		else if (type == 30) {
			return WarehouseBill.OUT;
		}
		//生产退料
		else if (type == 32) {
			return WarehouseBill.OUT;
		}
		// 成品入库
		else if (type == 31) {
			return WarehouseBill.IN;
		}
		// 成品退库
		else if (type == 33) {
			return WarehouseBill.IN;
		}
		// 销售出货
		else if (type == 41) {
			return WarehouseBill.OUT;
		}
		// 销售退货
		else if (type == 42) {
			return WarehouseBill.OUT;
		}
		//调仓单
		else if(type == 21){
			if(moveOut!=null&&moveOut==1){
				return WarehouseBill.MOVE_OUT;
			}else{
				return WarehouseBill.MOVE_IN;
			}
		}
		return WarehouseBill.IN;
	}

	/**
	 * 判断是进还是出，以对数量进行加减
	 * @param quentity
	 * @param type
	 * @param moveOut
	 * @return
	 */
	private int checkInOut(double quentity, int type, Short moveOut) {
		// 期初库存
		if (type == 91) {
			return WarehouseBill.IN;
		}
		// 采购入库
		else if (type == 11) {
			return WarehouseBill.IN;
		}
		// 采购退货
		else if (type == 12) {
			return WarehouseBill.OUT;
		}
		// 盘点单(盘盈)
		else if (type == 20 && quentity > 0) {
			return WarehouseBill.IN;
		}
		// 盘点单(盘亏)
		else if (type == 20 && quentity < 0) {
			return WarehouseBill.OUT;
		}
		// 报损单
		else if (type == 22) {
			return WarehouseBill.OUT;
		}
		// 生产领料
		else if (type == 30) {
			return WarehouseBill.OUT;
		}
		//生产退料
		else if (type == 32) {
			return WarehouseBill.IN;
		}
		// 成品入库
		else if (type == 31) {
			return WarehouseBill.IN;
		}
		// 成品退库
		else if (type == 33) {
			return WarehouseBill.OUT;
		}
		// 销售出货
		else if (type == 41) {
			return WarehouseBill.OUT;
		}
		// 销售退货
		else if (type == 42) {
			return WarehouseBill.IN;
		}
		//调仓单
		else if(type == 21){
			if(moveOut!=null&&moveOut==1){
				return WarehouseBill.MOVE_OUT;
			}else{
				return WarehouseBill.MOVE_IN;
			}
		}
		return WarehouseBill.IN;
	}

	/**
	 * 详情
	 */
	@RequestMapping("/exportDetail")
	public void exportDetail(GoodsStockVo vo, HttpServletResponse response) throws Exception {
		PageParamater pageParamater = new PageParamater();
		pageParamater.setPage(1);//导出所有
		pageParamater.setRows(Integer.MAX_VALUE);

		List<GoodsStockDetailVo> list = webService.getDetailList(vo, pageParamater);

		BigDecimal lastAccountQuentity = new BigDecimal(vo.getLastAccountQuentity());
		BigDecimal accountPrice = vo.getAccountPrice();

		BigDecimal lastAccountAmount = BigDecimal.ZERO;
		for(GoodsStockDetailVo detail:list){
			double qurntity = Double.parseDouble(detail.getQuentity());
			int type = detail.getType();
			Short moveOut = detail.getMoveOut();
			BigDecimal detailAccountQuentity =  new BigDecimal(detail.getQuentity());
			Integer calTotal = vo.getCalTotal();

			int checkOut = checkInOut(qurntity,type,moveOut);
			if(checkOut==WarehouseBill.IN){
				lastAccountQuentity = lastAccountQuentity.add(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}else if(checkOut==WarehouseBill.OUT){
				lastAccountQuentity = lastAccountQuentity.subtract(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}else if(checkOut==WarehouseBill.MOVE_IN&&(calTotal==null||calTotal==0)){
				lastAccountQuentity = lastAccountQuentity.add(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}else if(checkOut==WarehouseBill.MOVE_OUT&&(calTotal==null||calTotal==0)){
				lastAccountQuentity = lastAccountQuentity.subtract(detailAccountQuentity);
				lastAccountAmount = NumberUtil.multiply(lastAccountQuentity, accountPrice);

			}
			detail.setAccountCostAmount(NumberUtil.bigDecimalToStr(NumberUtil.stripTrailingZeros(lastAccountAmount)));
			detail.setAccountQuentity(NumberUtil.bigDecimalToStr(NumberUtil.stripTrailingZeros(lastAccountQuentity)));
		}
		
		//过滤查询条件
		List<GoodsStockDetailVo> results = Lists.newArrayList();
		
		for(GoodsStockDetailVo detail:list){
			
			boolean flag = true;
			if(vo.getBillType()!=null){
				if(detail.getType()==null || detail.getType()!=vo.getBillType()){
					flag = false;
				}
			}
			if(vo.getIntoutTag()!=null){
				double qurntity = Double.parseDouble(detail.getQuentity());
				int type = detail.getType();
				Short moveOut = detail.getMoveOut();
				int inOutCheck = checkInOut(qurntity,type,moveOut);
				
				if(inOutCheck!=vo.getIntoutTag()){
					flag = false;
				}
			}
			if(flag==true)results.add(detail);
		}
		
		try {
			ExcelUtils.exportExcel(GoodsStockDetailVo.class, results, "货品库存明细.xls", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
