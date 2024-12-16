package superapp.data;

public class CommandId {

	private String superapp;
	private String miniapp;
	private String internalCommandId;

	public CommandId() {

	}

	public CommandId(String miniApp) {
		super();
		this.miniapp = miniApp;
	}

	public String getMiniApp() {
		return miniapp;
	}

	public void setMiniApp(String miniApp) {
		this.miniapp = miniApp;
	}

	public String getInternalCommandId() {
		return internalCommandId;
	}

	public void setInternalCommandId(String internalCommandId) {
		this.internalCommandId = internalCommandId;
	}

	public String getSuperapp() {
		return superapp;
	}

	public void setSuperapp(String superapp) {
		this.superapp = superapp;
	}

}
