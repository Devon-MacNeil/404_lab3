package textdb;

/**
TableHandler.java - A java program for accessing and updating a database table stored as a tab
separated text file.  Each record is stored on a new line. Implement the following methods associated
with a menu item. The run() method will prompt for the input required for execution of these methods

1 - void readAll()
2 - String findRecord(String key )
3 - void insertRecord(String record)
4 - void deleteRecord(String key)
5 - void updateRecord(String key, int col, String value)

Helper method - long findStartOfRecord(String key) to find byte offset from start of file for start of record.
*/

import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;

public class TableHandler {
	private BufferedReader reader; // Reader from console for user input
	RandomAccessFile raFile; // File to manipulated
	private String columnNames; // First row of file is a tab separated list of
								// column names

	public static void main(String[] args) {
		TableHandler app = new TableHandler();
		app.init();

		try {
			app.run();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	private void init() {
		raFile = null;
		reader = new BufferedReader(new InputStreamReader(System.in)); // Set up
																		// console
																		// reader
	}

	private void run() throws SQLException { // Continually read command from
												// user in a loop and then do
												// the requested query
		String choice;
		fileCreate();
		printMenu();
		choice = getLine();
		while (!choice.equals("X")) {
			if (choice.equals("1")) { // Read in and output entire raFile using
										// API for class RandomAccessFile
				System.out.println(readAll());
			} else if (choice.equals("2")) { // Locate and output the record
												// with the following key
				System.out.print("Please enter the key to locate the record: ");
				String key = getLine().trim();
				System.out.println(findRecord(key));
			} else if (choice.equals("3")) { // Prompt for a record to insert,
												// then append to raFile
				System.out.print("Enter the record:");
				String record = getLine().trim();
				insertRecord(record);
			} else if (choice.equals("4")) { // prompt for key of record to
												// delete
				System.out.print("Please enter the key to locate the record to delete: ");
				String key = getLine().trim();
				deleteRecord(key);
			} else if (choice.equals("5")) { // read in key value and call
												// update method
				System.out.print("Enter key of record to update: ");
				String key = getLine();
				System.out.print("Enter column number of field to update: ");
				String column = getLine();
				int col = Integer.parseInt(column);
				System.out.print("Enter value of field to update: ");
				String value = getLine();
				updateRecord(key, col, value);
			} else if (choice.equals("F")) { // Specify the File to Manipulate
				fileCreate();
			} else
				System.out.println("Invalid input!");

			printMenu();
			choice = getLine();
		}

		try {
			raFile.close();
		} catch (IOException io) {
			System.out.println(io);
		}
	}

	/****************************************************************************************
	 * readAll() reads each record in RandomAccessFile raFile and outputs each
	 * record to a String. Each record should be on its own line (add a "\n" to
	 * end of each record). Note: You do not have to parse each record. Just
	 * append the whole line (make sure to trim() input). Catch any IOException
	 * and re-throw it as a SQLException if any error occurs.
	 * 
	 * @throws IOException
	 *************************************************************************************/
	public String readAll() throws SQLException {
		/*
		 * Reads the text file using readLine() from the random access file and
		 * puts the line into a string builder. A boolean controls the while
		 * loop. An if statement checks to see if the nextLine is null and sets
		 * the boolean to false if it is.
		 */
		StringBuilder output = new StringBuilder();
		boolean checkNull = true;
		try {
			raFile.seek(0);
			while (checkNull) {
				String temp = raFile.readLine();
				if (temp != null) {
					output.append(temp.trim() + "\n");
				} else {
					checkNull = false;
				}
			}

			return output.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}

	}

	/**************************************************************************************
	 * findRecord() takes parameter key holding the key of the record to return,
	 * Returns a String consisting of the columnNames string an EOL, any record
	 * if found, and an EOL. If no record is found, should still return the
	 * columnNames string and an EOL. Catch any IOException and re-throw it as a
	 * SQLException if any error occurs.
	 **************************************************************************/
	public String findRecord(String key) throws SQLException {
		/*
		 * .At the start moves the read/write pointer to the start of the
		 * desired recored using seek. Reads desired line of the text file using
		 * readLine and then puts the line in a temp string. As asked prints out
		 * a string of the column names whether or not a record is found.
		 * returns a string
		 */
		StringBuilder output = new StringBuilder();
		try {
			long x = findStartOfRecord(key);
			System.out.println("offset: " + x);

			raFile.seek(x);
			if (x != 0) {
				output.append("ProductID	ProductName	SupplierID	CategoryID\n");
			}
			String temp = raFile.readLine();
			if (temp != null) {

				output.append(temp.trim() + "\n");

			}
			return output.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}
	}

	/**************************************************************************************
	 * findStartOfRecord(String key) takes parameter key holding the key of the
	 * record to find returns the cursor position of located record
	 *
	 * Helper method to locate a record and find location of cursor Catch any
	 * IOException and re-throw it as a SQLException if any error occurs.
	 **************************************************************************/
	private long findStartOfRecord(String key) throws SQLException {
		/*
		 * Sets the initial pointer location to the start of the file Inside a
		 * while loop gets the current pointer location and the next line of
		 * text checks to see if the string is = to null. then checks to see if
		 * the line of text starts with the key. If it starts with the key the
		 * boolean the controls the loop is set to false so the to pend the loop
		 * and get the pointer location before the key. returns a long
		 * 
		 */
		long pointer = 0;
		boolean checkNull = true;
		try {
			System.out.println("key: " + key);
			raFile.seek(0);
			while (checkNull) {
				pointer = raFile.getFilePointer();
				String temp = raFile.readLine();
				if (temp != null) {
					if (temp.startsWith(key)) {
						return pointer;

					}
				} else {
					checkNull = false;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
			throw new SQLException(e);

		}
		return 0;
	}

	/*************************************************************************************
	 * updateRecord(String key, int col, String value) parameters key - key
	 * value of record to update col - column of field to update value - new
	 * value for this field of the record Returns the count of how many records
	 * were updated (either 0 or 1).
	 *
	 * method must find the record with key and update the field. The updated
	 * record must be put back in the file with all other records maintained.
	 * Catch any IOException and re-throw it as a SQLException if any error
	 * occurs.
	 ************************************************************************************/
	public int updateRecord(String key, int col, String value) throws SQLException { 
		/*
		 * gets the location for the update using findStartOfRecord. Gets the record and splits it into an array of strings
		 * changes the value in the indicated column. Then overwrites the old record with white space. Builds new record using string
		 * builder. turns new string into array of bytes. Then checks to see if the new record is larger than the old 1. If it isn't
		 * it just writes the new record into the file. If it is it increases the file size by the new record size and then makes room
		 * for the new record in a loop. It then writes the new record using the created byte array.
		 */
		long recordLocation = findStartOfRecord(key);
		StringBuilder output = new StringBuilder();
		boolean checkLoc = true;
		try{
			raFile.seek(recordLocation);
			String record = raFile.readLine();
			long recordLength = record.length();
			String recordArray[] = record.split("\\t+");
	        
			recordArray[col-1] = value;
			
			raFile.seek(recordLocation);
			for(int i=0;i<recordLength;i++){
				raFile.writeBytes(" ");
			}
			for(int i=0;i<recordArray.length;i++){
				output.append((recordArray[i])+"\t");
			}
			String out = output.toString();
			byte[] stringtoBytes = out.getBytes();
			if((output.toString().length())>(recordLength)){
				long fileLength = raFile.length();
				long newLength = fileLength + output.toString().length();
				long writePointer = newLength-1;
				long readPointer = fileLength-1;
				int readByte;
				while(checkLoc){
					if(readPointer == (recordLocation-1)){
						checkLoc = false;
					}else{
						raFile.seek(readPointer);
						readByte = raFile.read();
						readPointer--;
						raFile.seek(writePointer);
						raFile.write(readByte);
						writePointer--;
					}
				}
				
				raFile.seek(recordLocation);
				System.out.println(output.toString());
				for(int i =0; i<stringtoBytes.length;i++){
					raFile.write(stringtoBytes[i]);
				}
	        }else{
	        	raFile.seek(recordLocation);
				raFile.writeBytes(output.toString());
	        }
			return 1;
		}catch (IOException e) {

			e.printStackTrace();
			throw new SQLException(e);

		}
	
	}

	/**************************************************************************************
	 * deleteRecord() takes parameter key holding the key of the record to
	 * delete the method must maintain validity of entire text file
	 *
	 * Returns the count of how many records were deleted (either 0 or 1).
	 * Locate a record, delete the record, rewrite the rest of file to remove
	 * empty space of deleted record Catch any IOException and re-throw it as a
	 * SQLException if any error occurs.
	 **************************************************************************/
	public int deleteRecord(String key) throws SQLException {
		/*
		 * Finds the pointer location of the record to delete using the findStartOfRecord with the record key
		 * Then checks to see if the pointer is at zero, if at there nothing is deleted.
		 * initializes a writerPointer and a readPointer and a temp int
		 * in the while loop reads the file byte by byte and then writes over the record to be deleted byte
		 * by byte. It then sets a new length for the file so that it deletes extra rows
		 */
		long x = findStartOfRecord(key);
		boolean checkNull = true;

		try {

			if (x == 0) {
				return 0;
			} else {
				raFile.seek(x);
				long recordLength = raFile.readLine().length();// never used but required for it to work
				long writePointer = x;
				long readPointer = raFile.getFilePointer();
				int temp;
				raFile.seek(readPointer);
				temp = raFile.read();

				while (checkNull) {
					raFile.seek(readPointer);
					temp = raFile.read();
					readPointer = raFile.getFilePointer();
					if (temp != -1) {
						raFile.seek(writePointer);
						raFile.write(temp);
						writePointer = raFile.getFilePointer();
					} else {
						checkNull = false;
					}
				}
				long newFileLength = writePointer;
				raFile.setLength(newFileLength);
				return 1;
			}
		} catch (IOException e) {

			e.printStackTrace();
			throw new SQLException(e);

		}
	}

	/**************************************************************************************
	 * insertRecord() Appends records to end of file. the method must maintain
	 * validity of entire text file
	 *
	 * Return 1 if record successfully inserted, 0 otherwise. Catch any
	 * IOException and re-throw it as a SQLException if any error occurs.
	 **************************************************************************/
	public int insertRecord(String record) throws SQLException { 
		// writes the string to the end of the file
		try{
		record+="\n";
		long fileLength = raFile.length();
		raFile.seek(fileLength);
		raFile.writeBytes(record);
		return 1; 
		} catch (IOException e) {

			e.printStackTrace();
			throw new SQLException(e);

		}
	}

	/**********************************************************************************
	 * The below methods read in a line of input from standard input and create
	 * a RandomAccessFile to manipulate. printMenu() prints the menu to enter
	 * options
	 *
	 * The code works and should not need to be updated.
	 ************************************************************************/
	// Input method to read a line from standard input
	private String getLine() {
		String inputLine = "";
		try {
			inputLine = reader.readLine();
		} catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		} // end catch
		return inputLine;
	}

	private void fileCreate() {
		System.out.print("Enter the file name to manipulate:");
		String fileName = getLine();
		try {
			fileCreate(fileName);
		} catch (SQLException e) {
			System.out.println("Error opening file: " + fileName + ". " + e);
		}
	}

	// Creates a RandomAccessFile object from text file
	public void fileCreate(String fileName) throws SQLException {
		// Create a RandomAccessFile with read and write privileges
		try {
			raFile = new RandomAccessFile(fileName, "rw");
			if (raFile.length() < 1)
				System.out.println("File has " + raFile.length() + " bytes. Is the file name correct?");
			// Read the first row of column names
			columnNames = raFile.readLine().trim();
			// Go back to start of file
			raFile.seek(0);
		} catch (FileNotFoundException fnf) {
			throw new SQLException("File not found: " + fileName);
		} catch (IOException io) {
			throw new SQLException(io);
		}
	}

	public void close() throws SQLException {
		if (raFile != null) {
			try {
				raFile.close();
			} catch (IOException e) {
				throw new SQLException(e);
			}
		}
	}

	private void printMenu() {
		System.out.println("\n\nSelect one of these options: ");
		System.out.println("  1 - Read and Output All Lines");
		System.out.println("  2 - Return Record with Key");
		System.out.println("  3 - Insert a New Record");
		System.out.println("  4 - Delete Record with Key");
		System.out.println("  5 - Update Record with Key");
		System.out.println("  F - Specify Different Data File");
		System.out.println("  X - Exit application");
		System.out.print("Your choice: ");
	}
}
