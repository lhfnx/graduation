# -*- coding: utf-8 -*-
import scrapy
import json
from TouTiao.items import ToutiaoItem, InformationItem,KeyWordItem
from urllib.parse import urlencode
import re
import urllib.request
from lxml import etree
from jieba import analyse,posseg


def get_page_index(start_url, offset, keyword):
    data = {
        'aid': '24',
        'app_name': 'web_search',
        'offset': offset,
        'format': 'json',
        'keyword': keyword,
        'autoload': 'true',
        'count': '20',
        'en_qc': '1',
        'cur_tab': '1',
        'from': 'search_tab',
        'pd': 'synthesis',
    }
    url = start_url + urlencode(data)
    return url


class ToutiaoSpider(scrapy.Spider):
    name = 'toutiao'
    allowed_domains = ['toutiao.com']
    keywords = ['电信诈骗','金融诈骗','贷款诈骗','交友诈骗','兼职诈骗', '金融风控','银行风控','智能风控']
    cont = 0
    keyword = keywords[0]
    offset = 0  # 偏移量
    start_url = 'https://www.toutiao.com/search_content/?'
    url = get_page_index(start_url, offset, keyword)
    start_urls = [url]

    def parse(self, response):  # 解析页面
        print('关键词：', self.keyword)
        dic = json.loads(response.text)  # 将response返回的json数转换成字典数据
        if dic and 'data' in dic.keys():  # dic数据中有data数据段
            print('数据转换成功')
            for node in dic.get('data'):  # data中的每个结点object
                item = ToutiaoItem()
                try:
                    imgUrl = node.get("image_url")
                    if not imgUrl:
                        item['img_url'] = "img/toutiao.jpg"
                    else:
                        item['img_url'] = "http:" + node.get("image_url")
                    item['title'] = node.get("title")
                    item['comments_count'] = node.get("comments_count")
                    item['datetime'] = node.get("datetime")
                    item['announcer'] = node.get("media_name")
                    url = node.get("item_source_url")
                    url = "https://www.toutiao.com" + url
                    item['connect_url'] = url
                    item['attitude_count'] = 0
                    item['repost_count'] = 0
                    item['hot_degree'] = int(item['repost_count']) + int(item['attitude_count']) + \
                                         int(item['comments_count'])
                    # 获取文章内容
                    headers = {
                        "User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36"}
                    request = urllib.request.Request(url, headers=headers)
                    response = urllib.request.urlopen(request)
                    html = str(response.read(), 'utf-8')
                    content = re.findall(r"content: '(.+);',", str(html))
                    inf = InformationItem(node.get("media_name"), node.get("comments_count"))
                    overdict = inf.__dict__
                    item['information'] = json.dumps(overdict, ensure_ascii=False)

                    if len(content) == 0:  # 若获取的页面为静态
                        s = etree.HTML(html)
                        content = s.xpath("//p/text()")
                        sss = ""
                        for con in content:
                            sss += con.strip()
                    else:  # 若获取的页面为动态
                        content = re.findall(u"[\u4E00-\u9FA5]|[\uFE30-\uFFA0]+", content[0])
                        sss = ""
                        for con in content:
                            sss += con.strip()
                    item['content'] = sss
                    textrank = analyse.textrank #关键词抽取
                    keyword_list = textrank(sss)
                    keywordItems = []
                    for ky in keyword_list:
                        words = posseg.cut(ky)
                        for w in words:
                            keywordItem = KeyWordItem(w.word,w.flag)
                            keywordItems.append(keywordItem.__dict__)
                    myky = KeyWordItem(self.keyword,"ky")
                    keywordItems.append(myky.__dict__) #搜索关键词添加进关键词列表
                    item['keys'] = json.dumps(keywordItems,ensure_ascii=False)
                    yield item
                except:
                    pass
        if self.offset < 300:
            self.offset += 20
            url = get_page_index(self.start_url, self.offset, self.keyword)
            yield scrapy.Request(url, callback=self.parse)  # 迭代调用parse方法
        if self.offset >= 300:
            if (self.cont < len(self.keywords)-1):
                self.cont = self.cont + 1
                self.keyword = self.keywords[self.cont]
                self.offset = 0
                url = get_page_index(self.start_url, self.offset, self.keyword)
                yield scrapy.Request(url, callback=self.parse)  # 迭代调用parse方法

        # print(self.keyword, '头条')
        # print('-------------------------------------------------------------------')
        # print(self.keyword)
        # dic = json.loads(response.text)
        # if dic and 'data' in dic.keys():
        #     for node in dic.get('data'):
        #         item = ToutiaoItem()
        #         try:
        #             item['key'] = self.keyword
        #             item['img'] = "http:"+node.get("image_url")
        #             item['theme'] = node.get("title")
        #             item['comments_count'] = node.get("comments_count")
        #             item['datetime'] = node.get("datetime")
        #             item['announcer'] = node.get("media_name")
        #             url = node.get("item_source_url")
        #             url = "https://www.toutiao.com" + url
        #             item['url'] = url
        #             item['attitude_count'] = 0
        #             item['repost_count'] = 0
        #             item['attention'] = int(item['repost_count']) + int(item['attitude_count']) + \
        #                                 int(item['comments_count'])
        #             item['platform'] = "今日头条"
        #             # 获取文章内容
        #             headers = {
        #                 "User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36"}
        #             request = urllib.request.Request(url, headers=headers)
        #             response = urllib.request.urlopen(request)
        #             html = str(response.read(), 'utf-8')
        #             content = re.findall(r"content: '(.+);',", str(html))
        #             inf = InformationItem(node.get("media_name"), node.get("comments_count"))
        #             overdict = inf.__dict__
        #             item['information'] = json.dumps(overdict, ensure_ascii=False)
        #
        #             if len(content) == 0:  # 若获取的页面为静态
        #                 s = etree.HTML(html)
        #                 content = s.xpath("//p/text()")
        #                 sss = ""
        #                 for con in content:
        #                     sss += con.strip()
        #             else:  # 若获取的页面为动态
        #                 content = re.findall(u"[\u4E00-\u9FA5]|[\uFE30-\uFFA0]+", content[0])
        #                 sss = ""
        #                 for con in content:
        #                     sss += con.strip()
        #             item['content'] = sss
        #             yield item
        #         except:
        #             pass
        # except RequestException:
        #     print('请求索引页出错')
        #     return None

        # if self.offset < 240:
        #     self.offset += 20
        #     url = get_page_index(self.start_url, self.offset, self.keyword)
        #     yield scrapy.Request(url, callback=self.parse)
