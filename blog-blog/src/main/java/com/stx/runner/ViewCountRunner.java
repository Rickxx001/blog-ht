package com.stx.runner;

import com.stx.constants.SystemConstants;
import com.stx.domain.entity.Article;
import com.stx.mapper.ArticleDao;
import com.stx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

//开启时 将浏览量存入redis
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleDao articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息  id  viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()
                ));
        //存储到redis中
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW,viewCountMap);
    }
}
