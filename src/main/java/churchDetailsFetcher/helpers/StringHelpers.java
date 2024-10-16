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

	public static String capCaseString(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}

		String[] words = str.split(" ");
		StringBuilder sb = new StringBuilder();

		for (String word : words) {
			if (!word.isEmpty()) {
				sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
			}
		}

		return sb.toString().trim();
	}

}
