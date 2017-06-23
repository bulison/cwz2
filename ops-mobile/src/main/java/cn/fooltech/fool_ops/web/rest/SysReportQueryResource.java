package cn.fooltech.fool_ops.web.rest;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.common.entity.ResultObject;
import cn.fooltech.fool_ops.domain.report.service.SysReportQueryService;
import cn.fooltech.fool_ops.domain.report.vo.SysReportQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Derek on 2017/6/6.
 */
@RestController
@RequestMapping("/api/report")
public class SysReportQueryResource {

    /**
     * 系统报表查询网页服务类
     */
    @Autowired
    private SysReportQueryService queryService;

    /**
     * 分页查询
     * @param vo
     * @param paramater
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity query(SysReportQueryVo vo, PageParamater paramater){
        ResultObject result = queryService.query(vo, paramater);
        return ResponseEntity.ok().body(result);
    }
}
