<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>科目初始数据</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style>
.tree-hit{
  margin-left:0
}
.hideout{
  display: inline-block;
}
#optionsBox{
    width:90%;
    border: 1px solid #ccc;
    margin:10px auto; 
    text-align: center;
  }
  #optionsBox p{
    display: inline-block;
    margin:20px 30px;
    font-size: 16px
  }
  #btns{text-align: center;}
  #myList a{
  	margin-right:5px;
  }
</style>
</head>
<body>
	<div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">财务初始化</a></li>
	        <li><a href="javascript:;" class="curr">科目初始数据</a></li>
	    </ul>
	</div>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>科目初始数据</h1>
                </div>             
    </div>
	<div id="myList" style="margin: 10px 0px;"> 
	<fool:tagOpt optCode="inibalExport"><a href="${ctx}/initiBalance/export" id="export" class="btn-ora-export" >导出</a></fool:tagOpt>
	<fool:tagOpt optCode="inibalImport"><a href="javascript:;" id="import" class="btn-ora-import" >导入</a></fool:tagOpt>
	<fool:tagOpt optCode="inibalCheck"><a href="javascript:;" id="trial" class="btn-ora-trial" >试算</a></fool:tagOpt>
	<fool:tagOpt optCode="inibalBegin"><a href="javascript:;" id="period" class="btn-ora-import" >期初</a></fool:tagOpt>
	<input id="search-fiscalSubjectCode" /> <input id="search-fiscalSubjectName" /> <input id="search-type"/> <fool:tagOpt optCode="inibalSearch"><a href="javascript:;" id="search-btn" class="Inquiry btn-blue btn-s">筛选</a></fool:tagOpt>
	</div>
	<table id="subjectList"></table>
	<div id="signBox"></div> 
	<div id="periodBox"></div>
	<div id='pop-window'></div>
<script type="text/javascript">
var editingId;
enterSearch("Inquiry");
//导入按钮点击事件
$("#import").click(function(){
	var s={
			target:$("#pop-window"),
			boxTitle:"导入科目期初数据",
			downHref:"${ctx}/ExcelMapController/downTemplate?downFile=科目初始数据.xls",
			action:"${ctx}/initiBalance/import",
			fn:'okCallback()'
	};
	importBox(s);
});

//导入回调
function okCallback(){
	$('#subjectList').treegrid('reload');
	$("#pop-window").window("close");
}

//试算按钮点击事件
$('#trial').click(function(){
	$.post('${ctx}/initiBalance/trailInital',{},function(data){
		dataDispose(data);
		if(data.result==0){
			$.fool.alert({msg:data.msg});
		}else if(data.result==1){
			/* console.log(data); */
			$.fool.alert({msg:"<p style='color:red'>"+data.msg+"</p>"});
		}else{
			$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
		}
	});
});

