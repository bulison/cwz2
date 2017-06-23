<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>

           
            <table id="datagrid"></table>
            <div id="pager"></div>
<div id="addRole" data-options="left:'500px',top:'200px'"></div>         
                           
          
<script type="text/javascript">
/* $('#datagrid').datagrid({
        onBeforeEdit:function(index,row){
			row.editing = true;
			updateActions(index);
		},
		onAfterEdit:function(index,row){
			row.editing = false;
			updateActions(index);
			$.post("${ctx}/roleController/saveOrUpdate",{fid:row.fid,roleDesc:row.roleDesc,roleName:row.roleName,validation:row.validation});
		},
		onCancelEdit:function(index,row){
			row.editing = false;
			updateActions(index);
		}
}); */

function editrow(fid){
		$('#addRole').window({		
			title : '修改角色',
			width:450,
			modal : true,
			top:$(window).height()/2-$(window).height()/5,
			left:$(window).width()/2-$(window).width()/5,
			minimizable : false,
			maximizable : false,
			href:'${ctx}/roleController/addRoleUI?fid='+fid,
		});
		
	}
	
function deleterow(fid){
		$.fool.confirm({
			msg:'确定删除？',
			fn:function(r){
				if (r){				
					$.post("${ctx}/roleController/delete",{fid:fid},function(){
						$('#datagrid').trigger('reloadGrid');
					});
				}
			}
		});
}
$('#add').click(function(){
	$('#addRole').window({
		href:'${ctx}/roleController/addRoleUI',
		width:425,
		top:$(window).height()/2-$(window).height()/5,
		left:$(window).width()/2-$(window).width()/5,
		modal : true,
		title:'新增角色',
		minimizable:false,
		maximizable:false
	});
});

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
                  {name:'roleName',label:'角色名称',align:"center",sortable:false,editor:"text",width:100},
                  {name:'validation',label:'是否有效',align:"center",sortable:false,formatter:function(value){
						for(var i=0; i<options.length; i++){
							if (options[i].id == value) return options[i].name;
						}
						return value;
					},width:100},
					<fool:tagOpt optCode="UserDelUpt">
                  {name:'roleDesc',label:'描述',align:"center",sortable:false,editor:"text",width:300},
                  {name:'action',label:'操作',align:"center",sortable:false,width:100,formatter:function(value,options,row){               	  
              				var e='<a href="javascript:;" onclick="editrow(\''+ row.fid+ '\')" class="btn-edit"></a>';
              				var d='<a href="javascript:;" onclick="deleterow(\''+ row.fid+ '\')" class="btn-del"></a>';
              				return e+d;
            				
                }}
                  </fool:tagOpt>
        ],
	    gridComplete:function(){//列表权限控制
			<fool:tagOpt optCode="UserDelUpt1">//</fool:tagOpt>$('.btn-edit').remove();
			<fool:tagOpt optCode="UserDelUpt2">//</fool:tagOpt>$('.btn-del').remove();
		}
	
}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false});

//分页
//setPager($('#datagrid'));
</script>
</body>
