<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
          <div>
            <table id='datagrid'>
            </table>
            <div id="pager"></div>
          </div>
<script type="text/javascript">
var options = [
      		    {id:'0',name:'无效'},
      		    {id:'1',name:'有效'}
      		  ];
$('#datagrid').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/roleController/roleList',
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
			  {name:'fid',label:'fid',hidden:true},
              {name:'roleName',label:'角色名称',align:"center",sortable:false,width:50},
              {name:'validation',label:'是否有效',align:"center",sortable:false,formatter:function(value){
					for(var i=0; i<options.length; i++){
						if (options[i].id == value) return options[i].name;
					}
					return value;
				},width:50},
              {name:'roleDesc',label:'描述',align:"center",sortable:false,width:200},
              <fool:tagOpt optCode="RoleAuthorityUser">
              {name:'user',label:'关联用户',align:"center",sortable:false,formatter:function(val,options,row){
					return val='<a href="javascript:;" onclick="toUser('+options.rowId+')">用户</a>';
				},width:50},</fool:tagOpt>
				
				<fool:tagOpt optCode="RoleAuthorityUser">
				<c:choose>
                <c:when test="${user.name == 'system'}">
	              {name:'users',label:'所有用户',align:"center",sortable:false,formatter:function(val,options,row){
						return val='<a href="javascript:;" onclick="toOterUser('+options.rowId+')">全部用户</a>';
					}},
				</c:when>
			    </c:choose>
			    </fool:tagOpt>
              /* {name:'dept',label:'关联部门',align:"center",sortable:false,width:80,formatter:function(val,options,row){
					return val='<a href="javascript:;" onclick="toDept('+options.rowId+')">部门</a>';
              }}, */
			  <fool:tagOpt optCode="RoleAuthorityUpt">
              {name:'action',label:'角色权限',align:"center",sortable:false,width:80,formatter:function(val,options,row){
					return val='<a href="javascript:;" onclick="jump('+options.rowId+')">角色权限</a>';
				},width:50}</fool:tagOpt>
         ]
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});
//分页
//setPager($('#datagrid'));

function jump(index){
	var row = $('#datagrid').jqGrid("getRowData", index);
	var fid = row.fid;
	if(fid){
		$("#permissions").window({
			href:'${ctx}/resourceController/permissions?fid='+fid,
			width:$(window).width()-90,
			height:600,
			title:'角色权限设置',
			minimizable:false,
			maximizable:false
		});
	}
	
};
function toUser(index){
	var row = $('#datagrid').jqGrid("getRowData", index);
	var fid = row.fid;
	if(fid){
		$("#permissions").window({
			href:'${ctx}/resourceController/permissionsToUser?fid='+fid,
			width:$(window).width()-90,
			height:600,
			title:'用户',
			minimizable:false,
			maximizable:false
		});
	}
};
function toOterUser(index){
	var row = $('#datagrid').jqGrid("getRowData", index);
	var fid = row.fid;
	if(fid){
		$("#permissions").window({
			href:'${ctx}/resourceController/onPermissionsToUser?roleId='+fid,
			width:$(window).width()-90,
			height:600,
			title:'全部用户',
			minimizable:false,
			maximizable:false
		});
	}
};
function toDept(index){
	var row = $('#datagrid').jqGrid("getRowData", index);
	var fid = row.fid;
	if(fid){
		$("#permissions").window({
			href:'${ctx}/roleDept/onPermissionsToDept?fid='+fid,
			width:300,
			height:400,
			title:'部门',
			minimizable:false,
			maximizable:false
		});
	}
};
</script>
</body>
