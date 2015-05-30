<%@ tag isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ attribute name="head" fragment="true" required="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="navBar" class="org.unidal.cat.view.NavigationBar" scope="page" />

<!DOCTYPE html>
<html lang="en">

<head>
	<title>CAT</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="description" content="CAT">

	<link href="${model.webapp}/css/bootstrap.css" type="text/css" rel="stylesheet">
	<link href="${model.webapp}/css/bootstrap-responsive.css" type="text/css" rel="stylesheet">
	<script src="${model.webapp}/js/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script src="${model.webapp}/js/bootstrap.js" type="text/javascript"></script>
	<script type="text/javascript">var contextpath = "${model.webapp}";</script>

	<jsp:invoke fragment="head"/>
</head>

<body data-spy="scroll" data-target=".subnav" data-offset="50">
	<div><h4>CAT</h4></div>
	<div class="container-fluid" style="min-height:524px;">
		<div class="row-fluid">
			<div class="span3">
				...nav body
			</div>
			<div class="span9">
				<jsp:doBody />
			</div>
		</div>
	
		<br />
		<div class="container">
			<footer><center>&copy;2015 CAT Team</center></footer>
		</div>
	</div>
	<!--/.fluid-container-->

</body>
</html>
