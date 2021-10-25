package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.FlatformAdminGroup;
import ZMetalHeartY.demo.Entity.FlatformAdminMenu;
import ZMetalHeartY.demo.Mapper.FlatformAdminGroupMapper;
import ZMetalHeartY.demo.Mapper.FlatformAdminMenuMapper;
import ZMetalHeartY.demo.Service.FlatformAdminMenuService;
import ZMetalHeartY.demo.Utils.PageBean;
import com.github.pagehelper.PageHelper;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FlatformAdminMenuServiceImpl implements FlatformAdminMenuService {

    @Autowired
    public FlatformAdminMenuMapper flatformAdminMenuMapper;
    @Autowired
    public FlatformAdminGroupMapper flatformAdminGroupMapper;

    @Override
    public List<FlatformAdminMenu> findByUid(Integer uid) {

        List<FlatformAdminMenu> flatformAdminMenus = new ArrayList<>();
        FlatformAdminGroup flatformAdminGroup = flatformAdminGroupMapper.findById(uid);
        List<String> result = Arrays.asList(flatformAdminGroup.getAg_auth().split(","));
        if(result!=null&&result.size()>0) {
            for (int i = 0; i < result.size(); i++) {
                flatformAdminMenus.add(flatformAdminMenuMapper.findById(Integer.parseInt(result.get(i))));
            }
        }
        return flatformAdminMenus;

    }

    @Override
    public List<FlatformAdminMenu> findAll() {
        JSONArray json = new JSONArray(flatformAdminMenuMapper.findAll());
        return flatformAdminMenuMapper.findAll();
    }

    @Override
    public List<FlatformAdminMenu> findAllPage(FlatformAdminMenu flatformAdminMenu) {
        return flatformAdminMenuMapper.findAllPage(flatformAdminMenu);
    }

    @Override
    public int count(FlatformAdminMenu flatformAdminMenu) {
        return flatformAdminMenuMapper.count(flatformAdminMenu);
    }

    @Override
    public FlatformAdminMenu findById(Integer id) {
        return flatformAdminMenuMapper.findById(id);
    }

    @Override
    public int insert(FlatformAdminMenu flatformAdminMenu) {
        return flatformAdminMenuMapper.insert(flatformAdminMenu);
    }

    @Override
    public int update(FlatformAdminMenu flatformAdminMenu) {
        return flatformAdminMenuMapper.update(flatformAdminMenu);
    }

    @Override
    public int delete(FlatformAdminMenu flatformAdminMenu) {
        return flatformAdminMenuMapper.delete(flatformAdminMenu);
    }

    @Override
    public List<FlatformAdminMenu> findByPage(FlatformAdminMenu flatformAdminMenu, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage,pageSize);//PageHelper使用
        List<FlatformAdminMenu> flatformAdminMenus = flatformAdminMenuMapper.findAllPage(flatformAdminMenu);       //全部记录列表
        int countNums = flatformAdminMenuMapper.count(flatformAdminMenu);         //总记录数
        PageBean<FlatformAdminMenu> pageData = new PageBean<>(currentPage, pageSize, countNums);
        pageData.setItems(flatformAdminMenus);
        return pageData.getItems();
    }


    @Override
    public FlatformAdminMenu findByPid(Integer pid) {
        return flatformAdminMenuMapper.findById(pid);
    }


    @Override
    public List<FlatformAdminMenu> findByMenu(Integer value) {
        System.out.println(value+"-------------------------------");
        return flatformAdminMenuMapper.findByAuLevel(value);
    }


    @Override
    public List<FlatformAdminMenu> findByType(String type) {
        return flatformAdminMenuMapper.findByType(type);
    }


}
