<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<title>工资打印条</title>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/resources/js/comm.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/lodop/LodopFuncs.js?v=${js_v}"></script>
<script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>
<style>
#myList a{
	margin-right:5px;
}
</style>
</head>
<body>
<div class="titleCustom">
        <div class="squareIcon">
             <span class='Icon'></span>
             <div class="trian"></div>
             <h1>工资打印条</h1>
       </div>             
    </div>
    <div class="nav-box">
		<ul>
	    	<li class="index"><a href="${ctx}/indexController/indexPage">首页</a></li>
	        <li><a href="javascript:;">工资</a></li>
	        <li><a href="javascript:;" class="curr">工资打印条</a></li>
	    </ul>
	</div>
	<div id="myList" style="margin: 10px 0px;">
	    <fool:tagOpt optCode="wagepPrint"><a href="javascript:;" id="print" class="btn-ora-printer" >打印</a></fool:tagOpt>
		<form id="search-form" style="display: inline-block;">
		    <input id="memberId" type="hidden"/> <input id="month" /> <input id="dept" />
		     <input id="member" /> <fool:tagOpt optCode="wagepSearch"><a href="javascript:;" id="search-btn" class="btn-blue btn-s">筛选</a></fool:tagOpt>
		     <a href="javascript:;" id="clear-btn" class="btn-blue btn-s">清空</a>
		</form>
	</div>
	
	<!-- <div id="list"></div> -->
	<table id="list"></table> 
    <div id="pager"></div>
	<script type="text/javascript">
	var texts = [];
	var thead = [
	             {key:'date',title:'月份'},
	             {key:'memberDept',title:'部门'},
                 {key:'memberCode',title:'人员编号'},
                 {key:'memberName',title:'人员名称'},
                 <c:forEach items="${titles}" var="title">
                 {key:'${title.fid}',title:'${title.columnName}'},
				  </c:forEach>
	             ];
	var tfoot = [];
	    var chooserWindow='';
	    //控件初始化
		$("#month").datebox({
			prompt:'月份',
			width:100,
			height:30,
			editable:false,
			required:true,
			novalidate:true,
			onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
				span.trigger('click'); //触发click事件弹出月份层
				if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
					tds = p.find('div.calendar-menu-month-inner td');
			        tds.click(function (e) {
			        	e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
			            var year = /\d{4}/.exec(span.html())[0];//得到年份
			            var month = parseInt($(this).attr('abbr'), 10) + 1; //月份
			            $('#month').datebox('hidePanel');//隐藏日期对象
			            $('#month').datebox('setValue', year + '-' + month); //设置日期的值
			        });
				}, 0);
				$("#month").datebox('calendar').find('.calendar-header').hide();
				$("#month").datebox('calendar').parent().siblings('.datebox-button').hide();
				$('input.calendar-menu-year').attr("disabled","disabled");//避免填写年份后会回到选择日期的面板的问题
			},
			parser: function (s) {//配置parser，返回选择的日期
				if (!s) return new Date();
		        var arr = s.split('-');
		        return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);	                
			},
			formatter: function (d) {
				if(d.getMonth()==0){
					return d.getFullYear()-1 + '-12'; 
				}else{
					return d.getFullYear() + '-' + d.getMonth();
				}
			}//配置formatter，只返回年月
		});
		var p = $('#month').datebox('panel'), //日期选择对象
	    tds = false, //日期选择对象中月份
	    span = p.find('span.calendar-text'); //显示月份层的触发控件	    
		  
		//部门初始化	
			var deptValue='';	
				$.ajax({
					url:"${ctx}/orgController/getAllTree?num="+Math.random(),
					async:false,		
					success:function(data){		  	
						deptValue=formatData(data[0].children,'id','texr');	
				    }
					});
				var deptName= $("#dept").fool("dhxCombo",{
					  prompt:'部门',
					  width:160,
					  height:32,
					clearOpt:false,
					  required:true,
					  novalidate:true,
					  data:deptValue,
                    toolsBar:{
                    	name:"部门",
                        refresh:true
                    },
                    editable:false,
					  focusShow:true,
				});	
	    
		$("#member").textbox({
			prompt:'人员',
			width:100,
			height:30,
			editable:false
		});
		/* $('#list').datagrid({
			url:'${ctx}/wage/wageReport',
			width:'100%',
			fitColumns:true,
			remoteSort:false,
			singleSelect:true,
			scrollbarSize:0,
			columns:[[
                      {field:'fid',title:'fid',hidden:true},
                      {field:'date',title:'月份',width:100},
                      {field:'memberCode',title:'人员编号',width:100,sortable:true},
                      {field:'memberName',title:'人员名称',width:100},
                      {field:'memberDept',title:'部门',width:100},
                      <c:forEach items="${titles}" var="title">
                        {field:'${title.fid}',title:'${title.columnName}',width:100},
      				  </c:forEach>
			          ]]
		});  */
		
		$('#list').jqGrid({
			datatype:function(postdata){
				$.ajax({
					url:'${ctx}/wage/wageReport',
					data:postdata,
			        dataType:"json",
			        complete: function(data,stat){
			        	if(stat=="success") {
// 			        		console.log(data.responseJSON);
			        		var json=JSON.parse(data.responseJSON);
// 			        		console.log(json);
			        		data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
			        		$("#list")[0].addJSONData(json);
			        	}
			        }
				});
			},
			autowidth:true,//自动填满宽度
			height:'100%',
			viewrecords:true,
			rowNum:'',
			forceFit:true,//调整列宽度，表格总宽度不会改变
			jsonReader:{
				records:"total",
				total: "totalpages",  
			},
			colModel:[
                      {name:'fid',label:'fid',align:'center',hidden:true},
                      {name:'date',label:'月份',align:'center',width:100},
                      {name:'memberCode',label:'人员编号',align:'center',width:100,sortable:true},
                      {name:'memberName',label:'人员名称',align:'center',width:100},
                      {name:'memberDept',label:'部门',align:'center',width:100},
                      <c:forEach items="${titles}" var="title">
                        {name:'${title.fid}',label:'${title.columnName}',align:'center',width:100},
      				  </c:forEach>
			          ]
		}).navGrid('#pager',{add:false,del:false,edit:false,search:false,view:false}); 		
		//人员选择框弹出页面
		$("#member").textbox('textbox').click(function(){
			chooserWindow=$.fool.window({'title':"选择人员",href:'${ctx}/member/window?okCallBack=selectMember&onDblClick=selectMemberDBC&singleSelect=true'});
		});
		function selectMember(rowData){
			$("#memberId").val(rowData[0].fid);
			$("#member").textbox('setValue',rowData[0].username);
			chooserWindow.window('close');
		}
		function selectMemberDBC(rowData){
			$("#memberId").val(rowData.fid);
			$("#member").textbox('setValue',rowData.username);
			chooserWindow.window('close');
		}
		
		//搜索按钮点击事件
		$("#search-btn").click(function (){
			$('#search-form').form('enableValidation');
			if($('#search-form').form('validate')){
				var month=$("#month").datebox('getValue');
				var deptId=deptName.getSelectedValue();//获取控件值
				var memberId=$("#memberId").val();
				var options = {"wageDate":month,"deptId":deptId,"memberId":memberId};
				$('#list').jqGrid('setGridParam',{postData:options}).trigger("reloadGrid");
			}else{
				return false;
			};
		});
		//清空按钮点击事件
		$("#clear-btn").click(function (){
			//$("#search-form").form('clear');
			cleanBoxInput($("#search-form"));
			deptName.setComboText("");
			deptName.setComboValue("");
		});
		
		//打印按钮
		$("#print").click(function(){
			var wageDate=$("#month").datebox("getValue");
			//var deptId=$("#dept").combotree("getValue");
			var deptId=deptName.getSelectedValue();//获取控件值
			var memberId=$("#member").textbox("getValue");
			printWage(wageDate,deptId,memberId);
		});
	</script>
</body>
</html>
