(function($){	
	//staticaaa
	$.fool={
		methods:{
			/**
			 * 按部门区分的人员树下拉框
			 * @param valTarget 选择值赋予的元素(jQ对象)
			 * @returns 
			 */
			memberDeptComBoxTree:function(setting){
				var $t =$.fool._get$(this);
				var opts = $.extend(setting,{'asynUrl':getRootPath()+'/member/queryTreeByDept?deptId='});
				return $t.fool('_asynDeptComBoxTreeHelp',opts);
			},			
			/**
			 * 按部门区分的用户树下拉框
			 * @param valTarget 选择值赋予的元素(jQ对象)
			 * @returns 
			 */
			userDeptComBoxTree:function(setting){
				var $t =$.fool._get$(this);
				var opts = $.extend(setting,{'asynUrl':getRootPath()+'/userController/usersTreeByDep?orgId='});
				return $t.fool('_asynDeptComBoxTreeHelp',opts);
			},
			/**
			 * 部门下拉树
			 * @param valTarget 选择值赋予的元素(jQ对象)
			 */
			deptComBoxTree:function(setting){
				var $t =$.fool._get$(this);
				var _opt = {url:getRootPath()+'/orgController/getAllTree',isLeaf:true,openRoot:true};
				var opts = $.extend({},_opt,setting);		
				return $t.fool('combotree',opts);
			},
			/**
			 * 凭证字下拉树
			 * @param 
			 */
			voucherWordTree:function(setting){
				var $t =$.fool._get$(this);
				var _opt = {url:getRootPath()+'/basedata/voucherWord',openRoot:true,isLeaf:true,editable:false,required:true,novalidate:true,width:160,height:30,missingMessage:'该项不能为空！',
					onShowPanel:function(){
							$t.combotree('panel').find('.tree-title').eq(0).html('请选择');
					}};
				var opts = $.extend({},_opt,setting);		
				return $t.fool('combotree',opts); 
			},
			/**
			 * 获取该对象所在行index
			 */
			getRowIndex:function(setting){
				var $t =$.fool._get$(this);
				var tr = $t.closest('tr.datagrid-row');
				return parseInt(tr.attr('datagrid-row-index'));
			},
			/**
			 * 根据ID field 获取行号
			 * {idField:''}
			 */
			getRowIndexByIdField:function(setting){
				var $t =$.fool._get$(this);
				return $t.datagrid('getRowIndex',setting.idField);
			},
			/**
			 * 根据ID field 与 编辑器字段名获取编辑器对象
			 * {idField:'',field:'',index:''}
			 * index与idField 二选一
			 */
			getEditorByField:function(setting){
				var $t =$.fool._get$(this);
				var _i = setting.index;
				if(_i == undefined){
					_i = $t.fool('getRowIndexByIdField',{'idField':setting.idField});
				}
				return $t.datagrid('getEditor', {index:_i,'field':setting.field});
			},
			/**
			 * 根据ID field 与 编辑器字段名获取编辑器JQ对象
			 * {idField:'',field:'',index:''}
			 * index与idField 二选一
			 */
			getEditor$:function(setting){
				var $t =$.fool._get$(this);
				var $e = $t.fool('getEditorByField',{idField:setting.idField,index:setting.index,field:setting.field});
				return $e.target;
			},
			/**
			 * 初始化搜索框样式
			 */
			initSearch:function(setting){
				var $t =$.fool._get$(this);
				var _opt = {
						'prompt':'请输入搜索内容',
						iconWidth:26,
						width:220,
						height:38
				};
				var opts = $.extend({},_opt,setting);
				return $t.searchbox(opts);
			},
			/**
			 * 表单验证,启动所有验证并返回验证结果
			 * 需要启动的元素，必须在表单内并设置有ID
			 */
			fromVali:function(setting){
				var $t =$.fool._get$(this);
				$t.find("[id]").each(function(i,n){
					$.fool._formValidHelp($(this));
				});
				return $t.form('validate');
			},
			/**
			 * 设置符合条件的元素，在失去焦点时候启动验证
			 * 需要启动的元素，必须在表单内并设置有ID
			 */
			fromBlurVali:function(setting){
				var $t =$.fool._get$(this);
				$t.find("[id]").each(function(i,n){
					//绑定时候，如果为combo则需要添加特殊事件，blur无效
					$(this).bind('blur',function(){
						$.fool._formValidHelp($(this));
					});
				});
			},
			/**
			 * 设置表单是否可编辑
			 * $(".obj").fool('fromEnable',{'enable':false});
			 */
			fromEnable:function(setting){
				var $t = $.fool._get$(this);
				var _opt = {'enable':true};
				var opts = $.extend({},_opt,setting);
				return fromEnable($t,opts.enable);
			},
			/**
			 * 验证文本框帮助方法
			 * 设置了一些样式
			 */
			validatebox:function(s){
				var $t =$.fool._get$(this);
				var $opt = $.fool._opts$($t);
				$opt = $.extend($opt,s,this.p);			
				return $t.addClass('textBox').css({width:$opt.width-5,height:$opt.height-3}).validatebox($opt);
			},
			/**
			 * 表单验证方法
			 * @param  onSubmit 重写了onSubmit(boolean)
			 */
			form:function(setting){
				var $t =$.fool._get$(this),_b=false;
				if(typeof setting != 'undefined')_b=true;								
				var _opt = {
					onSubmit:function(){
						var isValid = $t.form({novalidate:false}).form('validate');
						if(_b&&typeof setting.onSubmit == 'function'){
							var _bo= eval(setting.onSubmit(isValid));
							if(typeof _bo == 'boolean')						
								isValid = _bo;
						}
						return isValid;
					}
				};
				var opts = $.extend({},setting,_opt);
				return $t.form(opts).form({novalidate:true});
			},
			/**
			 * datebox帮助方法
			 */
			datebox:function(setting){
				var $t =$.fool._get$(this);
				var $opt = $.fool._opts$($t);
				//$.extend($opt,s,this.p,{width:(this.p.width-2)});
				var myArray = ['dateFormat'];
				var _$opt = {
						onShowPanel:function(){
							if(typeof setting.inputDate == 'boolean' && setting.inputDate == true){
								$t.datebox('textbox').keydown(dateboxInput);  
								$t.datebox('textbox').keyup(dateboxInput);  
								$t.datebox('textbox').keypress(dateboxInput);
								if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
									$t.datebox('textbox').bind('propertychange',function(){
										$t.datebox('panel').find('.calendar-nav-hover').length>0?$t.datebox('panel').find('.calendar-nav-hover').removeClass("calendar-nav-hover"):null;
									});//IE专用
								}else{
									$t.datebox('textbox').bind('input',function(){
										$t.datebox('panel').find('.calendar-nav-hover').length>0?$t.datebox('panel').find('.calendar-nav-hover').removeClass("calendar-nav-hover"):null;
									});
								}
				    		}
					    }
					};
				if("undefined" != typeof setting){
					myArray = myArray.concat(setting.validType);
				}
				$opt = $.extend($opt,this.p, setting,_$opt,{validType:myArray});
				function myfuc($t,$opt){
					$t.datebox($opt);
					if($t.length <=0){return false;}
					if(typeof setting.focusShow == 'boolean' && setting.focusShow == true){
						$t.datebox('textbox').focus(function(){
							$t.datebox('showPanel');
						});
					}
				}
				return myfuc($t,$opt);	
				//return $t.datebox($opt);
			},
			/**
			 * datetimebox帮助方法
			 */
			datetimebox:function(s){
				var $t =$.fool._get$(this);
				var $opt = $.fool._opts$($t);
				$.extend($opt,s,this.p,{width:(this.p.width-2)});
				return $t.datetimebox($opt);
			},
			/**
			 * combogrid 帮助方法
			 * 在要匹配的表格字段里面加入[searchKey:true],例：{field:'username',title:'名称',width:120,searchKey:true} 
			 */
			combogrid:function(setting){
				var $t =$.fool._get$(this);
				var myLoad = typeof setting.onLoadSuccess == 'function'?setting.onLoadSuccess():'';
				setting.onLoadSuccess = undefined;
				var _$opts = $.extend({
					delay:300,
					panelHeight:'auto',
					panelWidth:'auto',
					hasDownArrow:false,
					_$isInitQueryFun:false,
					keyHandler:{
						up: function(e){
							var grid=$t.combogrid("grid");
							var selected=grid.datagrid("getSelected");
							var index=grid.datagrid("getRowIndex",selected);
							var rows=grid.datagrid("getRows");
							if(!selected){
								grid.datagrid("selectRow",rows.length-1);
							}else{
								if(index-1<0){
									grid.datagrid("selectRow",rows.length-1);
								}else{
									grid.datagrid("selectRow",index-1);
								}
							}
							e.stopPropagation();
						},
						down: function(e){
							var grid=$t.combogrid("grid");
							var selected=grid.datagrid("getSelected");
							if(!selected){
								grid.datagrid("selectRow",0);
							}else{
								var index=grid.datagrid("getRowIndex",selected);
								var rows=grid.datagrid("getRows");
								if(index+1>rows.length-1){
									grid.datagrid("selectRow",0);
								}else{
									grid.datagrid("selectRow",index+1);
								}
							}
							e.stopPropagation();
						},
						enter: function(e){
							var grid=$t.combogrid("grid");
							var selected=grid.datagrid("getSelected");
							if(selected){
								var index=grid.datagrid("getRowIndex",selected);
								grid.datagrid("selectRow",index);
								$t.combogrid("hidePanel");
							}
							e.stopPropagation();
						},
					},
					onLoadSuccess:function(data){
			    		eval(myLoad);
				    	var _t = $(this);
				    	var _opts = _t.combogrid('options');
				    	if(typeof setting.focusShow == 'boolean' && setting.focusShow == true){
			    			_t.combogrid('textbox').focus(function(){
			    				_t.combogrid('showPanel');
			    			});
			    		}
				    	var _b = (_opts&&!_opts._$isInitQueryFun);
				    	if(_b){
				    		_opts.keyHandler.query=function(q,e){
				    			var _$t = $(this);
				    			var _$opts = _$t.combogrid('options');
				    			//全部删除输入的字符后，不进行搜索tyc
				    			/*if(!q){
				    				var _$p = _$t.combogrid('panel');
				    				_$p.parent().hide();
				    				return;
				    			}*/
				    			_$t.combo('setValue',q);
				    			var _$g = _$t.combogrid('grid');
				    			var _$params={};
				    			for(var i in _$opts.columns[0]){
				    				if(_$opts.columns[0][i].searchKey)_$params[_$opts.columns[0][i].field] = q;
				    			}
				    			_$g.datagrid('reload',_$params);
				    		};
				    		var _$g = _t.combogrid('grid');
			    			_t.combo('textbox').blur(function(e){
			    				var length = _$g.datagrid('getRows').length;
			    				var myidField = _opts.idField;
			    				var valueId = _t.combogrid('getValue');
			    				var selectId = "undefined" != typeof _$g.datagrid('getRows')[0]?_$g.datagrid('getRows')[0][myidField]:'';
			    				if(length == 1 && selectId!='' && selectId!=valueId){
			    					_$g.datagrid('selectRow',0);
			    				}
			    			});
				    		_opts._$isInitQueryFun = true;
				    		return;
				    	}
				    	
				    	var _p = _t.combogrid('panel');
				    	var _pandWidth='auto',_pandHeight='auto';
				    	//_p.parent().hide();
				    	if(!_opts){
				    		_pandHeight = _opts.panelHeight;
				    		_pandWidth = _opts.panelWidth;
				    	}
				    	if(data.total<=0){
				    		_p.parent().hide();
				    	}else if(_opts&&_opts._$isInitQueryFun){
				    		_p.parent().show();
				    		_p.panel('resize',{width:_pandWidth,height:_pandHeight});
				    	}
				    	
				    },				  
				},this.p, setting);
				
				return $t.combogrid(_$opts);
			},
			/**
			 * combobox 帮助方法
			 * @param valTarget 选择值赋予的元素(jQ对象)
			 * @param valueField 参考easyUI API
			 * @param textField 参考easyUI API
			 */
			combobox:function(setting){
				var $t =$.fool._get$(this);	
				//var _opt = {};
				var opts = $.extend({},this.p, setting);			
				return $t.combobox(opts);		
			},
			/**
			 * 科目搜索combobox 帮助方法
			 * @param valTarget 选择值赋予的元素(jQ对象)
			 * @param valueField 参考easyUI API
			 * @param textField 参考easyUI API
			 * @param focusShow -- true 默认false，聚焦显示下拉框
			 * @param searchHelp -- true 默认false,true的时候选中有子节点的节点时按右键会自动重新搜索父节点下的子节点
			 * @param url 不填的话默认是getRootPath()+"/fiscalSubject/getSubject?"，显示全部节点
			 */
			subjectCombobox:function(setting){
				var $t =$.fool._get$(this);	
				//var _opt = {};
				var url = getRootPath()+"/fiscalSubject/getSubject?";
				var panelHeight = 198;
				var valueField="id";
				if(typeof setting.url == 'string'){
					url = setting.url;
					if(url.search(/\?/) == -1){
						url+='?';
					}
					if(typeof setting.searchHelp == "boolean" && setting.searchHelp == true){
						url = getRootPath()+"/fiscalSubject/getSubject?";
					}
				}
				if(typeof setting.validType == 'string' || setting.validType instanceof Array){
					var myArray = ['subjValid'];
					myArray = myArray.concat(setting.validType);
				}
				if(typeof setting.panelHeight == 'number'){
					panelHeight = setting.panelHeight;
				}
				if(typeof setting.valueField == 'string'){
					valueField = setting.valueField;
				}
				var vkd = ''; 
				var myopts = {
						required:false,
						validType:myArray,
						valueField:valueField,
						textField:"text",
						panelHeight:panelHeight,
						url:url,
						mode:"remote",
						onLoadSuccess:function(){
		                	var editor=$(this);
		                	if(typeof setting.searchHelp == "boolean" && setting.searchHelp == true){
			                	var maxWidth = (editor.next().width() - editor.next().find('.textbox-addon-right').width())/2-2;
			                	var marginTop = (editor.combobox('textbox').outerHeight()-16-2)/2;
			                	if(editor.next().find('.subj').length==0){
			                		editor.next().append('<div class="subjn1 subj" style="max-width:'+maxWidth+'px;margin-top:'+marginTop+'px;display:none;"><div class="subj1"></div><a href="javascript:;" title="清除" style="position:absolute;margin-left:-15px;" onclick="clearBut(this)"><img src="/ops/resources/images/cancel.png"></a></div>');
			                	}
			                	editor.next().find('.subj').attr('mytips',0);
		                	}
		                	var items=editor.combobox("panel").find(".combobox-item");
		                	if(items[0]){
		                		$(items[0]).attr("class","combobox-item combobox-item-hover");
		                	}
		                	if(typeof setting.onLoadSuccess == 'function'){
								eval(setting.onLoadSuccess());
	                		}
		                },onSelect:function(record){
		                	var editor=$(this);
		                	if(typeof setting.searchHelp == "boolean" && setting.searchHelp == true){
			                	editor.combobox('enableValidation');
			                	editor.combobox('validate');
			                		vkd!=''?editor.combobox('textbox').unbind('keydown',vkd):null;
			                		if(record.level >= 1){
			                			editor.combobox('textbox').bind('keydown',vkd = function(e){
			                				if(record.flag == 0 && e.keyCode == 39){
			                        			editor.next().find('.subj1').html(record.text);
			                        			var width1 = (editor.next().outerWidth() - editor.next().find('.textbox-addon-right').outerWidth())/2-9;
			                        			var width2 = editor.next().find('.subj').outerWidth()-15;
			                        			editor.combobox('textbox').css({'width':width1,'float':'right'});
			                        			editor.next().find('.subj1').css({'width':width2,"overflow":"hidden","margin-right":"15px"});
			        	                		editor.next().find('.subjn1').show();
			        	                		editor.combobox('reload',url+'&q=&parentId='+record.fid);//传一次q为空的参数去服务器显示下级科目数据
			        	                		editor.combobox('reload',url+'parentId='+record.fid);
			        	                		editor.combobox('clear');
			        	                		editor.combobox('textbox').focus();
			                				}else if(editor.combobox('getText')=='' && e.keyCode == 8){
			                					editor.next().find('.subjn1 a').click();
			                					editor.combobox('textbox').unbind('keydown',vkd);
			                				}
			                			});
				                		editor.combobox('textbox').focus();
			                		}
			                		if(record.flag==1){
			                			editor.next().find('.subj').attr('mytips',0);
			                		}else if(record.flag==0){
			                			editor.next().find('.subj').attr('mytips',1);
			                		}
		                	}
		                	if(typeof setting.onSelect == 'function'){
								eval(setting.onSelect(record));
		                	}
		                }
		        };
				if(typeof setting.required == 'boolean'){
					myopts = $.extend(myopts,{required:setting.required});
				}
				var opts = $.extend({},this.p, setting,myopts);
				function myfuc($t,opts){
					$t.combobox(opts);
					if($t.length <=0){return false;}
					if(typeof setting.focusShow == 'boolean' && setting.focusShow == true){
						$t.combobox('reload',url+'&q=').combobox('reload',url);
						$t.combo('textbox').focus(function(){
							$t.combo('showPanel');
						});
					}
				}
				return myfuc($t,opts);		
			},
			/**
			 * combotree 帮助方法素
			 * @param valTarget 选择值赋予的元素(jQ对象)
			 * @param isLeaf 为true则只能选中叶子节点
			 * @param onBeforeSelect 在用户选择一个节点之前触发,如果isLeaf 设置为true 则该属性无效
			 * @param openRoot 为true时则默认打开根节点
			*/
			combotree:function(setting){			
				var $t =$.fool._get$(this);			
				var _opts = {
					onBeforeSelect:function(node){
						var _return = true;
						if(setting.isLeaf==true||setting.isLeaf=='true'){
							var t = $t.combotree('tree');
							if(t.tree('isLeaf',node.target)){
								_return = true;
							}else{
								_return = false;
							}
						}
						if(typeof setting.onBeforeSelect == 'function')
							eval(setting.onBeforeSelect(node));
						return _return;
					},
					onSelect:function(node){
						if(typeof setting.valTarget != 'undefined'){
							$.fool._get$(setting.valTarget).val(node.id);
						}
						if(typeof setting.onSelect == 'function')
							eval(setting.onSelect(node));
					}
					,onLoadSuccess:function(node,data){
						if(typeof setting.openRoot == 'boolean' && setting.openRoot==true)
							expandNode($t.combotree('tree'));
						if(typeof setting.onLoadSuccess == 'function')
							eval(setting.onLoadSuccess(node,data));
						
						if(data.length<=0)return;
						var _t = $t.combotree('tree');
						var _roots = _t.tree('getRoots');
						if(_roots.length==1){
							_t.tree('update', {
								target: _roots[0].target,
								text: '请选择',
								id:''
							});
						}else{
							_t.tree('insert', {
								before: _roots[0].target,
								data: [{id: '', text: '请选择'}]
							});

						}
					},onBeforeExpand:function(node,param){
						if(typeof setting.onBeforeExpand == 'function')
							eval(setting.onBeforeExpand(node,param));
					}
				};			
				var opts = $.extend({},$t.p,setting,_opts);		
				return $t.combotree(opts);	
			},
			tree:function(s){
				var $t=$.fool._get$(this);
				var $opt=$.extend({},this.p,$.fool._opts$($t),s,{
					onLoadSuccess:function(node,data){
						if(s.openRoot==true)
							expandNode($t);
						if(typeof s.onLoadSuccess == 'function')
							eval(s.onLoadSuccess(node,data));
					}
				});
				return $t.tree($opt);
			},
			window:function(s){
				var $t =$.fool._get$(this);
				var opts = $.extend({},{//默认参数
					top:100,
					width:$(window).width()-10,
					height:$(window).height()-60,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					resizable:false
				}, s);
				return $t.window(opts);
			},
			_asynDeptComBoxTreeHelp:function(setting){
				var $t =$.fool._get$(this);		
				var _opt={
					url:getRootPath()+'/orgController/getAllTree?asynLeaf=true',
					isLeaf:true,openRoot:true,
					onBeforeExpand:function(node,param){
						var t =$t.combotree('tree');					
						var loadSucFun = t.tree('options').onLoadSuccess;
						if(t.tree('getChildren',node.target).length==0){
							t.tree('options').onLoadSuccess = function(){};
							t.tree('options').url = setting.asynUrl+node.id;
						}else{
							t.tree('options').onLoadSuccess = loadSucFun;
						}
					}
				};
				var opts = $.extend({},setting,_opt);
				return $t.fool('combotree',opts);
			},
			/**
			 * dhxCombo 帮助方法
			 * 在API里面的初始化方法是需要div或者span这些标签的对象进行初始化的，使用input是不行的
			 * 但在此方法设置了如果使用input标签对象进行初始化也可以初始化成功
			 * @param width 输入框的宽度
			 * @param height 输入框的高度
			 * *以下验证是根据easyui的validatebox来验证的，参数是一样的*
			 * @param required 验证是否为空，默认false
			 * @param novalidate 是否开启验证，默认true
			 * @param validType 验证的方法，同easyui
			 * @param missingMessage 为空时候的信息提示
			 * ************
			 * @param onChange 当数据改变的时候触发的函数，参数是value，text
			 * @param onSelectChange 当选项改变的时候触发的函数，无参数
			 * @Param onOpen 当下拉框打开的时候触发的函数，无参数
			 * @param focusShow 为true的时候，聚焦就会出现下拉框
			 * @param hasDownArrow 是否显示下拉按钮
			 * @param prompt 输入框的提示字
			 * @param value 初始化加载完成后输入框的value值
			 * @param text 初始化加载完成后输入框显示的值
			 * @param editable 定义是否只可以选择不能输入编辑,默认是true,可编辑的
			 * @param clearOpt 定义是否设置首个选项为置空项，默认为是（true）
			 * @param setTemplate 定义下拉框和输入的显示格式，详情请看dhtmlxCombo的方法setTemplate
			 * @param data 载入的json数据，数据必须按照格式才能显示
			 * @param selectFirst 为true的时候聚焦就会自动选中第一个
			 * @param onLoadSuccess 控件加载完成后执行的函数,参数是dhxCombo的对象
			 * @param toolsBar 是否定义下拉框下方添加数据项的工具栏，必须是对象，其中属性包括name（需要添加数据的名称），addUrl（跳转页面的url）,refresh（是否有刷新按钮）
			 * **combo对象不是全局对象的时候如何获取该对象**
			 * 找出输入框的父元素span或者div对象，dhxcombo的对象就存放在此父元素对象的comboObj属性里面
			*/
			dhxCombo:function(setting){
				var $t = $.fool._get$(this);
				if(typeof $t.attr("id")=="undefined"){
					return false;
				}
				var name = $t.attr("name");
				var val="";
				if($t[0].tagName == "INPUT" && (!$t.attr("class") || $t.attr("class").indexOf("dhxCombo-f")==-1)){
					typeof name == "string"?$t.removeAttr("name"):null;
					val = $t.attr("value");
					if($('#dhxDiv_'+$t.attr("id")).length>0){
						$t.after("<div id='dhxDiv2_"+$t.attr("id")+"' class='dhxDiv'></div>");
					}else{
						$t.after("<div id='dhxDiv_"+$t.attr("id")+"' class='dhxDiv'></div>");
					}
					$t.addClass("dhxCombo-f");
					$t.hide();
				}
				if($t.attr("class")&&$t.attr("class").indexOf("validatebox-text") != -1){//如果本来的input已经加了验证的，就取消验证
					$t.validatebox({required:false,novalidate:true});
				}
				if($t.attr("class") && $t.attr("class").indexOf("dhxCombo-f")!=-1){
					$t = $t.next();
				}
				var _id = $t.attr("id");
				var width = 182;
				var height = 32;
				if(typeof setting != "undefined" && typeof setting.width != 'undefined')width=setting.width;
				if(typeof setting != "undefined" && typeof setting.height != 'undefined')height=setting.height;
				$t.width(width);
				if(typeof name != "string")name="myCombo";
				var $e = $t[0].comboObj == null?new dhtmlXCombo(_id,name):$t[0].comboObj;
				$t.height(height);
				$t.find(".dhxcombo_material").width($t.width()-2);
				$t.find(".dhxcombo_material").height($t.height()-2);
				$t.find(".dhxcombo_material").find("input").width($t.find(".dhxcombo_material").width()-$t.find(".dhxcombo_select_button").width()-4);
				$t.find(".dhxcombo_material").find("input").height($t.find(".dhxcombo_material").height());
				$t.find(".dhxcombo_input").css("line-height",$t.find(".dhxcombo_material").height()+"px");
				$t.find(".dhxcombo_select_button").css("height","100%");
				$t.css({"display":"inline-block","vertical-align":"middle"});//控制输入框适应同行布局
				$t.find(".dhxcombo_input").attr("id","input_"+_id);
				$t[0].comboObj = $e;//让combo的对象存放在父元素对象的comboObj属性里面
				if(typeof setting != "undefined"){
					var required=false,novalidate=true,validType="",missingMessage="该项不能为空！";//默认验证设置
					if(typeof setting.required == "boolean"){
						required=setting.required;
					}else if($t.prev().attr("_required")=="true"){ 
						required=true;  
						novalidate=false;  
					}
					if(typeof setting.novalidate == "boolean"){
						novalidate=setting.novalidate;
					}
					if(typeof setting.validType == "string" || $.isArray(setting.validType)){
						validType=setting.validType;
					}
					if(typeof setting.missingMessage == "string"){
						missingMessage=setting.missingMessage;
					}
					$($e.getInput()).validatebox({//用validatebox进行验证
						required:required,
						novalidate:novalidate,
						validType:validType,
						missingMessage:missingMessage
					});
					$e.attachEvent("onChange",  function(value,text){
						if(typeof setting.onChange == "function"){
							eval(setting.onChange(value,text));
						}
						if(setting.required){
							$.fool._formValidHelp($($e.getInput()));
						}
					});
					if(typeof setting.onSelectionChange == "function"){
						$e.attachEvent("onSelectionChange", setting.onSelectionChange);
					}
					$e.attachEvent("onOpen",function(){
						if(typeof setting.onOpen == "function"){
							eval(setting.onOpen());
						}
//						if(typeof setting.toolsBar == 'object' && $($e.getList()).find("div").eq(0).attr("class") == "mytoolsBar"){
//							//$($e.getList()).find(".mytoolsBar").css("margin-top",$($e.getList()).height()+'px');
//						}
						if(typeof setting.toolsBar == 'object'){
							$($e.getList()).find(".mytoolsBar").css("width",($($e.getList()).width()-10)+'px');
							if($($e.getList()).find("div").eq(0).attr("class") == "mytoolsBar"){
								$($e.getList()).find(".mytoolsBar").appendTo($($e.getList()));
							}
							var scroll = $(document).scrollTop();
							var a = $($e.getList()).find(".mytoolsBar");
							var myheight = a.parent().height();
							var max = a.parent().find(".dhxcombo_option").length*32;
							var mt = myheight - max -scroll;
							a.css("margin-top",mt);
							
						}
					});
					if(typeof setting.editable == "boolean" && setting.editable == false){
						$e.readonly(!setting.editable);
					}
					if(typeof setting.focusShow == "boolean" && setting.focusShow == true){
						$e.mykey = 0;
						$t.find('input.dhxcombo_input').focus(function(){
							if($e.mykey == 1){$e.mykey = 0;return;}
								$e._showList();
								$e.mykey = 0;
						});
					}
					if(typeof setting.hasDownArrow == 'boolean' && setting.hasDownArrow == false){
						$t.find(".dhxcombo_select_button").hide();
						$t.find(".dhxcombo_material").css("width",$t.width()-2+"px");
						$t.find(".dhxcombo_input").css("width",$t.width()-4+"px");
					}
					if(typeof setting.prompt == 'string')$e.setPlaceholder(setting.prompt);
					if(typeof setting.setTemplate == 'object')$e.setTemplate(setting.setTemplate);
					if(typeof setting.data == 'object'){
						var newData = $.map(setting.data, function(obj){
							return $.extend(true,{},obj);//返回对象的深拷贝
						});
						var clearOpt = true;
						if(typeof setting.clearOpt == "boolean" && setting.clearOpt == false){
							clearOpt = false;
						}
						if(newData.length>0 && typeof setting.editable == "boolean" && setting.editable == false && clearOpt){
							newData.unshift({text:"",value:""});
						}
						$e.load({options:newData});
					}
					if(typeof setting.selectFirst == 'boolean' && setting.selectFirst == true){
						$t.find(".dhxcombo_input").focus(function(){
							if($e.getComboText()=="" && typeof setting.focusShow == "boolean" && setting.focusShow == true){
								var row = $e.getOptionByIndex(1);
								if(row != null){
									$($e.getList().firstChild).addClass("dhxcombo_option_selected");
									$e.setComboValue(row.value);
									$e.openSelect();
								}
							}
						});
					}
					if(typeof setting.value == 'string' || typeof setting.value == 'number'){
						$e.setComboValue(setting.value);
					}
					if(typeof setting.text == 'string' || typeof setting.text == 'number'){
						$e.setComboText(setting.text);
					}
					if(typeof setting.onLoadSuccess == "function"){
						eval(setting.onLoadSuccess($e));
					}
				}
				if(typeof setting.toolsBar == 'object'){
					var opt = setting.toolsBar;
					if($($e.getList()).find(".mytoolsBar").length==0){
						$($e.getList()).append('<div class="mytoolsBar" style="width:'+($t.width()-12)+'px;"></div>');
						if(typeof opt.addUrl == "string" && typeof opt.name == "string"){
							$($e.getList()).find(".mytoolsBar").append('<a href="javascript:;" onclick=dhxAddUrl(\''+opt.addUrl+'\',\''+opt.name+'\'); title="添加'+opt.name+'" class="btn-add" style="padding-left: 20px;font-size: 14px;line-height: 20px;margin: 5px 0;"></a>');
						}
						if(typeof opt.refresh == "boolean" && opt.refresh == true){
							$($e.getList()).find(".mytoolsBar").append('<a href="javascript:;" class="btn-refresh" title="刷新数据" style="padding-left: 20px;font-size: 14px;line-height: 20px;margin: 5px 0;"></a>');
							//dhxCombo下拉框的工具栏，刷新数据功能按钮
							$($e.getList()).find(".mytoolsBar a.btn-refresh").click(function(){
								var url = dhxGetCode(opt.name);
								var _type;
								if(opt.name=='货运地址')
									_type='get';
								else
									_type='post';
								$.ajax({
							    	  url:url,
							    	  type:_type,
							    	  async:false,
							    	  success:function(data){
								    	  if(typeof data.rows != "undefined"||opt.name=="计划模板"||(data instanceof Array && !data[0].children)){
								    			if(typeof data.rows != "undefined"){
								    				data = data.rows;
								    			}
								    			if(data.length != 0&&data[0].id){
								    				var newData = formatData(data,"id");
								    				if(newData.length>0 && typeof setting.editable == "boolean" && setting.editable == false && clearOpt){
														newData.unshift({text:"",value:""});
													}
										    		  $e.load({options:newData});
//										    		  $e.setComboText("");
										    	  }else{
										    		  var newData = formatData(data,"fid");
										    		  if(newData.length>0 && typeof setting.editable == "boolean" && setting.editable == false && clearOpt){
										    			  newData.unshift({text:"",value:""});
										    		  }
										    		  $e.load({options:newData});
//										    		  $e.setComboText("");
										    	  }
								    	  }else if(opt.name=="在职状况"||opt.name=="学历"){
                                              var newData = formatData(data[0].children,"text","id");
                                              if(newData.length>0 && typeof setting.editable == "boolean" && setting.editable == false && clearOpt){
                                                  newData.unshift({text:"",value:""});
                                              }
                                              $e.load({options:newData});
//                                              $e.deleteOption("");
//                                              $e.setComboText("");
										  }else if(opt.name=='货运地址'){
                                              var newData = formatTree(data,"text","fid");
                                              $e.load({options:newData});
//                                              $e.deleteOption("");
                                          }else{
								    		  var newData = formatTree(data[0].children,"text","id");
							    				if(newData.length>0 && typeof setting.editable == "boolean" && setting.editable == false && clearOpt){
													newData.unshift({text:"",value:""});
												}
								    		  $e.load({options:newData});
//											  $e.deleteOption("");
//								    		  $e.setComboText("");
								    	  }
								    	  //$($e.getList()).find(".mytoolsBar").css("margin-top",$($e.getList()).height()+'px');
	//							    	  $($e.getList()).find(".mytoolsBar").appendTo($($e.getList()));
								    	  $e.closeAll();
								    	  $e.openSelect();
							      }});
							});
						}
					}
					
//						var dis = $($e.getList()).height() - 256;
//						if($($e.getList()).siblings("#time-line").length > 0){
//							dis-=10;
//						}
//						if(dis > 0){
//							$($e.getList()).find(".mytoolsBar").css("margin-top",-dis+'px');
//						}
				}
				setTimeout(function(){if(typeof val != "undefined" && val != ""){
					/*for(var i=0;i<newData.length;i++){
						if(newData[i].value!=val||newData[i].text!=val){
							
						}
					}; */
					$e.setComboText(val);
					$e.setComboValue(val);
				}},300);
				return $e;
			},
			/**
			 * dhxComboGrid 帮助方法
			 * 在API里面的初始化方法是需要div或者span这些标签的对象进行初始化的，使用input是不行的
			 * 但在此方法设置了如果使用input标签对象进行初始化也可以初始化成功
			 * @param width 输入框的宽度
			 * @param height 输入框的高度
			 * *以下验证是根据easyui的validatebox来验证的，参数是一样的*
			 * @param required 验证是否为空，默认false
			 * @param novalidate 是否开启验证，默认true
			 * @param validType 验证的方法，同easyui
			 * @param missingMessage 为空时候的信息提示
			 * ************
			 * @param onChange 当数据改变的时候触发的函数，参数是value，text
			 * @param onSelectChange 当选项改变的时候触发的函数，无参数
			 * @Param onOpen 当下拉框打开的时候触发的函数，无参数
			 * @param focusShow 为true的时候，聚焦就会出现下拉框
			 * @param onlySelect 为true的时候，搜索剩下1个选项时，失去焦点自动选择剩下的选项
			 * @param hasDownArrow 是否显示下拉按钮
			 * @param prompt 输入框的提示字
			 * @param value 初始化加载完成后输入框的value值
			 * @param text 初始化加载完成后输入框显示的值
			 * @param editable 定义是否只可以选择不能输入编辑,默认是true,可编辑的
			 * @param setTemplate 定义下拉框和输入的显示格式，详情请看dhtmlxCombo的方法setTemplate
			 * @param data 载入的json数据，数据必须按照格式才能显示
			 * @param method 自动搜索后台返回数据时对请求方法进行定义，默认post
			 * @param selectFirst 为true的时候聚焦就会自动选中第一个
			 * @param filterUrl 如果需要输入自动搜索后台返回数据，需要填写这个url
			 * @param searchKey 设置filterUrl后，必须设置searchKey,此属性定义了传递参数的字段名，默认是“searchKey”
			 * @param onLoadSuccess 控件加载完成后执行的函数,参数是dhxCombo的对象
			 * @param toolsBar 是否定义下拉框下方添加数据项的工具栏，必须是对象，其中属性包括name（需要添加数据的名称），addUrl（跳转页面的url）,refresh（是否有刷新按钮）
			 * ***如果需要消除事件，需要注意的是filterUrl的输入搜索事件是存放在dhxCombo对象的fevId属性里面***
			 * **combo对象不是全局对象的时候如何获取该对象**
			 * 找出输入框的父元素span或者div对象，dhxcombo的对象就存放在此父元素对象的comboObj属性里面
			*/
			dhxComboGrid:function(setting){
				var $t = $.fool._get$(this);
				if(typeof $t.attr("id")=="undefined"){
					return false;
				}
				var val="";
				var name = $t.attr("name");
				if($t[0].tagName == "INPUT" && (!$t.attr("class") || $t.attr("class").indexOf("dhxCombo-f")==-1)){
					typeof name == "string"?$t.removeAttr("name"):null;
					val = $t.attr("value");
					if($('#dhxDiv_'+$t.attr("id")).length>0){
						$t.after("<div id='dhxDiv2_"+$t.attr("id")+"' class='dhxDiv'></div>");
					}else{
						$t.after("<div id='dhxDiv_"+$t.attr("id")+"' class='dhxDiv'></div>");
					}
					$t.addClass("dhxCombo-f");
					$t.hide();
				}
				if($t.attr("class") && $t.attr("class").indexOf("validatebox-text") != -1){//如果本来的input已经加了验证的，就取消验证
					$t.validatebox({required:false,novalidate:true});
				}
				if($t.attr("class") && $t.attr("class").indexOf("dhxCombo-f")!=-1){
					$t = $t.next();
				}
				var _id = $t.attr("id");
				var width = 182;
				var height = 32;
				if(typeof setting != "undefined" && typeof setting.width != 'undefined')width=setting.width;
				if(typeof setting != "undefined" && typeof setting.height != 'undefined')height=setting.height;
				$t.width(width);
				if(typeof name != "string")name="myComboGrid";
				var $e = $t[0].comboObj == null?new dhtmlXCombo(_id,name):$t[0].comboObj;
				$t.height(height);
				$t.find(".dhxcombo_material").width($t.width()-2);
				$t.find(".dhxcombo_material").height($t.height()-2);
				$t.find(".dhxcombo_material").find("input").width($t.find(".dhxcombo_material").width()-$t.find(".dhxcombo_select_button").width()-4);
				$t.find(".dhxcombo_material").find("input").height($t.find(".dhxcombo_material").height());
				$t.find(".dhxcombo_input").css("line-height",$t.find(".dhxcombo_material").height()+"px");
				$t.find(".dhxcombo_select_button").css("height","100%");
				$t.css({"display":"inline-block","vertical-align":"middle"});//控制输入框适应同行布局
				$t[0].comboObj = $e;//让combo的对象存放在父元素对象的comboObj属性里面
				$t.find(".dhxcombo_input").addClass("div_grid");
				$t.find(".dhxcombo_input").attr("id","input_"+_id);
				if(typeof setting != "undefined"){
					var required=false,novalidate=true,validType="",missingMessage="该项不能为空！";//默认验证设置
					if(typeof setting.required == "boolean"){
						required=setting.required;
					}else if($t.prev().attr("_required")=="true"){  
						required=true;  
						novalidate=false;   
					}
					if(typeof setting.novalidate == "boolean"){
						novalidate=setting.novalidate;
					}
					if(typeof setting.validType == "string" || $.isArray(setting.validType)){
						validType=setting.validType;
					}
					if(typeof setting.missingMessage == "string"){
						missingMessage=setting.missingMessage;
					}
					$($e.getInput()).validatebox({//用validatebox进行验证
						required:required,
						novalidate:novalidate,
						validType:validType,
						missingMessage:missingMessage
					});
					if(typeof setting.onChange == "function"){
						$e.attachEvent("onChange", setting.onChange);
					}
					if(typeof setting.onSelectionChange == "function"){
						$e.attachEvent("onSelectionChange", setting.onSelectionChange);
					}
					$e.attachEvent("onOpen",function(){
						if(typeof setting.onOpen == "function"){
							eval(setting.onOpen());
						}
						if(typeof setting.toolsBar == 'object'){
							var w = $($e.getList()).width()-10;
							var scroll = $(document).scrollTop();
							var a = $($e.getList()).find(".mytoolsBar");
							var myheight = a.parent().height();
							var max = a.parent().find(".dhxcombo_option").length*33;
							var mt = myheight - max -scroll;
							a.css({"margin-top":mt,"width":w});
						}
					});
					if(typeof setting.editable == "boolean" && setting.editable == false){
						$e.readonly(!setting.editable);
					}
					if(typeof setting.focusShow == "boolean" && setting.focusShow == true){
						$e.mykey = 0;
						$t.find('input.dhxcombo_input').focus(function(){
							if($e.mykey == 1){$e.mykey = 0;return;}
								$e._showList();
								$e.mykey = 0;
						});
					}
					if(typeof setting.onlySelect == "boolean" && setting.onlySelect == true){
						$t.find('input.dhxcombo_input').blur(function(e){
								if($($e.getList()).find(".dhxcombo_option").length==1){
									setTimeout(function(){//防止输入框失去焦点后，下拉框依然存在的问题
										$e.selectOption(0);
										if(!$($e.getInput()).is(":focus")){
											$e.closeAll();
										}
									},100);
								}
						});
					}
					if(typeof setting.hasDownArrow == 'boolean' && setting.hasDownArrow == false){
						$t.find(".dhxcombo_select_button").hide();
						$t.find(".dhxcombo_material").css("width",$t.width()-2+"px");
						$t.find(".dhxcombo_input").css("width",$t.width()-4+"px");
					}
					if(typeof setting.prompt == 'string')$e.setPlaceholder(setting.prompt);
					if(typeof setting.setTemplate == 'object')$e.setTemplate(setting.setTemplate);
					if(typeof setting.data == 'object'){
						$e.load({options:setting.data});}
					if(typeof setting.filterUrl == 'string'){
						$e.enableFilteringMode(true,"dummy"); 
						$e.dekey = 0;
						$e.fevId = $e.attachEvent("onDynXLS", myComboFilter);
						function myComboFilter(text){
							$e.dekey = $e.dekey+1;
							var dekey = $e.dekey;
							var sdata = {searchKey:text};
							if(typeof setting.searchKey == "string"){
								sdata = {};
								sdata[setting.searchKey] = text;
							}
							setTimeout(function(){
								if($e.dekey != dekey){return;}
								$e.clearAll();
								var method = "post";
								if(typeof setting.method == 'string'){
									method = setting.method;
								}
							      $.ajax({
							    	  url:setting.filterUrl,
							    	  type:method,
							    	  data:sdata,
							    	  success:function(data){
								    	  if(typeof data.rows != "undefined"){
								    			data = data.rows;
								    	  }
								    	  if(setting.idField){
								    		  if(data.length != 0){
									    		  $e.addOption(formatData(data,setting.idField));
									    	  }
								    	  }else{
								    		  if(data.length != 0&&data[0].id){
									    		  $e.addOption(formatData(data,"id"));
									    	  }else{
									    		  $e.addOption(formatData(data,"fid"));
									    	  }
								    	  }

                                          if(data[0] && typeof data[0].children != "undefined" && setting.filterUrl.indexOf("/fiscalSubject/getSubject")==-1){//科目搜索除外
                                              var newData = formatTree(data[0].children,'text','id');
                                              $e.load({options:newData});
                                          }

								    	  if(typeof setting.toolsBar == 'object'){
								    		  	$($e.getList()).find(".mytoolsBar").appendTo($($e.getList()));
											}
								    	  $($e.getInput()).is(":focus")?$e._showList():null;
								    	  if(typeof setting.onLoadSuccess == "function"){
												eval(setting.onLoadSuccess($e));
								    	  }
							      }});
							},300);//给输入一个缓冲时间，减少搜索压力，防止列表加载重复数据
						}
					}
					if(typeof setting.selectFirst == 'boolean' && setting.selectFirst == true){
						$t.find(".dhxcombo_input").focus(function(){
							if($e.getComboText()=="" && typeof setting.focusShow == "boolean" && setting.focusShow == true){
								var row = $e.getOptionByIndex(0);
								if(row != null){
									$($e.getList().firstChild).addClass("dhxcombo_option_selected");
									$e.setComboValue(row.value);
									$e.openSelect();
								}
							}
						});
					}
					if(typeof setting.value == 'string' || typeof setting.value == 'number'){
						$e.setComboValue(setting.value);
					}
					if(typeof setting.text == 'string' || typeof setting.text == 'number'){
						$e.setComboText(setting.text);
					}
					if(typeof setting.onLoadSuccess == "function"){
						eval(setting.onLoadSuccess($e));
					}
				}
				if(typeof setting.toolsBar == 'object'){
					if($($e.getList()).find(".mytoolsBar").length > 0){
						$($e.getList()).find(".mytoolsBar").remove();
					}
					var opt = setting.toolsBar;
					var w = $($e.getList()).width()+17;
					if($($e.getList()).width() == 0){
						w = $($e.getList()).prev().width()+17;
					}
					if($($e.getList()).find(".mytoolsBar").length==0){
						$($e.getList()).append('<div class="mytoolsBar" style="width:'+w+'px;"></div>');
						if(typeof opt.addUrl == "string" && typeof opt.name == "string"){
							$($e.getList()).find(".mytoolsBar").append('<a href="javascript:;" onclick=dhxAddUrl(\''+opt.addUrl+'\',\''+opt.name+'\'); title="添加'+opt.name+'" class="btn-add" style="padding-left: 20px;font-size: 14px;line-height: 20px;margin: 5px 0;"></a>');
						}
						if(typeof opt.refresh == "boolean" && opt.refresh == true && typeof setting.filterUrl == 'string'){
							$($e.getList()).find(".mytoolsBar").append('<a href="javascript:;" class="btn-refresh" title="刷新数据" style="padding-left: 20px;font-size: 14px;line-height: 20px;margin: 5px 0;"></a>');
							//dhxComboGrid下拉框的工具栏，刷新数据功能按钮
							$($e.getList()).find(".mytoolsBar a.btn-refresh").click(function(){
								var sdata = {searchKey:""};
								if(typeof setting.searchKey == "string"){
									sdata = {};
									sdata[setting.searchKey] = "";
								}
								$e.clearAll();
								var method = "post";
								if(typeof setting.method == 'string'){
									method = setting.method;
								}
							      $.ajax({
							    	  url:setting.filterUrl,
							    	  type:method,
							    	  async:false,
							    	  data:sdata,
							    	  success:function(data){
								    	  if(typeof data.rows != "undefined"){
								    			data = data.rows;
								    		}
								    	  if(data.length != 0&&data[0].id){
								    		  $e.addOption(formatData(data,"id"));
								    	  }else{
								    		  $e.addOption(formatData(data,"fid"));
								    	  }
//								    	  $e.setComboText("");
                                          if(data[0] && typeof data[0].children != "undefined" && setting.filterUrl.indexOf("/fiscalSubject/getSubject")==-1){//科目搜索除外
                                              var newData = formatTree(data[0].children,"text","id");
                                              $e.load({options:newData});
                                          }

								    	  $($e.getInput()).is(":focus")?$e._showList():null;
								    	  if(typeof setting.onLoadSuccess == "function"){
												eval(setting.onLoadSuccess($e));
								    	  }
							      }});
							      $($e.getList()).find(".mytoolsBar").appendTo($($e.getList()));
							});
						}
					}
				}
				setTimeout(function(){if(typeof val != "undefined" && val != ""){
					$e.setComboText(val);
					$e.setComboValue(val);
				}},300);
				return $e;
			}
		},
		/**
		 * 弹出窗口帮助类
		 */
		window:function(s){
			var opts = $.extend({},{//默认参数
				width:950,
				height:480,
				collapsible:false,
				minimizable:false,
				maximizable:false,
				resizable:false,
				modal:true 
			}, s);
			
			var $obj = null;
			if(typeof s.targetObj=='undefined'){
				$obj = $(".div-dialog-div");
				if ($obj[0] == undefined)
					$obj = $("<div class='div-dialog-div'></div>").append("body");
			}else{
				$obj =$.fool._get$(s.targetObj);
			}
			return $obj.window(opts);
		},
		/**
		 * 提示框
		 * {title, msg, icon, fn, time}
		 * icon四种设置："error"、"info"、"question"、"warning"
		 */
		alert:function(s){
			if(typeof s.msg=='undefined')s.msg='';
			//if(typeof s.title=='undefined')s.title='&nbsp;';
			if(typeof s.icon=='undefined')s.icon='warning';
			var win = $.messager.alert(s.title,s.msg,s.icon,function(b){
				if(typeof s.fn=='function')eval(s.fn(b));
			});
			if(typeof s.time!='undefined'){
				//win.find('.l-btn').css('display','none');
				win.find('.messager-button').prepend('<p style="margin-bottom:7px;">'+(s.time/1000)+'秒后自动关闭</p>');
				var t = s.time/1000-1;
				var t1 =setInterval(function(){win.find('.messager-button p').html((t--)+'秒后自动关闭');},1000);
				setTimeout(function(){clearInterval(t1);win.find('.l-btn').click();},s.time+200);
			}
			if(typeof s.btnName!='undefined' && typeof s.btnAct!='undefined'){//增加一个按钮并添加按钮事件
				win.find('.l-btn').css('width','auto');
				win.find('.messager-button').prepend('<a href="javascript:void(0)" onclick="'+s.btnAct+'" class="l-btn l-btn-small" group="" id="" style="margin-left: 10px; width: auto;"><span class="l-btn-left"><span class="l-btn-text">'+s.btnName+'</span></span></a>');
				if(typeof s.yesbtnName == "string"){
					win.find('.messager-button a').eq(1).find(".l-btn-text").text(s.yesbtnName);
				}
			}
			
		},
		/**
		 * 确认框
		 * {title, msg, fn(b)}
		 * @param s
		 */
		confirm:function(s){
			if(typeof s.title == 'undefined')s.title='&nbsp;';
			if(typeof s.msg == 'undefined')s.msg='';
			var win = $.messager.confirm(s.title,s.msg,function(b){
				if(typeof s.fn=='function')eval(s.fn(b));
				mywin.unbind('keydown',mykd);
			});
			win.find('.l-btn').eq(0).blur();
			var mywin = win.closest('html');
			var mykd = '';
			mywin.keydown(mykd = function(e){
				if(e.keyCode == 27){//esc键点击取消
					win.find('.l-btn').eq(1).click();
				}else if(e.keyCode == 13){//回车键点击确认
					win.find('.l-btn').eq(0).click();
				}
			});
		},
		_formValidHelp:function($o){//操作对象
			var $_id = $("#"+$o.attr('id'));
			if(undefined==$_id)return;
			var clazz = $_id.attr("class");
			if(undefined==clazz)return;
			if(clazz.indexOf('validatebox-text')>0){
				$_id.validatebox('enableValidation').validatebox('validate');
			}else if(clazz.indexOf('combo-f')>0){
				$_id.combo('enableValidation').combo('validate');
			}else if(clazz.indexOf('easyui-datebox')>0){
				$_id.datebox('enableValidation').datebox('validate');
			}else if(clazz.indexOf('combotree-f')>0){
				$_id.combotree('enableValidation').combotree('validate');				
			}else if(clazz.indexOf('numberbox-f')>0){
				$_id.numberbox('enableValidation').numberbox('validate');				
			}
		},
		_opts$:function(obj){
			var $t =$.fool._get$(obj);
			var s=$.trim($t.attr("data-options"));
			if(s){				
				if(s.substring(0,1)!="{") s="{"+s+"}";
				return (new Function("return "+s))();
			}
			return {};			
		},
		get$:function(obj){
			return $.fool._get$(obj);
		},
		_get$:function(obj){
			if(typeof obj == 'undefined')$.error('Conversion jQuery object error');
			return (typeof obj == 'string' ? $(obj): obj);
		}		
	};
	$.fn.fool=function(m,p){
		this.p={//全局基础属性
			editable:true,
			required: true,
			novalidate:true,
			width:160,
			height:30,
			panelHeight:'auto',
			panelMaxHeight:300
		};				
		if($.fool.methods[m]){
			return $.fool.methods[m].apply(this,Array.prototype.slice.call(arguments,1));
		}else if(typeof m=='object' || !m){
			return $.fool.methods.init.apply(this,arguments);
		}else {
			$.error('Method ' + m + ' does not exist on jQuery.fool');
		}
	};	
})(jQuery);

