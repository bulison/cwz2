package cn.fooltech.fool_ops.web.cashier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.cashier.entity.BankCheckdate;
import cn.fooltech.fool_ops.domain.cashier.service.BankCheckdateService;
import cn.fooltech.fool_ops.domain.cashier.vo.BankCheckdateVo;
import cn.fooltech.fool_ops.web.base.BaseController;



/**
 * 轧账日期网页控制类
 * @author lgk
 * @date 2015年12月22日上午9:48:38
 * @version V1.0
 */
@Controller
@RequestMapping(value = "/cashierBankCheckdateController")
public class BankCheckdateController extends BaseController{
    @Autowired
	private BankCheckdateService bankCheckdateWebService;
    
    /**
     * 轧帐管理页面
     * @return
     */
    @RequestMapping(value="/goManager")
    public String goCheckdate(){
    	return "/cashier/checkdate/manager";
    }
    /**
     * 结账列表
     * @param bankCheckdateVo
     * @param paramater
     * @return
     */
    @RequestMapping(value="/query")
    @ResponseBody
    public PageJson query(BankCheckdateVo bankCheckdateVo,PageParamater paramater){
    	Page<BankCheckdate> page = bankCheckdateWebService.query(bankCheckdateVo, paramater);
    	PageJson pageJson = new PageJson();
    	pageJson.setTotal(page.getTotalElements());
    	pageJson.setRows(bankCheckdateWebService.getVos(page.getContent()));
        return pageJson;
    }
    /**
     * 轧账
     * @param vo
     * @return
     */
    @RequestMapping(value="/save")
    @ResponseBody
    public RequestResult save(BankCheckdateVo vo){
    	return bankCheckdateWebService.save(vo);
    }
    /**
     * 反轧帐
     * @return
     */
    @RequestMapping(value="/cancel")
    @ResponseBody
    public RequestResult cancel(){
    	return bankCheckdateWebService.cancle();
    }

}
