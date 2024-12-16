package superapp.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import superapp.restApi.boundaries.InvokedBy;
import superapp.restApi.boundaries.TargetObjectBoundary;

import org.springframework.data.annotation.Id;

@Document(collection = "MiniApp")
public class MiniAppCommandEntity {
	@Id
	private String id;
	private String internalCommandId;
	private String miniApp;
	private String command;
	private Date invocationTimeStamp;
	private TargetObjectBoundary targetObject;
	private InvokedBy invokedBy;
	private Map<String,Object> commandAttributes;
	
	public MiniAppCommandEntity() {
		commandAttributes = new HashMap<>();
	}
	
	public MiniAppCommandEntity(String id, String internalCommandId, String miniApp, String command,
			Date invocationTimeStamp, TargetObjectBoundary targetObject, InvokedBy invokedBy,
			Map<String, Object> commandAttributes) {
		super();
		this.id = id;
		this.internalCommandId = internalCommandId;
		this.miniApp = miniApp;
		this.command = command;
		this.invocationTimeStamp = invocationTimeStamp;
		this.targetObject = targetObject;
		this.invokedBy = invokedBy;
		this.commandAttributes = commandAttributes;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	public String getInternalCommandId() {
		return internalCommandId;
	}

	public void setInternalCommandId(String internalCommandId) {
		this.internalCommandId = internalCommandId;
	}


	public String getMiniApp() {
		return miniApp;
	}


	public void setMiniApp(String miniApp) {
		this.miniApp = miniApp;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public Date getInvocationTimeStamp() {
		return invocationTimeStamp;
	}


	public void setInvocationTimeStamp(Date invocationTimeStamp) {
		this.invocationTimeStamp = invocationTimeStamp;
	}

	public TargetObjectBoundary getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(TargetObjectBoundary targetObject) {
		this.targetObject = targetObject;
	}

	public InvokedBy getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}


	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}
	

	
}
