package project;

import java.text.DecimalFormat;

import org.pi4.locutil.GeoPosition;
import org.pi4.locutil.MACAddress;


public class Misc {

	public static double getEuclidianDistance(double x1, double x2, double y1, double y2, double z1, double z2) 
	{
		return Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2) + Math.pow(z1 - z2, 2));
	}
	
	public static GeoPosition getGeoPosOfAP(MACAddress mac)
	{
		GeoPosition retVal = null;

		if(mac.toString().equals("00:14:BF:B1:7C:54")) 		retVal = new GeoPosition(-23.626, -18.596);
		else if(mac.toString().equals("00:16:B6:B7:5D:8F")) retVal = new GeoPosition(-10.702, -18.596);
		else if(mac.toString().equals("00:14:BF:B1:7C:57")) retVal = new GeoPosition(8.596, -14.62);
		else if(mac.toString().equals("00:14:BF:B1:97:8D"))	retVal = new GeoPosition(8.538, -9.298);
		else if(mac.toString().equals("00:16:B6:B7:5D:9B"))	retVal = new GeoPosition(-1.93, -2.749);
		else if(mac.toString().equals("00:14:6C:62:CA:A4"))	retVal = new GeoPosition(4.035, -0.468);
		else if(mac.toString().equals("00:14:BF:3B:C7:C6"))	retVal = new GeoPosition(13.333, -2.69);
		else if(mac.toString().equals("00:14:BF:B1:97:8A")) retVal = new GeoPosition(21.17, -2.69);
		else if(mac.toString().equals("00:14:BF:B1:97:81"))	retVal = new GeoPosition(32.398, -2.69);
		else if(mac.toString().equals("00:16:B6:B7:5D:8C"))	retVal = new GeoPosition(32.573, 13.86);
		else if(mac.toString().equals("00:11:88:28:5E:E0"))	retVal = new GeoPosition(7.135, 6.023);


		return retVal;
	}
	
	public static double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
	}
}
