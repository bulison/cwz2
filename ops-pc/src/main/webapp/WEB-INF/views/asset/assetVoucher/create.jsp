<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>固定资产生成凭证</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript"
	src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<style>
.form1{
	padding:0;
}
.intro{border: 1px solid red;}
.dhxDiv{margin: 0px 5px 10px 0px;}
.toolBox{width:99%;}
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>固定资产生成凭证</h1>
                </div>             
             </div>
	<div class="form1">
		<form class="toolBox" id="search-form" style="margin-left:5px;">
			<div class="toolBox-pane" style="margin: 10px 0 0 10px;">
				<input id="search-startDay" />-<input id="search-endDay" />
				<input id="search-type" class="textBox"/>
				<input id="search-assetCode" class="textBox"/>
				<input id="search-assetName" class="textBox"/>
				<p style="margin-right: 5px">状态：
					<input type="checkbox" name="search-generate" value="0"/>未生成
					<input type="checkbox" name="search-generate" value="1"/>已生成
				</p>
				<fool:tagOpt optCode="asvocuherSearch"><a href="javascript:;" id="search-btn" class="btn-blue btn-s">筛选</a></fool:tagOpt> <a id="clear-btn" class="btn-blue btn-s">清空</a>
			</div>
			
	</form>
		<p><font><em style="color:red;">*</em>凭证日期：</font><input id="voucherDate" name="voucherDate" class=" textbox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true"/></p>
		<p><font><em style="color:red;">*</em>凭证字：</font><input id="voucherWordId" name="voucherWordId" class="textBox"/><!-- <input id="voucherWordId" name="voucherWordId" type="hidden" /> --> </p>
		<p><font>凭证摘要：</font><input id="voucherResume" name="voucherResume" class="textBox"/></p>
		<div style="display:inline-block;margin-left:5px;">
			<fool:tagOpt optCode="asvocuherMake"><a href="javascript:;" id="create" class="btn-yellow2 btn-xs">生成凭证</a></fool:tagOpt>
		</div>
	</div>
	<table id="cardList"></table>
	<div id="pager"></div>
	<div id="addBox"></div>
<script>
var tips='';
var rowNum = 10;
var pgChos = [];
var rowChos = [];
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
/* $('#search-type').combobox({
	editable:false,
	width:158,
	height:30,
	data:[{value:"1",text:'资产购入'},{value:"2",text:'资产折旧'},{value:"3",text:'资产清算'}],
	
	}); */
	//类型
	var typeName= $("#search-type").fool("dhxCombo",{
		  width:160,
		  height:32,
		clearOpt:false,
		  prompt:"类型",
		  editable:false,
		  focusShow:true,
		  data:[		   
			      {
			    	  value: '1',
				      text: '资产购入'
			      },{
			    	  value: '2',
				      text: '资产折旧'
			      },{
			    	  value: '3',
				      text: '资产清算'
			      }
			     ]
    });
	
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

//凭证字
var voucherWordValue="";
var dataValue='';
	$.ajax({
		 url:"${ctx}/basedata/voucherWord?num="+Math.random(),
		async:false,
		data:{},
		success:function(data){
			dataValue=data;
			voucherWordValue=formatTree(data[0].children,"text","id")
		}
	});
  var voucherWordName=$('#voucherWordId').fool("dhxCombo",{
		height:"32px",
		width:"160px",
		editable:false,
		data:voucherWordValue,
		setTemplate:{
			input:"#name#",
			option:"#name#"
		},
      toolsBar:{
          name:"凭证字",
          addUrl:"/basedata/listAuxiliaryAttr",
          refresh:true
      },
      focusShow:true,
		required:true,
		novalidate:true,
		onLoadSuccess:function(combo){
		 for(var i = 0; i<voucherWordValue.length; i++){
			if(voucherWordValue[i].text.name=='记'){
				combo.setComboValue(voucherWordValue[i].text.fid);
				}
			}
		}
	});

$("#search-assetCode").textbox({
	  width:158,
	  height:30,
	  prompt:"资产编号"
});
$("#search-assetName").textbox({
	  width:158,
	  height:30,
	  prompt:"资产名称"
});
$("#clear-btn").click(function(){
	 $("#search-form").form('clear'); 
	 typeName.setComboText("");
	 typeName.setComboValue("");
	 checkbox = "";
 });

