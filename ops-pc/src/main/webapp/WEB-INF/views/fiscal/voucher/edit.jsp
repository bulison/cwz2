<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<html>
<head>
</head>
<body>
<%-- <%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>  --%>
          <div class="form1" >
              <form id="form">
                <input id="fid" name="fid" type="hidden" value="${obj.fid}"/>
                <input id="updateTime" name="updateTime" type="hidden" value="${obj.updateTime}"/>
                <input id="creatorId" name="creatorId" type="hidden" value="${obj.creatorId}"/>
                <input id="auditorId" name="auditorId" type="hidden" value="${obj.auditorId}"/>
                <input id="cancelorId" name="cancelorId" type="hidden" value="${obj.cancelorId}"/>
                <input id="supervisorId" name="supervisorId" type="hidden" value="${obj.supervisorId}"/>
                <input id="postPeopleId" name="postPeopleId" type="hidden" value="${obj.postPeopleId}"/>
                <input id="voucherWordId" name="voucherWordId" type="hidden" value="${obj.voucherWordId}"/>
                <input id="fiscalAccountId" name="fiscalAccountId" type="hidden" value="${obj.fiscalAccountId}"/>
                <input id="getLastResume" type="hidden"/>
                <input id="editVoucherNumber" type="hidden"/>
                <p><font><em>*</em>凭证日期：</font><input id="voucherDate" name="voucherDate" class="textBox" value=""/></p>
                <p><font><em>*</em>凭证字：</font><input id="voucherWordName" name="" class="textBox" value=""/></p>
                <p><font><em>*</em>凭证号：</font><input id="voucherNumber" name="voucherNumber" class="textBox" value=""/></p><br/>
                <p><font>附件数：</font><input id="accessoryNumber" name="accessoryNumber" class="textBox" value="${obj.accessoryNumber}"/></p>
                <p><font>顺序号：</font><input id="number" name="number" class="textBox" value="${obj.number}"/></p>
                <p><font>状态：</font><input id="recordStatus" name="recordStatus" class="textBox" value=""/></p><br/>
                <h3 style="display: inline-block;margin-left:44px">&emsp;其他信息：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>
                <p class="hideOut"><font>创建人：</font><input id="creatorName" class="textBox" value="${obj.creatorName}" disabled="disabled"/></p>
                <p class="hideOut"><font>创建日期：</font><input id="createTime" class="textBox" value="${obj.createTime}" disabled="disabled"/></p>
                <p class="hideOut"><font>审核人：</font><input id="auditorName" class="textBox" value="${obj.auditorName}" disabled="disabled"/></p>
                <p class="hideOut"><font>审核日期：</font><input id="auditTime" class="textBox" value="${obj.auditDate}" disabled="disabled"/></p>
                <p class="hideOut"><font>作废人：</font><input id="cancelorName" class="textBox" value="${obj.cancelorName}" disabled="disabled"/></p>
                <p class="hideOut"><font>作废日期：</font><input id="cancelTime" class="textBox" value="${obj.cancelDate}" disabled="disabled"/></p>
                <p class="hideOut"><font>凭证主管：</font><input id="supervisorName" class="textBox" value="${obj.supervisorName}" disabled="disabled"/></p>
                <p class="hideOut"><font>记账人：</font><input id="postPeopleName" class="textBox" value="${obj.postPeopleName}" disabled="disabled"/></p>
              </form>
          </div>
          <div id="gridBox">
            <div id="detailList"></div>
            <div id="signBox">
                <p class="hideOut"><font>外币币别：</font><input id="currencyName" readonly="readonly"/></p>
                <p class="hideOut"><font>汇率：</font><input id="exchangeRate" /></p>
                <p class="hideOut"><font>外币金额：</font><input id="currencyAmount" /></p>
                <p class="hideOut"><font>单位：</font><input id="unitName" readonly="readonly"/></p>
                <p class="hideOut"><font>数量：</font><input id="quantity" /></p>
                <p class="hideOut"><font>供应商：</font><input id="supplierName" /></p>
                <p class="hideOut"><font>客户：</font><input id="customerName" /></p>
                <p class="hideOut"><font>部门：</font><input id="departmentName" /></p>
                <p class="hideOut"><font>职员：</font><input id="memberName" /></p>
                <p class="hideOut"><font>仓库：</font><input id="warehouseName" /></p>
                <p class="hideOut"><font>项目：</font><input id="projectName" /></p>
                <p class="hideOut"><font>货品：</font><input id="goodsName" /></p>
            </div>
          </div>
          <div class="form1" id="btnBox"></div>
          
          
