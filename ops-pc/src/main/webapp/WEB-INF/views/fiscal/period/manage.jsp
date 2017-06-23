<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>财务会计期间</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<style>
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
	        <li><a href="javascript:;" class="curr">财务会计期间</a></li>
	    </ul>
	</div>
	<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>财务会计期间</h1>
       </div>             
    </div>
	<div id="addBox"></div>
	<div id="createBox" class="form1" >   
	  <p><font>会计期间开始日期：</font><input id="showDays" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'输入显示天数',novalidate:true,validType:'ddPrice[1,28]'"/></p>
	  <p><font style="width:60px"><input id="createBtn" type="button" class="btn-blue btn-s" value="设置"/></font></p> 
	</div>
	<div id="myList" style="margin: 10px 0px;">
	  <fool:tagOpt optCode="fperiodAdd"><a href="javascript:;" id="add" class="btn-ora-add">新增</a></fool:tagOpt>
	  <fool:tagOpt optCode="fperiodCreat"><a href="javascript:;" id="create" class="btn-ora-import">生成</a></fool:tagOpt>
	</div>  
	<table id="periodList"></table>
	<div id="pager"></div>
<script type="text/javascript">
var dhxkey = "${param.dhxkey}";
var dhxname = "${param.dhxname}";
var dhxtab = "${param.dhxtab}";
//结账状态
var status = [
                {id:'-1',name:'未启用'},
      		    {id:'0',name:'未结账'},
      		    {id:'1',name:'已结账'}
      		];

//生成窗口初始化
$('#createBox').window({
	title:'设置显示开始日期',
	top:80, 
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	width:430, 
	closed:true
});

//添加 修改 窗口初始化
$('#addBox').window({
	top:80,  
	modal:true,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false,
	width:656,
	closed:true,
	onClose:function(){
	}
});

