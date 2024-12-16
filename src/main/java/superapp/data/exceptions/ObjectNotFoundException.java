package superapp.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -2509731730564940439L;
	
	public ObjectNotFoundException() {
	}

	public ObjectNotFoundException(String message) {
		super(message);
		System.err.println(message);
	}

	public ObjectNotFoundException(Throwable cause) {
		super(cause);
	}

	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
