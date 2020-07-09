package ack.util;

import java.util.Collection;
import java.util.stream.Collectors;

public class Utils {

	public static final String ECARTICO = "src/main/resources/local-databases/ecartico-fixed.ttl";
	
	private Utils() {};

	public static <T> String printify(Collection<T> set, String seperator) {
		return set.stream().map(T::toString)
				.collect(Collectors.joining(seperator));
	}
	
}
