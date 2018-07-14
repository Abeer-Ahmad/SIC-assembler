package parserImpl;

import parserInterafces.IStatement;

public class Statement implements IStatement {
	
	private String location;
	private String label;
	private String operation;
	private String operands;
	private String objectCode;
	private String error;
	
	public Statement() {
		location = "";
		label = "";
		operation = "";
		operands = "";
		objectCode = "";
		error = "";
	}
	
	// to be removed
	public Statement(String statement) {
		this.label = null;
		this.operands = null;
		this.objectCode = null;
		this.error = null;
		setStatement(statement);
	}

	private void setStatement(String statement) {
		String[] split = statement.split("\\s+");
		int i = 0;
		location = split[i++];
		if (statement.charAt(5) != ' ') // label exists (starting at index 5 of the string)
			label = split[i++];
		operation = split[i++].toUpperCase();
		if (i < split.length)
			operands = split[i];		
	}
	
	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public void setOperands(String operands) {
		this.operands = operands;
	}

	@Override
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}

	@Override
	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String location() {
		return location;
	}

	@Override
	public String label() {
		return label;
	}

	@Override
	public String operation() {
		return operation;
	}

	@Override
	public String operands() {
		return operands;
	}

	@Override
	public String objectCode() {
		return objectCode;
	}

	@Override
	public String error() {
		return error;
	}
	@Override
	public String toString(){
	  return location+" "+label+" "+operation+" "+operands;
	}

}
