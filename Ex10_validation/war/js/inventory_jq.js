// Version of the inventory script to exploit the benefits of jQuery and JSON

// Function to execute when the page has loaded
$(function(){
	// Load the inventory via ajax
	getAction();
	// Add unobtrusive handler for the post new button
	$('#postNewBut').on('click', postAction);
	
	statusBox = $('#ajaxResults');
});

//
//HTTP GET to get complete inventory
//

function getAction()
{
	$.ajax('rs/item/all',
			{
			'dataType' : 'json', 		  // parse the response as JSON
			'success':displayInventory,
			'error':ajaxError
			}
		);
}

// Display the inventory when downloaded
function displayInventory(items){
	// items is an array of item objects automatically converted from JSON response
	var inventoryTable = $('#inventory');
	var len = items.item.length;
	var html="<tr><th>Product Id</th><th>Quantity</th><th></th></tr>";
	for(var i=0; i < len; i++){
		var item = eval(items.item[i]);
		brokenExHint(item);
		var prodId = item.productId;
		var qty = item.quantity;		
		html += "<tr><td>" + prodId + "</td><td>" +
		'<input class="qty" id="' + prodId + '" type="text" value="' + qty + '"/>' + "</td><td>" +
		'<input type="button" value="DELETE" onclick="deleteAction(\'' + prodId + '\')">' + "</td><td>" +
		'<input type="button" value="UPDATE" onclick="putAction( this, \'' + prodId + '\',\'0\')">' +
		"</td></tr>";
		
	}
	inventoryTable.html(html);
}

// Report errors
function ajaxError(jqXHR, status, error){
	alert("AJAX call failed. Status=" + status + " Message=" + error);
	
}


function ajaxSuccess(){
	
	getAction();
	statusBox.fadeOut(4000);
}


//
// HTTP POST to create a new entry in the inventory
// For e.g.:
// <item quantity="4" />
//
function postAction() {
	var item = new Object();
	item.quantity=$('#postNewQty').val();
	item.productId = $('#postNewId').val();
	var url = "rs/item";
	str = JSON.stringify(item);
	statusBox.html("Ajax POST request in progress<br/>JSON: " + str);
	statusBox.fadeIn(300);
	$.ajax(url, { 'type' : 'POST',
		'contentType' : 'application/json',
		'data' : str,
		'error' : ajaxError, 
		'success' : ajaxSuccess });	// On success, reload the list
}


//
// HTTP DELETE to delete an item in the inventory. No XML
// content is sent. The request URI is used to delete the item
// For e.g: item/1
//
function deleteAction(id) {
	url = "rs/item/"+id;
	statusBox.html("Ajax DELETE request in progress<br/>Id: " + id);	
	statusBox.fadeIn(300);
	$.ajax(url, { 'type' : 'DELETE',
		'contentType' : 'application/json',
		'error' : ajaxError, 
		'success' : ajaxSuccess });	// On success, reload the list
}

//
// HTTP PUT to update an item in the inventory. 
// The request URI is used to identify the item, and XML content
// updates to the resource
// For e.g: item/1
//	'{"quantity":"5"}'
//
function putAction(ele, id) {

	var url = "rs/item/"+id;
	var item = new Object();
	item.quantity = $('#'+id).val();
	str = JSON.stringify(item);
	statusBox.html("Ajax PUT request in progress<br/>JSON: " + str);
	statusBox.fadeIn(300);
	$.ajax(url, { 'type' : 'PUT',
		'contentType' : 'application/json',
		'data' : str, //'{"quantity":"5"}',
		'error' : ajaxError, 
		'success' : ajaxSuccess });	// On success, reload the list
}



function brokenExHint(item)
{
	if(item.productId == undefined){
		$('#ajaxResults').html("There is no productId associated with the inventory items. <br/>This probably means that you have not yet removed the @XmlAttribute from the Item class");	
		$('#ajaxResults').fadeIn(300);
	}
}


