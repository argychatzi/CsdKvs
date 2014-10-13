package write;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writetofile {
	public static void main(String[] args){
		String input = "key value store";
		FileWriter fwObj;
		try {
			fwObj = new FileWriter("C:\\Users\\jawadmunir\\CSD\\db.txt");
			BufferedWriter bwObj= new BufferedWriter(fwObj);
			bwObj.write(input);
			bwObj.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
