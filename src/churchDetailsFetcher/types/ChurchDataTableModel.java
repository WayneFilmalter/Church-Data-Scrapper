package churchDetailsFetcher.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import churchDetailsFetcher.types.DataTypes.NameEmail;
import churchDetailsFetcher.types.DataTypes.NamePhoneNumber;

public class ChurchDataTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -6700243010514583315L;

	public static final String NAME_COLUMN = "Name";
    public static final String DENOMINATION_COLUMN = "Denomination";
    public static final String ADDRESS_COLUMN = "Address";
    public static final String PHONE_NUMBER_COLUMN = "Phone Number";
    public static final String WEBSITE_COLUMN = "Website";
    public static final String EMAIL_COLUMN = "Email";
    public static final String RATING_COLUMN = "Rating";
    public static final String RATINGS_COUNT_COLUMN = "Ratings Count";

    private List<ChurchTableData> churchDataList;
    private boolean[] columnVisible; // An array to track column visibility
    private String[] columnNames;

    // Constructor
    public ChurchDataTableModel(List<ChurchTableData> churchData) {
        this.churchDataList= churchData;
        this.columnNames = new String[]{
            NAME_COLUMN,
            DENOMINATION_COLUMN,
            ADDRESS_COLUMN,
            PHONE_NUMBER_COLUMN,
            WEBSITE_COLUMN,
            EMAIL_COLUMN,
            RATING_COLUMN,
            RATINGS_COUNT_COLUMN
        };
        this.columnVisible = new boolean[columnNames.length];
        Arrays.fill(columnVisible, true); // Set all columns to visible by default
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

	@Override
	public String getColumnName(int column) {
		int actualColumnIndex = -1;
		for (int i = 0, j = 0; i < columnVisible.length; i++) {
			if (columnVisible[i]) {
				if (j == column) {
					actualColumnIndex = i;
					break;
				}
				j++;
			}
		}
		return columnNames[actualColumnIndex];
	}

	public void addChurchData(ChurchTableData churchInfo) {
		churchDataList.add(churchInfo);
		fireTableRowsInserted(churchDataList.size() - 1, churchDataList.size() - 1);
	}

	public void setColumnVisible(String columnName, boolean isVisible) {
		int index = Arrays.asList(columnNames).indexOf(columnName);
		if (index != -1) {
			columnVisible[index] = isVisible; // Set visibility based on the index
		}
		fireTableStructureChanged(); // Notify the table that the structure has changed
	}

	public boolean isColumnVisible(int columnIndex) {
		return columnVisible[columnIndex];
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
		case 0:
			return churchData.getName();
		case 1:
			return "None"; // Placeholder for Denomination
		case 2:
			return churchData.getAddress();
		case 3:
			return churchData.getPhoneNumber();
		case 4:
			return churchData.getWebsite();
		case 5:
			return churchData.getEmail();
		case 6:
			return churchData.getRating();
		case 7:
			return churchData.getUserRatingsTotal();
		default:
			return null;
		}
	}

}
