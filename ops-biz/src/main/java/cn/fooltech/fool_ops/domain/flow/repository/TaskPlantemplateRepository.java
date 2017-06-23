package cn.fooltech.fool_ops.domain.flow.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.domain.flow.entity.PlanGoods;
import cn.fooltech.fool_ops.domain.flow.entity.TaskPlantemplate;
import cn.fooltech.fool_ops.domain.flow.vo.PlanGoodsVo;
import cn.fooltech.fool_ops.utils.SecurityUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface TaskPlantemplateRepository
		extends FoolJpaRepository<TaskPlantemplate, String>, FoolJpaSpecificationExecutor<TaskPlantemplate> {


}
