/**
 * 余额调整
 */
function initManage(){
	init('',false);
	project();
	$("#search-btn").click(function(){
		var subjectId = ($('#projectName').next())[0].comboObj.getSelectedValue();
		init(subjectId,true);
	});
	$("#clear-btn").click(function(){
		//($('#projectName').next())[0].comboObj.setComboValue("");
		($('#projectName').next())[0].comboObj.setComboText("");
	});
}

//初始化
function init(subjectId,isShow){
	if(subjectId != ''){
		$.post(getRootPath()+'/balanceAdjust/query',{subjectId:subjectId}, function(sourceData) {
			if(sourceData != null){
				gridTable(jQuery.parseJSON(sourceData),isShow);
			}
		});
	}else{
		$.post(getRootPath()+'/balanceAdjust/query', function(sourceData) {
			if(sourceData != null){
				gridTable(jQuery.parseJSON(sourceData),isShow);
			}
		});
	}
}
function project(){
	$('#projectName').fool('dhxComboGrid',{
		prompt:"请输入或者选择",
		width:200,
		height:34,
		focusShow:true,
		data:getComboData(getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&direction=1&q="),
		filterUrl:getRootPath()+"/fiscalSubject/getSubject?leafFlag=1&bankSubject=1&direction=1",
		searchKey:"q",
		setTemplate:{
			input:"#name#",
			columns:[
						{option:'#code#',header:'编号',width:100},
						{option:'#name#',header:'名称',width:100},
					],
		},
		toolsBar:{
			refresh:true
		}
    });
	$('#projectName').next().find(".dhxcombo_select_button").click(function(){
		($('#projectName').next())[0].comboObj.closeAll();
		mysubjectName();
	});
}
function mysubjectName(){
	  chooserWindow=$.fool.window({width:780,height:480,'title':"选择科目",href:getRootPath()+'/fiscalSubject/window?okCallBack=selectSubject&onDblClick=selectSubject&singleSelect=true&bankSubject=1&direction=1&flag=1',
		  onLoad:function(){($('#projectName').next())[0].comboObj.closeAll();}});//解决IE点击弹窗下拉框不消失问题
}
function selectSubject(rowData){
	var data = '';
	if(typeof rowData[0]!='undefined'){
		data = rowData[0];
	}else{
		data = rowData;
	}
    if(data.name == "科目"){
  	  $.fool.alert({msg:"请选择科目子节点！"});
  	  return false;
    }
    ($('#projectName').next())[0].comboObj.setComboValue(data.fid);
    /*($('#projectName').next())[0].comboObj.setComboText(data.name);*/
	  /*$("#projectName").combobox("setValue",data.fid);
	  $("#projectName").combobox("setText",data.name);*/
	  chooserWindow.window('close');
}
//table
function gridTable(sourceData,isShow){
	var mianData = new Array();
	var _columns = [{name:'columnName1',label:'项目',resizable:false,sortable:false,align:'center',width:'10px'},{name:'money1',label:'金额',resizable:false,align:'center',sortable:false,width:'10px'},{name:'columnName2',label:'项目',resizable:false,sortable:false,align:'center',width:'10px'},{name:'money2',label:'金额',resizable:false,align:'center',width:'10px'}];

	var leftFirstInfos = sourceData.leftFirst.infos;
    var leftSecondInfos = sourceData.leftSecond.infos;
    var rightFirstInfos = sourceData.rightFirst.infos;
    var rightSecondInfos = sourceData.rightSecond.infos;

    var topleftsize = leftFirstInfos.length;
	var botleftsize= leftSecondInfos.length;
	var toprightsize = rightFirstInfos.length;
	var botrightsize = rightSecondInfos.length;

	var topdiffer = topleftsize-toprightsize;
	var botdiffer = botleftsize-botrightsize;

	mianData.push({"columnName1":"银行日记账余额","money1":sourceData.leftFirst.journalAmount,"columnName2":"银行对账单余额","money2":sourceData.rightFirst.statementAmount});
	mianData.push({"columnName1":"加：银行已收企业未收","money1":sourceData.leftFirst.enterpriseUnReceiveAmount,"columnName2":"加：企业已收银行未收","money2":sourceData.rightFirst.bankUnReceiveAmount});
	if(topdiffer==0&&topleftsize!=0){
		for(var i=0;i<topleftsize;i++){
            if(leftFirstInfos[i].amount==0&&leftFirstInfos[i].amount==rightFirstInfos[i].amount)
                continue;
            mianData.push({"columnName1":leftFirstInfos[i].date,"money1":leftFirstInfos[i].amount,"columnName2":rightFirstInfos[i].date,"money2":rightFirstInfos[i].amount});
		}
	}else if(topdiffer>0){//左侧数据多
        for(var i=0;i<toprightsize;i++){
            if(leftFirstInfos[i].amount==0&&leftFirstInfos[i].amount==rightFirstInfos[i].amount)
                continue;
            mianData.push({"columnName1":leftFirstInfos[i].date,"money1":leftFirstInfos[i].amount,"columnName2":rightFirstInfos[i].date,"money2":rightFirstInfos[i].amount});
        }
        for(var i=0;i<topdiffer;i++){
            if(leftFirstInfos[i].amount!=0)
            	mianData.push({"columnName1":leftFirstInfos[i].date,"money1":leftFirstInfos[i].amount,"columnName2":"","money2":"0.0"});
        }
	}else if(topdiffer<0){//右侧数据多
        for(var i=0;i<topleftsize;i++){
            if(leftFirstInfos[i].amount==0&&leftFirstInfos[i].amount==rightFirstInfos[i].amount)
                continue;
            mianData.push({"columnName1":leftFirstInfos[i].date,"money1":leftFirstInfos[i].amount,"columnName2":rightFirstInfos[i].date,"money2":rightFirstInfos[i].amount});
        }
        for(var i=0;i<-topdiffer;i++){
            if(rightFirstInfos[i].amount!=0)
            	mianData.push({"columnName1":"","money1":"0.0","columnName2":rightFirstInfos[i].data,"money2":rightFirstInfos[i].amount});
        }
	}
	mianData.push({"columnName1":"减：银行已付企业未付","money1":sourceData.leftSecond.enterpriseUnPayAmount,"columnName2":"减：企业已付银行未付","money2":sourceData.rightSecond.bankUnPayAmount});
    if(botdiffer==0&&botleftsize!=0){
        for(var i=0;i<botleftsize;i++){
            if(leftSecondInfos[i].amount==0&&leftSecondInfos[i].amount==rightSecondInfos[i].amount)
                continue;
            mianData.push({"columnName1":leftSecondInfos[i].date,"money1":leftSecondInfos[i].amount,"columnName2":rightSecondInfos[i].date,"money2":rightSecondInfos[i].amount});
        }
    }else if(botdiffer>0){//左侧数据多
        for(var i=0;i<botrightsize;i++){
            if(leftSecondInfos[i].amount==0&&leftSecondInfos[i].amount==rightSecondInfos[i].amount)
                continue;
            mianData.push({"columnName1":leftSecondInfos[i].date,"money1":leftSecondInfos[i].amount,"columnName2":rightSecondInfos[i].date,"money2":rightSecondInfos[i].amount});
        }
        for(var i=0;i<botdiffer;i++){
            if(leftSecondInfos[i].amount!=0)
                mianData.push({"columnName1":leftSecondInfos[i].date,"money1":leftSecondInfos[i].amount,"columnName2":"","money2":"0.0"});
        }
    }else if(botdiffer<0){//右侧数据多
        for(var i=0;i<botleftsize;i++){
            if(leftSecondInfos[i].amount==0&&leftSecondInfos[i].amount==rightSecondInfos[i].amount)
                continue;
            mianData.push({"columnName1":leftSecondInfos[i].date,"money1":leftSecondInfos[i].amount,"columnName2":rightSecondInfos[i].date,"money2":rightSecondInfos[i].amount});
        }
        for(var i=0;i<-botdiffer;i++){
            if(rightSecondInfos[i].amount!=0)
                mianData.push({"columnName1":"","money1":"0.0","columnName2":rightSecondInfos[i].date,"money2":rightSecondInfos[i].amount});
        }
    }
	mianData.push({"columnName1":"调节后余额（企业）","money1":sourceData.adjustedOfEnterprise,"columnName2":"调节后余额（银行）","money2":sourceData.adjustedOfBank});
	$("#dataTable").jqGrid({
		datatype:"local",
		autowidth:true,
		//width:$('#gridBox').width(),
		viewrecords:true,
		jsonReader:{
			records:"total",
			total: "totalpages",  
		},  
		colModel:_columns,
	});
    $('#dataTable').jqGrid("setGridWidth",$("#gridBox").width()*0.98);
    $('#dataTable').jqGrid("setGridHeight",418);
	$("#dataTable")[0].addJSONData(mianData);
	if(isShow){
		if(sourceData.adjustedOfEnterprise==sourceData.adjustedOfBank){
			$.fool.alert({msg:'银行日记账与银行对账单平衡'});
		}else{
			$.fool.alert({msg:'银行日记账与银行对账单不平衡'});
		}
	}
}

function getComboData(url,mark){
	var result="";
	$.ajax({
		url:url,
		async:false,
		success:function(data){
			if(data){
				result=data;
			}
		}
	});
	if(mark!="tree"){
		return formatData(result,"fid");
	}else{
		return formatTree(result[0].children,"text","id");
	}
}