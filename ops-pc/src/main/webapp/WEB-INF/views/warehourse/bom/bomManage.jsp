<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<title>bom表</title>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>

<style>
#search-form span{
  margin-right: 5px;
  margin-bottom: 10px
}
#search-form a{
  margin-right: 5px;
  margin-bottom: 10px
}
#Inquiry{
  margin-left: 5px;
}
#grabble{
  float: right;
}
#bolting{
  width: 160px;
  height: 27px;
  position:relative; 
  border: 1px solid #ccc; 
  background: #fff;
  margin-right: 25px;
}
.button_a{
  display:block; 
  width:25px; 
  height:25px;
  background:#76D5FC; 
  -moz-border-radius: 3px;
  /* Gecko browsers */-webkit-border-radius: 3px;
  /* Webkit browsers */border-radius:3px;
  /* W3C syntax */float: right; 
  right:29px; 
  top:63px; 
  position: absolute;
}
.button_span{
  width:0; 
  height:0; 
  border-left:8px solid transparent; 
  border-right:8px solid transparent;
  border-top:8px solid #fff;
  top:10px; 
  position: absolute;
  right:5px;
}
.input_div{
  display: none;
  background:#F5F5F5; 
  padding: 10px 0px 5px 0px; 
  border: 1px solid #D5DBEA;
  position: absolute;
  right: 23px; 
  top:93px;
  z-index: 1;
}
.input_div p{ 
  font-size: 12px; 
  color:#747474;
  vertical-align: middle;
  text-align: right;  
  margin: 0 20px 0 10px;
  width:240px;
}
.button_clear{ 
  border-top: 1px solid #D5DBEA;
  margin-top: 10px; 
  padding-top:8px;
  text-align: right;
}
.roed{
   -moz-transform:scaleY(-1);
   -webkit-transform:scaleY(-1);
   -o-transform:scaleY(-1);
   transform:scaleY(-1);
   filter:FlipV();
}
#bill p.hideOut{
  display: none;
} 
</style>
</head>
<body>
	<div class="titleCustom">
                <div class="squareIcon">
                   <span class='Icon'></span>
                   <div class="trian"></div>
                   <h1>Bom表</h1>
                </div>             
             </div>
	<div id="addBox"></div> 
	<div style="margin: 10px 0px 10px 0px;">
	  <a href="javascript:;" id="addNew" class="btn-ora-add" style="vertical-align: top;">新增</a>
	  <div id="grabble"><input type="text" name="inMemberId" id="bolting" value="请选择筛选条件" readonly="readonly" /><a href="javascript:;" class="button_a"><span class="button_span"></span></a></div>
	  <div class="input_div">
	    <form id="search-form">
	      <!-- <p>货品编码：<input id="select-goodsCode" name="goodsCode" /></p> -->
	      <p>货品：<input id="select-goods" _class="goods-combogrid" name="goodsName"><input id="select-goodsid" type="hidden" name="goodsId"/></p>
	      <!-- <p>制单人：<input id="select-people" name="creatorName" class="textBox"/><input type="hidden" name="creatorId" id="select-peopleid" /></p> -->
	      <p style="margin: 5px 20px 5px 0px;">状态：<input type=checkbox value='' name="enable">全部
	      <input type=checkbox value='1' name="enable">启用<input type=checkbox value='0' name="enable">停用</p>
	      <div class="button_clear">
	        <a href="javascript:;" id="search-btn" class="btn-blue btn-s" style="vertical-align:middle;">查询</a>
	        <a href="javascript:;" id="clear-btn" class="btn-blue btn-s" style="vertical-align:middle;">清空</a>
	        <a href="javascript:;" id="clear-btndiv" class="btn-blue btn-s" style="vertical-align:middle;">关闭</a>
	      </div>
	    </form>
	  </div>
	</div>
	<table id="billList"></table>
	<div id="pager"></div>
<script type="text/javascript" src="${ctx}/resources/js/warehousebill.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/warehouseEdit.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript">
initManage("bom","bom表");
var chechbox='';
var decide=true;
var yesNo=[{value:"0",text:"否"},{value:"1",text:"是"}];
$(":checkbox").click(function(){//点击复选框并且只让一个选中
	$(this).attr("checked","checked").siblings().removeAttr("checked");
	 chechbox=$(this).val();
});

