package utilities;

public enum UserType {
	INSTRUCTOR("iyte.edu.tr"), 
	STUDENT("std.iyte.edu.tr"), 
	TEACHING_ASSISTANT("iyte.edu.tr");
	
	public final String domain;

    private UserType(String domain) {
        this.domain = domain;
    }
}
