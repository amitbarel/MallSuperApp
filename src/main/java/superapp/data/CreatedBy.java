package superapp.data;

public class CreatedBy {
	
	private UserId userId;

	public CreatedBy() {
		super();
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CreatedBy [userId=" + userId + "]";
	}

	

}