(function($){
	
	
	//鼠标移到元素上交替显示隐藏效果
	$(".hover-show-list .show-btn").hover(function(){
			$(".hover-show-list").find(".show-list").hover(function(){$(this).show();},function(){$(this).hide();}).show();
		},function(){
			$(".hover-show-list").find(".show-list").hide();	
		}
	);
	
	//初始化导航条
	//initNavPath();
	
	//初始化日期文本框
	$(".ops-date").fool('datebox');
})(jQuery); 

//禁止后退键 作用于Firefox、Opera   
//document.onkeypress=doKey;   
//禁止后退键  作用于IE、Chrome   
document.onkeydown=doKey;

//处理键盘事件
function doKey(e) {
	var ev = e || window.event; //获取event对象   
	var obj = ev.target || ev.srcElement; //获取事件源   
	var t = obj.type || obj.getAttribute('type'); //获取事件源类型 
	var code = e.keyCode || e.which || 0;
	//处理backspa键
	/*if (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") {
		return false;
	}*/
       /* if (ev.keyCode == 13) {
            GetSearch();
            return false;
        }*/
	
	return unEnableRefresh(ev,obj,code,t);
}
//日期输入格式限制
function dateboxInput() {  
    this.value = this.value.replace(/\D/g, '').replace(/.{4}(?!$)/, '$&-').replace(/.{4}\-(?![0-1])/,'$&0').replace(/.{4}\-.{2}(?!$)/, '$&-').replace(/.{4}\-.{2}\-(?![0-3])/,'$&0').replace(/(.{4}\-.{2}\-.{2})(.*)/, '$1');
} 

