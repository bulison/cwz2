package cn.fooltech.fool_ops.web.rate;

import cn.fooltech.fool_ops.component.core.PageJson;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.rate.service.LoanRateLogService;
import cn.fooltech.fool_ops.domain.rate.service.LoanRateService;
import cn.fooltech.fool_ops.domain.rate.vo.LoanRateLogVo;
import cn.fooltech.fool_ops.domain.rate.vo.LoanRateVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by admin on 2017/3/20.
 */
@RestController
@RequestMapping("/api/rate/loanrate")
public class LoanRateApiController {
    @Autowired
    LoanRateService loanRateWebService;
    @Autowired
    LoanRateLogService loanRateLogWebService;

    @ApiOperation("保存央行贷款利率，数值为利率*100")
    @PutMapping("/save")
    public RequestResult save(LoanRateLogVo vo) {
        vo.setIncrease("0");//增幅写入0
        LoanRateVo loanRateVo=loanRateWebService.getVoByLogVo(vo);
        RequestResult result=loanRateWebService.update(loanRateVo);
        if (result.isSuccess())
            return loanRateLogWebService.save(vo);
        else
            return  result;
    }

    @ApiOperation("获取央行贷款利率，数值为利率*100")
    @GetMapping("/queryRate")
    public PageJson query(LoanRateVo vo, PageParamater paramater) throws Exception{
        return new PageJson(loanRateWebService.query(vo, paramater));
    }

    @ApiOperation("获取央行贷款利率数据日志，数值为利率*100")
    @GetMapping("/queryLog")
    public PageJson queryLog(LoanRateLogVo vo, PageParamater paramater) throws Exception{
        return new PageJson(loanRateLogWebService.query(vo, paramater));
    }

    /*@ApiOperation("删除数据")
    @DeleteMapping("/delete")
    public RequestResult delete(String fid){
        return loanRateLogWebService.delete(fid);
    }*/

    @ApiOperation("获取央行贷款利率值")
    @GetMapping("/queryRateValue")
    public double query(){
        return loanRateWebService.getBankRate(SecurityUtil.getCurrentOrgId());
    }
}
