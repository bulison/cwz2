package cn.fooltech.fool_ops.taglib;

import cn.fooltech.fool_ops.component.core.SpringBeanUtils;
import cn.fooltech.fool_ops.domain.flow.entity.Plan;
import cn.fooltech.fool_ops.domain.flow.service.PlanService;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;
import java.util.Set;


/**
 * 判断某个用户是否有权限
 * @author xjh
 *
 */
public class PlanSecure extends TagSupport {				

	private static final long serialVersionUID = 7142696261166754344L;
	
	/**
	 * 事件级别
	 */
	private String secureCodes;
	
	/**
	 * 计划ID
	 */
	private String planId;
	
	
	@Override
	public int doStartTag() throws JspException {
		if(Strings.isNullOrEmpty(secureCodes)||Strings.isNullOrEmpty(planId))return Tag.SKIP_BODY;
		String userId = SecurityUtil.getCurrentUserId();
		Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
		
		PlanService planService = (PlanService) SpringBeanUtils.getBean(PlanService.class);
		Plan plan = planService.get(planId);
		
		List<String> authStrs = splitter.splitToList(secureCodes);
		Set<String> authIds = planService.getUserIds(plan, authStrs);
		if(authIds.contains(userId)){
			return Tag.EVAL_BODY_INCLUDE;
		}else{
			return Tag.SKIP_BODY;
		}
	}


	public String getSecureCodes() {
		return secureCodes;
	}


	public void setSecureCodes(String secureCodes) {
		this.secureCodes = secureCodes;
	}


	public String getPlanId() {
		return planId;
	}


	public void setPlanId(String planId) {
		this.planId = planId;
	}

}
