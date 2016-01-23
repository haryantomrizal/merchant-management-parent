function roundAmount(amount) {
	var a = parseFloat(amount);
	var result = 0;

	if (a < 100) {
		result = 100;
	} else {
		var aa = parseInt(amount).toString();
		var ratusan = aa.substring(amount.length - 3);
		var irwin = parseInt(aa) - parseInt(ratusan);
		var mm = parseFloat(parseInt(ratusan) / 100);
		var mir = Math.round(mm);
		var mpit = mir * 100;
		result = parseInt(mpit) + parseInt(irwin)
	}

	return result;
}

function roundAmount50(amount) {
	var a = parseFloat(amount);
	var result = 0;

	if (a <= 50) {
		result = 50;
	} else {
		var aa = parseInt(amount).toString();
		var puluhan = aa.substring(amount.length - 2);
		var irwin = parseInt(aa) - parseInt(ratusan);
		var mm = parseInt(ratusan);
		var mpit = 0;
		if (mm <= 50) {
			mpit = 50;
		} else {
			mpit = 100;
		}
		result = parseInt(mpit) + parseInt(irwin)
	}

	return result;
}

function addCommaSeparator(nStr) {
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

// LIST PAGE COMMON SCRIPT
function doCreate() {
	document.location.href="create";
}

function doDetail(id) {
	jQuery.ajax({
		type: "get",
		url: "detailAjax.html",
		dataType: "text",
		data: {id:id}, 
		success: function(data, textStatus) {
			if (textStatus == "success") {
				$('#detail_' + id).html(data);
				$('#detail_' + id).toggle();							
			}					  																				  		
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			var html = textStatus + " " + errorThrown;
		}																									

	});

}

function doEdit(id) {
	//closeDetail();
	document.location.href="edit?id=" + id;
}

function doDelete(id) {
	var confirm = window.confirm('Are you sure want to delete this record ?');
	if (confirm) {
//		closeDetail();
		document.location.href="delete?id=" + id;
	}					
}
				
function closeDetail(id) {
	$("#detail_" + id).toggle();
}

function doSearch(){
	document.forms.listForm.start.value = 0;
	loadList();
}

function doReset(){
	document.forms.listForm.searchKeyword.value = "";
	document.forms.listForm.start.value = 0;
	loadList();
}

function doChangePage(start){
	document.forms.listForm.start.value = start;
	loadList();
}

function doGoPage() {
	loadList();
}

function fill(val, defaultPageSize) {
	var input = val.value;
	if (input > 0) {
		input = input - 1;
	}
	document.forms.listForm.start.value = input * defaultPageSize;
}

function doOrderPage(){
	document.forms.listForm.start.value = 0;
	loadList();
}

function loadList(){	
	jQuery.ajax({
		type: "get",
		url: "listAjax.html",
		dataType: "text",						
		data: {searchKeyword:$("input[name='searchKeyword']")[0].value, 
				order:$("select[name='order']")[0].value, 
				beanProperty:$("select[name='beanProperty']")[0].value, 
				start:$("input[name='start']")[0].value},
		success: function(data, textStatus) {						  			
  			if (textStatus == "success") {										
		  		$('#list').html(data);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			var html = textStatus + " " + errorThrown;
			$('#list').html(html);
		}																	
		
	});
}

function doEnterSearch(event) {
	var key = -1;
	if (event.which) {
		key = event.which;
	} else if (event.keyCode) {
		key = event.keyCode;
	}
	
	if (key == 13) {
		// 13 is enter HAHAHAHAHA !!!!
		$("input[value='Search']")[0].click();
	}
}

// CREATE PAGE COMMON SCRIPT
function doCancel() {
	document.location.href="list";
}

function doCancelCreate() {
	document.location.href="list";
}

function doSaveCreate() {
	var msg = doValidate();
	if (msg == "") {
		document.forms.createForm.submit();
	} else {
		alert(msg);
	}
}

//EDIT PAGE COMMON SCRIPT
function doCancelEdit() {
	document.location.href="list.html";
}

function doSaveEdit() {
	var msg = doValidate();
	if (msg == "") {
		document.forms.editForm.submit();
	} else {
		alert(msg);
	}
}
