package ZMetalHeartY.demo.Entity;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    private List<MenuTree> list;

    private ArrayList checkedId;

    public List<MenuTree> getList() {
        return list;
    }

    public void setList(List<MenuTree> list) {
        this.list = list;
    }

    public ArrayList getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(ArrayList checkedId) {
        this.checkedId = checkedId;
    }
}