$("#periodList").jqGrid({
	datatype:function(postdata){
		$.ajax({
			url: '${ctx}/fiscalPeriod/list',
			data:postdata,
		    dataType:"json",
		    complete: function(data,stat){
		    	if(stat=="success") {
		    		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
		    		$("#periodList")[0].addJSONData(data.responseJSON);
		    	}
		    }
		});
	},
	autowidth:true,
	height:$(window).height()-170,
	pager:"#pager",
	rowNum:10,
	rowList:[10,20,30],
	viewrecords:true,
	jsonReader:{
		records:"total",
		total: "totalpages",  
	},
	colModel:[
              {name : 'fid',label : 'fid',hidden:true}, 
              {name:'isLast',label:'isLast',hidden:true},
              {name:'period',label:'会计期间',align:'center',width:"100px"},
  	  		  {name:'startDate',label:'开始日期',align:'center',width:"100px"},
  	  		  {name:'endDate',label:'结束日期',align:'center',width:"100px"},	  		
  			  {name:'fiscalAccountName',label:'账套',align:'center',width:"100px"},
  			  {name:'description',label:'描述',align:'center',width:"100px"},
  			  {name:'checkoutStatus',label:'财务结账状态',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
  				  var str="";
  				  if(cellvalue == "-1"){
  					  str='未启用';
  				  }else if(cellvalue == "0"){
  					  str='未结账';
  				  }else if(cellvalue == "1"){
  					  str='已结账';
  				  }else{
  					  str=cellvalue;
  				  }
  				  return str;
  			  }},
  			  <fool:tagOpt optCode="fperiodAction">
  			  {name:'action',label:'财务操作',align:'center',width:"40px",formatter:function(cellvalue, options, rowObject){
  				  var e = '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+rowObject.fid+'\')"></a>';
  				  var c = '<a class="btn-detail mybtn1" title="财务结账" href="javascript:;" onclick="checkById(\''+rowObject.fid+'\')"></a>';
  	  			  var unC = '<a class="btn-detail mybtn2" title="财务反结账" href="javascript:;" onclick="unCheckById(\''+rowObject.fid+'\')"></a>';
  	  			  var d = '<a class="btn-del" title="删除" href="javascript:;" onclick="deleteById(\''+rowObject.fid+'\')"></a>';
  	  			  var l = '<a class="btn-approve" title="启用" href="javascript:;" onclick="launchById(\''+rowObject.fid+'\',\''+options.rowId+'\')"></a>';
  	  			  var copy = '<a style="display:none;" class="btn-copy" title="复制凭证到下一个会计期间" href="javascript:;" onclick="copyById(\''+rowObject.fid+'\')"></a>';
  	  			  var actions = "";
  	  			  if(rowObject.isLast==0){
  	  				  if(rowObject.checkoutStatus==-1){//-1未启用  0已启用  1已结账
  	  					  actions = l;
  	  				  }else if(rowObject.checkoutStatus==0){
  	  					  actions = c+copy;
  	  				  }else if(rowObject.checkoutStatus==1){
  	  					  actions = unC+copy;
  	  				  }
  	  			  }else if(rowObject.isLast==1){
  	  				  if(rowObject.checkoutStatus==-1){
  	  					  actions=l+""+e+""+d;
  	  				  }else if(rowObject.checkoutStatus==0){
  	  					  actions=c+copy;
  	  				  }else{
  	  					  actions=unC+copy;
  	  				  }
  	  			  }
  	  			  return actions; 
  			  }},
  	  		  </fool:tagOpt>
  			  {name:'stockCheckoutStatus',label:'仓库结账状态',align:'center',width:"100px",formatter:function(cellvalue, options, rowObject){
  				  var str="";
				  if(cellvalue == "-1"){
					  str='未启用';
				  }else if(cellvalue == "0"){
					  str='未结账';
				  }else if(cellvalue == "1"){
					  str='已结账';
				  }else{
					  str=cellvalue;
				  }
				  return str;
  			  }},
  			  <fool:tagOpt optCode="fperiodAction">
  			  {name:'stockaction',label:'仓库操作',align:'center',width:"40px",formatter:function(cellvalue, options, rowObject){
  				  var e = '<a class="btn-edit" title="编辑" href="javascript:;" onclick="editById(\''+rowObject.fid+'\')"></a>'; 
  			      var d = '<a class="btn-del" title="删除" href="javascript:;" onclick="deleteById(\''+rowObject.fid+'\')"></a>';
  			      var l = '<a class="btn-approve" title="启用" href="javascript:;" onclick="launchById(\''+rowObject.fid+'\',\''+cellvalue+'\')"></a>';	  			
  			      var c = '<a class="btn-detail mybtn1" title="仓储结账" href="javascript:;" onclick="stock_checkById(\''+rowObject.fid+'\')"></a>';
  			      var unC = '<a class="btn-detail mybtn2" title="仓储反结账" href="javascript:;" onclick="stock_unCheckById(\''+rowObject.fid+'\')"></a>';
  			      var actions = "";
  			      if(rowObject.isLast==0){
  			    	  if(rowObject.stockCheckoutStatus==-1){//-1未启用  0已启用  1已结账
  			    		  actions = l;
  			    	  }else if(rowObject.stockCheckoutStatus==0){
  			    		  actions = c;
  			    	  }else if(rowObject.stockCheckoutStatus==1){
  			    		  actions = unC;
  			    	  }
  			      }else if(rowObject.isLast==1){
  			    	  if(rowObject.stockCheckoutStatus==-1){
  			    		  actions=l+""+e+""+d;
  			    	  }else if(rowObject.stockCheckoutStatus==0){
  			    		  actions=c;
  			    	  }else{
  			    		  actions=unC;
  			    	  }
  			      }
  			      //console.log(actions);
  			      return actions; 
  			  }}
  		      </fool:tagOpt> 
              ],
    onSelectRow:function(rowid){
    	editRow(rowid);
    }
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

//新增按钮点击事件
$('#add').click(function(){
	$('#addBox').window('setTitle',"新增会计期间");
	$('#addBox').window('move',{top:$(document).scrollTop() + 200});
	$('#addBox').window('open');
	$('#addBox').window('refresh',"${ctx}/fiscalPeriod/add");
});

//编辑按钮点击事件 
function editById(fid){
	$('#addBox').window('setTitle',"修改会计期间");
	$('#addBox').window('move',{top:$(document).scrollTop() + 200});
	$('#addBox').window('open');
	$('#addBox').window('refresh',"${ctx}/fiscalPeriod/edit?fid="+fid);
} 

//生成按钮点击事件，弹出窗口
$('#create').click(function(){
	$('#createBox').window("open");  
});

//生成按钮点击事件，生成数据
$('#createBtn').click(function(){
	$('#showDays').validatebox("enableValidation");
	if($('#showDays').validatebox("isValid")){
		$('#createBtn').attr("disabled","disabled");
		$.post("${ctx}/fiscalPeriod/create",{number:$("#showDays").val()},function(data){
			dataDispose(data);
			if(data.result==0){
				$.fool.alert({
					time:1000,msg:"生成成功！",
					fn:function(){
						$('#createBtn').removeAttr("disabled");
						$('#createBox').window('close');
						$('#periodList').trigger("reloadGrid"); 
					}
				});
			}else if(data.result==1){
				$.fool.alert({
					msg:data.msg,
					fn:function(){
						$('#createBtn').removeAttr("disabled");
						$('#periodList').trigger("reloadGrid"); 
					}
				});
			}else{
				$.fool.alert({
					msg:"系统正忙，请稍后再试。",
					fn:function(){
						$('#createBtn').removeAttr("disabled");
						$('#periodList').trigger("reloadGrid"); 
					}
				});
			}
		});
	}else{
		return false; 
	}
});
//复制凭证到下一个会计期间
function copyById(fid){
	$.fool.confirm({title:'确定',msg:'确定要复制凭证到下一个会计期间吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/fiscalPeriod/copyToNext',
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {
					    console.log(data)
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'复制成功！',fn:function(){
								$('#periodList').trigger("reloadGrid"); 
							}});
						}else if(data.result == "1"){
							$.fool.alert({msg:data.msg});
						}else{
							$.fool.alert({msg:"服务器繁忙,请稍后再试"});
						}
						
					}		
		 });
		 }
	}});
}
//会计结账按钮点击事件
function checkById(fid){
	 $.fool.confirm({title:'确定',msg:'确定要结账吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/fiscalPeriod/checked',
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'结账成功！',fn:function(){
								$('#periodList').trigger("reloadGrid"); 
							}});
						}else{
							if(data.obj&&data.obj.errorCode=="102"){
								$.fool.confirm({title:'确认',msg:data.msg,fn:function(r){
	        						if(r){
	        							$.ajax({
	        								type : 'post',
	        			   					url : '${ctx}/fiscalPeriod/checked?flag=1',
	        			   					data : {fid:fid},
	        			   					dataType : 'json',
	        			   					success : function(data) {
	        			   						if(data.returnCode == '0'){
	        			   							$.fool.alert({time:1000,msg:'结账成功！',fn:function(){
	        			   								$('#periodList').trigger("reloadGrid"); 
	        			   							}});
	        			   						}else{
	        			   							$.fool.alert({msg:data.msg,fn:function(){
	        			   								$('#periodList').trigger("reloadGrid"); 
	        			   							}});
	        			   						}
	        			   		    		},
	        			   		    		error:function(){
	        			   		    			$.fool.alert({msg:"系统繁忙，稍后再试!"});
	        			   		    		}
	        							});
	        						}
	        					}});
							}else{
								$.fool.alert({msg:data.msg});
							}
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		    		}
				});
		 }
	 }});
}

