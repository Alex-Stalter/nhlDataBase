import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;


/**
 * 
 * The GUI table requires a model to define what data appears at each row/column
 */
public class DatabaseTableModel extends AbstractTableModel {
	String[] columnNames = { "First Name", "Last Name","Rank", "Goals", "Assists","Total Points","Position","Age","Team","Games Played" };
	public ArrayList<Row> table = new ArrayList<Row>();

	public void setTable(ArrayList<Row> newTable) {
		table = newTable;
	}
	public String getColumnName(int col) {
		return columnNames[col].toString();
	}
	public int getRowCount() { return table.size(); }
	public int getColumnCount() { return columnNames.length; }

	
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return table.get(row).zero;
		} else if(col==1){
			return table.get(row).one;
		} else if(col==2){
			return table.get(row).two;
		} else if(col==3){
			return table.get(row).three;
		} else if(col==4){
			return table.get(row).four;
		} else if(col==5){
			return table.get(row).five;
		} else if(col==6){
			return table.get(row).six;
		} else if(col==7){
			return table.get(row).seven;
		} else if(col==8){
			return table.get(row).eight;
		}else{
			return table.get(row).nine;
		}
	}
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	public void setValueAt(Object value, int row, int col) {
	}

}
