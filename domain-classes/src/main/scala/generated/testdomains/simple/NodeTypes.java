package testdomains.simple;

import java.util.HashSet;
import java.util.Set;

public class NodeTypes {


public static final String THING = "thing";

public static Set<String> ALL = new HashSet<String>() {{
add(THING);
}};

}