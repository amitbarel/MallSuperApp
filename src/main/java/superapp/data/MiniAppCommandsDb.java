package superapp.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import superapp.data.exceptions.ForbiddenException;
import superapp.data.exceptions.MiniAppCommandException;
import superapp.data.exceptions.ObjectNotFoundException;
import superapp.data.exceptions.UserNotFoundException;
import superapp.logic.MiniAppCommandsCrud;
import superapp.logic.MiniAppCommandsQueries;
import superapp.logic.ObjectCrud;
import superapp.logic.UserCrud;
import superapp.restApi.boundaries.MiniAppCommandBoundary;

@Service
public class MiniAppCommandsDb implements MiniAppCommandsQueries {

	private MiniAppCommandsCrud miniAppCommandsCrud;
	private UserCrud userCrud;
	private ObjectCrud objectCrud;
	private String superapp;
	private char delimeter = '_';
	private ObjectMapper jackson;
	private JmsTemplate jmsTemplate;

	@Autowired
	public void setMiniAppCommandsCrud(MiniAppCommandsCrud miniAppCommandsCrud) {
		this.miniAppCommandsCrud = miniAppCommandsCrud;
	}

	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Autowired
	public void setObjectCrud(ObjectCrud objectCrud) {
		this.objectCrud = objectCrud;
	}

	@PostConstruct
	public void setup() {
		this.jackson = new ObjectMapper();
		this.jmsTemplate.setDeliveryDelay(3000L);
	}

	@Autowired
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@Value("${spring.application.name:2023b.shir.zur}")
	public void setSuperapp(String name) {
		this.superapp = name;
	}

	@Deprecated
	@Override
	public Object invokeCommand(MiniAppCommandBoundary command) {
		String userSuperapp = command.getInvokedBy().getUserId().getSuperapp();
		String userEmail = command.getInvokedBy().getUserId().getEmail();
		UserEntity userEntity = this.userCrud.findById(userSuperapp + delimeter + userEmail)
				.orElseThrow(() -> new UserNotFoundException(
						"Could not find User with superapp = " + userSuperapp + " and email = " + userEmail));

		String objcetSuperapp = command.getTargetObject().getObjectId().getSuperapp();
		String objectInternalId = command.getTargetObject().getObjectId().getInternalObjectId();
		ObjectEntity objectEntity = this.objectCrud.findById(objcetSuperapp + delimeter + objectInternalId)
				.orElseThrow(() -> new ObjectNotFoundException("Could not find object with superapp = " + objcetSuperapp
						+ " and internalObjectId = " + objectInternalId));
		;

		if (userEntity.getRole() != UserRole.MINIAPP_USER || objectEntity.getActive() == false) {
			throw new ForbiddenException("User not allowed to make this operation");
		}

		command.getCommandId().setInternalCommandId(UUID.randomUUID().toString());
		if (objcetSuperapp != null)
			command.getCommandId().setSuperapp(this.superapp);
		else
			throw new MiniAppCommandException("Superapp inside commandId can not be empty!");
		if (command.getInvokedBy().getUserId().getSuperapp() != null)
			command.getCommandId().setSuperapp(this.superapp);
		else
			throw new MiniAppCommandException("Superapp inside userId can not be empty!");

		MiniAppCommandEntity entity = this.toEntity(command);
		entity = this.miniAppCommandsCrud.save(entity);
		return (MiniAppCommandBoundary) toBoundary(entity);
	}

	private MiniAppCommandBoundary toBoundary(MiniAppCommandEntity entity) {
		MiniAppCommandBoundary boundary = new MiniAppCommandBoundary();
		boundary.setCommand(entity.getCommand());
		boundary.setCommandId(new CommandId(entity.getMiniApp()));
		boundary.getCommandId().setSuperapp(this.superapp);
		boundary.getCommandId().setInternalCommandId(entity.getId().split("_")[2]);
		boundary.setInvocationTimestamp(entity.getInvocationTimeStamp());
		boundary.setCommandAttributes(entity.getCommandAttributes());
		boundary.setInvokedBy(entity.getInvokedBy());
		boundary.setTargetObject(entity.getTargetObject());
		return boundary;
	}