//期初按钮点击事件
$('#period').click(function(){
	/* $('#periodBox').window('refresh',"${ctx}/initiBalance/period");
	$('#periodBox').window('open'); */
	$.fool.confirm({title:'确认',msg:'导入将会覆盖原始数据，确定要导入期初数据吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/initiBalance/saveByInitalPayReceive',
					data : {},
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'导入成功！',fn:function(){
								$('#subjectList').treegrid('reload');
							}});
						}else{
							$.fool.alert({msg:data.msg,fn:function(){
								$('#subjectList').treegrid('reload');
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
//科目初始数据列表初始化
var $options = {
    height:$(window).height()-$(".titleCustom").height()-$("#myList").height()-70,
    <%--url:'${ctx}/initiBalance/tree',--%>
    idField:'subjectId',
    treeField:'subjectCode',
    fitColumns:true,
    remoteSort:false,
    onClickRow:function(row){
        if(row.subjectFlag==1&&row.isCheckSubject!=1&&row.editing!=true){
            editById(row.subjectId,this,row.fid,row.updateTime);
        };
    },
    onLoadSuccess:function(){//列表权限控制
        <fool:tagOpt optCode="inibalAction1">//</fool:tagOpt>$('.btn-edit').remove();
        <fool:tagOpt optCode="inibalAction2">//</fool:tagOpt>$('.btn-detail').remove();
    },
    columns:[[
        {field:'fid',title:'fid',hidden:true},
        {field:'subjectId',title:'subjectId',hidden:true},
        {field:'updateTime',title:'updateTime',hidden:true},
        {field:'editing',title:'editing',hidden:true},
        {field:'isCheckSubject',title:'是否核算',hidden:true},
        {field:'subjectCode',title:'科目编号',width:100},
        {field:'subjectName',title:'科目名',width:100},
        {field:'amount',title:'期初余额',width:100,editor:{type:"numberbox",options:{validType:['amount','isBank'],precision:2,required:true,missingMessage:"该输入项为必输项"}},formatter:function(value,row){
            var children=row.children;
            var dir=row.direction;
            if(children.length>0){
                value=0;
                for(var i=0;i<children.length;i++){
                    if(children[i].direction==dir){
                        value=parseFloat(value)+parseFloat(children[i].amount);
                    }else{
                        value=parseFloat(value)+(-1*parseFloat(children[i].amount));
                    }
                    row.amount = value;
                };
            }
            value = (parseFloat(value)).toFixed(2) +"";
            var re=/(-?\d+)(\d{3})/ ;
            while(re.test(value)){
                value=value.replace(re,"$1,$2");
            }
            return value;
        }},
        {field:'direction',title:'余额方向',width:100,formatter:function(value){
            for(var i=0; i<direction.length; i++){
                if (direction[i].id == value) return direction[i].name;
            }
            return value;
        }},
        {field:'description',title:'描述',width:100},
        <fool:tagOpt optCode="inibalAction">
        {field:'action',title:'操作',width:100,formatter:function(value,row,index){
            var s='<a class="btn-detail" title="核算项目" href="javascript:;" onclick="signById(\''+row.subjectId+'\')"></a> ';
            if(row.isCheckSubject==1){
                return s;
            }else if(row.editing){
                var save='<a title="保存" href="javascript:;" class="btn-save" onclick="saver(\''+row.subjectId+'\',\''+row.fid+'\',\''+row.updateTime+'\')"></a>';
                var cancel='<a title="取消" href="javascript:;" class="btn-cancel" onclick="canceler(\''+row.subjectId+'\')"></a>';
                return save+"  "+cancel;
            }/* else{
			 if(row.subjectFlag==1){
			 return e;
			 };
			 } */
        }}
        </fool:tagOpt>
    ]],
}
$('#subjectList').treegrid($options);
//筛选按钮点击事件
$("#search-btn").click(function(){
	var subjectCode=$("#search-fiscalSubjectCode").textbox('getValue');
	var subjectType=($("#search-type").next())[0].comboObj.getSelectedValue();
	var subjectName=$("#search-fiscalSubjectName").textbox('getValue');
	if(subjectType==null)
        subjectType="";
    $options.url='${ctx}/initiBalance/tree?subjectCode='+subjectCode+'&subjectName='+subjectName+'&subjectType='+subjectType;
    $('#subjectList').treegrid($options);
});

//核算项目按钮点击事件
function signById(fid){
	$('#signBox').window({modal:true});
	$('#signBox').window('move',{top:$(document).scrollTop() + 80});
	$('#signBox').window('open');
	$('#signBox').window('refresh',"${ctx}/initiBalance/sign?fid="+fid);
}

//搜索输入框初始化
$("#search-fiscalSubjectCode").textbox({
	prompt:'科目编号',
	width:160,
	height:32
});
$("#search-fiscalSubjectName").textbox({
	prompt:'科目名称',
	width:160,
	height:32
});
/* $("#search-type").combobox({
	prompt:'科目类型',
	width:160,
	height:32,
	data:[{value:"1",text:'资产'},{value:"2",text:'负债'},{value:"3",text:'共同'},{value:"4",text:'所有者权益'},{value:"5",text:'成本'},{value:"6",text:'损益'}]
}); */
var searchType=$("#search-type").fool('dhxCombo',{
	prompt:'科目类型',
	width:160,
	height:33,
	editable:false,
	data:[{value:"1",text:'资产'},{value:"2",text:'负债'},{value:"3",text:'共同'},{value:"4",text:'所有者权益'},{value:"5",text:'成本'},{value:"6",text:'损益'}]
});

//新增、修改、详情窗口初始化
$('#signBox').window({
	title:"核算科目初始余额录入",
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	width:$(window).width()-210,
});

/* //期间导入窗口初始化
$('#periodBox').window({
	title:"选择期间",
	href:"${ctx}/initiBalance/period",
	top:150,  
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	closed:true,
	modal:true,
	width:$(window).width()-210,
}); */

//余额方向
var direction = [
             {id:'1',name:"借方"},
             {id:'-1',name:"贷方"}
             ];


//列表操作方法
function editById(subjectId,target,fid,updateTime){
	if (subjectId){
		$('#subjectList').treegrid('update',{
			id: subjectId,
			row: {
				editing:true
			}
		});
		$('#subjectList').treegrid('beginEdit', subjectId);
		var ed=$('#subjectList').treegrid('getEditor',{id:subjectId,field:"amount"});
		var value=$(ed.target).numberbox("getValue");
		if(!value||value==""||value==0){
			$(ed.target).numberbox('setValue',"0");
		};
		$(ed.target).numberbox("textbox").select();
		$(ed.target).numberbox("textbox").keyup(function(e){
			if(e.keyCode=="13"){
				saver(subjectId,fid,updateTime);
			}
			e.stopPropagation();
		});
	};
}
function saver(subjectId,fid,updateTime){
    var ed=$('#subjectList').treegrid('getEditor', {id:subjectId,field:'amount'});
	var amount=$(ed.target).numberbox('getValue');
	var vo="";
	if(fid=='undefined'){
		vo=[{subjectId:subjectId,amount:amount}];
	}else{
		vo=[{subjectId:subjectId,amount:amount,fid:fid,updateTime:updateTime}];
	}
	if (subjectId){
		if($(ed.target).numberbox('isValid')){
			$.post('${ctx}/initiBalance/save',{vos:JSON.stringify(vo)},function(data){
				dataDispose(data);
				if(data.result =='0'){
					$('#subjectList').treegrid('endEdit', subjectId);
					$('#subjectList').treegrid('update',{
						id: subjectId,
						row: {
							fid:data.obj,
							editing:false
						}
					});
                    updateParent(subjectId);
					/*$('#subjectList').treegrid('reload');*/
						/* if(!$('#subjectList').treegrid("getPanel").find(".datagrid-row-editing")){
							$('#subjectList').treegrid('reload');
						} */
				}else if(data.result =='1'){
					$.fool.alert({msg:data.msg});
				}else{
					$.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
				}
				return true;
			});
		}else{
			return false;
		}
	}
}
function canceler(subjectId){
	if (subjectId){
		$('#subjectList').treegrid('cancelEdit', subjectId);
		$('#subjectList').treegrid('update',{
			id: subjectId,
			row: {
				editing:false
			}
		});
	}
}

//更新父节点
function updateParent(subjectId) {
    var parentNode = $('#subjectList').treegrid('getParent', subjectId);
    if (parentNode) {
        $('#subjectList').treegrid('update', {
            id: parentNode.subjectId,
            row: {}
        });
        updateParent(parentNode.subjectId);
    }
}

function enterSearch(searchClass){//页面搜索按钮class
	$('#myList').bind('keydown',function(e){
		if(e.keyCode == 13){
			//页面弹窗后回车搜索失效
			var bool = true;
			for(var i=0; i<$('body').find('.window').length; i++){
				if($('body').find('.window').eq(i).css('display') == 'none'){
					bool = bool && true;
				}
			}
			bool?$("."+searchClass).click():null;
		}
	});
}
$.extend($.fn.validatebox.defaults.rules, {
    amount:{
    	validator:function(value,param){
    		return ((/^(([+]?[0-9]\d*)|\d)(\.\d{1,2})?$/).test(value)||(/^(([-]?[0-9]\d*)|\d)(\.\d{1,2})?$/).test(value))&&parseFloat(value)<=9999999999.99&&parseFloat(value)>=-9999999999.99;
    	},
    	message:'正数最多能输入10位数，小数点后最多2位，请输入正确的金额'
    }
});
</script>
</body>
</html>