//复选框变单选
$(":checkbox").click(function () {
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	checkbox = $(this).val();
});

//筛选按钮
$("#search-btn").click(function(){
	  var startDay=$("#search-startDay").datebox('getValue');
	  var endDay=$("#search-endDay").datebox('getValue');
	  var assetName=$("#search-assetName").textbox('getValue');
	  var assetCode=$("#search-assetCode").textbox('getValue');
	  var type=typeName.getSelectedValue();
	  var showGened = checkbox;
	  var options={startDay:startDay,endDay:endDay,assetName:assetName,assetCode:assetCode,type:type,showGened:showGened};
	  $('#cardList').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
	  //$("#cardList").datagrid('load',options);
	 // $("#cardList").datagrid('clearChecked');
    pgChos= [];
    rowChos= [];
});

$('#cardList').jqGrid({	
	datatype:function(postdata){
	    if(!postdata.showGened){
	        postdata.showGened = "";
		}
		$.ajax({
			url:'${ctx}/assetdetail/queryByPage',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#cardList")[0].addJSONData(data.responseJSON);
		    	}
		    }
		});
	},
	autowidth:true,
	height:$(window).outerHeight()-312+"px",
	pager:"#pager",
	multiselect:true,
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	colModel:[
             {name:'fid',label:'fid',hidden:true,formatter:function(value,options,row){            	
            	 if(value){
            		 return value;
            	 }else{
            		 return "";
            	 }
             }},
             {name:'assetTypeId',label:'assetTypeId',hidden:true},
             {name:'assetCode',label:'固定资产编号',align:'center',width:100},
             {name:'assetName',label:'固定资产名称',align:'center',width:100},
             {name:'deptName',label:'部门',align:'center',width:100},
             {name:'date',label:'日期',align:'center',width:100},
             {name:'type',label:'类型',align:'center',width:100,formatter:function(value){
            		switch(value){
            		case 1: return '资产购入';break;
            		case 2: return '资产折旧';break;
            		case 3: return '资产清算';break;
            		default:return '';break;
            		}
             }},
             {name:'amount',label:'金额',align:'center',width:100},
         	 {name:'debitSubject',label:'清算科目',align:'center',width:100},
			 {name:'depreciationSubject',label:'折旧科目',align:'center',width:100},			 
             {name:'creditSubject',label:'贷方科目',align:'center',width:100},
             {name:'voucherWordNumber',label:'凭证号',align:'center',width:100,formatter:function(value,index,row){
            	 if(value){
            	 	 return '<a href="javascript:;" onclick="clickDo(\''+row.voucherId+'\')">'+value+'</a>';
            	 }else{
            		 return '';
            	 }
           	 }}
  	          ],
  	          gridComplete:function(){
  	        	  var rows = $('#cardList').jqGrid("getRowData");
  	        	  var index = "";
  	        	  for(var i in rows){
  	        		  if(rows[i].fid == ""){
  	        			  index = parseInt(i)+1;
  	        			  //console.log($('#cardList tr#'+index+' input#jqg_cardList_'+index));
  	        			$('#cardList tr#'+index+' input#jqg_cardList_'+index).css("display","none");
  	        		  }
  	        	  }
  	        	if($('#cardList').jqGrid("getGridParam").postData.rows != rowNum){
	  	  			pgChos= [];
	  	  			rowChos= [];
	  	  	   		rowNum = $('#cardList').jqGrid("getGridParam").postData.rows;
	  	  	   	}else{
	  	  	   		var num = $('#cardList').jqGrid('getGridParam').page;
	  	  			if(pgChos[num]){
	  	  				var rowids = pgChos[num];
	  	  				for(var i = 0; i < rowids.length; i++){
	  	  					$('#cardList').jqGrid('setSelection',rowids[i],false);
	  	  				}
	  	  			}
	  	  	   	}
  	          },
  	        onSelectAll:function(aRowids,status){
	  	  		var num = $('#cardList').jqGrid('getGridParam').page;
	  	  		if(status){
	  	  			var rowids = $('#cardList').jqGrid('getDataIDs')
	  	  			pgChos[num] = rowids;
	  	  			var rows = $('#cardList').jqGrid("getRowData");
	  	  			/* for(var i=0;i<rowids.length;i++){
	  	  				var row = $('#goods-table').jqGrid("getRowData",rowids[i]);
	  	  				rows.push(row);
	  	  			} */
	  	  			rowChos[num] = rows;
	  	  		}else{
	  	  			pgChos[num] = [];
	  	  			rowChos[num] = [];
	  	  		}
	  	  	},
	  	  	onSelectRow:function(rowid,status){
	  	  		var rowids = $('#cardList').jqGrid('getGridParam', 'selarrrow');
	  	  		var num = $('#cardList').jqGrid('getGridParam').page;
	  	  		pgChos[num] = rowids;
	  	  		var rows = [];
	  	  		for(var i=0;i<rowids.length;i++){
	  	  			var row = $('#cardList').jqGrid("getRowData",rowids[i]);
	  	  			rows.push(row);
	  	  		}
	  	  		rowChos[num] = rows;
	  	  	},
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
$("#cardList").jqGrid('setGroupHeaders', {
    useColSpanStyle: false, 
    groupHeaders:[
                  {startColumnName: 'debitSubject', numberOfColumns: 2, titleText: '借方科目'},
    ]  
});

