package cn.fooltech.fool_ops.web.rest;


import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.component.core.RequestResult;
import cn.fooltech.fool_ops.domain.basedata.service.VehicleInformationService;
import cn.fooltech.fool_ops.domain.basedata.vo.VehicleInformationVo;
import cn.fooltech.fool_ops.domain.common.service.AttachService;
import cn.fooltech.fool_ops.domain.common.vo.Base64Img;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *
 */
@RestController
@RequestMapping("/api/attach")
public class AttachResource extends AbstractBaseResource {

    private static final String NameSpace = "attach";

    @Autowired
    private AttachService attachService;

    /**
     * 获取base64格式图片
     * @param busId
     * @return
     */
    @GetMapping("/getImgs")
    @ApiOperation("获取base64格式图片")
    public List<Base64Img> getImgs(String busId) {
        return attachService.getImgs(busId);
    }
}
