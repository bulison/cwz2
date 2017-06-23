<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>个人信息管理</title>
    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <%@ include file="/WEB-INF/views/common/js.jsp" %>
    <script type="text/javascript" src="${ctx}/resources/js/ajaxupload.js?v=${js_v}"></script>

    <style>
        #form h1{font-size:18px;padding:10px 0 10px 50px;margin:0 0 0 60px}
        .base{background:url("${ctx}/resources/images/jibenziliao.png") no-repeat;}
        .tontact{background:url("${ctx}/resources/images/lianxifangshi.png") no-repeat;}
        .upPsw{background:url("${ctx}/resources/images/xiugaimima.png") no-repeat;}
        .form1{margin-bottom:10px}

    </style>
</head>
<body>
<div class="titleCustom">
    <div class="squareIcon">
        <span class='Icon'></span>
        <div class="trian"></div>
        <h1>个人信息管理</h1>
    </div>
</div>


<form id="form" method="post">
    <fool:tagOpt optCode="userBase">
        <div class="form1">
            <h1 class="base">基本资料</h1>
            <div class="uploadPanel" style="text-align: center; width:110px;display: inline-block;margin-left:65px;vertical-align:top;" >
                <img alt="头像" src="${ctx}/userController/getHeadPortrait/${rs.fid}.jpg" style="width:70px;height:70px" id="pic" name="fileName"><br/>
                <fool:tagOpt optCode="userHead"><input type="button" class="setPic btn-blue4 btn-s btn" value="更改" style="margin-top:5px" name="uploadPick"></fool:tagOpt>
            </div>
            <div style="display: inline-block;">
                <p><font style="width:80px">姓名：</font><input id="userName" type="text" name="userName" value='${rs.userName}' class="easyui-validatebox textBox" data-options="required:true,validType:['normalChar','length[0,50]'],missingMessage:'该项不能为空',novalidate:true"/></p><br/>
                <p><font style="width:80px">性别：</font><input id="sex" name="sex" value='${rs.sex}'/><!-- <select id="select-sex"  class="easyui-combobox" style="width:167px;height:30px;"  data-options="editable:false,required:true,missingMessage:'该项不能为空',novalidate:true,panelHeight:'auto'">
                                         <option>男</option>
                                         <option>女</option>
                                        </select> --></p><br/>
                <p><font style="width:80px">输入法：</font><input id="inputType" name="inputType" value='${rs.inputType}'/><!-- <select id="input_method"  class="easyui-combobox" style="width:167px;height:30px;" data-options="editable:false">
                                         <option>拼音</option>
                                         <option>五笔</option>
                                        </select> --></p><br />
                <p><font style="width:80px">仓储缓存：</font><input id="localCache" name="localCache" value='${rs.localCache}' data-options=""/></p>
            </div>

            <br/>

            <p><font>地址：</font><textarea id="address" name="faddress" class="easyui-validatebox textArea" data-options="validType:['normalChar','length[6,100]'],missingMessage:'该项不能为空',novalidate:true" style="width:465px !important;">${rs.faddress}</textarea></p>
        </div>
    </fool:tagOpt>
    <fool:tagOpt optCode="userContact">
        <div class="form1">
            <h1 class="tontact">联系方式</h1>
            <p><font>手机：</font><input type="text" id="phone" name="phoneOne" value='${rs.phoneOne}'class="easyui-validatebox textBox" data-options="required:true,validType:['number','length[11,11]'],invalidMessage:'手机号码格式不正确',missingMessage:'该项不能为空',novalidate:true"/></p><br/>
            <p><font>邮箱：</font><input type="text" id="email" name="email" value='${rs.email}' class="easyui-validatebox textBox" data-options="required:true,validType:'email',missingMessage:'该项不能为空',novalidate:true"/></p>
        </div>
    </fool:tagOpt>
    <fool:tagOpt optCode="userPassword">
        <div class="form1">
            <h1 class="upPsw">密码修改</h1>
            <p><font>原始密码：</font><input id="oldPsw" name="passWord" class="easyui-validatebox textBox" type="password" data-options="required:true,missingMessage:'该项不能为空',novalidate:true" /></p><br/>
            <p><font>新密码：</font><input id="newPsw" type="password" class="easyui-validatebox textBox" data-options="required:true,missingMessage:'该项不能为空',validType:['noChinese','normalChar','length[6,15]'],novalidate:true" /></p><br/>
            <p><font>确认密码：</font><input id="rePsw" name="rePsw" type="password" class="easyui-validatebox textBox" validType="equals['#newPsw']" invalidMessage="两次输入密码不匹配" data-options="required:true,missingMessage:'该项不能为空',novalidate:true"/></p><br/>
        </div>
    </fool:tagOpt>
    <fool:tagOpt optCode="userSave"><input type="button" id="save" value="保存" class="btn-blue4 btn-s btn" style="margin-left:500px"/>
        <input type="button" id="reset" value="重填" class="btn-gray btn-s btn"/></fool:tagOpt>
