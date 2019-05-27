package com.dhu.crawler.weibo;

import com.alibaba.fastjson.JSON;
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
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 微博爬虫启动器
 */
@Component
public class WeiboCrawlerExecutor {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Random random = new Random(System.currentTimeMillis());
    private List<CrawlerStoreDO> store = Lists.newArrayList();
    private List<CrawlerStoreDO> lastStore = Lists.newArrayList();
    private Long lastRunTime = null;
    private Set<String> filterKey = Sets.newHashSet();
    private List<String> queryKey = Lists.newArrayList();
    private Integer interval = 3;
    private String dateFormat = "yyyy-MM-dd-H";
    private Integer filterKeyCount = 5;
    private Integer storeKeyCount = 10;
    private List<String> hosts = Lists.newArrayList();

    @Autowired
    private KeyWordExecutor keyWordExecutor;
    @Autowired
    private WeiBoRepository weiBoRepository;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private WeiboIPUtils ipUtils;
    private String useHost = null;



    @Scheduled(cron = "0 0 0/5 * * ?")
    public void execute() {
        cacheService.refreshConfig();
        logger.info("微博爬虫开始");
        getConfig();//读取配置
        Map<String, String> cookie = CookieUtils.getCookie();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        useHost = null;
        for (String queryWord : queryKey) {
            for (int i = interval; i >= 0; i--) {
                try {
                    String before = LocalDateTime.now().minusHours(i).format(timeFormatter);//搜索的时间间隔参数1h
                    String now = LocalDateTime.now().minusHours(i + 1).format(timeFormatter);
                    doCrawler(cookie, before, now, queryWord, 1);
                    Thread.sleep((random.nextInt(10) + 1) * 1000);
                } catch (Exception e) {
                    logger.error("爬虫异常", e);
                    useHost = null;
                    Map<String, String> temp = CookieUtils.getCookie();
                    if (MapUtils.isNotEmpty(temp)){
                        cookie = temp;
                    }
                }
            }
        }
        //去重过滤保存
        List<CrawlerStoreDO> result = Lists.newArrayList();
        store.forEach(s -> {
            boolean flag = filterKeyWord(s);
            if (flag) {
                for (CrawlerStoreDO c : lastStore) {
                    if (c.getUrl().equals(s.getUrl())) {
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                result.add(s);
                lastStore.add(s);
            }

        });
        storeCrawler(result);
        logger.info("微博爬虫运行完毕");
        cacheService.refreshCache();
    }

    //爬虫实现
    private void doCrawler(Map<String, String> cookie, String now, String before, String queryWord,
                           Integer page) throws Exception {
        String url = "https://s.weibo.com/weibo/%25E9%25A3%258E%25E6%258E%25A7?q=" + queryWord +
                "&scope=ori&suball=1&timescope=custom:" + before + ":" + now + "&Refer=g&page=" + page.toString();
        Connection connection = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/72.0.3626.121 Safari/537.36")
                .cookies(cookie)
                .timeout(10 * 1000);
        //判断IP封锁
        if (StringUtils.isNotEmpty(useHost)) {
            connection = connection.proxy(useHost.split(":")[0], NumberUtils.toInt((useHost.split(":")[1])));
        }
        Document document = connection.get();
        if (document.baseUri().contains("search_need_login")) {
            logger.info("尝试更换ip");
            cookie = CookieUtils.getCookie();
            for (String h : hosts) {
                try {
                    document = connection.url(url).proxy(h.split(":")[0], NumberUtils.toInt((h.split(":")[1])))
                            .cookies(cookie).timeout(10 * 1000).get();
                    if (!document.baseUri().contains("search_need_login")) {
                        logger.info("更换ip成功" + h);
                        useHost = h;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
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
            Element text = content.children().stream()
                    .filter(c -> c.hasAttr("node-type") &&
                            c.attr("node-type").equals("feed_list_content_full"))
                    .findFirst().orElse(null);
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
            logger.info(JSON.toJSONString(storeDO));
        }
        logger.info("微博爬取搜索：" + queryWord + ",共" + page.toString() + "页数据" + store.size() + "条");
        Element pages = document.getElementsByClass("s-scroll").first();
        if (Objects.nonNull(pages) && pages.getElementsByTag("li").size() != page) {
            Thread.sleep((random.nextInt(10) + 1) * 1000);
            doCrawler(cookie, now, before, queryWord, page + 1);
        }
    }

    //解析点赞评论等信息
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

    //关键词过滤
    private boolean filterKeyWord(CrawlerStoreDO storeDO) {
        List<KeyWordDO> keyWords = keyWordExecutor.execute(storeDO, filterKeyCount);
        for (KeyWordDO keyWordDO : keyWords) {
            if (filterKey.contains(keyWordDO.getKeyWord())) {
                storeDO.setKeyWord(keyWordExecutor.execute(storeDO, storeKeyCount).stream()
                        .filter(k -> k.getKeyWord().length() > 1).collect(Collectors.toList()));
                storeDO.setHotDegree(NumberUtils.toInt(storeDO.getFavorite()) * 2 + NumberUtils.toInt(storeDO
                        .getForward()) + NumberUtils.toInt(storeDO.getComment()) + NumberUtils.toInt(storeDO.getLike
                        ()));
                return true;
            }
        }
        return false;
    }

    //保存
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
        queryKey = StringToCollectionUtils.stringToList(cacheService.getConfig("QueryKey"));//搜索关键词
        filterKey = StringToCollectionUtils.stringToSet(cacheService.getConfig("FilterKey"));//过滤关键词
        interval = NumberUtils.toInt(cacheService.getConfig("TimeInterval"), 3);//搜索间隔
        dateFormat = cacheService.getConfig("DateFormat");//时间格式
        filterKeyCount = NumberUtils.toInt(cacheService.getConfig("FilterKeyCount"), 5);//过滤关键词数
        storeKeyCount = NumberUtils.toInt(cacheService.getConfig("StoreKeyCount"), 10);//保存关键词数
        hosts = ipUtils.execute();//预构建IP池
        store = Lists.newArrayList();
        if (lastRunTime == null){//针对小于1h的爬虫缓存上次运行时间
            lastRunTime = System.currentTimeMillis();
        }else if (System.currentTimeMillis() - lastRunTime >= 60 * 60 * 1000){
            lastStore = Lists.newArrayList();
            lastRunTime = System.currentTimeMillis();
        }
    }
}
