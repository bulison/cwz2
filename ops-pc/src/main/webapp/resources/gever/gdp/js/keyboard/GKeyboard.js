(function(){
	function GKeyboard(){this.process(); }
	GKeyboard.prototype.process = function(){
		JUtil.ext( this , JComponent );
		var o = this;
		o.key = [
			[0,1,2,3,4,5,6,7,8,9],
			["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"],
			["%u9000%u683C","%u5927%u5C0F%u5199","%u5173%u95ED","%u786E%u8BA4"]//退格,大小写,关闭,确认
		];
		o.isOpen = false;//键盘是否打开
		o.isCaps = false;//大小写
		o.mouseOnKeyboard = false;
		o.KEYEVENT_MAP = new Array(0);
		o.createUI();
	}
	//
	GKeyboard.prototype.createUI = function(){
		var o = this;
		var tab = JUtil.create( "table" , {border:0,cellSpacing:1,cellPadding:2} , {fontSize:"12px"} );
		var tr,td;
		tr = tab.insertRow(0);
		var size = o.key[2].length;
		for( var i=0;i<size;i++ ){
			td = tr.insertCell(i);
			JUtil.setAttribute( td , {className:"keyboard_key_out",noWrap:true, innerHTML:"&nbsp;"+unescape(o.key[2][i])+"&nbsp;"} );
			(function(a){ JEvent.addListener( a , "mouseover" , function(){ JUtil.setAttribute( a , {className:"keyboard_key_over"} ); } ); })(td);
			(function(a){ JEvent.addListener( a , "mouseout" , function(){ JUtil.setAttribute( a , {className:"keyboard_key_out"} ); } ); })(td);
			(function(a){ JEvent.addListener( a , "mousedown" , function(){ JUtil.setAttribute( a , {className:"keyboard_key_click"} ); } ); })(td);
			(function(a){ JEvent.addListener( a , "mouseup" , function(){ JUtil.setAttribute( a , {className:"keyboard_key_over"} ); } ); })(td);
			(function(a,b){ JEvent.addListener( a , "click" , function(){
				if( "%u5173%u95ED%u786E%u8BA4".indexOf(b)>-1 ){//如果是确认或是关闭
					o.close();o.setIconVisible(false);
				}else if( b=="%u5927%u5C0F%u5199" ){//大小写
					o.setCaps();
				}else{//退格
					o.inputObj.focus();
					o.inputObj.value = o.inputObj.value.substring( 0 , o.inputObj.value.length-1 );
				}
			} ); })(td,o.key[2][i]);
			
		}
		JEvent.addListener( o.container , "mouseover" , function(){o.mouseOnKeyboard=true;} );
		JEvent.addListener( o.container , "mouseout" , function(){o.mouseOnKeyboard=false;} );
		o.container.appendChild(tab);
		JUtil.setAttribute( o.container, {className:"GKeyboard_container",align:"center"} );
		JUtil.setStyle( o.container, {position:"absolute"} );
		o.setBackground("#dddddd");
		o.setVisible(false);
	}
	//
	GKeyboard.prototype.attach = function(a){
		var o = this;
		o.inputObj = !JPattern.isEmpty(a)? JUtil.get(a) : a;
		o.icon = JUtil.create("label",{innerHTML:"&nbsp;"},{visibility:"hidden"});
		var img = JUtil.create("img",{src:basePath+"/gever/gdp/js/keyboard/imgs/GKeyboard.png"},{cursor:"pointer"});
		JEvent.addListener( img , "click" , function(){if(o.isOpen){ o.close(); }else{ o.open(); }o.inputObj.focus();} );
		JEvent.addListener( img , "mouseover" , function(){o.mouseOnKeyboard=true;} );
		JEvent.addListener( img , "mouseout" , function(){o.mouseOnKeyboard=false;} );
		o.icon.appendChild( img );
		o.inputObj.parentNode.insertBefore( o.icon , o.inputObj.nextSibling );

		JEvent.addListener( o.inputObj , "focus" , function(){ o.setIconVisible(true); } );
		JEvent.addListener( o.inputObj , "blur" , function(){ if( !o.isMouseOnKeyboard() ){o.setIconVisible(false);o.close();}} );

	}
	//
	GKeyboard.prototype.random = function(){//运算随机数
		var o = this;
		var keyNum = new Array(0),keyWord = new Array(0);
		var size = o.key[0].length;
		for( var i=0;i<size;i++ ){//复制数组
			keyNum.push( o.key[0][i] );
		}
		size = o.key[1].length;
		for(var i=0;i<size;i++){
			keyWord.push( o.key[1][i] );
		}
		var randomKeyNum = new Array(0),randomKeyWord = new Array(0);
		var r;
		while( keyNum.length>0 ){
			r = Math.round(Math.random()*(keyNum.length-1));
			randomKeyNum.push(keyNum.splice(r,1));
		}
		while( keyWord.length>0 ){
			r = Math.round(Math.random()*(keyWord.length-1));
			randomKeyWord.push(keyWord.splice(r,1));
		}
		return [randomKeyNum,randomKeyWord];
	}
	//
	GKeyboard.prototype.createKey = function(){
		var o = this;
		while( o.container.childNodes.length>1 ){
			o.container.removeChild( o.container.firstChild );
		}
		while( o.KEYEVENT_MAP.length>0 ){
			JEvent.removeListener(o.KEYEVENT_MAP.pop());
		}
		var key = o.random();
		var keyNum = key[0], keyWord = key[1];
		var len = keyNum.length , size = Math.ceil((len+keyWord.length)/len);//运算总共要建多少行
		var tab = JUtil.create("table",{border:0,cellSpacing:1,cellPadding:2});
		var tr,td,arr,index;
		for(var i=0;i<size;i++){
			tr = tab.insertRow(i);
			for( var j=0;j<len;j++ ){
				td = tr.insertCell(j);
				arr = (i>0)? keyWord : keyNum;
				index = (i>0)? len*(i-1)+j : len*i+j;
				if( !arr[index] ){break;}
				JUtil.setAttribute( td , {className:"keyboard_key_out", innerHTML:"&nbsp;"+arr[index]+"&nbsp;"} );
				JUtil.setStyle( td , {width:"18px",fontSize:"12px"} );
				(function(a){ o.KEYEVENT_MAP.push(JEvent.addListener( a , "mouseover" ,function(){JUtil.setAttribute(a,{className:"keyboard_key_over"});} )); })(td);
				(function(a){ o.KEYEVENT_MAP.push(JEvent.addListener( a , "mouseout" ,function(){JUtil.setAttribute(a,{className:"keyboard_key_out"});} )); })(td);
				(function(a){ o.KEYEVENT_MAP.push(JEvent.addListener( a , "mousedown" ,function(){JUtil.setAttribute(a,{className:"keyboard_key_click"});} )); })(td);
				(function(a){ o.KEYEVENT_MAP.push(JEvent.addListener( a , "mouseup" ,function(){JUtil.setAttribute(a,{className:"keyboard_key_over"});} )); })(td);
				(function(a){ o.KEYEVENT_MAP.push(JEvent.addListener( a , "click" ,function(){
					o.inputObj.focus();
					o.inputObj.value += a.innerHTML.replace(/&nbsp;/gi,"");
				} )); })(td);
			}
		}
		o.container.insertBefore( tab , o.container.firstChild );
		o.isCaps = false;
		o.tab = tab;
	}
	
	//
	GKeyboard.prototype.open = function(){
		var o = this;
		o.createKey();
		if( !JPattern.isInHtmlDom(o.container) ){
			document.body.appendChild( o.container );
		}
		var pos = JUtil.getPosition( o.inputObj );
		pos[0] += o.inputObj.offsetWidth + 1;
		pos[1] += o.inputObj.offsetHeight+1;
		o.setPosition( pos[0],pos[1] );
		o.isOpen = true;
		o.setVisible(o.isOpen);
	}
	//
	GKeyboard.prototype.close = function(){
		this.isOpen = false;
		this.mouseOnKeyboard = false;
		this.setVisible(false);
	}
	//
	GKeyboard.prototype.setIconVisible = function(a){//设置键盘图标显示或隐藏
		if( this.icon ){
			JUtil.setStyle( this.icon, {visibility:a? "visible":"hidden"} );
		}
	}
	//
	GKeyboard.prototype.setCaps = function(){//大小写切换
		
		var o = this;
		var ch,tr,td;
		var len = o.key[0].length , size = Math.ceil((len+o.key[1].length)/len);//运算总共要建多少行
		for( var i=1;i<size;i++ ){
			tr = o.tab.rows.item(i);
			for(var j=0;j<len;j++){
				td = tr.cells.item(j);
				ch = td.innerHTML;
				if( JPattern.isEmpty(ch) ){break;}
				ch = ch.replace( /&nbsp;/gi, "" ).charCodeAt(0);
				ch = ( o.isCaps )? ch+32 : ch-32;
				td.innerHTML = "&nbsp;"+String.fromCharCode(ch)+"&nbsp;";
			}
		}
		o.isCaps = !o.isCaps;
	}
	//判断鼠标是否在软键盘上
	GKeyboard.prototype.isMouseOnKeyboard = function(){
		return this.mouseOnKeyboard;
	}
	window.GKeyboard = GKeyboard;
})();