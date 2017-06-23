<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
 
   <p id="subDept0-p"><input id="subDept0" type="radio" name="subDept" value="0" checked="checked" onclick="setSubDept(this)"/>显示当前节点信息</p>
   <p id="subDept1-p"><input id="subDept1" type="radio" name="subDept" value="1" onclick="setSubDept(this)"/>显示当前节点及其子节点信息</p>  
   <table id="datagrid"></table>
   <div id="pager"></div>         
<script type="text/javascript">
var options = [
    		    {id:'0',name:'否'},
    		    {id:'1',name:'是'}
    		];
if("${param.subDept}"){
	$("input:radio[value=${param.subDept}]").attr('checked','true');
}
$('#datagrid').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/userController/userList',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#datagrid")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,//自动填满宽度
	height:"100%",
	mtype:"post",
	pager:'#pager',
	postData:{orgId:"${orgId}",subDept:"${param.subDept}"},
	rowNum:10,
	rowList:[ 10, 20, 30 ],
	jsonReader:{
		records:"total",
		total: "totalpages",  
	}, 
	viewrecords:true,
	forceFit:true,//调整列宽度，表格总宽度不会改变
	height:$(window).outerHeight()-200+"px",
	colModel:[
                  {name:'userName',label:'姓名',align:"center",sortable:false,width:10},
                  {name:'userCode',label:'登录帐号',align:"center",sortable:false,width:10},
                  {name:'email',label:'邮箱',align:"center",sortable:false,width:10},
                  {name:'phoneOne',label:'手机',align:"center",sortable:false,width:10},
                  {name:'fax',label:'传真',align:"center",sortable:false,width:10},
                  {name:"validPrice",label:"有效报价",align:'center',sortable:false,width:10,formatter:function(value){
                	  if(value == 1){
                		  return "是";
                	  }else if(value == 0){
                		  return "否";
                	  }else{
                		  return "";
                	  }
                  }},
                  {name:'isMobileLogin',label:'移动登陆',align:"center",sortable:false,width:10,formatter:function(value){
						for(var i=0; i<options.length; i++){
							if (options[i].id == value) return options[i].name;
						}
						return value?value:"";
					}},
					<fool:tagOpt optCode="UserDelUpt">
                  {name:'action',formatter:function(value,options,row){
                	  			var e='';
                	  			var d='';
                	  			<fool:tagOpt optCode="UserDelUpt1">
                				e='<a href="javascript:;" onclick="editrow(\''+ row.fid+ '\')" class="btn-edit"></a>';
                				</fool:tagOpt>
                				<fool:tagOpt optCode="UserDelUpt2">
                				d='<a href="javascript:;" onclick="deleterow(\''+ row.fid+ '\')" class="btn-del"></a>';
                				</fool:tagOpt>
                				return e+d;
                  },label:'操作',align:"center",sortable:false,width:10}
                  </fool:tagOpt>
        ],
	
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

function editrow(userId){
	$('#aaa').window({
		title : '修改',
		top:100,
		left:90,
		width:1000,
		modal : true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		resizable : false,
		href:'${ctx}/userController/addUserUI?userId='+userId,
	});
};

function deleterow(fid){
	$.fool.confirm({
		msg:'确定删除这条记录吗?',
		fn:function(r){
			if (r){
			    $.post("${ctx}/userController/delete",{fid:fid},function(data){
			    	dataDispose(data);
			    	if(data.returnCode=='0'){
		    			  $.fool.alert({msg:'删除成功！',fn:function(){
		        			  $('#datagrid').trigger('reloadGrid');
		    			  }});
		    		  }else if(data.returnCode=='1'){
		    			  $.fool.alert({msg:data.message});
		    		  }else{
		    			  $.fool.alert({msg:"系统正忙，请稍后再试。"});
		    		  }
			    });			    
			}
		}
	});
} 

function setSubDept(target){
	subDept=$(target).val();
	$('#datagrid').jqGrid("setGridParam",{postData:{
		orgId:"${orgId}",
		subDept:subDept,
	}}).trigger("reloadGrid");
};
//分页
//setPager($('#datagrid'));

</script>
</body>
