package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.domain.basedata.service.AuxiliaryAttrService;
import cn.fooltech.fool_ops.domain.basedata.vo.AuxiliaryAttrVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 辅助属性
 * Created by xjh on 2016/12/19.
 */
@RestController
@RequestMapping("/api/auxiliaryAttr")
public class AuxiliaryAttrResource {

    /**
     * 辅助属性网页服务类
     */
    @Autowired
    private AuxiliaryAttrService attrService;

    /**
     * 获取属性列表
     *
     * @return
     */
    @ApiOperation("获取属性列表")
    @GetMapping("/subAuxiliary/{typeCode}")
    public ResponseEntity subAuxiliary(@PathVariable String typeCode) {
        List<AuxiliaryAttrVo> vos = attrService.findByCategoryCode(typeCode);
        return ResponseEntity.ok().body(vos);
    }
}