<script type="text/javascript">
//弹出框变量
var chooserWindow="";
var edIndex="";
var $grid="";
var initialVo="";
var initialDetail="";
var rowData=[];
if('${obj.fid}'||"${fid}"){
	var fid="";
	if('${obj.fid}'){
		fid='${obj.fid}';
	}else{
		fid="${fid}";
	}
	$.ajax({
		url:"${ctx}/voucherdetail/getDetails",
		async:false,
        data:{voucherId:fid},
        success:function(data){
        	rowData=data;
        	initialDetail=data.slice(0);
        }
	});
}
  //表单控件初始化
  $("#recordStatus").combobox({
	  value:'${obj.recordStatus}',
	  disabled:true,
	  hasDownArrow:false,
	  editable:false,
	  width:160,
	  height:32,
	  data:[{value:"0",text:'草稿单'},{value:"1",text:'已审核'},{value:"2",text:'已作废'},{value:"3",text:'已过账'}]
  });
  $("#voucherNumber").validatebox({
	  validType:['isBank','positiveInt'],
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
  });
  $("#voucherWordName").fool('voucherWordTree',{
	    width:160,
	    height:32,
		onBeforeSelect:function(node){
			if(node.text=="请选择"){
				return false;
			}
		},
		onSelect:function(node){
			if("${obj.voucherWordId}"&&node.id=="${obj.voucherWordId}"&&$("#voucherDate").datebox("getValue")=="${obj.voucherDate}"){
				$("#voucherNumber").val("${obj.voucherNumber}");
				  $("#voucherWordId").val("${obj.voucherWordId}");
				  $("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
				  $("#getLastResume").val("${getLastResume}");
				  $("#editVoucherNumber").val("${editVoucherNumber}");
				  if($("#editVoucherNumber").val()==1){
						$("#voucherNumber").attr("readonly","readonly");
				  }else{
						$("#voucherNumber").removeAttr("readonly");
				  }
				  return;
			}
			$("#voucherWordId").val(node.id);
			$.post("${ctx}/voucher/getDefaultMessage",{voucherDate:$("#voucherDate").datebox('getValue'),voucherWordId:node.id},function(data){
				$("#voucherNumber").val(data.voucherNumber);
				$("#getLastResume").val(data.getLastResume);
				$("#editVoucherNumber").val(data.editVoucherNumber);
				if($("#editVoucherNumber").val()==1){
					$("#voucherNumber").attr("readonly","readonly");
				}else{
					$("#voucherNumber").removeAttr("readonly");
				}
			});
		},
		onLoadSuccess:function(){
			if(!'${obj.fid}'){
				  $("#voucherWordName").combotree("setValue","${voucherWordId}");
			  }else{
				  $("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
			  }
		}
  });
  $("#voucherDate").datebox({
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
	  validType:"voucherDate",
	  width:160,
	  height:32,
	  editable:false,
	  onSelect:function(date){
		  var y = date.getFullYear();
		  var m = date.getMonth()+1;
		  var d = date.getDate();
		  if(m<9){
			  m="0"+m;
		  }
		  if(d<9){
			  d="0"+d;
		  }
		  var value=y+"-"+m+"-"+d;
		  if("${obj.voucherDate}"&&value=="${obj.voucherDate}"&&"${obj.fid}"){
			  $("#voucherNumber").val("${obj.voucherNumber}");
			  $("#voucherWordId").val("${obj.voucherWordId}");
			  $("#voucherWordName").combotree("setValue","${obj.voucherWordId}");
			  $("#getLastResume").val("${getLastResume}");
			  $("#editVoucherNumber").val("${editVoucherNumber}");
			  if($("#editVoucherNumber").val()==1){
					$("#voucherNumber").attr("readonly","readonly");
			  }else{
					$("#voucherNumber").removeAttr("readonly");
			  }
			  return ;
		  }
		  $.post('${ctx}/voucher/getDefaultMessage',{voucherDate:$("#voucherDate").datebox('getValue')},function(data){
			  $("#voucherNumber").val(data.voucherNumber);
			  $("#voucherWordId").val(data.voucherWordId);
			  if(data.voucherWordId){
				  $("#voucherWordName").combotree("setValue",data.voucherWordId);
			  }else{
				  $("#voucherWordName").combotree("clear");
			  }
			  $("#getLastResume").val(data.getLastResume);
			  $("#editVoucherNumber").val(data.editVoucherNumber);
			  if($("#editVoucherNumber").val()==1){
					$("#voucherNumber").attr("readonly","readonly");
			  }else{
					$("#voucherNumber").removeAttr("readonly");
			  }
		  });
		  $("#voucherDate").datebox("enableValidation");
	  }
  });
  $("#accessoryNumber").validatebox({
	  validType:'positiveInt',
  });
  $("#number").validatebox({
	  validType:'positiveInt',
  });
  $("#currencyName").textbox({
	  validType:'isBank',
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
  });
  $("#exchangeRate").textbox({
	  validType:['isBank','exchangeRate'],
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
	  onChange:function(newValue, oldValue){
		  valueGrid("exchangeRate",newValue);
		  var row=$('#detailList').datagrid("getSelected");
		  if(newValue&&row.editing){
			  var debitAmount=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("getValue");
			  var creditAmount=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'creditAmount'}).target).textbox("getValue");
			  var currencyAmount="";
			  if(debitAmount&&debitAmount!=0){
				  currencyAmount=(debitAmount/newValue).toFixed(2);
			  }else if(creditAmount&&creditAmount!=0){
				  currencyAmount=(creditAmount/newValue).toFixed(2);
			  }
			  $("#currencyAmount").textbox("setValue",currencyAmount);
		  }
	  }
  });
  $("#currencyAmount").textbox({
	  validType:['isBank','amount'],
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
	  onChange:function(newValue, oldValue){
		  valueGrid("currencyAmount",newValue);
		  /* var exchangeRate=$("#exchangeRate").textbox("getValue");
		  if(exchangeRate){
			  var amount=(newValue*exchangeRate).toFixed(2);
			  var debitAmount=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("getValue");
			  var creditAmount=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'creditAmount'}).target).textbox("getValue");
			  if((!debitAmount||debitAmount==0)&&(!creditAmount||creditAmount==0)){
				  $($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("setValue",amount);
			  }else if(debitAmount&&debitAmount!=0){
				  $($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("setValue",amount);
			  }else if(creditAmount&&creditAmount!=0){
				  $($('#detailList').datagrid('getEditor',{index:edIndex,field:'creditAmount'}).target).textbox("setValue",amount);
			  }
		  } */
	  }
  });
  $("#currencyAmount").textbox("textbox").keyup(function(){
	  var exchangeRate=$("#exchangeRate").textbox("getValue");
	  var newValue=$("#currencyAmount").textbox("getText");
	  if(exchangeRate){
		  var amount=(newValue*exchangeRate).toFixed(2);
		  var debitAmount=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("getValue");
		  var creditAmount=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'creditAmount'}).target).textbox("getValue");
		  if((!debitAmount||debitAmount==0)&&(!creditAmount||creditAmount==0)){
			  $($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("setValue",amount);
		  }else if(debitAmount&&debitAmount!=0){
			  $($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("setValue",amount);
		  }else if(creditAmount&&creditAmount!=0){
			  $($('#detailList').datagrid('getEditor',{index:edIndex,field:'creditAmount'}).target).textbox("setValue",amount);
		  }
	  }
  });
  
  $("#unitName").textbox({
	  validType:'isBank',
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
  });
  $("#quantity").textbox({
	  validType:['isBank','intOrFloat'],
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
	  onChange:function(newValue, oldValue){
		  valueGrid("quantity",newValue);
	  }
  });
  $("#supplierName").textbox({
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
  });
  $("#customerName").textbox({
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
  });
  $("#departmentName").combotree({
	  width:100,
	  disabled:true,
	  url:"${ctx}/orgController/getAllTree",
	  required:true,
	  novalidate:true,
	  editable:false,
	  onLoadSuccess:function(node, data){
		  if(data[0].id!=""){
			  var node=$("#departmentName").combotree('tree').tree("find",data[0].id);
			  $("#departmentName").combotree('tree').tree('update',{
				  target: node.target,
				  text: '请选择',
				  id:"",
			  });
		  }
	  },
	  onBeforeSelect:function(node){
		  if(!node.id){
			  return false;
		  }
	  },
	  onSelect:function(record){
		  valueGrid("departmentName",record.text);
		  valueGrid("departmentId",record.id);
	  }
  });
  $("#memberName").textbox({
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
  });
  $("#warehouseName").combotree({
	  width:100,
	  disabled:true,
	  url:"${ctx}/basedata/warehourseList",
	  required:true,
	  novalidate:true,
	  editable:false,
	  onLoadSuccess:function(node, data){
		  if(data[0].id!=""){
			  var node=$("#warehouseName").combotree('tree').tree("find",data[0].id);
			  $("#warehouseName").combotree('tree').tree('update',{
				  target: node.target,
				  text: '请选择',
				  id:"",
			  });
		  }
	  },
	  onBeforeSelect:function(node){
		  if(!node.id){
			  return false;
		  }
	  },
	  onSelect:function(record){
		  valueGrid("warehouseName",record.text);
		  valueGrid("warehouseId",record.id);
	  }
  });
  $("#projectName").combotree({
	  width:100,
	  disabled:true,
	  url:"${ctx}/basedata/findSubAuxiliaryAttrTree?code=012",
	  required:true,
	  novalidate:true,
	  editable:false,
	  onLoadSuccess:function(node, data){
		  if(data[0].id!=""){
			  var node=$("#projectName").combotree('tree').tree("find",data[0].id);
			  $("#projectName").combotree('tree').tree('update',{
				  target: node.target,
				  text: '请选择',
				  id:"",
			  });
		  }
	  },
	  onBeforeSelect:function(node){
		  if(!node.id){
			  return false;
		  }
	  },
	  onSelect:function(record){
		  valueGrid("projectName",record.text);
		  valueGrid("projectId",record.id);
	  }
  });
  $("#goodsName").textbox({
	  width:100,
	  disabled:true,
	  required:true,
	  missingMessage:'该项不能为空！',
	  novalidate:true,
  });
  
  //初始化凭证字号
  if(!'${obj.fid}'){
	  $("#voucherDate").datebox('setValue',"${voucherDate}");
	  $("#voucherNumber").val("${voucherNumber}");
	  $("#voucherWordId").val("${voucherWordId}");
	  $("#getLastResume").val("${getLastResume}");
	  $("#editVoucherNumber").val("${editVoucherNumber}");
	  if($("#editVoucherNumber").val()==1){
			$("#voucherNumber").attr("readonly","readonly");
		}else{
			$("#voucherNumber").removeAttr("readonly");
		}
  }else{
	  $("#voucherDate").datebox('setValue','${obj.voucherDate}');
	  $("#voucherNumber").val("${obj.voucherNumber}");
	  $("#voucherWordId").val("${obj.voucherWordId}");
	  $("#getLastResume").val("${getLastResume}");
	  $("#editVoucherNumber").val("${editVoucherNumber}");
	  if($("#editVoucherNumber").val()==1){
			$("#voucherNumber").attr("readonly","readonly");
		}else{
			$("#voucherNumber").removeAttr("readonly");
		}
  }
  
  //明细列标初始化
  $("#detailList").datagrid({
	  width:$("#gridBox").width()*0.95,
	  data:rowData,
	  autoRowHeight:true,
	  nowrap:false,
	  singleSelect:true,
	  scrollbarSize:0,
	  fitColumns:true,
	  showFooter:true,
	  columns:[[
                {field:'newRow',title:'newRow',hidden:true},
                {field:'quantity',title:'quantity',hidden:true},
                {field:'exchangeRate',title:'exchangeRate',hidden:true},
                {field:'currencyAmount',title:'currencyAmount',hidden:true},
                {field:'unitName',title:'unitId',hidden:true},
                {field:'currencyName',title:'currencyId',hidden:true},
                {field:'supplierName',title:'supplierId',hidden:true},
                {field:'customerName',title:'customerId',hidden:true},
                {field:'departmentName',title:'departmentId',hidden:true},
                {field:'memberName',title:'memberId',hidden:true},
                {field:'warehouseName',title:'warehouseId',hidden:true},
                {field:'projectName',title:'projectId',hidden:true},
                {field:'goodsName',title:'goodsId',hidden:true},
                
                {field:'unitId',title:'unitId',hidden:true},
                {field:'currencyId',title:'currencyId',hidden:true},
                {field:'supplierId',title:'supplierId',hidden:true},
                {field:'customerId',title:'customerId',hidden:true},
                {field:'departmentId',title:'departmentId',hidden:true},
                {field:'memberId',title:'memberId',hidden:true},
                {field:'warehouseId',title:'warehouseId',hidden:true},
                {field:'projectId',title:'projectId',hidden:true},
                {field:'goodsId',title:'goodsId',hidden:true},
                {field:'subjectId',title:'subjectId',hidden:true},
                {field:'subjectCode',title:'subjectCode',hidden:true},
                {field:'parentSubjectCode',title:'parentSubjectCode',hidden:true},
                {field:'parentSubjectName',title:'parentSubjectName',hidden:true},

                {field:'currencySign',title:'核算外币',hidden:true},
                {field:'supplierSign',title:'核算供应商',hidden:true},
                {field:'customerSign',title:'核算客户',hidden:true},
                {field:'departmentSign',title:'核算部门',hidden:true},
                {field:'memberSign',title:'核算职员',hidden:true},
                {field:'warehouseSign',title:'核算仓库',hidden:true},
                {field:'projectSign',title:'核算项目',hidden:true},
                {field:'goodsSign',title:'核算货品',hidden:true},
                {field:'quantitySign',title:'核算数量',hidden:true},

                {field:'resume',title:'摘要',width:70,editor:{type:'combotree',options:{validType:'isBank',editable:true,url:"${ctx}/basedata/resume",onLoadSuccess:function(node, data){
                	if(data[0].id!=""){
                		var node=$(this).tree("find",data[0].id);
                		$(this).tree('update',{
        			    	target: node.target,
        				    text: '请选择',
        				    id:"",
        			    });
                	}
                },onBeforeSelect:function(node){
                	if(!node.id){
                		return false;
                	}
                }}}},
                {field:'subjectName',title:'科目',width:200,editor:{type:"combobox",options:{required:true,valueField:"id",textField:"name",url:"${ctx}/fiscalSubject/getSubject?leafFlag=1",mode:"remote",onClickIcon:function(){
                	$(this).combobox('hidePanel');
                	subjectChooser(this);
                },onSelect:function(record){
                	var editor=$(this);
                	if(/* $("#subjectIsLeaf").val()==1&& */record.flag==0){
                		editor.combobox("unselect",record.id);
              		  /* $.fool.alert({msg:"凭证填制必须是科目明细。",fn:function(){
              			editor.combobox('clear');
              		  }});
              		  return false; */
                	}
                	if(getValue("subjectId")==record.fid){
                		return false;
                	}else{
                		valueGrid('subjectId',record.fid);
                		valueGrid('subjectCode',record.code);
                		if(record.parentCode){
                			valueGrid('parentSubjectCode',record.parentCode);
                		}
                		if(record.parentName){
                			valueGrid('parentSubjectName',record.parentName);
                		}
                    	initialSign(record);
                    	signInputer(record);
                    	clearInputer();
                    	setInputer(record);
                	} 
                	disableInputer(record,"enable");
                }}},formatter:function(value,row,index){
                	if(value=="合计"){
                		return value;
                	}
                	var str="";
                	if(row.subjectCode){
                		str+=row.subjectCode+" ";
                	};
                	if(row.parentSubjectName&&row.parentSubjectName!="科目"){
                		str+=row.parentSubjectName+"|";
                	};
                	if(value){
                		str+=value;
                	};
                	if(row.supplierName){
                		str+="|"+row.supplierName;
                	};
                	if(row.customerName){
                		str+="|"+row.customerName;
                	};
                	if(row.departmentName){
                		str+="|"+row.departmentName;
                	};
                	if(row.memberName){
                		str+="|"+row.memberName;
                	};
                	if(row.warehouseName){
                		str+="|"+row.warehouseName;
                	};
                	if(row.projectName){
                		str+="|"+row.projectName;
                	};
                	if(row.goodsName){
                		str+="|"+row.goodsName;
                	};
                	
                	return str;
                }},
                {field:'debitAmount',title:'借方金额',width:40,editor:{type:'textbox',options:{required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
                	if(newValue&&newValue!=0){
                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("setText",'0');
                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("disable");
                	}else{
                		//$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("setText","");
                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="creditAmount"]').find('.datagrid-editable-input').textbox("enable");
                	}
                	/* if(getValue("currencySign")==1){
                		var exchangeRate=$("#exchangeRate").textbox("getValue");
                		if(exchangeRate){
                			var currencyAmount=(newValue/exchangeRate).toFixed(2);
                    		$("#currencyAmount").textbox("setValue",currencyAmount);
                		}
                	} */
                }}},formatter:function(value){
                	if(value){
                		value = (parseFloat(value)).toFixed(2) +"";
    		  			var re=/(-?\d+)(\d{3})/ ;
    		  			while(re.test(value)){
    		  				value=value.replace(re,"$1,$2"); 
    		  			}
    		  			return value;
                	}else{
                		return 0.00;
                	}
				}},
                {field:'creditAmount',title:'贷方金额',width:40,editor:{type:'textbox',options:{required:true,missingMessage:'该项不能为空！',validType:'amount',onChange:function(newValue, oldValue){
                	if(newValue&&newValue!=0){
                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("setText",'0');
                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("disable");
                	}else{
                		//$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("setText","");
                		$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="debitAmount"]').find('.datagrid-editable-input').textbox("enable");
                	}
                	/* if(getValue("currencySign")==1){
                		var exchangeRate=$("#exchangeRate").textbox("getValue");
                		if(exchangeRate){
                			var currencyAmount=(newValue/exchangeRate).toFixed(2);
                    		$("#currencyAmount").textbox("setValue",currencyAmount);
                		}
                	} */
                }}},formatter:function(value){
                	if(value){
                		value = (parseFloat(value)).toFixed(2) +"";
    		  			var re=/(-?\d+)(\d{3})/ ;
    		  			while(re.test(value)){
    		  				value=value.replace(re,"$1,$2"); 
    		  			}
    		  			return value;
                	}else{
                		return 0.00;
                	}
				}},
                {field:'action',title:'操作',width:50,formatter:function(value,row,index){
                	if(value||'${obj.recordStatus}'==1||'${obj.recordStatus}'==2||'${obj.recordStatus}'==3){
                		return "";
                	}
                	if(row.editing){
                		var s = '<a class="btn-save" title="确认" href="javascript:;" onclick="saverow(this)"></a>';
              		    var c = '<a class="btn-back" title="撤销" href="javascript:;" onclick="cancelrow(this)"></a>';
                        return s+" "+c;
                	}else{
                		var e= '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editrow(this)"></a>';
              		    var d= '<a class="btn-del" title="删除" href="javascript:;" onclick="deleterow(this)"></a>';
	                    return e+" "+d;
                	}
                }}
                ]],
      onBeforeEdit:function(index,row){
    	  row.editing = true;
		  updateActions(index);
      },
	  onAfterEdit:function(index,row){
		  row.editing = false;
		  updateActions(index);
	  },
	  onCancelEdit:function(index,row){
		  row.editing = false;
		  updateActions(index);
	  },
	  onLoadSuccess:function(data){
		  $grid=$("#gridBox").find(".datagrid").find(".datagrid-view2").find(".datagrid-body");
		  loadFooter();
		  if('${param.mark}'=="1"){
			  var rows=data.rows;
			  var debitAmount="";
			  var creditAmount="";
			  for(var i=0;i<rows.length;i++){
				  if(rows[i].debitAmount){
					  debitAmount=-(rows[i].debitAmount);
				  }
				  if(rows[i].creditAmount){
					  creditAmount=-(rows[i].creditAmount);
				  }
				  $("#detailList").datagrid('updateRow',{
					  index: i,
					  row: {
						  debitAmount: debitAmount,
						  creditAmount: creditAmount
					  }
				  });
				  debitAmount="";
				  creditAmount="";
			  }
			  var number=parseInt($("#number").val())+1;
			  $("#number").val(number);
			  loadFooter();
		  }
		  initialVo=$('#form').serialize();
	  },
	  onSelect:function(index,row){
		  if(row.editing!=true&&editComplete()){
			  signInputer(row);
			  setInputer(row);
		  }
	  }
  });
  
  $grid.on("keyup","input.textbox-text",function(e){
	  var newValue="";
	  if($(this).closest(".datagrid-cell").parent().attr("field")=="debitAmount"){
		  newValue=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'debitAmount'}).target).textbox("getText");  
	  }else if($(this).closest(".datagrid-cell").parent().attr("field")=="creditAmount"){
		  newValue=$($('#detailList').datagrid('getEditor',{index:edIndex,field:'creditAmount'}).target).textbox("getText");
	  }else{
		  return false;
	  }
	  if(getValue("currencySign")==1){
		  var exchangeRate=$("#exchangeRate").textbox("getValue");
		  if(exchangeRate){
			  var currencyAmount=(newValue/exchangeRate).toFixed(2);
			  $("#currencyAmount").textbox("setValue",currencyAmount);
		  }
	  }
  });
  
  //判断状态，初始化表单及按钮
  if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''){
	  $("#btnBox").append('<p><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p> <p><input type="button" id="nextVoucher" class="btn-blue2 btn-xs" value="下一张凭证" /></p>');
  }else if('${obj.recordStatus}'==0){
	  $("#btnBox").append('<p><input type="button" id="save" class="btn-blue2 btn-xs" value="保存" /></p> <p><input type="button" id="nextVoucher" class="btn-blue2 btn-xs" value="下一张凭证" /></p> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" id="verify" class="btn-blue2 btn-xs" value="审核"/></p> <p><input type="button" id="cancel" class="btn-blue2 btn-xs" value="作废"/></p> <p><input type="button" id="writeOff" class="btn-blue2 btn-xs" value="冲销"/></p> ');
  }else if('${obj.recordStatus}'==1){
	  $("#form").find("input").attr('disabled','disabled');
	  $("#addDetail").hide();
	  $("#btnBox").append('<p><input type="button" id="nextVoucher" class="btn-blue2 btn-xs" value="下一张凭证" /></p> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" id="unVerify" class="btn-blue2 btn-xs" value="反审核"/></p> <p><input type="button" id="cancel" class="btn-blue2 btn-xs" value="作废"/></p> <p><input type="button" id="writeOff" class="btn-blue2 btn-xs" value="冲销"/></p> <p><input type="button" id="sign" class="btn-blue2 btn-xs" value="签字"/></p>');
  }else if('${obj.recordStatus}'==2){
	  $("#form").find("input").attr('disabled','disabled');
	  $("#addDetail").hide();
	  $("#btnBox").append('<p><input type="button" id="nextVoucher" class="btn-blue2 btn-xs" value="下一张凭证" /></p> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p> <p><input type="button" id="unCancel" class="btn-blue2 btn-xs" value="反作废"/></p> <p><input type="button" id="writeOff" class="btn-blue2 btn-xs" value="冲销"/></p>');
  }else if('${obj.recordStatus}'==3){
	  $("#form").find("input").attr('disabled','disabled');
	  $("#addDetail").hide();
	  $("#btnBox").append('<p><input type="button" id="nextVoucher" class="btn-blue2 btn-xs" value="下一张凭证" /></p> <p><input type="button" id="print" class="btn-blue2 btn-xs" value="打印" /></p>');
  }
  
  //展开、收起其他信息
  $("#openBtn").click(function(e) {
			  $("#form .hideOut").css("display","inline-block");
			  $('#openBtn').css("display","none");
			  $('#closeBtn').css("display","inline-block");
  });
  $("#closeBtn").click(function(e) {
			  $("#form .hideOut").css("display","none");
			  $('#openBtn').css("display","inline-block");
			  $('#closeBtn').css("display","none");	
  });
  
  //列表方法
  function updateActions(index){
	  $('#detailList').datagrid('updateRow',{
		  index: index,
		  row:{}
	  });
  }
  function getRowIndex(target){
	  var tr = $(target).closest('tr.datagrid-row');
	  return parseInt(tr.attr('datagrid-row-index'));
  }
  function editrow(target){
	  var rows=$('#detailList').datagrid('getRows');
	  if(!editComplete()&&rows.length>0){
		  $.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！'});
		  return false;
	  }
	  var index=getRowIndex(target);
	  edIndex=index;
	  var resume=getValue("resume");
	  $('#detailList').datagrid('selectRow', index);
	  hideBtn(1);
	  $('#detailList').datagrid('beginEdit', index);
	  $($('#detailList').datagrid('getEditor',{index:index,field:'resume'}).target).combotree({onLoadSuccess:function(){
		  $($('#detailList').datagrid('getEditor',{index:index,field:'resume'}).target).combotree("setText",resume);
	  }});
	  disableInputer({quantitySign:getValue("quantitySign"),currencySign:getValue("currencySign"),supplierSign:getValue("supplierSign"),customerSign:getValue("customerSign"),departmentSign:getValue("departmentSign"),memberSign:getValue("memberSign"),warehouseSign:getValue("warehouseSign"),projectSign:getValue("projectSign"),goodsSign:getValue("goodsSign")},'enable');
  }
  function deleterow(target){
	  $('#detailList').datagrid('deleteRow', getRowIndex(target));
	  loadFooter();
  }
  function saverow(target){
	  var index=getRowIndex(target);
	  var row=$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']");
	  var subjectId=getValue("subjectId");
	  var subjectName=$($('#detailList').datagrid('getEditor',{index:index,field:'subjectName'}).target).combobox('getText');
	  var resume=$($('#detailList').datagrid('getEditor',{index:index,field:'resume'}).target).combotree('getText');
	  var debitAmount=$($('#detailList').datagrid('getEditor',{index:index,field:'debitAmount'}).target).textbox('getText');
	  var creditAmount=$($('#detailList').datagrid('getEditor',{index:index,field:'creditAmount'}).target).textbox('getText');
	  if(debitAmount==0&&creditAmount==0){
		  $.fool.alert({time:1000,msg:"借方与贷方金额不能同时为0！"});
		  return false;
	  }
	  hideBtn();
	  var unitId=getValue("unitId");
	  var currencyId=getValue("currencyId");
	  var supplierId=getValue("supplierId");
	  var customerId=getValue("customerId");
	  var departmentId=getValue("departmentId");
	  var memberId=getValue("memberId");
	  var warehouseId=getValue("warehouseId");
	  var projectId=getValue("projectId");
	  var goodsId=getValue("goodsId");
	  var unitName=getValue("unitName");
	  var currencyName=getValue("currencyName");
	  var supplierName=getValue("supplierName");
	  var customerName=getValue("customerName");
	  var departmentName=getValue("departmentName");
	  var memberName=getValue("memberName");
	  var warehouseName=getValue("warehouseName");
	  var projectName=getValue("projectName");
	  var goodsName=getValue("goodsName");
	  var quantity=getValue("quantity");
	  var exchangeRate=getValue("exchangeRate");
	  var currencyAmount=getValue("currencyAmount");
	  var currencySign=getValue("currencySign");
	  var supplierSign=getValue("supplierSign");
	  var customerSign=getValue("customerSign");
	  var departmentSign=getValue("departmentSign");
	  var memberSign=getValue("memberSign");
	  var warehouseSign=getValue("warehouseSign");
	  var projectSign=getValue("projectSign");
	  var goodsSign=getValue("goodsSign");
	  var quantitySign=getValue("quantitySign");
	  var parentSubjectCode=getValue("parentSubjectCode");
	  var parentSubjectName=getValue("parentSubjectName");
	  var subjectCode=getValue("subjectCode");
	  if(subjectId){
		  $.ajax({
			  url:"${ctx}/fiscalSubject/getById",
			  async:false,
              data:{fid:subjectId},
              success:function(data){
            	  if(data){
    				  if(data.name!=subjectName){
    					  $($('#detailList').datagrid('getEditor',{index:index,field:'subjectName'}).target).combobox('clear');
    					  return false;
    				  }
    			  }else{
    				  $($('#detailList').datagrid('getEditor',{index:index,field:'subjectName'}).target).combobox('clear');
    				  return false;
    			  }
              }
		  });
	  }else{
		  $($('#detailList').datagrid('getEditor',{index:index,field:'subjectName'}).target).combobox('clear');
		  return false;
	  }
	  $("#signBox").form('enableValidation');
	  if(row.form('validate')&&$("#signBox").form("validate")){
		  $('#detailList').datagrid('endEdit', getRowIndex(target));
		  $('#detailList').datagrid('updateRow',{
			  index: index,
			  row:{
				  'subjectId':subjectId,
				  'subjectName':subjectName,
				  'resume':resume,
				  'debitAmount':debitAmount,
				  'creditAmount':creditAmount,
				  'unitId':unitId,
				  'currencyId':currencyId,
				  'supplierId':supplierId,
				  'customerId':customerId,
				  'departmentId':departmentId,
				  'memberId':memberId,
				  'warehouseId':warehouseId,
				  'projectId':projectId,
				  'goodsId':goodsId,
				  'unitName':unitName,
				  'currencyName':currencyName,
				  'supplierName':supplierName,
				  'customerName':customerName,
				  'departmentName':departmentName,
				  'memberName':memberName,
				  'warehouseName':warehouseName,
				  'projectName':projectName,
				  'goodsName':goodsName,
				  'quantity':quantity,
				  'exchangeRate':exchangeRate,
				  'currencyAmount':currencyAmount,
				  'quantitySign':quantitySign,
				  'currencySign':currencySign,
				  'supplierSign':supplierSign,
				  'customerSign':customerSign,
				  'departmentSign':departmentSign,
				  'memberSign':memberSign,
				  'warehouseSign':warehouseSign,
				  'projectSign':projectSign,
				  'goodsSign':goodsSign,
				  'parentSubjectCode':parentSubjectCode,
				  'parentSubjectName':parentSubjectName,
				  'newRow':0,
				  'subjectCode':subjectCode
			  }
		  });
		  disableInputer("","disable");
		  loadFooter();
		  return false;
	  }else{
		  return false;
	  }
  }
  function cancelrow(target){
	  hideBtn();
	  $('#detailList').datagrid('selectRow', edIndex);
	  var row=$('#detailList').datagrid('getSelected');
	  if(row.newRow!=1){
		  signInputer(row);
		  setInputer(row);
		  disableInputer("","disable");
		  $('#detailList').datagrid('cancelEdit', getRowIndex(target));
	  }else{
		  $('#detailList').datagrid('deleteRow', getRowIndex(target));
		  signInputer("");
		  setInputer(row);
		  disableInputer("","disable");
	  }
  }
  
  //新增明细按钮点击事件
  function insert(target){
	  var rows=$('#detailList').datagrid('getRows');
	  if(!editComplete()&&rows.length>0){
		  $.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！'});
		  return false;
	  }
	  hideBtn(1);
	  var rows=$('#detailList').datagrid('getRows');
	  var index=rows.length;
	  $('#detailList').datagrid('insertRow',{
		  index:index,
		  row: {
			  newRow:1
		  }
	  });
	  $('#detailList').datagrid('selectRow',index);
	  $('#detailList').datagrid('beginEdit',index);
	  edIndex=index;
	  if(rows.length>=2){
		  var defaultResume=rows[index-1].resume;
		  if($("#getLastResume").val()==1){
			  $($('#detailList').datagrid('getEditor',{index:index,field:'resume'}).target).combotree({onLoadSuccess:function(){
				  $($('#detailList').datagrid('getEditor',{index:index,field:'resume'}).target).combotree("setText",defaultResume);
			  }});
		  }
		  var defaultDebit=0;
		  var defaultCredit=0;
		  var credit=0;
		  var debit=0;
		  for(var i=0;i<rows.length-1;i++){
			  debit=parseFloat(rows[i].debitAmount)+parseFloat(debit);
			  credit=parseFloat(rows[i].creditAmount)+parseFloat(credit);
		  }
		  var balance=debit-credit;
		  if(balance>0){
			  defaultCredit=balance;
		  }else if(balance<0){
			  defaultDebit=-balance;
		  }
		  $($('#detailList').datagrid('getEditor',{index:index,field:'debitAmount'}).target).textbox("setValue",defaultDebit.toFixed(2));
		  $($('#detailList').datagrid('getEditor',{index:index,field:'creditAmount'}).target).textbox("setValue",defaultCredit.toFixed(2));
		  $($('#detailList').datagrid('getEditor',{index:index,field:'debitAmount'}).target).textbox("setText",defaultDebit.toFixed(2).toString());
		  $($('#detailList').datagrid('getEditor',{index:index,field:'creditAmount'}).target).textbox("setText",defaultCredit.toFixed(2).toString());
	  }
  }
  
