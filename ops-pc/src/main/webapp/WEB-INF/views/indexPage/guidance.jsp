<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<c:set var="isDetail" value="${param.flag eq 'detail'}"></c:set>
<!-- 从列表页传入参数    flag - edit,detail,copy   billCode - 单据编码 -->
<c:set var="billflagName" value="${param.flag=='detail'?'查看':param.flag=='copy'?'复制':param.flag=='edit'?'编辑':'新增'}" scope="page"></c:set>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<%@ include file="/WEB-INF/views/common/js.jsp"%>
<link rel="stylesheet" href="${ctx}/resources/css/warehousebill.css" />
<title>${billName}</title>
</head>
<style>
*{margin: 0px;padding: 0px;}
.shadow{position: absolute; height: 89%; width:100%; }
#billbox{ height:675px; }
.initialize{width:1200px; margin: auto; margin-top: 25px; overflow: hidden; }
.series,.function_item{ float: left;}
.series{ width:132px; height:490px; padding-top: 10px;}
.function_item{width:900px; height:504px;}
.function_item ul{overflow: hidden;}
.function_item ul li{ width:100px;float: left; margin:0px 50px 30px 0px; text-align: center;cursor:pointer;}
.function_item ul li span{font-size: 12px;}
.process_three{width:150px; height: 45px; line-height:45px; text-align:center; cursor:pointer; margin-left:80%;
 border-radius:5px; -webkit-border-radius: 5px;-moz-border-radius: 5px; background: #66d0fc; color: #fff; font-size: 18px;}
/* .initialize .process_three img{cursor:pointer;} */
.process_three:hover{background: #51daf8;}
#title1{height: 18px;}
#title{ margin-bottom: 50px;}
a{color: #000;}
</style>
<body>

<form action="" class="bill-form myform" id="warehousebillForm">
	<div id="title">
		<div id="title1" class="shadow" style="margin:10px 0 0 0;	padding:11px 0; ">
			<div id="square1"><div id="triangle"></div></div><h1>初始化设置</h1><a id="hide" onClick="aniTo('bill');" style="display:none;position:absolute;right:40px;top:13px;" class="btn-ora-add">单据信息</a>
		</div>
	</div>
	<div id="billbox" class="shadow" style="margin-top:50px;">
		<div class="billTitle"><div id="square2"></div><h2>数据录入</h2></div>
		<div class='initialize'>
		    <div class="series"><img src="${ctx}/resources/images/step.png" width="80" height="450"></img></div>
		    <div class="function_item">
		        <ul>
		            <li _url="/orgController/deptMessage?sys_menu_id=402881114d8da47b014d8db6d269004a" _text="部门信息维护"><img src="${ctx}/resources/images/index/bmxxwh_icon.png" width="48" height="48"><br/><span>部门信息维护</span></li>
		            <li _url="/roleController/roleMessageUI?sys_menu_id=402881114d8da47b014d8dc1b63d008f" _text="角色信息维护"><img src="${ctx}/resources/images/index/jsxxwh.png" width="48" height="48"><br/><span>角色信息维护</span></li>
		            <li _url="/sysmanPageController/roleRights?sys_menu_id=402881114d8da47b014d8dceb1ef00ee" _text="角色权限维护"><img src="${ctx}/resources/images/index/jsqxwh_icon.png" width="48" height="48"><br/><span>角色权限维护</span></li>
		            <li _url="/basedata/listAuxiliaryAttr?sys_menu_id=402881b34fb50c8f014fb519cffa0007" _text="辅助属性"><img src="${ctx}/resources/images/index/fzsx_icon.png" width="48" height="48"><br/><span>辅助属性</span></li>
		            <li _url="/member/memberManager?sys_menu_id=402881b34fb50c8f014fb51826330005" _text="企业人员"><img src="${ctx}/resources/images/index/qyry_icon.png" width="48" height="48"><br/><span>企业人员</span></li>
		            <li _url="/sysmanPageController/authorizeByRole?sys_menu_id=402881114d8da47b014d8dd325470104" _text="按角色授权"><img src="${ctx}/resources/images/index/ajssq_icon.png" width="48" height="48"><br/><span>用户权限</span></li>
		        </ul>
		         <ul>
		            <li _url="/bankController/bankManager?sys_menu_id=402881b34fb50c8f014fb51b83220009" _text="银行信息"><img src="${ctx}/resources/images/index/yhxx_icon.png" width="48" height="48"><br/><span>现金银行信息</span></li>
		            <li _url="/storehouses/manage?sys_menu_id=402881104ddaf19b014ddaf7eb8f0009" _text="仓库管理"><img src="${ctx}/resources/images/index/ckgl_icon.png" width="48" height="48"><br/><span>仓库管理</span></li>
		            <li _url="/customer/manage?sys_menu_id=402881b34fd539d0014fd58eadb60001" _text="客户管理"><img src="${ctx}/resources/images/index/khgl_icon.png" width="48" height="48"><br/><span>客户管理</span></li>
		            <li _url="/supplier/manage?sys_menu_id=402881b34fd3d414014fd3dbc2dd0003" _text="供应商管理"><img src="${ctx}/resources/images/index/gysgl_icon.png" width="48" height="48"><br/><span>供应商管理</span></li>
		        </ul>
		        <ul>
		            <li _url="/fiscalPeriod/manage?sys_menu_id=402881e651187c8c0151187e89ac0000" _text="财务会计期间"><img src="${ctx}/resources/images/index/cwkjqj_icon.png" width="48" height="48"><br/><span>会计期间</span></li>
		            <li _url="/initialBankController/initialBankManager?sys_menu_id=4028811150117df5015011ffcfa60025" _text="现金银行期初"><img src="${ctx}/resources/images/index/xjyhkj_icon.png" width="48" height="48"><br/><span>现金银行期初</span></li>
		            <li _url="/initialPayController/initialPayManager?sys_menu_id=4028811150117df501501204f3b0002c" _text="期初应付"><img src="${ctx}/resources/images/index/yfcq_icon.png" width="48" height="48"><br/><span>期初应付</span></li>
		            <li _url="/initialReceivableController/initialReceivableManager?sys_menu_id=4028811150117df5015012061607002f" _text="期初应收"><img src="${ctx}/resources/images/index/qcyf_icon.png" width="48" height="48"><br/><span>期初应收</span></li> 
		        </ul>
		         <ul>
		            <li _url="/unitController/unitManage?sys_menu_id=4028811150117df50150120e0d22003f" _text="货品单位"><img src="${ctx}/resources/images/index/hpdw_icon.png" width="48" height="48"><br/><span>货品单位</span></li>
		            <li _url="/goodsspec/manage?sys_menu_id=4028811150117df50150120f28bd0041" _text="货品属性"><img src="${ctx}/resources/images/index/hpsx_icon.png" width="48" height="48"><br/><span>货品属性</span></li>
		            <li _url="/goods/manage?sys_menu_id=4028811150117df5015012122b290044" _text="货品维护"><img src="${ctx}/resources/images/index/hpwh_icon.png" width="48" height="48"><br/><span>货品维护</span></li>
		            <li _url="/goodsPriceController/goodsPriceManager?sys_menu_id=402881b34fb0e822014fb0ef301d000a" _text="货品定价"><img src="${ctx}/resources/images/index/hpdj_icon.png" width="48" height="48"><br/><span>货品定价</span></li> 
		            <li _url="/initialstock/qckc/manage?sys_menu_id=402881b35003365101500339c1ff0003" _text="期初库存"><img src="${ctx}/resources/images/index/qckc_icon.png" width="48" height="48"><br/><span>期初库存</span></li> 
		        </ul>
		         <ul>
		           	<li _url="/fiscalSubject/manage?sys_menu_id=402881e6511d9e7d01511d9ec1720012" _text="财务科目"><img src="${ctx}/resources/images/index/cwkm_icon.png" width="48" height="48"><br/><span>财务科目</span></li>
		            <li _url="/initiBalance/manage?sys_menu_id=402881e65123a48f015123a658840001" _text="科目初始数据"><img src="${ctx}/resources/images/index/kmcsh_icon.png" width="48" height="48"><br/><span>科目初始数据</span></li>
<%-- 		            <li _url="/fiscalConfig/manage?sys_menu_id=402881ed51226308015122cbd24b0007" _text="财务参数设置"><img src="${ctx}/resources/images/index/cwcssz_icon.png" width="48" height="48"><br/><span>财务参数设置</span></li>
		            <li _url="/fiscalAccount/manage?sys_menu_id=402881e6511445780151145e74690003" _text="财务帐套"><img src="${ctx}/resources/images/index/cwzt_icon.png" width="48" height="48"><br/><span>财务帐套</span></li> --%>
		        </ul>
		    </div>
		   <%--  <div class="process_three"><img src="${ctx}/resources/images/index/start.png" width="70" height="70"></div> --%>
		  
		</div>
		 <div class="process_three">点击这里进入系统</div>
	</div>
</form>
<script>
  $(function(){//获取信息调用父页面方法
	  $('ul li').click(function(){
		 var text=$(this).attr('_text');
		 var src=$(this).attr('_url');
		 parent.kk(src,text);//kk(src,text)为父页面index方法
	  });
    
    //点击绿色图标完成设置 
    var First=parent.userID();//用户ID  
   /*  var First='First';  */
    $('.process_three').click(function(){
		$.fool.confirm({msg:'确定已设置好初始数据，若后期修改可在对应的模块中设置',fn:function(r){
			if(r){	
			   localStorage.setItem(First,1); 
			   setTimeout("location.href='${ctx}/indexController/indexPage'",30)
			}
	 	},title:'确认'});
	  }); 
  });
 
  
</script>
</body>
</html>