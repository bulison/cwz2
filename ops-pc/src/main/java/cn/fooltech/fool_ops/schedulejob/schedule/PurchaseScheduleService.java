package cn.fooltech.fool_ops.schedulejob.schedule;//package cn.fooltech.fool_ops.domain.schedule;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//
//import cn.fooltech.fool_ops.domain.basedata.entity.GoodsPrice;
//import cn.fooltech.fool_ops.domain.bom.entity.Bom;
//import cn.fooltech.fool_ops.domain.bom.entity.BomDetail;
//import cn.fooltech.fool_ops.domain.flow.entity.MsgWarnSetting;
//import cn.fooltech.fool_ops.domain.flow.entity.Task;
//import cn.fooltech.fool_ops.domain.message.entity.Message;
//import cn.fooltech.fool_ops.domain.sysman.entity.Organization;
//import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBill;
//import cn.fooltech.fool_ops.domain.warehouse.entity.WarehouseBillDetail;
//import cn.fooltech.fool_ops.utils.DateUtilTools;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//
//import com.google.common.base.Strings;
//import com.google.common.collect.Lists;
//
///**
// * 库存不足触发采购计划和升级计划
// * @author xjh
// *
// */
//@Service("ops.PurchaseScheduleService")
//public class PurchaseScheduleService extends BaseScheduleService {
//
//	@Override
//	@Transactional
//	public void execute() {
//
//		List<Organization> topOrgs = orgService.queryAllTopOrg();
//
//		for(Organization org:topOrgs){
//			String orgId = org.getFid();
//
//			List<String> busSceneList = Lists.newArrayList(Task.STATUS_EXECUTING+"", Task.STATUS_DELAYED_UNSTART+"",
//					Task.STATUS_DELAYED_UNFINISH+"");
//
//			List<MsgWarnSetting> settingList = settingService.queryWarnSetting(busSceneList, MsgWarnSetting.TYPE_TASK, orgId);
//
//			for(MsgWarnSetting setting:settingList){
//				Integer status = Integer.parseInt(setting.getBusScene());
//				List<Task> tasks = taskService.queryByStatus(status, orgId);
//
//				for(Task task:tasks){
//					String billId = task.getBillId();
//					if(Strings.isNullOrEmpty(billId)){
//						continue;
//					}
//					WarehouseBill bill = billService.get(task.getBillId());
//
//					if(bill==null)continue;
//
//					//判断是否出库单据
//					if(auditUtil.isOutStorage(bill.getBillType())){
//						List<WarehouseBillDetail> details = bill.getDetails();
//
//						int daym = 0;//采购周期M
//						int days = 0;//生产周期S
//
//						for(WarehouseBillDetail detail:details){
//							String accId = bill.getFiscalAccount().getFid();
//							String goodsId = detail.getGoods().getFid();
//							String specId = detail.getGoodsSpec()==null?"":detail.getGoodsSpec().getFid();
//							Bom defaultBom = bomService.findByDefaultMaterial(null, accId, goodsId, specId);
//							if(defaultBom!=null){//有配方
//
//								Set<BomDetail> bomDetails = defaultBom.getDetails();
//
//								for(BomDetail bomDetail:bomDetails){
//									String bomDoodsId = bomDetail.getGoods().getFid();
//									String bomSpecId = bomDetail.getSpec()==null?"":bomDetail.getSpec().getFid();
//
//									GoodsPrice price = goodsPriceService.get(bomDoodsId, bomSpecId);
//									if(price!=null && price.getPurchasingCycle()!=null){
//										int tday = price.getPurchasingCycle().intValue();
//										daym = Math.max(daym, tday);
//									}
//								}
//
//								GoodsPrice price2 = goodsPriceService.get(goodsId, specId);
//								if(price2!=null && price2.getCapacity()!=null){
//									BigDecimal capacity = price2.getCapacity();
//									int tday = detail.getAccountQuentity().divide(capacity, BigDecimal.ROUND_FLOOR).intValue();
//									days = Math.max(days, tday);
//								}
//
//							}else{//无配方
//								GoodsPrice price = goodsPriceService.get(goodsId, specId);
//								if(price!=null && price.getPurchasingCycle()!=null){
//									int tday = price.getPurchasingCycle().intValue();
//									daym = Math.max(daym, tday);
//								}
//							}
//						}
//
//						//N+M+S
//						int dayDelta = -(setting.getDays()+daym+days);
//						Date planEndDate = task.getAntipateEndTime();
//						Date chufaDate = DateUtilTools.changeDateTime(planEndDate, 0, dayDelta, 0, 0, 0);
//
//						if(DateUtilTools.isToday(chufaDate)){
//							//发送消息
//							try {
//								sendMessage(task, bill, Message.TRIGGER_TYPE_PURCHASE_PRODUCE);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			}
//		}
//
//
//	}
//
//}