//保存按钮点击事件
$("#save").click(function(){
	if(!editComplete()){
		$.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！'});
		return;
	};
	var details=$("#detailList").datagrid('getRows');
	var footer=$("#detailList").datagrid('getFooterRows');
	if(details.length<2){
		$.fool.alert({msg:'必须添加至少两条明细。'});
		return;
	}
    if(footer[0].debitAmount!=footer[0].creditAmount){
    	$.fool.alert({msg:'借方与贷方金额不平衡。'});
		return;
	}
	var vo=$('#form').serialize(); 
	$('#form').form('enableValidation');
	if($('#form').form('validate')){
		$('#save').attr("disabled","disabled");
		$.post('${ctx}/voucher/save',vo+"&details="+JSON.stringify(details),function(data){
			if(data.returnCode =='0'){
				$.fool.alert({time:1000,msg:"保存成功！",fn:function(){
					$('#addBox').window('close');
	    			$('#save').removeAttr("disabled");
	    			$('#voucherList').datagrid('reload');
				}});
			}else if(data.returnCode =='1'){
	    		$.fool.alert({time:1000,msg:data.message});
	    		$('#save').removeAttr("disabled");
	    	}else{
	    		$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
	    		$('#save').removeAttr("disabled");
    		}
		});
	};
});

