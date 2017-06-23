<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
<title>计提税费</title>
<style>
#list{
  text-align: center;
}
#save{
  margin-top: 20px
}
#paction a{
	margin-right:5px;
}
</style>
</head>

<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>计提税费</h1>
                </div>             
             </div>
    <div id="inputer">
        <form id="form" class="form1" style="padding-bottom:0px;">
            <input name="baseSubjectId" id="baseSubjectId" type="hidden" <%-- value="${CODE_2221_02.fid}" --%>/>
            <input name="paySubjectId" id="paySubjectId" type="hidden" <%-- value="${CODE_6403.fid}" --%>/>
            <input name="addSubjectId" id="addSubjectId" type="hidden" <%-- value="${CODE_2221_09.fid}" --%>/>
            <input name="collectSubjectId" id="collectSubjectId" type="hidden" <%-- value="${CODE_1002.fid}" --%>/>
			<p><font><em>*</em>税费基数科目：</font><input name="baseSubjectName" id="baseSubjectName" <%-- value="${CODE_2221_02.name}" --%>/></p>
			<p><font><em>*</em>征收税费科目：</font><input name="collectSubjectName" id="collectSubjectName" <%-- value="${CODE_6403.name}" --%>/></p>
			<p><font><em>*</em>附加税费科目：</font><input name="addSubjectName" id="addSubjectName" <%-- value="${CODE_2221_09.name}" --%> /></p>
			<p><font><em>*</em>支付税费科目：</font><input name="paySubjectName" id="paySubjectName" <%-- value="${CODE_1002.name}" --%> /></p>
			<p><font><em>*</em>税点（%）：</font><input name="point" id="point"/></p>
			<p id="paction"><font></font><a href="javascript:;" id="addSub" class="btn-blue2 btn-s2">增加科目</a> <a id="deleteSub" class="btn-blue2 btn-s2">删除科目</a> <a id="deleteAll" class="btn-blue2 btn-s2">全部删除</a></p>
         </form>
    	<form id="vmake" class="form1" style="padding:0 0;">
    		<p><font><em>*</em>凭证日期：</font><input name="voucherDate" id="voucherDate"/></p>
    		<p><font><em>*</em>凭证字：</font><input name="voucherWordId" id="voucherWordId"/></p>
    		<p><font>凭证摘要：</font><input name="resume" id="voucherResume"/></p>
    		<a href="javascript:;" id="save" class="btn-yellow2 btn-xs">计提税费</a>
        	<a href="javascript:;" id="save2" class="btn-yellow2 btn-xs">支付税费</a>
    	</form>
    </div>
    <div id="list">
        <table id="subjectList"></table>
    </div>
