package vo;

public class Toy {
	
	private int toyNo;
	private String toyName;
	private int toyStock;
	private int point;
	
	public Toy() {}

	public int getToyNo() {
		return toyNo;
	}

	public void setToyNo(int toyNo) {
		this.toyNo = toyNo;
	}

	public String getToyName() {
		return toyName;
	}

	public void setToyName(String toyName) {
		this.toyName = toyName;
	}

	public int getToyStock() {
		return toyStock;
	}

	public void setToyStock(int toyStock) {
		this.toyStock = toyStock;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}