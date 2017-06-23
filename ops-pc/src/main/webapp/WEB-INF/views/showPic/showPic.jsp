<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>查看图片</title>
</head>
<body>
  <%-- <%@ include file="/WEB-INF/views/common/header.jsp"%>
  <%@ include file="/WEB-INF/views/common/js.jsp"%> --%>
  <style>
    #picBoxs{
      width:100%;
      height: 100%;
	}
	#picContainer{
	  width: 100%;
	  height: 100%;
	  position: relative;
    }
    .picItem{
      width: 100%;
	  height: 100%;
	  position: absolute;
  	  display: none;
	}
	#prevImg{
	  position: absolute;
	  left:0;
	  top:50%;
	  color: yellow;
	  z-index: 99;
	  background: url("${ctx}/resources/images/picBack_icon.png") 0 0 no-repeat;
	  padding:20px;
	}
	#nextImg{
	  position: absolute;
	  right:0;
	  top:50%;
	  color: yellow;
	  z-index: 99;
	  background: url("${ctx}/resources/images/picNext_icon.png") 0 0 no-repeat;
	  padding:20px;
	}
	#closeImg{
	  position: absolute;
	  right:0;
	  top:0;
	  color: yellow;
	  z-index: 99;
	  background: url("${ctx}/resources/images/picDelete_icon.png") 0 0 no-repeat;
	  padding:15px;
	}
  </style>
  <div id="picBoxs">
      <a id="closeImg" href="javascript:void(0)"></a>
      <a id="prevImg" href="javascript:void(0)"></a>
	      <div id="picContainer"></div>
	  <a id="nextImg" href="javascript:void(0)"></a>
  </div>
  <script type="text/javascript">
  console.log("${param.url}");
  console.log("${param.busId}");
    loadImgs("${param.url}","${param.busId}");
	$("#prevImg").click(function(){
		if($($(".isShow")[0]).prev().length>0){
			var isShow=$(".isShow");
			$(isShow[0]).removeClass("isShow");
			$(isShow[0]).prev().addClass("isShow");
			$(isShow[0]).hide("slow");
			$(isShow[0]).prev().show("slow");
		}
	});
	$("#nextImg").click(function(){
		if($($(".isShow")[0]).next().length>0){
			var isShow=$(".isShow");
			$(isShow[0]).removeClass("isShow");
			$(isShow[0]).next().addClass("isShow");
			$(isShow[0]).hide("slow");
			$(isShow[0]).next().show("slow");
		}
	});
	$("#closeImg").click(function(){
		$("#picWin").window("close");
	});
	function loadImgs(url,busId){
		$.ajax({
			url:url,
			async:false,
			data:{busId:busId},
			success:function(data){
				if(data.length<1){
					$.fool.alert({msg:"暂无数据。",fn:function(){
						$("#picWin").window("close");
					}});
				}
				for(var i=0;i<data.length;i++){
					$("#picContainer").append("<img src='data:image/"+data[i].imgType+";base64,"+data[i].base64+"' class='picItem' />")
				}
				$($("#picContainer").children()[0]).addClass("isShow");
				$($("#picContainer").children()[0]).show();
			}
		});
	}
		</script>
	</body>
</html>
