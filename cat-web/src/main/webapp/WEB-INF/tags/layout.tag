<%@ tag trimDirectiveWhitespaces="true"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="layout" class="org.unidal.cat.web.layout.entity.Layout" scope="request" />
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta charset="utf-8">
		<title>CAT</title>
		<link rel="stylesheet" type="text/css" href="${model.webapp}/assets/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="${model.webapp}/assets/css/font-awesome.min.css">
		<link rel="stylesheet" type="text/css" href="${model.webapp}/assets/css/jquery-ui.min.css">
		<link rel="stylesheet" type="text/css" href="${model.webapp}/assets/css/ace-fonts.css">
		<link rel="stylesheet" type="text/css" href="${model.webapp}/assets/css/ace.min.css" id="main-ace-style">
		<link rel="stylesheet" type="text/css" href="${model.webapp}/assets/css/ace-skins.min.css">
		<link rel="stylesheet" type="text/css" href="${model.webapp}/assets/css/ace-rtl.min.css">
		<link rel="stylesheet" type="text/css" href="${model.webapp}/css/body.css">
		<script type="text/javascript" src='${model.webapp}/assets/js/jquery.min.js'> </script>
		<script type="text/javascript" src="${model.webapp}/assets/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${model.webapp}/assets/js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="${model.webapp}/assets/js/jquery.ui.touch-punch.min.js"></script>
		<script type="text/javascript" src="${model.webapp}/assets/js/ace.min.js"></script>
		<script type="text/javascript" src="${model.webapp}/assets/js/ace-elements.min.js"></script>
		<script type="text/javascript" src="${model.webapp}/assets/js/ace-extra.min.js"></script>
		<script type="text/javascript" src="${model.webapp}/js/highcharts.js"></script>
	</head>
	<body class="no-skin">
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>

			<div class="navbar-container" id="navbar-container">
				<!-- #section:basics/sidebar.mobile.toggle -->
				<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
					<span class="sr-only">Toggle sidebar</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>

				<!-- /section:basics/sidebar.mobile.toggle -->
				<div class="navbar-header pull-left">
					<!-- #section:basics/navbar.layout.brand -->
					<a href="/cat/r/home"  class="navbar-brand">
						<span>CAT</span>
						<small style="font-size:65%">(Central Application Tracking)</small>
					</a>
					
				</div>
				<!-- #section:basics/navbar.dropdown -->
				<div class="navbar-buttons navbar-header pull-right" role="navigation">
				<ul class="nav ace-nav" style="height:auto;">
					<li class="light-blue">
						<a href="http://github.com/dianping/cat/" target="_blank">
							<i class="ace-icon glyphicon glyphicon-star"></i>
							<span>Star</span>
						</a>
					</li>
					<li class="light-blue" >
						<a data-toggle="dropdown" href="#" class="dropdown-toggle">
							<span class="user-info">
								<span id="loginInfo" ></span>
							</span>
							<i class="ace-icon fa fa-caret-down"></i>
						</a>
						<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
							<li>
								<a href="/cat/s/login?op=logout" ><i class="ace-icon fa fa-power-off"></i>
								注销</a>
							</li>
						</ul>
					</li>
				</ul>
				</div> 
			</div>
		</div>
		
		<div class="main-container" id="main-container">
		<script type="text/javascript">
			try{ace.settings.check('main-container' , 'fixed')}catch(e){}
		</script>
		<div id="sidebar" class="sidebar   responsive">
			<script type="text/javascript">
				try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
			</script>
			<div class="sidebar-shortcuts" id="sidebar-shortcuts">
				<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
					<c:forEach var="menu" items="${layout.menus}"> 
						<button class="${menu.clazz}" id="${menu.id}">
								<i class="${menu.icon}"></i>${menu.title}
						</button>
					</c:forEach>
				</div>
				<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
					<c:forEach var="menu" items="${layout.menus}"> 
						<span class="${menu.clazz}"></span>
					</c:forEach>
				</div>
			</div>
			<ul class="nav nav-list" style="top: 0px;">
				<c:forEach var="menu" items="${layout.menus}"> 
					<c:if test="${menu.active}">
						<c:forEach var="group" items="${menu.groups}">
							<c:choose>
								<c:when test="${group.standalone}">
									<c:forEach var="menuBar" items="${group.menuBars}">
										<c:choose>
											<c:when test="${menuBar.active}">
												<li id="${menuBar.id}" class="active">
											</c:when>
											<c:otherwise>
												<li id="${menuBar.id}">
											</c:otherwise>
										</c:choose>
											<a href="${menuBar.url}">
											<i class="menu-icon fa fa-caret-right"></i>${menuBar.title}</a>
											<b class="arrow"></b>
										</li>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${group.active}">
											<li id="${group.id}" class="hsub active">
										</c:when>
										<c:otherwise>
											<li id="${group.id}" class="hsub">
										</c:otherwise>
									</c:choose>
										<a href="#" class="dropdown-toggle"> <i class="${group.icon}"></i> <span class="menu-text">${group.title}</span>
											<b class="arrow fa fa-angle-down"></b>
										</a> 
										<b class="arrow"></b>
										<ul class="submenu">
											<c:forEach var="menuBar" items="${group.menuBars}">
												<c:choose>
													<c:when test="${menuBar.active}">
														<li id="${menuBar.id}" class="active">
													</c:when>
													<c:otherwise>
														<li id="${menuBar.id}">
													</c:otherwise>
												</c:choose>
													<a href="${menuBar.url}">
													<i class="menu-icon fa fa-caret-right"></i>${menuBar.title}</a>
													<b class="arrow"></b>
												</li>
											</c:forEach>
										</ul>
									</li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>	
				</c:forEach>
			</ul>
			<!-- #section:basics/sidebar.layout.minimize -->
			<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
				<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
			</div>

			<!-- /section:basics/sidebar.layout.minimize -->
			<script type="text/javascript">
				try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
			</script>
		</div>
		<div class="main-content">
				<div id="dialog-message" class="hide">
				<p>
					你确定要删除吗？(不可恢复)
				</p>
			</div>
				<div style="padding-top:2px;padding-left:2px;padding-right:8px;">
				<jsp:doBody/>
				</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	jQuery(function($) {
		//override dialog's title function to allow for HTML titles
		$.widget("ui.dialog", $.extend({}, $.ui.dialog.prototype, {
			_title: function(title) {
				var $title = this.options.title || '&nbsp;'
				if( ("title_html" in this.options) && this.options.title_html == true )
					title.html($title);
				else title.text($title);
			}
		}));
	
		$(".delete").on('click', function(e) {
			e.preventDefault();
			var anchor = this;
			var dialog = $( "#dialog-message" ).removeClass('hide').dialog({
				modal: true,
				title: "<div class='widget-header widget-header-small'><h4 class='smaller'><i class='ace-icon fa fa-check'></i>CAT提示</h4></div>",
				title_html: true,
				buttons: [ 
					{
						text: "Cancel",
						"class" : "btn btn-xs",
						click: function() {
							$( this ).dialog( "close" ); 
						} 
					},
					{
						text: "OK",
						"class" : "btn btn-primary btn-xs",
						click: function() {
							window.location.href=anchor.href;
						} 
					}
				]
			});
		});
		//tooltips
		$( "#show-option" ).tooltip({
			show: {
				effect: "slideDown",
				delay: 250
			}
		});
	
		$( "#hide-option" ).tooltip({
			hide: {
				effect: "explode",
				delay: 250
			}
		});
		$( "#open-event" ).tooltip({
			show: null,
			position: {
				my: "left top",
				at: "left bottom"
			},
			open: function( event, ui ) {
				ui.tooltip.animate({ top: ui.tooltip.position().top + 10 }, "fast" );
			}
		});
		//Menu
		$( "#menu" ).menu();
	
		//spinner
		var spinner = $( "#spinner" ).spinner({
			create: function( event, ui ) {
				//add custom classes and icons
				$(this)
				.next().addClass('btn btn-success').html('<i class="ace-icon fa fa-plus"></i>')
				.next().addClass('btn btn-danger').html('<i class="ace-icon fa fa-minus"></i>')
				
				//larger buttons on touch devices
				if('touchstart' in document.documentElement) 
					$(this).closest('.ui-spinner').addClass('ui-spinner-touch');
			}
		});
	
		//slider example
		$( "#slider" ).slider({
			range: true,
			min: 0,
			max: 500,
			values: [ 75, 300 ]
		});
	
		//jquery accordion
		$( "#accordion" ).accordion({
			collapsible: true ,
			heightStyle: "content",
			animate: 250,
			header: ".accordion-header"
		}).sortable({
			axis: "y",
			handle: ".accordion-header",
			stop: function( event, ui ) {
				// IE doesn't register the blur when sorting
				// so trigger focusout handlers to remove .ui-state-focus
				ui.item.children( ".accordion-header" ).triggerHandler( "focusout" );
			}
		});
		//jquery tabs
		$( "#tabs" ).tabs();
	});
</script>
<script  type="text/javascript">
	function getcookie(objname) {
		var arrstr = document.cookie.split("; ");
		for ( var i = 0; i < arrstr.length; i++) {
			var temp = arrstr[i].split("=");
			if (temp[0] == objname) {
				return temp[1];
			}
		}
		return "";
	}
	
	$(document).ready(function() {
		var ct = getcookie("ct");
		if (ct != "") {
			var length = ct.length;
			var realName = ct.split("|");
			var temp = realName[0];
			
			if(temp.charAt(0)=='"'){
				temp =temp.substring(1,temp.length);
			}
			var name = decodeURI(temp);
			var loginInfo=document.getElementById('loginInfo');
			loginInfo.innerHTML ='欢迎，'+name;
		} else{
			var loginInfo=document.getElementById('loginInfo');
			loginInfo.innerHTML ='<a href="/cat/s/login" data-toggle="modal">登录</a>';
		}
		var page = '${model.page.title}';
		$('#'+page+"_report").addClass("active open");
	}); 
</script>
</html>

