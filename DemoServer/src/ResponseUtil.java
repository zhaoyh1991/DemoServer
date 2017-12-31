import java.io.PrintWriter;

/**
 * Created by zhaoyh on 2017/12/31.
 */
public class ResponseUtil {
    //lack of headers and many thing so just for fun
    public static void response(PrintWriter out, String code,String codeDesc,String message){
        out.write("HTTP/1.1 "+code+" "+codeDesc+" \r\n");
        out.write("\r\n");
        out.write(message+" \r\n");
        out.close();
    }
}
