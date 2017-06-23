<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
          <div class="form1 billForm" >
              <form id="form">
                <input name="fid" id="fid" type="hidden" value="${entity.fid}"/>
                <input name="parentId" id="parentId" type="hidden" value="${entity.parentId}"/>
                <input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>
                <input name="relationId" id="relationId" type="hidden" value="${entity.relationId}"/>
				<p><font><em>*</em>科目编号：</font><input id="code" name="code" class="textBox" value="${entity.code}"/></p>
				<p><font><em>*</em>科目名称：</font><input id="name" name="name" class="textBox" value="${entity.name}"/></p>
				<p><font><em>*</em>科目类型：</font><input id="type" name="type" class="textBox"/></p>
				<p><font><em>*</em>科目类别：</font><input id="subjectId" name="subjectId" class="textBox"/></p>
				<p><font>助记码：</font><input id="zjm" name="zjm" class="textBox" value="${entity.zjm}"/></p>
				<p><font><em>*</em>余额方向：</font><input id="direction" name="direction" class="textBox" value="${entity.direction}"/></p>
				<p><font><em>*</em>是否有效：</font><input id="enable" name="enable" class="textBox"/></p>
				<p><font>关联类别：</font><input id="relationType" name="relationType" class="textBox"/></p>
				<p id="relations"><font>关联项：</font><input id="relationName" name="relationName" class="textBox"/></p><br/>
				<p class="checkP"><font></font><input id="cashSubject" name="cashSubject" type="checkbox" value="1" status="${entity.cashSubject}"/>现金科目</p>
				<p class="checkP"><font></font><input id="bankSubject" name="bankSubject" type="checkbox" value="1" status="${entity.bankSubject}"/>银行科目</p><br/>
				<div class="auxSign auxSign1">
				<p>&emsp;辅助核算：</p><br/>
				<p class="checkP signs"><font></font><input id="supplierSign" name="supplierSign" type="checkbox" value="1" status="${entity.supplierSign}"/>核算供应商</p>
				<p class="checkP signs"><font></font><input id="customerSign" name="customerSign" type="checkbox" value="1" status="${entity.customerSign}"/>核算销售商</p>
				<p class="checkP signs"><font></font><input id="departmentSign" name="departmentSign" type="checkbox" value="1" status="${entity.departmentSign}"/>核算部门</p>
				<p class="checkP signs"><font></font><input id="memberSign" name="memberSign" type="checkbox" value="1" status="${entity.memberSign}"/>核算职员</p>
				<p class="checkP signs"><font></font><input id="warehouseSign" name="warehouseSign" type="checkbox" value="1" status="${entity.warehouseSign}"/>核算仓库</p>
				<p class="checkP signs"><font></font><input id="projectSign" name="projectSign" type="checkbox" value="1" status="${entity.projectSign}"/>核算项目</p>
				<p class="checkP signs"><font></font><input id="goodsSign" name="goodsSign" type="checkbox" value="1" status="${entity.goodsSign}"/>核算货品</p>
				</div>
				<div class="auxSign">
				<p>&emsp;其他核算：</p><br/>
				<p class="checkP" style="display: none"><font></font><input id="cashSign" name="cashSign" type="checkbox" value="1" status="${entity.cashSign}"/>核算现金流量</p>
				<p class="checkP" style="display: none"><font></font><input id="cussentAccountSign" name="cussentAccountSign" type="checkbox" value="1" status="${entity.cussentAccountSign}"/>核算往来帐</p>
				<p class="checkP"><font></font><input id="quantitySign" name="quantitySign" type="checkbox" value="1" status="${entity.quantitySign}"/>核算数量</p>
				<p class="checkP"><font></font><input id="currencySign" name="currencySign" type="checkbox" value="1" status="${entity.currencySign}"/>核算外币</p>
				</div>
				<p><font><em>*</em>单位：</font><input id="unitId" name="unitId" class="textBox"/></p>
				<p><font><em>*</em>币别：</font><input id="currencyId" name="currencyId" class="textBox"/></p>
				<p style="width:570px"><font>描述：</font><input id="describe" name="describe" class="textBox"  value="${entity.describe}" style="width:78%"/></p><br/>
				<p><font></font><input id="save" class="btn-blue2 btn-xs" type="button" value="保存" /></p> 
              </form>
          </div>

