package parserInterafces;

public interface IStatement {

	/**
	 * @param location -in hexadecimal- of the current statement
	 */
	public void setLocation(String location);

	/**
	 * @param label -if exists- of the current statement
	 */
	public void setLabel(String label);

	/**
	 * @param operation/directive of the current statement
	 */
	public void setOperation(String operation);

	/**
	 * @param operand(s) of the current statement (ALPHA - ALPHA,X - etc...)
	 */
	public void setOperands(String operands);

	/**
	 * @param object code of the current statement
	 */
	public void setObjectCode(String objectCode);

	/**
	 * @param error message in case of a non-executable statement
	 */
	public void setError(String error);

	/**
	 * @return location -in hexadecimal- of the current statement, or "" otherwise
	 */
	public String location();

	/**
	 * @return label -if exists- of the current statement, or "" otherwise
	 */
	public String label();

	/**
	 * @return operation/directive of the current statement
	 */
	public String operation();

	/**
	 * @return operand(s) of the current statement, or "" otherwise
	 */
	public String operands();

	/**
	 * @return object code of the current statement, or "" otherwise
	 */
	public String objectCode();

	/**
	 * @return error message in case of a non-executable statement, or "" otherwise
	 */
	public String error();

}