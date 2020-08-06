package io.CarService.exceptionhandler;

public class DuplicateDataNotAllowedException extends Exception{

	private static final long serialVersionUID = 1L;

	public DuplicateDataNotAllowedException(String message) {
		super(message);
	}
	
}
