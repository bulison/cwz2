<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<title>明细分类账</title>
<style>
    .form1 input[type=checkbox]{vertical-align: middle}
    .toolBox-button{
        margin:5px 10px 5px 0px
    }
    .form1 p.hideP{
        display:none;
    }
    #result{
        height: 100%
    }
    .form1 p {
        margin:5px 0 5px 0;
    }
    #curSubjectCode,#curSubjectName{
      border-color:transparent;
      font-size: 16px;
      font-weight: bold;
    }
</style>
</head>
<body>
<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>明细分类账</h1>
                </div>             
             </div>
    <div id="toolBox">
        <div class="toolBox-button">
        <fool:tagOpt optCode="subledLast">
            <a href="javascript:void(0)" id="prevSubject" class="btn-ora-export" onclick="searcher(-1)">上一科目</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="subledNext">
		    <a href="javascript:void(0)" id="nextSubject" class="btn-ora-export" onclick="searcher(1)">下一科目</a>
		</fool:tagOpt>
		<%-- <fool:tagOpt optCode="subledExport">
		    <a href="javascript:void(0)" id="export" class="btn-ora-export" >导出</a>
		</fool:tagOpt>
		<fool:tagOpt optCode="subledPrint">
		    <a href="javascript:void(0)" id="print" class="btn-ora-export">打印</a>
		</fool:tagOpt> --%>
		<fool:tagOpt optCode="subledSearch">
		    <a href="javascript:void(0)" id="search" class="btn-blue4 btn-s" onclick="searcher()">查询</a>
		</fool:tagOpt>
		    <a href="javascript:void(0)" id="clear" class="btn-blue4 btn-s" onclick="clearer()">清空</a>
		    <span style="vertical-align: middle;"><input id="flag" type="checkbox" checked="checked"/>分页</span>
	    </div>
        <div id="filterBox">
            <form class="form1">
                <p><font>当前科目编号：</font><input class="textBox" id="curSubjectCode" value='${subject.code}' readonly="readonly"/></p>
                <p><font>当前科目名：</font><input class="textBox" id="curSubjectName" value='${subject.name}' readonly="readonly"/></p>
                <p class=""><input id="curSubjectId" type="hidden" value="${subject.fid}"/></p>
                <p class=""><input id="supplierId" _name="FSUPPLIERID" type="hidden"/></p>
                <p class=""><input id="customerId" _name="FCUSTOMERID" type="hidden"/></p>
                <p class=""><input id="deptId" _name="FDEPTID" type="hidden"/></p>
                <p class=""><input id="memberId" _name="FMEMBERID" type="hidden"/></p>
                <p class=""><input id="projectId" _name="FPROJECTID" type="hidden"/></p>
                <p class=""><input id="warehouseId" _name="FWAREHOUSEID" type="hidden"/></p>
                <p class=""><input id="goodsId" _name="FGOODSID" type="hidden"/></p>
                <br/><p><font>开始期间：</font><input id="startPeriod" _name="FSTART_PERIOD_ID" endDate="${period.endDate}"/></p>
                <p><font>结束期间：</font><input id="endPeriod" _name="FEND_PERIOD_ID" endDate="${period.endDate}"/></p>
                <p><font>状态：</font><input id="status" _name="FVOUCHERSTATUS"/></p>
                <br><h3 style="display: inline-block;margin-left:10px">&emsp;详细筛选：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>
                <p class="hideP"><font>起始科目编号：</font><input id="startCode"/></p>
                <p class="hideP"><font>结束科目编号：</font><input id="endCode"/></p>
                <p class="hideP"><font>科目级别：</font><input id="subjectLevel" value=1 /></p>
                
                <p class="hideP"><font>供应商：</font><input id="supplierName"/></p>
                <p class="hideP"><font>客户：</font><input id="customerName"/></p>
                <p class="hideP"><font>部门：</font><input id="deptName"/></p>
                <p class="hideP"><font>人员：</font><input id="memberName"/></p>
                <p class="hideP"><font>项目：</font><input id="projectName"/></p>
                <p class="hideP"><font>仓库：</font><input id="warehouseName"/></p>
                <p class="hideP"><font>货品：</font><input id="goodsName"/></p><br/>
                
                <p><font>显示供应商</font><input class="checkbox" id="showSupplier" _name="FSHOWSUPPLIER" type="checkbox" value=""/></p>&ensp;&ensp;
                <p>显示客户<input class="checkbox" id="showCustomer" _name="FSHOWCUSTOMER" type="checkbox" value=""/></p>&ensp;&ensp;
                <p>显示部门<input class="checkbox" id="showDept" _name="FSHOWDEPT" type="checkbox" value=""/></p>&ensp;&ensp;
                <p>显示人员<input class="checkbox" id="showMember" _name="FSHOWMEMBER" type="checkbox" value=""/></p>&ensp;&ensp;
                <p>显示项目<input class="checkbox" id="showProject" _name="FSHOWPROJECT" type="checkbox" value=""/></p>&ensp;&ensp;
                <p>显示仓库<input class="checkbox" id="showWarehouse" _name="FSHOWWAREHOUSE" type="checkbox" value=""/></p>&ensp;&ensp;
                <p>显示货品<input class="checkbox" id="showGoods" _name="FSHOWGOODS" type="checkbox" value=""/></p>&ensp;&ensp;<br/>
            </form>
        </div>
    </div>
    <div id="resultBox"></div>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <script type="text/javascript">
        $(document).ready(function(){
        	/* searcher(); */
        });
        var chooserWindow="";
        $("#startCode,#endCode").textbox({
        	width:158,
        	height:30
        });
        $("#supplierName").fool("dhxComboGrid",{
    		prompt:"选择供应商",
    		height:30,
    		width:160,
    		setTemplate:{
    			input:"#name#",
    			columns:[
    						{option:'#code#',header:'编号',width:100},
    						{option:'#name#',header:'名称',width:100},
    					],
    		},
    		filterUrl:getRootPath()+'/supplier/vagueSearch?searchSize=8',
    		data:getComboData(getRootPath()+'/supplier/vagueSearch?searchSize=8&q='),
    		searchKey:"searchKey", 
    		focusShow:true,
    		onSelectionChange:function(){
    			$("#supplierId").val(($("#supplierName").next())[0].comboObj.getSelectedValue());
    		}
    	});
        $("#customerName").fool("dhxComboGrid",{
    		prompt:"选择客户",
    		height:30,
    		width:160,
    		setTemplate:{
    			input:"#name#",
    			columns:[
    						{option:'#code#',header:'编号',width:100},
    						{option:'#name#',header:'名称',width:100},
    					],
    		},
    		filterUrl:getRootPath()+'/customer/vagueSearch?searchSize=8',
    		data:getComboData(getRootPath()+'/customer/vagueSearch?searchSize=8&q='),
    		searchKey:"searchKey", 
    		focusShow:true,
    		onSelectionChange:function(){
    			$("#customerId").val(($("#customerName").next())[0].comboObj.getSelectedValue());
    		}
    	});
        $("#memberName").fool("dhxComboGrid",{
    		prompt:"选择人员",
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
    		filterUrl:getRootPath()+'/member/vagueSearch?searchSize=8',
    		data:getComboData(getRootPath()+'/member/vagueSearch?searchSize=8'),
    		searchKey:"searchKey",
    		focusShow:true,
    		onSelectionChange:function(){
    			$("#memberId").val(($("#memberName").next())[0].comboObj.getSelectedValue());
    		}
    	});
        $("#goodsName").fool("dhxComboGrid",{
    		prompt:"选择货品",
    		height:30,
    		width:160,
    		setTemplate: {
                input: "#name#",
                columns: [
                    {option: '#name#', header: '名称', width: 100},
                    {option: '#code#', header: '编码', width: 100},
                    {option: '#barCode#', header: '条码', width: 100},
                ],
            },
    		filterUrl: getRootPath() + '/goods/vagueSearch?searchSize=8',
            data: getComboData(getRootPath() + '/goods/vagueSearch?searchSize=8'),
    		searchKey:"searchKey", 
    		focusShow:true,
    		onSelectionChange:function(){
    			$("#goodsId").val(($("#goodsName").next())[0].comboObj.getSelectedValue());
    		}
    	});
        $("#subjectLevel").textbox({
        	validType:"subjectLevel",
        	required:true,
        	width:158,
        	height:30,
        	missingMessage:"该项为必填项"
        });
        $("#deptName").fool("dhxCombo",{
        	width:160,
        	height:30,
        	editable:false,
        	setTemplate:{
      		  input:"#orgName#",
      		  option:"#orgName#"
      		},
      		data:getComboData('${ctx}/orgController/getAllTree',"tree"),
      		onSelectionChange:function(){
      			$("#deptId").val(($("#deptName").next())[0].comboObj.getSelectedValue());
      		}
        });
        $("#projectName").fool("dhxCombo",{
        	width:160,
        	height:30,
        	editable:false,
        	setTemplate:{
      		  input:"#name#",
      		  option:"#name#"
      		},
      		data:getComboData('${ctx}/basedata/findSubAuxiliaryAttrTree?code=012',"tree"),
      		onSelectionChange:function(){
      			$("#projectId").val(($("#projectName").next())[0].comboObj.getSelectedValue());
      		}
        });
        $("#warehouseName").fool("dhxCombo",{
        	width:160,
        	height:30,
        	editable:false,
        	setTemplate:{
      		  input:"#name#",
      		  option:"#name#"
      		},
      		data:getComboData('${ctx}/basedata/warehourseList',"tree"),
      		onSelectionChange:function(){
      			$("#warehouseId").val(($("#warehouseName").next())[0].comboObj.getSelectedValue());
      		}
        });
        var startPeriodCombo=$('#startPeriod').fool("dhxCombo",{
        	width:160,
        	height:30,
    		editable:false,
    		required:true,
    		missingMessage:'该项不能为空！',
    		value:'${period.fid}',
    		setTemplate:{
    		  input:"#period#",
    		  option:"#period#"
    		},
    		data:getComboData('${ctx}/fiscalPeriod/getAll'),
    		focusShow:true,
    		onSelectionChange:function(){
        		$("#startPeriod").attr("endDate",($('#startPeriod').next())[0].comboObj.getSelectedText().endDate);
        	}
    	});
        /* $("#startPeriod").combobox({
        	url:"${ctx}/fiscalPeriod/getAll",
        	required:true,
    		valueField:"fid",
    		textField:"period",
        	width:158,
        	height:30,
        	editable:false,
        	value:'${period.fid}',
        	onSelect:function(record){
        		$("#startPeriod").attr("endDate",record.endDate);
        	}
        }); */
        var endPeriodCombo=$('#endPeriod').fool("dhxCombo",{
        	width:160,
        	height:30,
    		editable:false,
    		required:true,
    		missingMessage:'该项不能为空！',
    		validType:"compare",
    		value:'${period.fid}',
    		setTemplate:{
    		  input:"#period#",
    		  option:"#period#"
    		},
    		data:getComboData('${ctx}/fiscalPeriod/getAll'),
    		focusShow:true,
    		onSelectionChange:function(){
        		$("#endPeriod").attr("endDate",($('#endPeriod').next())[0].comboObj.getSelectedText().endDate);
        	}
    	});
        /* $("#endPeriod").combobox({
        	url:"${ctx}/fiscalPeriod/getAll",
        	required:true,
    		valueField:"fid",
    		textField:"period",
        	width:158,
        	height:30,
        	editable:false,
        	validType:"compare",
        	value:'${period.fid}',
        	onSelect:function(record){
        		$("#endPeriod").attr("endDate",record.endDate);
        	}
        }); */
        $("#status").fool("dhxCombo",{
        	width:160,
        	height:30,
    		editable:false,
    		setTemplate:{
    			input:"#name#",
    			option:"#name#"
    		},
    		focusShow:true,
    		data:[{value:"0",text:"未审核"},{value:"1",text:"审核"},{value:"3",text:"过账"}]
    	});
        /* $("#status").combobox({
        	width:158,
        	height:30,
        	editable:false,
        	multiple:true,
        	data:[{value:"0",text:"未审核"},{value:"1",text:"审核"},{value:"2",text:"过账"}]
        }); */
        
       /*  $("#flag").change(function(){
        	searcher();
    	}); */
        
        $(".checkbox").change(function(){
        	if($(this).val()!=1){
        		$(this).val(1);
        	}else{
        		$(this).val(0);
        	}
        });
        
      //展开、收起其他信息
  	  $("#openBtn").click(function(e) {
  				  $("#filterBox .hideP").css("display","inline-block");
  				  $('#openBtn').css("display","none");
  				  $('#closeBtn').css("display","inline-block");
  				  if($("#gbox_result").length>0){
  					$("#result").setGridHeight(200);
  				  }
  	  });
  	  $("#closeBtn").click(function(e) {
  				  $("#filterBox .hideP").css("display","none");
  				  $('#openBtn').css("display","inline-block");
  				  $('#closeBtn').css("display","none");	
  				  if($("#gbox_result").length>0){
  					$("#result").setGridHeight(300);
  				  }
  	  });
        
        //弹出框
       /*  $("#supplierName").textbox("textbox").click(function(){
        	chooserWindow=$.fool.window({'title':"选择供应商",href:'${ctx}/supplier/window?okCallBack=selectSup&onDblClick=selectSupDBC&singleSelect=true'});	
        });
        $("#customerName").textbox("textbox").click(function(){
        	chooserWindow=$.fool.window({'title':"选择客户",href:'${ctx}/customer/window?okCallBack=selectCus&onDblClick=selectCusDBC&singleSelect=true'});
        });
        $("#memberName").textbox("textbox").click(function(){
        	chooserWindow=$.fool.window({'title':"选择职员",href:'${ctx}/member/window?okCallBack=selectMember&onDblClick=selectMemberDBC&singleSelect=true'});
        });
        $("#goodsName").textbox("textbox").click(function(){
        	chooserWindow=$.fool.window({'title':"选择货品",href:'${ctx}/goods/window?okCallBack=selectGoods&onDblClick=selectGoodsDBC&singleSelect=true'});
        }); */
      //选择方法
       /*  function selectCus(rowData){
        	$("#customerId").val(rowData[0].fid);
        	$("#customerName").textbox("setValue",rowData[0].name);
        	chooserWindow.window('close');
        }
        function selectCusDBC(rowData){
        	$("#customerId").val(rowData.fid);
        	$("#customerName").textbox("setValue",rowData.name);
        	chooserWindow.window('close');
        }
        function selectMember(rowData){
        	$("#memberId").val(rowData[0].fid);
        	$("#memberName").textbox("setValue",rowData[0].username);
        	chooserWindow.window('close');
        }
        function selectMemberDBC(rowData){
        	$("#memberId").val(rowData.fid);
        	$("#memberName").textbox("setValue",rowData.username);
        	chooserWindow.window('close');
        }
        function selectSup(rowData) {
        	$("#supplierId").val(rowData[0].fid);
        	$("#supplierName").textbox("setValue",rowData[0].name);
        	chooserWindow.window('close');
        }
        function selectSupDBC(rowData) {
        	$("#supplierId").val(rowData.fid);
        	$("#supplierName").textbox("setValue",rowData.name);
        	chooserWindow.window('close');
        }
        function selectGoods(rowData) {
        	$("#goodsId").val(rowData[0].fid);
        	$("#goodsName").textbox("setValue",rowData[0].name);
        	chooserWindow.window('close');
        }
        function selectGoodsDBC(rowData) {
        	$("#goodsId").val(rowData.fid);
        	$("#goodsName").textbox("setValue",rowData.name);
        	chooserWindow.window('close');
        } */
        
        $.extend($.fn.validatebox.defaults.rules, {
        	subjectLevel:{
        		validator: function (value) {
        			var b = /^([+]?[0-9])+\d*$/i.test(value);
        	        if(!b){
        	        	return false;
        	        }else{
        	        	return value>=1;
        	        } 
        		},
        	        message: '请输入整数，并确保大于1'

        	},
        });
        function getConditions(){
        	var p=$(".form1").children("p");
        	var input;
        	var conditions=[];
        	for(var i=0;i<p.length;i++){
        		input=new Object;
        		input.aliasName=$(p[i]).children('input').attr("_name");
        		if($(p[i]).children('input').attr("_name")){
        			input.value=$(p[i]).children('input').val();
        		}else if($(p[i]).children('input').attr("textboxname")){
        			input.aliasName=$(p[i]).children('input').attr("textboxname");
        		}
        		if($(p[i]).children('span').attr("class")=="textbox"){
        			input.value=$(p[i]).children('input').textbox('getValue');
        		}else if($(p[i]).children('span').attr("class")=="textbox numberbox"){
        			input.value=$(p[i]).children('input').numberbox('getValue');
        		}else if($(p[i]).children('span').attr("class")=="textbox combo"){
        			if($(p[i]).children('input').combobox('getValues').length>1){
        				input.value=$(p[i]).children('input').combobox('getValues').toString();
        			}else{
        				input.value=$(p[i]).children('input').combobox('getValue');
        			}
        		}else if($(p[i]).children('span').attr("class")=="textbox combo datebox"){
        			input.value=$(p[i]).children('input').datebox('getValue'); 
        		}else if($(p[i]).find(".dhxDiv").length>0){
        			input.value=($(p[i]).children('input').next())[0].comboObj.getSelectedValue(); 
        		}
        		if(input.aliasName){
        			conditions.push(input);
        		}
         	}
        	return conditions;
        }
        
        function searcher(operationFlag){
        	var sPeriod=$("#startPeriod").next().form("validate");
        	var ePeriod=$("#endPeriod").next().form("validate");
        	if(!sPeriod||!ePeriod){
        		return;
        	}
        	var star=$("#startPeriod").attr("endDate");
            var end=$("#endPeriod").attr("endDate");
            var starStr=star.split("-").join("");
            var endStr=end.split("-").join("");
            if(parseInt(starStr)>parseInt(endStr)){
            	$.fool.alert({msg:'开始期间必须在结束期间之前'});
    			return false;
            }
        	var data = {};
        	var conditions=getConditions();
        	data.condition =JSON.stringify(conditions);
        	data.subjectLevel=$("#subjectLevel").val();
        	data.curSubjectId=$("#curSubjectId").val();
        	data.subjectStartCode=$("#startCode").textbox("getValue");
        	data.subjectEndCode=$("#endCode").textbox("getValue");
        	data.sysReportId="4028818652a4afc40152a504e8480087";
        	if(operationFlag){
        		data.operationFlag=operationFlag;
        	}
        	if($("#flag").prop('checked')){
        		data.flag = 1;
				$("#resultBox").load("${ctx}/detailCategoryAccount/report/query",data);
			}else{
				data.flag=0;
				$("#resultBox").load("${ctx}/detailCategoryAccount/report/query",data,function(){
					$('#pageaction').hide();
				});
			}
        }
        function clearer(){
        	var curSubjectId=$("#curSubjectId").val();
        	var curSubjectCode=$("#curSubjectCode").val();
        	var curSubjectName=$("#curSubjectName").val();
        	$(".form1").form("clear");
        	var inputs=$(".form1").find(".dhxDiv");
        	for(var i=0;i<inputs.length;i++){ 
        		if($(inputs[i]).prev().attr("id")!=="startPeriod"&&$(inputs[i]).prev().attr("id")!=="endPeriod"){
        			inputs[i].comboObj.setComboValue("");
        		}else{
        			inputs[i].comboObj.setComboValue('${period.fid}');
        		}
        	} 
        	$(".form1").form("validate");
        	($("#status").next())[0].comboObj.setComboValue("");
        	($("#status").next())[0].comboObj.setComboText("");
        	$("#curSubjectId").val(curSubjectId);
        	$("#curSubjectCode").val(curSubjectCode);
        	$("#curSubjectName").val(curSubjectName);
        	$("#subjectLevel").textbox("setValue",1);
        }
    </script>
</body>  
</html>
