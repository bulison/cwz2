package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.domain.basedata.dao.PurchasePriceReportDao;
import cn.fooltech.fool_ops.domain.basedata.vo.PurchasePriceVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/purchasePriceReport")
public class PurchasePriceReportController {

	@Autowired
	private PurchasePriceReportDao dao;

	/**
	 * 货品价格报价分析页面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage() {
		return "/report/purchasePriceAnalysis/report";
	}
	
	/**
	 * 货品价格报价曲线图页面
	 * @return
	 */
	@RequestMapping(value = "/chart")
	public String charts() {
		return "/report/purchasePriceAnalysis/chart";
	}


	/**
	 * 导出
	 * @date 2015年9月23日
	 */
	@RequestMapping(value="/export")
	public void export(PurchasePriceVo vo, HttpServletResponse response) throws Exception{

		if(vo.getStartDay()!=null){
			vo.setStartDay(DateUtilTools.changeDateTime(vo.getStartDay(), 0, 0, 0, 0, 0));
		}
		if(vo.getEndDay()!=null){
			vo.setEndDay(DateUtilTools.changeDateTime(vo.getEndDay(), 0, 0, 23, 59, 59));
		}

		vo.setAccId(SecurityUtil.getFiscalAccountId());
		List<PurchasePriceVo> datas = dao.queryReport(vo);

		try {

			ExcelUtils.exportExcel(PurchasePriceVo.class, datas, "货品价格报价.xls", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 货品价格报价查看图片
	 * @return
	 */
	@RequestMapping(value = "/showPic")
	public String showPic() {
		return "/showPic/showPic";
	}
}
