<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head></head>
<body>
    <div class="win-filterBox">
        <div> 
	        <div id="win-reportButton"></div>
	        <span><input id="win-flag" type="checkbox" checked="checked"/>分页</span>
        </div>
	    <div>
	        <div id="win-filterGrid" class="form1"></div>
	    </div>
	</div>
<script type="text/javascript">
$.ajax({
	url:"${ctx}/reportFilter/getWhereByReportId",
	async:false,
	data:{reportId:"${param.reportId}"},
	success:function(data){
		var rows=data.rows;
		if(rows.length!=0){
			var row="";
			for(var i in rows){
				row=rows[i];
				//区分快捷筛选和详细筛选
				if(row.show==1){ 
					$("#filterGrid").append("<p class='showP'><font><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" _required="+row.need+" data-options='required:"+row.need+",missingMessage:\""+missingMsg+"\"' mark="+row.mark+" ></p>");
				}else{
					if($("#openBtn").length<1){
						$("#filterGrid").append('<br><h3 style="display: inline-block;margin-left:10px;font-size:17px">&emsp;详细筛选：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none"><br/>');
					}
					$("#filterGrid").append("<p class='hideP'><font><nobr>"+row.displayName+"：</nobr></font><input id="+row.aliasName+" class='type"+row.inputType+"' order="+row.order+" tableName="+row.tableName+" fieldName="+row.fieldName+" inputType="+row.inputType+" data-options='required:"+row.need+",missingMessage:\""+missingMsg+"\"' mark="+row.mark+" ></p>");
				}
			}
			//展开、收起其他信息
			$("#openBtn").click(function(e) {
						  $("#filterGrid .hideP").css("display","inline-block");
						  $('#openBtn').css("display","none");
						  $('#closeBtn').css("display","inline-block");
			});
			$("#closeBtn").click(function(e) {
						  $("#filterGrid .hideP").css("display","none");
						  $('#openBtn').css("display","inline-block");
						  $('#closeBtn').css("display","none");	
			});
			initial();
			$("#resultGrid").html("");
		}else{
			$("#filterGrid").css('display','none');
			$("#resultGrid").html("");
			return false;
		}
		showPage=data.other.showPage;
		if(showPage==0){
			$("#flag").removeAttr("checked");
			$("#flag").parent().hide();
		}else{
			$("#flag").attr("checked","checked");
			$("#flag").parent().show();
		}
	}
});
</script>
</body>