//下一页凭证
$("#nextVoucher").click(function(){
	if('${obj.recordStatus}'==null||'${obj.recordStatus}'==''||'${obj.recordStatus}'==0){
		if(!editComplete()){
			$.fool.alert({msg:'你还有没编辑完成的凭证明细,请先保存！'});
			return;
		};
		var details=$("#detailList").datagrid('getRows');
		var footer=$("#detailList").datagrid('getFooterRows');
		if(details.length<2){
			$.fool.alert({msg:'必须添加至少两条明细。'});
			return;
		}
	    if(footer[0].debitAmount!=footer[0].creditAmount){
	    	$.fool.alert({msg:'借方与贷方金额不平衡。'});
			return;
		}
		var vo=$('#form').serialize();
		$('#form').form('enableValidation');
		if($('#form').form('validate')){
			$('#save').attr("disabled","disabled");
			if(vo!=initialVo||JSON.stringify(details)!=JSON.stringify(initialDetail)){
				$.post('${ctx}/voucher/save',vo+"&details="+JSON.stringify(details),function(data){
					if(data.returnCode =='0'){
						$.fool.alert({msg:"保存成功！",fn:function(){
							var index=$('#voucherList').datagrid('getRowIndex',"${obj.fid}");
							var rows=$('#voucherList').datagrid('getRows');
							var fid="";
							var status="";
							if(rows[index-1]){
								fid=rows[index-1].fid;
								status=rows[index-1].recordStatus;
							}
							if(fid==""){
								$('#addBox').window('setTitle',"新增凭证");
								$('#addBox').window('refresh',"${ctx}/voucher/add");
							}else{
								if(status==1||status==2||status==3){
									$('#addBox').window('setTitle',"查看凭证");
								}else{
									$('#addBox').window('setTitle',"修改凭证");
								}
								$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
							}
			    			$('#save').removeAttr("disabled");
			    			$('#voucherList').datagrid('reload');
						}});
					}else if(data.returnCode =='1'){
			    		$.fool.alert({msg:data.message});
			    		$('#save').removeAttr("disabled");
			    	}else{
			    		$.fool.alert({msg:"系统正忙，请稍后再试。"});
			    		$('#save').removeAttr("disabled");
		    		}
				});
			}else{
				var index=$('#voucherList').datagrid('getRowIndex',"${obj.fid}");
				var rows=$('#voucherList').datagrid('getRows');
				var fid="";
				var status="";
				if(rows[index-1]){
					fid=rows[index-1].fid;
					status=rows[index-1].recordStatus;
				}
				if(fid==""){
					$('#addBox').window('setTitle',"新增凭证");
					$('#addBox').window('refresh',"${ctx}/voucher/add");
				}else{
					if(status==1||status==2||status==3){
						$('#addBox').window('setTitle',"查看凭证");
					}else{
						$('#addBox').window('setTitle',"修改凭证");
					}
					$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
				}
    			$('#save').removeAttr("disabled");
    			$('#voucherList').datagrid('reload');
			}
		};
	}else if('${obj.recordStatus}'==1||'${obj.recordStatus}'==2||'${obj.recordStatus}'==3){
		var index=$('#voucherList').datagrid('getRowIndex',"${obj.fid}");
		var rows=$('#voucherList').datagrid('getRows');
		var fid="";
		var status="";
		if(rows[index-1]){
			fid=rows[index-1].fid;
			status=rows[index-1].recordStatus;
		}
		if(fid==""){
			$('#addBox').window('setTitle',"新增凭证");
			$('#addBox').window('refresh',"${ctx}/voucher/add");
		}else{
			if(status==1||status==2||status==3){
				$('#addBox').window('setTitle',"查看凭证");
			}else{
				$('#addBox').window('setTitle',"修改凭证");
			}
			$('#addBox').window('refresh',"${ctx}/voucher/edit?fid="+fid);
		}
	}
});