//取消刷新按钮
function unEnableRefresh(ev,key,keyCode,keyType){
	//alert(key+","+keyCode+","+keyType);
	var readOnly = key.readOnly || key.getAttribute('readOnly');//是否只读
	if (((keyCode == 8) &&//BackSpace 
	    ((keyType != "text" && keyType != "textarea" && keyType != "password") || readOnly == true)) || 
	    ((ev.ctrlKey) && ((keyCode == 78) || (keyCode == 82)) ) //Ctrl+N,Ctrl+R 
	    || (keyCode == 116)) {//F5 
	        ev.keyCode = 0; 
	        ev.returnValue = false;
	        return false;
	}
	return true;
}

//暂时没用
function initNavPath(){
	var navHtml = "<ul><li class=\"index\"><a href=\""+getRootPath()+"/indexController/indexPage\">首页</a></li>";
	var arr = null;
	try{
		arr = window.parent.window.getNavPath();
	}catch(e){
		$(".nav-box").html(navHtml+"</ul>");
		return;
	}
	if(arr){
		var _curr = arr.length;
		for(var i in arr){
			if(_curr-1==i) navHtml+="<li><a href=\"#\" class=\"curr\">"+arr[i]+"</a></li>"; 
			else navHtml+="<li><a href=\"#\">"+arr[i]+"</a></li>";
		}
	}
	$(".nav-box").html(navHtml+"</ul>");
}

