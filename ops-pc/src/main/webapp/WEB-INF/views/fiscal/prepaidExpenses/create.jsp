<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>待摊费用生成凭证</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
.form1{
	padding:0;
}
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>待摊费用生成凭证</h1>
                </div>             
             </div>
	<div class="form1">
			
		<form class="toolBox" id="search-form" style="margin-left:5px;">
			<div class="toolBox-pane" style="margin: 10px 0 0 10px;">
				<input id="search-startDay" />-<input id="search-endDay" />
				<input id="search-expensesCode" class="textBox"/>
				<input id="search-expensesName" class="textBox"/>
				<p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='0' name="search-generate">未生成
					<input type=checkbox value='1' name="search-generate">已生成</p>
				<a href="javascript:;" id="search-btn" class="btn-blue btn-s">筛选</a> <a id="clear-btn" class="btn-blue btn-s">清空</a>
			</div>
		</form>
		<p><font><em style="color:red;">*</em>凭证日期：</font><input id="voucherDate" name="voucherDate" class=" textbox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true"/></p>
		<p><font><em style="color:red;">*</em>凭证字：</font><input id="voucherWordName" name="voucherWordName" class="textBox"/><input id="voucherWordId" name="voucherWordId" type="hidden" /> </p>
		<p><font>凭证摘要：</font><input id="voucherResume" name="voucherResume" class="textBox"/></p>
		<div style="display:inline-block;margin-left:5px;">
				<a href="javascript:;" id="create" class="btn-yellow2 btn-xs">生成凭证</a>
			</div>
	</div>
	<table id="expensesList"></table>
	<div id="pager"></div>
	<div id="addBox"></div>
<script>
var $data = [];
var rowNum = 10;
/*var newData = "";*/
var checkbox = "";
/*var optionCount = "";*/
//控件初始化
/* $('#voucherResume').combotree({
		url:'${ctx}/basedata/resume',
		width:158,
		height:30,
		editable:true,
		onBeforeSelect:function(node){
			if(node.text=='摘要'){
				return false;
			}
		},
		onShowPanel:function(){
			$('#voucherResume').combotree('panel').find('.tree').children().children('.tree-node').find('.tree-title').text('请选择');
			if(tips != 1){
				$('#voucherResume').combotree('hidePanel');
			}else{
				tips = '';
				$('#voucherResume').parent().find('.textbox').children('.textbox-text').keydown(function(){
					$('#voucherResume').combotree('hidePanel');
				});
			}
		}
	});
$('#voucherResume').parent().find('.textbox').children('.textbox-addon').children('.textbox-icon').mousedown(function(){
	tips = 1;
}); */

var voucherResumeValue="";
$.ajax({
    url:"${ctx}/basedata/resume?num="+Math.random(),
    async:false,
    data:{},
    success:function(data){
        voucherResumeValue=formatTree(data[0].children,"text","id");
    }
});

var voucherResumeCombo=$("#voucherResume").fool('dhxComboGrid',{
	width:160,
	height:32,
	focusShow:true,
	editable:true,
	data:voucherResumeValue,
    filterUrl:'${ctx}/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num='+Math.random(),
	setTemplate:{
		input:"#name#",
        columns:[
            {option:'#name#',header:'内容',width:100},
        ],
	},
	toolsBar:{
		name:"摘要",
		addUrl:"/basedata/listAuxiliaryAttr",
		refresh:true,
	},
/*	onChange:function () {
        hasAbstract();
        newData = "";
    }*/
});
voucherResumeCombo.hdr = null;

/*function hasAbstract() {
	optionCount = voucherResumeCombo.getOptionsCount();
    if(optionCount <= 2){
        sInput.next().css('display', '');
    }else{
        sInput.next().css('display', 'none');
    }
}*/

