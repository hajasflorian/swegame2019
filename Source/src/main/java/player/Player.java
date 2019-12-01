package player;

public class Player {
	
	String studentFirstName;
	String studentLastName;
	String studentId;
	
	public Player() {
		this.studentFirstName = "Florian";
		this.studentLastName = "Hajas";
		this.studentId = "1207533";
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

}
