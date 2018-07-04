package cn.copoint.coeditor.controller;

import cn.copoint.coeditor.dao.GroupMapper;
import cn.copoint.coeditor.entity.EtherpadProps;
import cn.copoint.coeditor.entity.GroupEntity;
import cn.copoint.coeditor.entity.Login;
import cn.copoint.coeditor.utils.SimpleTreeNode;
import net.gjerull.etherpad.client.EPLiteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@Controller
public class zTreeController {

    private GroupMapper groupMapper;
    private EPLiteClient client;

    @Autowired
    public zTreeController(EtherpadProps etherpadProps, GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
        client = new EPLiteClient(etherpadProps.getUrl(), etherpadProps.getApiKey());
    }

    @GetMapping("/tree")
    public String codeMirror() {
        return "tree";
    }
    @RequestMapping("TestZTree")
    public @ResponseBody
    String TestZTree(HttpServletRequest request, HttpServletResponse response) {
        String groupID = null;
        Map result = null;
        List<String> padIDs = null;
        Login loginInfo = (Login) request.getSession().getAttribute("loginInfo");
        String authorName =  loginInfo.getUsername();
        List<SimpleTreeNode> lstTree = new ArrayList<>();
        List<GroupEntity> gs=groupMapper.getByAuthorName(authorName);
        List<GroupEntity> gsm=groupMapper.getByMemberName(authorName);
        //
        GroupEntity group = null;
        int id = 1;
        int pid = id;
        lstTree.add(new SimpleTreeNode(id,0,"Myself",true));
        id++;
        lstTree.add(new SimpleTreeNode(id,0,"Other",true));
        id++;
        for(int i=0;i<gs.size();i++) {
            group = gs.get(i);
//            System.out.println(group.ToString());
//            lstTree.add(new SimpleTreeNode(id,1,group.getGroupName().split("@")[0]));
            if (group.getMemberName() == null) {
                lstTree.add(new SimpleTreeNode(id,1,group.getGroupName()));
                pid = id;
                id++;
                //list pad
                result = client.createGroupIfNotExistsFor(group.getGroupName());
                groupID = (String) result.get("groupID");

                result =  client.listPads(groupID);

                padIDs = (List<String>)result.get("padIDs");
                for (int j=0;j<padIDs.size();j++) {
//                https://www.cnblogs.com/mingforyou/archive/2013/09/03/3299569.html
                    String [] _padIDs = padIDs.get(j).split("\\$");
                    lstTree.add(new SimpleTreeNode(id,pid,_padIDs[_padIDs.length-1]+".cp"));
                    id++;
                }
            }
        }
        for(int i=0;i<gsm.size();i++) {
            group = gsm.get(i);
//            System.out.println(group.ToString());
            lstTree.add(new SimpleTreeNode(id,2,group.getGroupName()));
            pid = id;
            id++;
            //list pad
            result = client.createGroupIfNotExistsFor(group.getGroupName());
            groupID = (String) result.get("groupID");

            result =  client.listPads(groupID);

            padIDs = (List<String>)result.get("padIDs");
            for (int j=0;j<padIDs.size();j++) {
//                https://www.cnblogs.com/mingforyou/archive/2013/09/03/3299569.html
                String [] _padIDs = padIDs.get(j).split("\\$");
                lstTree.add(new SimpleTreeNode(id,pid,_padIDs[_padIDs.length-1]+".cp"));
                id++;
            }
        }
        return makeTree(lstTree);
    }
    public static String makeTree(List<SimpleTreeNode> roles){
        //Check Roles is null
        StringBuffer sb = new StringBuffer();
        SimpleTreeNode r = null;
        sb.append("[");
        for(int i=0;i<roles.size();i++){
            r= roles.get(i);
            sb.append("{id:").append(r.getId()).append(",pId:").append(r.getPid()).append(",name:\"")
                    .append(r.getName()).append("\"").append(",open:").append(r.getIsopen()).append("},");
        }
        return sb.substring(0,sb.length()-1)+"]";
    }
}
