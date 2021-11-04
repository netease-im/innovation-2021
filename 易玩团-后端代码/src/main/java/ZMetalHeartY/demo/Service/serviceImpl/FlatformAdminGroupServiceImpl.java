package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.FlatformAdminGroup;
import ZMetalHeartY.demo.Mapper.FlatformAdminGroupMapper;
import ZMetalHeartY.demo.Service.FlatformAdminGroupService;
import ZMetalHeartY.demo.Utils.PageBean;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlatformAdminGroupServiceImpl implements FlatformAdminGroupService {

    @Autowired
    private FlatformAdminGroupMapper flatformAdminGroupMapper;

    @Override
    public FlatformAdminGroup findByUid(Integer uid) {
        return flatformAdminGroupMapper.findByUid(uid);
    }

    @Override
    public int insert(FlatformAdminGroup flatformAdminGroup) {
        return flatformAdminGroupMapper.insert(flatformAdminGroup);
    }

    @Override
    public int update(FlatformAdminGroup flatformAdminGroup) {
        return flatformAdminGroupMapper.update(flatformAdminGroup);
    }

    @Override
    public int delete(FlatformAdminGroup flatformAdminGroup) {
        return flatformAdminGroupMapper.delete(flatformAdminGroup);
    }

    @Override
    public FlatformAdminGroup findById(Integer id) {
        return flatformAdminGroupMapper.findById(id);
    }

    @Override
    public List<FlatformAdminGroup> findByPage(FlatformAdminGroup flatformAdminGroup, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage,pageSize);//PageHelper使用
        PageHelper.orderBy("ag_id DESC");//分页排序
        List<FlatformAdminGroup> flatformAccountgrouplist = flatformAdminGroupMapper.findAll(flatformAdminGroup);       //全部记录列表
        int countNums = flatformAdminGroupMapper.count(flatformAdminGroup);         //总记录数
        PageBean<FlatformAdminGroup> pageData = new PageBean<>(currentPage, pageSize, countNums);
        pageData.setItems(flatformAccountgrouplist);
        return pageData.getItems();
    }

    @Override
    public List<FlatformAdminGroup> findAll(FlatformAdminGroup flatformAdminGroup) {
        return flatformAdminGroupMapper.findAll(flatformAdminGroup);
    }

    @Override
    public int count(FlatformAdminGroup flatformAdminGroup) {
        return flatformAdminGroupMapper.count(flatformAdminGroup);
    }
}
