package testdomains.simple;

import java.util.HashSet;
import java.util.Set;

public class PropertyNames {


public static final String DESCRIPTION = "description";


public static final String NAME = "name";


public static final String ORDER = "order";


public static final String STRING_LIST = "string_list";

public static Set<String> ALL = new HashSet<String>() {{
add(DESCRIPTION);
add(NAME);
add(ORDER);
add(STRING_LIST);
}};

}