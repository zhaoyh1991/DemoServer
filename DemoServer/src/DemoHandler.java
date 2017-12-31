import java.io.PrintWriter;

/**
 * Created by zhaoyh on 2017/12/31.
 */
public class DemoHandler {
    @Mapping(reqUrl = "/2017")
    public void Goodbye(PrintWriter out ,@Field("date") String date){
        String goodbye="goodbye 2017 \n" +
                "something lose,but also get some friends and  many new books " +
                "i learn a lot from them\n" +
                "i am happy to find that i have the possibility" +
                "to be better.\n" +
                "keep learning keep moving keep coding\n" +
                "write by zhaoyh "+date;
        ResponseUtil.response(out,"200","ok",goodbye);
    }
    @Mapping(reqUrl = "/2018")
    public void welcome(PrintWriter out){
        String welcome="welcome 2018 \n" +
                "though i am a little worried about that i am older than last year but it's lucky i am still young .....laughter\n" +
                "in the new year i will  keep reading to understand some difficult questions \n" +
                "change some bad habits to be more patient and friendly to family and friends\n" +
                "work and learn more efficiently \n" +
                "of course at last i wish i can find the right one in the new year ....laughter";
        ResponseUtil.response(out,"200","ok",welcome);
    }


}
