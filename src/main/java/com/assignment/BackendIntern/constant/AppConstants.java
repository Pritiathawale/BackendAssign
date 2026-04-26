package com.assignment.BackendIntern.constant;



public class AppConstants {

    private AppConstants() {} 

   
    public static final String API_BASE = "/api/v1";
    public static final String AUTH_BASE = API_BASE + "/auth";
    public static final String TASK_BASE = API_BASE + "/tasks";
    public static final String ADMIN_BASE = API_BASE + "/admin";
    
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
  
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_DONE = "DONE";
    public static final String STATUS_PATTERN = "(?i)PENDING|(?i)IN_PROGRESS|(?i)DONE";

    public static final String USER_NOT_FOUND = "User not found";
    public static final String TASK_NOT_FOUND = "Task not found";
    public static final String UNAUTHORIZED = "You are not authorized to perform this action";
    public static final String USER_ALREADY_EXISTS = "User already exists with this email";
    public static final String REGISTER_SUCCESS = "User registered successfully";
    public static final String TASK_DELETED = "Task deleted successfully";
 
    public static final String SWAGGER_UI   = "/swagger-ui/**";
    public static final String SWAGGER_DOCS = "/v3/api-docs/**";
    public static final String SWAGGER_HTML = "/swagger-ui.html";
    
    public static final String AUTH_PATH    = "/api/v1/auth/";
    public static final String SWAGGER_PATH = "/swagger-ui";
    public static final String DOCS_PATH    = "/v3/api-docs";
    
    
    public static final String NAME_REQUIRED = "Name is required";
    public static final String NAME_SIZE     = "Name must be between 2 and 50 characters";


    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID  = "Invalid email format";


    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_SIZE     = "Password must be at least 6 characters";

 
    public static final String TITLE_REQUIRED = "Title is required";
    public static final String TITLE_SIZE     = "Title must be between 2 and 100 characters";
    public static final String DESC_SIZE      = "Description cannot exceed 500 characters";
    public static final String STATUS_INVALID = "Status must be PENDING, IN_PROGRESS or DONE";
	public static final long JWT_EXPIRY_MS = 24 * 60 * 60 * 1000; 
}