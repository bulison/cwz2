<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>数据分析界面</title>
    <style>
    </style>
</head>

<body>
<table id="result"></table>
<div id="pageaction" > </div>
<div id="detailBox"></div>
<script type="text/javascript">
    var gridCol="";
    var headCol="";
    var resultObj="";

    $(document).ready(function(){
        //新增、修改、详情窗口初始化
        $('#detailBox').window({
            top:10,
            collapsible:false,
            minimizable:false,
            maximizable:false,
            resizable:false,
            closed:true,
            modal:true,
            width:$(window).width()-10,
            height:$(window).height()-60,
        });
    });
    if($("#labels")){
        $("#labels").css("width",$("#filterGrid").width()+80);
    }
    var code=($("#resultType").next())[0].comboObj.getSelectedValue();
    resultObj=eval(${result});
    if(resultObj.data&&resultObj.data.message=="试算不平衡"){
    	$("#result").before('<p id="labels" style="border: 1px solid #95B8E7;color: red;">'+resultObj.data.message+'</p>');
    }else if(resultObj.data&&resultObj.data.message=="试算平衡"){
    	$("#result").before('<p id="labels" style="border: 1px solid #95B8E7;">'+resultObj.data.message+'</p>');
    }
    if(code=="402881285ada557f015adb7cbeb10000"){
        headers=resultObj.other;
        resultObj.list = resultObj.rows;
    }
    getHeader(headers);
    for (var i=0 ; i< resultObj.list.length;i++){
            if(resultObj.list[i][9] == 0){
                resultObj.list[i][9] = null;
            }
        }
        var rowNum=10;
        var str=0;
        var end=0;
        if(resultObj.list.length>0&&(resultObj.list[resultObj.list.length-1][0]=="合计"||resultObj.list[resultObj.list.length-1][0]=="合计：")){
            rowNum=resultObj.list.length-1;
        }else{
            rowNum=resultObj.list.length;
        }
        if("${param.rowNum}"!=""){
            rowNum="${param.rowNum}";
        }
        for(var i = 0 ; i<gridCol.length ; i++){
            gridCol[i]['width']= 120 ;
        }
        $("#result").jqGrid({
            datatype:function(postdata){
                var list=resultObj.list;
                var result={};
                if(list.length>0&&(list[list.length-1][0]=="合计"||list[list.length-1][0]=="合计：")){
                    $("#result").jqGrid("footerData","set",list[list.length-1]);
                    list.pop();
                }
                result.rows=resultObj.list;
                if(code!="402881285ada557f015adb7cbeb10000"){
                    result.total=resultObj.pageBean.totalRows;
                    result.totalpages=resultObj.pageBean.totalPages;
                    result.page=resultObj.pageBean.currentPage;
                }else {
                    result.totalpages=Math.ceil(result.total/rowNum)
                }
                $("#result")[0].addJSONData(result);
            },
            footerrow: true,
            shrinkToFit:false,
            width:$("#filterGrid").width()+80,
            height:$(window).height()-$(".filterBox").height()-$(".titleCustom").height()-160,
            pager:"#pageaction",
            rowNum:rowNum,
            rowList:[10,20,30],
            viewrecords:true,
            jsonReader:{
                records:"total",
                total: "totalpages",
            },
            colModel:gridCol,
            gridComplete:function(){

                var rows=$("#result").jqGrid("getRowData");
                if(rows.length<=0){
                    return;
                }
                if(($("#resultType").next())[0].comboObj.getSelectedValue()=="402881b8508d96b201508d994a760000"||($("#resultType").next())[0].comboObj.getSelectedValue()=="402881e450894e270150898c3a120001"){
                    $("#result").jqGrid("showCol","printer");
                }
                for(var i=0;i<rows.length;i++){
                    if(rows[i].color){
                        $("#result").find("#"+(i+1)).find("td").css("color",rows[i].color);
                    }
                }
            },
            ondblClickRow:function(rowid,iRow,iCol,e){
                var rowData=$("#result").jqGrid("getRowData",rowid);
                if(($("#resultType").next())[0].comboObj.getSelectedValue()=="402881e5508d13c401508d1ea0380001"||($("#resultType").next())[0].comboObj.getSelectedValue()=="402881e45087ef24015088f789780026"){
                    if(rowData.URL&&rowData.URL!=""){
                        $('#detailBox').window('setTitle',"查看详情");
                        $('#detailBox').window('open');
                        $('#detailBox').window('refresh',"${ctx}"+rowData.URL);
                    }
                }/* else if(($("#resultType").next())[0].comboObj.getSelectedValue()=="402881e450894e270150898c3a120001"){
                 if(rowData.URL&&rowData.URL!=""){
                 $('#detailBox').window('setTitle',"销售明细分析");
                 $('#detailBox').window('open');
                 $('#detailBox').window('refresh',"${ctx}/report/detail?reportId=40288186548391e4015483ff7e4200ad");
                 }
                 } */
            },
            onPaging:function(pgButton){//first,last,prev,next
                var data = {};
                var conditions=getConditions();
                data.condition =JSON.stringify(conditions);
                data.sysReportId=($("#resultType").next())[0].comboObj.getSelectedValue();
                if(pgButton=="first"){
                    data.page=1;
                    data.rows=resultObj.pageBean.pageSize;
                }else if(pgButton=="last"){
                    data.page=resultObj.pageBean.totalPages;
                    data.rows=resultObj.pageBean.pageSize;
                }else if(pgButton=="prev"){
                    data.page=parseInt(resultObj.pageBean.currentPage)-1;
                    data.rows=resultObj.pageBean.pageSize;
                }else if(pgButton=="next"){
                    data.page=parseInt(resultObj.pageBean.currentPage)+1;
                    data.rows=resultObj.pageBean.pageSize;
                }else if(pgButton=="records"){
                    /*     		setTimeout(function(){
                     var rows=$("#result").getGridParam().postData.rows;
                     data.rows=rows;
                     $("#resultGrid").load("${ctx}/report/query?rowNum="+data.rows,data);
                     }, 0.1);
                     return;
                     */    	}
                setTimeout(function(){
                    var rows=$("#result").getGridParam().postData.rows;
                    data.rows=rows;
                    $("#resultGrid").load("${ctx}/report/query?rowNum="+data.rows,data);
                }, 0.1);
            }
        }).navGrid('#pageaction',{add:false,del:false,edit:false,search:false,view:false});
        $("#result").jqGrid('setGroupHeaders', {
            useColSpanStyle: true,
            groupHeaders:headCol
        });
        /* if(($("#resultType").next())[0].comboObj.getSelectedValue()=="40288186548391e4015483ff7e4200ad"||($("#resultType").next())[0].comboObj.getSelectedValue()=="40288186548391e4015483dedbc6000e"){
            $("#result").setGridParam({multiselect:true}); 
        } */

        function setPrinter(cellvalue, options, rowObject){
            return "<a class='printer btn-printer' onclick='printer("+options.rowId+")'></a>";
        }

        function printer(index){
            var row=$("#result").jqGrid("getRowData",index);
            var detail_1=row[1];
            var detail_2=row[2];
            var detail_3=row[3];
            var detail_4=row[4];
            var detail_5=row[5];
            var detail_6=row[6];
            var detail_7=row[7];
            var detail_8=row[8];
            var detail_9=row[9];
            var start=$("#FSTART_DATE").datebox("getValue");
            var end=$("#FEND_DATE").datebox("getValue");
            var details={"detail_1":detail_1,"detail_2":detail_2,"detail_3":detail_3,"detail_4":detail_4,"detail_5":detail_5,"detail_6":detail_6,"detail_7":detail_7,"detail_8":detail_8,"detail_9":detail_9,"start":start,"end":end};
            if(($("#resultType").next())[0].comboObj.getSelectedValue()=="402881b8508d96b201508d994a760000"){
                printReport(details,"payment","",160);
            }else if(($("#resultType").next())[0].comboObj.getSelectedValue()=="402881e450894e270150898c3a120001"){
                printReport(details,"receivable","",160);
            }
        };
        function getHeader(header){
            var cols=[];
            var hcols=[];
            var number=0;
            var childs=[];
            var parents=[];

            if(code=="402881285ada557f015adb7cbeb10000"){
                for(var prop in header){
                    cols.push({name:prop,label:header[prop],align:'center'});
                }
            }
            else{
                var headers=header.split(",");
                for(var i=0;i<headers.length;i++){
                    if(headers[i].indexOf("[")==-1){
                        cols.push({name:number,label:headers[i],align:'center'});
                        number++;
                    }else{
                        childs=headers[i].slice(headers[i].indexOf("[")+1,headers[i].indexOf("]")).split(";");
                        parents=headers[i].slice(0,headers[i].indexOf("["));
                        hcols.push({startColumnName:number,numberOfColumns:childs.length,titleText:parents});
                        for(var j=0;j<childs.length;j++){
                            cols.push({name:number,label:childs[j],align:'center'});
                            number++;
                        }
                    }
                    childs=[];
                    parents=[];
                }
                cols.push({name:"isChecked",label:"是否勾对",align:'center',hidden:true});
                cols.push({name:"color",label:"颜色",align:'center',hidden:true});
                cols.push({name:"URL",label:"路径",align:'center',hidden:true});
                cols.push({name:"printer",label:"打印",align:'center',hidden:true,formatter:setPrinter});
                if(code=="40288186548391e4015483ff7e4200ad"||code=="40288186548391e4015483dedbc6000e"){
                	cols[0]={name:"",label:"",align:'center',formatter:function(value,option,row){
                		if(row[14]==0){
                			return "<input class='rowChecker' type='checkbox' onClick='checkRow(this,\""+row[16]+"\",\""+row[4]+"\")'>";
                		}else if(row[14]==1){
                			return "<input class='rowChecker' type='checkbox' checked='checked' onClick='checkRow(this,\""+row[16]+"\",\""+row[4]+"\")'>";
                		}else{
                			return "";
                		}
                	}};
                } 
            }
            gridCol=cols;
            headCol=hcols;
        }