<script type="text/javascript">
var chooserWindow="";
var parentCode="${entity.code}";
var parentCode="";
$.ajax({
	url:"${ctx}/fiscalSubject/getById",
	async:false,
	data:{fid:$("#parentId").val()},
	success:function(data){
		parentCode=data.code;
	}
});
$(document).ready(function(){
	//禁用文本框提示
	$("input").attr('autocomplete','off');
	
	//表单控件初始化
	$("#code").validatebox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		validType:['subjectCode','isBank','code["#parentId"]']
	});
	$("#code").tooltip({
		position: 'right',
		content: '<span style="color:#fff">请输入编号，父科目与子科目用"."隔开。</span>',    
		onShow: function(){        
			$(this).tooltip('tip').css({            
				backgroundColor: '#FFFFCC',            
				borderColor: '#666',
			}); 
			$(this).tooltip('tip').find("span").css("color",'#000000');
		}
	});
	if("${entity.flag}"==0){
		$("#code").attr("readonly","readonly");
	}else{
		$("#code").removeAttr("readonly");
	}
	$("#name").validatebox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		validType:'isBank'
	});
	/* $("#relationType").combobox({
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		data:[{value:"1",text:'银行'},{value:"2",text:'供应商'},{value:"3",text:'客户'},{value:"4",text:'部门'},{value:"5",text:'职员'},{value:"6",text:'仓库'},{value:"7",text:'项目'},{value:"8",text:'货品'},{value:"9",text:'现金'}],
		onLoadSuccess:function(){
			if('${entity.relationType}'){
				$("#relationType").combobox('setValue','${entity.relationType}');
			}
		},
		onSelect:function(record){
			relation(record.value,"","");
			var checkP=$("#form").find(".signs");
			$(checkP).each(function(){
				$(this).find(":checkbox").removeAttr('checked');
			});
		}
	}); */
	$("#relationType").fool("dhxCombo",{
		width:160,
		height:32,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:[{value:"1",text:'银行'},{value:"2",text:'供应商'},{value:"3",text:'客户'},{value:"4",text:'部门'},{value:"5",text:'职员'},{value:"6",text:'仓库'},{value:"7",text:'项目'},{value:"8",text:'货品'},{value:"9",text:'现金'}],
		onLoadSuccess:function(){
			if('${entity.relationType}'){
				($("#relationType").next())[0].comboObj.setComboValue('${entity.relationType}');
			}
		},
		onSelectionChange:function(){
		    var value=($("#relationType").next())[0].comboObj.getSelectedValue();
			relation(value,"","");
			var checkP=$("#form").find(".signs");
			$(checkP).each(function(){
				$(this).find(":checkbox").removeAttr('checked');
			});
		}
	});
	/* $("#type").combobox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		data:[{value:"1",text:'资产'},{value:"2",text:'负债'},{value:"3",text:'共同'},{value:"4",text:'所有者权益'},{value:"5",text:'成本'},{value:"6",text:'损益'}],
		onLoadSuccess:function(){
			if('${entity.type}'){
				$("#type").combobox('setValue','${entity.type}');
			}
		},
	}); */
	$("#type").fool("dhxCombo",{
		width:160,
		height:32,
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:[{value:"1",text:'资产'},{value:"2",text:'负债'},{value:"3",text:'共同'},{value:"4",text:'所有者权益'},{value:"5",text:'成本'},{value:"6",text:'损益'}],
		onLoadSuccess:function(){
			if('${entity.type}'){
				($("#type").next())[0].comboObj.setComboValue('${entity.type}');
			}
		},
	});
	/* $("#subjectId").combotree({
		url:"${ctx}/fiscalSubject/subjectType",
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		onLoadSuccess:function(){
			if('${entity.subjectId}'){
				$("#subjectId").combotree('setValue','${entity.subjectId}');
			}
		},
		onBeforeSelect:function(node){
        	if(!$(this).tree('isLeaf',node.target)||!node.attributes){
        		return false;
        	};
        }
	}); */
	$("#subjectId").fool("dhxCombo",{
		width:160,
		height:32,
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:getComboData('${ctx}/fiscalSubject/subjectType',"tree"),
		onLoadSuccess:function(){
			if('${entity.subjectId}'){
				($("#subjectId").next())[0].comboObj.setComboValue('${entity.subjectId}');
			}
		},
	});
	$("#zjm").validatebox({
	});
	$("#describe").validatebox({
		validType:'maxLength[200]'
	});
	/* $("#direction").combobox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		data:[{value:"1",text:'借方'},{value:"-1",text:'贷方'}],
		onLoadSuccess:function(){
			if('${entity.direction}'){
				$("#direction").combobox('setValue','${entity.direction}');
			}
		}
	}); */
	$("#direction").fool("dhxCombo",{
		width:160,
		height:32,
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:[{value:"1",text:'借方'},{value:"-1",text:'贷方'}],
		onLoadSuccess:function(){
			if('${entity.direction}'){
				($("#direction").next())[0].comboObj.setComboValue('${entity.direction}');
			}
		},
	});
	$("#unitId").combotree({
		url:"${ctx}/unitController/getTree",
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		onLoadSuccess:function(){
			if('${entity.fid}'&&'${entity.unitId}'){
				$("#unitId").combotree('setValue','${entity.unitId}');
			}
		},
		onBeforeSelect:function(node){
        	if(!$(this).tree('isLeaf',node.target)){
        		return false;
        	};
        }
	});
	/* $("#unitId").fool("dhxCombo",{
		width:160,
		height:32,
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:getComboData('${ctx}/unitController/getTree',"tree"),
		onLoadSuccess:function(){
			if('${entity.fid}'&&'${entity.unitId}'){
				($("#unitId").next())[0].comboObj.setComboValue('${entity.unitId}');
			}
		},
	}); */
	/* $("#currencyId").combotree({
		url:"${ctx}/fiscalSubject/currencyType",
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		onLoadSuccess:function(){
			if('${entity.fid}'&&'${entity.currencyId}'){
				$("#currencyId").combotree('setValue','${entity.currencyId}');
			}
		},
		onBeforeSelect:function(node){
        	if(!$(this).tree('isLeaf',node.target)){
        		return false;
        	};
        }
	}); */
	$("#currencyId").fool("dhxCombo",{
		width:160,
		height:32,
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:getComboData('${ctx}/fiscalSubject/currencyType',"tree"),
		onLoadSuccess:function(){
			if('${entity.fid}'&&'${entity.currencyId}'){
				($("#currencyId").next())[0].comboObj.setComboValue('${entity.currencyId}');
			}
		},
	});
	/* $("#enable").combobox({
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		width:160,
		height:32,
		data:[{value:"0",text:'无效'},{value:"1",text:'有效'}],
		onLoadSuccess:function(){
			if('${entity.enable}'){
				$("#enable").combobox('setValue','${entity.enable}');
			}
		}
	}); */
	$("#enable").fool("dhxCombo",{
		width:160,
		height:32,
		required:true,
		missingMessage:'该项不能为空！',
		novalidate:true,
		editable:false,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
		focusShow:true,
		data:[{value:"0",text:'无效'},{value:"1",text:'有效'}],
		onLoadSuccess:function(){
			if('${entity.enable}'){
				($("#enable").next())[0].comboObj.setComboValue('${entity.enable}');
			}
		},
	});
	/* $("#relationName").combo({
		width:160,
		height:32,
		value:"${entity.relationName}"
	}); */
	relation("${entity.relationType}","${entity.relationId}","${entity.relationName}");
	initialCheckbox();
	
	//设置父节点ID
	if("${parentId}"){
		$("#parentId").val("${parentId}");
	}
	//设置科目编号
	if('${code}'){
		$("#code").val('${code}');
	}else if(!"${entity.fid}"){
		$("#code").val('');
	}
	if("${entity.flag}"=='0'){
		($("#relationType").next())[0].comboObj.disable();
		$("#relationName").attr('disabled','disabled');
		$(".auxSign1").find(":checkbox").attr('disabled','disabled');
	}
	/* //设置科目类型
	if("${type}"){
		$("#type").combobox("setValue","${type}");
	}else{
		$("#type").combobox({hasDownArrow:true,width:164,height:31});
		$("#type").combobox('enable');
	}
	//设置余额方向
	if("${direction}"){
		$("#direction").combobox("setValue","${direction}");
	}else{
		$("#direction").combobox({hasDownArrow:true,width:164,height:31});
		$("#direction").combobox('enable');
	} */
	var checkP=$("#form").find(".signs");
	$(checkP).each(function(){
		$(this).find(":checkbox").click(function(){
			($("#relationType").next())[0].comboObj.unSelectOption();
			$("#relationId").val("");
			($("#relationName").next())[0].comboObj.unSelectOption();
			$(($("#relationName").next())[0].comboObj.getInput()).validatebox({//用validatebox进行验证
				required:false,
			});
		});
	});
	
	$("#cashSubject").click(function(){
		$("#bankSubject").removeAttr("checked");
	});
	$("#bankSubject").click(function(){
		$("#cashSubject").removeAttr("checked");
	});
	
});

