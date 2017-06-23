<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
  <div id="dept" style="height:90%;overflow: auto;">
    <div id="deptTree" style="margin-top:0;"></div>
  </div>
  <div style="text-align:right;height:10%;overflow: auto;">
        <a id='yes' href="javascript:;" class="btn-blue4 btn-s" style="margin:0 10px">确定</a>
        <a id='close' href="javascript:;" class="btn-blue4 btn-s" style="margin:0 10px">关闭</a>
  </div>  
<script type="text/javascript">
  $("#deptTree").tree({
	  url:'${ctx}/orgController/getAllTree',
	  checkbox:true,
	  cascadeCheck:false,
	  onLoadSuccess:function(){
		  $("#deptTree").tree("collapseAll");
		  var root=$("#deptTree").tree("getRoot");
		  $("#deptTree").tree("expand",root.target);
		  var hideNode=$("#deptTree").tree("find","${deptId}");
		  $(hideNode.target).find(".tree-checkbox").hide();
		  var hideChilds=$("#deptTree").tree("getChildren",hideNode.target);
		  for(var j=0;j<hideChilds.length;j++){
			  $(hideChilds[j].target).find(".tree-checkbox").hide();
		  }
		  var selectedNode="";
		  $.ajax({
				url:"${ctx}/roleDept/queryDeptIds",
				async:false,
				data:{roleId:"${roleId}"},
				success:function(data){
					for(var i=0;i<data.length;i++){
						selectedNode=$("#deptTree").tree("find",data[i]);
						if(selectedNode&&selectedNode!=""){
							$("#deptTree").tree("check",selectedNode.target);
						}
					}
				}
		  });
	  },
  });
  $("#yes").click(function(){
	  var selected=$("#deptTree").tree("getChecked");
	  var selectedIds="";
	  for(var i=0;i<selected.length;i++){
		  selectedIds+=selected[i].id+",";
	  }
	  $.post("${ctx}/roleDept/save",{deptIds:selectedIds,roleId:"${roleId}"},function(data){
		  if(data.returnCode==0){
			  $.fool.alert({msg:"操作成功",fn:function(){
				  $("#deptTree").tree("reload");
			  }});
		  }else if(data.returnCode==1){
			  $.fool.alert({msg:data.message,fn:function(){
				  $("#deptTree").tree("reload");
			  }});
		  }else{
			  $.fool.alert({msg:"系统繁忙，请稍后再试"});
		  }
	  });
  });
  $("#close").click(function(){
	  $("#permissions").window("close");
  });
</script>
</body>
