<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<html>
<head>
</head>
<body>
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<style>
.mybtn-footer{ padding-right: 20px; width:98%;}
.mybtn-footer p{ width:73px;}
.mybtn-footer p input{ background: #50B3E7; color:#fff;border-radius:4px; font-size:16px; line-height: 30px;}
.myform .textBox{height: 30px;}
#supe{ display:none;} 
.supervises{ display: none;}
</style>
              <form id="form" class="bill-form myform">
              <div id="title" >
		         <div id="title1" class="shadow" style="margin:10px 0px 0 0;padding:11px 0; height:18px;">
			         <div id="square1"><span class="backBtn" ></span><div id="triangle"></div></div><h1 style="margin-top:2px;">${billflagName}消息预警</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		         </div>
	          </div>
	          
	          <div id="bill" class="shadow" style="margin-top:50px;height: 500px;">
		         <div class="billTitle"><div id="square2"></div><h2 style="margin-top: -2px;">填写单据信息</h2></div>
		         <div class="in-box" id="list2">
                <input id="fid" type="hidden" name="fid" value="${msgWarnSetting.fid}"/>
                <p>
                   <font><em>*</em>任务级别：</font><input id="taskLevel" name="taskLevel" type="text" class="easyui-validatebox textBox" value="${msgWarnSetting.taskLevel}" data-options="novalidate:true,required:true,missingMessage:'该项不能为空！',validType:'accessory'"/>
                   <font>提前天数：</font><input id="days" class="easyui-validatebox textBox" type="text" name="days" value="${msgWarnSetting.days}" data-options="validType:'accessory'"/>
				   <font>重发天数：</font><input id="retryDays" name="retryDays" type="text" class="easyui-validatebox textBox" value="${msgWarnSetting.retryDays}" data-options="validType:'accessory'"/>
				</p>
				<p>
				  <font>值域：</font><input id="range" name="range" type="text" class="textBox easyui-numberbox" value="${msgWarnSetting.range}" data-options="min:0,precision:2,width:182,height:31," />
				  <font><em>*</em>消息接收类型：</font><input id="sendType" name="sendType" type="text" value="${msgWarnSetting.sendType}"/>  				                                 				                                
				  <font><em>*</em>触发类型：</font><input id="triggerType" name="triggerType" type="text" value="${msgWarnSetting.triggerType}" data-options="novalidate:true,required:true"/>
				</p>
				<p>
				 <font>事件类型：</font><input id="taskType" name="taskType" />
				   <font>上级发送：</font><input id="toSuperior" name="toSuperior"/>  				                                  
				    <font>下级发送：</font><input id="toSubordinate" name="toSubordinate" value="${msgWarnSetting.toSubordinate}"/></p>
				 <p>
				     <font><em>*</em>发送场景 ：</font><input id="busScene" name="busScene" type="text" value="${msgWarnSetting.busScene}"/>
				     <font>是否系统配置：</font><input id="isSystem" name="isSystem"/>
					 <span id="supe">					 
					    <font>监督人：</font><input type="text" id="superviseId" name="superviseId" value="${msgWarnSetting.superviseIds}"/>
					 </span>
					 <input type="text" id="superviseIds" name="superviseIds" type="hidden" value="${msgWarnSetting.superviseIds}"/> 
				</p>
				</div>
              </form>
              <c:forEach items="${supervises}" var="item">
              <div class='supervises'><span class='name'>${item.superviseName}</span><span class='id'>${item.superviseId}</span></div>
              </c:forEach>
   <!--   <div id="checkBox"></div>      -->     
  <div class="mybtn-footer" _id="${msgWarnSetting.fid}"></div>

<script type="text/javascript">
$('#superviseIds').hide();
$(".mybtn-footer").append('<p><input type="button" onclick="saver()" id="save" class="btn-blue2 btn-xs" value="保存" /></p>');
//返回按钮指向链接
$('#square1 .backBtn').click(function(){
	$("#addBox").window("close");
	$('html').css('overflow',''); 
});
var triggerTypeValue='';	
$.ajax({
	url:"${ctx}/flow/msgSetting/getTriggerMap?num="+Math.random(),
	async:false,
	type:"post",
	success:function(data){
		var data=JSON.parse(data); 
		triggerTypeValue=formatData(data,"id");	
    }
	});
var triggerTypeName= $("#triggerType").fool("dhxCombo",{
	 required:true,
	 novalidate:true,
	  width:182,
	  height:32,
	  data:triggerTypeValue,		
	  editable:false,
	  focusShow:true,
	clearOpt:false,
    onLoadSuccess:function(combo){
		  if('${msgWarnSetting.triggerType}'==''){
			  combo.setComboValue("10");
				}
			combo.setComboValue("${msgWarnSetting.triggerType}");
		}
});	 

var busSceneValue='';
$.ajax({
	url:"${ctx}/flow/msgSetting/getSceneMap?num="+Math.random(),
	async:false,		
	success:function(data){	
		var data=JSON.parse(data); 
		busSceneValue=formatData(data,"id");	
    	}
	});
var busSceneName= $("#busScene").fool("dhxCombo",{
	 required:true,
	 novalidate:true,
	  width:182,
	  height:32,
	  data:busSceneValue,		
	  editable:false,
	  focusShow:true,
	clearOpt:false,
    onLoadSuccess:function(combo){
		  if('${msgWarnSetting.busScene}'==''){
			  combo.setComboValue("0");
			 } 	
			combo.setComboValue("${msgWarnSetting.busScene}");
		}
});	

var isSystemName=$("#isSystem").fool("dhxCombo",{
	required:true,
	novalidate:true,
	width:182,
	height:32,
	data:[
	  {
		value:'0',
		text:'用户自定义配置',
	  },
	  {
		value:'1',
		text:'系统默认生产配置'
	  }
	],
	editable:false,
	focusShow:true,
	clearOpt:false,
    onLoadSuccess:function(combo){
	    if('${msgWarnSetting.isSystem}'=='1')
	        combo.setComboValue('1')
		else
			combo.setComboValue("0");
	}
});

var taskTypeName= $("#taskType").fool("dhxCombo",{
	width:182,
	height:32,
	editable:false,
	focusShow:true,
	clearOpt:false,
	  data:[
		{
		value:'0',
		text:'触发事件',
		},
		  {
		   value: '1',
		   text: '前关联事件'
		  },{
			value: '2',
			text: '后关联事件'
		  }
	  ],
    onLoadSuccess:function(combo){
		combo.setComboValue("${msgWarnSetting.taskType}");
	}
});	 

var toSuperiorName= $("#toSuperior").fool("dhxCombo",{
	width:182,
	height:32,
	editable:false,
	focusShow:true,
    clearOpt:false,
	  data:[
		{
		value:'0',
		text:'否',
		},
		  {
		   value: '1',
		   text: '是'
		  }
	  ],
	onLoadSuccess:function(combo){
	    if('${msgWarnSetting.toSuperior}'=='1')
			combo.setComboValue("1");
	    else
	        combo.setComboValue("0");
	}
});	 

var toSubordinateName= $("#toSubordinate").fool("dhxCombo",{
	width:182,
	height:32,
	editable:false,
	focusShow:true,
	clearOpt:false,
	data:[
		{
		value:'0',
		text:'否',
		},
		  {
		   value: '1',
		   text: '是'
		  }
		 ],
	onLoadSuccess:function(combo){
	    if('${msgWarnSetting.toSubordinate}'=='1')
			combo.setComboValue("1");
	    else
	        combo.setComboValue("0");
	}
});	 

var sendTypeValue= $("#sendType").fool("dhxCombo",{
	width:182,
	required:true,
	novalidate:true,
	height:32,
	editable:false,
	focusShow:true,
	clearOpt:false,
	data:[
		{
		value:'0',
		text:'发起人',
		},
		  {
			  value: '1',
			  text: '负责人'
		  },{
			  value: '2',
			  text: '承办人'
		  },{
			  value: '7',
			  text: '部门监督人'
		  },{
			  value: '8',
			  text: '公司监督人'
		  }
		  ,{
			  value: '9',
			  text: '审核人'
		  }
		 ],
	onChange:function(value,text){
		 if(value=='7'||value=='8'){
			   $('#supe').css('display','inline-block');
		   }else{
			   $('#supe').css('display','none');
		   }
		},
	onLoadSuccess:function(combo){
		combo.setComboValue("${msgWarnSetting.sendType}");
	}
});	

var combodata;
$.post('${ctx}/userController/userList',{},function(data){
	if(data){
		combodata = data.rows;
		$('#superviseId').combobox({       
		    width:182,
			height:31,
			data:combodata,
			valueField: 'fid',    
			textField: 'userName',
			novalidate:true,
			multiple:true,
			editable:false,
		  onSelect:function(index,row){
				var name=$("input[name='superviseId']");
				var fids='';
	             for(var i=0;i<name.length;i++){
	            	 fids+=name.eq(i).val()+',';
	             };
				$("#superviseIds").val(fids);				
			}
		});
	}else{
		return false;
	}
});

function savname(){
	var Id='';
	var Name='';
	var names=$('.name');
	var ids=$('.id');
	for(var j=0;j<names.length;j++){
		 Name+=names.eq(j).text()+',';
		 Id+=ids.eq(j).text()+',';
	}
	$("#superviseIds").val(Id);
	$("#superviseId").val(Id);
};
 savname(); 

 //设置为系统默认生产配置不能修改也不能删除
if('${msgWarnSetting.isSystem}'=='1'){
	 $('#taskLevel').attr('readonly','readonly').css('background','#EBEBE4');
	 sendTypeValue.disable();
	 toSubordinateName.disable();
	 toSuperiorName.disable();
	 taskTypeName.disable();
	 triggerTypeName.disable();
	 busSceneName.disable();
	 $('#form .textbox-text').css('background','#EBEBE4');
 }
 isSystemName.disable();

if('${msgWarnSetting.sendType}'){
	 if('${msgWarnSetting.sendType}'==7||'${msgWarnSetting.sendType}'==8){
		 $('#supe').css('display','inline-block');
	}
}
 
</script>
</body>
</html>