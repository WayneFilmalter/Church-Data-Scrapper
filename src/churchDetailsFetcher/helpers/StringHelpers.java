package churchDetailsFetcher.helpers;

public class StringHelpers {

	public static String capitalizeString(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}
	
	public static String replaceSpacesWithPlus(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return input.trim().replace(" ", "+");
    }

}
 