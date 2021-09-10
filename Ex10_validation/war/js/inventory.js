

// global request and table objects
var req;
var mytable;

var statusBox = $('#ajaxResults');

function init() {
    // branch for native XMLHttpRequest object
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
    // branch for IE/Windows ActiveX version
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
    }
}

//
// HTTP GET to get complete inventory
//
function getAction(url) {
	init();
    if (req) {
        req.onreadystatechange = gotLatestInventory;
        req.open("GET", url, true);
        req.setRequestHeader( "If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT" );
        req.send(null);
    }
}

//
// HTTP POST to create a new entry in the inventory
// For e.g.:
// <item quantity="4" />
//
function postAction(index) {

	
	value=document.getElementById(index).value;
	url = "/inventory/rs/item";
	id = index; // not really unique
	str = "<item productId=\"" + id + "\" quantity=\"" + value + "\" />";
	displayStatus("Ajax POST request in progress", str);
	init();
    if (req) {
        req.onreadystatechange = processInventoryChange;
        req.open("POST", url, true);
        req.setRequestHeader("Content-Type", "application/xml");
        req.send(str);
    }
}

//
// HTTP DELETE to delete an item in the inventory. No XML
// content is sent. The request URI is used to delete the item
// For e.g: item/1
//
function deleteAction(id) {
	statusBox.html("Ajax DELETE request in progress");
	statusBox.fadeIn(300);
	
	url = "/inventory/rs/item/"+id;
	init();
    if (req) {
        req.onreadystatechange = processInventoryChange;
        req.open("DELETE", url, true);
        req.send(null);
    }
}

//
// HTTP PUT to update an item in the inventory. 
// The request URI is used to identify the item, and XML content
// updates to the resource
// For e.g: item/1
//	<item quantity="4" />
//
function putAction(id, index) {

	value=document.getElementById(index).value;
	str = "<item quantity=\""+value+"\" />";
	url = "/inventory/rs/item/"+id;
	displayStatus("Ajax PUT request in progress", str);
	init();
    if (req) {
        req.onreadystatechange = processInventoryChange;
        req.open("PUT", url, true);
        req.setRequestHeader("Content-Type", "application/xml");
        req.send(str);
    }
}


//
// HTTP GET is done. Display the updated inventory table
//
function gotLatestInventory() {
    // only if req shows "loaded"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
            updateTable();
         } else {
            alert("getLatestInventory failed: " +req.status+
				": "+req.statusText);
         }
    }
}

//
// HTTP PUT, POST, DELETE is done. Display the updated inventory table
// by doing HTTP GET to get the latest inventory
//
function processInventoryChange() {
    // only if req shows "loaded"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
            start();
         } else {
            alert("processInventoryChange failed: " +req.status+
				": "+req.statusText);
         }
    }
}

function start() {
	getAction("/inventory/rs/item/all");
}

// 
// Updates display
//
function updateTable() {
	statusBox.fadeOut(4000);	
	
	// get the reference for the body
	//var mybody=document.getElementsByTagName("body").item(0);
	// deletes TABLE from BODY
	if (mytable != null) {
		var tableSection = document.getElementById("inventory");
		tableSection.removeChild(mytable);
	}
	// creates an element whose tag name is TABLE
	mytable = document.createElement("TABLE");
	// creates an element whose tag name is TBODY
	mytablebody = document.createElement("TBODY");
	var items = req.responseXML.getElementsByTagName("item");
	// loop through <item> elements, and add each nested
	for (var i = 0; i < items.length; i++) {
		// creates an element whose tag name is TR
		mycurrent_row=document.createElement("TR");

		// creates an element whose tag name is TD
		mycurrent_cell=document.createElement("TD");
		// creates a Text Node
		id = items[i].getAttribute("productId");
		currenttext=document.createTextNode(id);
		// appends the Text Node we created into the cell TD
		mycurrent_cell.appendChild(currenttext);
		// appends the cell TD into the row TR
		mycurrent_row.appendChild(mycurrent_cell);

		// creates an element whose tag name is TD
		mycurrent_cell=document.createElement("TD");
		// creates a Text Node
		text = items[i].getAttribute("quantity");
		mycurrent_cell.innerHTML= "<form id=\"myForm\"><input id=\""+i+"\" type=\"text\" value=\""+text+"\"/></form>";
		// appends the cell TD into the row TR
		mycurrent_row.appendChild(mycurrent_cell);

		// creates an element whose tag name is TD
		mycurrent_cell=document.createElement("TD");
		mycurrent_cell.innerHTML= "<form id=\"myForm\"><input type=\"button\" onclick=\"deleteAction('"+id+"')\" value=\"DELETE\"/></form>";
		// appends the cell TD into the row TR
		mycurrent_row.appendChild(mycurrent_cell);

		// creates an element whose tag name is TD
		mycurrent_cell=document.createElement("TD");
		str = "<form id=\"myForm\"><input type=\"button\" onclick=\"putAction('"+id+"','"+i+"')\" value=\"UPDATE\"/></form>";
		mycurrent_cell.innerHTML=  str;
		// appends the cell TD into the row TR
		mycurrent_row.appendChild(mycurrent_cell);

		// appends the row TR into TBODY
		mytablebody.appendChild(mycurrent_row);
	}
	// creates an element whose tag name is TR
	mycurrent_row=document.createElement("TR");

	// creates an element whose tag name is TD
	mycurrent_cell=document.createElement("TD");
	// creates a Text Node
	currenttext=document.createTextNode("");
	// appends the Text Node we created into the cell TD
	mycurrent_cell.appendChild(currenttext);
	// appends the cell TD into the row TR
	mycurrent_row.appendChild(mycurrent_cell);

	// creates an element whose tag name is TD
	mycurrent_cell=document.createElement("TD");
	// creates a Text Node
	mycurrent_cell.innerHTML= "<form id=\"myForm\"><input id=\""+items.length+"\" type=\"text\" value=\"\"/></form>";
	// appends the cell TD into the row TR
	mycurrent_row.appendChild(mycurrent_cell);

	// creates an element whose tag name is TD
	mycurrent_cell=document.createElement("TD");
	mycurrent_cell.innerHTML= "<form id=\"myForm\"><input type=\"button\" onclick=\"postAction('"+items.length+"')\" value=\"POST NEW\"/></form>";
	// appends the cell TD into the row TR
	mycurrent_row.appendChild(mycurrent_cell);

	// appends the row TR into TBODY
	mytablebody.appendChild(mycurrent_row);

	// appends TBODY into TABLE
	mytable.appendChild(mytablebody);
	var tableSection = document.getElementById("inventory");
	// appends TABLE into the correct section
	tableSection.appendChild(mytable);
	// sets the border attribute of mytable to 2;
	mytable.setAttribute("border","2");
}

function displayStatus(msg, data)
{
	if(data != undefined){
		data = data.replace('<', '&lt;');
	}
	statusBox.html(msg + "<br/>" + data );
	statusBox.fadeIn(300);		
}