//打印按钮
$("#print").click(function(){
	printVoucher($("#fid").val(),0);
});

//审核按钮
$("#verify").click(function(){
	verifyById($("#fid").val());
});

//反审核按钮
$("#unVerify").click(function(){
	unVerifyById($("#fid").val());
});

//作废按钮
$("#cancel").click(function(){
	cancelById($("#fid").val());
});

//反作废按钮
$("#unCancel").click(function(){
	unCancelById($("#fid").val());
});

//冲销按钮
$("#writeOff").click(function(){
	$('#addBox').window('setTitle',"新增凭证");
	$('#addBox').window('refresh',"${ctx}/voucher/writeOff?mark=1&fid="+fid);
});

//导出按钮
$("#export").click(function(){});

//签字按钮
$("#sign").click(function(){
	signById($("#fid").val());
});

//弹出框定义
function subjectChooser(target){
	  edIndex=getRowIndex(target);
	  editor=$(target);  
	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubjectDBC&singleSelect=true'});
}
/* $("#voucherWordName").click(function(){
	chooserWindow=$.fool.window({'title':"选择凭证字",href:'${ctx}/voucher/voucherWordWindow?okCallBack=selectVoucherWord&onDblClick=selectVoucherWordDBC&singleSelect=true'});
}); */
$("#supplierName").textbox('textbox').click(function(){
	chooserWindow=$.fool.window({'title':"选择供应商",href:'${ctx}/supplier/window?okCallBack=selectSup&onDblClick=selectSupDBC&singleSelect=true'});
});
$("#customerName").textbox('textbox').click(function(){
	chooserWindow=$.fool.window({'title':"选择客户",href:'${ctx}/customer/window?okCallBack=selectCus&onDblClick=selectCusDBC&singleSelect=true'});
});
$("#memberName").textbox('textbox').click(function(){
	chooserWindow=$.fool.window({'title':"选择职员",href:'${ctx}/member/window?okCallBack=selectMember&onDblClick=selectMemberDBC&singleSelect=true'});
});
$("#goodsName").textbox('textbox').click(function(){
	chooserWindow=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectGoods&onDblClick=selectGoodsDBC&singleSelect=true'});
});

