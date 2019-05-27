package com.dhu.crawler;

import com.alibaba.fastjson.JSON;
import com.dhu.crawler.toutiao.ToutiaoCrawlerExecutor;
import com.dhu.crawler.weibo.CrawlerStoreDO;
import com.dhu.crawler.weibo.KeyWordExecutor;
import com.dhu.crawler.weibo.WeiboCrawlerExecutor;
import com.dhu.crawler.weibo.WeiboIPUtils;
import com.dhu.model.DO.KeyWordDO;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeiboCrawlerExecutorApplicationTests {

	@Autowired
	WeiboCrawlerExecutor weiboCrawlerExecutor;
	@Autowired
	ToutiaoCrawlerExecutor toutiaoCrawlerExecutor;
	@Autowired
	WeiboIPUtils ipUtils;
	@Autowired
	KeyWordExecutor keyWordExecutor;
	@Test
	public void contextLoads() {
		weiboCrawlerExecutor.execute();
	}

	@Test
	public void test() throws IOException {
//		Response response = HttpUtils.getRes("https://weibo.com/2373665642/HlrLmaecf?refer_flag=1001030103_");
//		List<String> cookies =  response.headers("Set-Cookie");
//		String cookie = "";
//		for (String c : cookies) {
//			if (c.contains("TC-Page-G0")){
//				cookie = c.replace("TC-Page-G0=", "").replace(";Path=/", "");
//			}
//		}
		toutiaoCrawlerExecutor.execute();
	}

	@Test
	public void testPattern() throws IOException {
		System.out.println(ipUtils.execute().toString());
	}

	@Test
	public void testHan(){
		String str = "国际黄金原油分析师提示：美盘开盘，但黄金并未有太大的波动，大家还需要耐心等待，晚间操作上黄金我们依旧看多做多，回调就是做多的机会，但大家 进场一定要做好风控，不知道怎么操作的朋友可以跟上老师实盘实时操作。【 美元指数 大小非农 今日黄金走势分析 黄金原油期货分析 金融投资理财 外汇黄金 恒生指数 黄金喊单 】";
		//		List<String> keywords = TextRankKeyword.getKeywordList(str, 10);
		TfIdfCounter keywordExtractor = new TfIdfCounter();
		List<Map.Entry<String, Double>> keywords = keywordExtractor.getKeywordsWithTfIdf(str,10);
		System.out.println(keywords.toString());
		KeyWordComputer ky = new KeyWordComputer(10);
		List<Keyword> keywordList = ky.computeArticleTfidf(str);
		System.out.println(keywordList);
		CrawlerStoreDO storeDO = new CrawlerStoreDO();
		storeDO.setText(str);
		List<KeyWordDO> wordDOS = keyWordExecutor.execute(storeDO, 10);
		System.out.println(JSON.toJSONString(wordDOS));
//		File file = new File("C:\\Users\\hasee\\Desktop\\毕业设计\\pku98\\199801.txt");
//		StringBuilder result = new StringBuilder();
//		try{
//			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
//			String s = null;
//			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
//				keywordExtractor.add(s);
//			}
//			br.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		List<Map.Entry<String, Double>> keywords2 = keywordExtractor.getKeywordsWithTfIdf(str,10);
//		System.out.println(keywords2.toString());
	}

	@Test
    public void testAnsj(){
	    String str = "深圳市公安部龙华分局";
		System.out.println(HanLP.segment(str));
		System.out.println(NLPTokenizer.segment(str));
		Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
		System.out.println(segment.seg(str));
		System.out.println(IndexTokenizer.segment(str));
    }

    @Test
	public void testDate(){
		LocalDateTime now = LocalDateTime.now().plusHours(10);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd-H");
		System.out.println(now.format(dateTimeFormatter));
	}

	@Test
	public void testEmoji(){
		CrawlerStoreDO storeDO = new CrawlerStoreDO();
		storeDO.setText("简直要被刚刚发生的一起“电信诈骗未遂”气笑了。 骗钱就骗钱吧，能不能用点儿高端的手法，哪怕你伪装成银行工作人员呢？（不过星期天晚上七点哪家法国银行还上班，鬼才信你哦\uD83C" +
				"\uDF1A）这个二傻子直接给我sg的app上发了个100欧的opération让我点accepter，说是为了annuler必须先点确认。 " +
				"我寻思着我用法兴的卡好歹也四五年了，从来没听说过还有这种操作呢？于是直接点了refuser" +
				"，结果这二傻子又给我发了一遍，气急败坏地说你得点确认才行！这就没劲了嘛，抢钱不成急赤白脸了可还行？最气的是软硬兼施软磨硬泡我也坚决不吃这一套的情况下，这二傻子骂了句putin然后挂了电话… " +
				"我一时拿着手机心情复杂，久久不能平息——骂putin的人不应该是我才对吗？？？ #法国# 收起全文d");
		storeDO.setHotDegree(1);
		storeDO.setKeyWord(Lists.newArrayList());
		storeDO.setComment("0");
		storeDO.setForward("0");
		storeDO.setLike("0");
		storeDO.setFavorite("0");
		storeDO.setAuthor("lhf");
		storeDO.setImgUrl("");
		storeDO.setUrl("");
//		crawlerExecutor.storeCrawler(Lists.newArrayList(storeDO));
	}
}

