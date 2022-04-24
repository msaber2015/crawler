package com.web.crawler.presentation.httpservices;

import com.web.crawler.core.service.crawler.WebCrawler;
import com.web.crawler.core.vo.SiteMapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
public class CrawlerController {

    @Autowired
    private WebCrawler webCrawler;

    @GetMapping(value = "/map")
    public SiteMapVO generateSiteMap(@RequestParam("url") String rootSiteUrl) {
        return webCrawler.generateSiteMap(rootSiteUrl);
    }
}
