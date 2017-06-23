<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div id="memberChooser">
            <table id="memberTable"></table>
             <div id="pager2"></div> 
          </div>
          <div id="customerChooser">
            <table id="customerTable"></table>
            <div id="pager1"></div> 
          </div>
          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${entity.fid}"/> 
                <input name="billDate" id="billDate" type="hidden" value="${entity.billDate}"/> 
                <input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>
                <input name="customerId" id="customerId" type="hidden" value="${entity.customerId}"/>
                <input name="memberId" id="memberId" type="hidden" value="${entity.memberId}"/>
				<p><font><em>*</em>单号：</font><input id="code" value="${entity.code}" type="text" class="easyui-validatebox textBox" readonly="readonly" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true"/></p>
				<p><font><em>*</em>客户：</font><input id="customerName" type="text" value="${entity.customerName}" class="easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true" /></p>
				<p><font><em>*</em>应收金额：</font><input id="amount" type="text" value="${entity.amount}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:['amount','numMaxLength[10]']" /></p>
				<c:choose>
                  <c:when test="${entity.fid != null}">
                   <p><font>优惠金额：</font><input id="freeAmount" type="text" value="${entity.freeAmount}" class=" easyui-validatebox textBox" data-options="width:167,height:31,validType:['amount','maxLength[10]']" disabled="disabled"/></p>
	              </c:when>
	              <c:otherwise>
	               <input id="freeAmount" type="hidden" value="${entity.freeAmount}" class=" easyui-validatebox textBox" data-options="width:167,height:31,validType:['amount','maxLength[10]']" disabled="disabled"/>
                  </c:otherwise>
	            </c:choose>				
	            <%-- <p><font><em>*</em>单据日期：</font><input id="billDate" type="text" value="${entity.billDate}" class=" easyui-datebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,width:167,height:31,editable:false" /></p> --%>
				<p><font><em>*</em>负责人：</font><input id="memberName" type="text" value="${entity.memberName}" class="easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true" /></p>
				<p><font>描述：</font><input id="describe" type="text" value="${entity.describe}" class=" easyui-validatebox textBox" style="width:469px"/></p><br/> 
				<p><font></font><input id="save" type="button" class="btn-blue2 btn-xs" value="保存"/> <input id="zjjh" type="button" class="btn-blue2 btn-xs" value="资金计划" style="display: none"/></p>
              </form>
          </div>
          <div id="pop-win"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
var _amount="${entity.amount}";
var options = [
     		    {id:'0',name:'不允许'},
     		    {id:'1',name:'允许'}
     		];
var yesNo = [
    		    {id:'0',name:'不是'},
    		    {id:'1',name:'是'}
    		];
var sex = [
    		    {id:'1',name:'男'},
    		    {id:'2',name:'女'}
    		];
$(function(){
	$("input").attr('autocomplete','off');
	if('${code}'){
		$("#code").val('${code}');
	}
	if("${entity.fid}"){
		$("#zjjh").show();
	}
});
$("#amount").bind("change",function(e){
	if(!isNaN($(this).val()) && $.trim($(this).val())!=""){//修复填入空格出现Nan的问题
		var nv = $(this).val()+"";
		value = parseFloat(nv).toFixed(2)+'';
		$(this).val(value);
	}else{
		$(this).val(0);
	}
});


$("#customerName").fool("dhxComboGrid",{
    width:160,
    height:32,
    focusShow:true,
    required:true,
    data:getComboData((getRootPath()+'/customer/list?showDisable=0'),'fid'),
    filterUrl:getRootPath()+'/customer/list?showDisable=0',
    setTemplate:{
        input:"#name#",
        columns:[
            {option:"#code#",header:'编号',width:100},
            {option:"#name#",header:'名称',width:100},
            {option:"#categoryName#",header:'类别',width:100}
        ]
    },
    toolsBar:{
        name:'客户',
        addUrl:'/customer/manage',
        refresh:true
    },
    onLoadSuccess:function (combo) {
        combo.setComboValue("${entity.customerId}");
    }
});

/* $("#customerName").click(function(){
	 customerChooserOpen=true;
	 $('#customerChooser').window({
			title:'选择客户',
			top:($(window).height()-320)/2+$(document).scrollTop(),
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			width:700,
			height:275,
			onBeforeOpen:function(){
				$('#customerTable').jqGrid({									
					datatype:function(postdata){
						$.ajax({
							url:'${ctx}/customer/list', 
							data:postdata,
						    dataType:"json",
						    complete: function(data,stat){		    	
						    	if(stat=="success") {
						    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
						    		$("#customerTable")[0].addJSONData(data.responseJSON);
						    	}
						    }
						});
					}, 
					width:678,
					height:'100%',					
					pager:"#pager1",
					rowNum:10,
					rowList:[10,20,30], 
					viewrecords:true,
					jsonReader:{
						records:"total",
						total: "totalpages",  
					}, 
					colModel:[
					  		{name:'fid',label:'fid',hidden:true,width:100},
					  		{name:'code',label:'客户编号',sortable:true,width:100},
					  		{name:'name',label:'客户名称',sortable:true,width:100},
					  		{name:'categoryName',label:'类别',sortable:true,width:100},
					      ],
					 ondblClickRow:function(rowid,iRow,iCol,e){
						var rowData = $('#customerTable').jqGrid("getRowData",rowid);
						$("#customerId").val(rowData.fid);
						$("#customerName").val(rowData.name);
						$('#customerChooser').window('close');
					}
				}).navGrid('#pager1',{add:false,del:false,edit:false,search:false,view:false});
			}
		});
 });*/
