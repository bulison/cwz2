package cn.fooltech.fool_ops.domain.basedata.vo;

import java.util.List;

import com.google.common.collect.Lists;

import cn.fooltech.fool_ops.utils.tree.FastTreeVo;
import lombok.Data;

@Data
public class FastStorehousesVo extends FastTreeVo<FastStorehousesVo> {
	private String fid;
	private String code;
	private String name;
	private String parentId;
	private List<FastStorehousesVo> children = Lists.newArrayList();
	@Override
	public String getId() {
		return fid;
	}
	@Override
	public String getText() {
		return code +" "+name;
	}
}