<script type="text/javascript">
    var chooserWindow="";
    var tips='';
    $(function(){
    	//判断是否导入了标准的会计科目模板来赋予默认值
    	/* function getSubj(sname,code){
    		$.post("${ctx}/fiscalSubject/getByCode",{code:code},function(data){
        		if(data[0].name == "未交增值税"){
        			$("#baseSubjectName").combobox('setValue',"未交增值税");
        			$("#paySubjectName").combobox('setValue',"银行存款");
        			$("#addSubjectName").combobox('setValue',"应交教育附加费");
        			$("#collectSubjectName").combobox('setValue',"营业税金及附加");
        			$("#baseSubjectId").val("402881f554ebee260154ec037bd60072");
        			$("#paySubjectId").val("402881f554ebee260154ec0378bc0003");
        			$("#addSubjectId").val("402881f554ebee260154ec037bdb0079");
        			$("#collectSubjectId").val("402881f554ebee260154ec037f6900c5");
        		}
        		$("#"+sname+"Name").combobox('setValue',data.name);
        		$("#"+sname+"Id").val(data.id);
        	});
    	} */
    });
    
    $("#point").textbox({
    	value:7,
    	required:true,
    	missingMessage:"该项不能为空",
    	//validate:true,
    	height:32,
    	validType:['numMaxLength[10]','pointNum'],
    	onChange:function(nv,ov){
    		if(nv.indexOf('.') != -1){//判断是否有小数点
    			var max = nv.length-1;
    			var value = parseInt(nv);
    			if(parseInt(nv.substring(nv.indexOf('.')+1,max)) != 0){
    				value = parseFloat(nv).toFixed(2)+'';
    			}
    			$("#point").textbox('setValue',value);
    		}
    	}
    });
    /* $("#baseSubjectName").fool('subjectCombobox',{
    	url:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
    	width:174,
    	height:32,
  	    required:true,
  	    novalidate:true,
  	  	focusShow:true,
  	  	onClickIcon:function(){
  	  		$("#baseSubjectName").combobox("hidePanel");
        	$("#baseSubjectName").combobox('textbox').blur();
  	  		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectBaseSubject&onDblClick=selectBaseSubjectDBC&singleSelect=true&flag=1',
  	  			onLoad:function(){$('#baseSubjectName').combobox('hidePanel');}});//解决IE点击弹窗下拉框不消失问题
  	  	},
  	  	onSelect:function(record){
  	  		$("#baseSubjectId").val(record.id);
  	  	}
  	    //hasDownArrow:false
    }); */
		
    var baseSubjectCombo = $('#baseSubjectName').fool('dhxComboGrid',{
		width:174,
		height:32,
		value:"${CODE_2221_02.fid}",
		text:"${CODE_2221_02.name}",
		novalidate:true,
		required:true,
		focusShow:true,
	  	data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  	filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  	searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
        toolsBar:{
	  	    name:"科目",
            addUrl:"/fiscalSubject/manage",
            refresh:true
        },
	  	onSelectionChange:function(){
        	$("#baseSubjectId").val(($('#baseSubjectName').next())[0].comboObj.getSelectedValue());
        	$("#baseSubjectName").attr("subjectType",($('#baseSubjectName').next())[0].comboObj.getSelectedText().type);
        }
	});
	$('#baseSubjectName').next().find(".dhxcombo_select_button").click(function(){
		baseSubjectCombo.closeAll();
		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectBaseSubject&onDblClick=selectBaseSubjectDBC&singleSelect=true&flag=1',
	  			onLoad:function(){baseSubjectCombo.closeAll();}});//解决IE点击弹窗下拉框不消失问题
	});
    
    /* $("#paySubjectName").fool('subjectCombobox',{
    	url:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
    	width:174,
  	    height:32,
  	    required:true,
  	    novalidate:true,
  	  	focusShow:true,
  	  	onClickIcon:function(){
  	  		$("#paySubjectName").combobox("hidePanel");
        	$("#paySubjectName").combobox('textbox').blur();
    	  	chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectPaySubject&onDblClick=selectPaySubjectDBC&singleSelect=true&flag=1',
  	  			onLoad:function(){$('#paySubjectName').combobox('hidePanel');}});//解决IE点击弹窗下拉框不消失问题
	  	},
  	  	onSelect:function(record){
  	  		$("#paySubjectId").val(record.id);
  	  	}
  	    //hasDownArrow:false
    }); */
    
    var paySubjectCombo = $('#paySubjectName').fool('dhxComboGrid',{
    	value:"${CODE_1002.fid}",
		text:"${CODE_1002.name}",
		width:174,
		height:32,
		novalidate:true,
		required:true,
		focusShow:true,
	  	data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  	filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  	searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
        toolsBar:{
            name:"科目",
            addUrl:"/fiscalSubject/manage",
            refresh:true
        },
	  	onSelectionChange:function(){
        	$("#paySubjectId").val(($('#paySubjectName').next())[0].comboObj.getSelectedValue());
        	$("#paySubjectName").attr("subjectType",($('#paySubjectName').next())[0].comboObj.getSelectedText().type);
        }
	});
	$('#paySubjectName').next().find(".dhxcombo_select_button").click(function(){
		paySubjectCombo.closeAll();
		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectPaySubject&onDblClick=selectPaySubjectDBC&singleSelect=true&flag=1',
	  			onLoad:function(){paySubjectCombo.closeAll();}});//解决IE点击弹窗下拉框不消失问题
	});
    
    /* $("#addSubjectName").fool('subjectCombobox',{
    	url:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
    	width:174,
  	    height:32,
  	    required:true,
  	    novalidate:true,
  	  	focusShow:true,
  	  	onClickIcon:function(){
  	  		$("#addSubjectName").combobox("hidePanel");
        	$("#addSubjectName").combobox('textbox').blur();
    	  	chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectTaxSubject&onDblClick=selectTaxSubjectDBC&singleSelect=true&flag=1',
  	  			onLoad:function(){$('#addSubjectName').combobox('hidePanel');}});//解决IE点击弹窗下拉框不消失问题
	  	},
  	  	onSelect:function(record){
  	  		$("#addSubjectId").val(record.id);
  	  	}
  	    //hasDownArrow:false
    }); */
    
    var addSubjectCombo = $('#addSubjectName').fool('dhxComboGrid',{
    	value:"${CODE_2221_09.fid}",
		text:"${CODE_2221_09.name}",
		width:174,
		height:32,
		novalidate:true,
		required:true,
		focusShow:true,
	  	data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  	filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  	searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
        toolsBar:{
	  	    name:"科目",
            addUrl:"/fiscalSubject/manage",
            refresh:true
        },
	  	onSelectionChange:function(){
        	$("#addSubjectId").val(($('#addSubjectName').next())[0].comboObj.getSelectedValue());
        	$("#addSubjectName").attr("subjectType",($('#addSubjectName').next())[0].comboObj.getSelectedText().type);
        }
	});
	$('#addSubjectName').next().find(".dhxcombo_select_button").click(function(){
		addSubjectCombo.closeAll();
		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectTaxSubject&onDblClick=selectTaxSubjectDBC&singleSelect=true&flag=1',
	  			onLoad:function(){addSubjectCombo.closeAll();}});//解决IE点击弹窗下拉框不消失问题
	});
    
    /* $("#collectSubjectName").fool('subjectCombobox',{
    	url:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
    	width:174,
  	    height:32,
  	    required:true,
  	    novalidate:true,
  	    focusShow:true,
  	  	onClickIcon:function(){
  	  		$("#collectSubjectName").combobox("hidePanel");
        	$("#collectSubjectName").combobox('textbox').blur();
  	  		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectCollectSubject&onDblClick=selectCollectSubjectDBC&singleSelect=true&flag=1',
  	  			onLoad:function(){$('#collectSubjectName').combobox('hidePanel');}});//解决IE点击弹窗下拉框不消失问题
	  	},
  	  	onSelect:function(record){
  	  		$("#collectSubjectId").val(record.id);
  	  	}
  	    //hasDownArrow:false
    }); */
    
    var collectSubjectCombo = $('#collectSubjectName').fool('dhxComboGrid',{
    	value:"${CODE_6403.fid}",
		text:"${CODE_6403.name}",
		width:174,
		height:32,
		novalidate:true,
		required:true,
		focusShow:true,
	  	data:getComboData("${ctx}/fiscalSubject/getSubject?leafFlag=1&q="),
	  	filterUrl:"${ctx}/fiscalSubject/getSubject?leafFlag=1",
	  	searchKey:"q", 
	  	setTemplate:{
	  		input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
	  	},
        toolsBar:{
            name:"科目",
            addUrl:"/fiscalSubject/manage",
            refresh:true
        },
	  	onSelectionChange:function(){
        	$("#collectSubjectId").val(($('#collectSubjectName').next())[0].comboObj.getSelectedValue());
        	$("#collectSubjectName").attr("subjectType",($('#collectSubjectName').next())[0].comboObj.getSelectedText().type);
        }
	});
	$('#collectSubjectName').next().find(".dhxcombo_select_button").click(function(){
		collectSubjectCombo.closeAll();
		chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectCollectSubject&onDblClick=selectCollectSubjectDBC&singleSelect=true&flag=1',
	  			onLoad:function(){collectSubjectCombo.closeAll();}});//解决IE点击弹窗下拉框不消失问题
	});
    
    $("#voucherDate").fool('datebox',{
		width:174,
		height:32,
		editable:true,
		inputDate:true,
		required:true,
		novalidate:true,
		focusShow:true
	});
    $.post('${ctx}/voucher/getDefaultMessage',function(data){
		$('#voucherDate').datebox('setValue',data.voucherDate);
	});
    
    /* $("#voucherWordId").fool('voucherWordTree',{
    	width:174,
		height:32,
		required:true,
		novalidate:true,
		onBeforeSelect:function(node){
			if(node.text != '请选择'){
				$('#voucherWordId').val(node.id);
			}
		},
		onLoadSuccess:function(node,data){
			console.log(data);
			for(var i = 0; i<data[0].children.length; i++){
				if($.trim(data[0].children[i].text) == '记'){
					$('#voucherWordId').combotree('setValue',data[0].children[i].id);
				}
			}
		}
	}); */
	var voucherWordData=getComboData(getRootPath()+'/basedata/voucherWord',"tree");
	voucherWordData[0].selected=true;
	var voucherWordCombo=$("#voucherWordId").fool('dhxCombo',{
		width:160,
		height:32,
		focusShow:true,
		editable:false,
		data:voucherWordData,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
        toolsBar:{
            name:"凭证字",
            addUrl:"/basedata/listAuxiliaryAttr",
            refresh:true
        },
		onLoadSuccess:function(){
			/* var childs=getComboData(getRootPath()+'/basedata/voucherWord',"tree");
			for(var i=0;i<childs.length;i++){
				if(childs[i].text.name=="记"){
					($("#voucherWordId").next())[0].comboObj.setComboValue(childs[i].value);
					($("#voucherWordId").next())[0].comboObj.setComboText(childs[i].text.name);
					break;
				}
			} */
		},
	});
    var voucherResumeValue = "";
    $.ajax({
        url: "${ctx}/basedata/resume?num=" + Math.random(),
        async: false,
        data: {},
        success: function (data) {
            voucherResumeValue = formatTree(data[0].children, "text", "id")
        }
    });
	var voucherResumeCombo=$("#voucherResume").fool('dhxComboGrid',{
		width:174,
		height:32,
		focusShow:true,
		editable:true,
        data:voucherResumeValue,
        filterUrl:'${ctx}/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num='+Math.random(),
		setTemplate:{
			input:"#name#",
			columns:[
                {option:'#name#',header:'内容',width:100}
			    ]
		},
        toolsBar:{
		    name:"摘要",
            addUrl:"/basedata/listAuxiliaryAttr",
            refresh:true
        },
	});

    voucherResumeCombo.hdr = null;

	var refresh = $(".mytoolsBar a.btn-refresh")[0];
	var sInput = $(voucherResumeCombo.getInput());
	    sInput.css("width",sInput.width()- 20 );
	    sInput.after("<a class='btn-save' id='resumeSave' href='javascript:;'><a/>")
        sInput.next().css({'float': 'right', 'margin': '5px 23px', 'display': ''});
    $("#resumeSave").click(function () {
        var name = ($('#voucherResume').next())[0].comboObj.getComboText();
        if (name == "") {
            $.fool.alert({msg: '摘要不能为空！', time: 1000});
            return;
        }
        $.ajax({
            url: '${ctx}/basedata/saveFast',
            type: 'POST',
            data: {name: name, categoryCode: '014'},
            success: function (data) {
                dataDispose(data);
                if (data) {
                    if (data.returnCode == 0) {
                        $.fool.alert({
                            msg: '摘要保存成功！', time: 1000, fn: function () {
                                newData = data.data;
                                voucherResumeValue.push({value: "", text: name});
                                //重新加载一下Combo
                                ($('#voucherResume').next())[0].comboObj.load({options: voucherResumeValue});
                                refresh.click();
                            }
                        });
                        /*sInput.next().css('display', 'none');*/
                    } else if (data.returnCode == 1) {
                        $.fool.alert({msg: data.msg});
                    } else {
                        $.fool.alert({msg: '系统繁忙，请稍后再试'});
                    }
                }
            }

        })
    });

    $("#subjectList").jqGrid({
		datatype:function(postdata){
			$.ajax({
				url: '${ctx}/accrueTaxation/query',
				data:postdata,
		        dataType:"json",
		        complete: function(data,stat){
		        	if(stat=="success") {
		        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		        		$("#subjectList")[0].addJSONData(data.responseJSON);
		        	}
		        }
			});
		},
		autowidth:true,
		height:$(window).height()-$("#inputer").height()-120,
		viewrecords:true,
        multiselect:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:[ 
                    {name : 'fid',label : 'fid',hidden:true}, 
//                    {name : '',label :"",align:'center',width:"20px",formatter:function(cellvalue, options, rowObject){ return "<input class='checker' fid='"+rowObject.fid+"' type='checkbox'/>"; }},
	                {name : 'baseSubjectId',label : 'outSubjectId',hidden:true}, 
	                {name : 'collectSubjectId',label : 'inSubjectId',hidden:true},
	                {name : 'addSubjectId',label : 'outSubjectId',hidden:true}, 
	                {name : 'paySubjectId',label : 'inSubjectId',hidden:true},
	                
	                {name:'baseSubjectName',label:'税费基数科目',align:'center',width:"100px"},
	                {name:'collectSubjectName',label:'征收税费科目',align:'center',width:"100px"},
	                {name:'addSubjectName',label:'附加税费科目',align:'center',width:"100px"},
	                {name:'paySubjectName',label:'支付税费科目',align:'center',width:"100px"},
	                {name:'point',label:'税点',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
	    	        	  return cellvalue+"%";
	                }},
	              ],
	}); 
    
    /* $("#baseSubjectName").next().find('.textbox-icon').click(function (){
  	  $("#baseSubjectName").combobox("hidePanel");
  	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectBaseSubject&onDblClick=selectBaseSubjectDBC&singleSelect=true&flag=1'});
    }); */
    /* $("#addSubjectName").next().find('.textbox-icon').click(function (){
  	  $("#addSubjectName").combobox("hidePanel");
  	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectTaxSubject&onDblClick=selectTaxSubjectDBC&singleSelect=true&flag=1'});
    }); */
   /*  $("#collectSubjectName").next().find('.textbox-icon').click(function (){
    	  $("#collectSubjectName").combobox("hidePanel");
    	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectCollectSubject&onDblClick=selectCollectSubjectDBC&singleSelect=true&flag=1'});
      }); */
    /* $("#paySubjectName").next().find('.textbox-icon').click(function (){
  	  $("#paySubjectName").combobox("hidePanel");
  	  chooserWindow=$.fool.window({'title':"选择科目",href:'${ctx}/fiscalSubject/window?okCallBack=selectPaySubject&onDblClick=selectPaySubjectDBC&singleSelect=true&flag=1'});
    }); */
    function selectBaseSubject(rowData){
  	  $("#baseSubjectId").val(rowData[0].fid);
  	  $("#baseSubjectCode").val(rowData[0].code);
  	  baseSubjectCombo.setComboText(rowData[0].name);
  	  baseSubjectCombo.setComboValue(rowData[0].fid);
  	  chooserWindow.window("close");
    }
    function selectBaseSubjectDBC(rowData){
  	  $("#baseSubjectId").val(rowData.fid);
  	  $("#baseSubjectCode").val(rowData.code);
  	  baseSubjectCombo.setComboText(rowData.name);
	  baseSubjectCombo.setComboValue(rowData.fid);
  	  chooserWindow.window("close");
    }
    function selectTaxSubject(rowData){
  	  $("#addSubjectId").val(rowData[0].fid);
  	  $("#addSubjectCode").val(rowData[0].code);
  	  addSubjectCombo.setComboText(rowData[0].name);
  	  addSubjectCombo.setComboValue(rowData[0].fid);
  	  chooserWindow.window("close");
    }
    function selectTaxSubjectDBC(rowData){
  	  $("#addSubjectId").val(rowData.fid);
  	  $("#addSubjectCode").val(rowData.code);
  	  addSubjectCombo.setComboText(rowData.name);
 	  addSubjectCombo.setComboValue(rowData.fid);
  	  chooserWindow.window("close");
    }
    function selectPaySubject(rowData){
  	  $("#paySubjectId").val(rowData[0].fid);
  	  $("#paySubjectCode").val(rowData[0].code);
  	  paySubjectCombo.setComboText(rowData[0].name);
  	  paySubjectCombo.setComboValue(rowData[0].fid);
  	  chooserWindow.window("close");
    }
    function selectPaySubjectDBC(rowData){
  	  $("#paySubjectId").val(rowData.fid);
  	  $("#paySubjectCode").val(rowData.code);
  	  paySubjectCombo.setComboText(rowData.name);
	  paySubjectCombo.setComboValue(rowData.fid);
  	  chooserWindow.window("close");
    }
    function selectCollectSubject(rowData){
    	  $("#collectSubjectId").val(rowData[0].fid);
    	  $("#collectSubjectCode").val(rowData[0].code);
    	  collectSubjectCombo.setComboText(rowData[0].name);
    	  collectSubjectCombo.setComboValue(rowData[0].fid);
    	  chooserWindow.window("close");
    }
    function selectCollectSubjectDBC(rowData){
    	  $("#collectSubjectId").val(rowData.fid);
    	  $("#collectSubjectCode").val(rowData.code);
    	  collectSubjectCombo.setComboText(rowData.name);
    	  collectSubjectCombo.setComboValue(rowData.fid);
    	  chooserWindow.window("close");
    }
    
    
    $("#addSub").click(function(){
    	$("#baseSubjectName").next().form("enableValidation");
    	$("#addSubjectName").next().form("enableValidation");
    	$("#paySubjectName").next().form("enableValidation");
    	$("#collectSubjectName").next().form("enableValidation");
    	$("#point").numberbox("enableValidation");
    	var r=($("#baseSubjectName").next().form("validate")&&$("#addSubjectName").next().form("validate")&&$("#paySubjectName").next().form("validate")&&$("#point").numberbox("isValid")&&$("#collectSubjectName").next().form("validate"));
    	if(r){
    		if($('#addSubjectId').val()==$("#collectSubjectId").val()||$('#baseSubjectId').val()==$("#collectSubjectId").val()||$('#baseSubjectId').val()==$("#addSubjectId").val()){
        		$.fool.alert({msg:"税费基数科目、征收税费科目、附加税费科目不能相同，请重新选择。",fn:function(){}});
        		return;
        	}
        	var obj={
        			baseSubjectId:$("#baseSubjectId").val(),
        			addSubjectId:$("#addSubjectId").val(),
        			paySubjectId:$("#paySubjectId").val(),
        			collectSubjectId:$("#collectSubjectId").val(),
        			point:$("#point").numberbox("getValue"),
        			voucherWordId:voucherWordCombo.getSelectedValue()
        	};
        	$.ajax({
    			type : 'post',
    			url : '${ctx}/accrueTaxation/save',
    			data : obj,
    			dataType : 'json',
    			success : function(data) {
    				if(data.returnCode == '0'){
    					$('#subjectList').trigger("reloadGrid"); 
    				}else{
    					$.fool.alert({msg:data.message,fn:function(){
    					}});
    				}
        		},
        		error:function(){
        			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
        		}
    		});
    	}
    });
    function getFid(rowid) {
        return $('#'+rowid+' [aria-describedby="subjectList_fid"]').text();
    }
    $("#deleteSub").click(function(){
        var ids = $('#subjectList').jqGrid('getGridParam','selarrrow');
        var fids="";
        if(ids.length<1){
            $.fool.alert({msg:"请选择要删除的数据。"});
            return false;
        }
        for(var i=0;i<ids.length;i++){
            fids += getFid(ids[i])+',';
        }
        $.ajax({
            type : 'post',
            url : '${ctx}/accrueTaxation/delete',
            data : {fids:fids},
            dataType : 'json',
            async:false,
            success : function(data) {
                if(data.returnCode == '0'){
                    $.fool.alert({time:1000,msg:"删除成功！",fn:function(){
                        $('#subjectList').trigger("reloadGrid");
                    }});
                }else if(data.returnCode == '1'){
                    $.fool.alert({msg:data.message,fn:function(){
                    }});
                }
            },
            error:function(){
                $.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
                return false;
            }
        });
    });
    $("#deleteAll").click(function(){
    	$.fool.confirm({title:'确认',msg:'确定要删除所有记录吗？',fn:function(r){
    		if(r){
    			$.ajax({
    				type : 'post',
   					url : '${ctx}/accrueTaxation/deleteAll',
   					data : {},
   					dataType : 'json',
   					success : function(data) {
   						if(data.returnCode == '0'){
   							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){
   								$('#subjectList').trigger("reloadGrid"); 
   							}});
   						}else{
   							$.fool.alert({msg:data.message,fn:function(){
   							}});
   						}
   		    		},
   		    		error:function(){
   		    			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
   		    		}
    			});
    		}
    	}});
    });
    $("#save").click(function(){
    	/* $("#voucherWordId").combotree("enableValidation"); */
    	$('#vmake').form('enableValidation');
    	if(/* $("#voucherWordId").combotree("isValid") */$('#vmake').form('validate')){
    		var data = $('#vmake').serializeJson();
    		var resume = voucherResumeCombo.getComboText();
    		data = $.extend(data,{resume:resume});
    		console.log(data);
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/accrueTaxation/saveAccrued',
    			data : data,
    			dataType : 'json',
    			success : function(data) {
    				if(data.returnCode == '0'){
    					if(data.data==" "||!data.data){
    						$.fool.alert({time:1000,msg:'操作成功！无凭证生成。',fn:function(){
    							$('#subjectList').trigger("reloadGrid"); 
        					}});
    					}else{
    						var src='/voucher/manage?id='+data.data;
    			    		var text='填制凭证';
    			    		parent.kk(src,text);
    					}
    				}else{
    					$.fool.alert({time:1000,msg:data.message});
    				}
        		},
        		error:function(){
        			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
        		}
    		});
    	}
    });
    $("#save2").click(function(){
    	/* $("#voucherWordId").combotree("enableValidation"); */
    	$('#vmake').form('enableValidation');
    	if(/* $("#voucherWordId").combotree("isValid") */$('#vmake').form('validate')){
    		var data = $('#vmake').serializeJson();
    		var resume = voucherResumeCombo.getComboText();
    		data = $.extend(data,{resume:resume});
    		$.ajax({
    			type : 'post',
    			url : '${ctx}/accrueTaxation/savePayTax',
    			data : data,
    			dataType : 'json',
    			success : function(data) {
    				if(data.returnCode == '0'){
    					if(data.data==" "||!data.data){
    						$.fool.alert({time:1000,msg:'操作成功！无凭证生成。',fn:function(){
    							$('#subjectList').trigger("reloadGrid"); 
        					}});
    					}else{
    						var src='/voucher/manage?id='+data.data;
    			    		var text='填制凭证';
    			    		parent.kk(src,text);
    					}
    				}else{
    					$.fool.alert({time:1000,msg:data.message});
    				}
        		},
        		error:function(){
        			$.fool.alert({time:1000,msg:"系统繁忙，稍后再试!"});
        		}
    		});
    	}
    });
    function getComboData(url,mark){
		var result="";
		$.ajax({
			url:url,
			data:{num:Math.random()},
			async:false,
			success:function(data){
				if(data){
					result=data;
				}
			}
		});
		if(mark!="tree"){
			return formatData(result,"fid");
		}else{
			return formatTree(result[0].children,"text","id");
		}
	}
</script>
</body>  
</html>
