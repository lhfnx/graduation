# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

from TouTiao.MySqlHelperAgain import MysqlHelper


class ToutiaoPipeline(object):
    def __init__(self):
        self.f = open("theme.txt", 'w', encoding='utf-8')
        self.helper = MysqlHelper('localhost', 3306, 'graduation', 'root', 'lhf19970213', 'utf8')
        self.helper.open()
        self.tableName = "crawler_toutiao"
        sql = """  create table if not exists %s(
                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                  `title` varchar(500) NOT NULL DEFAULT '' COMMENT '文章标题',
                  `connect_url` varchar(1000) NOT NULL DEFAULT '' COMMENT '跳转链接',
                  `datachange_createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                  `key_word` varchar(1000) DEFAULT NULL COMMENT '关键词',
                  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除',
                  `information` varchar(500) DEFAULT NULL COMMENT '信息',
                  `hot_degree` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '热度',
                  `img_url` varchar(1000) DEFAULT NULL COMMENT '图片链接',
                  PRIMARY KEY (`id`)
                ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;""" % (self.tableName)
        self.helper.getCursor().execute(sql)

    def process_item(self, item, spider):
        if item['title'] != None:
            # sql = "insert into toutiao(datetime,platform,announcer,theme,content,url,emotion,attitude_count,repost_count,comments_count,attention,sensitiveWords,information) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')" % \
            #       (item['datetime'], self.platform, item['announcer'], item['theme'],
            #        item['content'], item['url'], None, item['attitude_count'],
            #        item['repost_count'], item['comments_count'], item['attention'], None, item['information'])

            sql = "insert into crawler_toutiao(title,connect_url,datachange_createtime,key_word,is_active,information,hot_degree,img_url) VALUES('%s','%s','%s','%s', '%d','%s','%s','%s')" % \
                  (item['title'],item['connect_url'],item['datetime'],item['keys'],1,  item['information'],  item['hot_degree'], item['img_url'])

            self.helper.cud(sql)
            self.f.write(item['title'] + "\n")
            self.f.flush()
        return item

    def closeFile(self):
        self.helper.close()
        self.f.close()