$("#search-btn").click(function(){
	refreshData();
});

$("#search-form").find("input[_class]").each(function(i,n){($(this));});


$("#clear-btn").click(function(){
	cleanBoxInput($("#search-form"));
});

$('#billList').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/bom/list',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#billList")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	forceFit:true,
	pager:'#pager',
	rowList:[ 10, 20, 30 ],
	viewrecords:true,
	rowNum:10,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	autowidth:true,//自动填满宽度
	height:$(window).outerHeight()-200+"px",
	colModel:[
	  		{name:'fid',label:'fid',align:"center",hidden:true,width:100},
	  		{name:'goodsCode',label:'货品编号',align:"center",sortable:true,width:100},
	  		{name:'goodsName',label:'货品',align:"center",sortable:true,width:100,formatter:function(value,options,row){
	  			return '<a href="javascript:;" onclick="editById(\''+row.fid+'\',\''+row.enable+'\')">'+value+'</a>';
	  		}},
        	{name:'accountUnitName',label:'记账单位',align:"center",sortable:true,width:100},
        	{name:'accountQuentity',label:'记账数量',align:"center",sortable:true,width:100},
	        {name:'specName',label:'属性',align:"center",sortable:true,width:100},
	  		{name:'enable',label:'是否有效',align:"center",sortable:true,width:100,formatter:yesNoAction},
	  		{name:'fdefault',label:'是否为默认配方',align:"center",sortable:true,width:100,formatter:yesNoAction},
	  		{name:'describe',label:'描述',align:"center",sortable:true,width:100},
	  		{name:'action',label:'操作',align:"center",sortable:true,width:100,formatter:function(value,options,row){
	  			var d='',l='',s='',de='',c='';
	  			d='<a class="btn-del" title="删除" href="javascript:;" onclick="removeById(\''+row.fid+'\')"></a> ';
	  			c='<a class="btn-copy" title="复制" href="javascript:;" onclick="copyById(\''+row.fid+'\')"></a> ';
	  			l='<a class="btn-approve" title="启用" href="javascript:;" onclick="launchById(\''+row.fid+'\')"></a> ';
	  			s='<a class="btn-cancel" title="停用" href="javascript:;" onclick="stopById(\''+row.fid+'\')"></a> ';
	  			de='<a class="btn-initialize" title="设为默认" href="javascript:;" onclick="defaultById(\''+row.fid+'\')"></a> ';
	  			var str="";
	  			if(row.enable==0){
	  				str=l+""+d;
	  			}else{
	  				str=s;
	  				if(row.fdefault==0){
		  				str=str+" "+de;
		  			};
	  			}
	  			str=str+""+c;
	  			return str;
	  		}}
	      ],
	      gridComplete:function(){
	    	  warehouseAll();
	      }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

/* $('#addBox').window({
	top:10,
	left:0,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false, 
	width:$(window).width()-20,
	height:$(window).height()-20,
	closed:true,
	modal:true,
	onOpen:function(){
		$(this).parent().prev().css("display","none");
	}
}); */


function editById(id,enable){
	var url="${ctx}/bom/edit?id="+id+"&billCode=bom&flag=edit";
	if(enable==1){
		url="${ctx}/bom/edit?id="+id+"&billCode=bom&flag=detail";
	}
	warehouseWin("编辑BOM",url)
}

function copyById(id){
	var url="${ctx}/bom/edit?id="+id+"&mark=1&billCode=bom&flag=copy";
	warehouseWin("复制BOM",url)
}

//删除
function removeById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/bom/delete',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({msg:'已删除！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//启用
function launchById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要启用该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/bom/launch',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({msg:'已启用！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//停用
function stopById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要停用该记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/bom/stop',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({msg:'已停用！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//设为默认
function defaultById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要设为默认配方吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/bom/default',
					data : {id : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({msg:'操作成功！',fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}else{
							$.fool.alert({msg:data.message,fn:function(){
								$('#billList').trigger("reloadGrid");
							}});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
		    		}
				});
		 }
	 }});
}

//状态翻译
function yesNoAction(value){
	for(var i=0; i<yesNo.length; i++){
		if (yesNo[i].value == value) return yesNo[i].text;
	}
	return value;
}
</script>
</body>
</html>