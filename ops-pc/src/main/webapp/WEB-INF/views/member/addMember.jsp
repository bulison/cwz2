<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>

<html>
<head>
</head>
<body>
<style>
	.form,.form1{padding:10px 0px;}
	.form1 p{margin:10px 0px 0px 0px;}
	.em{display:none;}
</style>
<div class="form1" >
	<form id="form">
		<h3>&emsp;基础信息：</h3>
		<hr/>
		<input name="fid" id="fid" type="hidden" value="${entity.fid}"/>
		<input name="updateTime" id="updateTime" type="hidden" value="${entity.updateTime}"/>
		<p><font><em>*</em>编号：</font><input id="userCode" type="text" value="${entity.userCode}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',validType:'normalChar',novalidate:true" /></p>
		<p><font><em>*</em>名称：</font><input id="userName" type="text" value="${entity.username}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',validType:'normalChar',novalidate:true,width:167,height:31,editable:false" /></p>
		<p><font><em>*</em>手机号码：</font><input id="phoneOne" type="text" value="${entity.phoneOne}" class=" easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空！',novalidate:true,validType:'phone'" /></p>
		<p><font><em>*</em>部门：</font><input id="deptName" type="text" class="textBox" value="${entity.deptId}"/></p>
		<p><font><em>*</em>允许Web登录：</font><input id="isWebLogin" name="isWebLogin" type="text"/></p>
		<p><font><em>*</em>允许移动端登陆：</font><input id="isMobileLogin" name='isMobileLogin' type="text"/></p>
		<p><font><em class="em">*</em>登录帐号：</font><input id="loginName" type="text" class="easyui-validatebox" value="${entity.loginName}" data-options="required:false,validType:'myAccount'"/></p>
		${empty param.fid?'<p><font>登录密码：</font><input id="password" class="textBox easyui-textbox" data-options="prompt:\'初始密码123456\',width:160,height:30,type:\'password\',required:false"/></p>':''}
		<hr/><br/>
		<h3 style="display: inline-block;">&emsp;详细信息：</h3><img id="openBtn" alt="展开" src="${ctx}/resources/images/openNode.png" style="vertical-align: middle;"><img id="closeBtn" alt="展开" src="${ctx}/resources/images/closeNode.png" style="vertical-align: middle;display: none">
		<hr/>
		<div id="hideDiv" style="overflow: auto;max-height: 200px">
		<p class="hideOut"><font>性别：</font><input id="sex" name="sex" type="text" class=" textBox"/></p>
		<p class="hideOut"><font>工号：</font><input id="jobNumber" type="text" value="${entity.jobNumber}" class=" easyui-validatebox textBox"/></p>
		<p class="hideOut"><font>邮箱：</font><input id="email" type="text" value="${entity.email}" class=" easyui-validatebox textBox" data-options="validType:'email'"/></p>
		<p class="hideOut"><font>邮编：</font><input id="postcode" type="text" value="${entity.postcode}" class=" easyui-validatebox textBox"/></p>
		<p class="hideOut"><font>地址：</font><input id="address" type="text" value="${entity.address}" class=" easyui-validatebox textBox"/></p>
		<p class="hideOut"><font>传真：</font><input id="fax" type="text" value="${entity.fax}" class=" easyui-validatebox textBox"/></p>
		<p class="hideOut"><font>身份证：</font><input id="idCard" type="text" value="${entity.idCard}" class=" easyui-validatebox textBox"/></p>
		<p class="hideOut"><font>电话：</font><input id="phoneTwo" type="text" value="${entity.phoneTwo}" class="easyui-validatebox textBox" data-options="validType:'tel'"/></p>
		<p class="hideOut"><font>描述：</font><input id="userDesc" type="text" value="${entity.userDesc}" class=" easyui-validatebox textBox"/></p>
		<p class="hideOut"><font>是否部门负责人：</font><input id="isInterface" name="isInterface" type="text"/></p>
		<p class="hideOut"><font>入职日期：</font><input id="entryDay" type="text" value="${entity.entryDay}" class=" easyui-datebox textBox" data-options="width:160,height:31,editable:false"/></p>
		<p class="hideOut"><font>职位：</font><input id="position" type="text" value="${entity.position}" class=" easyui-validatebox textBox"/></p>
		<p class="hideOut"><font>工作状态：</font><input id="jobStatusName" type="text" class=" textBox"/></p>
		<p class="hideOut"><font>学历：</font><input id="educationName" type="text" class="textBox"/></p>
		<p class="hideOut"><font>离职日期：</font><input id="departureDate" type="text" value="${entity.departureDate}" class=" easyui-datebox textBox" data-options="width:160,height:31,editable:false"/></p>
		<p class="hideOut"><font>离职原因：</font><input id="departureReason" type="text" value="${entity.departureReason}" class=" easyui-validatebox textBox"/></p>
		</div>
		<hr/>
		<p style="margin-left:220px"><font><input id="save" type="button" class="btn-blue2 btn-xs" value="保存"/></font></p>

	</form>