//清空指定区域文本内容
function cleanBoxInput(obj){
	var $t =$.fool._get$(obj);
	$t.form('clear');
}

/**
 * 设置表单是否可用
 * @param target
 * @param flag
 */
function fromEnable(target,flag){
	var $t = $.fool._get$(target);
	if(flag){
		$t.find(":input[type='text']").not(".not-operate").removeAttr("readonly").css("background-color","#ffffff");
		$t.find(":input[type='radio']").not(".not-operate").removeAttr("disabled");
		$t.find("textarea").removeAttr("readonly").css("background-color","#ffffff");
		$t.find(":input[type='button']").show();
		$t.find(":input[type='submit']").show();
		$t.find(":input[type='reset']").show();
		$t.find("a[class^='btn']").show();
		//$t.find(".easyui-combobox").combobox({disable:false,enable:true,readonly:false,hasDownArrow:true});
		$t.find(".easyui-datebox").datebox({disable:false,enable:true,readonly:false});
		//$t.find("p em").hide();
	}else{
		$t.find(":input[type='text']").not(".not-operate").attr("readonly", "readonly").css("background-color","#D1D9E0");
		$t.find(":input[type='radio']").not(".not-operate").attr("disabled",true);
		$t.find("textarea").attr("readonly", "readonly").css("background-color","#D1D9E0");
		$t.find(":input[type='button']").not("#scancel,#ssave").hide();
		$t.find(":input[type='submit']").hide();
		$t.find(":input[type='reset']").hide();
		$t.find("a[class^='btn']").hide();
		//$t.find(".easyui-combobox").combobox({disable:true,readonly:true,hasDownArrow:false});
		$t.find(".easyui-datebox").datebox({disable:true,enable:false,readonly:true});
		//$t.find("p em").hide();
		$t.find('.clearBill').hide();
	}
	
	return $t;
}

