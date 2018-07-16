package ItemsEnchantment;

public class SliceInfo{
		private String generated_on;
		private String slice;
		private String version;
		public String getGenerated() {
			return this.generated_on;
		}
		 public String getSlice() {
			 return this.slice;
		 }
		 
		 public String getversion() {
			 return this.version;
		 }
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.generated_on+" "+this.slice+" "+this.version;
		}
}
	
	
