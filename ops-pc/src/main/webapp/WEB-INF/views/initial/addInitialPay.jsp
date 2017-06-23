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
          <div id="supplierChooser">
            <table id="supplierTable"></table>
            <div id="pager1"></div> 
          </div>

          <div class="form1" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${entity.fid}"/>
                <input name="billDate" id="billDate" type="hidden" value="${entity.billDate}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>
                <input name="supplierId" id="supplierId" type="hidden" value="${entity.supplierId}"/>
                <input name="memberId" id="memberId" type="hidden" value="${entity.memberId}"/>
                <p><font><em>*</em>单号：</font><input id="code" value="${entity.code}" type="text" class="easyui-validatebox textBox" readonly="readonly" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true"/></p> 
				<p><font><em>*</em>供应商：</font><input id="supplierName" value="${entity.supplierName}" type="text" class="easyui-validatebox textBox"  data-options="required:true,missingMessage:'该项不能为空！',novalidate:true"/></p>
				<p><font><em>*</em>应付金额：</font><input id="amount" type="text" value="${entity.amount}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:['amount','numMaxLength[10]']" /></p>
				<c:choose>
                  <c:when test="${entity.fid != null}">
                   <p><font>优惠金额：</font><input id="freeAmount" type="text" value="${entity.freeAmount}" class=" easyui-validatebox textBox" data-options="width:167,height:31,validType:['amount','maxLength[10]']" disabled="disabled"/></p>
	              </c:when>
	              <c:otherwise>
	               <input id="freeAmount" type="hidden" value="${entity.freeAmount}" class=" easyui-validatebox textBox" data-options="width:167,height:31,validType:['amount','maxLength[10]']" disabled="disabled"/>
                  </c:otherwise>
	            </c:choose>
			    <%-- <p><font><em>*</em>单据日期：</font><input id="billDate" type="text" value="${entity.billDate}" class=" easyui-datebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,width:167,height:31,editable:false" /></p> --%>
				<p><font><em>*</em>负责人：</font><input id="memberName" type="text" value="${entity.memberName}" class="textBox easyui-validatebox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,width:167,height:31,editable:false" /></p>
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
	if(!isNaN($(this).val())){
		var nv = $(this).val()+"";
		value = parseFloat(nv).toFixed(2)+'';
		$(this).val(value);
	}
});
/* $("#supplierName").click(function(){
	 $('#supplierChooser').window({
			title:'选择供应商',
			top:($(window).height()-320)/2+$(document).scrollTop(),
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			width:700,
			height:275,
			onBeforeOpen:function(){
				supplierChooserOpen=true;
				$('#supplierTable').datagrid({
					url:'${ctx}/supplier/list',
					idField:'fid',
					pagination:true,
					fitColumns:true,
					columns:[[
					  		{field:'fid',title:'fid',hidden:true,width:100},
					  		{field:'code',title:'编号',sortable:true,width:100},
					  		{field:'name',title:'供应商名称',sortable:true,width:100},
					  		{field:'categoryName',title:'类别',sortable:true,width:100},
					  		{field:'bussinessRange',title:'经营范围',sortable:true,width:100},
					      ]],
					onClickRow:function(rowIndex, rowData){
						$("#supplierId").val(rowData.fid);
						$("#supplierName").val(rowData.name);
						$('#supplierChooser').window('close');
					}
				});
			}
		});
}); */

/*$("#supplierName").click(function(){
	 $('#supplierChooser').window({
			title:'选择供应商',
			top:($(window).height()-320)/2+$(document).scrollTop(),
			collapsible:false,
			minimizable:false,
			maximizable:false,
			resizable:false,
			width:700,
			height:275,
			onBeforeOpen:function(){
				supplierChooserOpen=true;
				$('#supplierTable').jqGrid({
					datatype:function(postdata){
						$.ajax({
							url:'${ctx}/supplier/list',
							data:postdata,
						    dataType:"json",
						    complete: function(data,stat){
						    	if(stat=="success") {
						    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
						    		$("#supplierTable")[0].addJSONData(data.responseJSON);
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
					  		{name:'code',label:'编号',align:"center",sortable:true,width:100},
					  		{name:'name',label:'供应商名称',align:"center",sortable:true,width:100},
					  		{name:'categoryName',label:'类别',align:"center",sortable:true,width:100},
					  		{name:'bussinessRange',label:'经营范围',align:"center",sortable:true,width:100},
					      ],
					 ondblClickRow:function(rowid,iRow,iCol,e){
						var rowData = $('#supplierTable').jqGrid("getRowData",rowid);
						$("#supplierId").val(rowData.fid);
						$("#supplierName").val(rowData.name);
						$('#supplierChooser').window('close');
					}
				}).navGrid('#pager1',{add:false,del:false,edit:false,search:false,view:false});
			}
		});
});*/

/*var supplierNameData = "";
$.ajax({
    url:'${ctx}/supplier/list',
    dataType:"json",
    success:function (data) {
        supplierNameData = formatData(data.rows ,'fid');
    }
});*/

