package cn.fooltech.fool_ops.web.basedata;

import cn.fooltech.fool_ops.domain.basedata.service.VehicleInformationService;
import cn.fooltech.fool_ops.domain.basedata.vo.VehicleInformationVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆信息接口
 */
@RestController
@RequestMapping(value = "/api/vehicleInformation")
public class VehicleInformationApiController {

    @Autowired
    private VehicleInformationService vehicleInformationService;

    @GetMapping("/vagueSearch")
    @ApiOperation("模糊查询(根据可按车牌号、行驶证号、车主姓名、车主身份证、联系电话等进行模糊查询)")
    public List<VehicleInformationVo> vagueSearch(VehicleInformationVo vo){
        return vehicleInformationService.vagueSearch(vo);
    }

    @PostMapping("/vagueSearch2")
    @ApiOperation("仅测试模糊查询2(根据可按车牌号、行驶证号、车主姓名、车主身份证、联系电话等进行模糊查询)")
    public List<VehicleInformationVo> vagueSearch2(VehicleInformationVo vo){
        return vehicleInformationService.vagueSearch(vo);
    }
}