//获取jqGrid编辑框
        function getEditor(gridId,rowId,name){
            var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
            return editor;
        }

function checkRow(target,fid,billType){
	console.log($(target).prop("checked"));
	console.log(fid);
	console.log(billType);
	var url="";
	var flag="";
	if($(target).prop("checked")==true){
		flag=1;
	}else if($(target).prop("checked")==false){
		flag=0;
	}
	if(billType=="采购入库"||billType=="采购退货"||billType=="采购发票"||billType=="期初应付"||billType=="销售出货"||billType=="销售退货"||billType=="销售发票"){
		url="${ctx}/billCheck/wareHouseBillCheck?id="+fid+"&check="+flag;
	}else if(billType=="付款单"||billType=="付款勾对"||billType=="采购返利"||billType=="收款单"||billType=="收款勾兑"||billType=="销售返利"||billType=="优惠金额"){
		url="${ctx}/billCheck/paymentBillCheck?id="+fid+"&check="+flag;
	}else if(billType=="费用单"){
		url="${ctx}/billCheck/costBillCheck?id="+fid+"&check="+flag;
	}
	$.ajax({
		type : 'post',
		url : url,
		data : {},
		dataType : 'json',
		success : function(data) {
			if(data.returnCode != '0'){
				$.fool.alert({msg:data.message});
				if($(target).prop("checked")==true){
					$(target).prop("checked",false);
				}else if($(target).prop("checked")==false){
					$(target).prop("checked",true);
				}
			}
		},
		error:function(){
			$.fool.alert({msg:"系统正忙，请稍后再试。"});
		}
	});
}
</script>
</body>
</html>
