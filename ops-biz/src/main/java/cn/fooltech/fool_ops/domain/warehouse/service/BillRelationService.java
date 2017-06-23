package cn.fooltech.fool_ops.domain.warehouse.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import cn.fooltech.fool_ops.domain.base.service.AbstractBaseService;
import cn.fooltech.fool_ops.domain.warehouse.entity.BillRelation;
import cn.fooltech.fool_ops.domain.warehouse.repository.BillRelationRepository;

@Service
public class BillRelationService extends AbstractBaseService<BillRelation, String>{

	private Logger logger = LoggerFactory.getLogger(BillRelationService.class);
	
	@Autowired
	private BillRelationRepository relationRepo;
	
	/**
	 * 获取单据关联(不过滤状态)
	 * @param billId 仓库单据ID
	 */
	public List<BillRelation> getRelation(String billId){
		return relationRepo.findByBillId(billId);
	}
	
	/**
	 * 获取单据关联
	 * @param billId 仓库单据ID
	 */
	public List<BillRelation> getRelation(String billId, int status){
		return relationRepo.findByBillIdAndRecordStatus(billId, status);
	}
	
	/**
	 * 通过被关联的单据获取单据关联
	 * @param refBillId 被关联的单据ID
	 * @return
	 */
	public List<BillRelation> getRelationByRefBill(String refBillId){
		return relationRepo.findByRefBillId(refBillId);
	}

	/**
	 * 根据refBillIds查询仓储单据关联
	 * @param refBillIds
	 * @return
	 */
	public List<BillRelation> findByRefBillIdIn(List<String> refBillIds){
		return relationRepo.findByRefBillIdIn(refBillIds);
	}


	/**
	 * 判断某个单据是否被关联了
	 * @param billId 仓库单据ID
	 * @return true 已被关联   false 未被关联  
	 * @author rqh
	 */
	public boolean isAssociated(String billId){
		
		Long count = relationRepo.countByBillId(billId);
		if(count!=null && count>0){
			return true;
		}
		return false;
	}
	
	/**
	 * 删除单据关联
	 * @param billId 仓库单据ID
	 * @author rqh
	 */
	public void deleteRelation(String billId){
		List<BillRelation> datas = relationRepo.findByBillId(billId);
		relationRepo.delete(datas);
	}
	
	/**
	 * 更新仓库单据关联的状态
	 * @param billId 仓库单据ID
	 * @param status 状态
	 */
	public void updateStatus(String billId, int status){
		List<BillRelation> datas = relationRepo.findByBillId(billId);
		for(BillRelation relation : datas){
			if(relation.getRecordStatus() != status){
				relation.setRecordStatus(status);
				relationRepo.save(relation);
			}
		}
	}
	
	/**
	 * 取消单据关联
	 * @param billId 仓库单据ID
	 */
	public void cancelAssociate(String billId){
		updateStatus(billId, BillRelation.CALCLE);
	}

	@Override
	public CrudRepository<BillRelation, String> getRepository() {
		return relationRepo;
	}
}
