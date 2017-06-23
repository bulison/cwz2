package cn.fooltech.fool_ops.web.rate;

/**
 * Created by xjh on 2017/3/22.
 */

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.domain.rate.service.RateMemberService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *  收益率Controller
 */
@Controller
@RequestMapping(value = "/rate")
public class RateController {

    /**
     * 销售分析
     * @return
     */
    @RequestMapping(value = "/xsam")
    public String xsam(){
        return "/report/saleAnalysis/manage";
    }
    /**
     * 销售出库单明细弹窗
     * @return
     */
    @RequestMapping(value = "/xsckad")
    public String xsckad(){
        return "/report/saleAnalysis/window";
    }
    
    /**
     * 货品利润分析
     * @return
     */
    @RequestMapping(value = "/hpam")
    public String hpam(){
        return "/rate/goodsRateAnalysis/manage";
    }
    
    /**
     * 货品利润分析
     * @return
     */
    @RequestMapping(value = "/detailPage")
    public String detailPage(){
        return "/rate/goodsRateAnalysis/detail";
    }
    
    /**
     * 流程收益率分析
     * @return
     */
    @RequestMapping(value = "/flow")
    public String flow(){
        return "/rate/flow/manage";
    }
    
    /**
     * 流程收益率分析
     * @return
     */
    @RequestMapping(value = "/flowDetail")
    public String flowDetail(){
        return "/rate/flow/detail";
    }

}
