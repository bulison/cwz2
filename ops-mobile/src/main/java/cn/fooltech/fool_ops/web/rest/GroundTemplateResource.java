package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.domain.basedata.service.GroundTemplateService;
import cn.fooltech.fool_ops.domain.freight.entity.FreightAddress;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 场地报价
 * Created by xjh
 */
@RestController
@RequestMapping("/api/groundTemplate")
public class GroundTemplateResource extends AbstractBaseResource {

    @Autowired
    private GroundTemplateService groundTemplateService;



    @ApiOperation("场地报价模板")
    @GetMapping("/query")
    public ResponseEntity query(String searchKey, String groundId, String addressId) {
        ResponseEntity result = null;
        try {
            result = listReponse(groundTemplateService.query(searchKey, groundId, addressId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
