/**
 * 
 */
package cn.service;


/**
 * @author zjiang
 *
 */
public class WSException extends Exception {
	private ErrorCode errorCode;
	
	/**
	 * 
	 */
	public WSException(ErrorCode errorCode) {
		super(errorCode.getDesc());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode(){
		return this.errorCode;
	}
	
	public String getErrorMsg(){
		return this.errorCode.getDesc();
	}
	
	public String toString(){
		return this.errorCode.toString();
	}
	
}
