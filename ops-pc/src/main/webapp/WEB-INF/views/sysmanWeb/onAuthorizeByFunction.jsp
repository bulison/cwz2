<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
  <table id="user"></table>
  <div id="fpager"></div>  
<script type="text/javascript">
var options = [
   		    {id:'0',name:'否'},
   		    {id:'1',name:'是'}
   		];
$('#user').jqGrid({
	datatype:function(postdata){
		$.ajax({
			url:'${ctx}/userController/userList',
			data:postdata,
	        dataType:"json",
	        complete: function(data,stat){
	        	if(stat=="success") {
	        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
	        		$("#user")[0].addJSONData(data.responseJSON);
	        	}
	        }
		});
	},
	autowidth:true,//自动填满宽度
	height:"100%",
	mtype:"post",
	pager:'#fpager',
	postData:{orgId:"${orgId}"},
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
			  {name:'fid',label:'fid',hidden:true,width:100},
	          {name:'userName',label:'姓名',align:"center",sortable:false,editor:"text",width:100},
              {name:'email',label:'邮箱',align:"center",sortable:false,editor:"text",width:100},
              {name:'phoneOne',label:'电话',align:"center",sortable:false,editor:"text",width:100},
              {name:'fax',label:'传真',align:"center",sortable:false,editor:"text",width:100},
              {name:'isMobileLogin',label:'移动登陆',align:"center",sortable:false,formatter:function(value){
					for(var i=0; i<options.length; i++){
                        if (options[i].id == value){
                            return options[i].name;
                        }
					}
					if(value == undefined){
					    return "";
                    }
					return value;
				},
				width:100},
				<fool:tagOpt optCode="ByFunction">
	          {name:'rights',label:'功能',width:100,align:"center",sortable:false,formatter:function(val,options,row){
					return val='<a href="javascript:;" onclick="jump('+options.rowId+')">功能</a>';
				},}
	          </fool:tagOpt>
	]
}).navGrid('#fpager',{add:false,del:false,edit:false,search:false,view:false});


//分页
//setPager($('#user'));  


function jump(index){
	var fid=$("#user").jqGrid('getRowData',index).fid;
	if(fid){
		$("#authorize").window({
			href:'${ctx}/resourceController/function?fid='+fid,
			width:900,
			height:600,
			title:'功能',
			minimizable:false,
			maximizable:false
		});
	}
	
};
</script>
</body>
