package churchDetailsFetcher.types;

import javax.swing.table.DefaultTableModel;

import churchDetailsFetcher.types.DataTypes.NameEmail;
import churchDetailsFetcher.types.DataTypes.NamePhoneNumber;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ChurchDataTableModel extends AbstractTableModel {

	private final List<ChurchTableData> churchDataList;
	private final String[] columnNames;
	private final boolean[] columnVisible;

	public ChurchDataTableModel(List<ChurchTableData> churchDataList, String[] columnNames, boolean[] columnVisible) {
		this.churchDataList = churchDataList;
		this.columnNames = columnNames;
		this.columnVisible = columnVisible;
	}

	@Override
	public int getRowCount() {
		return churchDataList.size();
	}

	@Override
	public int getColumnCount() {
		int count = 0;
		for (boolean visible : columnVisible) {
			if (visible)
				count++;
		}
		return count;

	}

	public void clearData() {
		churchDataList.clear();
		fireTableDataChanged();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ChurchTableData churchData = churchDataList.get(rowIndex);
		int visibleIndex = -1;
		for (int i = 0; i < columnVisible.length; i++) {
			if (columnVisible[i]) {
				visibleIndex++;
				if (visibleIndex == columnIndex) {
					return getColumnValue(churchData, i);
				}
			}
		}
		return null;
	}

	public List<NamePhoneNumber> getNumbers() {
		List<NamePhoneNumber> contacts = new ArrayList<>();
		for (ChurchTableData churchData : churchDataList) {
			String name = churchData.getName();
			String email = churchData.getEmail();
			if (name != null && email != null) { // Ensure both name and email are not null
				contacts.add(new NamePhoneNumber(name, email));
			}
		}
		return contacts;
	}

	public List<NameEmail> getEmails() {
		List<NameEmail> contacts = new ArrayList<>();
		for (ChurchTableData churchData : churchDataList) {
			String name = churchData.getName();
			String email = churchData.getEmail();
			if (name != null && email != null) { // Ensure both name and email are not null
				contacts.add(new NameEmail(name, email));
			}
		}
		return contacts;
	}

    private Object getColumnValue(ChurchTableData churchData, int columnIndex) {
        switch (columnIndex) {
            case 0: return churchData.getName();
            case 1: return "None"; // Placeholder for Denomination
            case 2: return churchData.getAddress();
            case 3: return churchData.getPhoneNumber();
            case 4: return churchData.getWebsite();
            case 5: return churchData.getEmail();
            case 6: return churchData.getRating();
            case 7: return churchData.getUserRatingsTotal();
            default: return null;
        }
    }

	@Override
	public String getColumnName(int columnIndex) {
		int visibleIndex = -1;
		for (int i = 0; i < columnVisible.length; i++) {
			if (columnVisible[i]) {
				visibleIndex++;
				if (visibleIndex == columnIndex) {
					return columnNames[i];
				}
			}
		}
		return null;
	}

	public boolean isColumnVisible(int columnIndex) {
		return columnVisible[columnIndex];
	}
}