//摘要
var voucherResumeValue="";
$.ajax({
    url:"${ctx}/basedata/resume?num="+Math.random(),
    async:false,
    data:{},
    success:function(data){
        voucherResumeValue=formatTree(data[0].children,"text","id");
    }
});
var voucherResumeName=$('#voucherResume').fool("dhxComboGrid",{
    height:"32px",
    width:"160px",
    //editable:false,
    data:voucherResumeValue,
    filterUrl:'${ctx}/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num='+Math.random(),
    setTemplate: {
        input: "#name#",
        columns: [
            {option: '#name#', header: '内容', width: 100},
        ],
    },
    toolsBar:{
        name:"摘要",
        addUrl:"/basedata/listAuxiliaryAttr",
        refresh:true
    },
    focusShow:true,
/*    onChange:function(){
        hasAbstract();
        newData = "";
    }*/
});
//隐藏header
voucherResumeName.hdr = null;


/*function hasAbstract() {
	optionCount = voucherResumeName.getOptionsCount();
    if(optionCount <= 2){
        sInput.next().css('display', '');
    }else{
        sInput.next().css('display', 'none');
    }
}*/

var refresh = $(".mytoolsBar a.btn-refresh")[0];
var sInput =  $(voucherResumeName.getInput());
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

//凭证字号超链接
function clickDo(id){
	var url = "/voucher/manage?fid="+id;
	parent.kk(url,"填制凭证");
}

//凭证生成
$('#create').click(function(){
	var voucherWordId=voucherWordName.getSelectedValue();
	var voucherDate = $('#voucherDate').datebox('getValue');
	/* var voucherResume = voucherResumeName.getSelectedValue(); */
	 var voucherResume=voucherResumeName.getComboText();   	
	var rowids ='';
    //var rowids = $('#cardList').jqGrid('getGridParam','selarrrow');
    var rows = [];
	for(var i  in rowChos){
		rows = rows.concat(rowChos[i]);
	}
	for(var i=0;i<rows.length;i++){
		if(rows[i].fid!=''){
			rowids+=rows[i].fid+",";
		}
	}
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
	$('.form1').form('enableValidation'); 
	if($('.form1').form('validate')){
		if(!rowids){
			$.fool.alert({msg:"请选择固定资产！"});
			return false;
		}
		$.ajax({
			url:'${ctx}/vouchermake/makeVoucher?billType=120',
			type:'POST'	,
			data:{voucherWordId:voucherWordId,voucherDate:voucherDate,voucherResume:voucherResume,billIds:rowids}
		}).done(function(data){			
			dataDispose(data);
			if(data.returnCode==0){
				$.fool.alert({msg:"制作完成，成功制作"+data.dataExt.totalSuccess+"个，失败了"+data.dataExt.totalFail+"个！",fn:function(){
					$('#cardList').trigger("reloadGrid");
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
</script>
</body>
</html>