	private MiniAppCommandEntity toEntity(MiniAppCommandBoundary command) {
		MiniAppCommandEntity entity = new MiniAppCommandEntity();
		if (command.getCommand() == null || command.getCommand().isEmpty()) {
			throw new MiniAppCommandException("Command can not be empty");
		} else {
			entity.setCommand(command.getCommand());
		}
		entity.setInvocationTimeStamp(new Date());
		if (command.getInvokedBy() == null || command.getInvokedBy().getUserId().getEmail() == null
				|| command.getInvokedBy().getUserId().getEmail().isEmpty()
				|| command.getInvokedBy().getUserId().getSuperapp().isEmpty()) {
			throw new MiniAppCommandException("InvokedBy can not be empty");
		} else {
			command.getInvokedBy().getUserId().setSuperapp(this.superapp);
			entity.setInvokedBy(command.getInvokedBy());
		}
		entity.setMiniApp(command.getCommandId().getMiniApp());

		/*
		 * The result for entity.setId() id = superapp_miniApp_internalCommandID
		 * 
		 */
		String id = command.getCommandId().getSuperapp() + delimeter + command.getCommandId().getMiniApp() + delimeter
				+ command.getCommandId().getInternalCommandId();

		entity.setId(id);
		entity.setInternalCommandId(command.getCommandId().getInternalCommandId());
		if (command.getTargetObject() == null || command.getTargetObject().getObjectId().getInternalObjectId() == null
				|| command.getTargetObject().getObjectId().getInternalObjectId().isEmpty()
				|| command.getTargetObject().getObjectId().getSuperapp().isEmpty()) {
			throw new MiniAppCommandException("TargetObject can not be empty or null");
		} else {
			command.getTargetObject().getObjectId().setSuperapp(this.superapp);
			entity.setTargetObject(command.getTargetObject());
		}
		if (command.getCommandAttributes() != null) {
			entity.setCommandAttributes(command.getCommandAttributes());
		} else {
			entity.setCommandAttributes(new HashMap<>());
		}
		return entity;
	}

	@Deprecated
	@Override
	public List<MiniAppCommandBoundary> getAllCommands() {
		Iterable<MiniAppCommandEntity> iterable = this.miniAppCommandsCrud.findAll();
		Iterator<MiniAppCommandEntity> iterator = iterable.iterator();
		List<MiniAppCommandBoundary> list = new ArrayList<>();
		while (iterator.hasNext()) {
			MiniAppCommandBoundary boundary = toBoundary(iterator.next());
			list.add(boundary);
		}
		return list;
	}