//选择方法
function selectCus(rowData){
	$("#customerName").textbox("setValue",rowData[0].name);
	valueGrid('customerId',rowData[0].fid);
	valueGrid('customerName',rowData[0].name);
	chooserWindow.window('close');
}
function selectCusDBC(rowData){
	$("#customerName").textbox("setValue",rowData.name);
	valueGrid('customerId',rowData.fid);
	valueGrid('customerName',rowData.name);
	chooserWindow.window('close');
}
function selectMember(rowData){
	$("#memberName").textbox("setValue",rowData[0].username);
	valueGrid('memberId',rowData[0].fid);
	valueGrid('memberName',rowData[0].username);
	chooserWindow.window('close');
}
function selectMemberDBC(rowData){
	$("#memberName").textbox("setValue",rowData.username);
	valueGrid('memberId',rowData.fid);
	valueGrid('memberName',rowData.username);
	chooserWindow.window('close');
}
function selectSup(rowData) {
	$("#supplierName").textbox("setValue",rowData[0].name);
	valueGrid('supplierId',rowData[0].fid);
	valueGrid('supplierName',rowData[0].name);
	chooserWindow.window('close');
}
function selectSupDBC(rowData) {
	$("#supplierName").textbox("setValue",rowData.name);
	valueGrid('supplierId',rowData.fid);
	valueGrid('supplierName',rowData.name);
	chooserWindow.window('close');
}
function selectGoods(rowData) {
	$("#goodsName").textbox("setValue",rowData[0].name);
	valueGrid('goodsId',rowData[0].fid);
	valueGrid('goodsName',rowData[0].name);
	chooserWindow.window('close');
}
function selectGoodsDBC(rowData) {
	$("#goodsName").textbox("setValue",rowData.name);
	valueGrid('goodsId',rowData.fid);
	valueGrid('goodsName',rowData.name);
	chooserWindow.window('close');
}
/* function selectVoucherWord(rowData) {
	  $("#voucherWordName").focus();
	  if(rowData[0].children.length==0){
		  $("#voucherWordName").val(rowData[0].text);
		  $("#voucherWordId").val(rowData[0].id);
		  $.post("${ctx}/voucher/getDefaultMessage",{voucherDate:$("#voucherDate").datebox('getValue'),voucherWordId:rowData[0].id},function(data){
			  $("#voucherNumber").val(data.voucherNumber);
			  $("#getLastResume").val(data.getLastResume);
			  $("#subjectIsLeaf").val(data.subjectIsLeaf);
		  });
		  chooserWindow.window('close');
	  }else{
			$.fool.alert({msg:"请选择子节点"});
	  }
}
function selectVoucherWordDBC(rowData) {
	  $("#voucherWordName").focus();
	  if(rowData.children.length==0){
		  $("#voucherWordName").val(rowData.text);
		  $("#voucherWordId").val(rowData.id);
		  $.post("${ctx}/voucher/getDefaultMessage",{voucherDate:$("#voucherDate").datebox('getValue'),voucherWordId:rowData.id},function(data){
			  $("#voucherNumber").val(data.voucherNumber);
			  $("#getLastResume").val(data.getLastResume);
			  $("#subjectIsLeaf").val(data.subjectIsLeaf);
		  });
		  chooserWindow.window('close');
	  }else{
			$.fool.alert({msg:"请选择子节点"});
	  }
} */
function selectSubject(rowData){
	  if(/* $("#subjectIsLeaf").val()==1&& */rowData[0].children.length>0){
		  $.fool.alert({msg:"凭证填制必须是科目明细。"});
		  return false;
	  }
	  editor.combobox('setValue',rowData[0].name);
	  if(getValue("subjectId")==rowData[0].fid){
		  return false;
	  }else{
		  valueGrid('subjectId',rowData[0].fid);
		  valueGrid('subjectCode',rowData[0].code);
		  if(rowData[0].parentCode){
			  valueGrid('parentSubjectCode',rowData[0].parentCode);
		  }
		  if(rowData[0].parentName){
			  valueGrid('parentSubjectName',rowData[0].parentName);
		  }
		  initialSign(rowData[0]);
		  signInputer(rowData[0]);
      	  clearInputer();
      	  setInputer(rowData[0]);
	  }
	  disableInputer(rowData[0],"enable");
	  chooserWindow.window('close');
}
function selectSubjectDBC(rowData) {
	  if(/* $("#subjectIsLeaf").val()==1&& */rowData.children.length>0){
		  $.fool.alert({msg:"凭证填制必须是科目明细。"});
		  return false;
	  }
	  editor.combobox('setValue',rowData.name);
	  if(getValue("subjectId")==rowData.fid){
		  return false;
	  }else{
		  valueGrid('subjectId',rowData.fid);
		  valueGrid('subjectCode',rowData.code);
		  if(rowData.parentCode){
			  valueGrid('parentSubjectCode',rowData.parentCode);
		  }
		  if(rowData.parentName){
			  valueGrid('parentSubjectName',rowData.parentName);
		  }
		  initialSign(rowData);
		  signInputer(rowData);
      	  clearInputer();
      	  setInputer(rowData);
	  }
	  disableInputer(rowData,"enable");
	  chooserWindow.window('close');
}

