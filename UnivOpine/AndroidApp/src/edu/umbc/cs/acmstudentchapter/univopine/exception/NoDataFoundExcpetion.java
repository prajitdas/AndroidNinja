/**
 * 
 */
package edu.umbc.cs.acmstudentchapter.univopine.exception;

/**
 * @author Prajit
 *
 */
public class NoDataFoundExcpetion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	/**
	 * @return 
	 * 
	 */
	public NoDataFoundExcpetion() {
		setMessage(getMessage() + "Some error!");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}