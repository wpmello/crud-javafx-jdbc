package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	// nessa execeção personalizada vamos usar o 'Map' para pegar nossos erros através do seus nomes
	
	private static final long serialVersionUID = 1L;

	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getError() {
		return errors;
	}
	
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