var refresh = $(".mytoolsBar a.btn-refresh")[0];
var sInput =  $(voucherResumeCombo.getInput());
//修改input宽度
sInput.css("width",sInput.width()-20);
sInput.after('<a href="javascript:;" onclick="resumeSave()" class="btn-save"></a>');
sInput.next().css({'float':'right','margin':'5px 23px','display':''});
function resumeSave(){
    var name = ($('#voucherResume').next())[0].comboObj.getComboText();
    if(name == ""){
        $.fool.alert({msg:'摘要不能为空！',time:1000});
        return;
    }
    $.ajax({
        url:'${ctx}/basedata/saveFast',
        type:'POST',
        data:{name:name,categoryCode:'014'},
        success:function (data) {
            dataDispose(data);
            if(data){
                if(data.returnCode == 0){
                    $.fool.alert({msg:'摘要保存成功！',time:1000,fn:function () {
                        newData = data.data;
                        voucherResumeValue.push({value:"",text:name});
                        //重新加载一下Combo
                        ($('#voucherResume').next())[0].comboObj.load({options:voucherResumeValue});
                        refresh.click();
                    }});
                    /*sInput.next().css('display','none');*/
                }else if(data.returnCode == 1){
                    $.fool.alert({msg:data.msg});
                }else{
                    $.fool.alert({msg:'系统繁忙，请稍后再试'});
                }
            }
        }

    })
}


$("#voucherDate").datebox({
		width:158,
		height:30,
		editable:false,
	});
$.post('${ctx}/voucher/getDefaultMessage',function(data){
	$('#voucherDate').datebox('setValue',data.voucherDate);
});
$("#search-startDay").datebox({
	  editable:false,
	  width:158,
	  height:30,
	  prompt:"开始日期"
});
$("#search-endDay").datebox({
	  editable:false,
	  width:158,
	  height:30,
	  prompt:"结束日期"
});

//已生成||未生成
$("#search-generate")

//凭证字选择
/* $('#voucherWordName').fool('voucherWordTree',{
	onBeforeSelect:function(node){
		if(node.text != '请选择'){
			$('#voucherWordId').val(node.id);
		}
	},
	onLoadSuccess:function(node,data){
		for(var i = 0; i<data[0].children.length; i++){
			if($.trim(data[0].children[i].text) == '记'){
				$('#voucherWordName').combotree('setValue',data[0].children[i].id);
				$('#voucherWordId').val(data[0].children[i].id);
			}
		}
	}
}); */
var voucherWordCombo=$("#voucherWordName").fool('dhxCombo',{
	width:160,
	height:32,
	focusShow:true,
	editable:false,
	data:getComboData(getRootPath()+'/basedata/voucherWord',"tree"),
	setTemplate:{
		input:"#name#",
		option:"#name#"
	},
	toolsBar:{
		name:"凭证字",
		addUrl:"/basedata/listAuxiliaryAttr",
		refresh:true,
	},
	onLoadSuccess:function(){
		var childs=getComboData(getRootPath()+'/basedata/voucherWord',"tree");
		for(var i=0;i<childs.length;i++){
			if(childs[i].text.name=="记"){
				($("#voucherWordName").next())[0].comboObj.setComboValue(childs[i].value);
				($("#voucherWordName").next())[0].comboObj.setComboText(childs[i].text.name);
				break;
			}
		}
	},
});
/*$("#voucherWordName").click(function(){
		  chooserWindow=$.fool.window({
			    width:780,
			    height:480,
			  	'title':"选择凭证字",
			 	href:'${ctx}/voucher/voucherWordWindow?okCallBack=selectVoucherWordName&onDblClick=selectVoucherWordDBC&singleSelect=true',
			 	onLoad:function(){
			 		chooserWindow.window('panel').find('#customer-table').treegrid({
			 			onLoadSuccess:function(row,data){
			 					chooserWindow.window('panel').find('tr[node-id="13154bfaffa04db3b9380226a5e94ec8"]').find('.tree-title').text('请选择');
			 			}
			 		});
			 	}
		  });
		  
	});

	function selectVoucherWordName(rowData) {
		if(rowData[0].text != '凭证字'){
			$("#voucherWordName").val(rowData[0].text);
			$("#voucherWordId").val(rowData[0].id);
			chooserWindow.window('close');
		}else{
			$.fool.alert({msg:'请选择凭证字子节点！'});
			return false;
		}
	}
	function selectVoucherWordDBC(rowData) {
		if(rowData.text != '凭证字'){
			$("#voucherWordName").val(rowData.text);
			$("#voucherWordId").val(rowData.id);
			chooserWindow.window('close');
		}else{
			$.fool.alert({msg:'请选择凭证字子节点！'});
			return false;
		}
	}*/