</form>

<script type="text/javascript" src="${ctx}/resources/js/lib/easyui/easyui-validate-extend.js?v=${js_v}"></script>
<script type="text/javascript">
    var mylocalCache = '${rs.localCache}';
    var billCodes=["base","qckc","cgdd","cgrk","cgth","cgxjd","cgsqd","cgfp","pdd","dcd","bsd","scll","sctl","cprk","cptk","xsdd","xsch","xsth","xsfp","xsbjd","scjhd","fysqd","cgfld","xsfld","skd","fkd","fyd","bom"];
    $(document).ready(function(e) {

        var sexCombo = $("#sex").fool("dhxCombo",{
            width:169,
            height:32,
            required:true,
            novalidate:true,
            editable:false,
            data:[{
                value:0,text:"女"
            },{
                value:1,text:"男"
            }],
            focusShow:true
        });
        var inputTypeCombo = $("#inputType").fool("dhxCombo",{
            width:169,
            height:32,
            required:true,
            missingMessage:'该项不能为空！',
            novalidate:true,
            editable:false,
            data:[{
                value:'FIVEPEN',text:"五笔"
            },{
                value:'PINYIN',text:"拼音"
            }],
            focusShow:true,
            onLoadSuccess:function(combo){
                '${rs.inputType}'==""?combo.setComboValue("PINYIN"):null;
            }
        });
        var localCacheCombo = $("#localCache").fool("dhxCombo",{
            width:169,
            height:32,
            data:[{value:0,text:'关闭'},{value:1,text:'开启'}],
            novalidate:true,
            editable:false,
            focusShow:true,
            onLoadSuccess:function(combo){
                mylocalCache==""?combo.setComboValue(0):null;
            }
        });
        /* if($("#sex").val()==1){
         $("#select-sex").combobox("select",'男');
         }else{
         $("#select-sex").combobox("select",'女');
         };

         if($("#inputType").val()=='FIVEPEN'){
         $("#input_method").combobox("select",'五笔');
         }else{
         $("#input_method").combobox("select",'拼音');
         }; */
        $('#userName').bind('blur', function(){$('#userName').validatebox('enableValidation').validatebox('validate');});
        $('#address').bind('blur', function(){$('#address').validatebox('enableValidation').validatebox('validate');});
        $('#phone').bind('blur', function(){$('#phone').validatebox('enableValidation').validatebox('validate');});
        $('#email').bind('blur', function(){$('#email').validatebox('enableValidation').validatebox('validate');});
        $(sexCombo.getInput()).bind('blur', function(){$(sexCombo.getInput()).validatebox('enableValidation').validatebox('validate');});
        $('#oldPsw').bind('blur', function(){
            if($('#oldPsw').val()!=null&&$('#oldPsw').val()!=""){
                $('#oldPsw').validatebox('enableValidation').validatebox('validate');
                $('#newPsw').validatebox('enableValidation').validatebox('validate');
                $('#rePsw').validatebox('enableValidation').validatebox('validate');
            }else{
                $('#oldPsw').validatebox('disableValidation');
                $('#newPsw').validatebox('disableValidation');
                $('#rePsw').validatebox('disableValidation');
            }
        });

        $("#save").click(function(e) {
            $('#userName').validatebox('enableValidation').validatebox('validate');
            $('#address').validatebox('enableValidation').validatebox('validate');
            $('#phone').validatebox('enableValidation').validatebox('validate');
            $('#email').validatebox('enableValidation').validatebox('validate');
            $(sexCombo.getInput()).validatebox('enableValidation').validatebox('validate');
            var localCache = localCacheCombo.getSelectedValue();
            if($('#form').form('validate')){
                if(mylocalCache != localCache && localCache == "0"){
                    $.fool.confirm({title:"操作提示",msg:"选择关闭缓存会刷新系统，所有页面将会关闭，是否继续保存？",fn:function(r){
                        if(!r){return false;}
                        $.post('${ctx}/userController/save',{localCache:localCache,inputType:inputTypeCombo.getSelectedValue(),userName:$('#userName').val(),sex:sexCombo.getSelectedValue(),faddress:$('#address').val(),phoneOne:$('#phone').val(),email:$('#email').val(),oldPsw:$('#oldPsw').val(),newPsw:$('#newPsw').val(),fid:"${rs.fid}"},function(data){
                            dataDispose(data);
                            if(data.returnCode=="1"){
                                $.fool.alert({msg:data.message});
                            }else {
                                $.fool.alert({time:1000,msg:'修改成功!',fn:function(){
                                    parent.location.reload();
                                    for(var i=0;i<billCodes.length;i++){
                                        if(localStorage[billCodes[i]]){
                                            localStorage.removeItem(billCodes[i]);
                                        }
                                    }
                                }});
                            };
                        });
                    }})
                }else{
                    $.post('${ctx}/userController/save',{localCache:localCache,inputType:inputTypeCombo.getSelectedValue(),userName:$('#userName').val(),sex:sexCombo.getSelectedValue(),faddress:$('#address').val(),phoneOne:$('#phone').val(),email:$('#email').val(),oldPsw:$('#oldPsw').val(),newPsw:$('#newPsw').val(),fid:"${rs.fid}"},function(data){
                        dataDispose(data);
                        if(data.returnCode=="1"){
                            $.fool.alert({msg:data.message});
                        }else {
                            $.fool.alert({time:1000,msg:'修改成功!',fn:function(){
                                window.location.reload();
                            }});
                        };
                    });
                }
                return true;
            }else{
                return false;
            };
        });
        $("#reset").click(function(e) {
            $('#userName').val("${rs.userName}");
            $('#address').val("${rs.faddress}");
            $('#phone').val('${rs.phoneOne}');
            $('#email').val('${rs.email}');
            //$("#sex").val('${rs.sex}');
            sexCombo.setComboValue('${rs.sex}');
            //$("#inputType").val('${rs.inputType}');
            '${rs.inputType}' == ""?inputTypeCombo.setComboValue('PINYIN'):inputTypeCombo.setComboValue('${rs.inputType}');
            /* if('${rs.sex}'==1){
             $("#select-sex").combobox("select",'男');
             }else{
             $("#select-sex").combobox("select",'女');
             }; */
            /* if('${rs.inputType}'=='FIVEPEN'){
             $("#input_method").combobox("select",'五笔');
             }else{
             $("#input_method").combobox("select",'拼音');
             }; */
            //$("#localCache").combobox("setValue","${rs.localCache}");
            '${rs.localCache}' == ""?localCacheCombo.setComboValue(0):localCacheCombo.setComboValue('${rs.localCache}');
            $('#oldPsw').val(null);
            $('#newPsw').val(null);
            $('#rePsw').val(null);
            $('#oldPsw').validatebox('disableValidation');
            $('#newPsw').validatebox('disableValidation');
            $('#rePsw').validatebox('disableValidation');
        });
    });


    /* $("#select-sex").combobox({
     onSelect:function(){
     if($("#select-sex").combobox("textbox").val()=="男"){
     $("#sex").val(1);
     }else{$("#sex").val(2);}
     }
     });

     $("#input_method").combobox({
     onSelect:function(){
     if($("#input_method").combobox("textbox").val()=="五笔"){
     $("#inputType").val('FIVEPEN');
     }else{$("#inputType").val('PINYIN');}
     }
     }); */

    $.extend($.fn.validatebox.defaults.rules, {
        equals: {
            validator: function(value,param){
                return value == $(param[0]).val();
            },
            message: '两次输入的密码不一样'
        }
    });

    $.extend($.fn.validatebox.defaults.rules,{
        noChinese:{
            validator:function (value,param) {

                return /^[^\u4e00-\u9fa5]{0,}$/.test(value);

            },
            message:'密码不能包括汉字！'
        }
    });


    function ajaxUpload(btn, fileName, _data){
        new AjaxUpload(btn, {
            action: '${ctx}/userController/Upload',
            data:_data,
            name:"file",
            onSubmit:function(file,ext){
                btn.val("正在上传...");
                btn.attr('disabled',true);
            },
            onComplete:function(file,response){
                if(response=='0'){
                    $.fool.alert({
                        msg:"文件类型错误，请选择jpeg jpg png bmp格式的图片。",
                        fn:function(){
                            btn.val("更改");
                            btn.attr('disabled',false);
                        }
                    });
                }else{
                    $("#pic").attr("src","${ctx}/userController/getHeadPortrait/${rs.fid}.jpg");
                    window.parent.changePic();
                    btn.val("更改");
                    btn.attr('disabled',false);
                }
            }
        });
    }

    var data = {userId:"${rs.fid}"};
    var panels = $(".uploadPanel");
    for(var i=0; i<panels.length; i++){
        var fileName = panels.eq(i).children("input[name='fileName']");
        var uploadPick = panels.eq(i).children("input[name='uploadPick']");
        ajaxUpload(uploadPick, fileName, data);
    };
</script>
</body>
</html>