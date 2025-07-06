"# SWE_Project" 
"# SWE_Project" 

## Setup Database Instructions
Before running the application, make sure to create the database manually in your MySQL server
1. Create the database  
- Go to MySQL, login
- CREATE DATABASE obs_db
2. Rename application-template.properties to application.properties 
3. Put your local MySQL login info into 
"spring.datasource.username=your_username_here
   spring.datasource.password=your_password_here"
- Spring Boot is configured to automatically create and update the tables based on the entity classes Once the database obs_db exists, Spring Boot will generate the necessary tables  when the application starts.
