/* Use hashMap and sorting: O(n)
 * 1. Create a hashMap "freqMap" to record the frequency of every character
 * 2. Sort the hashMap and output the answer string
 */

import java.util.*; // Stack

class plane {
	public int x;
	public int y;

	plane(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public double distence(plane ob1, plane ob2){
		return Math.sqrt(Math.pow((ob1.x - ob2.x), 2) + Math.pow((ob1.y - ob2.y), 2));
	}
}

public class Space extends plane{
	public int z;
	
	Space(int x, int y, int z){
		super(x, y);
		System.out.println("x: " + x + ", y: " + y + ", z: " + z);
		this.z = z;
	}

	public double distence(Space ob1, Space ob2){
		double dis;

		dis = Math.pow((ob1.z - ob2.z), 2);
		dis += Math.pow((ob1.y - ob2.y), 2);
		dis += Math.pow((ob1.x - ob2.x), 2);
		dis =  Math.sqrt(dis);

		return dis;
	}

    public static void main(String[] args){
		Space ob1;
		Space ob2;
		plane ob3;
		plane ob4;
		
		ob1 = new Space(0, 1, 1);
 		ob2 = new Space(1, 2, 2);
		ob3 = new plane(0, 1);
 		ob4 = new plane(1, 2);
		
		System.out.println("distence between ob1 and ob2: " + ob1.distence(ob1, ob2));
		System.out.println("distence between ob3 and ob4: " + ob3.distence(ob3, ob4));
	}
}
