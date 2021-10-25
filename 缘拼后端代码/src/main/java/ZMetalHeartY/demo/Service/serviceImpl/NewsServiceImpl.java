package ZMetalHeartY.demo.Service.serviceImpl;

import ZMetalHeartY.demo.Entity.Issue;
import ZMetalHeartY.demo.Entity.News;
import ZMetalHeartY.demo.Mapper.NewsMapper;
import ZMetalHeartY.demo.Service.NewsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    NewsMapper newsMapper;

    @Override
    public int deleteByNId(Integer nId) {
        return newsMapper.deleteByPrimaryKey(nId);
    }

    @Override
    public int insert(News news) {
        return newsMapper.insertSelective(news);
    }

    @Override
    public News selectByNId(Integer nId) {
        return newsMapper.selectByPrimaryKey(nId);
    }

    @Override
    public PageInfo<News> getNewsByPage(News news, Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<News> newsList = newsMapper.selectAllByCondition(news);
        PageInfo<News> newsPageInfo = new PageInfo<>(newsList);
        return newsPageInfo;
    }

    @Override
    public int updateByNIdSelective(News record) {
        return newsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<News> getAllNews(News news) {
        return newsMapper.selectAllByCondition(news);
    }


}