//判断是否编辑完成
function editComplete(){
	var _dataPanel = $('#detailList').datagrid('getPanel');
	var _editing = _dataPanel.find(".datagrid-editable");
	$("#signBox").form('enableValidation');
	if(_editing.length>0||!$("#signBox").form("validate")){
		return false;
	}else{
		return true;
	}
}

//为编辑状态单元格赋值
function valueGrid(field,value){
	$grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="'+field+'"]').children().text(value);
}

//获取编辑状态单元格值
function getValue(field){
	return $grid.find(".datagrid-row[datagrid-row-index='"+edIndex+"']").find('[field="'+field+'"]').children().text();
}

//初始化列表的核算属性
function initialSign(data){
	valueGrid("currencySign",data.currencySign);
	valueGrid("supplierSign",data.supplierSign);
	valueGrid("customerSign",data.customerSign);
	valueGrid("departmentSign",data.departmentSign);
	valueGrid("memberSign",data.memberSign);
	valueGrid("warehouseSign",data.warehouseSign);
	valueGrid("projectSign",data.projectSign);
	valueGrid("goodsSign",data.goodsSign);
	valueGrid("quantitySign",data.quantitySign);
	
	valueGrid("unitName","");
	valueGrid("currencyName","");
	valueGrid("supplierName","");
	valueGrid("customerName","");
	valueGrid("departmentName","");
	valueGrid("memberName","");
	valueGrid("warehouseName","");
	valueGrid("projectName","");
	valueGrid("goodsName","");
	valueGrid("unitId","");
	valueGrid("currencyId","");
	valueGrid("supplierId","");
	valueGrid("customerId","");
	valueGrid("departmentId","");
	valueGrid("memberId","");
	valueGrid("warehouseId","");
	valueGrid("projectId","");
	valueGrid("goodsId","");
	valueGrid("quantity","");
	valueGrid("exchangeRate","");
	valueGrid("currencyAmount","");
}

//初始化核算输入框
function signInputer(data){
	if(data.currencySign!=1){
		valueGrid("currencyId","");
		valueGrid("currencyName","");
        $("#currencyName").parent().hide();
        $("#exchangeRate").parent().hide();
        $("#currencyAmount").parent().hide();
	}else{
		valueGrid("currencyId",data.currencyId);
		valueGrid("currencyName",data.currencyName);
        $("#currencyName").parent().show();
        $("#exchangeRate").parent().show();
        $("#currencyAmount").parent().show();
	}
	  
	if(data.supplierSign!=1){
		$("#supplierName").parent().hide();
	}else{
		$("#supplierName").parent().show();
	}
	  
	if(data.customerSign!=1){
		$("#customerName").parent().hide();
	}else{
		$("#customerName").parent().show();
	}
	  
	if(data.departmentSign!=1){
		$("#departmentName").parent().hide();
	}else{
		$("#departmentName").parent().show();
	}
	  
	if(data.memberSign!=1){
		$("#memberName").parent().hide();
	}else{
		$("#memberName").parent().show();
	}
	  
	if(data.warehouseSign!=1){
		$("#warehouseName").parent().hide();
	}else{
		$("#warehouseName").parent().show();
	}
	  
	if(data.projectSign!=1){
		$("#projectName").parent().hide();
	}else{
		$("#projectName").parent().show();
	}
	 
	if(data.goodsSign!=1){
		$("#goodsName").parent().hide();
	}else{
		$("#goodsName").parent().show();
	}
	  
	if(data.quantitySign!=1){
		valueGrid("unitId","");
		valueGrid("unitName","");
        $("#unitName").parent().hide();
        $("#quantity").parent().hide();
	}else{
		valueGrid("unitId",data.unitId);
		valueGrid("unitName",data.unitName);
        $("#unitName").parent().show();
        $("#quantity").parent().show();
	}
	var p=$("#signBox p");
	for(var i=0;i<p.length;i++){
		if($(p[i]).css('display')=="block"){
			$(p[i]).css('display','inline-block');
		}
	}
}

