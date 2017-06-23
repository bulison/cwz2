package cn.fooltech.fool_ops.domain.message.repository;

import cn.fooltech.fool_ops.component.core.FoolJpaRepository;
import cn.fooltech.fool_ops.component.core.FoolJpaSpecificationExecutor;
import cn.fooltech.fool_ops.component.core.PageParamater;
import cn.fooltech.fool_ops.domain.message.entity.Message;
import cn.fooltech.fool_ops.domain.message.sender.factory.SenderCode;
import cn.fooltech.fool_ops.domain.message.vo.TodoVo;
import cn.fooltech.fool_ops.utils.DateUtilTools;
import cn.fooltech.fool_ops.utils.PredicateUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface MessageRepository extends FoolJpaRepository<Message, String>,
	FoolJpaSpecificationExecutor<Message> {

	/**
	 * 根据参数查找分页
	 * @param receiverId
	 * @param type
	 * @param status
	 * @param lastEndTime
	 * @param page
	 * @return
	 */
	public default Page<Message> findPageBy(String receiverId, Integer type, Integer status, 
			String lastEndTime, String accId, String title, Pageable page){
		
			return findAll(new Specification<Message>() {
				
				@Override
				public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
					List<Predicate> predicates = Lists.newArrayList();
					predicates.add(builder.equal(root.get("sendType"), SenderCode.INNER));
					predicates.add(builder.equal(root.get("receiverId"), receiverId));
					if(!Strings.isNullOrEmpty(lastEndTime)){
						Date endTime = DateUtilTools.string2Time(lastEndTime);
						predicates.add(builder.lessThan(root.get("sendTime"), endTime));
					}
					if(!Strings.isNullOrEmpty(accId)){
						predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));
					}
					if(type !=null&&type!=-1){
						predicates.add(builder.equal(root.get("type"), type));
					}
					
					if(status!=null&&status!=-1){
						predicates.add(builder.equal(root.get("status"), status));
					}else{
						//消息状态为未停用
						predicates.add(builder.notEqual(root.get("status"), Message.STATUS_SUSPEND));
					}
					
					//关键字查找
					if(!Strings.isNullOrEmpty(title)){
						predicates.add(builder.notEqual(root.get("title"), PredicateUtils.getAnyLike(title)));
					}
					return builder.and(predicates.toArray(new Predicate[] {}));
				}
			}, page);
		
	}

	/**
	 * 找出所有未读的消息
	 * @param receiverId 接收人
	 * @param accId 账套iID
	 */
	@Query("select m from Message m where m.receiverId=?1 and m.fiscalAccount.fid=?2 and m.status="+Message.STATUS_UNREAD)
	public List<Message> findRelatedUnreadMsg(String receiverId, String accId);

	/**
	 * 查找未通知的消息
	 * @param receiverId
	 * @param accId
	 * @return
	 */
	public default List<Message> findUnNotifyMessage(String receiverId, String accId){
		return findAll(new Specification<Message>() {

			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("isnotify"), Boolean.FALSE));
				predicates.add(builder.equal(root.get("status"), Message.STATUS_SUSPEND));
				predicates.add(builder.equal(root.get("sendType"), SenderCode.INNER));
				predicates.add(builder.equal(root.get("receiverId"), receiverId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		});
	}

	/**
	 * 查找未通知的消息
	 * @param receiverId
	 * @param accId
	 * @return
	 */
	public default long countUnReadMessage(String receiverId, String accId){
		Long count = count(new Specification<Message>() {

			@Override
			public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				predicates.add(builder.equal(root.get("status"), Message.STATUS_UNREAD));
				predicates.add(builder.equal(root.get("sendType"), SenderCode.INNER));
				predicates.add(builder.equal(root.get("receiverId"), receiverId));
				predicates.add(builder.equal(root.get("fiscalAccount").get("fid"), accId));

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		});
		if(count==null)return 0L;
		return count;
	}
}	

