package cn.fooltech.fool_ops.web.rest;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.VehicleInformationService;
import cn.fooltech.fool_ops.domain.basedata.vo.VehicleInformationVo;
import cn.fooltech.fool_ops.domain.common.vo.Base64Img;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *	车辆信息
 *
 */
@RestController
@RequestMapping("/api/vehicleInformation")
public class VehicleInformationResource extends AbstractBaseResource {

    private static final String NameSpace = "vehicleInformation";

    @Autowired
    private VehicleInformationService vehicleInformationService;

    @ApiOperation("新增/修改")
    @PostMapping("/save")
    public ResponseEntity save(@RequestBody VehicleInformationVo vo) {
        RequestResult result = vehicleInformationService.save(vo);
        return reponse(result);
    }

    @ApiOperation("查询分页")
    @GetMapping("/query")
    public ResponseEntity query(VehicleInformationVo vo, PageParamater paramater) {
        return pageReponse(NameSpace, vehicleInformationService.query(vo, paramater));
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        return reponse(vehicleInformationService.delete(id));
    }

    @GetMapping("/vagueSearch")
    @ApiOperation("模糊查询(根据可按车牌号、行驶证号、车主姓名、车主身份证、联系电话等进行模糊查询)")
    public List<VehicleInformationVo> vagueSearch(VehicleInformationVo vo) {
        return vehicleInformationService.vagueSearch(vo);
    }

    /**
     * 前端未查出问题，只能先用post
     * @param vo
     * @return
     */
    @PostMapping("/vagueSearch2")
    @ApiOperation("模糊查询(根据可按车牌号、行驶证号、车主姓名、车主身份证、联系电话等进行模糊查询)")
    public List<VehicleInformationVo> vagueSearch2(VehicleInformationVo vo) {
        return vehicleInformationService.vagueSearch(vo);
    }
}