//负责人
$("#memberName").fool("dhxComboGrid",{
    width:160,
    height:32,
    focusShow:true,
    required:true,
    data:getComboData((getRootPath()+'/member/list?rows=99999'),"fid"),
    filterUrl:getRootPath()+'/member/list?rows=99999',
    setTemplate:{
        input:"#username#",
        columns:[
            {option:'#userCode#',header:'编号',sortable:true,width:100},
            {option:'#username#',header:'名称',sortable:true,width:100},
            {option:'#deptName#',header:'部门',sortable:true,width:100}
        ]
    },
    toolsBar:{
        name:'负责人',
        addUrl:'/member/memberManager',
        refresh:true
    },
    onLoadSuccess:function (combo) {
        combo.deleteOption("");
        combo.setComboValue('${entity.memberId}');
    }

});
/* $("#memberName").click(function(){
	 memberChooserOpen=true;
	 $('#memberChooser').window({
			title:'选择负责人',
			top:($(window).height()-320)/2+$(document).scrollTop(),
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			width:700,
			height:275,
			onBeforeOpen:function(){
				$('#memberTable').jqGrid({
					datatype:function(postdata){
						$.ajax({
							url:'${ctx}/member/list',
							data:postdata,
						    dataType:"json",
						    complete: function(data,stat){		    	
						    	if(stat=="success") {
						    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
						    		$("#memberTable")[0].addJSONData(data.responseJSON);
						    	}
						    }
						});
					}, 
					width:678,
					height:'100%',					
					pager:"#pager2",
					rowNum:10,
					rowList:[10,20,30], 
					viewrecords:true,
					jsonReader:{
						records:"total",
						total: "totalpages",  
					}, 
					colModel:[
					  		{name:'fid',label:'fid',hidden:true,width:100},
					  		{name:'userCode',label:'人员编号',sortable:true,width:100},
					  		{name:'jobNumber',label:'工号',sortable:true,width:100},
					  		{name:'username',label:'人员名称',sortable:true,width:100},
					  		{name:'sex',label:'性别',sortable:true,width:100,formatter:function(value){
								for(var i=0; i<sex.length; i++){
									if (sex[i].id == value) return sex[i].name;
									if(value==undefined) return '';
								}
								return value;
							}},
					  		{name:'deptName',label:'部门',sortable:true,width:100},
					  		{name:'position',label:'职位',sortable:true,width:100},
					  		{name:'isInterface',label:'是否部门负责人',formatter:function(value){
								for(var i=0; i<yesNo.length; i++){
									if (yesNo[i].id == value) return yesNo[i].name;
									if(value==undefined) return '';
								}
								return value;
							},sortable:true,width:100},
					      ],
					ondblClickRow:function(rowid,iRow,iCol,e){
						var rowData = $('#memberTable').jqGrid("getRowData",rowid);
						$("#memberId").val(rowData.fid);
						$("#memberName").val(rowData.username);
						$('#memberChooser').window('close');
					}
				}).navGrid('#pager2',{add:false,del:false,edit:false,search:false,view:false});
			}
		});
 });*/
 
$('#save').click(function(e) {
	var code=$("#code").val();
	var fid=$("#fid").val();
	var amount=$("#amount").val();
	var freeAmount=$("#freeAmount").val();
	var describe=$("#describe").val();
	var updateTime=$("#updateTime").val();
	var customerId=$("#customerName").next()[0].comboObj.getSelectedValue();
	var memberId=$("#memberName").next()[0].comboObj.getSelectedValue();
        /* var billDate=$("#billDate").datebox('getValue'); */
	$('#form').form('disableValidation'); //取消验证
		if($('#form').form('validate')){
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/initialReceivableController/save',{freeAmount:freeAmount,code:code,fid:fid,amount:amount,describe:describe,updateTime:updateTime,customerId:customerId,memberId:memberId,/* billDate:billDate */},function(data){
			    	dataDispose(data);
			    	if(data.result=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			$('#addBox').window('close');
			    			$('#iniRecList').trigger('reloadGrid');
			    			$('#save').removeAttr("disabled");
			    		}}); 
			    	}else if(data.result=='1'){
			    		if(data.errorCode=='103' || data.errorCode=='104' || data.errorCode=='105'){
			    			$.fool.alert({btnName:'转到会计期间页面',btnAct:'mybtnAct(this)',msg:data.msg,fn:function(){
			    			}});
			    		}else{
				    		$.fool.alert({msg:data.msg,fn:function(){
				    		}});
			    		}
			    		$('#save').removeAttr("disabled");
			    	}else{
			    		$.fool.alert({msg:'服务器繁忙，请稍后再试！',fn:function(){
			    		}});
			    		return false;
					}
			    });
			}else{
				return false;
				}
});

$.extend($.fn.validatebox.defaults.rules, {
	amount:{
          validator: function (value, param) {
           	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
           },
           message:'请输入正确的金额'

        },
});
$("#zjjh").click(function(){
	win = $("#pop-win").fool('window',{modal:true,'title':"资金计划",height:480,width:780,href:getRootPath()+'/capitalPlanDetail/window?relationId='+$("#fid").val()+'&relationSign=93&billType=qcys'});
});
 </script>
</body>
</html>