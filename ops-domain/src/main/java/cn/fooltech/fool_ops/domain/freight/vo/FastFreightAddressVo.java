package cn.fooltech.fool_ops.domain.freight.vo;

import cn.fooltech.fool_ops.config.Constants;
import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Derek on 2017/3/14.
 */
@Data
public class FastFreightAddressVo extends FastTreeVo<FreightAddressVo> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fid;
    private String code;//编号
    private String name;//名称
    private String parentId;
    private String assetwarehouseId;//辅助属性仓库Id
    private String assetwarehouseName;//辅助属性仓库名称

    private List<FreightAddressVo> children = Lists.newArrayList();

    @Override
    public String getId() {
        return fid;
    }

    @Override
    public String getText() {
        return code +" "+name;
    }
}
