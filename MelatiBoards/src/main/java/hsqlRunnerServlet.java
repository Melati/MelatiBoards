import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

class RunHSQL implements Runnable {
  
  public void run() {
    String[] args = new String[2];
    args[0] = "-port";
    args[1] = "9124";
    org.hsqldb.Server.main(args);
  }
}

public class hsqlRunnerServlet extends HttpServlet {
  
  public void init(ServletConfig conf) throws ServletException {
    new Thread(new RunHSQL()).start();
    super.init(conf);
  }

}
