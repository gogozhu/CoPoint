package cn.copoint.coeditor.utils;

import java.util.ArrayList;
import java.util.List;
/**
 * <p></p>
 *
 * @package: com.tree
 * @class: SimpleTreeNode1
 * @date: 2018/3/27
 * @author: jcroad(caoyajing @ yunmel.com) https://blog.csdn.net/jcroad/article/details/79735790
 * @since 1.0
 */
public class SimpleTreeNode {

    private Integer id;
    private Integer pid;
    private String name;
    private boolean isopen;
    private List<SimpleTreeNode> children;
    public SimpleTreeNode(Integer id, Integer pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.isopen = false;
    }
    public SimpleTreeNode(Integer id, Integer pid, String name,boolean isopen) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.isopen = isopen;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return this.pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean getIsopen() {
        return this.isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

    public List<SimpleTreeNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<SimpleTreeNode> children) {
        this.children = children;
    }


}

