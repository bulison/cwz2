<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <%@ taglib prefix="fool" uri="/WEB-INF/tld/fool.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/js.jsp" %>
</head>
<body>
<div id="container">
<input type="button" id="post"/>
</div>


<script type="text/javascript">
  var data={
    "desc": "注册表单",
    "fromAction": "register.do",
    "fromClass": "registerClass",
    "fromId": "registerFrom",
    "fromName": "registerFrom",
    "propertys": [
        {
            "control": {
                "desc": "文本框",
                "type": "text"
            },
            "controlClass": "",
            "controlId": "",
            "initType": null,
            "initValue": null,
            "name": "username",
            "ownerId": "ower1",
            "text": "",
            "title": "用户名",
            "value": "张三"
        },
        {
            "control": {
                "desc": "密码框",
                "type": "password"
            },
            "controlClass": "",
            "controlId": "",
            "initType": null,
            "initValue": null,
            "name": "password",
            "ownerId": "ower1",
            "text": "",
            "title": "密码",
            "value": "123456789"
        },
        {
            "control": {
                "desc": "单选框",
                "type": "radio"
            },
            "controlClass": "",
            "controlId": "",
            "initType": "list",
            "initValue": [
                {
                    "control": {
                        "desc": "单选框",
                        "type": "radio"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "sex",
                    "ownerId": "",
                    "text": "未知",
                    "title": "",
                    "value": "0"
                },
                {
                    "control": {
                        "desc": "单选框",
                        "type": "radio"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "sex",
                    "ownerId": "",
                    "text": "男",
                    "title": "",
                    "value": "1"
                },
                {
                    "control": {
                        "desc": "单选框",
                        "type": "radio"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "sex",
                    "ownerId": "",
                    "text": "女",
                    "title": "",
                    "value": "2"
                }
            ],
            "name": "sex",
            "ownerId": "ower1",
            "text": "",
            "title": "性别",
            "value": "0"
        },
        {
            "control": {
                "desc": "日历控件",
                "type": "calendar"
            },
            "controlClass": "",
            "controlId": "",
            "initType": null,
            "initValue": null,
            "name": "riqi",
            "ownerId": "ower1",
            "text": "",
            "title": "生日",
            "value": "2015-05-05"
        },
        {
            "control": {
                "desc": "省市区联动控件",
                "type": "addressControl"
            },
            "controlClass": "",
            "controlId": "",
            "initType": "list",
            "initValue": [
                {
                    "control": {
                        "desc": "省市区联动控件",
                        "type": "addressControl"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "sheng",
                    "ownerId": "",
                    "text": "广东省",
                    "title": "",
                    "value": "guangdongsheng"
                },
                {
                    "control": {
                        "desc": "省市区联动控件",
                        "type": "addressControl"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "shi",
                    "ownerId": "",
                    "text": "佛山市",
                    "title": "",
                    "value": "foshanshi"
                },
                {
                    "control": {
                        "desc": "省市区联动控件",
                        "type": "addressControl"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "qu",
                    "ownerId": "",
                    "text": "南海区",
                    "title": "",
                    "value": "nanhaiqu"
                },
                {
                    "control": {
                        "desc": "文本框",
                        "type": "text"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "address",
                    "ownerId": "",
                    "text": "",
                    "title": "",
                    "value": ""
                }
            ],
            "name": "",
            "ownerId": "ower1",
            "text": "",
            "title": "地址",
            "value": ""
        },
        {
            "control": {
                "desc": "复选框",
                "type": "checkbox"
            },
            "controlClass": "",
            "controlId": "",
            "initType": "list",
            "initValue": [
                {
                    "control": {
                        "desc": "复选框",
                        "type": "checkbox"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "shishi",
                    "ownerId": "",
                    "text": "时事",
                    "title": "",
                    "value": "shishi"
                },
                {
                    "control": {
                        "desc": "复选框",
                        "type": "checkbox"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "yule",
                    "ownerId": "",
                    "text": "娱乐",
                    "title": "",
                    "value": "yule"
                },
                {
                    "control": {
                        "desc": "复选框",
                        "type": "checkbox"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "qiche",
                    "ownerId": "",
                    "text": "汽车",
                    "title": "",
                    "value": "qiche"
                } 
            ],
            "name": "",
            "ownerId": "ower1",
            "text": "",
            "title": "关注内容",
            "value": "shishi,yule" 
        },
        {
            "control": {
                "desc": "下拉框",
                "type": "select"
            },
            "controlClass": "",
            "controlId": "",
            "initType": "list",
            "initValue": [
                {
                    "control": {
                        "desc": "下拉框选项",
                        "type": "option"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "",
                    "ownerId": "",
                    "text": "家",
                    "title": "",
                    "value": "0"
                },
                {
                    "control": {
                        "desc": "下拉框选项",
                        "type": "option"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "",
                    "ownerId": "",
                    "text": "网吧",
                    "title": "",
                    "value": "2"
                },
                {
                    "control": {
                        "desc": "下拉框选项",
                        "type": "option"
                    },
                    "controlClass": "",
                    "controlId": "",
                    "initType": null,
                    "initValue": null,
                    "name": "",
                    "ownerId": "",
                    "text": "办公室",
                    "title": "",
                    "value": "1"
                }
            ],
            "name": "select",
            "ownerId": "ower1",
            "text": "",
            "title": "登陆位置",
            "value": "0"
        }
    ]
}


  
  $(document).ready(function(e) {
	var fromId=data.fromId;
	builder(data,$('#container'));
	$("#post").click(function(){
		var pro=toData($("#"+fromId),data).propertys;
		$(pro).each(function(){
			writeObj(this); 
		});
	});
  });
  
  function writeObj(obj){ 
	    var description = ""; 
	    for(var i in obj){   
	        var property=obj[i];   
	        description+=i+" = "+property+"\n";  
	    }   
	    alert(description); 
	} 
  
  //生成
  //dataObj:数据对象  target:指定表单的容器(JQ对
  function builder(dataObj,target){
	buildForm(dataObj,target);
	var fromId=dataObj.fromId;
	var controls="";
    var propertys=getPropertys(dataObj);
	for(var i=0;i<propertys.length;i++){
	    buildControl(propertys[i],$("#"+fromId));
	};
	$(".VALIDATEBOX").textbox({
		width:164,
    	height:31, 
	    required:true,
	    missingMessage:"该项不能为空。"
	});
    $(".DATEBOX").datebox({
    	width:164,
    	height:31
    });
    $(".COMBOBOX").combobox({
    	width:164,
    	height:31,
        valueField:"id",
	    textField:"name",
	});
    $(".REGION").combobox({
    	width:98,
    	height:31,
        valueField:"id",
	    textField:"name",
	});
    
  }
  
  //生成表单
  //dataObj:数据对象  target:指定表单的容器(JQ对象)
  function buildForm(dataObj,target){
	  var fromAction="";
	  var fromClass="";
	  var fromId="";
	  var fromName="";
	  
	  if(dataObj.fromAction){
		  fromAction=dataObj.fromAction;
	  };
	  if(dataObj.fromClass){
		  fromClass=dataObj.fromClass;
	  };
	  if(dataObj.fromId){
		  fromId=dataObj.fromId;
	  };
	  if(dataObj.fromName){
		  fromName=dataObj.fromName;
	  }
	  var builderStr="<form action='"+fromAction+"' method='post' class='"+fromClass+" form' id='"+fromId+"' name='"+fromName+"'></form>"
	  target.append(builderStr);
  }
  
  //生成控件
  //propertyObj:控件对象  target:指定表单JQ对象
  function buildControl(propertyObj,target){
	   var controlClass="";
       var controlId="";
	   var control="";
       var initType="";
       var initValue="";
       var name="";
       var text="";
       var title="";
       var value="";
	   
	   if(propertyObj.controlClass){
		   controlClass=propertyObj.controlClass;
	   }
	   if(propertyObj.controlId){
		   controlId=propertyObj.controlId;
	   }
	   if(propertyObj.control){
		   control=propertyObj.control;
	   }
	   if(propertyObj.initType){
		   initType=propertyObj.initType;
	   }
	   if(propertyObj.initValue){
		   initValue=propertyObj.initValue;
	   }
	   if(propertyObj.name){
		   name=propertyObj.name;
	   }
	   if(propertyObj.text){
		   text=propertyObj.text;
	   }
	   if(propertyObj.title){
		   title=propertyObj.title;
	   }
	   if(propertyObj.value){
		   value=propertyObj.value;
	   }
	   
	   if(control.type=="text"){
		  var builderStr="<p class='control_text'><font>"+title+"：</font><input class='"+controlClass+" VALIDATEBOX' id='"+controlId+"' name='"+name+"' type='text' value='"+value+"' /></p>";
		  target.append(builderStr);
		  return true;
	   };
	   if(control.type=="password"){
		  var builderStr="<p class='control_password'><font>"+title+"：</font><input class='"+controlClass+" VALIDATEBOX' id='"+controlId+"' name='"+name+"' type='password' value='"+value+"' /></p>";
		  target.append(builderStr);
		  return true;
	   };
	   if(control.type=="radio"){
		  var builderStr="<p class='control_radio'><font>"+title+"：</font>";
		  for(var i=0;i<initValue.length;i++){
			  if(initValue[i].value==value){
				  builderStr+="<input name='"+name+"' class='"+initValue[i].controlClass+"' id='"+initValue[i].controlId+"' checked='true' type='radio' value='"+initValue[i].value+"'/>"+initValue[i].text;
			  }else{
				  builderStr+="<input name='"+name+"' class='"+initValue[i].controlClass+"' id='"+initValue[i].controlId+"' type='radio' value='"+initValue[i].value+"'/>"+initValue[i].text;
			  } 
		  }
		  builderStr+="</p>";
		  target.append(builderStr);
		  return true;
	   };
	   if(control.type=="checkbox"){
		  var valueArry=value.split(",");
		  var builderStr="<p class='control_checkbox'><font>"+title+"：</font>";
		 outer:for(var i=0;i<initValue.length;i++){
			 for(var j=0;j<valueArry.length;j++){
				 if(initValue[i].value==valueArry[j]){
					  builderStr+="<input name='"+initValue[i].name+"' class='"+initValue[i].controlClass+"' id='"+initValue[i].controlId+"' checked='true' type='checkbox' value='"+initValue[i].value+"'/>"+initValue[i].text;
					  continue outer;
				  }
			 }
			 builderStr+="<input name='"+initValue[i].name+"' class='"+initValue[i].controlClass+"' id='"+initValue[i].controlId+"' type='checkbox' value='"+initValue[i].value+"'/>"+initValue[i].text;
		  } 
		  builderStr+="</p>";
		  target.append(builderStr);
		  return true;
	   };
	   if(control.type=="calendar"){
		  var builderStr="<p class='control_calendar'><font>"+title+"：</font><input class='"+controlClass+" DATEBOX' id='controlId' name='"+name+"' value='"+value+"'/></p>";
		  target.append(builderStr);
		  return true;
	   };
	   if(control.type=="select"){
		  var builderStr="<p class='control_select'><font>"+title+"：</font><select class='"+controlClass+" COMBOBOX' id='controlId' name='"+name+"' value='"+value+"'>";
		  for(var i=0;i<initValue.length;i++){
			  if(initValue[i].value==value){
				  builderStr+="<option value='"+initValue[i].value+"' selected='selected'>"+initValue[i].text+"</option>";
			  }else{
				  builderStr+="<option value='"+initValue[i].value+"'>"+initValue[i].text+"</option>";
			  }
		  }
		  builderStr+="</select></p>";
		  target.append(builderStr);
		  return true;
	  };
	  if(control.type=="addressControl"){
		  var builderStr="<p class='control_region'><font>"+title+"：</font><input class='REGION province "+initValue[0].controlClass+"' name='"+initValue[0].name+"' id='"+initValue[0].controlId+"' value='"+initValue[0].text+"'/>&ensp;<input class='REGION city "+initValue[1].controlClass+"' name='"+initValue[1].name+"' id='"+initValue[1].controlId+"' value='"+initValue[1].text+"'/>&ensp;<input class='REGION district "+initValue[2].controlClass+"' name='"+initValue[2].name+"' id='"+initValue[2].controlId+"' value='"+initValue[2].text+"'/>&ensp;<input type='text' class='VALIDATEBOX"+initValue[3].controlClass+"' name='"+initValue[3].name+"' id='"+initValue[3].controlId+"' value='"+initValue[3].value+"'/></p>";
		  target.append(builderStr);
		  startRegion();
		  return true; 
	  };
  }
  
  function getPropertys(obj){
	  var propertys=obj.propertys;
	  return propertys;
  }
  
  //省市区组件联动
  function startRegion(){
	  $('.province').combobox({
		url:'${ctx}/regionsController/selectProvince',
	    valueField:"id",
	    textField:"name",
	    onSelect:function(record){
	    	/* alert(record.id); */
	    	$('.city').combobox({
	    		url:'${ctx}/regionsController/selectCity?parentId='+record.id,
	    	    valueField:"id",
	    	    textField:"name",
	    	    value:"",
	    	    onSelect:function(record){
	    	    	/* alert(record.id); */
	    	    	$('.district').combobox({
	    	    		url:'${ctx}/regionsController/selectDistrict?parentId='+record.id,
	    	    		valueField:"id",
	    	    	    textField:"name",
	    	    	    value:"",
	    	    	    onSelect:function(record){
	    	    	    	/* alert(record.id); */
	    	    	    }
	    	    	    	    	    	
	    	    	});
	    	    },
				
	    	     onChange:function(){
	    			$('.district').combobox('setText',''); 
	    			
		        }
	    	});
	    },
	     onChange:function(){
			$('.city').combobox('setText','');
			$('.district').combobox('setText','');
		} 
	  });
  }
  
  //获取表单控件组
  //target:表单的jq对象
  function getControl(target){
	  var controls=target.find("p");
	  return controls;
  }
  
  //生成控件数据
  //target:控件对象 <p>...</p>
  function getResult(target){
	  var controlType=target.attr("class");
	  var result=new Object(); 
	  if(controlType=="control_text"){
		  result.value=target.find("input").val();
		  result.type="text";
		  return result;
	  }else if(controlType=="control_password"){
		  result.value=target.find("input").val();
		  result.type="password";
		  return result;
	  }else if(controlType=="control_radio"){
		  result.value=target.find("input:radio[name='sex']:checked").val(); 
		  result.type="radio";
		  return result;
	  }else if(controlType=="control_checkbox"){
		  var str="";
		  var checked=target.find(":checkbox:checked");
		  checked.each(function(){
			  str+=$(this).val()+",";
		  });
		  result.value=str;
		  result.type="checkbox";
		  return result;
	  }else if(controlType=="control_calendar"){
		  result.value=target.find("input").datebox('getValue');
		  result.type="calendar";
		  return result;
	  }else if(controlType=="control_select"){
		  result.value=target.find(".COMBOBOX").combobox('getValue'); 
		  result.type="select";
		  return result;
	  }else if(controlType=="control_region"){
		  var controls=target.find("input.REGION");
		  var address=target.find("input.VALIDATEBOX");
		  result.value=$(controls[0]).combobox('getValue')+","+$(controls[1]).combobox('getValue')+","+$(controls[2]).combobox('getValue');
		  result.text=$(controls[0]).combobox('getText')+","+$(controls[1]).combobox('getText')+","+$(controls[2]).combobox('getText')+","+address.val(); 
		  result.type="region";
		  return result;
	  }
  }
  
  //结果赋值
  //target:表单的jq对象   data:数据源
  function toData(target,data){
	  var controls=getControl(target);
	  var result;
	  var property=getPropertys(data);
	  var resultType;
	  var valueArray;
	  var regions;
	  var textArray;
	  for(var i=0;i<controls.length;i++){
		  result=getResult($(controls[i])); 
		  resultType=result.type;
		  if(resultType=="text"||resultType=="password"||resultType=="radio"||resultType=="checkbox"||resultType=="calendar"||resultType=="select"){
			  property[i].value=result.value;
		  }else if(result.type=="region"){
			  valueArray=result.value.split(",");
			  textArray=result.text.split(",");
			  regions=property[i].initValue;
			  regions[0].value=valueArray[0];
			  regions[1].value=valueArray[1];
			  regions[2].value=valueArray[2];
			  regions[0].text=textArray[0];
			  regions[1].text=textArray[1];
			  regions[2].text=textArray[2];
			  regions[3].value=textArray[3];
			  property[i].initValue=regions;
		  }
	  }
	  data.propertys=property;
	  return data;
  }
</script>
</body>  
</html>