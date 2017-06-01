package piou.plectre.com.piou.handler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by El Di@blo on 23/05/2017.
 */

public class ClassArray {

    public ClassArray(HashMap map) {


        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            HashMap<String, String> result = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            result.put("Second Line", pair.getValue().toString());
            result.put("First Line", pair.getKey().toString());


        }
    }

}