/**
 * 设置分页属性
 * @param jqTableObj jquery easyUI table 对象
 */
function setPager(jqTableObj){
	if(!jqTableObj)return;
	//jqTableObj.datagrid({scrollbarSize:0});
	var p = jqTableObj.datagrid('getPager');
	$(p).pagination({	
		layout:['first','prev','links','next','last'],
		displayMsg:'{total}条记录&nbsp;&nbsp;{currPage}/{pages}页'
	});
}

/**
 * 根据code 查询数据字典 dialog
 * @param id
 */
function openDictByCode(id , code) {
	$(id).click(
			function() {
				return regDialog(
						getRootPath()+'/systemDictionaryController/goSystemDictionaryList?code' + code , '数据字典列表', 500, 500);
			});
}



/**
 * 弹出层的公共方法
 * @param url     路径
 * @param title   标题
 * @param w  宽度	
 * @param h  高度
 * @returns {Boolean}
 */
function regDialog(url, title, w, h) {
	var $obj = $(".div-dialog-div");
	if ($obj[0] == undefined)
		$obj = $("<div class='div-dialog-div'></div>").append("body");
	$obj.dialog({
		title : title,
		width : w,
		height : h,
		cache : false,
		href : url,
		modal : true,
	}).dialog('open');
	return false;
}



//js获取项目根路径，如： http://localhost:8083/uimcardprj
function getRootPath(){
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath=window.document.location.href;
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083
	var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return (localhostPaht+projectName);
}

//格式化日期
function getDateStr(value, formatStr){
	formatStr = formatStr|| "yyyy-MM-dd"; //默认格式
	var dateReg = /^([0-9]{4})[-/\.年]([0-1]?[0-9]{1})[-/\.月]([0-3]?[0-9]{1})[日]?.?([0-2]?[0-9](:[0-6][0-9]){2})?/;
	var arr = dateReg.exec(value);
	if (arr && arr[0]) {
		if (formatStr) {
			var subReg = /y+(.)M+(.)d+(.)?/i;
			var subArr = subReg.exec(formatStr);
			if (subArr) {
				var resStr = arr[1] + subArr[1];
				resStr += arr[2] + subArr[2];
				resStr += arr[3] + (subArr[3] || "");
				return resStr;
			}
		}
		return arr[0];
	}
	return "";
}

/**
 * 时间对象的格式化;
 */
Date.prototype.format = function(format) {
	/*
	 * eg:format="yyyy-MM-dd hh:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
		// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
						- RegExp.$1.length));
	}

	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1
							? o[k]
							: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
//默认展开树的第一层
function expandFirst(){
	var roots=$('.tree-box .tree').tree('getRoots');
	$('.tree-box .tree').tree('collapseAll');
	roots[0].target.click(); 
	$('.tree-box .tree').tree('select',-1);
	expandNode($('.tree-box .tree'));
}

/**
 * 展开树根节点
 * @param $treeObj
 */
function expandNode($treeObj){
	var roots=$treeObj.tree('getRoots');
	for(var i=0;i<roots.length;i++){
		$treeObj.tree('expand',roots[i].target);
	} 
}

/**
 * 四舍五入
 * @param len:小数点长度
 * @return string
 */
Number.prototype.toSSWRFixed=function(len){
	var add = 0;
	var s,temp;
	var s1 = this + "";
	var start = s1.indexOf(".");
	if(start==-1)return this.toFixed(len);
	if(s1.substr(start+len+1,1)>=5)add=1;
	var temp = Math.pow(10,len);
	s = Math.floor(this * temp) + add;
	return parseFloat(s/temp).toFixed(2);
}
//展示信息
function show(t,message){
	$.messager.show({
        title: t,
        msg: message,
        showType:'fade',
        style:{
            right:'',
            bottom:''
        }
    });
}

function daysBetween(DateOne,DateTwo)  
{   
    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));  
    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);  
    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));  
    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-')); 
    var i = DateTwo.indexOf("00:00:00.0");
    var TwoDay='';
    if(i>0){
       TwoDay = DateTwo.substring(DateTwo.lastIndexOf(' '),DateTwo.lastIndexOf ('-')+1);  
    }else{
       TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1); 
    }
    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));
    
	var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);   
    return cha;  
};

function importBox(s){
	var fn=s.fn;
	if(!s.boxTitle){s.boxTitle="导入";};
	if(!s.downHref){s.downHref="";};
	if(!s.action){s.action="";};
	if(!s.items){s.items="";};
	//url 三次  encodeURI 
	s.target.window({
		title:s.boxTitle,
		top:80,
		modal:true,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false,
		shadow:false,
		href:getRootPath()+'/ExcelMapController/importer?action='+s.action+'&downHref='+encodeURI(encodeURI(encodeURI(s.downHref)))+'&fn='+fn+'&items='+s.items ,
		width:370
	});
};

//获取url参数
function getQueryString(name){
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]);return null;
}

//表单序列化为JSON对象
(function($){  
    $.fn.serializeJson=function(){  
        var serializeObj={};  
        var array=this.serializeArray();  
        var str=this.serialize();  
        $(array).each(function(){
            if(serializeObj[this.name]){  
                if($.isArray(serializeObj[this.name])){  
                    serializeObj[this.name].push(this.value);  
                }else{  
                    serializeObj[this.name]=[serializeObj[this.name],this.value];  
                }  
            }else{  
                serializeObj[this.name]=this.value;   
            }  
        });  
        return serializeObj;  
    };  
})(jQuery);  


