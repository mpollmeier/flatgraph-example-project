package testdomains.simple;

import java.util.HashSet;
import java.util.Set;

public class PropertyNames {


public static final String description = "description";


public static final String name = "name";


public static final String order = "order";


public static final String string_list = "string_list";

public static Set<String> ALL = new HashSet<String>() {{
add(description);
add(name);
add(order);
add(string_list);
}};

}