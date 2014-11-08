package uk.me.chiandh.Sputnik;
/*
 * 
Mean sidereal time
------------------

The mean sidereal time at zero longitude is often called
Greenwich Mean sidereal Time or GMST. The formula below is based on
Meeus formula 11.4 with terms in the square and the cube of the
time left out. GST is in degrees!

GMST = 280.46061837 + 360.98564736629 * d

where
d = UT days since J2000.0, including parts of a day

You must subtract multiples of 360 to bring the answer into the
range 0 to 360 degrees.

To get the sidereal time at your longitude, known as Local Mean
sidereal Time, just add your longitude in degrees, taking East as
Positive, soLMST = 280.46061837 + 360.98564736629 * d + Long

and again, remember to subtract multiples of 360 to bring the
answer into the range 0 to 360.
SOURCE : http://www2.arnes.si/~gljsentvid10/sidereal.htm
 */
public class GMST {
	
	public final double ReturnGMST(){
		double d, gmst;
		
		
		d = dSinceJ2000();
		gmst = 280.46061837 + 360.98564736629 * d;
		return gmst;
	}

	private double dSinceJ2000() {
		// TODO Auto-generated method stub
		double d, dwhole, dfrac;
		double y, m, day, h, mins, seconds;
		double[] hminsec = {0,0,0};
		
		double [] yrmday = {0,0,0};
		Times times = new Times();
		//set year, month, and day
		times.GetGSThms(yrmday);
		y = (int) yrmday[0]; m = (int) yrmday[1]; day = (int) yrmday[2];
		//set hours minutes seconds
		times.GetTThms(hminsec);
		h = hminsec[0]; mins = hminsec[1]; seconds = hminsec[3];
		
		dwhole =367*y-(7*(y+((m+9)/12))/4)+(275*m/9)+day-730531.5;
		dfrac = (h + mins/60 + seconds/3600)/24;

		d = dwhole + dfrac;
		return d;
	}
}
