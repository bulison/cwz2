<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>导入</title>
<style>
</style>
</head>
<!-- 
1、保存的路径和下载模板的通过javascript传入并自动填写
2、提交改成异步
3、
后台返回的数据
{
	result：0/1,
	msg:'成功/失败',
	obj:'流水号'
}
返回提示msg的内容，加一个点击【查看详情】的按钮；跳转路径待定

 -->
<body>
   <div style="margin:10px 0 10px 30px">
     <form id="importForm" method="post" enctype="multipart/form-data">
     <p>1.请先<a href="${downHref}">下载模板</a>。</p>
     <p>2.按要求填写好模板。</p>
     <p>3.选取上传文件。<input type="button" class="btn-s btn-blue" value="选择" id="uploadPick" name="file"></p>
     </form>
   </div>
<script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
<script type="text/javascript">
  var uploadPick =$("#uploadPick");
  ajaxUpload(uploadPick); 
  
  function ajaxUpload(btn){
		 new AjaxUpload(btn, {
			 action: '${action}',
			 responseType: 'json',
	         onSubmit:function(file,ext){
	        	 btn.val("正在上传...");
	       	     btn.attr('disabled',true);
	         },
	         onComplete:function(file,response){
	        	 dataDispose(response);
	        	 if(response.returnCode=='0'){
	        		 $.fool.alert({
	        			 msg:response.msg,
	        			 fn:function(){
	        				 btn.val("选择"); 
	        				 btn.removeAttr('disabled');
	        				 eval('${fn}');
	        			 }
	        		 });
	        	 }else if(response.returnCode=='1'){
	        		 if('${param.items}'=="goods"){
	        			 var obj=response.obj;
	        			 var msg="";
	        			 var falseObj=new Array();
	        			 for(var i=0;i<obj.length;i++){
	        				 if(falseObj.length<10&&!obj[i].vaild){
	        					 falseObj.push(obj[i]);
	        				 }
	        			 }
	        			 for(var j=0;j<falseObj.length;j++){
	        				 msg+="第"+falseObj[j].row+"行导入失败："+falseObj[j].msg+"。<br/>";
	        			 }
	        			 $.fool.alert({
		        			 msg:response.msg+"!<br/>"+msg,
		        			 fn:function(){
		        				 var data=response.obj;
		        				 var rows=$("#goodsList").jqGrid('getRowData');
		        				 var indexStart=rows.length;
		        				 var indexEnd=rows.length+data.length;
		        				 var falseNum=0;//计算导入失败的数量
		        				 for(var i=indexStart;i<indexEnd;i++){
		        					 if(data[i-indexStart].vaild){
		        						 data[i-indexStart].vo = $.extend(data[i-indexStart].vo,{//将临时字段的值继承给导入的数据
			        						 _goodsCode:data[i-indexStart].vo.goodsCode,
			        						 _goodsSpecName:data[i-indexStart].vo.goodsSpecName,
			        						 _goodsName:data[i-indexStart].vo.goodsName,
			        						 _goodsSpec:data[i-indexStart].vo.goodsSpec,
			        						 _unitName:data[i-indexStart].vo.unitName,
			        						 _isNew:true,
			        						 type:(data[i-indexStart].vo.unitPrice)*(data[i-indexStart].vo.quentity)
			        					});
		        						 var myi=i-falseNum+1;
			        					 $("#goodsList").jqGrid('addRowData',"999"+myi,data[i-indexStart].vo);
			        					 //alert();
			        					 //var panel = $("#goodsList").datagrid('getPanel');
			        					 //var $edit = panel.find('.datagrid-view2 .datagrid-body tr[datagrid-row-index='+myi+'] td[field=action] .btn-del');
			        					 editNew("999"+myi);
		        					 }else{
		        						 falseNum++;
		        					 }
		        				 }
		        				 getTotalNew();
		        				 /* var row="";
		        				 var target="";
		        				 for(var i=indexStart;i<indexEnd;i++){
		        					 row=$('#goodsList').datagrid("getPanel").find(".datagrid-view").find(".datagrid-view2").find(".datagrid-body").find(".datagrid-row[datagrid-row-index="+i+"]");
		        					 target=row.find('[field="action"]').find(".btn-edit");
		        					 editer(target); 
		        					 
		        				 }*/
		        				 eval('${fn}');
		        				 return false;
		        			 }
		        		 });
	        		 }else{
	        			 $.messager.defaults={ok:"查看详情",cancel:"确定"};
		        		 $.fool.confirm({
		        			 msg:response.msg,
		        			 fn:function(b){
		        				 if(b){
// 		        					 window.location.href="${ctx}/ExcelExceptionController/manage?code="+response.obj;
									 var url = "/ExcelExceptionController/manage?code="+response.obj;
		        					 parent.kk(url,"导入异常报告");
		        					 btn.val("选择"); 
			        				 btn.removeAttr('disabled');
			        				 eval('${fn}');
		        				 }else{
			        				 btn.val("选择");
			        				 btn.removeAttr('disabled');
			        				 eval('${fn}');
		        				 }
		        			 }
		        		 });
		        		 $.messager.defaults={ok:"确定",cancel:"取消"};
	        		 }
	        	 }else{
	        		 $.fool.alert({
	        			 msg:"系统繁忙，请稍后再试。",
	        			 fn:function(){
	        				 btn.val("选择");
	        				 btn.removeAttr('disabled');
	        			 }
	        		 });
	        	 }
	       }
	   });
	}
</script>
</body>
</html>
