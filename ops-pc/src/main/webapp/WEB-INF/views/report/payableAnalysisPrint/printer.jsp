<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>应付分析打印模板</title>
</head>
<body>
<div id="warp-div">
    <table  cellspacing="0%">
      <thead>
        <tr>
          <td colspan="8" align="center"><h1>对账单</h1></td>
        </tr>
        <tr>
          <td colspan="8" id="csvName"></td>
        </tr>
        <tr>
          <td colspan="8"><p>&emsp;&emsp;&emsp;下列数据出自本公司账簿记录，如与贵公司相符，请在本函下端"数据证明无误"处签章证明；若有不符，请在"数据不符"处列明不符金额，回函请传真或直接寄至我公司。</p></td>
        </tr>
        <tr>
          <td colspan="8"><p>&emsp;&emsp;&emsp;公司名称：${org.orgName}</p></td>
        </tr>
        <tr>
          <td colspan="8"><p>&emsp;&emsp;&emsp;通信地址：${org.faddress}</p></td>
        </tr>  
        <tr>
          <td colspan="8"><p>&emsp;&emsp;&emsp;联系人：${org.principal}</p></td>
        </tr>      
        <tr>
          <td colspan="8"><p>&emsp;&emsp;&emsp;电话（传真）：${org.phoneOne}</p></td>
        </tr>
        <tr>
          <td colspan="8" style="height: 30px;vertical-align: bottom;"><p>1、本公司与贵公司的往来账项列示如下：</p></td>
        </tr>
        <tr id="table-th">
          <td align="center">上期欠付</td>
          <td align="center">本期采购</td>
          <td align="center">本期退货</td>
          <td align="center">本期已付</td>
          <td align="center">优惠金额</td>
          <td align="center">费用金额</td>
          <td align="center">回扣金额</td>
          <td align="center" class="last">本期结余未付</td>
        </tr>  
      </thead>
      <tbody>
        <tr id="table-tb">
          <td id="detail1"></td>
          <td id="detail2"></td>
          <td id="detail3"></td>
          <td id="detail4"></td>
          <td id="detail5"></td>
          <td id="detail6"></td>
          <td id="detail7"></td>
          <td id="detail8" class="last"></td>
        </tr>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="8" style="height: 50px;vertical-align: bottom;"><p>2、其他事项：本函仅为复核帐目之用，并非催款结算。若款项在上述日期之后已经付清，仍请及时函复为盼。</p></td>
        </tr>
        <tr>
          <td colspan="4" style="border: 1px solid #000000;">
            <p>1、信息证明无误 </p>
            <p style="margin-top: 100px">经办人： </p>
            <p>日期： </p>
          </td>
          <td colspan="4" style="border: 1px solid #000000;">
            <p>2、信息不符，请列明不符金额 </p>
            <p style="margin-top: 100px">经办人： </p>
            <p>日期： </p>
          </td>
        </tr>
        <tr>
          <td colspan="4">
            <p>（公司签章）：   年    月    日</p>
            <p>经办人： </p>
          </td>
          <td colspan="4">
            <p>（公司签章）：   年    月    日</p>
            <p>经办人： </p>
          </td>
        </tr>
      </tfoot>
    </table>
<style>
#warp-div{
	position: relative;
}
#table-th td{
	border: 1px solid #000000;
	border-right: none;
	margin:0;
	padding:0;
}
#table-tb td{
	border: 1px solid #000000;
	border-right: none;
	border-top: none;
	margin:0;
	padding:0;	
}
.last{
    border-right:1px solid #000000 !important;
}
</style>

<script type="text/javascript">
  var orgName="${param.orgName}";
  var details=${param.details};
  $("#csvName").html("<p>致："+decodeURI(orgName)+"公司</p>");
  $("#detail1").text(details.detail_2);
  $("#detail2").text(details.detail_3);
  $("#detail3").text(details.detail_4);
  $("#detail4").text(details.detail_5);
  $("#detail5").text(details.detail_6);
  $("#detail6").text(details.detail_7);
  $("#detail7").text(details.detail_8);
  $("#detail8").text(details.detail_9);
</script>
</div>
</body>
</html>