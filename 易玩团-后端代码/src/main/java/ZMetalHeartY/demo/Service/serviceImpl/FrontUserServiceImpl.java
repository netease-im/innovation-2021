package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.FrontUser;
import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Mapper.FrontUserMapper;
import ZMetalHeartY.demo.Service.FrontUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontUserServiceImpl implements FrontUserService {

    FrontUserMapper frontUserMapper;

    public FrontUserServiceImpl(FrontUserMapper frontUserMapper) {
        this.frontUserMapper = frontUserMapper;
    }

    @Override
    public int deleteByFuId(Integer fuId) {
        return frontUserMapper.deleteByPrimaryKey(fuId);
    }

    @Override
    public int insert(FrontUser frontUser) {
        return frontUserMapper.insertSelective(frontUser);
    }

    @Override
    public FrontUser selectByFuId(Integer fuId) {
        return frontUserMapper.selectByPrimaryKey(fuId);
    }

    @Override
    public PageInfo<FrontUser> getFrontUserByPage(FrontUser frontUser, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<FrontUser> frontUsers = frontUserMapper.selectAllByCondition(frontUser);
        PageInfo<FrontUser> frontUserPageInfo = new PageInfo<>(frontUsers);
        return frontUserPageInfo;
    }

    @Override
    public int updateByFuIdSelective(FrontUser record) {
        return frontUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<FrontUser> getAllFrontUser(FrontUser frontUser) {
        return frontUserMapper.selectAllByCondition(frontUser);
    }
}
