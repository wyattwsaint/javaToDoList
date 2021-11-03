package toDoList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.sql.*;

public class toDoList {
	
	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://192.168.100.174/db";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";


	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		
		BufferedReader bchoice = new BufferedReader(new FileReader("taskList.txt"));

		while (bchoice.readLine() != null) {
			System.out.println("Press e to enter a task, press q to quit/print list, or press d to delete a task");
			Scanner scanner = new Scanner(System.in);
			String choice = scanner.nextLine();
			if (choice.equals("e")) {
				addTaskAndStore();
			} else if (choice.equals("q")) {
				printTodoListToTerminal();
				break;
			} else if (choice.equals("d")) {
				deleteTask();
				copyTempToTaskList();
			}
		}
		
	}

	public static void addTaskAndStore() throws IOException, ClassNotFoundException, SQLException {
		System.out.println("Enter task: ");
		Scanner scanner = new Scanner(System.in);
		String task = scanner.nextLine();
		BufferedWriter bw = new BufferedWriter(new FileWriter("taskList.txt", true));
		bw.write(task);
		bw.newLine();
		bw.flush();
		printTodoListToTerminal();
		databaseInsert(task);
	}

	public static void printTodoListToTerminal() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("taskList.txt"));
		String st = br.readLine();
		while (st != null) {
			System.out.println(st);
			st = br.readLine();
		}
	}

	public static void deleteTask() throws IOException, ClassNotFoundException, SQLException {
		printTodoListToTerminal();
		BufferedReader br = new BufferedReader(new FileReader("taskList.txt"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("temp.txt"));
		System.out.println("Enter task to delete: ");
		Scanner scanner = new Scanner(System.in);
		String deletedTask = scanner.nextLine();

		String line;
		while ((line = br.readLine()) != null) {
			if (!line.equals(deletedTask)) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			} else if (line.equals(deletedTask)) {
				System.out.println("Deleted " + line);
			}
		}
		databaseDelete(deletedTask);
	
	}
	
	public static void copyTempToTaskList() throws IOException {
		InputStream is = null;
		OutputStream os = null;
		is = new FileInputStream("temp.txt");
		os = new FileOutputStream("taskList.txt");
		byte[] tempToTaskList = new byte[1024];
		int length;
		while ((length = is.read(tempToTaskList)) > 0) {
			os.write(tempToTaskList, 0, length);
		}
		is.close();
		os.close();
	}
	
	static void databaseInsert(String task) throws ClassNotFoundException, SQLException {
		
		Connection conn = null;
        Statement stmt = null;
            //STEP 2: Register JDBC driver
        	Class.forName("org.mariadb.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "root", "root");
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Inserting task in given table...");
            stmt = conn.createStatement();

            String sql = "insert into testtable (task) VALUES " + "('" + task + "')";
            

            stmt.executeUpdate(sql);
            System.out.println("Inserted task in given database table...");
		
	}
	
	static void databaseDelete(String task) throws ClassNotFoundException, SQLException {
		
		Connection conn = null;
        Statement stmt = null;
            //STEP 2: Register JDBC driver
        	Class.forName("org.mariadb.jdbc.Driver");
            
            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "root", "root");
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Deleting task in given table...");
            stmt = conn.createStatement();

            String sql = "delete from testtable where task = " + "('" + task + "')";
            

            stmt.executeUpdate(sql);
            System.out.println("Deleted task in given database table...");
		
	}

}
