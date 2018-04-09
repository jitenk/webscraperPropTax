
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebScraperPropTax {

    public static void main(String[] args)  {

        final WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getCookieManager().clearCookies();

        /*String[] allIds= {
                    "R453157",
                    "R453319",
                    "R453254",
                    "R453312",
                    "R453318",
                    "R453314",
                    "R453317",
                    "R453274",
                    "R453260",
                    "R453283",
                    "R453275",
                    "R453268",
                    "R453321",
                    "R453253",
                    "R453306",
                    "R453269",
                    "R453257",
                    "R453264",
                    "R453293",
                    "R453303",
                    "R453298",
                    "R453278",
                    "R453250",
                    "R453310",
                    "R453285",
                    "R453270",
                    "R453281",
                    "R453265",
                    "R453261",
                    "R453266",
                    "R453313",
                    "R453255",
                    "R453252",
                    "R453302",
                    "R453287",
                    "R453292",
                    "R453309",
                    "R453284",
                    "R453277",
                    "R453262",
                    "R453280",
                    "R453258",
                    "R453251",
                    "R453288",
                    "R453267",
                    "R453295",
                    "R453279",
                    "R453256",
                    "R453301",
                    "R453291",
                    "R453299",
                    "R453286",
                    "R453315",
                    "R453272",
                    "R453273",
                    "R453276",
                    "R453316",
                    "R453297",
                    "R453259",
                    "R453304",
                    "R453307",
                    "R453289",
                    "R453271",
                    "R453263",
                    "R453308",
                    "R453320",
                    "R453296",
                    "R453282",
                    "R453300",
                    "R453311",
                    "R453290",
                    "R453305",
                    "R453294"
        };*/

        String[] allIds = {
                "168581",
                "168484",
                "164873",
                "168577",
                "168513",
                "168493",
                "168482",
                "164868",
                "168494",
                "168495",
                "168496",
                "168497",
                "168498",
                "168499",
                "168500",
                "168501",
                "168502",
                "168503",
                "168504",
                "168505",
                "168506",
                "360813",
                "360814",
                "168511",
                "164869",
                "164866",
                "168489",
                "168490",
                "168580",
                "164863",
                "164778",
                "168579",
                "168512",
                "164867",
                "168584",
                "168583",
                "164933",
                "168509",
                "168582",
                "168487",
                "168510",
                "168576",
                "168488",
                "164934",
                "168486",
                "164935",
                "168483",
                "168585",
                "164779",
                "168485",
                "164864",
                "168481",
                "164872",
                "164870",
                "168578",
                "164865",
                "168492",
                "168491",
                "164871",
                "168575"
        };

       /* String[] allIds= {
                "164872",
        };*/

        List<String> allIdList = Arrays.asList(allIds);
        List<String> finalList = new ArrayList<String>();

        for (String s : allIdList) {
            //lookupEachWCAD(webClient, s, finalList);
            lookupEachTCAD(webClient,s,finalList);
        }

        webClient.close();

        for (String s : finalList) {
            System.out.println(s);
        }
    }

    public static  void lookupEachWCAD(WebClient webClient, String id, List<String> finalList){
        webClient.getOptions().setJavaScriptEnabled(false);
         HtmlPage page = null;
         String totalMarketValue = null;
         String sqFeet = null;
        try {
            page = webClient.getPage("http://search.wcad.org/Property-Detail?PropertyQuickRefID="+id);
        } catch (IOException e) {

        }

        List<?> submissionString1 = page.getByXPath("//tr/td[@id='dnn_ctr1460_View_tdVITotalMV']/text()");
        if (submissionString1 != null && submissionString1.size() > 0)
            totalMarketValue = submissionString1.get(0).toString();


        List<?> submissionString = page.getByXPath("//tr/td[@class='CIFixedWidthCell improvementsFieldData']/text()");
        if (submissionString != null && submissionString.size() > 3)
            sqFeet = submissionString.get(2).toString();

        finalList.add(id+"\t"+sqFeet+"\t"+totalMarketValue);
    }

    public static  void lookupEachTCAD(WebClient webClient, String id, List<String> finalList){
        HtmlPage page = null;
        String withCurrentException = null;
        String withOutCurrentException = null;
        String sqfeet = null;
        String exemption = null;
        try {
            page = webClient.getPage("http://propaccess.traviscad.org/clientdb/?cid=1");
            page = webClient.getPage("http://propaccess.traviscad.org/ClientDB/Property.aspx?prop_id="+id);
        } catch (IOException e) {

        }

        if (page.getByXPath("//table[@summary='Property Details']") != null && page.getByXPath("//table[@summary='Property Details']").size() > 0){
            final HtmlTable table = (HtmlTable) page.getByXPath("//table[@summary='Property Details']").get(0);
            if (table != null)
                exemption = table.getRow(17).getCell(3).getTextContent();
        }

        if (page.getByXPath("//table[@class='improvements']") != null && page.getByXPath("//table[@class='improvements']").size() > 0){
            final HtmlTable table = (HtmlTable) page.getByXPath("//table[@class='improvements']").get(0);
            if (table != null)
                sqfeet = table.getRow(0).getCell(5).getTextContent();
        }

        if (page.getByXPath("//table[@class='tableData']") != null && page.getByXPath("//table[@class='tableData']").size() > 0){
            final HtmlTable table1 = (HtmlTable) page.getByXPath("//table[@class='tableData']").get(0);

            if (table1 != null) {
                withCurrentException = table1.getRow(8).getCell(5).getTextContent();
                withOutCurrentException = table1.getRow(9).getCell(5).getTextContent();
            }
        }

        finalList.add(id+"\t"+sqfeet+"\t"+withCurrentException+"\t"+withOutCurrentException+"\t"+exemption);

    }
}