//清空核算输入框
function clearInputer(){
	$("#signBox").form('clear');
}

//核算输入框赋值
function setInputer(data){
	if(data.currencySign==1){
		$("#currencyName").textbox('setValue',data.currencyName);
		$("#exchangeRate").textbox('setValue',data.exchangeRate);
		$("#currencyAmount").textbox('setValue',data.currencyAmount);
	}
	  
	if(data.supplierSign==1){
		$("#supplierName").textbox('setValue',data.supplierName);
	}
	  
	if(data.customerSign==1){
		$("#customerName").textbox('setValue',data.customerName);
	}
	  
	if(data.departmentSign==1){
		$("#departmentName").combotree('setValue',data.departmentId);
	}
	  
	if(data.memberSign==1){
		$("#memberName").textbox('setValue',data.memberName);
	}
	  
	if(data.warehouseSign==1){
		$("#warehouseName").combotree('setValue',data.warehouseId);
	}
	  
	if(data.projectSign==1){
		$("#projectName").combotree('setValue',data.projectId);
	}
	 
	if(data.goodsSign==1){
		$("#goodsName").textbox('setValue',data.goodsName);
	}
	  
	if(data.quantitySign==1){
		$("#unitName").textbox('setValue',data.unitName);
        $("#quantity").textbox('setValue',data.quantity);
	}
}

//显示、隐藏核算输入框
function disableInputer(data,action){
	if(action=="disable"){
		$("#currencyName").textbox('disable');
        $("#exchangeRate").textbox('disable');
        $("#currencyAmount").textbox('disable');
        $("#supplierName").textbox('disable');
        $("#customerName").textbox('disable');
        $("#departmentName").combotree('disable');
        $("#memberName").textbox('disable');
        $("#warehouseName").combotree('disable');
        $("#projectName").combotree('disable');
        $("#goodsName").textbox('disable');
        $("#unitName").textbox('disable');
        $("#quantity").textbox('disable');
        $("#currencyName").textbox('disableValidation');
        $("#exchangeRate").textbox('disableValidation');
        $("#currencyAmount").textbox('disableValidation');
        $("#supplierName").textbox('disableValidation');
        $("#customerName").textbox('disableValidation');
        $("#departmentName").combotree('disableValidation');
        $("#memberName").textbox('disableValidation');
        $("#warehouseName").combotree('disableValidation');
        $("#projectName").combotree('disableValidation');
        $("#goodsName").textbox('disableValidation');
        $("#unitName").textbox('disableValidation');
        $("#quantity").textbox('disableValidation');
        $("#signBox").form("disableValidation");
	}else if(action=="enable"){
		if(data.currencySign==1){
			$("#currencyName").textbox('enable');
			$("#exchangeRate").textbox('enable');
		    $("#currencyAmount").textbox('enable');
		}else{
			$("#currencyName").textbox('disable');
			$("#exchangeRate").textbox('disable');
	        $("#currencyAmount").textbox('disable');
	        $("#currencyName").textbox('disableValidation');
			$("#exchangeRate").textbox('disableValidation');
		    $("#currencyAmount").textbox('disableValidation');
		}
		if(data.supplierSign==1){
			$("#supplierName").textbox('enable');
		}else{
			$("#supplierName").textbox('disable');
			$("#supplierName").textbox('disableValidation');
		}
		if(data.customerSign==1){
			$("#customerName").textbox('enable');
		}else{
			$("#customerName").textbox('disable');
			$("#customerName").textbox('disableValidation');
		}
		if(data.departmentSign==1){
			$("#departmentName").combotree('enable');
		}else{
			$("#departmentName").combotree('disable');
			$("#departmentName").combotree('disableValidation');
		}
		if(data.memberSign==1){
			$("#memberName").textbox('enable');
		}else{
			$("#memberName").textbox('disable');
			$("#memberName").textbox('disableValidation');
		}
	    if(data.warehouseSign==1){
	    	$("#warehouseName").combotree('enable');
		}else{
			$("#warehouseName").combotree('disable');
			$("#warehouseName").combotree('disableValidation');
		}
		if(data.projectSign==1){
			$("#projectName").combotree('enable');
		}else{
			$("#projectName").combotree('disable');
			$("#projectName").combotree('disableValidation');
		}
		if(data.goodsSign==1){
			$("#goodsName").textbox('enable');
		}else{
			$("#goodsName").combotree('disable');
			$("#goodsName").combotree('disableValidation');
		}
		if(data.quantitySign==1){
			$("#unitName").textbox('enable');
			$("#quantity").textbox('enable');
			
		}else{
			$("#unitName").textbox('disable');
	        $("#quantity").textbox('disable');
	        $("#unitName").textbox('disableValidation');
	        $("#quantity").textbox('disableValidation');
		}
	}
}

//加载页脚
function loadFooter(){
	var debit=0;
	var credit=0;
	var rows=$('#detailList').datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		if(!rows[i].debitAmount){
			debit=parseFloat(debit)+0;
		}else{
			debit=parseFloat(debit)+parseFloat(rows[i].debitAmount);
		}
		if(!rows[i].creditAmount){
			credit=parseFloat(credit)+0;
		}else{
			credit=parseFloat(credit)+parseFloat(rows[i].creditAmount);
		}
	}
	$('#detailList').datagrid('reloadFooter',[{resume:'<a class="btn-add" id="addDetail" title="插入" href="javascript:;" onclick="insert(this)"></a>', subjectName:'合计', debitAmount:debit.toFixed(2),creditAmount:credit.toFixed(2),signDetail:"0",action:"0"}]);
}

function hideBtn(hide,target){
	if(hide=="1"){
		$grid.find(".datagrid-row").find("[field='action']").find("a").hide();
	}else{
		$grid.find(".datagrid-row").find("[field='action']").find("a").show();
	}
}

$.extend($.fn.validatebox.defaults.rules, {
	intOrFloat:{
        validator: function (value) {
            var b = /^\d+(\.\d+)?$/i.test(value);
            if(!b)return false;
            else 
           	 return value>0&&parseFloat(value)<=9999999999.9999999999&&parseFloat(value)>=-9999999999.9999999999;
        },
        message: '请输入数字，并确保大于0'
    },
    amount:{
    	validator:function(value,param){
    		return ((/^(([+]?[0-9]\d*)|\d)(\.\d{1,2})?$/).test(value)||(/^(([-]?[0-9]\d*)|\d)(\.\d{1,2})?$/).test(value))&&parseFloat(value)<=9999999999.99&&parseFloat(value)>=-9999999999.99;
    	},
    	message:'最多能输入10位数，小数精确到2位，请输入正确的金额'
    },
    exchangeRate:{
    	validator:function(value,param){
    		return ((/^(([+]?[0-9]\d*)|\d)(\.\d{1,4})?$/).test(value)||(/^(([-]?[0-9]\d*)|\d)(\.\d{1,4})?$/).test(value))&&parseFloat(value)<=9999999999.99&&parseFloat(value)>=-9999999999.99;
    	},
    	message:'最多能输入10位数，小数精确到4位，请输入正确的汇率'
    },
    voucherDate:{
    	validator:function(value,param){
    		var result="";
    		$.ajax({
    			url:"${ctx}/voucher/getDefaultMessage",
    			async:false,
    			data:{voucherDate:value},
    			success:function(data){
    				result=data.voucherDate;
    			}
    		});
    		if(result){
    			return true;
			}else{
				return false;
			}
    	},
    	message:'根据当前凭证日期找不到未结账的会计期间' 
    }
});
enterController('form');
</script>
</body>
</html>