delete from tsb_cost_billcheck where FACC_ID=':FACC_ID';
delete from tsb_cost_bill where FACC_ID=':FACC_ID';
delete from tsb_payment_check where FACC_ID=':FACC_ID';
delete from tsb_payment_bill where FACC_ID=':FACC_ID';

delete a from tsb_period_stock_amount a,tbd_stock_period b where a.FSTOCK_PERIOD_ID=b.FID and b.FACC_ID=':FACC_ID';
delete a from tsb_period_amount as a left join tbd_stock_period as b on a.FSTOCK_PERIOD_ID=b.FID where b.FACC_ID=':FACC_ID';
delete a from tsb_storage_out as a left join tsb_warehouse_billdetail as b  on a.FBILL_DETAIL_IN_ID=b.FID left join tsb_warehouse_bill as c on b.FWAREHOUSE_BILL_ID=c.FID  where c.FACC_ID=':FACC_ID';
delete a from tsb_storage_in as a left join tsb_warehouse_billdetail as b  on a.FBILL_DETAIL_ID=b.FID left join tsb_warehouse_bill as c on b.FWAREHOUSE_BILL_ID=c.FID  where c.FACC_ID=':FACC_ID';
delete a from tsb_bill_relation as a left join tsb_warehouse_bill as b on a.FBILL_ID=B.FID where b.FACC_ID=':FACC_ID';
delete a from tsb_warehouse_billdetail as a left join tsb_warehouse_bill as b on a.FWAREHOUSE_BILL_ID=B.FID where b.FACC_ID=':FACC_ID';
delete from tsb_warehouse_bill where FACC_ID=':FACC_ID';
delete from tsb_initial_bank where FORG_ID=':FORG_ID';
delete from tsb_bill_pricing where FACC_ID=':FACC_ID';
delete from tsb_stock_store where FACC_ID=':FACC_ID';
delete from tsb_bom where FACC_ID=':FACC_ID';
delete from tsb_expense_apply_bill where FACC_ID=':FACC_ID';


delete from tsb_cost_analysis_billdetail where FACC_ID=':FACC_ID';
delete from tsb_cost_analysis_bill where FACC_ID=':FACC_ID';
delete a from tcm_attach as a left join tflow_plan as b on a.FBUS_ID=B.FID where b.FACC_ID=':FACC_ID';
delete a from tcm_attach as a left join tflow_task as b on a.FBUS_ID=B.FID where b.FACC_ID=':FACC_ID';
delete a from tflow_attach as a left join tflow_task as b on a.FPLAN_ID=B.FID where b.FACC_ID=':FACC_ID';
delete a from tflow_attach as a left join tflow_plan as b on a.FPLAN_ID=B.FID where b.FACC_ID=':FACC_ID';
delete from tflow_task where FACC_ID=':FACC_ID';
delete from tflow_plan where FACC_ID=':FACC_ID';
delete a from tflow_plan_template_detail a inner join tflow_plan_template b on b.fid=a.FPLAN_TEMPLATE_ID where b.FACC_ID=':FACC_ID'
delete from tflow_plan_template where FACC_ID=':FACC_ID';
delete from tflow_plan_goods_detail where FACC_ID=':FACC_ID';
delete from tflow_plan_goods where FACC_ID=':FACC_ID';
delete from tflow_task_plantemplate_detail where FACC_ID=':FACC_ID';
delete from tflow_task_plantemplate where FACC_ID=':FACC_ID';
delete from tflow_task_bill where FACC_ID=':FACC_ID';
delete from tflow_plan_template_relation where FACC_ID=':FACC_ID';
delete from tsb_purchase_price where FACC_ID=':FACC_ID';
delete from tsb_peer_quote where FACC_ID=':FACC_ID';
delete from tsb_transport_price_detail2 where FACC_ID=':FACC_ID';
delete from tsb_transport_price_detail1 where FACC_ID=':FACC_ID';
delete from tsb_transport_price where FACC_ID=':FACC_ID';
delete from tsb_ground_price where FACC_ID=':FACC_ID';

delete from tcapital_plan where FACC_ID=':FACC_ID';
delete from tcapital_plan_bill where FACC_ID=':FACC_ID';
delete from tcapital_plan_change_log where FACC_ID=':FACC_ID';
delete from tcapital_plan_detail where FACC_ID=':FACC_ID';
delete from tflow_rank where FACC_ID=':FACC_ID';
delete a from tmc_message_paramater a inner join  tmc_message b on a.FMESSAGE_ID=b.fid where b.FACC_ID=':FACC_ID';
delete from tmc_message where FACC_ID=':FACC_ID';
delete from rate_member where FACC_ID=':FACC_ID';