/**
 * 导航栏js
 */
/*$(function(){
	//分页条 
	var menuid = getQueryString('sys_menu_id');
	if(menuid!=null && menuid.length>0 && $('.nav-box').length>0){
		var data = parent.getMenuDatas();
		var path = new Array();
		var pathindex = 0;
		if(checkData(data,menuid,path,pathindex)){
			$('.nav-box').html(createNavHtml(path,menuid));
		}
	}
});
*/
//创建导航来html
function createNavHtml(data,menuid){
	var html='';
	html+='<ul><li class="index"><a href="'+getRootPath()+'/indexController/indexPage">首页</a></li>';
	for(var i=0;i<data.length;i++){
		html+='<li><a href="javascript:void(0);">';
		if(data[i].id==menuid){
			html+='<li><a href="javascript:void(0);" class="curr">';
		}else{
			html+='<li><a href="javascript:void(0);">';
		}
		html+=data[i].label;
		html+='</a></li>';
		if(data[i].id==menuid)break;
	}
	html+='</ul>';
	return html;
}

//查找导航栏数据
function checkData(data,menuid,path,pathindex){
	for(var i=0;i<data.length;i++){
		if(data[i].id==menuid){
			path[pathindex]=data[i];
			return true;
		}else if(data[i].children.length>0){
			path[pathindex]=data[i];
			var flag = checkData(data[i].children,menuid,path,pathindex+1);
			if(flag)return true;
			if(!flag && i==data.length-1){
				return false;
			}
		}
	}
	return false;
}

//组装导出的get请求
function exportFrom(url,jsonObj){
	if(url.indexOf('?')<0)url+='?';
	for(var item in jsonObj){  
        var jValue=jsonObj[item];//key所对应的value
        url+='&';
        url+=item;
        url+='=';
        url+=jValue;
    }
	url=encodeURI(encodeURI(url));
	
	$.ajax({
	    type:"GET",
	    url:url,
	    success:function(data){
	    	window.location.href=url;
	    },
	    error:function(e){
	        $.fool.alert({msg:e.responseJSON.error});
	    }
	});
	/*try{
		window.location.href=url;
	}catch(e){
		alert();
		return;
	}*/
}
//页面搜索栏的宽度控制
$('.toolBox-pane').css('width',$(window).width()-$('.toolBox-button').width()-30);

//键盘操作绑定事件
//参数 target:列表对象
function keyHandler(target){
	var selected="";
	var row="";
	var index="";
	var field="";
	var panel="";
	var id=$(target).attr("id");
	if(target){
		panel=$("#gbox_"+id);
	}else{
		target=$("#goodsList");
		panel=$("#gbox_goodsList");
	}
	panel.on("focus","input",function(e){
		selected=$(this);
		row=$(this).closest("tr[role='row']");
		index=row.attr("id");
		var describedby=selected.closest("td").attr("aria-describedby");
		field=describedby.slice(describedby.lastIndexOf("_")+1);
		/*target.datagrid('selectRow',index);*/
		/*$(this).parent().prev().attr('class').search(/numberbox-f/)!=-1?setTimeout(function(){selected.select();},50):null;//数字input聚焦后选中
*/	});
	panel.bind('keydown', function (e) {
		switch (e.keyCode) {
		    case 37: // left
		            if(selected){
		            	var ed="";
		            	if(target){
		            		ed=getLastField(row,field,index,target);
		            	}else{
		            		ed=getLastField(row,field,index);
		            	}
		            	if(ed){
		            		if($(ed).next().attr("class")=="dhxDiv"){
		            			($(ed).next())[0].comboObj.openSelect();
		            			$(ed).next().find(".dhxcombo_input").click();
		            			$(ed).next().find(".dhxcombo_input").focus();
		            		}else if($(ed).next().attr("class").search(/numberbox/)!=-1){
		            			/*$(ed).numberbox("textbox").focus();
		            			$(ed).numberbox("textbox").click();*/
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed).next().attr("class").search(/textbox/)!=-1){
		            			$(ed).textbox("textbox").focus();
		            			$(ed).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed).focus();
			            		$(ed).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed).select();
		            			},1);
		            		}
		            	}
		            }
	                break;
	                
	        case 38: // up
	        	    if(selected){
	        	    	var ed="";
	        	    	if(target){
		            		ed=getUpField(row,field,index,target);
		            	}else{
		            		ed=getUpField(row,field,index);
		            	}
	        	    	if(ed){
	        	    		if($(ed).next().attr("class")=="dhxDiv"){
		            			($(ed).next())[0].comboObj.openSelect();
		            			$(ed).next().find(".dhxcombo_input").click();
		            			$(ed).next().find(".dhxcombo_input").focus();
		            		}else if($(ed).next().attr("class").search(/numberbox/)!=-1){
		            			/*$(ed).numberbox("textbox").focus();
		            			$(ed).numberbox("textbox").click();*/
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed).next().attr("class").search(/textbox/)!=-1){
		            			$(ed).textbox("textbox").focus();
		            			$(ed).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed).focus();
			            		$(ed).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed).select();
		            			},1);
		            		}
		            	}
	        	    }
                    break; 
		
			case 39: // right
				    if(selected){
				    	var ed="";
		            	if(target){
		            		ed=getNextField(row,field,index,target);
		            	}else{
		            		ed=getNextField(row,field,index);
		            	}
		            	if(ed){
		            		if($(ed).next().attr("class")=="dhxDiv"){
		            			($(ed).next())[0].comboObj.openSelect();
		            			$(ed).next().find(".dhxcombo_input").click();
		            			$(ed).next().find(".dhxcombo_input").focus();
		            		}else if($(ed).next().attr("class").search(/numberbox/)!=-1){
		            			/*$(ed).numberbox("textbox").focus();
		            			$(ed).numberbox("textbox").click();*/
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed).next().attr("class").search(/textbox/)!=-1){
		            			$(ed).textbox("textbox").focus();
		            			$(ed).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed).focus();
			            		$(ed).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed).select();
		            			},1);
		            		}
		            	}
	                }
			        break;
			case 40: // down
				    if(selected){
				    	var ed="";
				    	if(target){
		            		ed=getDownField(row,field,index,target);
		            	}else{
		            		ed=getDownField(row,field,index);
		            	}
				    	if(ed&&ed.length>0){
				    		if($(ed).next().attr("class")=="dhxDiv"){
		            			($(ed).next())[0].comboObj.openSelect();
		            			$(ed).next().find(".dhxcombo_input").click();
		            			$(ed).next().find(".dhxcombo_input").focus();
		            		}else if($(ed).next().attr("class").search(/numberbox/)!=-1){
		            			/*$(ed).numberbox("textbox").focus();
		            			$(ed).numberbox("textbox").click();*/
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else if($(ed).next().attr("class").search(/textbox/)!=-1){
		            			$(ed).textbox("textbox").focus();
		            			$(ed).textbox("textbox").click();
		                		//获取焦点后全选内容
		                		setTimeout(function(){
		                			$(ed).numberbox("textbox").select();
		            			},1);
		            		}else{
		            			$(ed).focus();
			            		$(ed).click();
			            		//获取焦点后全选内容
			            		setTimeout(function(){
			            			$(ed).select();
		            			},1);
		            		}
		            	}
				    }
		            break;
			case 13: // enter 需求变更
				if(selected&&target&&selected.closest("td[aria-describedby]").attr("aria-describedby")!="goodsList_barCode"){
					if(selected.attr("class").search(/dhxcombo_input/)==-1){
						if($(row).find(".btn-save")){
							$(row).find(".btn-save").click();
						}
					}
				}
	            break;
		}
	});
}

