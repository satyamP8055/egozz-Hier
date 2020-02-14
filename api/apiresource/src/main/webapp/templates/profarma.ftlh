<html>
<head>
	<title></title>
</head>
<body style='padding:15px;'>
	<table style='width:100%; margin-bottom:50px;'>
		<tr>
			<th style='width:75%; margin-bottom: 50px; margin-top: 25px;  padding-top:150px; text-align:left;'>
				<h2 style='color:rgb(8,11,90); margin:5px; '>PROFARMA</h2>
				<h3 style='color:rgb(8,11,90); margin:5px; '><b>EGOZZ </b>- <span style='color:rgb(14,117,10);'>Travelling Made Easy</span></h3>  
			</th>
			<th style='width:25%; text-align:center; margin-bottom: 25px; padding-top:150px;'>
				<img src='${logo}' style='height:70px; width:auto; max-width:150px;'/>
			</th>
		</tr>
	</table>
	<table style='margin:5px; margin-top: 150px; margin-bottom:50px; width:100%; border-collapse:collapse;'>
		<tr>
			<th style='border-left:1px solid black; padding: 5px; text-align:center; border-top:1px solid black; border-bottom:1px solid black; border-right:1px solid black; width:40px;'>#</th>
			<th style='padding: 5px; text-align:center; border-top:1px solid black; border-bottom:1px solid black; border-right:1px solid black;'>DESCRIPTION</th>
			<th style='padding: 5px; text-align:center; border-top:1px solid black; border-bottom:1px solid black; border-right:1px solid black; width:120px;'>UNIT CHARGE</th>
			<th style='padding: 5px;  text-align:center;border-top:1px solid black; border-bottom:1px solid black; border-right:1px solid black; width:120px;'>TOTAL UNITS</th>
			<th style='border-right:1px solid black; padding: 5px; text-align:center; border-top:1px solid black; border-bottom:1px solid black; width:140px;'>TOTAL CHARGE</th>
		</tr>
		<tr>
			<td style='border-left:1px solid black; border-right:1px solid black; text-align:center; padding:5px;'>1</td>
			<td style='border-right:1px solid black; padding:5px;'>Fuel Charge</td>
			<td style='border-right:1px solid black; padding:5px;'>${fuelCharge}</td>
			<td style='border-right:1px solid black; padding:5px;'>${fuelUnit}</td>
			<td style='border-right:1px solid black; text-align:right; padding:5px;'>
				${fuelCharge}</td>
		</tr>
		<tr>
			<td style='border-left:1px solid black; border-right:1px solid black; text-align:center; padding:5px;'>2</td>
			<td style='border-right:1px solid black; padding:5px;'>Day Reservation Charge</td>
			<td style='border-right:1px solid black; padding:5px;'>
				${reservationUnitCharge}
			</td>
			<td style='border-right:1px solid black; padding:5px;'>
				${reservationUnit}
			</td>
			<td style='border-right:1px solid black; text-align:right;  padding:5px;'>
				${reservationCharge}
			</td>
		</tr>
		<tr>
			<td style='border-left:1px solid black; border-right:1px solid black; text-align:center; padding:5px;'>3</td>
			<td style='border-right:1px solid black; padding:5px;'>Distance Fare</td>
			<td style='border-right:1px solid black; padding:5px;'> ${kmsUnitCharge}</td>
			<td style='border-right:1px solid black; padding:5px;'> ${kmsUnit}</td>
			<td style='border-right:1px solid black; text-align:right;  padding:5px;'>
				${kmsCharge}
			</td>
		</tr>
		
		<!-- Blank ROWS START HERE-->
		<tr>
			<td style='border-left:1px solid black;  height : 150px; border-right:1px solid black; text-align:center; padding:5px;'></td>
			<td style='height : 150px;  border-right:1px solid black; padding:5px;'></td>
			<td style='height : 150px; border-right:1px solid black; padding:5px;'></td>
			<td style='height : 150px; border-right:1px solid black; padding:5px;'></td>
			<td style='height : 150px; border-right:1px solid black; text-align:right; padding:5px;'></td>
		</tr>
		<!-- Blank ROWS END HERE-->
		
		<tr>
			<td style='border-top:1px solid black; border-right:1px solid black; text-align:center; padding:5px;' colspan='3'></td>
			<td style='border-top:1px solid black; border-right:1px solid black; padding:5px;'>CGST (9%)</td>
			<td style='border-right:1px solid black; text-align:right; border-top:1px solid black; padding:5px;'>
			${ninePercent}</td>
		</tr>
		<tr>
			<td style='border-right:1px solid black; text-align:center; padding:5px;' colspan='3'></td>
			<td style='border-top:1px solid black; border-right:1px solid black; padding:5px;'>SGST (9%) </td>
			<td style='border-right:1px solid black; text-align:right; border-top:1px solid black;  padding:5px;'>
			${ninePercent}
			</td>
		</tr>
		<tr>
			<td style='border-right:1px solid black; text-align:center; padding:5px;' colspan='3'></td>	
			<td style='border-top:1px solid black; border-right:1px solid black; padding:5px;'>SubTotal </td>
			<td style='border-right:1px solid black; text-align:right; border-top:1px solid black;  padding:5px;'>
				${subTotal}
			</td>
		</tr>
		<tr>
			<td style='border-right:1px solid black; text-align:center; padding:5px;' colspan='3'></td>
			<td style='border-top:1px solid black; border-right:1px solid black; padding:5px;'>Discounts</td>
			<td style='border-right:1px solid black; text-align:right; border-top:1px solid black;  padding:5px;'>-
				${eighteenPercent}
			</td>
		</tr>
		<tr>
			<td style='border-right:1px solid black; text-align:center; padding:5px;' colspan='3'></td>
			<th style=' padding:5px;border-top:1px solid black; border-bottom:1px solid black; border-right:1px solid black;'>Total</th>
			<th style=' border-right:1px solid black; text-align:right; padding:5px;border-top:1px solid black; border-bottom:1px solid black; width:140px;'>
				<span class='fa fa-inr'></span>${totalCharge}
			</th>
		</tr>
	</table>
	<div style='width:30%; margin-top:100px; margin-bottom:30px;'>
		<h4 style='padding:5px; padding-bottom:2px; margin-bottom:0px;'>Terms & Conditions </h4>
		<h5 style='padding:5px; padding-bottom:2px; padding-top:2px; margin-top:5px; margin-bottom:5px; font-weight:lighter;'>Payment is due within 15 days.</h5>
		<h5 style='padding:5px; padding-bottom:2px; padding-top:2px; margin-top:25px; margin-bottom:5px; color:rgb(8,11,90);'>EGOZZ</h5>
		<p style='padding:5px; padding-bottom:2px; padding-top:2px; margin-top:5px; margin-bottom:5px;'>921, Chandaka Industrial Estate, DLF Cybercity, Patia, Bhubaneswar, Odisha 751024, India</p>
	</div>
</body>
</html>