<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/views/common/header.jsp"%>
	<title>数据清空服务</title>
</head>
<body style="overflow:hidden;">
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>数据格式化</h1>
                </div>             
             </div>
	<div class="form">
		<form id="clearForm">
			<p><font></font><input type="checkbox" id="billinit" class="billinit" name="qx" value="1"/><label for="billinit">单据格式化</label></p>
			<p ><font></font><input type="checkbox" id="finance"  name="qx" value="2" /><label for="finance">财务格式化</label></p>
           	<p ><font></font><input type="checkbox" id="sysinit"  name="qx" value="3" /><label for="sysinit">系统格式化</label></p>
			<p><font></font><a href="javascript:;" id="clear" class="btn-blue btn-s">格式化</a></p>
		</form>
	</div>
	<div id="mybox"></div>
	<%@ include file="/WEB-INF/views/common/js.jsp"%>
	<script type="text/javascript">	
	var tag=[];
	$(function(){
		 $("input[type='checkbox']").click(function(){
			if($(this).attr("checked")==undefined){
				$(this).attr("checked","checked")
				  tag.push($(this).val()); 
			}else{
				$(this).removeAttr("checked"); 
				removeByValue(tag,$(this).val());
			}
		})	
		 
	})
	
function removeByValue(tag,val) {
  for(var i=0; i<tag.length; i++) {
    if(tag[i]== val) {   
    tag.splice(i,1); 
      break;
    }
  }
}
	
	
		//组装Tag	
		function getTag(){
			var qx = $("input[name='qx']:checked").map(function () {
	               return $(this).val();
	           }).get();
			var val = 3;
			if($(qx).length>0){
				$(qx).each(function(){
					val = val && parseInt(this);
				});
			}else{
				val=0;
			}

			return val;
		}
		
		var flag = false;
		$("#clear").click(function(){
			if(flag)return;			
			/* var tag = getTag(); */
			if(tag.length>0){
				 $.messager.confirm('温馨提示', '清空数据不可再恢复，你真的要清空吗?', function(r){
					 if(r){
					 $('#mybox').fool('window',{
							top:300,
							left:$(window).width()/2-150,
							modal:true,
							width:300,
							height:150,
							title:"数据格式化验证",
							href:"${ctx}/indexController/userCheck?callBack=userCheck&type=1"		
						}); 
					 }
				}); 				
			}else{
				$.fool.alert({msg:'至少勾选一项'});
			}
		});
		
		function userCheck(pwd){
			/* var tag = getTag(); */
			clearData(tag,pwd);		
		    $("#mybox").window("close");		    
		}
		
		function clearData(tag,pwd){
			flag = true;
			var url = "${ctx}/data/clear";
			$.ajax({
			    type: 'post',
			    url: url, 
			    dataType: 'json',
			    data: {tag:tag,pwd:pwd},
			    success: function(data){
			    	if(data.returnCode == "0" ){
			    		$.fool.alert({msg:'操作成功'});
					}else if(data.returnCode == "1"){
						$.fool.alert({msg:data.message});
					}else{
						$.fool.alert({msg:"系统繁忙，请稍后再试。"});
					}
			    	flag = false;
			    },
			    error:function(){
			    	flag = false;
			    }
			 });
		}
		$("#billinit").tooltip({
		    position: 'right',
		    content: '<span style="color:#fff">'
		        +'删除系统内进销存中的全部单据。保留基础数据。'
		        /*+'删除系统内进销存中的全部单据。保留基础数据。<br/>'
		    	+'删除 费用单勾对表（tsb_cost_billcheck）<br/>'
		    	+'删除 费用单表（tsb_cost_bill）<br/>'
		    	+'删除 收付款单勾对表结构（tsb_payment_check）<br/>'
		    	+'删除 付款单表结构（tsb_payment_bill）<br/>'
		    	+'删除 期间分仓库存表（tsb_period_stock_amount）<br/>'
		    	+'删除 期间总库存金额表（tsb_period_amount）<br/>'
		    	+'删除 出库记录表（tsb_storage_out）<br/>'
		    	+'删除 入库记录表（tsb_storage_in）<br/>'
		    	+'删除 单据关联表（tsb_bill_relation）<br/>'
		    	+'删除 仓库单据记录明细表（tsb_warehouse_billdetail）<br/>'
		    	+'删除 仓库单据表（tsb_warehouse_bill）<br/>'
		    	+'删除 现金银行期初（tsb_initial_bank）<br/>'
		    	+'删除 期初应付（tsb_initial_pay）<br/>'
		    	+'删除 期初应收（tsb_initial_receivable） */+'</span>',
		    onShow: function(){
				$("#billinit").tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
		    }
		});
		$("#finance").tooltip({
		    position: 'right',
		    content: '<span style="color:#fff">'
		        +'删除系统内财务的全部凭证，只保留财务科目。'
		        +'</span>',
		    onShow: function(){
				$("#finance").tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
		    }
		});
		$("#sysinit").tooltip({
		    position: 'right',
		    content: '<span style="color:#fff">'
		    	 +'删除进销存和财务全部数据，只保留登录名和密码。'
		       /*  +'重置 单据单号生成规则表（tsb_bill_rule）<br/>'
		    	+'删除 银行表（tbd_bank）<br/>'
		    	+'删除 货品定价（tsb_goods_price）<br/>'
		    	+'删除 会计期间（tbd_stock_period）<br/>'
		    	+'删除 人员表（tbd_member）<br/>'
		    	+'删除 单位表（tbd_unit）<br/>'
		    	+'删除 货品属性表（tbd_good_spec）<br/>'
		    	+'删除 货品表（tbd_goods）<br/>'
		    	+'删除 供应商表（tbd_supplier）<br/>'
		    	+'删除 客户表（tbd_customer）<br/>'
		    	+'删除 辅助属性表（tbd_auxiliary_attr）<br/>'
		    	+'删除 用户查询条件模版表结构（rep_user_template）<br/>'
		    	+'删除 用户查询条件模版内容表结构（rep_user_template_detail）<br/>'*/+'</span>', 
		    onShow: function(){
				$("#sysinit").tooltip('tip').css({
					backgroundColor: '#666',
					borderColor: '#666'
				});
		    }
		});
	</script>
</body>
</html>