package superapp.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MiniAppCommandException extends RuntimeException {
	private static final long serialVersionUID = -2509731730564940439L;

	public MiniAppCommandException() {
	}

	public MiniAppCommandException(String message) {
		super(message);
		System.err.println(message);
	}
}
