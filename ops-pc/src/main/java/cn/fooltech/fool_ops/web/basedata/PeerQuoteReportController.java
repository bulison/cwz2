package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.domain.basedata.dao.PeerQuoteReportDao;
import cn.fooltech.fool_ops.domain.basedata.vo.PeerQuoteVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.ExcelUtils;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/peerQuoteReport")
public class PeerQuoteReportController {

	@Autowired
	private PeerQuoteReportDao dao;
	/**
	 * 同行报价分析页面
	 * @return
	 */
	@RequestMapping(value = "/manage")
	public String manage() {
		return "/report/peerQuoteAnalysis/report";
	}
	
	/**
	 * 同行报价分析页面
	 * @return
	 */
	@RequestMapping(value = "/chart")
	public String charts() {
		return "/report/peerQuoteAnalysis/chart";
	}

	/**
	 * 导出
	 * @date 2015年9月23日
	 */
	@RequestMapping(value="/export")
	public void export(PeerQuoteVo vo, HttpServletResponse response) throws Exception{

		if(vo.getStartDay()!=null){
			vo.setStartDay(DateUtilTools.changeDateTime(vo.getStartDay(), 0, 0, 0, 0, 0));
		}
		if(vo.getEndDay()!=null){
			vo.setEndDay(DateUtilTools.changeDateTime(vo.getEndDay(), 0, 0, 23, 59, 59));
		}
		vo.setAccId(SecurityUtil.getFiscalAccountId());
		List<PeerQuoteVo> datas = dao.queryReport(vo);

		try {

			ExcelUtils.exportExcel(PeerQuoteVo.class, datas, "同行报价.xls", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
