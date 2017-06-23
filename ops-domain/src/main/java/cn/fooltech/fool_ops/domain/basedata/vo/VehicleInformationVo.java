package cn.fooltech.fool_ops.domain.basedata.vo;


import cn.fooltech.fool_ops.domain.basedata.entity.VehicleInformation;
import cn.fooltech.fool_ops.config.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

@ApiModel("")
@Data
public class VehicleInformationVo extends VehicleInformation {

    @ApiModelProperty(value = "开始日期，查询用")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private String startDay;

    @ApiModelProperty(value = "结束日期，查询用")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDay;

    /**
     * 记录状态
     */
    @ApiModelProperty(value = "记录状态")
    private String recordStatus = VehicleInformation.STATUS_SAC;
    /**
     * 搜索关键字
     */
    @ApiModelProperty(value = "搜索关键字")
    private String searchKey;
    /**
     * 模糊搜索结果集大小
     */
    @ApiModelProperty(value = "模糊搜索结果集大小")
    private Integer searchSize = Constants.VAGUE_SEARCH_SIZE;

    @ApiModelProperty(value = "图片base64的json字符串")
    private String base64Str;
}