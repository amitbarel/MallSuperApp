package superapp.data;

public class UserId {
	
	public UserId(String superapp, String email) {
		super();
		this.superapp = superapp;
		this.email = email;
	}


	private String superapp;
	private String email;
	
	
	public UserId(String email) {
		super();
		this.email = email;
	}

	public UserId() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public UserId setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getSuperapp() {
		return superapp;
	}
	
	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}


	@Override
	public String toString() {
		return "UserId [superapp=" + superapp + ", email=" + email + "]";
	}
	
	
	
	
	
	

}