//获取指定行的编辑列
function getEdFields(row){
	if(row){
		var tds=row.children();
		var edFields=[];
		for(var i=0;i<tds.length;i++){
			if ($(($(tds[i]).children())[0]).attr('class')&&$(($(tds[i]).children())[0]).attr('class').search(/editable/)!=-1&&$(tds[i]).css("display")!="none"){
				if(!row.children("[aria-describedby='goodsList_goodsSpecGroupId']").children().val()&&$(tds[i]).attr("aria-describedby")=="goodsList_goodsSpecName"){
					continue;
				}
				edFields.push($(tds[i]));
			}
		}
		return edFields;
	}
}
//获取同行下一个编辑框，没有就保存
//参数 target:列表对象
function nextOrSave(row,field,index,target){ 
	var edFields=getEdFields(row);
	var listTarget="";
	if(target){
		listTarget=target;
	}else{
		listTarget=$("#goodsList");
	}
	for(var i=0;i<=edFields.length;i++){
		if($(edFields[i]).attr("aria-describedby")==listTarget.attr("id")+"_"+field&&parseInt(i)+1<edFields.length){
			/*return listTarget.datagrid('getEditor',{index:index,field:$(edFields[i+1]).attr("field")});*/
			return getEditor(listTarget.attr("id"),row.attr("id"),$(edFields[i+1]).attr("aria-describedby"));
		}
	}
	return false;
}
//获取下一个编辑框
//参数 target:列表对象
function getNextField(row,field,index,target){
	var edFields=getEdFields(row);
	var result="";
	var listTarget="";
	var nextField="";
	if(target){
		listTarget=target;
	}else{
		listTarget=$("#goodsList");
	}
	for(var i=0;i<=edFields.length;i++){
		if($(edFields[i]).attr("aria-describedby")==listTarget.attr("id")+"_"+field&&parseInt(i)+1<edFields.length){
			nextField=$(edFields[i+1]).attr("aria-describedby").slice($(edFields[i+1]).attr("aria-describedby").lastIndexOf("_")+1);
			result=getEditor(listTarget.attr("id"),row.attr("id"),nextField);
			if($(result.target).attr("disabled")=="disabled"){
				result=getNextField(row,nextField,index,listTarget);
			}
			return result;
		}else if($(edFields[i]).attr("aria-describedby")==listTarget.attr("id")+"_"+field&&i+1>=edFields.length){
			var rows=listTarget.getRowData();
			var num=rows.length+1;
			var nextRow="";
			var fields="";
			for (var j=parseInt(index)+1;j<num;j++){
				nextRow=row.siblings("[id="+j+"]");
				fields=getEdFields($(nextRow[0]));
				if(fields.length>0){
					/*result=listTarget.datagrid('getEditor',{index:j,field:$(fields[0]).attr('field')});*/
					nextField=$(fields[0]).attr("aria-describedby").slice($(fields[0]).attr("aria-describedby").lastIndexOf("_")+1);
					result=getEditor(listTarget.attr("id"),nextRow.attr("id"),nextField);
					if($(result).attr("disabled")=="disabled"){
						result=getNextField(nextRow,nextField,j,listTarget);
					}
					return result;
				}
			}
		};
		nextField="";
	}
}
//获取上一个编辑框
//参数 target:列表对象
function getLastField(row,field,index,target){
	var edFields=getEdFields(row);
	var result="";
	var listTarget="";
	var lastField="";
	if(target){
		listTarget=target;
	}else{
		listTarget=$("#goodsList");
	}
	for(var i=0;i<edFields.length;i++){
		if($(edFields[i]).attr("aria-describedby")==listTarget.attr("id")+"_"+field&&i-1>=0){
			lastField=$(edFields[i-1]).attr("aria-describedby").slice($(edFields[i-1]).attr("aria-describedby").lastIndexOf("_")+1);
			result=getEditor(listTarget.attr("id"),row.attr("id"),lastField);
			if($(result.target).attr("disabled")=="disabled"){
				result=getLastField(row,lastField,index);
			}
			return result;
		}else if($(edFields[i]).attr("aria-describedby")==listTarget.attr("id")+"_"+field&&i-1<0){
			var lastRow="";
			var fields="";
			for (var j=parseInt(index)-1;j>=0;j--){
				lastRow=row.siblings("[id="+j+"]");
				fields=getEdFields($(lastRow[0]));
				if(fields.length>0){
					lastField=$(fields[fields.length-1]).attr("aria-describedby").slice($(fields[fields.length-1]).attr("aria-describedby").lastIndexOf("_")+1);
					result=getEditor(listTarget.attr("id"),lastRow.attr("id"),lastField);
					if($(result.target).attr("disabled")=="disabled"){
						result=getLastField(lastRow,lastField,j);
					}
					return result;
				}
			}
		};
	}
}
//获取上一行编辑框
//参数 target:列表对象
function getUpField(row,field,index,target){
	var lastRow="";
	var fields="";
	var listTarget="";
	if(target){
		listTarget=target;
	}else{
		listTarget=$("#goodsList");
	}
	for (var j=parseInt(index)-1;j>=0;j--){
		lastRow=row.siblings("[id="+j+"]");
		if(lastRow.length>0&&lastRow.attr("editable")!=1){
			editNew(j); 
		}
		fields=getEdFields($(lastRow[0]));
		if(fields&&fields.length>0){
			/*return listTarget.datagrid('getEditor',{index:j,field:field});*/
			return getEditor(listTarget.attr("id"),j,field);
		}
	}
}
//获取下一行编辑框
//参数 target:列表对象
function getDownField(row,field,index,target){
	var listTarget="";
	if(target){
		listTarget=target;
	}else{
		listTarget=$("#goodsList");
	}
	var rows=row.nextAll();
	var num=rows.length;
	var nextRow="";
	var fields="";
	if(num>0){
		nextRow=rows[0];
		if($(nextRow).attr("editable")!=1){
			editNew($(nextRow).attr("id")); 
		}
	}
	fields=getEdFields($(nextRow));
	if(fields){
		return getEditor(listTarget.attr("id"),$(nextRow).attr("id"),field);
	}
}
//编辑框回车代替tab操控函数
function enterController(paramId,notFocus){//输入框的父元素ID
	var kdown=[];
	var fkd="";
	var tips="";
	var topkd='';
    
	//可添加额外类型
	var $target = $('#'+paramId).find('input,button,select');
	$target.each(function(){
		var myinput = $(this);
		var nxtIdx = $target.index(this);
		if(myinput.parent().prev().attr("class")){
			if(myinput.parent().prev().attr("class").indexOf("datebox-f")!=-1){
				myinput.focus(function(){
					myinput.unbind('keydown');
					myinput.bind('keydown', kdown[nxtIdx]);
					myinput.bind('keydown', topkd);
					var date=myinput.parent().prev().datebox('panel');
					date.find('.calendar-selected').addClass('calendar-nav-hover');
				});
				myinput.blur(function(){
					$(".calendar-nav-hover").removeClass('calendar-nav-hover');
				});
				myinput.bind('keydown', kdown[nxtIdx]=function (e) {
					var date=$(this).parent().prev().datebox('panel');
					var sIndex = date.find("td").index($('.calendar-nav-hover'));
					switch (e.keyCode) {
					case 37: // 左
					if(sIndex%7!=0){
					date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
					date.find('td').eq(sIndex-1).addClass('calendar-nav-hover');
					}
					break;
					case 38: // 上
					if(sIndex>6){
						date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
						date.find('td').eq(sIndex-7).addClass('calendar-nav-hover');
					}
					break;
					case 39: // 右
					if(sIndex%7!=6){
						date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
						date.find('td').eq(sIndex+1).addClass('calendar-nav-hover');
					}
					break;
					case 40: // 下
					if(sIndex<date.find("td").length-1){
						date.find('td').eq(sIndex).removeClass('calendar-nav-hover');
						date.find('td').eq(sIndex+7).addClass('calendar-nav-hover');
					}
					break;
					case 219: // [键切换月份减1
					date.find(".calendar-prevmonth").click();
					date.find("td").eq(sIndex).addClass('calendar-nav-hover');
					break;
					case 221: // ]键切换月份加1
					date.find(".calendar-nextmonth").click();
					date.find("td").eq(sIndex).addClass('calendar-nav-hover');
					break;
					case 32: //空格键
					date.find('td').eq(sIndex).click();
					break;
					case 13: //回车
						date.find('td').eq(sIndex).click();
						$(this).parent().prev().datebox('hidePanel');
					break;
					}
				});
				
			}else if(myinput.parent().prev().attr("class").indexOf("combotree-f")!=-1){
				myinput.focus(function(){
					myinput.unbind('keydown');
					myinput.bind('keydown', kdown[nxtIdx]);
					myinput.bind('keydown', topkd);
					var tree=myinput.parent().prev().combotree('panel');
					tree.find("div").eq(1).addClass('tree-node-hover');
					tree.scrollTop(0);
				});
				myinput.blur(function(){
					$(".tree-node-hover").removeClass('tree-node-hover');
				});
				myinput.bind('keydown', kdown[nxtIdx]=function (e) {
				var tree=$(this).parent().prev().combotree('panel');
				var sIndex = tree.find("div").index($('.tree-node-hover'));
				switch (e.keyCode) {
				case 38: // 上
				e.preventDefault();//解决页面有滑动条时浏览器按上下会滑动整个窗口的问题
				if(sIndex>1){
					if(tree.find("div").length > 7 && tree.find('div').eq(sIndex).position().top < 34){//判断上拉滚动条
						tree.scrollTop(24*sIndex-178);
					}
					tree.find('div').eq(sIndex).removeClass('tree-node-hover');
					tree.find('div').eq(sIndex-1).addClass('tree-node-hover');
				}
				break;
				case 40: // 下
				e.preventDefault();//解决页面有滑动条时浏览器按上下会滑动整个窗口的问题
				if(sIndex<tree.find("div").length-1){
					if(tree.find("div").length > 7 && tree.find('div').eq(sIndex).position().top > 154){//判断下拉滚动条
						tree.scrollTop(24*(sIndex-6));
					}
					tree.find('div').eq(sIndex).removeClass('tree-node-hover');
					tree.find('div').eq(sIndex+1).addClass('tree-node-hover');
				}
				break;
				case 32: //空格键
					tree.parent().css('display')=='block'?tree.find('div').eq(sIndex).click():null;
				break;
				case 13: //回车
					tree.parent().css('display')=='block'?tree.find('div').eq(sIndex).click():null;
				break;
				}
				});
				
			}
		}
	});
	//下拉框的input，enter跳转时收起下拉框
	$target.bind('keydown',function(e){
		if(e.keyCode == 13){
			if($(this).parent().prev().attr("class")){
				if($(this).parent().prev().attr("class").search(/combo-f/)!=-1){
					$(this).parent().prev().combo('hidePanel');
				}
			}
		}
	});
	function checkit(index){
		if($target.eq(index).attr("type") == "hidden" || $target.eq(index).attr("disabled") == "disabled" || $target.eq(index).css("display") == "none"){
			return false;
		}else{
			if($target.eq(index).parent().attr("class") =="hideOut" && $target.eq(index).attr("readonly") == "readonly"){
				return false;
			}else if($target.eq(index).attr("readonly") == "readonly"){
				if(paramId == 'signBox' && $target.eq(index).parent().prev().attr('class').indexOf('combotree-f') == -1){ //填制凭证signBox特例
					return false;
				}else{
					tips=1;
					return $target.eq(index).prev().length>0 && ($target.eq(index).prev().attr('class')&&$target.eq(index).prev().attr('class').search(/textbox-addon/)!=-1)?
							true:$target.eq(index).attr('class')&&$target.eq(index).attr('class').search(/textbox-text-readonly/)!=-1?false:true;
				}
			}else if($target.eq(index).parent().prev().attr('class')){
				return true;
			}else{
				return true;
			}
		}
	}
	
	//找出第一个可操作的输入框并点击
	for(var a=1; checkit(a)==false || ($target.eq(a).parent().prev().attr('class')&&$target.eq(a).parent().prev().attr('class').search(/datebox-f/) != -1); a++){
		;
	}
	if(tips==1){
		$target.eq(a).focus(function(){
				$target.eq(a).css("border-color","#6495ED");
				tips="";
		});
		$target.eq(a).blur(function(){
				$target.eq(a).css("border-color","#ccc");
		});
	}
	if(!notFocus){
		$target.eq(a).focus();
		if($target.eq(a).parent().prev().attr("class")&&$target.eq(a).parent().prev().attr("class").indexOf("combo-f")!=-1){
			$target.eq(a).parent().prev().attr('show')=="false"?null:$target.eq(a).parent().prev().combo('hidePanel');
		}
	}
		$target.eq(a).bind('keydown', fkd = function (e){
			if(e.keyCode == 40){
				if($target.eq(a).parent().prev().attr("class").indexOf("combotree-f")!=-1){
					$target.eq(a).unbind('keydown',topkd);
					$target.eq(a).parent().prev().combotree('showPanel');
				}else if($target.eq(a).parent().prev().attr("class").indexOf("combobox-f")!=-1){
					$target.eq(a).parent().prev().combobox('showPanel');
				}else if($target.eq(a).parent().prev().attr("class").indexOf("datebox-f")!=-1){
					$target.eq(a).parent().prev().datebox('showPanel');
				}
			}
		});
	
	//回车键寻找下一个输入框
	$target.bind('keydown',topkd = function (e) {
		if(fkd!=''){
			$target.eq(a).unbind('keydown',fkd);
			fkd="";
		}
	    var key = e.keyCode;
	    if (key == 13) {//回车键控制
	        e.preventDefault();//消除默认事件
	        var nxtIdx = $target.index(this)+1;
	        for(; checkit(nxtIdx)==false; nxtIdx++){
				;
			}
			if(tips==1){//让readonly的输入框被选中更明显
				$target.eq(nxtIdx).focus(function(){
						$target.eq(nxtIdx).css("border-color","#6495ED");
						tips="";
				});
				$target.eq(nxtIdx).blur(function(){
						$target.eq(nxtIdx).css("border-color","#ccc");
				});
			}
			$target.eq(nxtIdx).focus();
			if($target.eq(nxtIdx).attr('class')){
				if($target.eq(nxtIdx).attr('class').indexOf('btn-blue') == -1){
					$target.eq(nxtIdx).click();
				}
			}else if($target.eq(nxtIdx).attr('type') != 'checkbox' ){
				$target.eq(nxtIdx).click();
			}
			if($target.eq(nxtIdx).parent().prev().attr("class")){
				if($target.eq(nxtIdx).parent().prev().attr("class").indexOf("datebox-f")!=-1){
					$target.eq(nxtIdx).parent().prev().datebox('showPanel');
				}else if($target.eq(nxtIdx).parent().prev().attr("class").indexOf("combotree-f")!=-1){
					$target.eq(nxtIdx).parent().prev().combotree('showPanel');
				}
			}
	    }
	
	});
}

//回车搜索快捷键函数
function enterSearch(searchClass){//页面搜索按钮class
	$('body').bind('keydown',function(e){
		if(e.keyCode == 13){
			//页面弹窗后回车搜索失效
			var bool = true;
			for(var i=0; i<$('body').find('.window').length; i++){
				if($('body').find('.window').eq(i).css('display') == 'none'){
					bool = bool && true;
				}else{
					bool = bool && false;
				}
			}
			if($('.input_div').css("display")!="none"){
				bool = false;
			}
			bool?$("."+searchClass).click():null;
		}
	});
}
//返回会计期间错误的时候，链接去会计期间tab页
function mybtnAct(target){
	$(target).next().click();
	parent.kk('/fiscalPeriod/manage','财务会计期间');
	/*var content='<div title="财务会计期间" ><iframe scrolling="auto" frameborder="0"  src="'+getRootPath()+'/fiscalPeriod/manage" style="width:100%;height:'+($(window).outerHeight()-$('.nav').outerHeight())+'px;"></iframe></div>';
	if(!parent.$('#tabs').tabs('exists','财务会计期间')){
		parent.$('#tabs').tabs('add',{
			title:'财务会计期间',
			content:content,
			closable:true
		});
	}else{
		var tab = parent.$('#tabs').tabs('getTab','财务会计期间');
		parent.$('#tabs').tabs('select','财务会计期间');
	}
	parent.$('#listTab').find('li[listtitle="财务会计期间"]').find('.tit').click();*/
}

//仓储新增、编辑窗口的定义和弹出
function warehouseWin(_billCodeName,url,fn,args){
	$('#addBox').window({
		top:10+$(window).scrollTop(),
		left:0,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		resizable:false, 
		width:$(window).width()-10,
		height:$(window).height()-60,
		closed:true,
		modal:true,
		onOpen:function(){
			//$('html').css('overflow','hidden');
			$(this).parent().prev().css("display","none");
		},
		onClose:function(){
			if($("#checkBox").length>0 && $('#checkBox').html()){
				$("#checkBox").window("destroy");
			}
			if($("#pop-win").length>0 && $("#pop-win").html()){
				$("#pop-win").window("destroy");
			}
			if($("#import-win").length>0 && $("#import-win").html()){
				$('#import-win').window("destroy");
			}
			//$('html').css('overflow',"");
		},
		onLoad:function(){
			if(fn){
				fn(args);
			}
		}
	});
	$('#addBox').window("setTitle",_billCodeName);
	$('#addBox').window("open");
	$('#addBox').window("refresh",url);
}

