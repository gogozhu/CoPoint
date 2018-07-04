package cn.copoint.coeditor.entity;

import cn.copoint.coeditor.utils.ReflectToStringUtil;

import java.io.Serializable;

public class GroupEntity implements Serializable{
    private Long id;


    private String authorName;

    private String groupName;

    private String memberName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getGroupName()
    {
        return this.groupName;
    }
    public void setGroupName(String groupName)
    {
        this.groupName=groupName;
    }
    public String getMemberName()
    {
        return this.memberName;
    }
    public void setMemberName(String memberName)
    {
        this.memberName=memberName;
    }
    public String ToString()
    {
        return ReflectToStringUtil.toStringUtil(this,true);
    }
}

