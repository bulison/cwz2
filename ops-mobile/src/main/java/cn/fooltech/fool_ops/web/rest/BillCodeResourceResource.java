package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 同行报价
 * Created by xjh
 */
@RestController
@RequestMapping("/api/billCode")
public class BillCodeResourceResource extends AbstractBaseResource {

    @Autowired
    private BillRuleService billRuleService;

    /**
     * 获取新单号
     *
     * @param billType
     * @return
     */
    @ApiOperation("获取新单号")
    @GetMapping("/getNewCode/{billType}")
    public ResponseEntity getNewCode(@PathVariable Integer billType) {
        String newCode = billRuleService.getNewCode(billType);
        RequestResult result = new RequestResult();
        result.setData(newCode);
        result.setReturnCode(RequestResult.RETURN_SUCCESS);
        return ResponseEntity.ok(result);
    }

}
