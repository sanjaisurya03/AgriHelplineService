# Agriculture Service

The **Agriculture Service** project is a desktop application designed to assist farmers and agricultural professionals in managing queries and accessing expert advice. Built using **Java Swing** for a graphical interface and **JDBC** for seamless database connectivity, the project is both user-friendly and functional. The underlying database schema includes two primary tables: **users** (for authentication) and **queries** (for query management).

## Features

### 1. **User Management**
- **Admin** and **User** roles:
  - **Admin**: Respond to queries and manage the system.
  - **User**: Submit queries and view responses.
- Secure user authentication with unique usernames and hashed passwords.

### 2. **Query Submission**
- Users can submit agriculture-related queries through a simple form.
- Queries are stored in the **queries** table for further processing.

### 3. **Expert Responses**
- Admins can view and respond to user queries.
- Responses are updated in real-time and visible to users.

### 4. **Data Management**
- All user and query data is stored in the **agri_helpline** database.
- **Cascade delete** ensures that when a user is deleted, their associated queries are removed.

---

## Technologies Used

### 1. **Frontend**:
- **Java Swing**: Provides a desktop GUI for interacting with the system.

### 2. **Backend**:
- **JDBC (Java Database Connectivity)**: Facilitates communication between the application and the MySQL database.

### 3. **Database**:
- **MySQL**:
  - **users** table: Stores user credentials and roles.
  - **queries** table: Manages user queries and admin responses.
---

## Workflow

1. **User Registration**:
   - New users register with a unique username and password.
   - Admin credentials are predefined or created by a super-admin.

2. **Login System**:
   - Users and admins log in using their credentials.
   - Role-based access controls ensure secure navigation.

3. **Query Management**:
   - Users submit queries via a form.
   - Admins respond to queries and update the database.

4. **Real-Time Updates**:
   - Queries and responses are updated dynamically in the database.

---

## Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/agriculture-service.git
   ```

2. **Setup Database**:
   - Import the database schema into MySQL:
     ```sql
     CREATE DATABASE IF NOT EXISTS agri_helpline;
     USE agri_helpline;
     -- Paste table creation queries here
     ```

3. **Configure JDBC**:
   - Update the `DBConnection.java` file with your MySQL credentials:
     ```java
     String url = "jdbc:mysql://localhost:3306/agri_helpline";
     String user = "your-mysql-username";
     String password = "your-mysql-password";
     ```

4. **Run the Project**:
   - Open the project in **Eclipse IDE**.
   - Execute the `Main.java` file to launch the application.
