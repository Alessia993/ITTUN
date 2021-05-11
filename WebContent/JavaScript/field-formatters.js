function decimalonly(myfield, e) {
	var key;
	var keychar;
	var value = myfield.value;
	var isCtrl;

	if (window.event)
		key = window.event.keyCode;
	else if (e){
		key = e.which;
		isCtrl = e.ctrlKey;}
	else
		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key == null) || (key == 0) || (key == 8)) {
		return true;
	} else if ((("0123456789").indexOf(keychar) > -1)) {

		if (value.indexOf(".") != -1
				&& value.indexOf(".") <= value.length - 3) {
			return false;
		} else if (value.indexOf(",") != -1
				&& value.indexOf(",") <= value.length - 3) {
			return false;
		} else {
			if (value.length > 8 && value.indexOf(",") == -1
					&& value.indexOf(".") == -1) {
				return false;
			} else {
				return true;
			}
		}
	}

	else if ((keychar == ".") || (keychar == ",")) {

		if (value.indexOf(".") != -1 || value.indexOf(",") != -1) {
			return false;
		} else if (value.length == 0) {
			return false;
		} else {
			return true;
		}
	} 
	else if(isCtrl && ((key==120)||(key==97)||(key==99)||(key==118)))
	{
		return true;
	}else
		return false;
}
	
function numbersonly(myfield, e) {
	var key;
	var keychar;
	var value = myfield.value;
	var isCtrl;
	
	if (window.event)
	{
		key = window.event.keyCode;
	}
	else if (e)
	{
		key = e.which;
		isCtrl = e.ctrlKey;
	}
	else
		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key == null) || (key == 0) || (key == 8) || (key == 9)
			|| (key == 13) || (key == 27)) {
		return true;
	} else if ((("0123456789").indexOf(keychar) > -1)) {
			if (value.length > 8) {
				return false;
			} else {
				return true;
			}
		}
	else if(isCtrl && ((key==120)||(key==97)||(key==99)||(key==118)))
	{
		return true;
	}
	 else
		return false;
}

function textArea(myfield, number, e) {
	if (window.event)
	{
		key = window.event.keyCode;
	}
	else if (e)
	{
		key = e.which;
		isCtrl = e.ctrlKey;
	}
	else
		return true;
	
	if ((key == null) || (key == 0) || (key == 8) || (key == 9)
			|| (key == 13) || (key == 27)) {
		return true;
	}
	
	if(myfield.value.length>=number)
	{
		return false;
	}
	else
	{
		return true;
	}
}

function textAreaCut(myfield, number)
{
	if(myfield.value.length >= number)
	{
		myfield.value = myfield.value.substring(0, number);
	}
}

function checkdecimal(field)
{
	var value = field.value;
	
	for(var i=0;i<value.length;i++)
	{
		if(!isnumberOrDot(value.charAt(i)))
		{
			field.value="";
			return false;
		}
	}
	var flag=false;
	for(var i=0;i<value.length;i++)
	{
		if((".,").indexOf(value.charAt(i)) > -1)
		{
			if(flag)
			{
				field.value="";
				return;
			}
			
			flag=true;
		}
	}

	if (value.indexOf(".") != -1
			&& value.indexOf(".") < value.length - 3) {
		field.value="";
		return;
	} else if (value.indexOf(",") != -1
			&& value.indexOf(",") < value.length - 3) {
		field.value="";
		return;
	} else {
		if (value.length > 9 && value.indexOf(",") == -1
				&& value.indexOf(".") == -1) {
			field.value="";
			return;
		} 
	}
}

function checknumber(field)
{
	var value = field.value;
	
	for(var i=0;i<value.length;i++)
	{
		if(!isnumber(value.charAt(i)))
		{
			field.value="";
			return false;
		}
	}
}

function isnumberOrDot(char)
{
	if(("0123456789.,").indexOf(char) > -1)
	{
		return true;
	}
	else{return false;}
	}

function isnumber(char)
{
	if(("0123456789").indexOf(char) > -1)
	{
		return true;
	}
	else{return false;}
	}

function ReCulcTotal() 
{
	var sum1= 0;
	var sum2= 0;
	var i=0;
	var field1_Id = null;
	var field1 = document.getElementById("FormList:table:0:amountRealized");
	var field2_Id = null;
	var field2 = document.getElementById("FormList:table:0:amountToAchieve");
	var disable = false;
	var temp;
	while(field1!=null && field2!=null)
	{
		if(disable)
		{
			field1.disabled=true;
		}
		else
		{
			field1.disabled=false;
		}
		i++;
		sum1 +=  isNaN(parseFloat(field1.value))?0:parseFloat(field1.value);
		field1_Id = "FormList:table:"+i+":amountRealized";
		field1 = document.getElementById(field1_Id);
		if(field1!=null && field1.value!="0" && field1.value!="")
		{
			temp = "FormList:table:"+(i-1)+":amountToAchieve";
			document.getElementById(temp).disabled=true;
		}
		else if (field1!=null && field1.value=="0")
		{
			temp = "FormList:table:"+(i-1)+":amountToAchieve";
			document.getElementById(temp).disabled=false;
		}
		
		sum2 += isNaN(parseFloat(field2.value))?0:parseFloat(field2.value);
		if(field2.value!="0" && field2.value!="")
		{
			disable = true;
		}
		field2_Id = "FormList:table:"+i+":amountToAchieve";
		field2 = document.getElementById(field2_Id);
		
		}

	var field1_total = document.getElementById("FormList:table:"+i+":amountRealized_text");
	field1_total.innerHTML = FormatMoneyString(sum1);;
	var field2_total = document.getElementById("FormList:table:"+i+":amountToAchieve_text");
	field2_total.innerHTML = FormatMoneyString(sum2);
}

function FormatMoneyString(str)
{
	var str1 = parseFloat(str).toFixed(2);
	str1=str1.replace('.', ',');
	var str2 = str1.substring(0, str1.indexOf(',', 0));
	var str3 = str1.substring(str1.indexOf(',', 0), str1.length);
	var str4 = '';
	var i=0;
	while(str2[i]!=null)
	{
		if(i!=0 && (str2.length-i)%3==0)
		{
			str4+='.';
		}
		str4+=str2[i];
		i++;
	}
	i=0;
	while(str3[i]!=null)
	{
		str4+=str3[i];
		i++;
	}
	return "€&nbsp;"+str4;
}

function makeUppercase(field) {
	var ss = field.selectionStart;
	field.value = field.value.toUpperCase();
	field.selectionStart = ss;
	field.selectionEnd = ss;
	}