	@Deprecated
	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName) {

		Iterable<MiniAppCommandEntity> iterable = this.miniAppCommandsCrud.findAll();
		Iterator<MiniAppCommandEntity> iterator = iterable.iterator();
		List<MiniAppCommandBoundary> list = new ArrayList<>();
		while (iterator.hasNext()) {
			MiniAppCommandBoundary boundary = toBoundary(iterator.next());
			if (boundary.getCommandId().getMiniApp().equals(miniAppName))
				list.add(boundary);
		}
		return list;
	}

	@Deprecated
	@Override
	public void deleteAllCommands() {
		this.miniAppCommandsCrud.deleteAll();
	}

	@Override
	public MiniAppCommandBoundary invokeCommandWithAsyncOption(MiniAppCommandBoundary miniAppCommandBoundary,
			boolean isAsync) {
		miniAppCommandBoundary.getCommandId().setInternalCommandId(UUID.randomUUID().toString());
		miniAppCommandBoundary.setInvocationTimestamp(new Date());
		if (miniAppCommandBoundary.getTargetObject() == null) {
			throw new ObjectNotFoundException("The object that the command was invoked on was null");
		}

		String objSuperapp = miniAppCommandBoundary.getTargetObject().getObjectId().getSuperapp();
		String objIntId = miniAppCommandBoundary.getTargetObject().getObjectId().getInternalObjectId();
		String objEmail = miniAppCommandBoundary.getInvokedBy().getUserId().getEmail();

		UserEntity userEntity = this.userCrud.findById(objSuperapp + delimeter + objEmail)
				.orElseThrow(() -> new UserNotFoundException(
						"could not find User with superapp = " + objSuperapp + " and email = " + objEmail));
		if (userEntity.getRole() != UserRole.MINIAPP_USER) {
			throw new ForbiddenException("This action is not allowed");
		} else {
			ObjectEntity objectEntity = this.objectCrud.findById(objSuperapp + delimeter + objIntId)
					.orElseThrow(() -> new ObjectNotFoundException("Could not find object with superapp = "
							+ objSuperapp + " and internalObjectId = " + objIntId));
			if (objectEntity.getActive() == false) {
				throw new ForbiddenException("It is not allowed to operate on a non-active object");
			}
			if (objIntId != null)
				if (objSuperapp != null)
					miniAppCommandBoundary.getCommandId().setSuperapp(this.superapp);
				else
					throw new MiniAppCommandException("Superapp inside commandId can not be empty!");
			if (miniAppCommandBoundary.getInvokedBy().getUserId().getSuperapp() != null)
				miniAppCommandBoundary.getCommandId().setSuperapp(this.superapp);
			else
				throw new MiniAppCommandException("Superapp inside userId can not be empty!");
			if (isAsync) {
				try {
					String json = this.jackson.writeValueAsString(miniAppCommandBoundary);
					this.jmsTemplate.convertAndSend("handleLaterCommand", json);
					return miniAppCommandBoundary;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				MiniAppCommandEntity entity = this.toEntity(miniAppCommandBoundary);
				entity = this.miniAppCommandsCrud.save(entity);
				return (MiniAppCommandBoundary) toBoundary(entity);
			}

		}

	}

	@JmsListener(destination = "handleLaterCommand")
	public void listenToCommand(String json) {
		try {
			System.err.println("*** received: " + json);
			MiniAppCommandBoundary miniAppCommandBoundary = this.jackson.readValue(json, MiniAppCommandBoundary.class);
			if (miniAppCommandBoundary.getCommandId().getInternalCommandId() == null) {
				miniAppCommandBoundary.getCommandId().setInternalCommandId(UUID.randomUUID().toString());
			}
			if (miniAppCommandBoundary.getInvocationTimestamp() == null) {
				miniAppCommandBoundary.setInvocationTimestamp(new Date());
			}
			MiniAppCommandEntity entity = this.toEntity(miniAppCommandBoundary);
			entity = this.miniAppCommandsCrud.save(entity);
			System.err.println("*** saved: " + this.toBoundary(entity));
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	@Override
	public List<MiniAppCommandBoundary> getSpecificMiniAppCommandsAdminOnly(String miniAppName, String superapp,
			String email, int size, int page) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"could not find User with superapp = " + superapp + " and email = " + email));
		if (userEntity.getRole() == UserRole.ADMIN)
			return this.miniAppCommandsCrud
					.findAllByMiniApp(miniAppName, PageRequest.of(page, size, Direction.DESC, "id")).stream()
					.map(this::toBoundary).toList();
		else
			throw new ForbiddenException("This user does not have permission to do this");
	}

	@Override
	public void deleteAllCommandsAdminOnly(String superapp, String email) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"could not find User with superapp = " + superapp + " and email = " + email));
		if (userEntity.getRole() == UserRole.ADMIN)
			this.miniAppCommandsCrud.deleteAll();
		else
			throw new ForbiddenException("This user does not have permission to do this");
	}

	@Override
	public List<MiniAppCommandBoundary> getAllMiniAppsCommandsAdminOnly(String superapp, String email, int size,
			int page) {
		UserEntity userEntity = this.userCrud.findById(superapp + delimeter + email)
				.orElseThrow(() -> new UserNotFoundException(
						"could not find User with superapp = " + superapp + " and email = " + email));
		if (userEntity.getRole() == UserRole.ADMIN)
			return this.miniAppCommandsCrud
					.findAll(PageRequest.of(page, size, Direction.DESC, "miniApp", "internalCommandId")).stream()
					.map(this::toBoundary).toList();
		else
			throw new ForbiddenException("This user does not have permission to do this");
	}
	

}
