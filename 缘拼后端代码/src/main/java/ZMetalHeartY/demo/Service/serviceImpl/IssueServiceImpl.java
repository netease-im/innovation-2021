package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.*;
import ZMetalHeartY.demo.Mapper.*;
import ZMetalHeartY.demo.Service.IssueService;
import ZMetalHeartY.demo.Utils.Graphic;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@Slf4j
public class IssueServiceImpl implements IssueService {

    IssueMapper issueMapper;
    @Autowired
    CurrencyMapper currencyMapper;
    @Autowired
    IssueCommentMapper issueCommentMapper;
    @Autowired
    FrontUserMapper frontUserMapper;
    @Autowired
    IssueAttendMapper issueAttendMapper;
    @Autowired
    FrontUserFollowMapper frontUserFollowMapper;
    @Autowired
    Graphic graphic;
    @Autowired
    FrontUserIssueLikeMapper frontUserIssueLikeMapper;
    @Autowired
    FrontUserIssueCollectionsMapper frontUserIssueCollectionsMapper;

    public IssueServiceImpl(IssueMapper issueMapper) {
        this.issueMapper = issueMapper;
    }

    @Override
    public int deleteByIId(Integer iId) {
        return issueMapper.deleteByPrimaryKey(iId);
    }

    @Override
    public int insert(Issue issue) {
        return issueMapper.insertSelective(issue);
    }

    @Override
    public Issue selectByIId(Integer iId) {
        return issueMapper.selectByPrimaryKey(iId);
    }

    @Override
    public PageInfo<Issue> getIssueByPage(Issue issue, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<Issue> issues = issueMapper.selectAllByCondition(issue);


        PageInfo<Issue> issuePageInfo = new PageInfo<>(issues);
        return issuePageInfo;
    }

    @Override
    public int updateByIIdSelective(Issue record) {
        return issueMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Issue> getAllIssue(Issue issue) {
        return issueMapper.selectAllByCondition(issue);
    }

    @Override
    public int adopt(Integer iId) {
        return issueMapper.adopt(iId);
    }

    @Override
    public int refuse(Integer iId) {
        return issueMapper.refuse(iId);
    }


    @Override
    public List<Issue> getGetListContent(List<Issue> issues, Integer fromFuId) {

        for (Issue is:issues) {
            this.getGetIssueContent(is,fromFuId);
        }
        return issues;
    }

    @Override
    public Issue getGetIssueContent(Issue issue, Integer fromFuId) {

            //评论
            IssueComment issueComment = new IssueComment();
            issueComment.setiId(issue.getiId());
            issue.setIssueComments(issueCommentMapper.selectAllByCondition(issueComment));

            //参加人
            IssueAttend issueAttend = new IssueAttend();
            issueAttend.setiId(issue.getiId());
            issue.setIssueAttend(issueAttendMapper.selectAllByCondition(issueAttend));


            //地图图标
            FrontUser frontUser = frontUserMapper.selectByPrimaryKey(issue.getFuId());
            issue.setIconPath( graphic.transferImgForRoundImgage(frontUser.getFuImg()+"" , frontUser.getFuSex()==1?"男":"女",frontUser.getFuNickname()) );

            FrontUserFollow frontUserFollow = new FrontUserFollow();
            frontUserFollow.setFuId(fromFuId);
            frontUserFollow.setFufFuId(issue.getFuId());
            if(frontUserFollowMapper.selectAllByCondition(frontUserFollow).size() > 0){
                issue.setFollow(true);
            }else{
                issue.setFollow(false);
            }

            issue.setWidth(45);
            issue.setHeight(52);

            JSONObject jsonAddress = JSONObject.parseObject(issue.getiAddress());
            JSONObject locationJson = JSON.parseObject( String.valueOf(jsonAddress.get("location")) );

            issue.setLongitude(Double.parseDouble(String.valueOf(locationJson.get("longitude"))));
            issue.setLatitude(Double.parseDouble(String.valueOf(locationJson.get("latitude"))));

            FrontUserIssueLike frontUserIssueLike = new FrontUserIssueLike();
            frontUserIssueLike.setFuId(issue.getFuId());
            frontUserIssueLike.setiId(issue.getiId());
            if (frontUserIssueLikeMapper.selectAllByCondition(frontUserIssueLike).size()>0){
                issue.setLike(true);
            }else {
                issue.setLike(false);
            }

            FrontUserIssueCollections frontUserIssueCollections = new FrontUserIssueCollections();
            frontUserIssueCollections.setFuId(issue.getFuId());
            frontUserIssueCollections.setiId(issue.getiId());

            if (frontUserIssueCollectionsMapper.selectAllByCondition(frontUserIssueCollections).size() > 0 ){
                issue.setCollection(true);
            }else {
                issue.setCollection(false);
            }


        return issue;
    }




}
