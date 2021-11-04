package ZMetalHeartY.demo.Service;


import ZMetalHeartY.demo.Entity.News;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZMetalHeartY
 */
@Service
public interface NewsService {

    int deleteByNId(Integer nId);

    int insert(News news);

    News selectByNId(Integer nId);

    PageInfo<News> getNewsByPage(News news, Integer page, Integer limit);

    int updateByNIdSelective(News record);

    List<News> getAllNews(News news);

}
