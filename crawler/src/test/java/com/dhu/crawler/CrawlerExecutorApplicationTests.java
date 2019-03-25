package com.dhu.crawler;

import com.alibaba.fastjson.JSONObject;
import com.dhu.common.utils.CookieUtils;
import com.dhu.common.utils.HttpUtils;
import com.dhu.crawler.weibo.CrawlerExecutor;
import com.dhu.crawler.weibo.CrawlerStoreDO;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.summary.TextRankKeyword;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import okhttp3.Response;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.IndexAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerExecutorApplicationTests {

	@Autowired
	CrawlerExecutor crawlerExecutor;
	@Test
	public void contextLoads() {
		crawlerExecutor.execute();
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
	}

	@Test
	public void testPattern(){
		String reg = "\\D+(\\d+)$";
		String s = " 转发 2231";
		Pattern p = Pattern.compile(reg);
		Matcher r = p.matcher(s);
		if (r.find()) {
			System.out.println(r.group(1));
		}
	}

	@Test
	public void testHan(){
		String str = "【PeckShield: EOS竞猜游戏EOSVagas遭交易阻塞攻击】今天下午16:46-17:18之间，PeckShield安全盾风控平台DAppShield监测到黑客向EOS" +
				"竞猜类游戏EOSVagas发起连续攻击，并已成功获利。PeckShield安全人员分析发现黑客采用的攻击手段是交易阻塞攻击（CVE-2019-6199" +
				"），而且该黑客帐号异常活跃，刚刚攻击过竞猜类游戏EOS Cube，其相关账号已被标记为高危。 " +
				"PeckShield安全人员在此提醒，开发者应在合约上线前做好安全测试，特别注意加强风控，阻止高危帐号带来的威胁，必要时可寻求第三方安全公司协助，帮助其完成合约上线前攻击测试及基础安全防御部署。 " +
				"\uE627区块链\uE627比特币 收起全文d\n";

		List<String> keywords = TextRankKeyword.getKeywordList(str, 10);
		System.out.println(keywords.toString());
	}

	@Test
    public void testAnsj(){
	    String str = "深圳市公安部龙华分局";
		System.out.println(ToAnalysis.parse(str));
		System.out.println(BaseAnalysis.parse(str));
		System.out.println(NlpAnalysis.parse(str));
		System.out.println(IndexAnalysis.parse(str));
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

