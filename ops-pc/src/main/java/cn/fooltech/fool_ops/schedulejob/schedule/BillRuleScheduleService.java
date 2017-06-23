package cn.fooltech.fool_ops.schedulejob.schedule;

import cn.fooltech.fool_ops.domain.basedata.entity.BillRule;
import cn.fooltech.fool_ops.domain.basedata.service.BillRuleService;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单据单号生产规则定时器
 * 每天00:00:01更新格式=yyyyMMdd的延时单号为1，判断是否1号，是则更新格式=yyyyMM的延时单号为1
 * @author xjh
 *
 */
@Service("ops.BillRuleScheduleService")
public class BillRuleScheduleService implements OpsSchedule {

	/**
	 * 单据单号生成规则服务类
	 */
	@Autowired
	private BillRuleService ruleService;
	
	@Override
	public void execute() {
		if(DateUtilTools.todayIsFirstInMonth()){
			ruleService.updateAllLazyCode(null);
		}else{
			List<String> formats = Lists.newArrayList(BillRule.DATE_YYMMDD, BillRule.DATE_YYYYMMDD);
			ruleService.updateAllLazyCode(formats);
		}
	}

}
