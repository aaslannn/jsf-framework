function randomString()
{
	var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
	var string_length = 8;
	var randomstring = '';
	for ( var i = 0; i < string_length; i++)
	{
		var rnum = Math.floor(Math.random() * chars.length);
		randomstring += chars.substring(rnum, rnum + 1);
	}
	return randomstring;
}

String.prototype.replaceAll = function(token, newToken, ignoreCase)
{
	var _token;
	var str = this + "";
	var i = -1;

	if (typeof token === "string")
	{
		if (ignoreCase)
		{
			_token = token.toLowerCase();

			while ((i = str.toLowerCase().indexOf(_token,
					i >= 0 ? i + newToken.length : 0)) !== -1)
			{
				str = str.substring(0, i) + newToken
						+ str.substring(i + token.length);
			}

		}
		else
		{
			return this.split(token).join(newToken);
		}

	}
	return str;
};

function showStatus()
{
	statusDialog.show();
}

function hideStatus()
{
	statusDialog.hide();
}