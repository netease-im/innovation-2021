package ZMetalHeartY.demo.Utils;

import ZMetalHeartY.demo.Entity.FlatformAdminGroup;
import ZMetalHeartY.demo.Entity.FlatformAdminMenu;

import java.util.ArrayList;
import java.util.List;

public class MakeMenuTree {
    private List<FlatformAdminMenu> menuCommon;
    private List<FlatformAdminMenu> list =new ArrayList<FlatformAdminMenu>();

    public List<FlatformAdminMenu> menuList(List<FlatformAdminMenu> menu){
        this.menuCommon=menu;
        for(FlatformAdminMenu bean:menu){
            if(bean.getAu_p_id()==0){
                FlatformAdminMenu adminmenu=new FlatformAdminMenu();
                adminmenu.setAu_clickable(bean.getAu_clickable());
                adminmenu.setAu_id(bean.getAu_id());
                adminmenu.setAu_level(bean.getAu_level());
                adminmenu.setAu_url(bean.getAu_url());
                adminmenu.setAu_p_id(bean.getAu_p_id());
                adminmenu.setAu_sort(bean.getAu_sort());
                adminmenu.setAu_title(bean.getAu_title());
                adminmenu.setAu_class(bean.getAu_class());
                adminmenu.setAu_info(bean.getAu_info());
                adminmenu.setSub_list(menuChild(bean.getAu_id()));
                list.add(adminmenu);
            }
        }
        return list;
    }

    private List<FlatformAdminMenu> menuChild(Integer auId) {
        // TODO Auto-generated method stub
        List<FlatformAdminMenu> sub_list =new ArrayList<FlatformAdminMenu>();
        for(FlatformAdminMenu bean:menuCommon){
            if(bean.getAu_p_id().equals(auId)){
                FlatformAdminMenu adminmenu=new FlatformAdminMenu();
                adminmenu.setAu_clickable(bean.getAu_clickable());
                adminmenu.setAu_id(bean.getAu_id());
                adminmenu.setAu_level(bean.getAu_level());
                adminmenu.setAu_url(bean.getAu_url());
                adminmenu.setAu_p_id(bean.getAu_p_id());
                adminmenu.setAu_sort(bean.getAu_sort());
                adminmenu.setAu_title(bean.getAu_title());
                adminmenu.setAu_class(bean.getAu_class());
                adminmenu.setAu_info(bean.getAu_info());
                adminmenu.setSub_list(menuChild(bean.getAu_id()));
                sub_list.add(adminmenu);
            }
        }
        return sub_list;
    }

    /**
     * 生成权限列表
     * @param
     * @return
     */
    public List<FlatformAdminMenu>  makeAuthList(List<FlatformAdminMenu> menu_list, FlatformAdminGroup group_info) {
        // TODO Auto-generated method stub
        if(group_info==null){
            group_info=new FlatformAdminGroup();
            group_info.setAg_auth("0");
        }
        String auth = group_info.getAg_auth();
        String[] authlist = auth.split(",");
        for(int i=0;i<authlist.length;i++){
            makeChecked(menu_list,authlist[i]);
        }
        return menu_list;
    }

    /**
     * 修改每个权限的选中状态
     * @param list
     * @param list
     */
    public void makeChecked(List<FlatformAdminMenu> list, String au_id){
        //顶层的 p_id=0
        name:for(FlatformAdminMenu item:list){
            //子功能权限
            if(item.getAu_id()==Integer.parseInt(au_id)){
                item.setChecked(2);
            }
            for(FlatformAdminMenu bean:item.getSub_list()){
               // System.out.println(bean.getAuTitle());
                if(bean.getAu_id()==Integer.parseInt(au_id)&&bean.getAu_clickable()==1){
               //     System.out.println(bean.getAuTitle()+"11111111111111    ");
                    bean.setChecked(2);
                    break name;
                }
            }
        }
        return;
    }
}

