package sy;

import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


public class HttpDemo1 {
	public static void main(String[] args) {
		
		HttpDemo1 demo1 = new HttpDemo1();
//        System.out.println("Word:A, Resutl:" + demo1.getDictWord("A"));
//        System.out.println("Word:elaborate, Resutl:" + demo1.getDictWord("elaborate"));
//        System.out.println("Word:Test, Resutl:" + demo1.getDictWord("Test"));
//        System.out.println("Word:City, Resutl:" + demo1.getDictWord("City"));
//        System.out.println("Word:Come, Resutl:" + demo1.getDictWord("Come"));
//        System.out.println("Word:in, Resutl:" + demo1.getDictWord("in"));
//        System.out.println("Word:right, Resutl:" + demo1.getDictWord("right"));
//        System.out.println("Word:tel, Resutl:" + demo1.getDictWord("tel"));
		
		String returnBody = HttpUtil.Get("http://www.h2database.com/html/main.html");
		returnBody = Jsoup.parse(returnBody).text();
		System.out.println("returnBody:" + returnBody);
	}
	
	public String getDictWord(String word)
	{
		String returnBody = HttpUtil.Get("http://dict.cn/" + word);
		String returnStr = "";
		Document doc = Jsoup.parse(returnBody);
		Elements els = doc.getElementsByClass("dict-basic-ul");
		for(Element el : els)
		{
			List<Node> nds = el.childNodes();
			for(Node nd : nds)
			{
				String ndStr = nd.toString().trim();
				if(ndStr.length()>0 && ndStr.length()<200)
				{
					returnStr += ndStr + "\r";
				}
			}
		}
		returnStr = returnStr.replaceAll("<[^>]+>", "").replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&amp;","&");
		if(returnStr.trim().length()==0)
		{
			return "Not found";
		}
		else return returnStr;
	}
	
	
	
}

