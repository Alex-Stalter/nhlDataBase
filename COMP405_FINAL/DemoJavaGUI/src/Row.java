
// Class to transfer data from database to GUI table
class Row {
	public String zero;
	public String one;
	public String two;
	public String three;
	public String four;
	public String five;
	public String six;
	public String seven;
	public String eight;
	public String nine;
	Row(String artist, String title, String Goals, String three, String four, String five,
		 String six, String seven, String eight, String nine) {
		if(Goals==""){
			this.zero = artist;
			this.one = title;
		}else if(three==""){
			this.zero = artist;
			this.one = title;
			this.two = Goals;
		}else {
			this.zero = artist;
			this.one = title;
			this.two = Goals;
			this.three = three;
			this.four = four;
			this.five = five;
			this.six = six;
			this.seven = seven;
			this.eight = eight;
			this.nine = nine;
		}
	}
}