//勾选核算数量
$("#quantitySign").change(function(){
	if(!$("#quantitySign").prop("checked")){
		$("#unitId").combotree({width:164,height:31,editable:false,});
		$("#unitId").combotree("disableValidation");
		$("#unitId").combotree("disable");
	}else{
		$("#unitId").combotree({width:164,height:31,editable:false,});
		$("#unitId").combotree("enableValidation");
		$("#unitId").combotree("enable");
	}
});

//勾选核算外汇
$("#currencySign").change(function(){
	if(!$("#currencySign").prop("checked")){
		/* $("#currencyId").combobox({hasDownArrow:false,width:164,height:31,editable:false,}); */
		$(($("#currencyId").next())[0].comboObj.getInput()).validatebox({
			requried:false,
			novalidate:true
		});
		($("#currencyId").next())[0].comboObj.disable();
	}else{
		/* $("#currencyId").combobox({hasDownArrow:true,width:164,height:31,editable:false,}); */
		$(($("#currencyId").next())[0].comboObj.getInput()).validatebox({
			requried:true,
			novalidate:true
		});
		($("#currencyId").next())[0].comboObj.enable();
	}
});

//保存按钮点击事件
$('#save').click(function(e) {
	$('#form').form('enableValidation');
	if($('#form').form('validate')){
		var node=$("#subjectTree").tree('find',$("#parentId").val());
		var childs="";
		var name="";
		var code="";
		var index="";
		var text="";
		var nodeText="";
		var childCode="";
		if(node){
			childs=$("#subjectTree").tree('getChildren',node.target);
			name=$("#name").val();
			code=$("#code").val();
			nodeText=code+" "+name;
		}
		for(var i=0;i<childs.length;i++){
			index=(childs[i].text).indexOf(" ");
			text=(childs[i].text).slice(index+1);
			childCode=(childs[i].text).slice(0,index);
			if(nodeText!=childs[i].text && name==text && childCode!="${entity.code}"){
				$.fool.confirm({msg:"科目名称有重复，确定保存吗？",title:"确认",fn:function(b){
					if(b){
						$('#save').attr("disabled","disabled");
						var parentId=$("#parentId").val();
						$.post('${ctx}/fiscalSubject/save',$('#form').serialize(),function(data){
							dataDispose(data);
							console.log(data);
							if(data.result==0){
								$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
									if(dhxkey == 1){
				                        selectTab(dhxname,dhxtab);
				                    }
									$('#save').removeAttr("disabled");
									$('#editBox').window('close');
									$('#subjectList').trigger("reloadGrid");
									var searchType=$("#searchType").combobox("getValue");
									/* var searchType=($("#searchType").next())[0].comboObj.getSelectedValue(); */
									if(searchType!=2){
										$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").searchbox("getValue")},function(data){
											$('#subjectTree').tree('loadData',data);
											if($("#fid").val()&&$("#fid").val()!=""){
												parentId=$("#fid").val();
											}
											var node=$("#subjectTree").tree("find",parentId);
											$("#subjectTree").tree("expandTo",node.target);
											$(node.target).click();
										});
									}else{
										$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:($("#search").next())[0].comboObj.getSelectedValue()},function(data){
											$('#subjectTree').tree('loadData',data);
											if($("#fid").val()&&$("#fid").val()!=""){
												parentId=$("#fid").val();
											}
											var node=$("#subjectTree").tree("find",parentId);
											$("#subjectTree").tree("expandTo",node.target);
											$(node.target).click();
										});
									}
								}});
							}else if(data.result==1){
								$.fool.alert({msg:data.msg});
								$('#subjectList').trigger("reloadGrid");
								var id=$('#subjectTree').tree('getSelected').id;
								var searchType=$("#searchType").combobox("getValue");
								if(searchType!=2){
									$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").searchbox("getValue")},function(data){
										$('#subjectTree').tree('loadData',data);
										var node=$('#subjectTree').tree('find',id);
										$("#subjectTree").tree("expandTo",node.target);
										$(node.target).click();
									});
								}else{
									$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").combobox("getValue")},function(data){
										$('#subjectTree').tree('loadData',data);
										var node=$('#subjectTree').tree('find',id);
										$("#subjectTree").tree("expandTo",node.target);
										$(node.target).click();
									});
								}
							}else{
								$.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
								$("#save").removeAttr("disabled");
							}
							return true;
						});
					}else{
						return;
					}
				}});
				return false;
			}
		}
		$('#save').attr("disabled","disabled");
		$.post('${ctx}/fiscalSubject/save',$('#form').serialize(),function(data){
			dataDispose(data);	
			if(data.result==0){
				$.fool.alert({time:1000,msg:'保存成功！',fn:function(){
                    if(dhxkey == 1){
                        selectTab(dhxname,dhxtab);
                    }
					$('#save').removeAttr("disabled");
					var parentId=$("#parentId").val();
					$('#editBox').window('close');
					$('#subjectList').trigger("reloadGrid");
					var searchType=$("#searchType").combobox("getValue");
					/* var searchType=($("#searchType").next())[0].comboObj.getSelectedValue(); */
					if(searchType!=2){
						$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").searchbox("getValue")},function(data){
							$('#subjectTree').tree('loadData',data);
							if($("#fid").val()&&$("#fid").val()!=""){
								parentId=$("#fid").val();
							}
							var node=$("#subjectTree").tree("find",parentId);
							$("#subjectTree").tree("expandTo",node.target);
							$(node.target).click();
						});
					}else{
						$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").combobox("getValue")},function(data){
							$('#subjectTree').tree('loadData',data);
							if($("#fid").val()&&$("#fid").val()!=""){
								parentId=$("#fid").val();
							}
							var node=$("#subjectTree").tree("find",parentId);
							$("#subjectTree").tree("expandTo",node.target);
							$(node.target).click();
						});
					}
				}});
			}else if(data.result==1){
				$.fool.alert({msg:data.msg});
				$('#subjectList').trigger("reloadGrid");
				$('#save').removeAttr("disabled");
				var id=$('#subjectTree').tree('getSelected').id;
				var searchType=$("#searchType").combobox("getValue");
				if(searchType!=2){
					$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:$("#search").searchbox("getValue")},function(data){
						$('#subjectTree').tree('loadData',data);
						var node=$('#subjectTree').tree('find',id);
						$("#subjectTree").tree("expandTo",node.target);
						$(node.target).click();
					});
				}else{
					$.post('${ctx}/fiscalSubject/getTree?showDisable=1',{searchType:searchType,searchKey:($("#search").next())[0].comboObj.getSelectedValue()},function(data){
						$('#subjectTree').tree('loadData',data);
						var node=$('#subjectTree').tree('find',id);
						$("#subjectTree").tree("expandTo",node.target);
						$(node.target).click();
					});
				}
			}else{
				$.fool.alert({time:1000,msg:"系统繁忙，请稍后再试。"});
				$("#save").removeAttr("disabled");
			}
			return true;
		});
	}else{
		return false;
	}
});


