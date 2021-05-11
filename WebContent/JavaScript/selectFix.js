function expand(control)
{
	if (navigator.appName == 'Microsoft Internet Explorer')
	{
		control.setAttribute('open', 'false');
		control.style.width = '252px';
		return;
	}
}

function collapse(control)
{
	if (navigator.appName == 'Microsoft Internet Explorer')
	{
		if (control.getAttribute('open') == null
				|| control.getAttribute('open') == 'false')
		{
			control.style.width = 'auto';
			if (control.clientWidth < 252)
			{
				control.style.width = '252px';
			}
			control.setAttribute('open', 'true');
		}
		return;
	}
}