$("#search-expensesCode").textbox({
	  width:158,
	  height:30,
	  prompt:"待摊费用编号"
});
$("#search-expensesName").textbox({
	  width:158,
	  height:30,
	  prompt:"待摊费用名称"
});
$("#clear-btn").click(function(){
	 $("#search-form").form('clear');
    checkbox = "";
 });

//复选框只让一个选中
$(":checkbox").click(function () {
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	checkbox = $(this).val();
});

//筛选按钮
$("#search-btn").click(function(){
	  var startDate=$("#search-startDay").datebox('getValue');
	  var endDate=$("#search-endDay").datebox('getValue');
	  var expensesName=$("#search-expensesName").textbox('getValue');
	  var expensesCode=$("#search-expensesCode").textbox('getValue');//search-generate
	  var showGened = checkbox;
	  var options={startDate:startDate,endDate:endDate,expensesName:expensesName,expensesCode:expensesCode,showGened:showGened};
	  $("#expensesList").setGridParam({postData:options}).trigger("reloadGrid"); 
	  $data=[];
});

$("#expensesList").jqGrid({
	datatype:function(postdata){
        if(!postdata.showGened){
            postdata.showGened = "";
        }
		$.ajax({
			url: '${ctx}/prepaidExpensesDetail/query',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#expensesList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,
	height:"100%",
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},  
	multiselect:true,
	colModel:[ 
              /* {name : '',label :"",align:'center',width:"20px",formatter:function(cellvalue, options, rowObject){
            	  if(rowObject.expensesName != "合计"){
            			return "<input class='checker' fid='"+rowObject.fid+"' type='checkbox' />"; 
            	  }else{
            		  return "";
            	  }
              }}, */
              {name : 'fid',label : 'fid',hidden:true}, 
              {name : 'expensesCode',label : '待摊费用编号',align:'center',width:"100px"},
              {name : 'expensesName',label : '待摊费用名称',align:'center',width:"100px"}, 
              {name : 'date',label : '日期',align:'center',width:"100px"},
              {name : 'amount',label : '金额',align:'center',width:"100px"}, 
              {name : 'voucherWordNumber',label : '凭证号',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
            	  if(cellvalue){
            		  return '<a href="javascript:;" onclick="clickDo(\''+rowObject.voucherId+'\')">'+cellvalue+'</a>';
            	  }else{
            		  return '';
            	  }
              }}, 
              ],
    gridComplete:function(){
        var pageRowNum = $('#expensesList').jqGrid("getGridParam").postData.rows;
        if(pageRowNum!=rowNum){
            rowNum=pageRowNum;
            $data=[];
        }
        var currPage = $('#expensesList').jqGrid('getGridParam').page;
        if($data.length>0){
            for(var i=0;i<$data.length;i++){
                if($data[i].page == currPage){
                    for(var m=0;m<$data[i].rowids.length;m++){
                        $('#jqg_expensesList_'+$data[i].rowids[m]).attr('checked',true);
                    }
                }
            }
        }
    },
    onSelectRow:function(rowid,status){
        var rowdata = $("#expensesList").jqGrid("getRowData",rowid);
        var currPage = $('#expensesList').jqGrid('getGridParam').page;
        var dataIsExist = false;
        var pageIsExist = false;
        if($data.length>0){
            for(var i=0;i<$data.length;i++){
                if($data[i].page==currPage){
                    pageIsExist=true;
                    if($data[i].pgdata.length>1){
                        for(var j = 0;j<$data[i].pgdata.length;j++){
                            if($data[i].pgdata[j].fid==rowdata.fid){
                                dataIsExist=true;
                                break;
                            }
                            continue;
                        }
                        if(status&&!dataIsExist){
                            $data[i].rowids[$data[i].rowids.length]=rowid;
                            $data[i].pgdata[$data[i].pgdata.length]=rowdata;
                            //$data[i].pgdata.push(rowdata)
                        }
                        else if(!status&&dataIsExist){
                            $data[i].pgdata.splice(j,1);
                            for(var m=0;m<$data[i].rowids.length;m++){
                                if($data[i].rowids[m]==rowid)
                                    $data[i].rowids.splice(m,1)
                            }
                        }
                    }else{
                        if(status){
                            $data[i].rowids.push(rowid);
                            $data[i].pgdata.push(rowdata);
                        }else{
                            $data.splice(i,1);
                        }
                    }
                    break;
                }
                continue;
            }
            if(!pageIsExist&&status)
                $data.push({page:currPage,rowids:[rowid],pgdata:[rowdata]});
        }else if($data.length==0&&status){
            $data.push({page:currPage,rowids:[rowid],pgdata:[rowdata]});
        }
    },
    onSelectAll:function(aRowids,status){
        var currPage = $('#expensesList').jqGrid('getGridParam').page;
        var pageIsExist = false;
        var datas = [];
        if(status){
            for(var rowid in aRowids){
                datas.push(jQuery('#expensesList').jqGrid('getRowData',rowid))
            }

        }
        for(var i=0;i<$data.length;i++){
            if($data[i].page == currPage){
                pageIsExist = true;
                if(status){
                    $data[i].rowids=aRowids;
                    $data[i].pgdata=datas;
                }else{
                    $data.splice(i,1)
                }
                break;
            }
            continue;
        }
        if(!pageIsExist)
            $data.push({page:currPage,rowids:aRowids,pgdata:datas})
    },
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 

//凭证字号超链接
function clickDo(id){
	var url = "/voucher/manage?fid="+id;
	parent.kk(url,"填制凭证");
}

//凭证生成
$('#create').click(function(){
	var voucherWordId = ($("#voucherWordName").next())[0].comboObj.getSelectedValue();
    var ids = [];
	var voucherDate = $('#voucherDate').datebox('getValue');
	var voucherResume = voucherResumeCombo.getComboText('getText');
/*    var hasValue = ($('#voucherResume').next())[0].comboObj.getSelectedValue();
    if(hasValue == null){
        hasValue = newData;
    }
    var hasName = ($('#voucherResume').next())[0].comboObj.getComboText();
    if(hasValue == null && hasName != "" && optionCount <= 2 || hasValue == "" && hasName != "" && optionCount <= 2){
        $.fool.alert({time:1000,msg:'请先保存凭证摘要！'});
        return ;
    }else if(hasValue == null && hasName != "" && optionCount > 2 || hasValue == "" && hasName != "" && optionCount > 2){
        $.fool.alert({time:1000,msg:'您没有选择凭证摘要！'});
        return ;
	};*/

    for(var i=0;i<$data.length;i++){
        for(var j=0;j<$data[i].pgdata.length;j++){
			if($data[i].pgdata[j].fid)
				ids.push(($data[i].pgdata[j].fid));
        }
    }
    var billIds=ids.join(',');
	$('.form1').form('enableValidation');
	if($('.form1').form('validate')){
		if(!billIds){
			$.fool.alert({msg:"请选择待摊费用！"});
			return false;
		}
		$.ajax({
			url:'${ctx}/vouchermake/makeVoucher?billType=130',
			type:'POST'	,
			data:{voucherWordId:voucherWordId,voucherDate:voucherDate,voucherResume:voucherResume,billIds:billIds}
		}).done(function(data){
			if(data.returnCode==0){
				$.fool.alert({msg:"制作完成，成功制作"+data.dataExt.totalSuccess+"个，失败了"+data.dataExt.totalFail+"个！",fn:function(){
					$('#expensesList').trigger("reloadGrid"); 
				},time:3000});
			}else if(data.returnCode==1){
				$.fool.alert({msg:data.message});
			}else{
				$.fool.alert({msg:'系统繁忙，请稍后再试。'});
			}
		});
	}
});
enterSearch('search-btn');

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