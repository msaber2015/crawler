package com.web.crawler.core.service.crawler;

import com.web.crawler.core.vo.SiteMapVO;

import java.io.IOException;
import java.net.MalformedURLException;

public interface WebCrawler {

    public SiteMapVO generateSiteMap(String rootSiteUrl);

}