</div>

<script type="text/javascript">
    //设置wed登录或者是移动端登录，登录名为填
	/*  $('#loginName').change(function(){
	 log();
	 }); */
	$('#hideDiv').scroll(function () {
        $(this).find('input').blur();
    })
    var vouComboIs = $("#isWebLogin").fool("dhxCombo",{
        width:160,
        height:32,
        required:true,
        novalidate:true,
        focusShow:true,
        clearOpt:false,
        editable:false,
        data:[
            {
                value: '1',
                text: '是'
            },{
                value: '0',
                text: '否'
            }
        ],
        onChange:function(){
            log();
        },
    });

    var vouComboLogin= $("#isMobileLogin").fool("dhxCombo",{
        width:160,
        height:32,
        required:true,
        novalidate:true,
        clearOpt:false,
        editable:false,
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
        onChange:function(){
            log();
        },
    });

    function log(){
        var isWebLogin=vouComboIs.getSelectedValue();//获取控件值
        var isMobileLogin=vouComboLogin.getSelectedValue();//获取控件值
        if(isWebLogin==1||isMobileLogin==1){
            $('#loginName').validatebox({required:true,validType:"myAccount"});
            $('.em').css('display','inline-block');
        }else{
            $('#loginName').validatebox({required:false,validType:"myAccount"});
            $('.em').css('display','none');
        }
    }

    if('${entity.fid}'){
        vouComboIs.setComboValue("${entity.isWebLogin}");
        vouComboLogin.setComboValue("${entity.isMobileLogin}");
    }

    $("input").attr('autocomplete','off');
    var sexValue= $("#sex").fool("dhxCombo",{
        width:160,
        height:32,
        editable:false,
        focusShow:true,
        clearOpt:false,
        data:[
            {
                value:'',
                text:'请选择',
            },
            {
                value: '1',
                text: '男'
            },{
                value: '0',
                text: '女'
            }
        ],
        onLoadSuccess:function(combo){
            combo.setComboValue("${entity.sex}");
        }
    });


    var isInterfaceValue= $("#isInterface").fool("dhxCombo",{
        width:160,
        height:32,
        editable:false,
        focusShow:true,
        clearOpt:false,
        data:[
            {
                value:'',
                text:'请选择',
            },
            {
                value: '1',
                text: '是'
            },{
                value: '0',
                text: '否'
            }
        ],
        onLoadSuccess:function(combo){
            combo.setComboValue("${entity.isInterface}");
        }
    });
    //部门初始化
    var deptValue='';
    $.ajax({
        url:"${ctx}/orgController/getAllTree?num="+Math.random(),
        async:false,
        success:function(data){
            deptValue=formatTree(data[0].children,'text','id');
        }
    });
    var deptNameValue= $("#deptName").fool("dhxCombo",{
        width:160,
        height:32,
        data:deptValue,
        clearOpt:false,
        required:true,
        novalidate:true,
        editable:false,
        focusShow:true,
        toolsBar:{
            name:"部门",
            addUrl:"/orgController/deptMessage",
            refresh:true
        },
        onLoadSuccess:function(combo){
            combo.setComboValue("${entity.deptId}");
        },
    });

    //工作状态初始化
    var jobStatusValue='';
    $.ajax({
        url:"${ctx}/member/jobStatusList?num="+Math.random(),
        async:false,
        success:function(data){
            jobStatusValue=formatData(data[0].children,"id","text");
        }
    });
    var jobStatusName= $("#jobStatusName").fool("dhxCombo",{
        width:160,
        height:32,
        data:jobStatusValue,
        editable:false,
        clearOpt:false,
        focusShow:true,
        toolsBar:{
            name:'在职状况',
            addUrl:'/basedata/listAuxiliaryAttr',
            refresh:true
        },
        onLoadSuccess:function(combo){
            combo.setComboValue("${entity.jobStatusId}");
        }
	});

	/* $("#educationName").combotree({
	 url:"${ctx}/member/eduList",
	 width:160,
	 height:31,
	 editable:false,
	 onLoadSuccess:function(node, data){
	 if(data[0].id!=""){
	 var node=$("#educationName").combotree('tree').tree("find",data[0].id);
	 $("#educationName").combotree('tree').tree('update',{
	 target: node.target,
	 text: '请选择',
	 id:""
	 });
	 }
	 $("#educationName").combotree('setValue','${entity.educationId}');
	 }
	 }); */

    //学历初始化数据
    var educationValue='';
    $.ajax({
        url:"${ctx}/member/eduList?num="+Math.random(),
        async:false,
        success:function(data){
            educationValue=formatData(data[0].children,'id','text');
        }
    });
    var educationName= $("#educationName").fool("dhxCombo",{
        width:160,
        height:32,
        clearOpt:false,
        data:educationValue,
        editable:false,
        focusShow:true,
        toolsBar:{
            name:'学历',
            addUrl:'/basedata/listAuxiliaryAttr',
            refresh:true
        },
        onLoadSuccess:function(combo){
            combo.setComboValue("${entity.educationId}");
        }
    });

    $("#openBtn").click(
        function(e) {
            $(".hideOut").css("display","inline-block");
            $('#openBtn').css("display","none");
            $('#closeBtn').css("display","inline-block");
        });
    $("#closeBtn").click(
        function(e) {
            $(".hideOut").css("display","none");
            $('#openBtn').css("display","inline-block");
            $('#closeBtn').css("display","none");
        });

    $('#save').click(function(e) {
        var fid=$('#fid').val();
        var userName=$('#userName').val();
        var sex=sexValue.getSelectedValue();//获取控件值
        var email=$('#email').val();
        var postcode=$('#postcode').val();
        var address=$('#address').val();
        var fax=$('#fax').val();
        var idCard=$('#idCard').val();
        var isWebLogin=vouComboIs.getSelectedValue();//获取控件值
        var isMobileLogin=vouComboLogin.getSelectedValue();//获取控件值
        var phoneOne=$('#phoneOne').val();
        var phoneTwo=$('#phoneTwo').val();
        var userDesc=$('#userDesc').val();
        var isInterface=isInterfaceValue.getSelectedValue();
        var userCode=$('#userCode').val();
        var jobNumber=$('#jobNumber').val();
        var entryDay=$('#entryDay').datebox('getValue');
        var position=$('#position').val();
        var departureDate=$('#departureDate').datebox('getValue');
        var departureReason=$('#departureReason').val();
        var updateTime=$('#updateTime').val();
        var deptId=deptNameValue.getSelectedValue();//获取控件值
        var jobStatusId=jobStatusName.getSelectedValue();
        var educationId=educationName.getSelectedValue();
        var loginName=$('#loginName').val();
        var mydata = {fid:fid,loginName:loginName, username:userName, sex:sex, email:email, postcode:postcode, address:address, fax:fax, idCard:idCard, isWebLogin:isWebLogin, isMobileLogin:isMobileLogin, phoneOne:phoneOne, phoneTwo:phoneTwo, userDesc:userDesc, isInterface:isInterface, userCode:userCode, jobNumber:jobNumber, entryDay:entryDay, position:position, departureDate:departureDate, departureReason:departureReason, updateTime:updateTime, deptId:deptId, jobStatusId:jobStatusId, educationId:educationId};
        if($('#password').length>0){
            var password = $('#password').textbox('getValue');
            mydata = $.extend(mydata,{password:password});
        }
        $('#form').form('enableValidation');
        if($('#form').form('validate')){
            $('#save').attr("disabled","disabled");
            $.post('${ctx}/member/save',mydata,function(data){
                dataDispose(data);
                if(data.result=='0'){
                    $.fool.alert({time:1000,msg:'保存成功！',fn:function(){
                    	if(dhxkey == 1){
                            selectTab(dhxname,dhxtab);
                        }
                        $('#save').removeAttr("disabled");
                        $('#addBox').window('close');
                        //$('#memberList').datagrid('reload');
                        $('#memberList').trigger('reloadGrid');
                    }});
                }else if(data.result=='1'){
                    $.fool.alert({msg:data.msg});
                    $('#save').removeAttr("disabled");
                }else{
                    $.fool.alert({time:1000,msg:"系统正忙，请稍后再试。"});
                    $('#save').removeAttr("disabled");
                }
                return true;
            });
        }else{
            return false;
        }
    });
</script>
</body>
</html>