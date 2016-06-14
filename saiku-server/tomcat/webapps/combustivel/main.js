window.APP_CONFIG = {
	URL: "http://www.topweb.sulnorte.com.br/top/xml/action/PRDAction.php?metodo="
};
$(document).ready(function () {
	try {
		window.tableController = new TableController();
	}
	catch(e) 
	{
		alert(e);
	}
});