//初始化checkbox方法
function initialCheckbox(){
	//初始化
	var checkboxs=$("#form p :checkbox");
	var radios=$("#form p :radio");
	var status="";
	for(var i=0;i<checkboxs.length;i++){
		status=$(checkboxs[i]).attr('status');
		if(status==1){
			$(checkboxs[i]).attr('checked','checked');
		}
	}
	
	//核算数量、外汇勾选状态初始化
	if(!$("#quantitySign").prop("checked")){
		$("#unitId").combotree('disable');
	}else{
		$("#unitId").combotree('enable');
		$("#unitId").combotree('setValue','${entity.unitId}');
		
	}
	if(!$("#currencySign").prop("checked")){
		($("#currencyId").next())[0].comboObj.disable();
	}else{
		($("#currencyId").next())[0].comboObj.enable();
		($("#currencyId").next())[0].comboObj.setComboValue('${entity.currencyId}');
	}
}

//初始化关联项控件
function relation(relationType,relationId,relationName){
	if(!relationId){
		relationId="";
	}
	if(!relationName){
		relationName="";
	}
	if(relationType==""){
		$("#relationName").fool("dhxCombo",{
			required:false,
			width:160,
			height:32,
			editable:false,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			data:[],
			value:relationId,
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==1){
		$("#relationName").fool("dhxCombo",{
			required:true,
			width:160,
			height:32,
			editable:false,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			data:getComboData('${ctx}/bankController/comboboxData?type=2'),
			value:relationId,
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==2){
		$("#relationName").fool("dhxComboGrid",{
			required:true,
			height:30,
			width:160,
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
			},
			editable:true,
			validType:"combogridValid['relationId']",
			filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8',
			data:getComboData(getRootPath()+'/supplier/vagueSearch?searchSize=8'),
			text:relationName,
			value:relationId,
			searchKey:"searchKey", 
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
		/* data:[{value:"1",text:'银行'},{value:"2",text:'供应商'},{value:"3",text:'客户'},{value:"4",text:'部门'},{value:"5",text:'职员'},{value:"6",text:'仓库'},{value:"7",text:'项目'},{value:"8",text:'货品'},{value:"9",text:'现金'}], */
	}else if(relationType==3){
		$("#relationName").fool("dhxComboGrid",{
			required:true,
			height:30,
			width:160,
			setTemplate:{
				input:"#name#",
				columns:[
							{option:'#code#',header:'编号',width:100},
							{option:'#name#',header:'名称',width:100},
						],
			},
			editable:true,
			validType:"combogridValid['relationId']",
			filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8',
			data:getComboData(getRootPath()+'/customer/vagueSearch?searchSize=8'),
			text:relationName,
			value:relationId,
			searchKey:"searchKey", 
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==5){
		$("#relationName").fool("dhxComboGrid",{
			required:true,
			height:30,
			width:160,
			setTemplate:{
				input:"#username#",
				columns:[
							{option:'#userCode#',header:'编号',width:100},
							{option:'#jobNumber#',header:'工号',width:100},
							{option:'#username#',header:'名称',width:100},
							{option:'#phoneOne#',header:'电话',width:100},
							{option:'#deptName#',header:'部门',width:100},
							{option:'#position#',header:'职位',width:100},
						],
			},
			editable:true,
			validType:"combogridValid['relationId']",
			filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8',
			data:getComboData(getRootPath()+'/member/vagueSearch?searchSize=8'),
			text:relationName,
			value:relationId,
			searchKey:"searchKey",
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==8){
		$("#relationName").fool("dhxComboGrid",{
			required:true,
			height:30,
			width:160,
			setTemplate:{
				input:"#name#",
				columns:[
				         {option:'#name#',header:'名称',width:100},
				         {option:'#code#',header:'编码',width:100},
				         {option:'#barCode#',header:'条码',width:100},
						],
			},
			editable:true,
			validType:"combogridValid['relationId']",
			filterUrl:getRootPath()+'/goods/vagueSearch?searchSize=6',
			data:getComboData(getRootPath()+'/goods/vagueSearch?searchSize=6'),
			text:relationName,
			value:relationId,
			searchKey:"searchKey", 
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==4){
		$("#relationName").fool("dhxCombo",{
			required:true,
			width:160,
			height:32,
			editable:false,
			setTemplate:{
				input:"#orgName#",
				option:"#orgName#"
			},
			data:getComboData('${ctx}/orgController/getAllTree',"tree"),
			value:relationId,
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==6){
		$("#relationName").fool("dhxCombo",{
			required:true,
			width:160,
			height:32,
			editable:false,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			data:getComboData('${ctx}/basedata/warehourseList',"tree"),
			value:relationId,
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==7){
		$("#relationName").fool("dhxCombo",{
			required:true,
			width:160,
			height:32,
			editable:false,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			data:getComboData('${ctx}/basedata/findSubAuxiliaryAttrTree?code=012',"tree"),
			value:relationId,
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}else if(relationType==9){
		$("#relationName").fool("dhxCombo",{
			required:true,
			width:160,
			height:32,
			editable:false,
			setTemplate:{
				input:"#name#",
				option:"#name#"
			},
			data:getComboData('${ctx}/bankController/comboboxData?type=1'),
			value:relationId,
			focusShow:true,
			onSelectionChange:function(){
				$("#relationId").val(($("#relationName").next())[0].comboObj.getSelectedValue());
			}
		});
	}
}
enterController('form');

$.extend($.fn.validatebox.defaults.rules, {
	code:{//验证编号
        validator: function (value, param) {
        	var code=$("#code").val();
        	if(!parentCode||parentCode=="ROOT"){
        		return true;
        	}
        	if(parentCode!=code.slice(0,code.lastIndexOf("."))){
        		return false;
        	}else{
        		return true;
        	}
         },
         message:'科目编号前缀与上一级不一致'

     },	
});

$("#code").change(function(){
	if("${entity.fid}"){
		return false;
	}
	var code=$(this).val();
	if(code=="ROOT"){
		code="";
	}
	$("#form").form("clear");
	$(this).val(code);
	if(code){
		var returner=true;
		$.ajax({
			url:"${ctx}/fiscalSubject/getByCode",
			async:false,
			data:{code:code},
			success:function(data){
				dataDispose(data);	
				if(data){
					$("#editBox").window('setTitle',"修改科目");
					$(".auxSign1").find(":checkbox").removeAttr('disabled');
					$("#parentId").val(data.parentId);
					$("#fid").val(data.fid);
					$("#name").val(data.name);
					$("#updateTime").val(data.updateTime);
					$("#relationId").val(data.relationId);
					($("#type").next())[0].comboObj.setComboValue(data.type);
					($("#subjectId").next())[0].comboObj.setComboValue(data.subjectId);
					$("#zjm").val(data.zjm);
					($("#direction").next())[0].comboObj.setComboValue(data.direction);
					($("#enable").next())[0].comboObj.setComboValue(data.enable);
					relation(data.relationType,data.relationId,data.relationName);
					if(data.cashSubject==1){
						$("#cashSubject").click();
					}
					if(data.bankSubject==1){
						$("#bankSubject").click();
					}
					if(data.supplierSign==1){
						$("#supplierSign").click();
					}
					if(data.customerSign==1){
						$("#customerSign").click();
					}
					if(data.departmentSign==1){
						$("#departmentSign").click();
					}
					if(data.memberSign==1){
						$("#memberSign").click();
					}
					if(data.warehouseSign==1){
						$("#warehouseSign").click();
					}
					if(data.projectSign==1){
						$("#projectSign").click();
					}
					if(data.goodsSign==1){
						$("#goodsSign").click();
					}
					if(data.cashSign==1){
						$("#cashSign").click();
					}
					if(data.cussentAccountSign==1){
						$("#cussentAccountSign").click();
					}
					if(data.quantitySign==1){
						$("#quantitySign").click();
						$("#unitId").combotree("setValue",data.unitId);
						$("#unitId").combotree("enable");
					}else{
						$("#unitId").combotree("disable");
					}
					if(data.currencySign==1){
						$("#currencySign").click();
						($("#currencyId").next())[0].comboObj.setComboValue(data.currencyId);
						($("#currencyId").next())[0].comboObj.enable();
					}else{
						($("#currencyId").next())[0].comboObj.disable();
					}
					$("#describe").val(data.describe);
					($("#relationType").next())[0].comboObj.enable();
					$("#relationName").removeAttr("disabled");
					($("#relationName").next())[0].comboObj.enable();
					returner=false;
					$('#form').form('validate');
				}else{
					$.ajax({
						url:"${ctx}/fiscalSubject/getByCode",
						async:false,
						data:{code:"ROOT"},
						success:function(data){
							$("#parentId").val(data.fid);
						}
					});
					$("#editBox").window('setTitle',"新增科目");
					/* ($("#relationType").next())[0].comboObj.disable();
					$("#relationName").attr('disabled','disabled');
					$(".auxSign1").find(":checkbox").attr('disabled','disabled'); */
					($("#relationType").next())[0].comboObj.enable();
					$("#relationName").removeAttr("disabled");
					($("#relationName").next())[0].comboObj.enable();
					$(".auxSign1").find(":checkbox").removeAttr('disabled');
					($("#type").next())[0].comboObj.setComboValue("");
					($("#subjectId").next())[0].comboObj.setComboValue("");
					($("#direction").next())[0].comboObj.setComboValue("");
					($("#enable").next())[0].comboObj.setComboValue("");
					returner=true;
				}
			}
		});
		if(!returner){
			return false;
		}
	}
	var index=code.lastIndexOf(".");
	var parentCode=code.slice(0,index);
	if(index!=-1){
		$.ajax({
			url:"${ctx}/fiscalSubject/getByCode",
			async:false,
			data:{code:parentCode},
			success:function(data){
				dataDispose(data);	
				if(data){
					$(".auxSign1").find(":checkbox").removeAttr('disabled');
					$("#parentId").val(data.fid);
					$("#updateTime").val(data.updateTime);
					$("#relationId").val(data.relationId);
					($("#type").next())[0].comboObj.setComboValue(data.type);
					($("#subjectId").next())[0].comboObj.setComboValue(data.subjectId);
					$("#zjm").val(data.zjm);
					($("#direction").next())[0].comboObj.setComboValue(data.direction);
					($("#enable").next())[0].comboObj.setComboValue(data.enable);
					relation(data.relationType,data.relationId,data.relationName);
					if(data.cashSubject==1){
						$("#cashSubject").click();
					}
					if(data.bankSubject==1){
						$("#bankSubject").click();
					}
					if(data.supplierSign==1){
						$("#supplierSign").click();
					}
					if(data.customerSign==1){
						$("#customerSign").click();
					}
					if(data.departmentSign==1){
						$("#departmentSign").click();
					}
					if(data.memberSign==1){
						$("#memberSign").click();
					}
					if(data.warehouseSign==1){
						$("#warehouseSign").click();
					}
					if(data.projectSign==1){
						$("#projectSign").click();
					}
					if(data.goodsSign==1){
						$("#goodsSign").click();
					}
					if(data.cashSign==1){
						$("#cashSign").click();
					}
					if(data.cussentAccountSign==1){
						$("#cussentAccountSign").click();
					}
					if(data.quantitySign==1){
						$("#quantitySign").click();
						$("#unitId").combotree("setValue",data.unitId);
						$("#unitId").combotree("enable");
					}else{
						$("#unitId").combotree("disable");
					}
					if(data.currencySign==1){
						$("#currencySign").click();
						($("#currencyId").next())[0].comboObj.setComboValue(data.currencyId);
						($("#currencyId").next())[0].comboObj.enable();
					}else{
						($("#currencyId").next())[0].comboObj.disable();
					}
					$("#describe").val(data.describe);
					($("#relationType").next())[0].comboObj.enable();
					$("#relationName").removeAttr("disabled");
					($("#relationName").next())[0].comboObj.enable();
					$('#form').form('validate');
				}else{
					$.ajax({
						url:"${ctx}/fiscalSubject/getByCode",
						async:false,
						data:{code:"ROOT"},
						success:function(data){
							$("#parentId").val(data.fid);
						}
					});
					/* ($("#relationType").next())[0].comboObj.disable();
					$("#relationName").attr('disabled','disabled');
					$(".auxSign1").find(":checkbox").attr('disabled','disabled'); */
					($("#relationType").next())[0].comboObj.enable();
					$("#relationName").removeAttr("disabled");
					($("#relationName").next())[0].comboObj.enable();
					$(".auxSign1").find(":checkbox").removeAttr('disabled');
				}
			}
		});
	}else{
		$.ajax({
			url:"${ctx}/fiscalSubject/getByCode",
			async:false,
			data:{code:"ROOT"},
			success:function(data){
				$("#parentId").val(data.fid);
			}
		});
		/* ($("#relationType").next())[0].comboObj.disable();
		$("#relationName").attr("disabled","disabled");
		$(".auxSign1").find(":checkbox").attr('disabled','disabled'); */
		/* ($("#relationName").next())[0].comboObj.disable(); */
		($("#relationType").next())[0].comboObj.enable();
		$("#relationName").removeAttr("disabled");
		($("#relationName").next())[0].comboObj.enable();
		$(".auxSign1").find(":checkbox").removeAttr('disabled');
		
		/* var root=$("#subjectTree").tree("getRoot");
		$("#parentId").val(root.id); */
	}
});
 </script>
</body>
</html>