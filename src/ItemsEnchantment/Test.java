package ItemsEnchantment;


public class Test {

	public static void main(String[] args) {
		/*
		MyExtractorMultithread t1 = new MyExtractorMultithread("uno", "0", "1500");
		t1.start();
		*/
		MyExtractorMultithread t2 = new MyExtractorMultithread("due", "0", "1500");		
		t2.start();
		/*
		MyExtractorMultithread t3 = new MyExtractorMultithread("tre", "25000","12500");		
		t3.start();
		MyExtractorMultithread t4 = new MyExtractorMultithread("quattro", "37500","12500");		
		t4.start();	
		*/	
		
	}

}
