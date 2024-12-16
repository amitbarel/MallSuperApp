package superapp.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
	private static final long serialVersionUID = -2509731730564940439L;

	public ForbiddenException() {
	}

	public ForbiddenException(String message) {
		super(message);
		System.err.println(message);
	}
}
