package ServletPrograms;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
@WebServlet(name="HandleReceivedSignals", urlPatterns={"/handleReceivedSignals"})
public class HandleReceivedSignalsBuffer extends HttpServlet {

}
