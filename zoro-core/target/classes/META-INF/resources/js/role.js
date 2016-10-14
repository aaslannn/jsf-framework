var isEnter= false;
function filterUsernameKeyPress(event,element)
{
	if (event.keyCode == 13)
	{
		isEnter = true;
		$(element).blur();
		event.preventDefault();
		return false;
	}
}

function doFilterUsernames()
{
	if (!isEnter)
	{
		return false;
	}
	else
	{
		isEnter = false;
	}
}