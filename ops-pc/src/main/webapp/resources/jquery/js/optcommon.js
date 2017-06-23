function hiddenOpt(){
	var jsonStr=$("#hiddenOpt").val();
	var jsonObj=eval("("+jsonStr+")");
	//alert(jsonObj.length);
	//alert(jsonObj[0].code);
	var aObjs=document.getElementsByTagName("a");			
	for(var i=0;i<aObjs.length;i++){
		var id=aObjs[i].id;
		for(var j=0;j<jsonObj.length;j++){
			var code=jsonObj[j].code;
			if(id==code){
				aObjs[i].style.display="none";
				break;
			}
		}						
	}		
	}