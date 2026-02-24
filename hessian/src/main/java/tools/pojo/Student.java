package tools.pojo;

import java.util.Map;
import java.util.Properties;

class Student {
    public String _name;
    private String id;
    public Properties properties;
    private Map<String,Object> map;

    public Student() {
    }

    public void hello(){
        System.out.println("hello");
    }

    private void kiss(){
        System.out.println("kiss");
    }


    public void setName1(String name) {
        this._name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
