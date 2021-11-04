package ZMetalHeartY.demo.Controller.WeChat;

import ZMetalHeartY.demo.Entity.News;
import ZMetalHeartY.demo.Service.NewsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/WeChat/news")
@ResponseBody
public class WeChatNewsController {

    @Autowired
    NewsService newsService;
    

    @RequestMapping("/listjson")
    @ResponseBody
    public JSONObject listjson(News news, Integer page, Integer limit){

        PageInfo<News> newsPageInfo = newsService.getNewsByPage(news,page,limit);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",newsPageInfo.getList());
        json.put("count",newsPageInfo.getTotal());
        return json;
    }

    @RequestMapping("/getAll")
    @ResponseBody
    public JSONObject getAll(News news){
        List<News> newsList = newsService.getAllNews(news);
        JSONObject json = new JSONObject();
        json.put("msg","");
        json.put("code","0");
        json.put("data",newsList);
        return json;
    }


    @RequestMapping("/delete")
    @ResponseBody
    public JSONObject delete(Integer nId){
        int ret = 0;
        JSONObject json = new JSONObject();
        ret = newsService.deleteByNId(nId);
        if(ret>0){
            json.put("msg","");
            json.put("code","0");
            json.put("data","");
            json.put("count","");
        }else{
            json.put("msg","");
            json.put("code","");
            json.put("data","");
            json.put("count","");
        }
        return json;
    }


    @RequestMapping("/getDetails")
    public String getDetails(@RequestParam("nId") Integer nId, Model model) {

        JSONObject json = new JSONObject();
        News news = newsService.selectByNId(nId);

        if(news != null){
            json.put("msg","");
            json.put("code","0");
            json.put("data",news);
            json.put("count","");
        }else{
            json.put("msg","");
            json.put("code","");
            json.put("data","");
            json.put("count","");
        }
        return json.toJSONString();
    }

    @RequestMapping("/update")//编辑
    @ResponseBody
    public JSONObject update(News news){
        System.out.println(JSON.toJSONString(news));
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();
        ret = newsService.updateByNIdSelective(news);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


    @RequestMapping("/add")
    @ResponseBody
    public JSONObject insert(News news){
        int ret = 0;
        HashMap<String, Object> map = new HashMap<String, Object>();

        ret = newsService.insert(news);
        if(ret>0){
            map.put("status","succ");
        }else{
            map.put("status","error");
        }
        return (JSONObject) JSONObject.toJSON(map);
    }


}
