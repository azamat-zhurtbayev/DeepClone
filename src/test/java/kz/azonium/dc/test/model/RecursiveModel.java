package kz.azonium.dc.test.model;

import java.util.LinkedList;
import java.util.List;

public class RecursiveModel {

    private List<RecursiveModel> children = new LinkedList<>();

    private String id;

    public RecursiveModel(String id) {
        this.id = id;
    }

    public List<RecursiveModel> getChildren() {
        return children;
    }

    public void setChildren(List<RecursiveModel> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
