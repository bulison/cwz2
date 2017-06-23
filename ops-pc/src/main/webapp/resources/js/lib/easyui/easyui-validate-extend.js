//ldx
//esayui验证扩展

$.extend($.fn.validatebox.defaults.rules, {
	amount:{//验证金额，小数点后两位
        validator: function (value, param) {
        	if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
  			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
  				  $(this).unbind("blur");
  				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
  					    var value = $(this).val();
          				var myvalue = "";
          				if(value!="" && !isNaN($.trim(value))){
	          					var nv = $.trim(value)+"";
	          					if(nv!=""){
	          						myvalue = parseFloat(nv).toFixed(2)+'';
	          					}
          				}
          				if($(this).attr("class").indexOf("textbox-text")!=-1){
      						$(this).parent().prev().textbox('setText',myvalue);
      						$(this).parent().prev().textbox('setValue',myvalue);
      					}else{
      						$(this).val(myvalue);
      					}	
          		});
          		}
        	  }
         	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
         },
         message:'金额必须大于等于0，并最多2位小数，请输入正确的金额'

     },	
     minus:{//验证金额，小数点后两位
         validator: function (value, param) {
         	if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
   			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
   				  $(this).unbind("blur");
   				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
   					    var value = $(this).val();
           				var myvalue = "";
           				if(value!="" && !isNaN($.trim(value))){
 	          					var nv = $.trim(value)+"";
 	          					myvalue = parseFloat(nv).toFixed(2)+'';
           				}
 	      					if($(this).attr("class").indexOf("textbox-text")!=-1){
 	      						$(this).parent().prev().textbox('setText',myvalue);
 	      						$(this).parent().prev().textbox('setValue',myvalue);
 	      					}else{
 	      						$(this).val(myvalue);
 	      					}
           		});
           		}
         	  }
          	return (/^([-][0-9]\d*)(\.\d{1,2})?$/).test(value);
          },
          message:'金额必须小于0，并最多2位小数，请输入正确的金额'
     },	
     nteger:{//验证金额，小数点后两位
        validator: function (value, param) {
         	return (/^(([1-9]\d*)|\d)?$/).test(value);
         },
         message:'请输入整数'

     },	
	intOrFloat:{
        validator: function (value) {
            var b = /^\d+(\.\d+)?$/i.test(value);
            if(!b)return false;
            else 
           	 return value>0;
        },
        message: '请输入数字，并确保大于0'
    },
	phone:{//手机验证,暂时取消限制
		validator: function (value, param) {
            return /*/^(?:1(3|5|7|8)\d)-?\d{5}(\d{3}|\*{3})$/.test(value);*/true;
		},message:"请输入11位数的手机号码！"
	},
	// tel:{//固定电话验证,暂时取消限制
	// 	 validator:function(value, param){
	//         return /^(\d{3}-|\d{4}-)?(\d{8}|\d{7})?(-\d{1,6})?$/.test(value);
	//      },message:"请输入正确的电话号码如：0751-5870829"
	// },
    tel:{//固定电话验证,控制字符长度
        validator:function(value, param){
            return /^\d{6,20}$/.test(value)
        },message:"请输入6-20个字符且不包含非法字符"
    },
	fax:{//传真验证
		validator:function(value,param){
			return /*/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-|0\d{2,3})?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value)*/true; 
		},message:"请输入正确的传真格式如：0751-5870829"
	},
	name:{//名称验证
		validator:function(value,param){
		    return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value); 
		},message:"用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）！"
	},
	age:{//验证年龄
		validator:function(value,param){
			return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value); 
		},message:"年龄必须在1-120之间！"
	},
	normalChar:{//验证是否为空或者含有非法字符
		validator:function(value,param){
		  return /^[a-zA-Z0-9\u4E00-\u9FA5]+$/.test(value);
		},message:"输入值不能为空和包含其他非法字符！"
	},
	norma:{//验证是否为空或者含有非法字符括号除外
		validator:function(value,param){
			return !/[@#\$%\^&……\￥*\！!\。，\.,\；;\‘“\'"\~·\~`\-_\——、\：:\?？\/\\=+]+/g.test(value); 
		},message:"输入值不能包含其他非法字符！"
	},
	currency:{//验证货币
		validator:function(value,param){
			return /^\d+(\.\d+)?$/i.test(value);
		},message:"货币格式错误！"
	},
	describe:{//描述
		validator:function(value,param){
			return "^\\d{6,80}$".test(value);
		},message:"请输入6-50个字符！"
	},
	minLength: {//最小值    
        validator: function(value, param){    
            return value.length >= param[0];    
        },message: "请输入至少 {0} 个字符！"   
    },
    maxLength: {//最大值
        validator: function(value, param){    
            return value.length <= param[0];    
        },message: "不能超过  {0} 个字符！"   
    },
    numMaxLength: {//限制小数点前位数最大值
        validator: function(value, param){
        	var absvalue = Math.abs(parseFloat(value))+"";
        	var myvalue =absvalue.indexOf('.')>0?absvalue.substring(0,absvalue.indexOf('.')):absvalue;
            return myvalue.length <= param[0];    
        },message: "小数点前的位数不能超过  {0} 位！"   
    },
    /*date:{ //时间格式 格式yyyy-MM-dd或yyyy-M-d
        validator : function(value) { 
            return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value); 
        },message : "清输入合适的日期格式"    
    }*/
    //日期比较拓展
    dateLargeThan: {
    	validator: function(value, param){
	    	date2 = $(param[0]).datetimebox('getValue');
	    	var end=new Date(value.replace("-", "/").replace("-", "/"));
	    	var start=new Date(date2.replace("-", "/").replace("-", "/"));
	    	var varify=end>=start;
	    	return varify;
    	},message: "{1}"
    },
    //日期比较拓展-2
    dateLargeThanVal: {
    	validator: function(value, param){
	    	var end=new Date(value.replace("-", "/").replace("-", "/"));
	    	var start=new Date(param[0].replace("-", "/").replace("-", "/"));
	    	var varify=end>=start;
	    	return varify;
    	},message: "{1}"
    },
    idcard:{// 验证身份证 
        validator : function(value) { 
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value); 
        },message :"身份证号码格式不正确" 
    },
    zip:{// 验证邮政编码 
        validator : function(value) { 
            return /^[1-9]\d{5}$/i.test(value); 
        },message : "邮政编码格式不正确,请输入不以零开头的六位数字," 
    },
    startDate:{
		validator : function(value,param){
			var date=new Date();
			var currMon="";
			var currday="";
			if(date.getMonth()>9){
				currMon=Number(date.getMonth()+1);
			}else{
				currMon='0'+(Number(date.getMonth())+1);
			}
			if(date.getDate()>9){
				currday=date.getDate();
			}else{
				currday='0'+date.getDate();
			}
			var curr=date.getFullYear()+"-"+currMon+"-"+currday; 
			var planStart=$(param[0]).val();
			var planEnd=$(param[1]).val();
			var parentStart=$(param[2]).val();
			var parentEnd=$(param[3]).val();
			 
			if(parentStart!=null&&parentStart!=''){
				return daysBetween(value,parentStart)>=0&&daysBetween(value,parentEnd)<=0&&daysBetween(value,curr)>=0;
			}else{
				return daysBetween(value,planStart)>=0&&daysBetween(value,planEnd)<=0&&daysBetween(value,curr)>=0;
			}
		}, 
        message : '事件开始时间必须在上级事件时间范围内且在当前时间之后！',
	},
	endDate:{
		validator : function(value,param){
			var planStart=$(param[0]).val();
			var planEnd=$(param[1]).val();
			var parentStart=$(param[2]).val();
			var parentEnd=$(param[3]).val();
			
			if(parentStart!=null&&parentStart!=''){
				return daysBetween(value,parentStart)>=0&&daysBetween(value,parentEnd)<=0;
			}else{ 
				return daysBetween(value,planStart)>=0&&daysBetween(value,planEnd)<=0;
			}
		},
		message:'事件结束时间必须在上级事件时间范围内！',
	},
	isBank:{
		validator : function(value,param){
			return $.trim(value) != '';
		},
		message:'不能为空，也不能全为空格'
	},
	subjectCode:{
        validator: function (value) {
            return /^\d+(\.\d+)*$/i.test(value);
        },
        message: '请输入数字，用"."分隔。'
    },
	planEnd:{
	    	validator: function (value, param) {
	    		var valueStr=value.slice(0,4)+value.slice(5,7)+value.slice(8,10);
	    		var billdate=$("#billDate").datebox("getValue");
	    		var billdateStr="";
	    		if(billdate){
	    			billdateStr=billdate.slice(0,4)+billdate.slice(5,7)+billdate.slice(8,10);
	    		}else{
	    			billdateStr=0;
	    		}
	    		return parseInt(valueStr)>=parseInt(billdateStr);
	    	},
	           message:'计划完成时间不可早于单据日期。'
	  },
	  integer: {// 验证整数 可正负数
          validator: function (value) {
              return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
          },
          message: '请输入整数'
      },
    number: {
        validator: function (value) {
            return /^\d*$/i.test(value);
        },
        message: '请输入数字'
    },
      accessoryNumber:{
    	  validator:function(value){
    		  return /^([+]?[0-9])+\d*$/i.test(value);
    	  },
    	  message: '请输入大于等于0的整数'
      },
      accessory:{
    	  validator:function(value){
    		  return /^([+]?[1-9])+\d*$/i.test(value);
    	  },
    	  message: '请输入大于0的整数'
      },
      assetValue:{
    	  validator:function(value){
    		  if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
    			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
    				  $(this).unbind("blur");
    				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
    					    var value = $(this).val();
            				var myvalue = "";
            				if(value!="" && !isNaN($.trim(value))){
	          					var nv = $.trim(value)+"";
	          					myvalue = parseFloat(nv).toFixed(2)+'';
            				}
            				if($.trim(value)==""){
                                myvalue="";
							}
	      					if($(this).attr("class").indexOf("textbox-text")!=-1){
	      						$(this).parent().prev().textbox('setText',myvalue);
	      						$(this).parent().prev().textbox('setValue',myvalue);
	      					}else{
	      						$(this).val(myvalue);
	      					}
            		});
            		}
          	  }
    		  return /^\d+(\.\d{1,2})*$/i.test(value);
    	  },
    	  message:'请输入大于等于0的数，且小数位最多2位'
      },
      combogridValid:{
      	validator: function(value,param){
      		return $('#'+param).val()==''?false:true;
      	},
      	message: '该项没有选中，请重新选择'
      },
      nocobgValid: {    
          validator: function(value,param){
              return $('#'+param).val()==$(this).val();    
          },    
          message: '该项已经被修改，但并未选中，请重新选择'   
      },
      balanceAmount:{
    	  validator:function(value){
    		  if("undefined"!=typeof $(this).attr("class")){//去除前后空格tyc
    			  if($(this).attr("class").indexOf("validatebox-text")!=-1){
    				  $(this).unbind("blur");
    				  $(this).blur(function(){//对数字进行去空格和自动保留2位小数
    					    var value = $(this).val();
            				var myvalue = "";
            				if(value!="" && !isNaN($.trim(value))){
	          					var nv = $.trim(value)+"";
	          					myvalue = parseFloat(nv).toFixed(2)+'';
            				}
	      					if($(this).attr("class").indexOf("textbox-text")!=-1){
	      						$(this).parent().prev().textbox('setText',myvalue);
	      						$(this).parent().prev().textbox('setValue',myvalue);
	      					}else{
	      						$(this).val(myvalue);
	      					}
            		});
            		}
          	  }
    		  return /^(-)?\d+(\.\d{1,2})*$/i.test(value);
    	  },
    	  message:'请输入数字，且小数位最多2位'
      },
      subjValid: {    
	      validator: function(value,param){
	          var mytips = $(this).next().attr('mytips');
	          return mytips==1?false:true;    
	      },    
	      message: '请按右箭头选择下级科目'   
	  },
	  dateCompar:{//日期比较,符合条件则返回false
	    	 validator:function(value,param){
	    		// alert(param[0]+","+param[1]+","+param[2]);//比较的对象ID或class，判断标识，错误提示信息
	    		 var _val = new Date(param[0].replace("-", "/").replace("-", "/"));
	    		 var v = new Date(value.replace("-", "/").replace("-", "/"));
	    		 var _a =(v-_val);
	    		 if($.fool._get$(param[0]).length > 0){
	    			 _val =$.fool._get$(param[0]).datetimebox('getValue');
	    			 _a =(Date.parse(value)-Date.parse(_val))/3600/1000;
	    		 }
	    		 if(param[1]=='-1'&&_a<0)//小于
	    			 return false;
	    		 else if(param[1]=='1'&&_a>0)//大于
	    		 	return false;
	    		 else if(param[1]=='0'&&_a==0)//等于
	    			 return false;    			 
	    		 return true;//传入参数错误
	    	 },
	    	 message:'{2}'
	  },
	  myAccount:{//登录账号验证
	    	 validator:function(value,param){
		    		 return /^[A-Za-z0-9]{1}(_|[A-Za-z0-9]){0,19}?$/i.test(value); 
		    	 },
		     message:'登录账号只能使用数字，字母，下划线，不能以下划线开头，并且不超过20个字符'
	  },
	  noNum:{
		  validator:function(value,param){
	    		 return /^[a-zA-Z\u4E00-\u9FA5]+?$/i.test(value); 
	    	 },
	      message:'名称不能包含数字和其他非法字符，请重新输入'
	  },
	  pointNum:{
    	  validator:function(value){
    		  return /^(-)?\d+(\.\d{1,6})*$/i.test(value);
    	  },
    	  message:'请输入数字，且小数位最多6位'
      },
      itemNum:{
    	  validator:function(value){
    		  return /^([0-9]*[a-zA-Z\u4E00-\u9FA5]+[0-9]*)+$/i.test(value);
    	  },
    	  message:'不能为非法字符或全是数字'
      },
      dateFormat:{//日期格式
 		 validator:function(value, param){
 	        return /^\d{4}-\d{2}-\d{2}$/.test(value);
 	     },message:"请输入正确的日期如：2000-01-01"
      },
      scale:{//比例范围
  		 validator:function(value, param){
  	        return value>=param[0]&&value<=param[1];
  	     },message:"比例范围为：{0}%到{1}%"
       },
});