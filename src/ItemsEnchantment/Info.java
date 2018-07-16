package ItemsEnchantment;

public class Info{
		private String generated_on;
		private String slice;
		private String version;
		
		public String getGenerated() {
			return this.generated_on;
		}
		 public String getSlice() {
			 return this.slice;
		 }
		 
		 public String getVersion() {
			 return this.version;
		 }
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.generated_on+" "+this.slice+" "+this.version;
		}
}
	
	
