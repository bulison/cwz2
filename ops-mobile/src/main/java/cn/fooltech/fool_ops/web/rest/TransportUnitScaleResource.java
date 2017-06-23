package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 根据运输单位获取估算的运算关系
 * Created by xjh on 2017/3/21.
 */
@RestController
@RequestMapping("/api/transportUnit")
public class TransportUnitScaleResource extends AbstractBaseResource {

    @Autowired
    private AuxiliaryAttrService attrService;

    @ApiOperation("根据运输单位ID查询相应属性")
    @GetMapping("/findByUnitId")
    public ResponseEntity findByUnitId(@RequestParam String transportUnitId){
        AuxiliaryAttrVo attr = attrService.getByFid(transportUnitId);
        return reponse(RequestResult.ok(attr));
    }
}
