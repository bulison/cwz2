/**
 * manage
 */
$('#search-code').textbox({
	prompt:'编号或名称',
	width:160,
	height:30,
});
$('#addBox').window({
	top:10,
	left:0,
	collapsible:false,
	minimizable:false,
	maximizable:false,
	resizable:false, 
	width:$(window).width()-10,
	height:$(window).height()-60,
	closed:true,
	modal:true,
});
function windowOpen(url){
	$('#addBox').window({
		onClose:function(){
			if("undefined" != typeof plankd){
				$(document).unbind('keydown',plankd);
			}
		}
	});
	$('#addBox').window("open");
	$('#addBox').window("setTitle","新增计划模板");
	$('#addBox').window("refresh",url);
}

$('#addPlan').click(function(){
	var url=getRootPath()+'/flow/planTemplate/edit';
	windowOpen(url);
});


//查询
function searchIt(){
	var searchKey = $('#search-code').textbox('getValue');
	$('#planList').datagrid('load',{"searchKey":searchKey});
}

//主表数据
$('#planList').datagrid({
	url:getRootPath()+"/flow/planTemplate/query",
	fitColumns:true,
	singleSelect:true,
	pagination:true,
	columns:[[
	          {field:"fid",title:"fid",hidden:true},
	          {field:"taskLevelId",title:'taskLevelId',hidden:true},
	          
	          {field:"name",title:'计划名称',width:10},
	          {field:"code",title:'计划编号',width:10},
	          {field:"taskLevelName",title:'任务级别名称',width:10},
	          {field:"days",title:'预计完成天数',width:10},
	          {field:"describe",title:'描述',width:10},
	          {field:"creatorName",title:'创建人',width:10},
	          {field:"status",title:'状态',width:10,formatter:function(value,row,index){
	        	  if(value == 0){
	        		  return "停用";
	        	  }else if(value == 1){
	        		  return "启用";
	        	  }
	          }},
	          {field:"action",title:'操作',width:10,formatter:function(value,row,index){
	        	  var e = '<a href="javascript:;" class="btn-edit" onclick="editById(\''+row.fid+'\')" title="修改"></a>';
	        	  var v = '<a href="javascript:;" class="btn-detail" onclick="editById(\''+row.fid+'\')" title="查看"></a>';
	        	  var d = '<a href="javascript:;" class="btn-del" onclick="delById(\''+row.fid+'\')" title="删除"></a>';
	        	  var r = '<a href="javascript:;" class="btn-in" onclick="runById(\''+row.fid+'\','+index+')" title="启用"></a>';
	        	  var u = '<a href="javascript:;" class="btn-cancel" onclick="unrunById(\''+row.fid+'\','+index+')" title="停用"></a>';
	        	  if(row.status == 0){
	        		  return e+r+d;
	        	  }else if(row.status == 1){
	        		  return v+u;
	        	  }
	          }},
	        ]],
});

//列表操作
function editById(id){
	var url=getRootPath()+'/flow/planTemplate/edit?id='+id+'&mark=1';
	windowOpen(url);
}
function delById(id){
	$.fool.confirm({title:"删除提示",msg:"确认删除该项计划模板？",fn:function(bool){
		if(bool){
			$.post(getRootPath()+'/flow/planTemplate/delete?id='+id,{},function(data){
				if(data.returnCode == 0){
					$.fool.alert({time:1000,msg:"删除成功！",fn:function(){
						$('#planList').datagrid('reload');
					}});
				}else if(data.returnCode == 1){
					$.fool.alert({msg:data.message,fn:function(){
					}});
				}else{
					$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
					}});
				}
			});
		}else{
			return false;
		}
	}});
}
function unrunById(id,index){
	$.post(getRootPath()+'/flow/planTemplate/updateUnUse?id='+id,{},function(data){
		if(data.returnCode == 0){
			$.fool.alert({time:1000,msg:"停用成功！",fn:function(){
				$('#planList').datagrid('updateRow',{
					index:index,
					row:{
						status:0
					}
				});
			}});
		}else if(data.returnCode == 1){
			$.fool.alert({msg:data.message,fn:function(){
			}});
		}else{
			$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
			}});
		}
	});
}
function runById(id,index){
	$.post(getRootPath()+'/flow/planTemplate/updateUse?id='+id,{},function(data){
		if(data.returnCode == 0){
			$.fool.alert({time:1000,msg:"启用成功！",fn:function(){
				$('#planList').datagrid('updateRow',{
					index:index,
					row:{
						status:1
					}
				});
			}});
		}else if(data.returnCode == 1){
			$.fool.alert({msg:data.message,fn:function(){
			}});
		}else{
			$.fool.alert({msg:"服务器繁忙，请稍后再试！",fn:function(){
			}});
		}
	});
}
enterSearch('mysearch');