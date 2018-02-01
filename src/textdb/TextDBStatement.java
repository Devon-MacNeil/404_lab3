package textdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class TextDBStatement implements Statement {

	private TableHandler table;
	private TextDBResultSet results;
	private int updateCount;

	public TextDBStatement(TableHandler table) {
		this.table = table;
	}

	@Override
	public boolean execute(String query) throws SQLException {
		if (query.toLowerCase().trim().startsWith("select")) {
			results = (TextDBResultSet) executeQuery(query);
			return true;
		} else {
			updateCount = executeUpdate(query);
			return false;
		}
	}

	@Override
	public ResultSet executeQuery(String query) throws SQLException {
		// TODO: Complete this method.

		// splits the query SELECT and KEY
		String[] q = query.split("\\s+");

		// if query contains all execute table.readAll, if not execute find
		// record using the key in the q array
		if (query.toUpperCase().contains("ALL")) {
			return new TextDBResultSet(table.readAll());
		} else {
			return new TextDBResultSet(table.findRecord(q[1]));
		}

	}

	@Override
	public int executeUpdate(String query) throws SQLException {
		// TODO: Complete this method.
		/*
		 * DELETE key (e.g. DELETE 5) INSERT record (e.g. INSERT 33 Lawrence 44
		 * 9). Note that the fields are already tab-delimited to make it easier
		 * to do the insert (can write the record directly to the file by
		 * calling appropriate method in TableHandler). UPDATE key column value
		 * (e.g. UPDATE 2 2 Change Special Company Export). Note that fields are
		 * also tab separated for convenience.
		 */
		// split the query in the case to get the query type
		String[] q = query.split("\\s+");

		switch (q[0].toLowerCase()) {
		case "delete":
			// delete the record with the key in q[1]
			return table.deleteRecord(q[1]);
		case "insert":
			// insert record after the word insert in the query
			return table.insertRecord(query.substring(6));
		case "update":
			// re-write the array q with the parameters for the update by
			// splitting the substring using tabs after 'update'
			q = query.substring(7).split("\\t+");
			return table.updateRecord(q[0], Integer.parseInt(q[1]), q[2]);
		default:
			return 0;

		}

	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return results;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return updateCount;
	}

	/*
	 * Do not modify any methods below.
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {

		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {

		return null;
	}

	@Override
	public void addBatch(String arg0) throws SQLException {

	}

	@Override
	public void cancel() throws SQLException {

	}

	@Override
	public void clearBatch() throws SQLException {

	}

	@Override
	public void clearWarnings() throws SQLException {

	}

	@Override
	public void close() throws SQLException {

	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {

		return false;
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {

		return false;
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {

		return false;
	}

	@Override
	public int[] executeBatch() throws SQLException {

		return null;
	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {

		return 0;
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {

		return 0;
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {

		return 0;
	}

	@Override
	public Connection getConnection() throws SQLException {

		return null;
	}

	@Override
	public int getFetchDirection() throws SQLException {

		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {

		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {

		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {

		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {

		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {

		return false;
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {

		return false;
	}

	@Override
	public int getQueryTimeout() throws SQLException {

		return 0;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {

		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {

		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {

		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {

		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {

		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {

		return false;
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {

	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {

	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {

	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {

	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {

	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {

	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {

	}

	@Override
	public void setQueryTimeout(int arg0) throws SQLException {

	}

	@Override
	public void closeOnCompletion() throws SQLException {

	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {

		return false;
	}

}
