package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.domain.basedata.service.CustomerAddressService;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户供应商默认地址
 * Created by xjh on 2016/12/31.
 */
@RestController
@RequestMapping("/api/customerAddress")
public class CustomerAddressResource extends AbstractBaseResource{

    @Autowired
    private CustomerAddressService customerAddressService;

    @ApiOperation("根据收支单位（客户ID/供应商ID）获取默认地址，没有则返回空")
    @GetMapping("/getByCsvId")
    public FreightAddressVo getFreightAddressByCsvId(@RequestParam String csvId) {
        return customerAddressService.getFreightAddressByCsvId(csvId);
    }

    @ApiOperation("根据收支单位（客户ID/供应商ID）获取地址")
    @GetMapping("/getListByCsvId")
    public ResponseEntity getListByCsvId(@RequestParam String csvId) {
        return listReponse(customerAddressService.query(csvId));
    }
}
