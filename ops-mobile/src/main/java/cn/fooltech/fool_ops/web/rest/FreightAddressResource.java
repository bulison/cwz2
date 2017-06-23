package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.redis.RedisService;
import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.domain.basedata.BaseConstant;
import cn.fooltech.fool_ops.domain.basedata.vo.CustomerVo;
import cn.fooltech.fool_ops.domain.freight.service.FreightAddressService;
import cn.fooltech.fool_ops.domain.freight.vo.FastFreightAddressVo;
import cn.fooltech.fool_ops.domain.freight.vo.FreightAddressVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.alibaba.fastjson.TypeReference;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 货运地址
 * Created by xjh
 */
@RestController
@RequestMapping("/api/freightAddress")
public class FreightAddressResource extends AbstractBaseResource {

    @Autowired
    private FreightAddressService addressService;

    @Autowired
    private RedisService redisService;


    /**
     * 获取企业下所有货运地址单位
     *
     * @return
     */
    @ApiOperation("获取企业下所有货运地址单位")
    @GetMapping("/queryAddress")
    public ResponseEntity queryAddress(FreightAddressVo vo) {
        List<FastFreightAddressVo> addresses = addressService.getFastTree(vo);
        return listReponse(addresses);
    }

    /**
     * 获取企业下所有货运地址单位
     *
     * @return
     */
    @ApiOperation("获取企业下所有货运地址单位，此接口需要缓存数据，不应该有查询参数，默认不显示无效地址")
    @GetMapping("/query")
    public ResponseEntity query() {

        String orgId = SecurityUtil.getCurrentOrgId();
        String accId = SecurityUtil.getFiscalAccountId();

        String scene = orgId + ":" + accId;
        String key = BaseConstant.ADDRESS + ":" + scene;

        FreightAddressVo vo = new FreightAddressVo();
        vo.setEnable(Constants.SENABLE);

        List<FastFreightAddressVo> addresses = redisService.get(String.valueOf(key),
                new TypeReference<List<FastFreightAddressVo>>() {});
        if (addresses == null) {
            addresses = addressService.getFastTree(vo);
            redisService.set(key, addresses, BaseConstant.timeout);
        }

        return listReponse(addresses);
    }

}
