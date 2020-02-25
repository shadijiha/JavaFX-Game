package game;

public class CodeNotImplementedException extends Exception {

	public CodeNotImplementedException() {
		super("Cannot call this function because implementation if not complete!");
	}

	public CodeNotImplementedException(String msg) {
		super(msg);
	}
}
