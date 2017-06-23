<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>单据打印模板</title>
</head>
<body>
<div id="warp-div">
    <table id="printer_table">
      <thead id="table-th">
        <tr id="header">
          <td id="th_subjectName"></td>
          <td id="th_resume"></td>
          <td id="th_0" class="amountS"></td>
          <td id="th_1" class="amountS"></td>
          <td id="th_2" class="amountS"></td>
          <td id="th_3" class="amountS"></td>
          <td id="th_4" class="amountS"></td>
          <td id="th_5" class="amountS"></td>
          <td id="th_6" class="amountS"></td>
          <td id="th_7" class="amountS"></td>
          <td id="th_8" class="amountS"></td>
          <td id="th_9" class="amountS"></td>
        </tr>
        <tr>
          <td></td>
          <td>
            &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;<span class="year"></span>&emsp;&emsp;&emsp;
            <span class="month"></span>&emsp;&emsp;&emsp;&emsp;
            <span class="day"></span>
          </td>
          <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
        </tr>
        <tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>
      </thead>
      <tbody id="table-tb"></tbody>
      <tfoot id="table-tfoot"></tfoot>
    </table>
    
    <style>
        #table-th tr{
          height:0.72cm;
          margin: 0;
          padding: 0
        }
        #table-th tr#header{
          height:1.5cm;
          padding:0;
          margin:0;
        }
        #th_subjectName{
          width:4.1cm
        }
        #th_resume{
          width:6.5cm
        }
        .amountS{
          width:0.3cm
        }
        #table-tb tr{
          height:0.72cm;
          margin: 0;
          padding: 0
        }
        #table-tb td{
          font-size:14px;
        }
        /* td{
          border: 1px solid black;
          border-collapse: collapse;
        } */
    </style>
    <script type="text/javascript">
        var vals = ${empty data?"''":data};
        var nullRow = 0;
        var spliced="";
        if(vals.length<6){
        	nullRow=6-vals.length;
        	for(var i=0;i<nullRow;i++){
        		spliced=vals.splice(-1,1,{subjectName:"",resume:"",debit:"",credit:""})
        		vals.push(spliced[0]);
        	}
        }
        var voucherDate=getDateStr(vals[0]["voucherDate"]);
        $(".year").text(voucherDate.slice(0,4));
        $(".month").text(voucherDate.slice(5,7));
        $(".day").text(voucherDate.slice(8,10));
        if("undefined" == typeof texts||vals.length==0){
            $.fool.alert({time:1000,msg:'必要参数不存在'});
        }

        var _dt = vals;
        if(_dt){
            var _str = "";
            var _amount="";
            for(var i=0;i<_dt.length;i++){
                _str += "<tr>";
                for(var j=0;j<thead.length;j++){
                	if(j+1==thead.length) {
                		if(thead[j].key == "subjectName" ){
                			_str += "<td style='text-align:center'>"+tranKey(_dt[i],thead[j].key)+"</td>";
                		}else if(thead[j].key == "resume" ){ 
                			if(tranKey(_dt[i],thead[j].key)=="合计"){
                				_str += "<td style='text-align:center'> </td>";
                			}else{
                				_str += "<td style='text-align:center'>"+tranKey(_dt[i],thead[j].key)+"</td>";
                			}
                		}else if (thead[j].key == "amount" && _dt[i] != undefined) {
                			if( _dt[i]["debit"] > 0){
                				_amount=tranKey(_dt[i], "debit").toString().split(".");
                    			if(_amount.length==2){
                    				if(_amount[1].length<2){
                    					_amount[1]=_amount[1]+"0";
                    				}
                    				_amount=_amount.join("");
                    			}else{
                    				_amount=_amount.join("")+"00";
                    			}
                    			for(var k=10;k>0;k--){
                    				if(k>_amount.length){
                    					_str += "<td style='text-align:center;' class='last-th'> </td>";
                    				}else{
                    					for(var l=0;l<_amount.length;l++){
                            				_str += "<td style='text-align:center;' class='last-th'>"+_amount[l]+"</td>";
                            			}
                    					break;
                    				}
                    			}
                			}else if(_dt[i]["credit"] > 0){
                        		_amount=tranKey(_dt[i], "credit").toString().split(".");
                            	if(_amount.length==2){
                    				if(_amount[1].length<2){
                    					_amount[1]=_amount[1]+"0";
                    				}
                    				_amount=_amount.join("");
                    			}else{
                    				_amount=_amount.join("")+"00";
                    			}
                    			for(var k=10;k>0;k--){
                    				if(k>_amount.length){
                    					_str += "<td style='text-align:center;' class='last-th'> </td>";
                    				}else{
                    					for(var l=0;l<_amount.length;l++){
                            				_str += "<td style='text-align:center;' class='last-th'>"+_amount[l]+"</td>";
                            			}
                    					break;
                    				}
                    			}
                        	}else if( _dt[i]["debit"] == ""){
                				for(var k=10;k>0;k--){
                					_str += "<td style='text-align:center;' class='last-th'> </td>";
                    			}
                			}
                			
                			/* _str += "<td style='text-align:center;' class='last-th'>"+tranKey(_dt[i], "debit")+"</td>"; */
                        } else {
                            _str += "<td style='text-align:center;' class='last-th'>"+tranKey(_dt[i],thead[j].key)+"</td>";
                        }
                	} else {
                		if(thead[j].key == "resume"&& tranKey(_dt[i],thead[j].key)=="合计"){ 
                			_str += "<td style='text-align:center'> </td>";
                		}else{
                			_str += "<td style='text-align:center;'>"+tranKey(_dt[i],thead[j].key)+"</td>";
                		}
                        /* _str += "<td style='text-align:center;'>"+tranKey(_dt[i],thead[j].key)+"</td>"; */
                    }
                }
                _str += "</tr>";
            }
            console.log(_str);
            $("#table-tb").html(_str);
        }
        
        function tranKey(obj,key){
            var _keys = null,_vals='';
            if("undefined" != typeof obj&&"undefined" != typeof key){
                if(key.indexOf(",")!=-1)_keys = key.split(",");
                if(_keys!=null){
                    for(var n in _keys)
                        _vals += obj[_keys[n]];
                }else
                    _vals = obj[key];
                return _vals;
            }
            return "<div></div>";

        }
    </script>
</div>
</body>
</html>