//大部分有列表的页面，多数据时，表头跟随滚动
function warehouseAll(){
	//管理表格表头跟随滚动
	/*var myobj = $('.datagrid-body td[field=fid]');	
	var offset = myobj.closest('.datagrid-body').siblings('.datagrid-header').offset();
	alert(offset);
	$window = $(window);
	$window.scroll(function(){
		var scroll = $(this).scrollTop();
		if(offset.top < scroll){
			myobj.closest('.datagrid-body').siblings('.datagrid-header').addClass('fixed');
			myobj.closest('.datagrid-body').css('margin-top',"35px");
		}else{
			myobj.closest('.datagrid-body').siblings('.datagrid-header').removeClass('fixed');
			myobj.closest('.datagrid-body').css('margin-top',0);
		}
	});*/
}
function clearBut(target){
	  if($(target).prev().attr('class')=='subj1'){
		  $(target).parent().siblings('.subjn2').find('.subj2').html('');
		  $(target).parent().siblings('.subjn2').hide();
		  $(target).closest('.textbox').prev().combobox('reload',getRootPath()+'/fiscalSubject/getSubject?q=').combobox('reload',getRootPath()+'/fiscalSubject/getSubject');
	  }
	  $(target).prev().html('');
	  $(target).parent().hide();
	  $(target).closest('.subj').siblings('.textbox-text').css('width',$(target).closest('.textbox').width()-30);
}
//首页搜索功能的快捷键
$(document).bind('keydown',function(e){
	if(e.keyCode == 72 && e.ctrlKey){ //ctrl+H
		if (e.preventDefault) {  // firefox
            e.preventDefault();
        } else { // other
        	e.keyCode = 0;
            e.returnValue = false;
        } 
		parent.$('#searchInput').searchbox('textbox').focus();
	}
});

//仓储、财务部分编辑页模板自定义滚动条
function scrollDiy(){
	// 自定义下滚动条
	if(($('#list2 p').outerWidth()-$('#list2').outerWidth())>0){
		var oDiv1 = $('#scroll1');
	    var oDiv2 = $('#scroll2');
	    var oDiv3 = $('#list2');
	   
	    var iMaxWidth = oDiv1.outerWidth() - oDiv2.outerWidth();
	    oDiv2.mousedown(function(ev) {
	        var ev = ev || event;
	        var disX = ev.clientX - this.offsetLeft;
	       $(window).mousemove(function(ev) {
	            var ev = ev || event;
	            var T = ev.clientX - disX;
	            if (T < 0) {
	                T = 0;
	            } else if (T > iMaxWidth) {
	                T = iMaxWidth;
	            }
	            var iScale = T / iMaxWidth;
	            oDiv3.scrollLeft(($('#list2 p').outerWidth()-$('#list2').outerWidth()) * iScale );
	            oDiv2.css('left',T + 'px');
	        });
	        $(window).mouseup(function(){
	        	$(window).unbind('mousemove');
	        	$(window).unbind('mouseup');
	        });
	        return false;
	    });
	}else{
		$('.mybtn-footer').css('padding',"5px 0");
		$('#scroll1').hide();
		$('#scroll2').hide();
	}
}
//dhtmlxCombo的数据格式化函数
function formatTree(data,name,value,i){
	i=i?i:0;
	  var mydata = $.map( data, function( item ) {
		var mytext =(!item.attributes || $.trim(item.attributes) == "")?item:item.attributes.detail;
		var a = [];
	if(item.children.length != 0){
			mytext.mypar = 1;//标识有子节点的父节点
			var a = formatTree(item.children,name,value,i+1);
	}else{
		mytext.myflag = 1;
	}
	if(i){
		mytext.child=i;//标识子节点的层数
	}
	var d = [{
        text: mytext,
        value: item[value]
      }];
	d=$.merge(d,a);
	return d;
    });
	return mydata;
}
function formatData(data,value){
	  var mydata = $.map( data, function( item ) {
    return {
      text: item,
      value: item[value]
    }
  });
	return mydata;
}
function formatGrid(data,value){
	  var mydata = $.map( data.rows, function( item ) {
  return {
    text: item,
    value: item[value]
  }
});
	return mydata;
}

// 访问url后，格式化返回dhtmlxCombo数据
function getComboData(url,mark,textField,idField){
	var result="";
	if(!idField){
		if(mark!="tree"){
			idField="fid";
		}else{
			idField="id";
			textField="text";
		}
	}
	$.ajax({
		url:url,    
		data:{num:Math.random()},
		async:false,
		success:function(data){
			if(data){
				result=data;
			}
		}
	});
	if(!mark){
		return formatData(result,idField);
	}else if(mark=="tree"){
		return formatTree(result[0].children,textField,idField);
	}else{
		return formatGrid(result,idField);
	}
}

//由于后台返回的数据格式可能有2种，这里做一下兼容处理
function dataDispose(data){
	if(typeof data.result != "undefined"){
		data.returnCode = data.result;
		data.message = data.msg;
		data.data = data.obj;
		data.dataExt?null:data.dataExt = data.obj;
		/*if(typeof data.data!="undefined"){
			typeof data.obj=="undefined"?data.obj = data.data:data.dataObj = data.data;
		}*/
	}else if(typeof data.returnCode != "undefined"){
		data.result = data.returnCode;
		
		
		data.msg = data.message;
		data.obj = data.data;
		data.dataExt?null:data.dataExt = data.data;
		/*if(typeof data.obj!="undefined"){
			typeof data.data=="undefined"?data.data = data.obj:data.dataObj = data.obj;
			typeof data.dataExt == "undefined"?data.dataExt = data.obj:null;
		}*/
	}
}
//获取jqGrid编辑框
function getEditor(gridId,rowId,name){
	  var editor = $('#'+gridId).find($('#'+rowId+"_"+name));
	  return editor;
}
//获取DataGrid返回JSON对象集合的第一条数据
function getDataFirst(data){
	var _d = data;
	if(typeof data[0]!='undefined'){
		_d = data[0];
	}
	return _d;
}

//让表格翻前一页
function getNext(target,url){
	var gridParam = $(target).jqGrid("getGridParam");
	var pageNumber=gridParam.page;
	var page = pageNumber - 1;
	if(pageNumber<=1){
		return false;
	}
	var result = "";
	var postdata = gridParam.postData;
	 $.ajax({
		url:url+"&page="+page,
		async:false,
		data:postdata,
		success:function(data){
			if(data){
				result=data.rows;
			}
		}
	});
	 $(target).jqGrid("setGridParam",{page:page}).trigger("reloadGrid");
		if(result){
			return result;
		}else{
			return false;
		}
}
//判断对象是否相等
function equals( x, y ) {   
    var in1 = x instanceof Object;  
    var in2 = y instanceof Object;  
    if(!in1||!in2){  
      return x===y;  
    }  
    if(Object.keys(x).length!==Object.keys(y).length){  
      return false;  
     }  
    for(var p in x){  
    var a = x[p] instanceof Object;  
    var b = y[p] instanceof Object;  
      if(a&&b){  
        return equals( x[p], y[p]);  
       }  
       else if(x[p]!==y[p]){  
         return false;  
       }  
    }  
      
    return true;  
}
//dhxComboGrid下拉框的工具栏，添加下拉框项数据功能按钮
function dhxAddUrl(url,name){
	var tabname = $(".titleCustom .squareIcon h1").text();
	var newtab = "添加"+name;
	url = url.indexOf("?")!= -1?url+"&dhxkey=1&dhxname="+tabname+"&dhxtab="+newtab:url+"?dhxkey=1&dhxname="+tabname+"&dhxtab="+newtab;
	url = encodeURI(url);
	parent.kk(url,newtab);
}

//选取已有的目录tab页
function selectTab(name,dhxtab){
	parent.$('#tabs').tabs('select',name);
	var tab = parent.$('#tabs').tabs('getSelected');
	var index = parent.$('#tabs').tabs('getTabIndex',tab);
	parent.$('#listTab li').eq(index-1).css('background','#495970').siblings().css('background','#53637a');
	parent.$('.cathet ul li').css('background','#53637a');
	parent.$('#listTab li[listtitle='+dhxtab+']').find("a").click();
}

//辅助属性对应编号
function dhxGetCode(name){
	var code = "";
	var url = "";
	switch(name){
		case "场地类型": 
			code = '022';break;
		case "费用项目": 
			code = '008';break;
		case "运输费用": 
			code = '018';break;
		case "摘要": 
			code = '014';break;
		case "货品类别": 
			code = '004';break;
		case "打印类型": 
			code = '017';break;
		case "运输费计价单位": 
			code = '021';break;
		case "客户类别": 
			code = '002';break;
		case "学历": 
			code = '006';break;
		case "在职状况":
			code = '005';break;
		case "运输方式": 
			code = '019';break;
		case "损耗地址":
			code = '023';break;
		case "财务项目": 
			code = '012';break;
		case "币别": 
			code = '011';break;
		case "征信级别": 
			code = '003';break;
		case "仓库": 
			code = '007';break;
		case "地区": 
			code = '001';break;
		case "科目类别": 
			code = '009';break;
		case "凭证字": 
			code = '010';break;
		case "结算方式": 
			code = '015';break;
		case "装运方式": 
			code = '020';break;
		case "资产类型": 
			code = '016';break;
		case "部门":
			code = 0;url = "/orgController/getAllTree";break;
		case "计划模板":
			code = 0;url = "/flow/planTemplate/queryAll";break;
		case "事件类别":
			code = 0;url = "/flow/taskType/queryAll";break;
		case "保密级别":
			code = 0;url = "/flow/security/queryAll";break;
		case "事件级别":
			code = 0;url = "/flow/taskLevel/queryAll";break;
		case "计划级别":
			code = 0;url = "/flow/taskLevel/queryAll";break;
		case "事件模板":
			code = 0;url = "/flow/taskTemplate/queryAll";break;
		case "货品属性组":
			code = 0;url = "/goodsspec/getSpecGroups";break;
		case "货品单位":
			code = 0;url = "/unitController/getAll";break;
		case "会计期间":
			code = 0;url = "/fiscalPeriod/getAll";break;
		case "现金银行":
			code = 0;url = "/bankController/comboboxData";break;
		case "工资项目":
			code = 0;url = "/wageFormula/queryAll";break;
        case "货运地址":
            code = 0;url = "/api/freightAddress/findAddressTree";break;
		default: code = 0;
	}
	if(code != 0){
		url = getRootPath()+"/basedata/findSubAuxiliaryAttrTree?code="+code+"&num="+Math.random();
	}else if(code == 014){
		url = getRootPath()+'/basedata/fuzzyFindSubAuxiliaryAttrTree?code=014&num='+Math.random();
    }else{
		if(name=="计划模板"){
			url = getRootPath()+url+"?status=1&num="+Math.random();
		}else if(name=="货运地址"){
            url = getRootPath()+url+"?enable=1&num="+Math.random();
		}else{
			url = getRootPath()+url+"?num="+Math.random();
		}
	}
	return url;
}
window.onresize=function(){
	
};
$(function(){
	//计划新增修改页面：修复下拉框工具栏会跟随窗口滚动的问题
	$(document).scroll(function(){
		var scroll = $(this).scrollTop();
		$('.mytoolsBar').each(function(){
			var myheight = $(this).parent().height();
			var a = 33;
			if($(this).parent().attr('class') == 'dhxcombolist_material'){
				a = 32;
			}
			var max = $(this).parent().find(".dhxcombo_option").length*a;
			var mytop = myheight - max; 
			var mt = mytop-scroll;
			$(this).css("margin-top",mt);
		});
	});
	$(".mtb-btnDown-l").click(function(){
		if(!$('.mtb-btnMore-l') || $('.mtb-btnMore-l').html()==""){
			return false;
		}
		$('.mtb-btnMore-l').slideToggle(300);
	});
	$(".mtb-right").click(function(){
		if(!$('.mtb-btnMore-r') || $('.mtb-btnMore-r').html()==""){
			return false;
		}
		$('.mtb-btnMore-r').slideToggle(300);
	});
	//设置按钮和下拉框阻止冒泡，然后设置整个界面的点击事件
	$(".mtb-btnMore-l,.mtb-btnDown-l,.mtb-right,.mtb-btnMore-r,.dhxcombolist_material").click(function(event){
		event.stopPropagation();
	});
	$(document).click(function(){
		$('.mtb-btnMore-l').slideUp(300);
		$('.mtb-btnMore-r').slideUp(300);
	});
});