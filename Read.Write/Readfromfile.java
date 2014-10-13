package read;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Readfromfile {
	public static void main(String[] args) throws FileNotFoundException{
		FileReader frObj;
		try {
			frObj = new FileReader("C:\\Users\\jawadmunir\\CSD\\db.txt");
			BufferedReader brObj = new BufferedReader(frObj);
			String str;
			
			while((str=brObj.readLine()) != null){
				System.out.println("the file contains\n");
				System.out.print(str);
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}
