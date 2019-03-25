package com.dhu.crawler.weibo;

import com.dhu.common.utils.CookieUtils;
import com.dhu.common.utils.JsonUtils;
import com.dhu.common.utils.StringToCollectionUtils;
import com.dhu.model.DO.InformationDO;
import com.dhu.model.DO.KeyWordDO;
import com.dhu.port.entity.CrawlerForWeiBo;
import com.dhu.port.repository.CacheService;
import com.dhu.port.repository.WeiBoRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CrawlerExecutor {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Random random = new Random(System.currentTimeMillis());
    private List<CrawlerStoreDO> store = Lists.newArrayList();
    private Set<String> filterKey = Sets.newHashSet();
    private List<String> queryKey = Lists.newArrayList();
    private Integer interval = 3;
    private String dateFormat = "YYYY-MM-dd-HH";

    @Autowired
    private KeyWordExecutor keyWordExecutor;
    @Autowired
    private WeiBoRepository weiBoRepository;
    @Autowired
    private CacheService cacheService;

    @Scheduled(cron = "0 0 0/5 * * ?")
    public void execute() {
        try {
            cacheService.refreshConfig();
            logger.info("微博爬虫开始");
            getConfig();
            Map<String, String> cookie = CookieUtils.getCookie();
            store = Lists.newArrayList();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            String now = LocalDateTime.now().format(timeFormatter);
            String before = LocalDateTime.now().minusHours(interval).format(timeFormatter);
            for (String queryWord : queryKey) {
                doCrawler(cookie, now, before, queryWord, 1);
            }
            List<CrawlerStoreDO> result = store.stream().filter(this::filterKeyWord).collect(Collectors.toList());
            storeCrawler(result);
            logger.info("微博爬虫运行完毕");
        } catch (Exception e) {
            logger.error("爬虫异常", e);
        }
        cacheService.refreshCache();
    }

    private void doCrawler(Map<String, String> cookie, String now, String before, String queryWord,
                           Integer page) throws Exception {
        Document document = Jsoup.connect("https://s.weibo" +
                ".com/weibo/%25E9%25A3%258E%25E6%258E%25A7?q=" + queryWord +
                "&scope=ori&suball=1&timescope=custom:" + before + ":" + now + "&Refer=g&page=" + page.toString())
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/72.0.3626.121 Safari/537.36")
                .cookies(cookie)
                .get();
        Elements elements = document.getElementsByClass("card-wrap");
        if (elements.size() == 0) {
            logger.error("爬虫未爬取到数据");
            return;
        }
        for (Element e : elements) {
            CrawlerStoreDO storeDO = new CrawlerStoreDO();
            Element all = e.children().select(".card").first().children().select(".card-feed").first();
            if (Objects.isNull(all)) {
                continue;
            }
            //设置图片链接
            Element avator = all.children().select(".avator").first();
            if (Objects.isNull(avator)) {
                continue;
            }
            setImg(avator, storeDO);

            Element content = all.children().select(".content").first();
            if (Objects.isNull(content)) {
                continue;
            }
            //设置作者
            Element info = content.select(".info").first().children().stream().filter(c -> !c.children().isEmpty()
                    && !c.hasAttr("class")).findFirst().orElse(null);
            if (Objects.isNull(info)) {
                continue;
            }
            //非认证用户过滤
            if (info.getElementsByClass("icon-vip").size() == 0) {
                continue;
            }
            Element name = info.getElementsByAttribute("nick-name").first();
            storeDO.setAuthor(name.attr("nick-name"));

            //设置内容
            Element text = content.children().stream().filter(c -> c.hasAttr("node-type") && c.attr
                    ("node-type").equals("feed_list_content_full")).findFirst().orElse(null);
            if (Objects.isNull(text)) {
                continue;
            }
            storeDO.setText(text.text());
            //设置跳转链接
            Element summary = content.children().stream().filter(c -> c.hasAttr("node-type") && c.attr
                    ("node-type").equals("feed_list_content")).findFirst().orElse(null);
            if (Objects.isNull(summary)) {
                continue;
            }
            setUrl(summary, storeDO);

            //设置点赞评论等信息
            Element act = e.children().select(".card").first().children().select(".card-act").first();
            if (Objects.isNull(act)) {
                continue;
            }
            setInformation(act, storeDO);

            store.add(storeDO);
        }
        logger.info("微博爬取搜索：" + queryWord + ",共" + page.toString() + "页数据" + store.size() + "条");
        Element pages = document.getElementsByClass("s-scroll").first();
        if (Objects.nonNull(pages) && pages.getElementsByTag("li").size() != page) {
            Thread.sleep((random.nextInt(10) + 1) * 1000);
            doCrawler(cookie, now, before, queryWord, page + 1);
        }
    }

    private void setInformation(Element act, CrawlerStoreDO storeDO) {
        Element ul = act.getElementsByTag("ul").first();
        if (Objects.isNull(ul)) {
            return;
        }
        Elements liList = ul.getElementsByTag("li");
        for (Element l : liList) {
            String text = l.text();
            if (text.contains("收藏")) {
                storeDO.setFavorite(String.valueOf(NumberUtils.toInt(StringUtils.trimToNull(text.replace("收藏", "")),
                        0)));
                continue;
            }
            if (text.contains("转发")) {
                storeDO.setForward(String.valueOf(NumberUtils.toInt(StringUtils.trimToNull(text.replace("转发", "")),
                        0)));
                continue;
            }
            if (text.contains("评论")) {
                storeDO.setComment(String.valueOf(NumberUtils.toInt(StringUtils.trimToNull(text.replace("评论", "")),
                        0)));
                continue;
            }
            storeDO.setLike(String.valueOf(NumberUtils.toInt(StringUtils.trimToNull(text), 0)));
        }
    }

    private void setImg(Element avator, CrawlerStoreDO storeDO) {
        Element imgLink = avator.getElementsByTag("a").first();
        if (Objects.isNull(imgLink)) {
            return;
        }
        Element img = imgLink.getElementsByTag("img").first();
        if (Objects.isNull(img)) {
            return;
        }
        storeDO.setImgUrl("https:" + img.attr("src"));
    }

    private void setUrl(Element summary, CrawlerStoreDO storeDO) {
        Element url = summary.children().stream().filter(c -> c.tagName().equals("a") && c.hasAttr("action-type") &&
                c.attr("action-type").equals("fl_unfold")).findFirst().orElse(null);
        if (Objects.isNull(url)) {
            return;
        }
        storeDO.setUrl("https:" + url.attr("href"));
    }

    private boolean filterKeyWord(CrawlerStoreDO storeDO) {
        List<KeyWordDO> keyWords = keyWordExecutor.execute(storeDO);
        for (KeyWordDO keyWordDO : keyWords) {
            if (filterKey.contains(keyWordDO.getKeyWord())) {
                storeDO.setKeyWord(keyWords);
                storeDO.setHotDegree(NumberUtils.toInt(storeDO.getFavorite()) * 2 + NumberUtils.toInt(storeDO
                        .getForward()) + NumberUtils.toInt(storeDO.getComment()) + NumberUtils.toInt(storeDO.getLike
                        ()));
                return true;
            }
        }
        return false;
    }

    private void storeCrawler(List<CrawlerStoreDO> crawlers) {
        if (CollectionUtils.isEmpty(crawlers)) {
            logger.warn("爬虫最终结果无数据存入数据库");
            return;
        }
        List<CrawlerForWeiBo> weiBoList = crawlers.stream().map(this::convert2CrawlerForWeiBo).collect(Collectors
                .toList());
        int cnt = weiBoRepository.batchInsertCrawler(weiBoList);
        logger.info("爬虫存储数据共" + cnt + "条");
    }

    private CrawlerForWeiBo convert2CrawlerForWeiBo(CrawlerStoreDO storeDO) {
        CrawlerForWeiBo weiBo = new CrawlerForWeiBo();
        weiBo.setSummary(storeDO.getText().substring(0, 100) + "...");
        weiBo.setConnectUrl(storeDO.getUrl());
        weiBo.setKeyWord(JsonUtils.serialize(storeDO.getKeyWord()));
        InformationDO information = new InformationDO();
        information.setAuthor(storeDO.getAuthor());
        information.setComment(storeDO.getComment());
        information.setFavorite(storeDO.getFavorite());
        information.setForward(storeDO.getForward());
        information.setLike(storeDO.getLike());
        weiBo.setInformation(JsonUtils.serialize(information));
        weiBo.setHotDegree(storeDO.getHotDegree());
        weiBo.setImgUrl(storeDO.getImgUrl());
        return weiBo;
    }

    private void getConfig() {
        queryKey = StringToCollectionUtils.stringToList(cacheService.getConfig("QueryKey"));
        filterKey = StringToCollectionUtils.stringToSet(cacheService.getConfig("FilterKey"));
        interval = NumberUtils.toInt(cacheService.getConfig("TimeInterval"), 3);
        dateFormat = cacheService.getConfig("DateFormat");
    }
}
