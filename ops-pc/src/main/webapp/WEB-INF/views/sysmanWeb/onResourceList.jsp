<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<body>
	 <div id="addResource"></div>
          <div class="form" id="show">
            <p><font>资源编号：</font><input type="text" class="easyui-validatebox textBox" value="${code}" /></p>
            <p><font>资源名称：</font><input type="text" class="easyui-validatebox textBox" value="${resName}" /></p>
            <p><font>资源类型：</font><input type="text" id="type" class="easyui-validatebox textBox" value="" /></p>
            <p><font>资源级别：</font><input type="text" id="permType" class="easyui-validatebox textBox" value="" /></p>
            <p><font>链接或JS函数：</font><input type="text" class="easyui-validatebox textBox" value="${resString}" /></p> 
            <p><font>资源图标：</font><input type="text" class="easyui-validatebox textBox"  value="${smallIcoPath}" /></p>
            <p><font>是否显示：</font><input id="showEnable" type ="text" class="easyui-validatebox textBox" value=""></p>
            <p><font>功能描述：</font><input type="text" class="easyui-validatebox textArea" value="${resDesc}" /></p>
          </div>
<script type="text/javascript">
$(document).ready(function(e) {
	if('${resType}'==0){
		$("#type").val("菜单目录");
	}else if('${resType}'==1){
		$("#type").val("菜单项");
	}else if('${resType}'==2){
		$("#type").val("URL");
	}else{
		$("#type").val("操作");
	}
	if('${permType}'==0){
		$("#permType").val("普通资源");
	}else if('${permType}'==1){
		$("#permType").val("系统资源");
	}
    if('${show}'==0){
        $("#showEnable").val("否");
    }else if('${show}'==1){
        $("#showEnable").val("是");
    }

    /*var enableCombo=$("#showEnable").fool("dhxCombo",{
        height:"35px",
        width:"162px",
        editable:false,
        /!*setTemplate:{
         input:"#name#",
         option:"#name#"
         },*!/
        required:true,
        focusShow:true,
        data:[
            {
                value: '1',
                text: '是'
            },{
                value: '0',
                text: '否'
            }
        ],
    });*/
});

</script>
</body>
