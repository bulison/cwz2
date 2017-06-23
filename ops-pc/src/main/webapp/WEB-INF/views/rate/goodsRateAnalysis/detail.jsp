<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>货品利润分析明细</title>
<style>
</style>
</head>
<body>
    <p></p>
	<table id="details"></table>
	<div id="_pager"></div>
<script>
    $('#details').jqGrid({
        datatype:function(postdata){
        	postdata.startDate=$("#startDate").datebox("getValue");
        	postdata.endDate=$("#endDate").datebox("getValue");
        	postdata.goodsId="${param.goodsId}";
        	postdata.specId="${param.specId}";
            $.ajax({
                url:getRootPath()+"/api/rate/goodsProfitAnalysisDetail",
                data:postdata,
                dataType:"json",
                complete: function(data,stat){
                    if(stat=="success") {
                        data.responseJSON.totalpages=Math.ceil(data.responseJSON.total/postdata.rows);
                        $("#details")[0].addJSONData(data.responseJSON);
                    }
                }
            });
        },
        gridComplete: completeMethod,
		footerrow:true,
        autowidth:true,
        height:$(window).outerHeight()-200+"px",
        pager:"#_pager",
        rowNum:10,
        rowList:[10,20,30],
        viewrecords:true,
        jsonReader:{
            records:"total",
            total: "totalpages",
        },
        colModel:[
            {name:"billDate",label:'日期',width:4,align:'center',formatter:function(value){
            	if(value){
            		return value.slice(0,10);
            	}else{
            		return "";
            	}
            }},
            {name:"billType",label:'单据类型',width:5,align:'center'},
            {name:"billCode",label:'单号',width:5,align:'center'},
            {name:"customerName",label:'客户名称',width:5,align:'center'},
            {name:"accountQuentity",label:'销售数量',width:5,align:'center',formatter:function(value){
    			if(value){
    				return value.toFixed(2);
    			}else{
    				return "0";
    			}
    		}},
            {name:"accountAmount",label:'销售金额',width:5,align:'center',formatter:function(value){
    			if(value){
    				return value.toFixed(2);
    			}else{
    				return "0";
    			}
    		}},
            {name:"costAmount",label:'成本金额',width:5,align:'center',formatter:function(value){
    			if(value){
    				return value.toFixed(2);
    			}else{
    				return "0";
    			}
    		}},
            {name:"profit",label:'利润',width:5,align:'center',formatter:function(value){
    			if(value){
    				return value.toFixed(2);
    			}else{
    				return "0";
    			}
    		}},
            {name:"profitRate",label:'利润率(%)',width:5,align:'center',formatter:function(value){
    			if(value){
    				return value.toFixed(2);
    			}else{
    				return "0";
    			}
    		}},
        ],
    }).navGrid('#_pager',{add:false,del:false,edit:false,search:false,view:false});

    function completeMethod() {
        var sumAccountQuentity = $("#details").getCol("accountQuentity",false,'sum');
        var sumAccountAmount = $("#details").getCol("accountAmount",false,'sum');
        var sumCostAmount =  $("#details").getCol("costAmount",false,"sum");
        var sumProfit = $("#details").getCol("profit",false,"sum");
        $("#details").footerData('set',{
            'billCode':'合计',
            accountQuentity:sumAccountQuentity,
            accountAmount:sumAccountAmount,
            costAmount:sumCostAmount,
            profit:sumProfit,
        });
    };


</script>
</body>
</html>
