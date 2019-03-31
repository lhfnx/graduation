# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy
import json


class ToutiaoItem(scrapy.Item):
    # define the fields for your item here like:
    # key = scrapy.Field()
    img_url = scrapy.Field()
    title = scrapy.Field()
    comments_count = scrapy.Field()
    connect_url = scrapy.Field()
    content = scrapy.Field()
    datetime = scrapy.Field()
    announcer = scrapy.Field()
    attitude_count = scrapy.Field()
    repost_count = scrapy.Field()
    hot_degree = scrapy.Field()
    information = scrapy.Field()
    keys = scrapy.Field()
    # pass


class InformationItem:
    def __init__(self, author, comment):
        self.author = author
        self.comment = comment

class KeyWordItem:
    def __init__(self,keyWord,nature):
        self.keyWord = keyWord
        self.nature = nature

class UserEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, KeyWordItem):
            return obj.name
        return json.JSONEncoder.default(self, obj)
