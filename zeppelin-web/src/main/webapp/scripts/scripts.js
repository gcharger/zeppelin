"use strict";function getPort(){var a=Number(location.port);return"https:"===location.protocol||"undifined"!==a&&0!==a?"https:"!==location.protocol||"undifined"!==a&&0!==a?(3333===a||9e3===a)&&(a=8080):a=443:a=80,a+1}function getWebsocketProtocol(){var a="ws";return"https:"===location.protocol&&(a="wss"),a}function getRestApiBase(){var a=Number(location.port);return("undefined"===a||0===a)&&(a=80,"https:"===location.protocol&&(a=443)),(3333===a||9e3===a)&&(a=8080),location.protocol+"//"+location.hostname+":"+a+skipTrailingSlash(location.pathname)+"/api"}function skipTrailingSlash(a){return"/"===a.slice(-1)?a.substring(0,a.length-1):a}angular.module("zeppelinWebApp",["ngAnimate","ngCookies","ngRoute","ngSanitize","angular-websocket","ui.ace","ui.bootstrap","ui.sortable","ngTouch","ngDragDrop","monospaced.elastic","puElasticInput","xeditable"]).filter("breakFilter",function(){return function(a){return void 0!==a?a.replace(/\n/g,"<br />"):void 0}}).config(["$routeProvider","WebSocketProvider",function(a,b){b.prefix("").uri(getWebsocketProtocol()+"://"+location.hostname+":"+getPort()),a.when("/",{templateUrl:"views/main.html"}).when("/notebook/:noteId",{templateUrl:"views/notebooks.html",controller:"NotebookCtrl"}).when("/notebook/:noteId/paragraph/:paragraphId?",{templateUrl:"views/notebooks.html",controller:"NotebookCtrl"}).when("/interpreter",{templateUrl:"views/interpreter.html",controller:"InterpreterCtrl"}).otherwise({redirectTo:"/"})}]),angular.module("zeppelinWebApp").controller("MainCtrl",["$scope","WebSocket","$rootScope","$window",function(a,b,c,d){c.compiledScope=a.$new(!0,c),a.WebSocketWaitingList=[],a.connected=!1,a.looknfeel="default";var e=function(){a.asIframe=d.location.href.indexOf("asIframe")>-1?!0:!1};e(),b.onopen(function(){if(console.log("Websocket created"),a.connected=!0,a.WebSocketWaitingList.length>0)for(var d in a.WebSocketWaitingList)b.send(JSON.stringify(a.WebSocketWaitingList[d]));setInterval(function(){c.$emit("sendNewEvent",{op:"PING"})},6e4)}),b.onmessage(function(b){var c;b.data&&(c=angular.fromJson(b.data)),console.log("Receive << %o, %o, %o",c.op,c,a);var d=c.op,e=c.data;"NOTE"===d?a.$broadcast("setNoteContent",e.note):"NOTES_INFO"===d?a.$broadcast("setNoteMenu",e.notes):"PARAGRAPH"===d?a.$broadcast("updateParagraph",e):"PROGRESS"===d?a.$broadcast("updateProgress",e):"COMPLETION_LIST"===d?a.$broadcast("completionList",e):"ANGULAR_OBJECT_UPDATE"===d&&a.$broadcast("angularObjectUpdate",e)}),b.onerror(function(b){console.log("error message: ",b),a.connected=!1}),b.onclose(function(b){console.log("close message: ",b),a.connected=!1});var f=function(c){"OPEN"!==b.currentState()?a.WebSocketWaitingList.push(c):(console.log("Send >> %o, %o",c.op,c),b.send(JSON.stringify(c)))};c.$on("sendNewEvent",function(a,b){a.defaultPrevented||(f(b),a.preventDefault())}),c.$on("setIframe",function(b,c){b.defaultPrevented||(a.asIframe=c,b.preventDefault())}),c.$on("setLookAndFeel",function(b,c){!b.defaultPrevented&&c&&""!==c&&(a.looknfeel=c,b.preventDefault())})}]),angular.module("zeppelinWebApp").controller("NotebookCtrl",["$scope","$route","$routeParams","$location","$rootScope","$http",function(a,b,c,d,e,f){a.note=null,a.showEditor=!1,a.editorToggled=!1,a.tableToggled=!1,a.viewOnly=!1,a.looknfeelOption=["default","simple","report"],a.cronOption=[{name:"None",value:void 0},{name:"1m",value:"0 0/1 * * * ?"},{name:"5m",value:"0 0/5 * * * ?"},{name:"1h",value:"0 0 0/1 * * ?"},{name:"3h",value:"0 0 0/3 * * ?"},{name:"6h",value:"0 0 0/6 * * ?"},{name:"12h",value:"0 0 0/12 * * ?"},{name:"1d",value:"0 0 0 * * ?"}],a.interpreterSettings=[],a.interpreterBindings=[];var g={};a.getCronOptionNameFromValue=function(b){if(!b)return"";for(var c in a.cronOption)if(a.cronOption[c].value===b)return a.cronOption[c].name;return b};var h=function(){e.$emit("sendNewEvent",{op:"GET_NOTE",data:{id:c.noteId}})};h(),a.removeNote=function(a){var b=confirm("Do you want to delete this notebook?");b&&(e.$emit("sendNewEvent",{op:"DEL_NOTE",data:{id:a}}),d.path("/#"))},a.runNote=function(){var b=confirm("Run all paragraphs?");b&&a.$broadcast("runParagraph")},a.toggleAllEditor=function(){a.editorToggled?a.$broadcast("closeEditor"):a.$broadcast("openEditor"),a.editorToggled=!a.editorToggled},a.showAllEditor=function(){a.$broadcast("openEditor")},a.hideAllEditor=function(){a.$broadcast("closeEditor")},a.toggleAllTable=function(){a.tableToggled?a.$broadcast("closeTable"):a.$broadcast("openTable"),a.tableToggled=!a.tableToggled},a.showAllTable=function(){a.$broadcast("openTable")},a.hideAllTable=function(){a.$broadcast("closeTable")},a.isNoteRunning=function(){var b=!1;if(!a.note)return!1;for(var c=0;c<a.note.paragraphs.length;c++)if("PENDING"===a.note.paragraphs[c].status||"RUNNING"===a.note.paragraphs[c].status){b=!0;break}return b},a.setLookAndFeel=function(b){a.note.config.looknfeel=b,a.setConfig()},a.setCronScheduler=function(b){a.note.config.cron=b,a.setConfig()},a.setConfig=function(b){b&&(a.note.config=b),e.$emit("sendNewEvent",{op:"NOTE_UPDATE",data:{id:a.note.id,name:a.note.name,config:a.note.config}})},a.sendNewName=function(){a.showEditor=!1,a.note.name&&e.$emit("sendNewEvent",{op:"NOTE_UPDATE",data:{id:a.note.id,name:a.note.name,config:a.note.config}})},a.$on("setNoteContent",function(b,d){a.paragraphUrl=c.paragraphId,a.asIframe=c.asIframe,a.paragraphUrl&&(d=j(a.paragraphUrl,d),e.$emit("setIframe",a.asIframe)),null===a.note?a.note=d:k(d),i(),l(m)});var i=function(){a.note.config.looknfeel?a.viewOnly="report"===a.note.config.looknfeel?!0:!1:a.note.config.looknfeel="default",e.$emit("setLookAndFeel",a.note.config.looknfeel)},j=function(a,b){var c={};c.id=b.id,c.name=b.name,c.config=b.config,c.info=b.info,c.paragraphs=[];for(var d=0;d<b.paragraphs.length;d++)if(b.paragraphs[d].id===a){c.paragraphs[0]=b.paragraphs[d],c.paragraphs[0].config||(c.paragraphs[0].config={}),c.paragraphs[0].config.editorHide=!0,c.paragraphs[0].config.tableHide=!1;break}return c};a.$on("moveParagraphUp",function(b,c){for(var d=-1,f=0;f<a.note.paragraphs.length;f++)if(a.note.paragraphs[f].id===c){d=f-1;break}0>d||d>=a.note.paragraphs.length||e.$emit("sendNewEvent",{op:"MOVE_PARAGRAPH",data:{id:c,index:d}})}),a.$on("insertParagraph",function(b,c){for(var d=-1,f=0;f<a.note.paragraphs.length;f++)if(a.note.paragraphs[f].id===c){d=f+1;break}return d===a.note.paragraphs.length?void alert("Cannot insert after the last paragraph."):void(0>d||d>a.note.paragraphs.length||e.$emit("sendNewEvent",{op:"INSERT_PARAGRAPH",data:{index:d}}))}),a.$on("moveParagraphDown",function(b,c){for(var d=-1,f=0;f<a.note.paragraphs.length;f++)if(a.note.paragraphs[f].id===c){d=f+1;break}0>d||d>=a.note.paragraphs.length||e.$emit("sendNewEvent",{op:"MOVE_PARAGRAPH",data:{id:c,index:d}})}),a.$on("moveFocusToPreviousParagraph",function(b,c){for(var d=!1,e=a.note.paragraphs.length-1;e>=0;e--)if(d===!1){if(a.note.paragraphs[e].id===c){d=!0;continue}}else{var f=a.note.paragraphs[e];if(!f.config.hide&&!f.config.editorHide&&!f.config.tableHide){a.$broadcast("focusParagraph",a.note.paragraphs[e].id);break}}}),a.$on("moveFocusToNextParagraph",function(b,c){for(var d=!1,e=0;e<a.note.paragraphs.length;e++)if(d===!1){if(a.note.paragraphs[e].id===c){d=!0;continue}}else{var f=a.note.paragraphs[e];if(!f.config.hide&&!f.config.editorHide&&!f.config.tableHide){a.$broadcast("focusParagraph",a.note.paragraphs[e].id);break}}});var k=function(b){b.name!==a.note.name&&(console.log("change note name: %o to %o",a.note.name,b.name),a.note.name=b.name),a.note.config=b.config,a.note.info=b.info;var c=b.paragraphs.map(function(a){return a.id}),d=a.note.paragraphs.map(function(a){return a.id}),e=c.length,f=d.length;if(e>f)for(var g in c)if(d[g]!==c[g]){a.note.paragraphs.splice(g,0,b.paragraphs[g]);break}if(e===f)for(var h in c){var i=b.paragraphs[h];if(d[h]===c[h])a.$broadcast("updateParagraph",{paragraph:i});else{var j=d.indexOf(c[h]);a.note.paragraphs.splice(j,1),a.note.paragraphs.splice(h,0,i),d=a.note.paragraphs.map(function(a){return a.id})}}if(f>e)for(var k in d)if(d[k]!==c[k]){a.note.paragraphs.splice(k,1);break}},l=function(b){f.get(getRestApiBase()+"/notebook/interpreter/bind/"+a.note.id).success(function(c,d,e,f){a.interpreterBindings=c.body,a.interpreterBindingsOrig=jQuery.extend(!0,[],a.interpreterBindings),b&&b()}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)})},m=function(){var b=!1;for(var c in a.interpreterBindings){var d=a.interpreterBindings[c];if(d.selected){b=!0;break}}if(!b){var e={};for(var c in a.interpreterBindings){var d=a.interpreterBindings[c];e[d.group]||(d.selected=!0,e[d.group]=!0)}a.showSetting=!0}};a.interpreterSelectionListeners={accept:function(a,b){return!0},itemMoved:function(a){},orderChanged:function(a){}},a.openSetting=function(){a.showSetting=!0,l()},a.closeSetting=function(){if(n()){var b=confirm("Changes will be discarded");if(!b)return}a.showSetting=!1},a.saveSetting=function(){var b=[];for(var c in a.interpreterBindings){var d=a.interpreterBindings[c];d.selected&&b.push(d.id)}f.put(getRestApiBase()+"/notebook/interpreter/bind/"+a.note.id,b).success(function(c,d,e,f){console.log("Interpreter binding %o saved",b),a.showSetting=!1}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)})},a.toggleSetting=function(){a.showSetting?a.closeSetting():a.openSetting()};var n=function(){return angular.equals(a.interpreterBindings,a.interpreterBindingsOrig)?!1:!0};a.$on("angularObjectUpdate",function(b,d){if(d.noteId===a.note.id){var f=e.compiledScope,h=d.angularObject.name;if(angular.equals(d.angularObject.object,f[h]))return;g[h]||(g[h]={interpreterGroupId:d.interpreterGroupId}),g[h].skipEmit=!0,g[h].clearWatcher||(g[h].clearWatcher=f.$watch(h,function(a,b){return g[h].skipEmit?void(g[h].skipEmit=!1):void e.$emit("sendNewEvent",{op:"ANGULAR_OBJECT_UPDATED",data:{noteId:c.noteId,name:h,value:a,interpreterGroupId:g[h].interpreterGroupId}})})),f[h]=d.angularObject.object}})}]),angular.module("zeppelinWebApp").controller("InterpreterCtrl",["$scope","$route","$routeParams","$location","$rootScope","$http",function(a,b,c,d,e,f){var g=function(a,b){var c={};for(var d in b.properties)c[d]={value:b.properties[d]};return{id:a,name:b.name,group:b.group,option:angular.copy(b.option),properties:c,interpreters:b.interpreterGroup}},h=function(){f.get(getRestApiBase()+"/interpreter/setting").success(function(b,c,d,e){var f=[];for(var h in b.body){var i=b.body[h];f.push(g(i.id,i))}a.interpreterSettings=f}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)})},i=function(){f.get(getRestApiBase()+"/interpreter").success(function(b,c,d,e){var f,g={};for(var h in b.body)f=b.body[h],g[f.group]||(g[f.group]=[]),g[f.group].push({name:f.name,className:f.className,properties:f.properties});a.availableInterpreters=g}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)})};a.copyOriginInterpreterSettingProperties=function(b){a.interpreterSettingProperties={};for(var c=0;c<a.interpreterSettings.length;c++){var d=a.interpreterSettings[c];if(d.id===b){angular.copy(d.properties,a.interpreterSettingProperties),angular.copy(d.option,a.interpreterSettingOption);break}}console.log("%o, %o",a.interpreterSettings[c],a.interpreterSettingProperties)},a.updateInterpreterSetting=function(b){var c=confirm("Do you want to update this interpreter and restart with new settings?");if(c){a.addNewInterpreterProperty(b);for(var d={option:{remote:!0},properties:{}},e=0;e<a.interpreterSettings.length;e++){var h=a.interpreterSettings[e];if(h.id===b){d.option=angular.copy(h.option);for(var i in h.properties)d.properties[i]=h.properties[i].value;break}}f.put(getRestApiBase()+"/interpreter/setting/"+b,d).success(function(c,d,e,f){for(var h=0;h<a.interpreterSettings.length;h++){var i=a.interpreterSettings[h];if(i.id===b){a.interpreterSettings.splice(h,1),a.interpreterSettings.splice(h,0,g(b,c.body));break}}}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)})}},a.resetInterpreterSetting=function(b){for(var c=0;c<a.interpreterSettings.length;c++){var d=a.interpreterSettings[c];if(d.id===b){angular.copy(a.interpreterSettingProperties,d.properties),angular.copy(a.interpreterSettingOption,d.option);break}}},a.removeInterpreterSetting=function(b){var c=confirm("Do you want to delete this interpreter setting?");c&&(console.log("Delete setting %o",b),f["delete"](getRestApiBase()+"/interpreter/setting/"+b).success(function(c,d,e,f){for(var g=0;g<a.interpreterSettings.length;g++){var h=a.interpreterSettings[g];if(h.id===b){a.interpreterSettings.splice(g,1);break}}}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)}))},a.newInterpreterGroupChange=function(){for(var b={},c=a.availableInterpreters[a.newInterpreterSetting.group],d=0;d<c.length;d++){var e=c[d];for(var f in e.properties)b[f]={value:e.properties[f].defaultValue,description:e.properties[f].description}}a.newInterpreterSetting.properties=b},a.restartInterpreterSetting=function(b){var c=confirm("Do you want to restart this interpreter?");c&&f.put(getRestApiBase()+"/interpreter/setting/restart/"+b).success(function(c,d,e,f){for(var h=0;h<a.interpreterSettings.length;h++){var i=a.interpreterSettings[h];if(i.id===b){a.interpreterSettings.splice(h,1),a.interpreterSettings.splice(h,0,g(b,c.body));break}}}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)})},a.addNewInterpreterSetting=function(){if(!a.newInterpreterSetting.name||!a.newInterpreterSetting.group)return void alert("Please determine name and interpreter");for(var b=0;b<a.interpreterSettings.length;b++){var c=a.interpreterSettings[b];if(c.name===a.newInterpreterSetting.name)return void alert("Name "+c.name+" already exists")}a.addNewInterpreterProperty();var d={name:a.newInterpreterSetting.name,group:a.newInterpreterSetting.group,option:angular.copy(a.newInterpreterSetting.option),properties:{}};for(var e in a.newInterpreterSetting.properties)d.properties[e]=a.newInterpreterSetting.properties[e].value;f.post(getRestApiBase()+"/interpreter/setting",d).success(function(b,c,d,e){a.resetNewInterpreterSetting(),h(),a.showAddNewSetting=!1}).error(function(a,b,c,d){console.log("Error %o %o",b,a.message)})},a.resetNewInterpreterSetting=function(){a.newInterpreterSetting={name:void 0,group:void 0,option:{remote:!0},properties:{}},a.newInterpreterSetting.propertyValue="",a.newInterpreterSetting.propertyKey=""},a.removeInterpreterProperty=function(b,c){if(void 0===c)delete a.newInterpreterSetting.properties[b];else for(var d=0;d<a.interpreterSettings.length;d++){var e=a.interpreterSettings[d];if(e.id===c){delete a.interpreterSettings[d].properties[b];break}}},a.addNewInterpreterProperty=function(b){if(void 0===b){if(!a.newInterpreterSetting.propertyKey||""===a.newInterpreterSetting.propertyKey)return;a.newInterpreterSetting.properties[a.newInterpreterSetting.propertyKey]={value:a.newInterpreterSetting.propertyValue},a.newInterpreterSetting.propertyValue="",a.newInterpreterSetting.propertyKey=""}else for(var c=0;c<a.interpreterSettings.length;c++){var d=a.interpreterSettings[c];if(d.id===b){if(!d.propertyKey||""===d.propertyKey)return;d.properties[d.propertyKey]={value:d.propertyValue},d.propertyValue="",d.propertyKey="";break}}};var j=function(){e.$emit("setLookAndFeel","default"),a.interpreterSettings=[],a.availableInterpreters={},a.resetNewInterpreterSetting(),h(),i()};j()}]),angular.module("zeppelinWebApp").directive("ngEnter",function(){return function(a,b,c){b.bind("keydown keypress",function(b){13===b.which&&(a.$apply(function(){a.$eval(c.ngEnter)}),b.preventDefault())})}}),angular.module("zeppelinWebApp").directive("dropdownInput",function(){return{restrict:"A",link:function(a,b){b.bind("click",function(a){a.stopPropagation()})}}}),angular.module("zeppelinWebApp").directive("resizable",function(){var a={autoHide:!0,handles:"se",helper:"resizable-helper",minHeight:100,grid:[1e4,10]};return{restrict:"A",scope:{callback:"&onResize"},link:function(b,c,d){d.$observe("allowresize",function(d){"true"===d&&(c.resizable(a),c.on("resizestop",function(){b.callback&&b.callback()}))})}}}),angular.module("zeppelinWebApp").controller("ParagraphCtrl",["$scope","$rootScope","$route","$window","$element","$routeParams","$location","$timeout","$compile",function(a,b,c,d,e,f,g,h,i){a.paragraph=null,a.editor=null;var j={scala:"ace/mode/scala",sql:"ace/mode/sql",markdown:"ace/mode/markdown"};a.init=function(b){a.paragraph=b,a.chart={},a.colWidthOption=[1,2,3,4,5,6,7,8,9,10,11,12],a.showTitleEditor=!1,a.paragraphFocused=!1,a.paragraph.config||(a.paragraph.config={}),k(),a.lastData||(a.lastData={}),"TABLE"===a.getResultType()?(a.lastData.settings=angular.copy(a.paragraph.settings),a.lastData.config=angular.copy(a.paragraph.config),a.loadTableData(a.paragraph.result),a.setGraphMode(a.getGraphMode(),!1,!1)):"HTML"===a.getResultType()?a.renderHtml():"ANGULAR"===a.getResultType()&&a.renderAngular()},a.renderHtml=function(){var b=function(){if($("#p"+a.paragraph.id+"_html").length)try{$("#p"+a.paragraph.id+"_html").html(a.paragraph.result.msg),$("#p"+a.paragraph.id+"_html").find("pre code").each(function(a,b){hljs.highlightBlock(b)})}catch(c){console.log("HTML rendering error %o",c)}else h(b,10)};h(b)},a.renderAngular=function(){var c=function(){if(angular.element("#p"+a.paragraph.id+"_angular").length)try{angular.element("#p"+a.paragraph.id+"_angular").html(a.paragraph.result.msg),i(angular.element("#p"+a.paragraph.id+"_angular").contents())(b.compiledScope)}catch(d){console.log("ANGULAR rendering error %o",d)}else h(c,10)};h(c)};var k=function(){var b=a.paragraph.config;b.colWidth||(b.colWidth=12),b.graph||(b.graph={}),b.graph.mode||(b.graph.mode="table"),b.graph.height||(b.graph.height=300),b.graph.optionOpen||(b.graph.optionOpen=!1),b.graph.keys||(b.graph.keys=[]),b.graph.values||(b.graph.values=[]),b.graph.groups||(b.graph.groups=[]),b.graph.scatter||(b.graph.scatter={})};a.getIframeDimensions=function(){if(a.asIframe){var b="#"+f.paragraphId+"_container",c=$(b).height();return c}return 0},a.$watch(a.getIframeDimensions,function(b,c){if(a.asIframe&&b){var e={};e.height=b,e.url=g.$$absUrl,d.parent.postMessage(angular.toJson(e),"*")}}),a.$on("updateParagraph",function(b,c){if(!(c.paragraph.id!==a.paragraph.id||c.paragraph.dateCreated===a.paragraph.dateCreated&&c.paragraph.dateFinished===a.paragraph.dateFinished&&c.paragraph.dateStarted===a.paragraph.dateStarted&&c.paragraph.status===a.paragraph.status&&c.paragraph.jobName===a.paragraph.jobName&&c.paragraph.title===a.paragraph.title&&c.paragraph.errorMessage===a.paragraph.errorMessage&&angular.equals(c.paragraph.settings,a.lastData.settings)&&angular.equals(c.paragraph.config,a.lastData.config))){a.lastData.settings=angular.copy(c.paragraph.settings),a.lastData.config=angular.copy(c.paragraph.config);var d=a.getResultType(),e=a.getResultType(c.paragraph),f=a.getGraphMode(),g=a.getGraphMode(c.paragraph),h=c.paragraph.dateFinished!==a.paragraph.dateFinished;a.paragraph.text!==c.paragraph.text&&(a.dirtyText?a.dirtyText===c.paragraph.text?(a.paragraph.text=c.paragraph.text,a.dirtyText=void 0):a.paragraph.text=a.dirtyText:a.paragraph.text=c.paragraph.text),a.paragraph.aborted=c.paragraph.aborted,a.paragraph.dateCreated=c.paragraph.dateCreated,a.paragraph.dateFinished=c.paragraph.dateFinished,a.paragraph.dateStarted=c.paragraph.dateStarted,a.paragraph.errorMessage=c.paragraph.errorMessage,a.paragraph.jobName=c.paragraph.jobName,a.paragraph.title=c.paragraph.title,a.paragraph.status=c.paragraph.status,a.paragraph.result=c.paragraph.result,a.paragraph.settings=c.paragraph.settings,a.asIframe?(c.paragraph.config.editorHide=!0,c.paragraph.config.tableHide=!1,a.paragraph.config=c.paragraph.config):(a.paragraph.config=c.paragraph.config,k()),"TABLE"===e?(a.loadTableData(a.paragraph.result),("TABLE"!==d||h)&&(q(),r()),f!==g?a.setGraphMode(g,!1,!1):a.setGraphMode(g,!1,!0)):"HTML"===e?a.renderHtml():"ANGULAR"===e&&a.renderAngular()}}),a.isRunning=function(){return"RUNNING"===a.paragraph.status||"PENDING"===a.paragraph.status?!0:!1},a.cancelParagraph=function(){console.log("Cancel %o",a.paragraph.id);var c={op:"CANCEL_PARAGRAPH",data:{id:a.paragraph.id}};b.$emit("sendNewEvent",c)},a.runParagraph=function(c){var d={op:"RUN_PARAGRAPH",data:{id:a.paragraph.id,title:a.paragraph.title,paragraph:c,config:a.paragraph.config,params:a.paragraph.settings.params}};b.$emit("sendNewEvent",d)},a.moveUp=function(){a.$emit("moveParagraphUp",a.paragraph.id)},a.moveDown=function(){a.$emit("moveParagraphDown",a.paragraph.id)},a.insertNew=function(){a.$emit("insertParagraph",a.paragraph.id)},a.removeParagraph=function(){var c=confirm("Do you want to delete this paragraph?");if(c){console.log("Remove paragraph");var d={op:"PARAGRAPH_REMOVE",data:{id:a.paragraph.id}};b.$emit("sendNewEvent",d)}},a.toggleEditor=function(){a.paragraph.config.editorHide?a.openEditor():a.closeEditor()},a.closeEditor=function(){console.log("close the note");var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);c.editorHide=!0,n(a.paragraph.title,a.paragraph.text,c,b)},a.openEditor=function(){console.log("open the note");var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);c.editorHide=!1,n(a.paragraph.title,a.paragraph.text,c,b)},a.closeTable=function(){console.log("close the output");var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);c.tableHide=!0,n(a.paragraph.title,a.paragraph.text,c,b)},a.openTable=function(){console.log("open the output");var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);c.tableHide=!1,n(a.paragraph.title,a.paragraph.text,c,b)},a.showTitle=function(){var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);c.title=!0,n(a.paragraph.title,a.paragraph.text,c,b)},a.hideTitle=function(){var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);c.title=!1,n(a.paragraph.title,a.paragraph.text,c,b)},a.setTitle=function(){var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);n(a.paragraph.title,a.paragraph.text,c,b)},a.columnWidthClass=function(b){return a.asIframe?"col-md-12":"col-md-"+b},a.changeColWidth=function(){var b=angular.copy(a.paragraph.settings.params),c=angular.copy(a.paragraph.config);n(a.paragraph.title,a.paragraph.text,c,b)},a.toggleGraphOption=function(){var b=angular.copy(a.paragraph.config);b.graph.optionOpen?b.graph.optionOpen=!1:b.graph.optionOpen=!0;var c=angular.copy(a.paragraph.settings.params);n(a.paragraph.title,a.paragraph.text,b,c)},a.toggleOutput=function(){var b=angular.copy(a.paragraph.config);b.tableHide=!b.tableHide;var c=angular.copy(a.paragraph.settings.params);n(a.paragraph.title,a.paragraph.text,b,c)},a.loadForm=function(b,c){var d=b.defaultValue;c[b.name]&&(d=c[b.name]),""===d&&(d=b.options[0].value),a.paragraph.settings.params[b.name]=d},a.aceChanged=function(){a.dirtyText=a.editor.getSession().getValue()},a.aceLoaded=function(c){var d=ace.require("ace/ext/language_tools"),e=ace.require("ace/range").Range;if(a.editor=c,"{{paragraph.id}}_editor"!==c.container.id){a.editor.renderer.setShowGutter(!1),a.editor.setHighlightActiveLine(!1),a.editor.setTheme("ace/theme/github"),a.editor.focus();var f=a.editor.getSession().getScreenLength()*a.editor.renderer.lineHeight+a.editor.renderer.scrollBar.getWidth();l(c.container.id,f),a.editor.getSession().setUseWrapMode(!0),-1!==navigator.appVersion.indexOf("Mac")?a.editor.setKeyboardHandler("ace/keyboard/emacs"):-1!==navigator.appVersion.indexOf("Win")||-1!==navigator.appVersion.indexOf("X11")||-1!==navigator.appVersion.indexOf("Linux"),a.editor.setOptions({enableBasicAutocompletion:!0,enableSnippets:!1,enableLiveAutocompletion:!1});var g={getCompletions:function(c,d,f,g,h){if(a.editor.isFocused()){var i=d.getTextRange(new e(0,0,f.row,f.column));b.$emit("sendNewEvent",{op:"COMPLETION",data:{id:a.paragraph.id,buf:i,cursor:i.length}}),a.$on("completionList",function(a,b){if(b.completions){var c=[];for(var d in b.completions){var e=b.completions[d];c.push({name:e,value:e,score:300})}h(null,c)}})}}};d.addCompleter(g),a.handleFocus=function(b){a.paragraphFocused=b,h(function(){a.$digest()})},a.editor.on("focus",function(){a.handleFocus(!0)}),a.editor.on("blur",function(){a.handleFocus(!1)}),a.editor.getSession().on("change",function(b,d){f=d.getScreenLength()*a.editor.renderer.lineHeight+a.editor.renderer.scrollBar.getWidth(),l(c.container.id,f),a.editor.resize()});var i=a.editor.getSession().getValue();String(i).startsWith("%sql")?a.editor.getSession().setMode(j.sql):String(i).startsWith("%md")?a.editor.getSession().setMode(j.markdown):a.editor.getSession().setMode(j.scala),a.editor.commands.addCommand({name:"run",bindKey:{win:"Shift-Enter",mac:"Shift-Enter"},exec:function(b){var c=b.getValue();c&&a.runParagraph(c)},readOnly:!1}),a.editor.commands.bindKey("ctrl-.","startAutocomplete"),a.editor.commands.bindKey("ctrl-space",null),a.editor.keyBinding.origOnCommandKey=a.editor.keyBinding.onCommandKey,a.editor.keyBinding.onCommandKey=function(b,c,d){if(a.editor.completer&&a.editor.completer.activated);else{var e,f;38===d||80===d&&b.ctrlKey?(e=a.editor.getSession().getLength(),f=a.editor.getCursorPosition().row,0===f&&a.$emit("moveFocusToPreviousParagraph",a.paragraph.id)):(40===d||78===d&&b.ctrlKey)&&(e=a.editor.getSession().getLength(),f=a.editor.getCursorPosition().row,f===e-1&&a.$emit("moveFocusToNextParagraph",a.paragraph.id))}this.origOnCommandKey(b,c,d)}}};var l=function(a,b){$("#"+a).height(b.toString()+"px")};a.getEditorValue=function(){return a.editor.getValue()},a.getProgress=function(){return a.currentProgress?a.currentProgress:0},a.getExecutionTime=function(){var b=a.paragraph,c=Date.parse(b.dateFinished)-Date.parse(b.dateStarted);return isNaN(c)||0>c?"&nbsp;":"Took "+c/1e3+" seconds"},a.$on("updateProgress",function(b,c){c.id===a.paragraph.id&&(a.currentProgress=c.progress)}),a.$on("focusParagraph",function(b,c){a.paragraph.id===c&&(a.editor.focus(),$("body").scrollTo("#"+c+"_editor",300,{offset:-60}))}),a.$on("runParagraph",function(b){a.runParagraph(a.editor.getValue())}),a.$on("openEditor",function(b){a.openEditor()}),a.$on("closeEditor",function(b){a.closeEditor()}),a.$on("openTable",function(b){a.openTable()}),a.$on("closeTable",function(b){a.closeTable()}),a.getResultType=function(b){var c=b?b:a.paragraph;return c.result&&c.result.type?c.result.type:"TEXT"},a.getBase64ImageSrc=function(a){return"data:image/png;base64,"+a},a.getGraphMode=function(b){var c=b?b:a.paragraph;return c.config.graph&&c.config.graph.mode?c.config.graph.mode:"table"},a.loadTableData=function(a){if(a&&"TABLE"===a.type){var b=[],c=[],d=[],e=a.msg.split("\n");a.comment="";for(var f=!1,g=0;g<e.length;g++){var h=e[g];if(f)a.comment+=h;else if(""!==h){for(var i=h.split("	"),j=[],k=[],l=0;l<i.length;l++){var m=i[l];0===g?b.push({name:m,index:l,aggr:"sum"}):(j.push(m),k.push({key:b[g]?b[g].name:void 0,value:m}))}0!==g&&(c.push(j),d.push(k))}else c.length>0&&(f=!0)}a.msgTable=d,a.columnNames=b,a.rows=c}},a.setGraphMode=function(b,c,d){if(c)m(b);else{q();var e=a.paragraph.config.graph.height;$("#p"+a.paragraph.id+"_graph").height(e),b&&"table"!==b?p(b,a.paragraph.result,d):o(a.paragraph.result,d)}};var m=function(b){var c=angular.copy(a.paragraph.config),d=angular.copy(a.paragraph.settings.params);c.graph.mode=b,n(a.paragraph.title,a.paragraph.text,c,d)},n=function(c,d,e,f){var g={op:"COMMIT_PARAGRAPH",data:{id:a.paragraph.id,title:c,paragraph:d,params:f,config:e}};b.$emit("sendNewEvent",g)},o=function(b,c,d){var e=function(a){return isNaN(a)&&a.length>"%html".length&&"%html "===a.substring(0,"%html ".length)?"html":""},f=function(a){if(isNaN(a)){var b=e(a);return""!==b?a.substring(b.length+2):a}var c=a.toString(),d=c.split("."),f=d[0].replace(/(\d)(?=(\d{3})+(?!\d))/g,"$1,");return d.length>1&&(f+="."+d[1]),f},g=function(){var b="";b+='<table class="table table-hover table-condensed">',b+="  <thead>",b+='    <tr style="background-color: #F6F6F6; font-weight: bold;">';for(var c in a.paragraph.result.columnNames)b+="<th>"+a.paragraph.result.columnNames[c].name+"</th>";b+="    </tr>",b+="  </thead>";for(var d in a.paragraph.result.msgTable){var g=a.paragraph.result.msgTable[d];b+="    <tr>";for(var h in g){var i=g[h].value;"html"!==e(i)&&(i=i.replace(/[\u00A0-\u9999<>\&]/gim,function(a){return"&#"+a.charCodeAt(0)+";"})),b+="      <td>"+f(i)+"</td>"}b+="    </tr>"}b+="</table>",$("#p"+a.paragraph.id+"_table").html(b),$("#p"+a.paragraph.id+"_table").perfectScrollbar();var j=a.paragraph.config.graph.height;$("#p"+a.paragraph.id+"_table").height(j)},i=function(){if($("#p"+a.paragraph.id+"_table").length)try{g()}catch(b){console.log("Chart drawing error %o",b)}else h(i,10)};h(i)},p=function(b,c,d){if(!a.chart[b]){var e=nv.models[b]();a.chart[b]=e}var f=[];if("scatterChart"===b){var g=v(c,d),i=g.xLabels,j=g.yLabels;f=g.d3g,a.chart[b].xAxis.tickFormat(function(a){return!i[a]||!isNaN(parseFloat(i[a]))&&isFinite(i[a])?a:i[a]}),a.chart[b].yAxis.tickFormat(function(a){return!j[a]||!isNaN(parseFloat(j[a]))&&isFinite(j[a])?a:j[a]}),a.chart[b].tooltipContent(function(b,c,d,e){var f="<h3>"+b+"</h3>";return a.paragraph.config.graph.scatter.size&&a.isValidSizeOption(a.paragraph.config.graph.scatter,a.paragraph.result.rows)&&(f+="<p>"+e.point.size+"</p>"),f}),a.chart[b].showDistX(!0).showDistY(!0).scatter.useVoronoi(!1)}else{var k=s(c);if("pieChart"===b){var l=t(k,!0).d3g;if(a.chart[b].x(function(a){return a.label}).y(function(a){return a.value}),l.length>0)for(var m=0;m<l[0].values.length;m++){var n=l[0].values[m];f.push({label:n.x,value:n.y})}}else if("multiBarChart"===b)f=t(k,!0,!1,b).d3g,a.chart[b].yAxis.axisLabelDistance(50);else if("lineChart"===b||"stackedAreaChart"===b){var o=t(k,!1,!0),i=o.xLabels;f=o.d3g,a.chart[b].xAxis.tickFormat(function(a){return!i[a]||!isNaN(parseFloat(i[a]))&&isFinite(i[a])?a:i[a]}),a.chart[b].yAxis.axisLabelDistance(50),a.chart[b].useInteractiveGuideline(!0),a.chart[b].forceY([0])}}var p=function(){var c=a.paragraph.config.graph.height,d=300,e=150;try{f[0].values.length>e&&(d=0)}catch(g){}d3.select("#p"+a.paragraph.id+"_"+b+" svg").attr("height",a.paragraph.config.graph.height).style("height",c+"px").datum(f).transition().duration(d).call(a.chart[b]);nv.utils.windowResize(a.chart[b].update)},q=function(){if(0!==$("#p"+a.paragraph.id+"_"+b+" svg").length)try{p()}catch(c){console.log("Chart drawing error %o",c)}else h(q,10)};h(q)};a.isGraphMode=function(b){return"TABLE"===a.getResultType()&&a.getGraphMode()===b?!0:!1},a.onGraphOptionChange=function(){q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeGraphOptionKeys=function(b){a.paragraph.config.graph.keys.splice(b,1),q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeGraphOptionValues=function(b){a.paragraph.config.graph.values.splice(b,1),q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeGraphOptionGroups=function(b){a.paragraph.config.graph.groups.splice(b,1),q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.setGraphOptionValueAggr=function(b,c){a.paragraph.config.graph.values[b].aggr=c,q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeScatterOptionXaxis=function(b){a.paragraph.config.graph.scatter.xAxis=null,q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeScatterOptionYaxis=function(b){a.paragraph.config.graph.scatter.yAxis=null,q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeScatterOptionGroup=function(b){a.paragraph.config.graph.scatter.group=null,q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)},a.removeScatterOptionSize=function(b){a.paragraph.config.graph.scatter.size=null,q(),a.setGraphMode(a.paragraph.config.graph.mode,!0,!1)};var q=function(){var b=function(a){for(var b=0;b<a.length;b++)for(var c=b+1;c<a.length;c++)angular.equals(a[b],a[c])&&a.splice(c,1)},c=function(b){for(var c=0;c<b.length;c++){for(var d=!1,e=0;e<a.paragraph.result.columnNames.length;e++){var f=b[c],g=a.paragraph.result.columnNames[e];if(f.index===g.index&&f.name===g.name){
d=!0;break}}d||b.splice(c,1)}},d=function(b){for(var c in b)if(b[c]){for(var d=!1,e=0;e<a.paragraph.result.columnNames.length;e++){var f=b[c],g=a.paragraph.result.columnNames[e];if(f.index===g.index&&f.name===g.name){d=!0;break}}d||(b[c]=null)}};b(a.paragraph.config.graph.keys),c(a.paragraph.config.graph.keys),c(a.paragraph.config.graph.values),b(a.paragraph.config.graph.groups),c(a.paragraph.config.graph.groups),d(a.paragraph.config.graph.scatter)},r=function(){0===a.paragraph.config.graph.keys.length&&a.paragraph.result.columnNames.length>0&&a.paragraph.config.graph.keys.push(a.paragraph.result.columnNames[0]),0===a.paragraph.config.graph.values.length&&a.paragraph.result.columnNames.length>1&&a.paragraph.config.graph.values.push(a.paragraph.result.columnNames[1]),a.paragraph.config.graph.scatter.xAxis||a.paragraph.config.graph.scatter.yAxis||(a.paragraph.result.columnNames.length>1?(a.paragraph.config.graph.scatter.xAxis=a.paragraph.result.columnNames[0],a.paragraph.config.graph.scatter.yAxis=a.paragraph.result.columnNames[1]):1===a.paragraph.result.columnNames.length&&(a.paragraph.config.graph.scatter.xAxis=a.paragraph.result.columnNames[0]))},s=function(b){for(var c=a.paragraph.config.graph.keys,d=a.paragraph.config.graph.groups,e=a.paragraph.config.graph.values,f={sum:function(a,b){var c=void 0!==a?isNaN(a)?1:parseFloat(a):0,d=void 0!==b?isNaN(b)?1:parseFloat(b):0;return c+d},count:function(a,b){var c=void 0!==a?parseInt(a):0,d=void 0!==b?1:0;return c+d},min:function(a,b){var c=void 0!==a?isNaN(a)?1:parseFloat(a):0,d=void 0!==b?isNaN(b)?1:parseFloat(b):0;return Math.min(c,d)},max:function(a,b){var c=void 0!==a?isNaN(a)?1:parseFloat(a):0,d=void 0!==b?isNaN(b)?1:parseFloat(b):0;return Math.max(c,d)},avg:function(a,b,c){var d=void 0!==a?isNaN(a)?1:parseFloat(a):0,e=void 0!==b?isNaN(b)?1:parseFloat(b):0;return d+e}},g={sum:!1,count:!1,min:!1,max:!1,avg:!0},h={},i={},j=0;j<b.rows.length;j++){for(var k=b.rows[j],l=h,m=i,n=0;n<c.length;n++){var o=c[n];l[o.name]||(l[o.name]={order:n,index:o.index,type:"key",children:{}}),l=l[o.name].children;var p=k[o.index];m[p]||(m[p]={}),m=m[p]}for(var q=0;q<d.length;q++){var r=d[q],s=k[r.index];l[s]||(l[s]={order:q,index:r.index,type:"group",children:{}}),l=l[s].children,m[s]||(m[s]={}),m=m[s]}for(var t=0;t<e.length;t++){var u=e[t],v=u.name+"("+u.aggr+")";l[v]||(l[v]={type:"value",order:t,index:u.index}),m[v]?m[v]={value:f[u.aggr](m[v].value,k[u.index],m[v].count+1),count:g[u.aggr]?m[v].count+1:m[v].count}:m[v]={value:"count"!==u.aggr?k[u.index]:1,count:1}}}return{schema:h,rows:i}},t=function(b,c,d,e){var f=[],g=b.schema,h=b.rows,i=a.paragraph.config.graph.values,j=function(a,b){return a?a+"."+b:b},k=function(a,b){for(var c in a.children)b[c]={},k(a.children[c],b[c])},l=function(a,b,c,e,f,g,h,i){"key"===b.type?(g=j(g,a),h=j(h,c)):"group"===b.type?i=j(i,c):("value"===b.type&&a===c||o)&&(i=j(i,c),f(g,h,i,e));for(var m in b.children)if(d&&"group"===b.children[m].type&&void 0===e[m]){var n={};k(b.children[m],n),l(m,b.children[m],m,n,f,g,h,i)}else for(var p in e)("key"===b.children[m].type||m===p)&&l(m,b.children[m],p,e[p],f,g,h,i)},m=a.paragraph.config.graph.keys,n=a.paragraph.config.graph.groups,i=a.paragraph.config.graph.values,o=0===m.length&&0===n.length&&i.length>0,p=0===m.length,q="multiBarChart"===e,r=Object.keys(g)[0],s={},t=0,u={},v=0,w={};for(var x in h)l(r,g[r],x,h[x],function(a,b,d,e){void 0===s[b]&&(w[t]=b,s[b]=t++),void 0===u[d]&&(u[d]=v++);var g=u[d];p&&q&&(g=0),f[g]||(f[g]={values:[],key:p&&q?"values":d});var h=isNaN(b)?c?b:s[b]:parseFloat(b),i=0;void 0===h&&(h=d),void 0!==e&&(i=isNaN(e.value)?0:parseFloat(e.value)/parseFloat(e.count)),f[g].values.push({x:h,y:i})});var y={};for(var z in u){var A=z.substring(0,z.lastIndexOf("("));y[A]?y[A]++:y[A]=1}if(o)for(var B=0;B<f[0].values.length;B++){var z=f[0].values[B].x;if(z){var A=z.substring(0,z.lastIndexOf("("));y[A]<=1&&(f[0].values[B].x=A)}}else{for(var C=0;C<f.length;C++){var z=f[C].key,A=z.substring(0,z.lastIndexOf("("));y[A]<=1&&(f[C].key=A)}if(1===n.length&&1===i.length)for(C=0;C<f.length;C++){var z=f[C].key;z=z.substring(0,z.lastIndexOf(".")),f[C].key=z}}return{xLabels:w,d3g:f}},u=function(b){for(var c,d,e,f=a.paragraph.config.graph.scatter.xAxis,g=a.paragraph.config.graph.scatter.yAxis,h=a.paragraph.config.graph.scatter.group,i={},j=0;j<b.rows.length;j++){var k=b.rows[j];f&&(c=k[f.index]),g&&(d=k[g.index]),h&&(e=k[h.index]);var l=c+","+d+","+e;i[l]?i[l].size++:i[l]={x:c,y:d,group:e,size:1}}var m=[];for(var n in i){var o=[];f&&(o[f.index]=i[n].x),g&&(o[g.index]=i[n].y),h&&(o[h.index]=i[n].group),o[b.rows[0].length]=i[n].size,m.push(o)}return m},v=function(b,c){var d,e,f,g=a.paragraph.config.graph.scatter.xAxis,h=a.paragraph.config.graph.scatter.yAxis,i=a.paragraph.config.graph.scatter.group,j=a.paragraph.config.graph.scatter.size,k=[],l=[],m={},n=[],o={},p={},q={},r={},s={},t={},v=0,x=0,y=0,z="";if(!g&&!h)return{d3g:[]};for(var A=0;A<b.rows.length;A++)f=b.rows[A],g&&(d=f[g.index],k[A]=d),h&&(e=f[h.index],l[A]=e);var B=g&&h&&w(k)&&w(l)||!g&&w(l)||!h&&w(k);for(m=B?u(b):b.rows,!i&&B?z="count":i||j?!i&&j&&(z=j.name):g&&h?z="("+g.name+", "+h.name+")":g&&!h?z=g.name:!g&&h&&(z=h.name),A=0;A<m.length;A++){f=m[A],g&&(d=f[g.index]),h&&(e=f[h.index]),i&&(z=f[i.index]);var C=B?f[f.length-1]:j?f[j.index]:1;void 0===q[z]&&(t[y]=z,q[z]=y++),g&&void 0===o[d]&&(r[v]=d,o[d]=v++),h&&void 0===p[e]&&(s[x]=e,p[e]=x++),n[q[z]]||(n[q[z]]={key:z,values:[]}),n[q[z]].values.push({x:g?isNaN(d)?o[d]:parseFloat(d):0,y:h?isNaN(e)?p[e]:parseFloat(e):0,size:isNaN(parseFloat(C))?1:parseFloat(C)})}return{xLabels:r,yLabels:s,d3g:n}},w=function(a){for(var b=function(a){for(var b={},c=[],d=0,e=0;e<a.length;e++){var f=a[e];1!==b[f]&&(b[f]=1,c[d++]=f)}return c},c=0;c<a.length;c++)if(isNaN(parseFloat(a[c]))&&("string"==typeof a[c]||a[c]instanceof String))return!0;var d=.05,e=b(a);return e.length/a.length<d?!0:!1};a.isValidSizeOption=function(a,b){for(var c=[],d=[],e=0;e<b.length;e++){var f=b[e],g=f[a.size.index];if(isNaN(parseFloat(g))||!isFinite(g))return!1;if(a.xAxis){var h=f[a.xAxis.index];c[e]=h}if(a.yAxis){var i=f[a.yAxis.index];d[e]=i}}var j=a.xAxis&&a.yAxis&&w(c)&&w(d)||!a.xAxis&&w(d)||!a.yAxis&&w(c);return j?!1:!0},a.setGraphHeight=function(){var b=$("#p"+a.paragraph.id+"_graph").height(),c=angular.copy(a.paragraph.settings.params),d=angular.copy(a.paragraph.config);d.graph.height=b,n(a.paragraph.title,a.paragraph.text,d,c)},"function"!=typeof String.prototype.startsWith&&(String.prototype.startsWith=function(a){return this.slice(0,a.length)===a}),a.goToSingleParagraph=function(){var b=c.current.pathParams.noteId,e=location.protocol+"//"+location.host+"/#/notebook/"+b+"/paragraph/"+a.paragraph.id+"?asIframe";d.open(e)}}]),angular.module("zeppelinWebApp").controller("NavCtrl",["$scope","$rootScope","$routeParams",function(a,b,c){a.notes=[],$("#notebook-list").perfectScrollbar({suppressScrollX:!0}),a.$on("setNoteMenu",function(b,c){a.notes=c});var d=function(){b.$emit("sendNewEvent",{op:"LIST_NOTES"})};d(),a.createNewNote=function(){b.$emit("sendNewEvent",{op:"NEW_NOTE"})},a.isActive=function(a){return c.noteId===a?!0:!1}}]),angular.module("zeppelinWebApp").directive("ngDelete",function(){return function(a,b,c){b.bind("keydown keyup",function(b){(27===b.which||46===b.which)&&(a.$apply(function(){a.$eval(c.ngEnter)}),b.preventDefault())})}}),angular.module("zeppelinWebApp").directive("popoverHtmlUnsafePopup",function(){return{restrict:"EA",replace:!0,scope:{title:"@",content:"@",placement:"@",animation:"&",isOpen:"&"},templateUrl:"views/popover-html-unsafe-popup.html"}}).directive("popoverHtmlUnsafe",["$tooltip",function(a){return a("popoverHtmlUnsafe","popover","click")}]);