//供应商
$("#supplierName").fool("dhxComboGrid",{
    width:160,
    height:32,
    required:true,
    focusShow:true,
    data:getComboData((getRootPath()+'/supplier/list'),'fid'),
    filterUrl:getRootPath()+'/supplier/list',
    setTemplate:{
        input:"#name#",
        columns:[
            {option:'#code#',header:'编号',width:100},
            {option:'#name#',header:'名称',width:100}
        ]
    },
    toolsBar:{
        name:'供应商',
        addUrl:'/supplier/manage',
        refresh:true
    },
    onLoadSuccess:function (combo) {
        combo.deleteOption("");
        combo.setComboValue('${entity.supplierId}');
    }

});

//负责人
$("#memberName").fool("dhxComboGrid",{
    width:160,
    height:32,
    focusShow:true,
    required:true,
    data:getComboData((getRootPath()+'/member/list?rows=9999'),"fid"),
    filterUrl:getRootPath()+'/member/list?rows=9999',
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

//$("#supplierChooser").removeAttr("style");
/* $("#memberName").click(function(){
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
				memberChooserOpen=true;
				$('#memberTable').datagrid({
					url:'${ctx}/member/list',
					idField:'fid',
					pagination:true,
					fitColumns:true,
					columns:[[
					  		{field:'fid',title:'fid',hidden:true,width:100},
					  		{field:'userCode',title:'人员编号',sortable:true,width:100},
					  		{field:'jobNumber',title:'工号',sortable:true,width:100},
					  		{field:'username',title:'人员名称',sortable:true,width:100},
					  		{field:'sex',title:'性别',sortable:true,width:100,formatter:function(value){
								for(var i=0; i<sex.length; i++){
									if (sex[i].id == value) return sex[i].name;
								}
								return value;
							}},
					  		{field:'deptName',title:'部门',sortable:true,width:100},
					  		{field:'position',title:'职位',sortable:true,width:100},
					  		{field:'isInterface',title:'是否部门负责人',sortable:true,width:100,formatter:function(value){
								for(var i=0; i<yesNo.length; i++){
									if (yesNo[i].id == value) return yesNo[i].name;
								}
								return value;
							}},
					  		{field:'sex',title:'性别',sortable:true,width:100},
					      ]],
					onClickRow:function(rowIndex, rowData){
						$("#memberId").val(rowData.fid);
						$("#memberName").val(rowData.username);
						$('#memberChooser').window('close');
					}
				});
			}
		});
});  */ 

/*$("#memberName").click(function(){
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
				memberChooserOpen=true;
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
					  		{name:'userCode',label:'人员编号',align:"center",sortable:true,width:100},
					  		{name:'jobNumber',label:'工号',align:"center",sortable:true,width:100},
					  		{name:'username',label:'人员名称',align:"center",sortable:true,width:100},
					  		{name:'sex',label:'性别',align:"center",sortable:true,width:100,formatter:function(value){
								for(var i=0; i<sex.length; i++){
									if (sex[i].id == value) return sex[i].name;
									if(value==undefined){
										return '';
									}
								}
								return value;
							}},
					  		{name:'deptName',label:'部门',align:"center",sortable:true,width:100},
					  		{name:'position',label:'职位',align:"center",sortable:true,width:100},
					  		{name:'isInterface',label:'是否部门负责人',align:"center",sortable:true,width:100,formatter:function(value){
								for(var i=0; i<yesNo.length; i++){
									if (yesNo[i].id == value) return yesNo[i].name;
									if(value==undefined){
										return '';
									}
								}
								return value;
							}},
					  		//{name:'sex',label:'性别',align:"center",sortable:true,width:100},
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
});  */
//$("#supplierChooser").removeAttr("style");

$('#save').click(function(e) {
	var code=$("#code").val();
	var fid=$("#fid").val();
	var amount=$("#amount").val();
	var freeAmount=$("#freeAmount").val();
	var describe=$("#describe").val();
	var updateTime=$("#updateTime").val();
	var supplierId=$("#supplierName").next()[0].comboObj.getSelectedValue();
	var memberId=$("#memberName").next()[0].comboObj.getSelectedValue();

	$('#form').form('disableValidation');//关闭验证
		if($('#form').form('validate')){
			$('#save').attr("disabled","disabled");
			    $.post('${ctx}/initialPayController/save',{freeAmount:freeAmount,code:code,fid:fid,amount:amount,describe:describe,updateTime:updateTime,supplierId:supplierId,memberId:memberId},function(data){
			    	dataDispose(data);
			    	if(data.result=='0'){
			    		$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
			    			$('#addBox').window('close');			    
			    			$('#iniPayList').trigger('reloadGrid');
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

$("#zjjh").click(function(){
	win = $("#pop-win").fool('window',{modal:true,'title':"资金计划",height:480,width:780,href:getRootPath()+'/capitalPlanDetail/window?relationId='+$("#fid").val()+'&relationSign=92&billType=qcyf'});
});
 </script>
</body>
</html>