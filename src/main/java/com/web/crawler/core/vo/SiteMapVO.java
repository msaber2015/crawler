package com.web.crawler.core.vo;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class SiteMapVO {
    Map<String, Set<String>> siteMap;
}