//仓储结账按钮点击事件
function stock_checkById(fid){
	 $.fool.confirm({title:'确定',msg:'确定要结账吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/periodController/changeStatus',
					data : {fid:fid}, 
					dataType : 'json',
					beforeSend:function(){
						$.messager.progress({
							text:'努力计算中，请稍后...'
						});
					},
					success : function(data) {	
						dataDispose(data);
						$.messager.progress('close');
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'结账成功！',fn:function(){
								$('#periodList').trigger("reloadGrid"); 
							}});
						}
						else{
							$.fool.alert({msg:data.message});
						}
		    		},
		    		error:function(){
		    			$.messager.progress('close');
		    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		    		}	
				});
		 }
	 }});
}

//会计反结账按钮点击事件
function unCheckById(fid){
	 $.fool.confirm({title:'确定',msg:'确定要反结账吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/fiscalPeriod/unchecked',
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'反结账成功！',fn:function(){
								$('#periodList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:data.msg});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		    		}
				});
		 }
	 }});
}
//仓储反结账
function stock_unCheckById(fid){
	 $.fool.confirm({title:'确定',msg:'确定要反结账吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/periodController/changeStatus',
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.returnCode == '0'){
							$.fool.alert({time:1000,msg:'反结账成功！',fn:function(){
								$('#periodList').trigger("reloadGrid"); 
							}});
						}
						else{
							$.fool.alert({msg:data.message});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		    		}
				});
		 }
	 }});
}

