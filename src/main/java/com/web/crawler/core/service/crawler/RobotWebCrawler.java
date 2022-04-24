package com.web.crawler.core.service.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.crawler.core.vo.SiteMapVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Service
public class RobotWebCrawler implements WebCrawler {

    @Value("${testRootSiteUrl}")
    private String testRootSiteUrl;

    @Override
    public SiteMapVO generateSiteMap(String rootSiteUrl) {

        SiteMapVO siteMapVO = new SiteMapVO();
        String currentPage;
        Set<String> visitedLinks = new HashSet<>();
        Stack<String> unvisitedPages = new Stack<>();
        unvisitedPages.push(rootSiteUrl);
        Set<String> links;
        Map<String, Set<String>> siteMap = new LinkedHashMap<>();

        // Get domain to match only urls contains that Domain
        String domain = rootSiteUrl.replaceFirst("//", "&&").split("/")[0].replaceFirst("&&", "//");

        // visit pages of unvisited stack
        while (!unvisitedPages.empty()) {
            currentPage = unvisitedPages.pop();
            System.out.println("CurrentPage :- " + currentPage);
            if (!visitedLinks.contains(currentPage)) {

                // Add Url to set to not visit it again
                visitedLinks.add(currentPage);
                // Get Urls in current page
                links = findLinks(currentPage, domain);
                // Add New Urls to unvisited list to visit them again
                links.forEach((e) -> {
                    if (!unvisitedPages.contains(e) && !visitedLinks.contains(e)) {
                        unvisitedPages.add(e);
                    }
                });

                // Put children of current page
                siteMap.put(currentPage, links);

            }
        }

        siteMapVO.setSiteMap(siteMap);
        createJsonFileWithSiteMap(siteMapVO);
        return siteMapVO;
    }

    /*
     * Desk # get all urls of specific domain from specific page
     * Parms [ url# url of page that you want to crawl through , domain# domain that you want to filter urls by  ]
     */
    private static Set<String> findLinks(String url, String domain) {

        Set<String> links = new HashSet<>();

        try {
            String currentLink;

            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("a[href]");
            for (Element element : elements) {
                currentLink = element.attr("href");
                if (!currentLink.startsWith("http") && currentLink.startsWith("/") && currentLink.length() > 1) {
                    currentLink = domain + currentLink;
                }
                if (currentLink.startsWith("http") && currentLink.startsWith(domain)
                        && !currentLink.contains(".png") && !currentLink.contains(".pdf")) {
                    if (currentLink.substring(currentLink.length() - 1, currentLink.length()).equals("/")) {
                        currentLink = currentLink.substring(0, currentLink.length() - 1);
                    }
                    links.add(currentLink);
                }

            }
        } catch (Exception e) {
            System.err.println(e);
        }

        return links;
    }

    // Add SiteMap set in json file
    private void createJsonFileWithSiteMap(SiteMapVO siteMapVO) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("siteMap.json"), siteMapVO);
            System.out.println("Site Map File Created Successfully In The same Path With Name :- siteMap.json");
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    @PostConstruct
    public void runCrawler() {
        generateSiteMap(testRootSiteUrl);
    }

}
