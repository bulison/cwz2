package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.domain.basedata.service.GroundTemplateDetailService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 场地报价模板明细
 * Created by xjh
 */
@RestController
@RequestMapping("/api/groundTemplateDetail")
public class GroundTemplateDetailResource extends AbstractBaseResource {

    @Autowired
    private GroundTemplateDetailService detailService;

    @Autowired
    private FreightAddressService addressService;

    @ApiOperation("场地报价模板明细")
    @GetMapping("/queryByTemplateId")
    public ResponseEntity queryByTemplateId(String templateId) {
        return listReponse(detailService.findByTemplateId(templateId));
    }

    @ApiOperation("场地报价模板")
    @GetMapping("/queryByGroundId")
    public ResponseEntity queryByGroundId(String groundId) {
        return listReponse(detailService.findByGroundId(groundId));
    }

    @ApiOperation("根据地址ID获取场地报价模板")
    @GetMapping("/queryByAddressId")
    public ResponseEntity queryByAddressId(String addressId) {
        ResponseEntity result = null;
        try {
            FreightAddress address = addressService.get(addressId);
            String groundId = address.getGround().getFid();
            result = listReponse(detailService.findByGroundId(groundId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
