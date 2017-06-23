package cn.fooltech.fool_ops.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.member.service.MemberService;
import cn.fooltech.fool_ops.domain.member.vo.MemberVo;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * <p> 人员 </p>
 * @author cwz
 * @date 2017年6月13日
 */
@RestController
@RequestMapping(value = "/api/member")
public class MemberResource extends AbstractBaseResource {

	private static final String NameSpace = "member";
	@Autowired
	private MemberService memberService;

	@ApiOperation("获取人员列表")
	@GetMapping("/query")
	public ResponseEntity query(MemberVo memberVo, PageParamater paramater) {
		Page<MemberVo> query = memberService.query(memberVo, paramater);
		return pageReponse(NameSpace, query);
	}

}
