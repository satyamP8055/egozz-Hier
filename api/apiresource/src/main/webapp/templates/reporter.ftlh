<html>
	<body style='padding:15px;'>
		<div style='text-align:center;'>
			<img src='${logo}' style='height:100px; width:auto; max-width:150px;'/>
			<h2 style='color:${primeColor}; font-family:serif;'><b>${name}</b></h2>
		</div>
		<h2 style='color:${primeColor}'><b>Hi Developer !</b></h2>
		<h4 style='color:${fallBackColor}'><b>We got some exception in  ${appName}</b></h4>
		<h4><b style='color:${backColor}'>Exception Class : </b>  ${exceptionType}</h4>
		<h4><b style='color:${backColor}'>Date & Time : </b> ${dateTime} </h4>
		<h4><b style='color:${backColor}'>IP Trace : </b>  ${ipTrace} </h4>
		<h4 style='color:${fallBackColor}'><b>Stack Trace of the Exception containing 
				${stackLength} rows is given below : </b></h4>
		<table style='width:100%; border-collapse: collapse; margin-top:25px; margin-bottom:25px; border:0.5px solid ${primeColor} ;'>
			<tr>
				<td style='border: 0.5px solid ${primeColor}; background-color:${primeColor}; color:white; padding:5px; padding-top:7px; font-weight:bold;' colspan='2'>
					${primeStack}
				</td>
			</tr>
			<#list stackRow as stack>
				<tr>
					<th style='border: 0.5px solid ${primeColor}; padding:5px; color:${primeColor}; background-color: ${backColor}; width:50px; text-align:center;'>
						AT
					</th>
					<td style='border: 0.5px solid ${primeColor}; padding:5px;'>
						${stack}
					</td>
				</tr>
			</#list>
		</table>
		<h3 style='color:${primeColor}'><b>Happy Coding !</b></h3>
		<h4><b style='color:${backColor}'>${name}</b></h4>
	</body>
</html>	