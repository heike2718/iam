// =====================================================
// Project: auth-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.auth_admin_api.domain.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthAdminCollectionUtils
 */
public class AuthAdminCollectionUtils {

	/**
	 * Gruppiert die gegebenen Strings zu Gruppen der größe maxBunchSize
	 *
	 * @param  strList
	 *                      List
	 * @param  maxBunchSize
	 *                      int
	 * @return
	 */
	public static List<List<String>> groupTheStrings(final List<String> strList, final int maxBunchSize) {

		List<List<String>> groupedLists = new ArrayList<>();

		for (int i = 0; i < strList.size(); i += maxBunchSize) {

			groupedLists.add(new ArrayList<>(strList.subList(i, Math.min(i + maxBunchSize, strList.size()))));
		}
		return groupedLists;

	}

	/**
	 * Zählt alle items.
	 *
	 * @param  groups
	 * @return        long
	 */
	public static long countElements(final List<List<String>> groups) {

		return groups.stream()
			.flatMap(List::stream) // Flatten the list of lists into a single stream of String
			.count(); // Count the elements in the stream
	}

	/**
	 * Fügt die Gruppen wieder zusammen.
	 *
	 * @param  groups
	 *                List of List
	 * @return        List
	 */
	public static List<String> joinTheGroups(final List<List<String>> groups) {

		List<String> result = new ArrayList<>();

		for (List<String> group : groups) {

			result.addAll(group);
		}
		return result;
	}

}