//启用按钮点击事件
function launchById(fid,index){
	if(index==0){
		$.fool.confirm({title:'确定',msg:'启用了会计期间，不能对期初数据进行修改，是否启用？',fn:function(r){
			if(r){
				$.ajax({
					type : 'post',
					url : '${ctx}/fiscalPeriod/launch',
					data : {fid:fid}, 
					dataType : 'json',
					success : function(data) {
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'启用成功！',fn:function(){
								$('#periodList').trigger("reloadGrid"); 
							}});
						}else{
							$.fool.alert({msg:"<p style='color:red'>"+data.msg+"</p>"});
						}
					},
					error:function(){
						$.fool.alert({msg:"系统正忙，请稍后再试。"});
					}
				});
			}
		}});
	}else{
		$.ajax({
			type : 'post',
			url : '${ctx}/fiscalPeriod/launch',
			data : {fid:fid}, 
			dataType : 'json',
			success : function(data) {
				dataDispose(data);
				if(data.result == '0'){
					$.fool.alert({time:1000,msg:'启用成功！',fn:function(){
						$('#periodList').trigger("reloadGrid"); 
					}});
				}else{
					$.fool.alert({msg:"<p style='color:red'>"+data.msg+"</p>"});
				}
    		},
    		error:function(){
    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
    		}
		});
	}
}

//删除按钮点击事件
function deleteById(fid){ 
	 $.fool.confirm({title:'确认',msg:'确定要删除选中的记录吗？',fn:function(r){
		 if(r){
			 $.ajax({
					type : 'post',
					url : '${ctx}/fiscalPeriod/delete',
					data : {fid : fid},
					dataType : 'json',
					success : function(data) {	
						dataDispose(data);
						if(data.result == '0'){
							$.fool.alert({time:1000,msg:'删除成功！',fn:function(){$('#periodList').trigger("reloadGrid"); }});
						}else{
							$.fool.alert({msg:data.msg});
						}
		    		},
		    		error:function(){
		    			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		    		}
				});
		 }
	 }});
}

//字段校验。
$.extend($.fn.validatebox.defaults.rules, {
	ddPrice:{
        validator:function(value,param){
          if(/^[1-9]\d*$/.test(value)){
            return value >= param[0] && value <= param[1];
          }else{
            return false;
          }
        },
        message:'请输入1到28之间正整数'
      },
      period:{
          validator:function(value,param){
              return /^(\d{4})(-)?(0[1-9]|1[0-2])(_[1-9])?$/.test(value); 
          },
          message:'请输入正确的格式，如“201501”或“2015-01”或“201501_1”或“2015-01_1”'
        }
});
</script>
</body>
</html>
