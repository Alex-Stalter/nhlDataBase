import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLiteGUIDemo implements KeyListener, TableModelListener, ActionListener {
	// Database connectivity
	Connection connection;
	String sql;
	String DB_PATH = SQLiteGUIDemo.class.getResource("nhl.sqlite").getFile();

	// GUI objects
	private JFrame frame;
	private JTable table;
	private JTextField textField;
	private JScrollPane scrollPane;
	private JComboBox<String> databases;
	private String[] tables = {"Player", "Team", "Coaches"};
	private int columns = 10;

	// Connect database with table
	DatabaseTableModel tableModel = new DatabaseTableModel();

	/**
	 * Create the application.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public SQLiteGUIDemo() throws ClassNotFoundException, SQLException {
		// load the sqlite-JDBC driver using the current class loader
		Class.forName( "org.sqlite.JDBC");

		// protocol (jdbc): subprotocol (sqlite):databaseName (Chinook_Sqlite_AutoIncrementPKs.sqlite)
		connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);

		// Initialize GUI
		initializeGUI();

		// Initialize event handlers
		initializeListeners();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	private void initializeGUI() throws ClassNotFoundException, SQLException {

		// Setup the main window
		frame = new JFrame();
		frame.setBounds(400, 400, 1300, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Place the search box at the top
		textField = new JTextField();
		textField.setToolTipText("Search");
		frame.getContentPane().add(textField, BorderLayout.NORTH);
		textField.setColumns(10);

		// Place the table in a scroll pane in the center
		table = new JTable(tableModel);
		scrollPane = new JScrollPane(table);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		databases = new JComboBox<>(tables);
		frame.getContentPane().add(databases, BorderLayout.SOUTH);
	}

	// Listeners (event handlers) define what to do in response to events.
	// GUIs use on event handlers to bind user actions with computation. 
	private void initializeListeners() {

		// When the user types something, tell the table to update itself
		textField.addKeyListener(this);

		databases.addActionListener(this);
		// Define what to do when the table needs to be updated
		// That is, run queries on user input
		// Registering event handler to DatabaseTableModel
		// addTableModelListener need TableModelListener as an argument - used as an Innerclass
		tableModel.addTableModelListener(this);
		
		// Load the table on startup
		tableModel.fireTableDataChanged();
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// fireTableDataChanged() is a method in AbstractTableModel
		// This method calls tableChanged method
		// This is defined in swing
		// We implement database query to update the table in the tableChanged() method
		tableModel.fireTableDataChanged();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		tableModel.fireTableDataChanged();
	}

	
	// Whenever the key is released, call this method
	@Override
	public void tableChanged(TableModelEvent e) {
		String param = textField.getText();
		// generate parameterized sql

		// System.out.println("\nSQL: " + sql + "\n");

		if(databases.getSelectedIndex() == 0){
			//Changes the names of the columns so that the data mataches the titles.
			JTableHeader firstHeader = table.getTableHeader();
			TableColumnModel firstColumn = firstHeader.getColumnModel();
			TableColumn tc = firstColumn.getColumn(0);
			tc.setHeaderValue("First Name");
			tc = firstColumn.getColumn(1);
			tc.setHeaderValue("Last Name");
			tc = firstColumn.getColumn(2);
			tc.setHeaderValue("Rank");
			tc = firstColumn.getColumn(3);
			tc.setHeaderValue("Goals");
			tc = firstColumn.getColumn(4);
			tc.setHeaderValue("Assists");
			tc = firstColumn.getColumn(5);
			tc.setHeaderValue("Total Points");
			tc = firstColumn.getColumn(6);
			tc.setHeaderValue("Position");
			tc = firstColumn.getColumn(7);
			tc.setHeaderValue("Age");
			tc = firstColumn.getColumn(8);
			tc.setHeaderValue("Team");
			tc = firstColumn.getColumn(9);
			tc.setHeaderValue("Games Played");
			firstHeader.repaint();
			//Allows the data to be searched by player last name and initially adds the dat onto the screen
			if ( param.equalsIgnoreCase("") ) {
				sql = "SELECT * from Player join Team using(team_code) ORDER BY Player.lname";
			} else {
				sql = "SELECT * from Player join team using(team_code) WHERE Player.lname like ? ORDER BY Player.lname ";
			}

			try {

				PreparedStatement stmt = connection.prepareStatement( sql );

				if ( !param.equalsIgnoreCase("") ) {
					stmt.setString( 1, "%" + param + "%" );
				}

				// get results
				ResultSet res = stmt.executeQuery();

				// Transfer data from database to GUI
				ArrayList<Row> table = new ArrayList<Row>();
				while ( res.next() ) {
					table.add(new Row(res.getString("fname"),res.getString("lname"),res.getString("Ranking"),res.getString("Goals"),res.getString("Assists"),res.getString("Tpoints"),res.getString("Position"),res.getString("Age"),res.getString("Name"),res.getString("GamesP")));
				}
				tableModel.setTable(table);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else if(databases.getSelectedIndex() == 1){
			//Changes the column headers so that the titles match the data and puts empty strings where there is not data.
			JTableHeader firstHeader = table.getTableHeader();
			TableColumnModel firstColumn = firstHeader.getColumnModel();
			TableColumn tc = firstColumn.getColumn(0);
			tc.setHeaderValue("Team Name");
			tc = firstColumn.getColumn(1);
			tc.setHeaderValue("Team Code");
			tc = firstColumn.getColumn(2);
			tc.setHeaderValue("Players");
			tc = firstColumn.getColumn(3);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(4);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(5);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(6);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(7);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(8);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(9);
			tc.setHeaderValue("");
			firstHeader.repaint();

			//Displays the team table joined with the player table so that the amount of players on each team can be counted.
			//The second sql allows for the teams to be searched by team name.
			if ( param.equalsIgnoreCase("") ) {
				sql = "SELECT Team.Name, Team.team_code, count(Player.lname) as players " +
						"from Team join Player using(team_code) " +
						"group by team_code ORDER BY Team.name";
			} else {
				sql = "SELECT Team.Name, Team.team_code, count(Player.lname) as players " +
						"from Team join Player using(team_code) " +
						"group by team_code like ? ORDER BY Team.name";
			}

			try {

				PreparedStatement stmt = connection.prepareStatement( sql );

				if ( !param.equalsIgnoreCase("") ) {
					stmt.setString( 1, "%" + param + "%" );
				}

				// get results
				ResultSet res = stmt.executeQuery();

				// Transfer data from database to GUI
				ArrayList<Row> table = new ArrayList<Row>();
				//Displays the data from the sql statement and puts it into the gui table.
				while ( res.next() ) {
					table.add(new Row(res.getString("Name"),res.getString("team_code"),res.getString("players"),"","","","","","",""));
				}
				tableModel.setTable(table);


			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}else if(databases.getSelectedIndex() == 2){
			//Sets the table headers so that the data matches the titles.
			JTableHeader firstHeader = table.getTableHeader();
			TableColumnModel firstColumn = firstHeader.getColumnModel();
			TableColumn tc = firstColumn.getColumn(0);
			tc.setHeaderValue("First Name");
			tc = firstColumn.getColumn(1);
			tc.setHeaderValue("Last Name");
			tc = firstColumn.getColumn(2);
			tc.setHeaderValue("Team");
			tc = firstColumn.getColumn(3);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(4);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(5);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(6);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(7);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(8);
			tc.setHeaderValue("");
			tc = firstColumn.getColumn(9);
			tc.setHeaderValue("");

			firstHeader.repaint();
			//Joins team and coach so that the team name can be displayed.
			//Second statement allows for the coaches to eb searched by last name.
			if ( param.equalsIgnoreCase("") ) {
				sql = "SELECT * from Coaches join Team using(team_code)";
			} else {
				sql = "SELECT * from Coaches join Team using(team_code) " +
						"WHERE Coaches.lname like ? order by Coaches.lname";
			}

			try {

				PreparedStatement stmt = connection.prepareStatement( sql );

				if ( !param.equalsIgnoreCase("") ) {
					stmt.setString( 1, "%" + param + "%" );
				}

				// get results
				ResultSet res = stmt.executeQuery();

				// Transfer data from database to GUI
				ArrayList<Row> table = new ArrayList<Row>();
				while ( res.next() ) {
					table.add(new Row(res.getString("fname"),res.getString("lname"), res.getString("name"),"","","","","","",""));
				}
				tableModel.setTable(table);

			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}



	}
// modTable does not work but was going to be bale to delete dolumns os that the table on teh gui would be cleaner.
	public void modTable(int num) {
		if (columns > num) {
			for (int i = columns; i > num; i--) {
				table.removeColumn(table.getColumn(table.getColumnName(i - 1)));
				columns--;
			}
		} else if (columns < num) {
			for (int i = columns; i < num; i++) {
				table.addColumn(new TableColumn());
				columns++;
			}


		}
	}

	/**
	 * Launch the application.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		SQLiteGUIDemo window = new SQLiteGUIDemo();
		window.frame.setVisible(true);
	}


}
